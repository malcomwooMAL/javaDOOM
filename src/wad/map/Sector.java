package wad.map;

public class Sector {
    public final int floorHeight;
    public final int ceilingHeight;
    public final String floorTexture;
    public final String ceilingTexture;
    public final int lightLevel;
    public final int specialType;
    public final int tagNumber;

    public Sector(int floorHeight, int ceilingHeight, String floorTexture, String ceilingTexture, int lightLevel, int specialType, int tagNumber) {
        this.floorHeight = floorHeight;
        this.ceilingHeight = ceilingHeight;
        this.floorTexture = floorTexture;
        this.ceilingTexture = ceilingTexture;
        this.lightLevel = lightLevel;
        this.specialType = specialType;
        this.tagNumber = tagNumber;
    }

    @Override
    public String toString() {
        return "Sector{" +
                "floorHeight=" + floorHeight +
                ", ceilingHeight=" + ceilingHeight +
                ", floorTexture='" + floorTexture + '\'' +
                ", ceilingTexture='" + ceilingTexture + '\'' +
                ", lightLevel=" + lightLevel +
                '}';
    }
}
