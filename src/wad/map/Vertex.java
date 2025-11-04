package wad.map;

public class Vertex {
    public final int x;
    public final int y;

    public Vertex(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
