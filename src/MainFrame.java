import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MainFrame extends JFrame {
    GamePanel gamePanel;
    ScoreBoard scoreBoard;

    public MainFrame() throws IOException {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setTitle("Shoot up");
        scoreBoard = new ScoreBoard(true);
        gamePanel = new GamePanel(scoreBoard);
        this.add(scoreBoard, BorderLayout.NORTH);
        this.add(gamePanel, BorderLayout.SOUTH);
        this.setLocationRelativeTo(null);
        this.pack();
        this.setVisible(true);

    }

    @Override
    public void dispose(){
        gamePanel.stop();
    }

}
