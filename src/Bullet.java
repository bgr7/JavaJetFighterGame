
import java.awt.Graphics;

// it extends bullets class and there are draw and move method according to bullet type.
public class Bullet extends Bullets {
    public Bullet(int x, int y, int width, int height) {
        super(x, y, width, height, "bullet.jpg");
        // that class uses super's image according to its type etc.
    }
    
    
    
    @Override
    public void move() {
        y -= 4;
        if (y < 0) {
            isVisible = false;
        }
        
    }
    
  
    public void draw(Graphics g) {
        if (isVisible) {
            g.drawImage(bulletImage, x, y, width, height, null);
        }
    }
}
