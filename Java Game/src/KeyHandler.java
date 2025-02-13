import java.awt.RenderingHints.Key;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    public boolean up = false;
    public boolean down = false;
    public boolean left = false;
    public boolean right = false;
    public boolean deadEye = false;
    public boolean canDeadEye = true;

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A) {
            this.left = true;
        }

        else if (key == KeyEvent.VK_W) {
            this.up = true;
        }

        else if (key == KeyEvent.VK_S) {
            this.down = true;
        }

        else if (key == KeyEvent.VK_D) {
            this.right = true;
        }

        else if (key == KeyEvent.VK_SPACE && this.canDeadEye && !this.deadEye) {
            this.deadEye = true;
        
        }

        else if (key == KeyEvent.VK_SPACE && this.deadEye) {
            this.deadEye = false;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A) {
            this.left = false;
        }

        else if (key == KeyEvent.VK_W) {
            this.up = false;
        }

        else if (key == KeyEvent.VK_S) {
            this.down = false;
        }

        else if (key == KeyEvent.VK_D) {
            this.right = false;
        }

    }


}
