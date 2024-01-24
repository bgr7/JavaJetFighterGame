import javax.swing.*;
import java.awt.*;
// I created abstract bullets class to create 2 different bullet type properly.
public abstract class Bullets {
    int x, y, width, height;
    boolean isVisible;
    protected Image bulletImage;
    protected Image enemybulletImage;

    public Bullets(int x, int y, int width, int height, String imageName) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isVisible = true;
        this.bulletImage = new ImageIcon(getClass().getResource(imageName)).getImage();
        this.enemybulletImage = new ImageIcon(getClass().getResource(imageName)).getImage();

    }

    public abstract void move();

    public void draw(Graphics g) {
        if (isVisible) {
            g.drawImage(bulletImage, x, y, width, height, null);
        }
    }
}

