import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class ScoreBoard extends JPanel {
    private final ArrayList<Heart> hearts;
    private static JLabel scoreLabel;
    private int parentWidth;
    private Image muteImage;

    public void setParentWidth(int parentWidth) {
        this.parentWidth = parentWidth;
    }

    Font pixelFont = new Font("Public Pixel", Font.PLAIN, 12);
    public ArrayList<Heart> getHearts() {
        return hearts;
    }

    public ScoreBoard(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        scoreLabel = new JLabel("Your score is: " + 0);
        scoreLabel.setFont(pixelFont);
        scoreLabel.setForeground(Color.white);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(Box.createVerticalGlue());
        add(scoreLabel);
        this.add(Box.createVerticalGlue());
        hearts = new ArrayList<>();
        muteImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("unmute.png"))).getImage();
    }

    public void addHearts(int index){
        int x = 0;
        for (int i = 0; i < index; i++){
            hearts.add(new Heart(x, 0, 50,50));
            x = x+50;
        }
        revalidate();
        repaint();
    }

    public void addMoreHearts (){
        int locLastHeart = hearts.get(hearts.size() -1).getLocationX();
        hearts.add(new Heart(locLastHeart + 50, 0, 50,50));
        revalidate();
        repaint();
    }

    public void updateHearts(int index){
        hearts.remove(index);
        revalidate();
        repaint();
    }

    public void setScore(int score) {
       scoreLabel.setText("Your score is: " + score);
    }

    public void drawMute (boolean mute) {
        if (mute){
            muteImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("mute.png"))).getImage();
        }
        if (!mute){
            muteImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("unmute.png"))).getImage();
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(new Color(36, 36, 36));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (int i = 0; i < hearts.size(); i++){
            Heart heart = hearts.get(i);
            if (heart != null){
                heart.drawImage(g2d);
            }
        }
        g.drawImage(muteImage, parentWidth - 40, 10, 30, 30, null);
    }
}
