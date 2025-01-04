import java.awt.*;

public class Gun {
    final int gunWidth = 80;
    final int gunHeight = 20;
    int playerX;
    int playerY;
    public double cooldown;
    public double gunAngle;

    Color gunColor = new Color(10, 10, 10);


    public Gun(int playerX, int playerY, double cooldown) {
        this.playerX = playerX;
        this.playerY = playerY;
        this.cooldown = cooldown;
    }

    public void pointAtMouse(int mouseX, int mouseY) {
        int diffX = (mouseX - this.playerX);
        int diffY = (mouseY - this.playerY);

        gunAngle = Math.atan2(diffY, diffX);

    }
}
