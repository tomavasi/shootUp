import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Heart {
    int x;
    int y;
    int width;
    int height;
    private final Image heart;
    private int yVelocity;
    private boolean isRemoved;
    private int heightParent;
    private int widthParent;

    public Heart(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        heart = new ImageIcon(Objects.requireNonNull(getClass().getResource("heart.png"))).getImage();
    }

    public Heart(int widthParent, int heightParent){
        this.heightParent = heightParent;
        this.widthParent = widthParent;
        heart = new ImageIcon(Objects.requireNonNull(getClass().getResource("heart.png"))).getImage();
    }

    public void setLocationX(int x) {
        this.x = x;
    }

    public int getLocationX(){
        return x;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }
    public Rectangle getBoundingBox() {
        return new Rectangle(x+20, y+20, width-40, height-38);
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
    public void drawImage (Graphics2D g){
        g.drawImage(heart,x,y,width,height,null);
    }
}
