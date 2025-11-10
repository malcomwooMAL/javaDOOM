package wad.texture;

public class MapPatch {
    public final int originX;
    public final int originY;
    public final int patchIndex;

    public MapPatch(int originX, int originY, int patchIndex) {
        this.originX = originX;
        this.originY = originY;
        this.patchIndex = patchIndex;
    }
}
