package wad.texture;

import java.util.List;

public class Texture {
    public final String name;
    public final int width;
    public final int height;
    public final List<MapPatch> patches;

    public Texture(String name, int width, int height, List<MapPatch> patches) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.patches = patches;
    }
}
