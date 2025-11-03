package wad;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WadLoader {

    private final String wadFile;
    private int numLumps;
    private int directoryOffset;
    private final List<Lump> lumps = new ArrayList<>();

    public WadLoader(String wadFile) {
        this.wadFile = wadFile;
    }

    public void load() throws IOException {
        try (RandomAccessFile file = new RandomAccessFile(wadFile, "r");
             FileChannel channel = file.getChannel()) {

            // 1. Ler o cabeçalho
            readHeader(channel);

            // 2. Ler o diretório
            readDirectory(channel);
        }
    }

    private void readHeader(FileChannel channel) throws IOException {
        // O cabeçalho do WAD tem 12 bytes
        ByteBuffer buffer = ByteBuffer.allocate(12);
        buffer.order(ByteOrder.LITTLE_ENDIAN); // Os dados no WAD estão em little-endian

        channel.read(buffer);
        buffer.flip();

        // Os primeiros 4 bytes são a "mágica" (identificação do tipo de WAD)
        byte[] magic = new byte[4];
        buffer.get(magic);
        String magicStr = new String(magic);

        if (!"IWAD".equals(magicStr) && !"PWAD".equals(magicStr)) {
            throw new IOException("Este não é um arquivo WAD válido: " + magicStr);
        }

        // Os próximos 4 bytes são o número de lumps
        numLumps = buffer.getInt();

        // Os últimos 4 bytes são o offset (localização) do diretório de lumps
        directoryOffset = buffer.getInt();
    }

    private void readDirectory(FileChannel channel) throws IOException {
        // Cada entrada do diretório tem 16 bytes
        int directoryEntrySize = 16;
        ByteBuffer dirBuffer = ByteBuffer.allocate(numLumps * directoryEntrySize);
        dirBuffer.order(ByteOrder.LITTLE_ENDIAN);

        // Pula para o início do diretório
        channel.position(directoryOffset);
        channel.read(dirBuffer);
        dirBuffer.flip();

        for (int i = 0; i < numLumps; i++) {
            int filepos = dirBuffer.getInt();
            int size = dirBuffer.getInt();

            byte[] nameBytes = new byte[8];
            dirBuffer.get(nameBytes);
            // O nome pode ter caracteres nulos, então precisamos limpá-lo
            String name = new String(nameBytes, StandardCharsets.US_ASCII).trim();

            lumps.add(new Lump(filepos, size, name));
        }
    }

    public int getNumLumps() {
        return numLumps;
    }

    public int getDirectoryOffset() {
        return directoryOffset;
    }

    public List<Lump> getLumps() {
        return lumps;
    }
}
