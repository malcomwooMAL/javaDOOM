package wad.view;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.HeadlessException;

public class DoomFrame extends JFrame {

    public DoomFrame(String title) throws HeadlessException {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(800, 600));
        setLocationRelativeTo(null); // Centralizar na tela
    }
}
