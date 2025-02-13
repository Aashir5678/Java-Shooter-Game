import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable{
    final int screenWidth = 1000;
    final int screenHeight = 600;
    final int playerWidth = 60; // 60
    final int playerHeight = 80; // 80
    int playerVelocity = 5;
    private int fps = 60;
    final int spawnRate = 100;
    final double recoilFactor = 9.3;
    final double playerRecoil = 0.3;
    final int deadEyeLength = 10 * 60;
    final int deadEyeXSize = 15;
    final double gunCooldown = 3.5; // Can only be whole multiples of 0.5 >= 1
    final int spawnRadius = 300;

    final Color playerColor = new Color(255, 0, 0);

    int playerX = (screenWidth / 2);
    int playerY = (screenHeight / 2);
    int playerCenterX = playerX + (playerWidth / 2);
    int playerCenterY = playerY + (playerHeight / 2);


    Gun gun = new Gun(playerCenterX, playerCenterY, gunCooldown);
    List<Bullet> bullets = new ArrayList<Bullet>();
    List<Enemy> enemies = new ArrayList<Enemy>();
    List<int[]> deadEyeTags = new ArrayList<int[]>();
    public int deadEyeTimer = 0;
    public int maxTags = 5; // Max amount of dead eye tags allowed

    public int removeBulletIndex;
    public int removeEnemyIndex;
    public boolean recoil = true;
    public boolean canDeadEye;
    public long startTime = 0;
    public double gunX, gunY;
    public static PointerInfo mouseInfo;
    public int recoilDeltaX, recoilDeltaY;

    final Color backgroundColor = new Color(200, 200, 200);
    KeyHandler keyHandler = new KeyHandler();
    MouseHandler mouseHandler = new MouseHandler();
    Thread gameThread;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(backgroundColor);
        this.setDoubleBuffered(true); // Improves games rendering performance
        this.addKeyListener(keyHandler);
        this.addMouseListener(mouseHandler);
        this.setFocusable(true);

    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        // To have 60 fps, finish game loop then sleep until 1/60 seconds have passed since the start of the loop to reiterate
        long endTime;
        long elapsedTime;
        Random rand = new Random();

        while (gameThread != null) {
            startTime = System.currentTimeMillis();
            if (keyHandler.deadEye) {
                deadEyeTimer += 1;
                if (deadEyeTimer > deadEyeLength) {
                    keyHandler.deadEye = false;
                    deadEyeTimer = 0;
                    deadEyeTags.clear();
                }
            }

            if (keyHandler.deadEye && playerVelocity != 0) {
                for (Enemy enemy : enemies) {
                    enemy.enemyVelocity = 0.8;
                }

                playerVelocity = 0;
                
            }

            else if (!keyHandler.deadEye && playerVelocity == 0) {
                playerVelocity = 5;

                for (Enemy enemy : enemies) {
                    enemy.enemyVelocity = 5.0;
                }

                deadEyeTags.clear();
            }

            mouseInfo = MouseInfo.getPointerInfo();


            gun.pointAtMouse(mouseInfo.getLocation().x, mouseInfo.getLocation().y);

            if (rand.nextInt(spawnRate) == 0) {
                spawnEnemy();
            }

            update();
            repaint();

            endTime = System.currentTimeMillis();
            elapsedTime = endTime - startTime;

            try {
                Thread.sleep((1000/fps) - elapsedTime);
            }

            catch (InterruptedException e){
                e.printStackTrace();
            }

            catch (IllegalArgumentException e) {
                e.printStackTrace();
            }



        }

    }

    public void spawnEnemy() {
        Random rand = new Random();

        int enemyX = rand.nextInt(screenWidth);
        int enemyY = rand.nextInt(screenHeight);
        Enemy enemy;

        if (euclidDist(enemyX, enemyY, playerCenterX, playerCenterY) >= spawnRadius) {
            enemy = new Enemy(enemyX, enemyY);
            if (keyHandler.deadEye) {
                enemy.enemyVelocity = 0.8;
            }
            enemies.add(enemy);
        }

    }

    public double euclidDist(int x1, int y1, int x2, int y2) {
        return Math.sqrt((float) ((x1-x2) * (x1-x2)) + ((y1-y2) * (y1-y2)));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2D = (Graphics2D) g; // Type cast
        if (keyHandler.deadEye) {
            this.setBackground(new Color(255, 130, 0));
        }

        else {
            this.setBackground(new Color(255, 255, 255));
        }

        if (!bullets.isEmpty()) {
            g2D.setColor(bullets.get(0).bulletColor);

            for (Bullet bullet : bullets) {
                g2D.fillRect(bullet.bulletX, bullet.bulletY, bullet.bulletSize, bullet.bulletSize);

            }
        }

        if (!enemies.isEmpty()) {
            for (Enemy enemy : enemies) {
                g2D.setColor(enemy.enemyColor);
                g2D.fillRect(enemy.x, enemy.y, enemy.size, enemy.size);
            }
        }

        g2D.setColor(playerColor);
        g2D.fillRect(playerX, playerY, playerWidth, playerHeight);

        g2D.setColor(gun.gunColor);
        g2D.translate(playerCenterX, playerCenterY);
        g2D.rotate(gun.gunAngle);
        g2D.translate(-playerCenterX, -playerCenterY);

        // Recoil control

        if (!keyHandler.deadEye) {
            if (recoil && startTime % gunCooldown == 0 && mouseHandler.shooting) {
                recoilDeltaX = (int) Math.abs(recoilFactor * Math.cos(gun.gunAngle));
                recoilDeltaY = (int) Math.abs(recoilFactor * Math.sin(gun.gunAngle));
    
                if (gun.gunAngle >= 0 && gun.gunAngle <= Math.PI / 2) { // Bottom right quadrant
                    gunX = playerCenterX - recoilDeltaX;
                    gunY = playerCenterY - recoilDeltaY;
    
                    playerX -= (int) recoilDeltaX * playerRecoil;
                    playerY -= (int) recoilDeltaY * playerRecoil;
    
                }
    
                else if (gun.gunAngle <= Math.PI && gun.gunAngle >= Math.PI / 2) { // Bottom left quadrant
                    gunX = playerCenterX + (recoilDeltaX);
                    gunY = playerCenterY - (recoilDeltaY);
    
                    playerX += (int) recoilDeltaX * playerRecoil;
                    playerY -= (int) recoilDeltaY * playerRecoil;
    
                }
    
                else if (gun.gunAngle <= -Math.PI / 2  && gun.gunAngle >= -Math.PI) { // Top left quadrant
                    gunX = playerCenterX + (recoilDeltaX);
                    gunY = playerCenterY + (recoilDeltaY);
    
                    playerX += (int) recoilDeltaX * playerRecoil;
                    playerY += (int) recoilDeltaY * playerRecoil;
    
    
                }
    
                else { // Top right quadrant
                    gunX = playerCenterX - (recoilDeltaX);
                    gunY = playerCenterY + (recoilDeltaY);
    
                    playerX -= (int) recoilDeltaX * playerRecoil;
                    playerY += (int) recoilDeltaY * playerRecoil;
    
                }
    
                playerCenterX = playerX + (playerWidth / 2);
                playerCenterY = playerY + (playerHeight / 2);
    
                g2D.fillRect((int) (gunX - (gun.gunWidth / 2)), (int) (gunY - (gun.gunHeight / 2)), gun.gunWidth, gun.gunHeight);
    
            }
    
            else {
                g2D.fillRect(playerCenterX - (gun.gunWidth / 2), playerCenterY - (gun.gunHeight / 2), gun.gunWidth, gun.gunHeight);
            }
        }

        else {
            g2D.fillRect(playerCenterX - (gun.gunWidth / 2), playerCenterY - (gun.gunHeight / 2), gun.gunWidth, gun.gunHeight);
            int x, y;

            // Draw the x's marked from dead eye
            for (int[] tags : deadEyeTags) {
                x = tags[0];
                y = tags[1];
                drawX(x, y, g2D);

            }
        }


        g2D.dispose();

    }

    public void drawX(int x, int y, Graphics2D g2D) {
        g2D.setColor(new Color(255, 0, 0));
        g2D.setStroke(new BasicStroke(10));
        g2D.drawLine(x - (deadEyeXSize / 2), y - (deadEyeXSize / 2), x + (deadEyeXSize / 2), y + (deadEyeXSize / 2));
        g2D.drawLine(x - (deadEyeXSize / 2), y + (deadEyeXSize / 2), x + (deadEyeXSize / 2), y - (deadEyeXSize / 2));
    }

    public void update() {
        removeBulletIndex = -1;
        removeEnemyIndex = -1;

        if (keyHandler.up) {
            playerY -= playerVelocity;
        } else if (keyHandler.down) {
            playerY += playerVelocity;
        } else if (keyHandler.right) {
            playerX += playerVelocity;
        } else if (keyHandler.left) {
            playerX -= playerVelocity;
        }

        playerCenterX = playerX + (playerWidth / 2);
        playerCenterY = playerY + (playerHeight / 2);

        gun.playerX = playerCenterX;
        gun.playerY = playerCenterY;


        if (mouseHandler.shooting && startTime % gun.cooldown == 0 && !keyHandler.deadEye) {
            Bullet bullet = new Bullet(playerCenterX, playerCenterY, gun.gunAngle);
            bullets.add(bullet);

        }

        else if (mouseHandler.tagging && keyHandler.deadEye) {
            int[] point = new int[] {mouseInfo.getLocation().x, mouseInfo.getLocation().y};
            if (!deadEyeTags.contains(point) && deadEyeTags.size() <= maxTags) {
                deadEyeTags.add(point);
            }
        }

        if (!bullets.isEmpty()) {
            for (Bullet bullet : bullets) {
                bullet.bulletX += bullet.bulletVelX;
                bullet.bulletY += bullet.bulletVelY;

                if (bullet.outOfBounds(screenWidth, screenHeight)) {
                    bullets.remove(bullet);
                    removeBulletIndex = bullets.indexOf(bullet);
                    break;

                }

            }

            if (removeBulletIndex != -1) {
                bullets.remove(removeBulletIndex);
            }


        }

        if (!enemies.isEmpty()) {
            for (Enemy enemy : enemies) {
                enemy.moveTowardsPoint(playerCenterX, playerCenterY);

                if (!bullets.isEmpty()) {
                    for (Bullet bullet : bullets) {
                        if (enemy.collideBullet(bullet)) {
                            removeEnemyIndex = enemies.indexOf(enemy);
                            removeBulletIndex = bullets.indexOf(bullet);
                        }
                    }
                }
            }

            if (removeEnemyIndex != -1) {
                enemies.remove(removeEnemyIndex);
                bullets.remove(removeBulletIndex);
            }
        }


    }
}
