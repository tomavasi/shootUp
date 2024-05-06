import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Shooter {
    private final Image shooter;
    private final int heightParent;
    private final int widthParent;
    private int width;
    private int height;
    private int x ;
    private int y ;
    private int lives;

    private int speed;
    private final KeyListeners keyListener;

    public Shooter(int widthParent, int heightParent, KeyListeners keyListener, int speed, int lives) {
        this.heightParent = heightParent;
        this.widthParent = widthParent;
        this.keyListener = keyListener;
        this.speed = speed;
        this.lives = lives;
        shooter = new ImageIcon(Objects.requireNonNull(getClass().getResource("shooter.png"))).getImage();
    }

    public void setLocationY(int y) {
        this.y = y;
    }
    public int getLocationY() {
        return y;
    }
    public int getLocationX() {
        return x;
    }
    public int getHeight() { return height;}
    public int getWidth() { return width;}

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }
    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }
    public Rectangle getBoundingBox() {
        return new Rectangle(x+10, y+12, width-24, height-15);
    }

    public void update(){
        if (keyListener.isMoveRight() && x <= widthParent - width) {
            this.x = this.x + speed;
        }
        if (keyListener.isMoveLeft() && x >= 0){
            this.x = this.x - speed;
        }
    }
    public void drawImage(Graphics2D g,int width,int height) {
        g.drawImage(shooter, this.x, this.y, width, height, null);
    }
}
