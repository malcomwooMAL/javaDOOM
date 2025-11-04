package wad.map;

import java.util.List;

public class MapData {
    public final List<Vertex> vertices;
    public final List<Linedef> linedefs;

    public MapData(List<Vertex> vertices, List<Linedef> linedefs) {
        this.vertices = vertices;
        this.linedefs = linedefs;
    }
}
