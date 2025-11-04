package wad;

import wad.map.MapData;
import wad.map.MapLoader;
import wad.map.MapData;
import wad.map.MapLoader;
import wad.view.MapPanel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        String wadFile = "doom-data/doom1.wad";
        if (args.length > 0) {
            wadFile = args[0];
        }

        try {
            System.out.println("Carregando WAD: " + wadFile);
            WadLoader wadLoader = new WadLoader(wadFile);
            wadLoader.load();

            System.out.println("Carregando Mapa E1M1...");
            MapData mapData = MapLoader.loadMap(wadLoader, "E1M1");

            System.out.println("Renderizando mapa em arquivo...");

            int width = 800;
            int height = 600;

            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = image.createGraphics();

            MapPanel mapPanel = new MapPanel(mapData);
            mapPanel.setSize(width, height);
            mapPanel.paint(g2d);

            g2d.dispose();

            File outputFile = new File("map_e1m1.png");
            ImageIO.write(image, "png", outputFile);

            System.out.println("Mapa salvo em: " + outputFile.getAbsolutePath());

        } catch (IOException e) {
            System.err.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
