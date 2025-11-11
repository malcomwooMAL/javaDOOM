package wad;

import wad.game.Player;
import wad.map.MapData;
import wad.map.MapLoader;
import wad.view.ViewPanel;

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

            // Posição inicial do jogador no E1M1
            Player player = new Player(1056, -3616, Math.PI / 2);

            System.out.println("Renderizando visão 3D em arquivo...");

            int width = 800;
            int height = 600;

            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = image.createGraphics();

            ViewPanel viewPanel = new ViewPanel(mapData, player);
            viewPanel.setSize(width, height);
            viewPanel.paint(g2d);

            g2d.dispose();

            File outputFile = new File("view_3d_e1m1.png");
            ImageIO.write(image, "png", outputFile);

            System.out.println("Visão 3D salva em: " + outputFile.getAbsolutePath());

        } catch (IOException e) {
            System.err.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
