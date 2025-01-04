import java.awt.*;


public class Bullet {
    public int bulletX;
    public int bulletY;
    final public int bulletVel = 20;
    final public int bulletSize = 8;
    final public Color bulletColor = new Color(150, 150, 0);

    public int bulletVelX;
    public int bulletVelY;

    public Bullet(int playerX, int playerY, double gunAngle) {
        bulletX = playerX;
        bulletY = playerY;

        bulletVelX = (int) (Math.cos(gunAngle) * bulletVel);
        bulletVelY = (int) (Math.sin(gunAngle) * bulletVel);
    }


    public boolean outOfBounds(int screenWidth, int screenHeight) {
        if (this.bulletX > screenWidth || this.bulletX < 0) {
            return true;
        }

        if (this.bulletY > screenHeight || this.bulletY < 0) {
            return true;
        }

        return false;
    }
}
