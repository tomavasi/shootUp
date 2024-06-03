import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Sounds {
    private Clip shootingApple;
    private Clip shootingBomb;
    private Clip shootingHeart;

    public Sounds() {

    }

    public void loadShootingApple(){
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(Objects.requireNonNull(getClass().getResourceAsStream("shootingApples2.wav"))));
            shootingApple = AudioSystem.getClip();
            shootingApple.open(audioInputStream);
        } catch (LineUnavailableException |
                 UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

    public void playShootingApple(){
        if (shootingApple != null){
            shootingApple.stop();
            shootingApple.setFramePosition(0);
            shootingApple.start();
        }
    }

    public void loadShootingBomb(){
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(Objects.requireNonNull(getClass().getResourceAsStream("shootingBomb.wav"))));
            shootingBomb = AudioSystem.getClip();
            shootingBomb.open(audioInputStream);
        } catch (LineUnavailableException |
                 UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

    public void playShootingBomb(){
        if (shootingBomb != null){
            shootingBomb.stop();
            shootingBomb.setFramePosition(0);
            shootingBomb.start();
        }
    }
    public void loadShootingHeart(){
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(Objects.requireNonNull(getClass().getResourceAsStream("shootingHeart.wav"))));
            shootingHeart = AudioSystem.getClip();
            shootingHeart.open(audioInputStream);
        } catch (LineUnavailableException |
                 UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

    public void playShootingHeart(){
        if (shootingHeart != null){
            shootingHeart.stop();
            shootingHeart.setFramePosition(0);
            shootingHeart.start();
        }
    }
}
