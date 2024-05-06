import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Bomb {
    private final int heightParent;
    private final int widthParent;
    private int width;
    private int height;
    private int yVelocity;
    private final Image bombImage;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    private int x;
    private int y;
    private boolean isRemoved = false;

    public Bomb(int widthParent, int heightParent){
        this.heightParent = heightParent;
        this.widthParent = widthParent;
        bombImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("bomb.png"))).getImage();
    }

    public void setLocationX(int x) {
        this.x = x;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }
    public Rectangle getBoundingBox() {
        return new Rectangle(x, y, width-5, height);
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
        g.drawImage(bombImage,x,y,width,height,null);
//        g.setColor(Color.RED);
//        g.draw(getBoundingBox());
    }
}
