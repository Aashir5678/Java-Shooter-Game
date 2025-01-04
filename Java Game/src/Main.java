import javax.swing.JFrame;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setSize(new Dimension(1000, 600));
        window.setBackground(new Color(200, 200, 200));

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.pack();


        window.setTitle("Java Game");
        window.setVisible(true);
        gamePanel.startGameThread();


    }
}