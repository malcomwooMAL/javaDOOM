package wad.map;

import java.util.List;

public class MapData {
    public final List<Vertex> vertices;
    public final List<Linedef> linedefs;
    public final List<Sidedef> sidedefs;
    public final List<Sector> sectors;

    public MapData(List<Vertex> vertices, List<Linedef> linedefs, List<Sidedef> sidedefs, List<Sector> sectors) {
        this.vertices = vertices;
        this.linedefs = linedefs;
        this.sidedefs = sidedefs;
        this.sectors = sectors;
    }
}
