package wad.map;

public class Sidedef {
    public final int textureOffset;
    public final int rowOffset;
    public final String topTexture;
    public final String middleTexture;
    public final String bottomTexture;
    public final int sectorNumber;

    public Sidedef(int textureOffset, int rowOffset, String topTexture, String middleTexture, String bottomTexture, int sectorNumber) {
        this.textureOffset = textureOffset;
        this.rowOffset = rowOffset;
        this.topTexture = topTexture;
        this.middleTexture = middleTexture;
        this.bottomTexture = bottomTexture;
        this.sectorNumber = sectorNumber;
    }

    @Override
    public String toString() {
        return "Sidedef{" +
                "topTexture='" + topTexture + '\'' +
                ", middleTexture='" + middleTexture + '\'' +
                ", bottomTexture='" + bottomTexture + '\'' +
                ", sectorNumber=" + sectorNumber +
                '}';
    }
}
