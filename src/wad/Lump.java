package wad;

public class Lump {
    public final int filepos;
    public final int size;
    public final String name;

    public Lump(int filepos, int size, String name) {
        this.filepos = filepos;
        this.size = size;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Lump{" +
                "name='" + name + '\'' +
                ", size=" + size +
                ", filepos=" + filepos +
                '}';
    }
}
