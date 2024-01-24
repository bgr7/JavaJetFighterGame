import java.awt.*;
//it extends bullets class and there are draw and move method according to bullet type.
public class EnemyBullet extends Bullets {
    public EnemyBullet(int x, int y, int width, int height) {
        super(x, y, width, height, "enemybullet.png");
    }
 // that class uses super's image according to its type etc.

    @Override
    public void move() {
        y += 3;
        if (y > 600) {
            isVisible = false;
        }
    }
    
    

    public void draw(Graphics g) {
        if (isVisible) {
            g.drawImage(enemybulletImage, x, y, width, height, null);
        }
    }
}
