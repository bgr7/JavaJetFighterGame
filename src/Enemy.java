import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;



public class Enemy implements Runnable{
    int x;
	int y;
	int width;
	int height;
    boolean isVisible;
    private Image enemyImage;
    private List<EnemyBullet> enemyBullets;
    private EnemyBullet enemyBullet;
    private Thread enemyThread;

    // I created a thread while enemy move after spawned thread is used.
    private int playerX;
    private int playerY;

    public Enemy(int x, int y, int width,int height, int playerX, int playerY) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height=height;
        this.isVisible = true;
        this.playerX = playerX;
        this.playerY = playerY;
        this.enemyImage = getRandomEnemyImage();
        this.enemyBullets = new ArrayList<>();
        this.enemyBullet = new EnemyBullet(x, y, 20, 20);
        enemyThread = new Thread(this);
        enemyThread.start(); 
    }
    
   // enemy constructor is created.

    

    public List<EnemyBullet> getEnemyBullets() {
        return enemyBullets;
    }
    

   
    public void shootBullet() {
        EnemyBullet bullet = new EnemyBullet(x + width/ 2, y + height, 5, 10);
        enemyBullets.add(bullet);
        enemyBullet.isVisible = true;
        enemyBullet.x = this.x + width / 2 - enemyBullet.width / 2;
        enemyBullet.y = this.y + height;
    }
    // it allows enemy to shoot enemybullet.
    public void setPlayerPosition(int playerX, int playerY) {
        this.playerX = playerX;
        this.playerY = playerY;
    }
   
    

    private Image getRandomEnemyImage() {
        Image[] enemyImages = {
                new ImageIcon(getClass().getResource("enemy1.png")).getImage(),
                new ImageIcon(getClass().getResource("enemy2.png")).getImage(),
                new ImageIcon(getClass().getResource("enemy3.png")).getImage(),
        };
        
        int selectedIndex = new Random().nextInt(enemyImages.length);
        return enemyImages[selectedIndex];
    }
    
    // I created that method to use create different enemy planes.

    public void move() {
        int playerCenterX = playerX + 25;
        int playerCenterY = playerY + 25;

        // I researhed and found that polar coordinate function to make enemy follow player.
        double angle = Math.atan2(playerCenterY - (y + height/ 2), playerCenterX - (x + width / 2));

       
        int speed = 2;
        x += speed * Math.cos(angle);
        y += speed * Math.sin(angle);

        if (y > 600) {
            isVisible = false;
        }

        if (enemyBullet.isVisible) {
            enemyBullet.move();
            if (enemyBullet.y > 600) {
                enemyBullet.isVisible = false;
            }
        }
    }
  // Move method provided specific movement to enemy which follows player's plane.
    

    public void draw(Graphics g) {
        if (isVisible) {
            
            g.drawImage(enemyImage, x, y,width, height, null);
        }
        if (enemyBullet.isVisible) {
            enemyBullet.draw(g);
        }
    }
    // enemy planes are drawn into game.

    public Thread getEnemyThread() {
        return enemyThread;
    }

    public void setEnemyThread(Thread enemyThread) {
        this.enemyThread = enemyThread;
    }
    
    @Override
    public void run() {
        while (isVisible) {
            move();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
