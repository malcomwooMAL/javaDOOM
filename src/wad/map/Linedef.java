package wad.map;

public class Linedef {
    public final int startVertex;
    public final int endVertex;
    public final int flags;
    public final int specialType;
    public final int sectorTag;
    public final int frontSidedef;
    public final int backSidedef;

    public Linedef(int startVertex, int endVertex, int flags, int specialType, int sectorTag, int frontSidedef, int backSidedef) {
        this.startVertex = startVertex;
        this.endVertex = endVertex;
        this.flags = flags;
        this.specialType = specialType;
        this.sectorTag = sectorTag;
        this.frontSidedef = frontSidedef;
        this.backSidedef = backSidedef;
    }

    @Override
    public String toString() {
        return "Linedef{" +
                "startVertex=" + startVertex +
                ", endVertex=" + endVertex +
                ", flags=" + flags +
                ", frontSidedef=" + frontSidedef +
                ", backSidedef=" + backSidedef +
                '}';
    }
}
