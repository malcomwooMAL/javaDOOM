package wad.view;

import wad.game.Player;
import wad.map.MapData;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class ViewPanel extends JPanel {

    private final MapData mapData;
    private final Player player;
    private final ViewRenderer viewRenderer;

    public ViewPanel(MapData mapData, Player player) {
        this.mapData = mapData;
        this.player = player;
        this.viewRenderer = new ViewRenderer(mapData, player);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        viewRenderer.render((Graphics2D) g, getWidth(), getHeight());
    }
}
