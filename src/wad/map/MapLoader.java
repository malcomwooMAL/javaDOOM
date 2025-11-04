package wad.map;

import wad.Lump;
import wad.WadLoader;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class MapLoader {

    public static MapData loadMap(WadLoader wadLoader, String mapName) throws IOException {
        List<Lump> lumps = wadLoader.getLumps();
        int mapIndex = findLumpIndex(lumps, mapName);
        if (mapIndex == -1) {
            throw new IOException("Mapa '" + mapName + "' não encontrado no WAD.");
        }

        // Os lumps de dados do mapa vêm logo após o lump marcador, em uma ordem específica.
        // A ordem é: THINGS, LINEDEFS, SIDEDEFS, VERTEXES, ...
        List<Linedef> linedefs = loadLinedefs(wadLoader, lumps.get(mapIndex + 2));
        List<Vertex> vertices = loadVertices(wadLoader, lumps.get(mapIndex + 4));

        return new MapData(vertices, linedefs);
    }

    private static int findLumpIndex(List<Lump> lumps, String name) {
        for (int i = 0; i < lumps.size(); i++) {
            if (lumps.get(i).name.equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
    }

    private static List<Vertex> loadVertices(WadLoader wadLoader, Lump vertexLump) throws IOException {
        if (!vertexLump.name.equalsIgnoreCase("VERTEXES")) {
            throw new IOException("Esperava o lump VERTEXES, mas encontrou: " + vertexLump.name);
        }

        List<Vertex> vertices = new ArrayList<>();
        try (RandomAccessFile file = new RandomAccessFile(wadLoader.getWadFile(), "r");
             FileChannel channel = file.getChannel()) {

            ByteBuffer buffer = ByteBuffer.allocate(vertexLump.size);
            buffer.order(ByteOrder.LITTLE_ENDIAN);

            channel.position(vertexLump.filepos);
            channel.read(buffer);
            buffer.flip();

            while (buffer.hasRemaining()) {
                // Em C, vertex_t usa short, não int.
                short x = buffer.getShort();
                short y = buffer.getShort();
                vertices.add(new Vertex(x, y));
            }
        }
        return vertices;
    }

    private static List<Linedef> loadLinedefs(WadLoader wadLoader, Lump linedefLump) throws IOException {
        if (!linedefLump.name.equalsIgnoreCase("LINEDEFS")) {
            throw new IOException("Esperava o lump LINEDEFS, mas encontrou: " + linedefLump.name);
        }

        List<Linedef> linedefs = new ArrayList<>();
        try (RandomAccessFile file = new RandomAccessFile(wadLoader.getWadFile(), "r");
             FileChannel channel = file.getChannel()) {

            ByteBuffer buffer = ByteBuffer.allocate(linedefLump.size);
            buffer.order(ByteOrder.LITTLE_ENDIAN);

            channel.position(linedefLump.filepos);
            channel.read(buffer);
            buffer.flip();

            while (buffer.hasRemaining()) {
                short startVertex = buffer.getShort();
                short endVertex = buffer.getShort();
                short flags = buffer.getShort();
                short specialType = buffer.getShort();
                short sectorTag = buffer.getShort();
                short frontSidedef = buffer.getShort();
                short backSidedef = buffer.getShort();
                linedefs.add(new Linedef(startVertex, endVertex, flags, specialType, sectorTag, frontSidedef, backSidedef));
            }
        }
        return linedefs;
    }
}
