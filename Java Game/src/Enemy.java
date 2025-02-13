import java.awt.*;

public class Enemy {
    public int x;
    public int y;
    public int centerX;
    public int centerY;
    public final int size = 20;
    public double enemyVelocity = 5.0;
    public final Color enemyColor = new Color(0, 100, 0);

    public Enemy(int x, int y) {
        this.x = x;
        this.y = y;

        this.centerX = this.x + (this.size / 2);
        this.centerY = this.y + (this.size / 2);
    }

    public boolean collideBullet(Bullet bullet) {
        if (bullet.bulletX >= this.x && bullet.bulletX <= this.x + this.size) {
            if (bullet.bulletY >= this.y - this.size && bullet.bulletY <= this.y) {
                return true;
            }

            if (bullet.bulletY <= this.y + this.size && bullet.bulletY >= this.y) {
                return true;
            }
        }

        return false;
    }

    public boolean collidePlayer(int playerX, int playerY) {
        if (playerX >= this.x && playerX <= this.x + this.size) {
            if (playerY >= this.y - this.size && playerY <= this.y) {
                return true;
            }

            if (playerY <= this.y + this.size && playerY >= this.y) {
                return true;
            }
        }

        return false;
    }

    public void moveTowardsPoint(int playerX, int playerY) {
        int diffX = this.x + (this.size / 2) - playerX;
        int diffY = this.y + (this.size / 2) - playerY;

        double angle = Math.atan2(diffY, diffX);

        if (diffX == 0 && diffY == 0) {
            return;
        }

        if (diffX > 0) {
            this.x -= (int) Math.abs(Math.cos(angle) * enemyVelocity);
        }

        else {
            this.x += (int) Math.abs(Math.cos(angle) * enemyVelocity);
        }

        if (diffY > 0) {
            this.y -= (int) Math.abs(Math.sin(angle) * enemyVelocity);
        }

        else {
            this.y += (int) Math.abs(Math.sin(angle) * enemyVelocity);
        }


        this.centerX = this.x + (this.size / 2);
        this.centerY = this.y + (this.size / 2);



    }
}
