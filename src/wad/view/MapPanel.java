package wad.view;

import javax.swing.JPanel;
import wad.map.MapData;
import wad.map.Vertex;
import wad.map.Linedef;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class MapPanel extends JPanel {

    private final MapData mapData;
    private double minX = Double.MAX_VALUE;
    private double minY = Double.MAX_VALUE;
    private double maxX = Double.MIN_VALUE;
    private double maxY = Double.MIN_VALUE;

    public MapPanel(MapData mapData) {
        this.mapData = mapData;
        calculateBounds();
    }

    private void calculateBounds() {
        for (Vertex v : mapData.vertices) {
            if (v.x < minX) minX = v.x;
            if (v.y < minY) minY = v.y;
            if (v.x > maxX) maxX = v.x;
            if (v.y > maxY) maxY = v.y;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Fundo preto
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Desenhar com linhas verdes
        g2d.setColor(Color.GREEN);

        // Calcular a escala e o deslocamento para centralizar o mapa
        double mapWidth = maxX - minX;
        double mapHeight = maxY - minY;

        double panelWidth = getWidth();
        double panelHeight = getHeight();

        double scaleX = panelWidth / mapWidth;
        double scaleY = panelHeight / mapHeight;
        double scale = Math.min(scaleX, scaleY) * 0.9; // 90% para ter uma margem

        double offsetX = (panelWidth - (mapWidth * scale)) / 2.0;
        double offsetY = (panelHeight - (mapHeight * scale)) / 2.0;

        // Aplicar a transformação
        for (Linedef linedef : mapData.linedefs) {
            Vertex v1 = mapData.vertices.get(linedef.startVertex);
            Vertex v2 = mapData.vertices.get(linedef.endVertex);

            // Transforma as coordenadas do mapa para as coordenadas da tela
            int x1 = (int) (offsetX + (v1.x - minX) * scale);
            int y1 = (int) (offsetY + (maxY - v1.y) * scale); // Inverte o eixo Y

            int x2 = (int) (offsetX + (v2.x - minX) * scale);
            int y2 = (int) (offsetY + (maxY - v2.y) * scale); // Inverte o eixo Y

            g2d.drawLine(x1, y1, x2, y2);
        }
    }
}
