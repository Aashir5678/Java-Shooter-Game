import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseHandler implements MouseListener {
    public boolean shooting = false;
    public boolean tagging = false;
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (!shooting) {
                shooting = true;
            }

            else {
                shooting = false;
            }
        }

        if (e.getButton() == MouseEvent.BUTTON3) {
            tagging = true;
        }

        else {
            tagging = false;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            shooting = false;
        }

        else if (e.getButton() == MouseEvent.BUTTON3) {
            tagging = false;
        }

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
