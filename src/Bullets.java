import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Bullets {
    private int x ;
    private int y ;
    private int speed = 20;
    private int width;
    private int height;
    private Image flameImage;
    private boolean bullet = false;



    public boolean isRemoved() {
        return isRemoved;
    }

    public void setRemoved(boolean removed) {
        isRemoved = removed;
    }

    private boolean isRemoved = false;

    public Bullets(int x, int y, int width, int height, int widthParent, int heightParent) {
        this.width = width;
        this.height = height;
        this.x = x + widthParent/2;
        this.y =  y + heightParent/2;
        flameImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("flame.png"))).getImage();
    }
    public void update(){
        if (this.y == -50){
            setRemoved(!isRemoved);
        }
        this.y = this.y - speed;
    }
    public Rectangle getBoundingBox() {
        return new Rectangle(x, y, width, height);
    }

    public void drawBullet(Graphics2D g) {
            if (bullet){
                g.setColor(Color.white);
                g.fillOval(this.x,this.y,width,height);
            } else {
                g.drawImage(flameImage,this.x,this.y,width,height,null);
            }
    }
}
