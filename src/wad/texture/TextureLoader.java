package wad.texture;

import wad.Lump;
import wad.WadLoader;

import java.awt.Color;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextureLoader {

    private final WadLoader wadLoader;
    private final List<Color> palette = new ArrayList<>();
    private final List<String> patchNames = new ArrayList<>();
    private final Map<String, Texture> textures = new HashMap<>();

    public TextureLoader(WadLoader wadLoader) {
        this.wadLoader = wadLoader;
    }

    public void load() throws IOException {
        loadPalette();
        loadPatchNames();
        loadTextures();
    }

    private void loadPalette() throws IOException {
        Lump paletteLump = findLump("PLAYPAL");
        try (RandomAccessFile file = new RandomAccessFile(wadLoader.getWadFile(), "r");
             FileChannel channel = file.getChannel()) {

            ByteBuffer buffer = readLump(channel, paletteLump);

            while (buffer.hasRemaining()) {
                int r = buffer.get() & 0xFF;
                int g = buffer.get() & 0xFF;
                int b = buffer.get() & 0xFF;
                palette.add(new Color(r, g, b));
            }
        }
    }

    private void loadPatchNames() throws IOException {
        Lump pnamesLump = findLump("PNAMES");
        try (RandomAccessFile file = new RandomAccessFile(wadLoader.getWadFile(), "r");
             FileChannel channel = file.getChannel()) {

            ByteBuffer buffer = readLump(channel, pnamesLump);
            int numPatches = buffer.getInt();

            for (int i = 0; i < numPatches; i++) {
                byte[] nameBytes = new byte[8];
                buffer.get(nameBytes);
                patchNames.add(new String(nameBytes, StandardCharsets.US_ASCII).trim());
            }
        }
    }

    private void loadTextures() throws IOException {
        Lump texture1Lump = findLump("TEXTURE1");
        try (RandomAccessFile file = new RandomAccessFile(wadLoader.getWadFile(), "r");
             FileChannel channel = file.getChannel()) {

            ByteBuffer buffer = readLump(channel, texture1Lump);
            int numTextures = buffer.getInt();

            for (int i = 0; i < numTextures; i++) {
                buffer.position(4 + i * 4); // Pula para o offset da textura
                int textureOffset = buffer.getInt();
                buffer.position(textureOffset);

                String name = readString(buffer, 8);
                buffer.getShort(); // masked, not used
                buffer.getShort(); // also masked
                short width = buffer.getShort();
                short height = buffer.getShort();
                buffer.getInt(); // columndirectory, not used
                short patchCount = buffer.getShort();

                List<MapPatch> mapPatches = new ArrayList<>();
                for (int j = 0; j < patchCount; j++) {
                    short originX = buffer.getShort();
                    short originY = buffer.getShort();
                    short patchIndex = buffer.getShort();
                    buffer.getShort(); // stepdir, not used
                    buffer.getShort(); // colormap, not used
                    mapPatches.add(new MapPatch(originX, originY, patchIndex));
                }
                textures.put(name, new Texture(name, width, height, mapPatches));
            }
        }
    }

    public java.awt.image.BufferedImage getTextureAsImage(String textureName) throws IOException {
        Texture texture = textures.get(textureName.toUpperCase());
        if (texture == null) {
            throw new IOException("Textura '" + textureName + "' não encontrada.");
        }

        java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(texture.width, texture.height, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g2d = image.createGraphics();

        for (MapPatch mapPatch : texture.patches) {
            String patchName = patchNames.get(mapPatch.patchIndex);
            Patch patch = loadPatch(patchName);
            g2d.drawImage(patch.image, mapPatch.originX, mapPatch.originY, null);
        }

        g2d.dispose();
        return image;
    }

    private Patch loadPatch(String patchName) throws IOException {
        Lump patchLump = findLump(patchName);
        try (RandomAccessFile file = new RandomAccessFile(wadLoader.getWadFile(), "r");
             FileChannel channel = file.getChannel()) {

            ByteBuffer buffer = readLump(channel, patchLump);
            short width = buffer.getShort();
            short height = buffer.getShort();
            buffer.getShort(); // leftoffset, not used for textures
            buffer.getShort(); // topoffset, not used for textures

            java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_INT_ARGB);

            int[] columnOffsets = new int[width];
            for (int i = 0; i < width; i++) {
                columnOffsets[i] = buffer.getInt();
            }

            for (int x = 0; x < width; x++) {
                buffer.position(columnOffsets[x]);
                while (true) {
                    short topDelta = (short) (buffer.get() & 0xFF);
                    if (topDelta == 0xFF) break;

                    short length = (short) (buffer.get() & 0xFF);
                    buffer.get(); // padding

                    for (int y = 0; y < length; y++) {
                        int paletteIndex = buffer.get() & 0xFF;
                        Color color = palette.get(paletteIndex);
                        image.setRGB(x, topDelta + y, color.getRGB());
                    }
                    buffer.get(); // padding
                }
            }
            return new Patch(image);
        }
    }

    private String readString(ByteBuffer buffer, int length) {
        byte[] bytes = new byte[length];
        buffer.get(bytes);
        return new String(bytes, StandardCharsets.US_ASCII).trim();
    }

    private Lump findLump(String name) throws IOException {
        for (Lump lump : wadLoader.getLumps()) {
            if (lump.name.equalsIgnoreCase(name)) {
                return lump;
            }
        }
        throw new IOException("Lump '" + name + "' não encontrado.");
    }

    private ByteBuffer readLump(FileChannel channel, Lump lump) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(lump.size);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        channel.position(lump.filepos);
        channel.read(buffer);
        buffer.flip();
        return buffer;
    }
}
