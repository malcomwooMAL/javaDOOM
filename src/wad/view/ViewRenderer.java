package wad.view;

import wad.game.Player;
import wad.map.Linedef;
import wad.map.MapData;
import wad.map.Vertex;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class ViewRenderer {

    private final MapData mapData;
    private final Player player;

    public ViewRenderer(MapData mapData, Player player) {
        this.mapData = mapData;
        this.player = player;
    }

    public void render(Graphics2D g2d, int width, int height) {
        // Fundo preto
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, width, height);
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(0, height / 2, width, height / 2);

        // Fov (campo de visão) de 90 graus
        double fov = Math.PI / 2;

        for (Linedef linedef : mapData.linedefs) {
            Vertex v1 = mapData.vertices.get(linedef.startVertex);
            Vertex v2 = mapData.vertices.get(linedef.endVertex);

            // Transformar vértices para o espaço da câmera
            double tx1 = v1.x - player.x;
            double ty1 = v1.y - player.y;
            double tx2 = v2.x - player.x;
            double ty2 = v2.y - player.y;

            // Rotacionar em torno do jogador
            double rtx1 = tx1 * Math.cos(-player.angle) - ty1 * Math.sin(-player.angle);
            double rty1 = tx1 * Math.sin(-player.angle) + ty1 * Math.cos(-player.angle);
            double rtx2 = tx2 * Math.cos(-player.angle) - ty2 * Math.sin(-player.angle);
            double rty2 = tx2 * Math.sin(-player.angle) + ty2 * Math.cos(-player.angle);

            // Se a parede estiver completamente atrás do jogador, não renderize
            if (rty1 <= 0 && rty2 <= 0) {
                continue;
            }

            // Projeção 3D
            double halfWidth = width / 2.0;
            double projFactor1 = halfWidth / Math.tan(fov / 2);
            double projFactor2 = halfWidth / Math.tan(fov / 2);

            double x1 = projFactor1 * rtx1 / rty1 + halfWidth;
            double x2 = projFactor2 * rtx2 / rty2 + halfWidth;

            // Se a parede estiver fora da tela, não renderize
            if (x1 > x2 || x2 < 0 || x1 > width) {
                continue;
            }

            // Altura da parede (simplificada por enquanto)
            double wallHeight = height * 0.8;
            double y1_top = height / 2.0 - wallHeight / (rty1 * 0.05);
            double y1_bottom = height / 2.0 + wallHeight / (rty1 * 0.05);
            double y2_top = height / 2.0 - wallHeight / (rty2 * 0.05);
            double y2_bottom = height / 2.0 + wallHeight / (rty2 * 0.05);

            // Desenhar a parede como um polígono
            g2d.setColor(Color.WHITE);
            Polygon wallPolygon = new Polygon();
            wallPolygon.addPoint((int)x1, (int)y1_top);
            wallPolygon.addPoint((int)x2, (int)y2_top);
            wallPolygon.addPoint((int)x2, (int)y2_bottom);
            wallPolygon.addPoint((int)x1, (int)y1_bottom);
            g2d.fill(wallPolygon);
        }
    }
}
