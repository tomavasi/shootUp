
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import javax.swing.*;

/**
 * @author Gebruiker
 */
public class Apple {
    private final int heightParent;
    private final int widthParent;
    private int width;
    private int height;
    private int yVelocity;
    private final Image appleImage;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    private int x;
    private int y;
    private boolean isRemoved = false;

    public Apple(int widthParent, int heightParent){
        this.heightParent = heightParent;
        this.widthParent = widthParent;
        appleImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("apple.png"))).getImage();
    }

    public void setLocationX(int x) {
        this.x = x;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }
    public Rectangle getBoundingBox() {
        return new Rectangle(x+15, y+10, width-30, height-22);
    }

    public void setVelocity(int yVelocity) {
        this.yVelocity = yVelocity;
    }

    public int getWidth() {
        return width;
    }

    public boolean getIsRemoved() {
        return isRemoved;
    }

    public synchronized void update() {
        if (this.y >= this.heightParent) {
            this.isRemoved = true;
        }
        this.y = this.y + yVelocity;
    }

    public void drawImage(Graphics2D g) {
        g.drawImage(appleImage,x,y,width,height,null);
    }

}
