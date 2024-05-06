import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Explosion extends Component {

    private int x;
    private int y;
    private int explosion;
    private int explosionTime;
    private boolean isRemoved;
    private final Image explosionImage;
    private final Image explosionBomb;

    public void setBomb(boolean bomb) {
        isBomb = bomb;
    }

    private boolean isBomb;

    public Explosion(int x, int y, int explosion, int explosionTime) {
        explosionImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("explosion.gif"))).getImage();
        explosionBomb = new ImageIcon(Objects.requireNonNull(getClass().getResource("bombExplosion.gif"))).getImage();
        this.x = x;
        this.y = y;
        this.explosion = explosion;
        this.explosionTime = explosionTime;
    }

    public synchronized void update(){
        explosion++;
        if (explosion > explosionTime){
            isRemoved = true;
        }
    }
    public boolean isRemoved() {
        return isRemoved;
    }

    public void drawImage(Graphics2D g, int width,int height){
        if (!isBomb){
            g.drawImage(explosionImage,x,y,width,height,null);
        } else {
            g.drawImage(explosionBomb,x,y,width,height,null);
        }

    }
}
