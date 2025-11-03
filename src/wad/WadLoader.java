package wad;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

public class WadLoader {

    private final String wadFile;
    private int numLumps;
    private int directoryOffset;

    public WadLoader(String wadFile) {
        this.wadFile = wadFile;
    }

    public void load() throws IOException {
        try (RandomAccessFile file = new RandomAccessFile(wadFile, "r");
             FileChannel channel = file.getChannel()) {

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
                throw new IOException("Este não é um arquivo WAD válido.");
            }

            // Os próximos 4 bytes são o número de lumps
            numLumps = buffer.getInt();

            // Os últimos 4 bytes são o offset (localização) do diretório de lumps
            directoryOffset = buffer.getInt();
        }
    }

    public int getNumLumps() {
        return numLumps;
    }

    public int getDirectoryOffset() {
        return directoryOffset;
    }
}
