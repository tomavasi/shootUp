import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyListeners implements KeyListener {

    private boolean moveRight;
    private boolean moveLeft;
    private boolean shoot;
    private boolean shootBig;
    private boolean pause = false;
    private boolean mute = false;
    public boolean isMute() {
        return mute;
    }

    public void setMute(boolean mute) {
        this.mute = mute;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e){
        if (e.getKeyCode() == KeyEvent.VK_RIGHT){
            setMoveRight(true);
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT){
            setMoveLeft(true);
        }
        if (e.getKeyCode() == KeyEvent.VK_S){
            setShoot(true);
        }
        if (e.getKeyCode() == KeyEvent.VK_W){
            setShootBig(true);
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
            setPause(!pause);
        }
        if (e.getKeyCode() == KeyEvent.VK_M){
            setMute(!mute);
            GamePanel gamePanel = (GamePanel) e.getSource();
            gamePanel.getScoreBoard().drawMute(mute);
        }
        if (e.getKeyCode() == KeyEvent.VK_R){
            GamePanel gamePanel = (GamePanel) e.getSource();
            if (pause || gamePanel.getRemainingLives() == 0){
                gamePanel.restart();
            }

        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT){
            setMoveRight(false);
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT){
            setMoveLeft(false);
        }
        if (e.getKeyCode() == KeyEvent.VK_S){
            setShoot(false);
        }
        if (e.getKeyCode() == KeyEvent.VK_W){
            setShootBig(false);
        }
    }

    public void setMoveRight(boolean moveRight) {
        this.moveRight = moveRight;
    }
    public void setPause(boolean pause) {this.pause = pause; }

    public void setMoveLeft(boolean moveLeft) {
        this.moveLeft = moveLeft;
    }
    public void setShoot(boolean shoot) {
        this.shoot = shoot;
    }

    public boolean isShootBig() {
        return shootBig;
    }

    public void setShootBig(boolean shootBig) {
        this.shootBig = shootBig;
    }

    public boolean isMoveLeft() {
        return moveLeft;
    }

    public boolean isShoot() {
        return shoot;
    }

    public boolean isMoveRight() {
        return moveRight;
    }

    public boolean isPause() {
        return pause;
    }

}
