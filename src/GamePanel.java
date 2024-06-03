import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class GamePanel extends JPanel {
    private final int FPS = 60;
    private final int originalSize = 14;
    private final int scale = 3;
    private final int tileSize = originalSize * scale;
    private final int maxScreenCol = 14;
    private final int maxScreenRow = 14;
    private final int screenWidth = tileSize * maxScreenCol;
    private final int screenHeight = tileSize * maxScreenRow;
    private ArrayList<Apple> apples;
    private int applePeriod = 3000;
    private ArrayList<Bomb> bombs;
    private int score = 0;
    private Shooter shooter;
    private Bullets bullet;
    private ArrayList<Bullets> bullets;
    private ArrayList<Explosion> explosions;
    private CopyOnWriteArrayList<Heart> hearts;
    private final KeyListeners keyListener = new KeyListeners();
    private Image backgroundImage;

    public ScoreBoard getScoreBoard() {
        return scoreBoard;
    }

    private final ScoreBoard scoreBoard;
    private int shotTime;
    private Sounds sounds = new Sounds();

    public int getRemainingLives() {
        return remainingLives;
    }

    private int remainingLives;
    private static ScheduledExecutorService executor;
    private ScheduledFuture<?> appleExecutor;

    public GamePanel(ScoreBoard scoreBoard) {
        this.scoreBoard = scoreBoard;
        scoreBoard.setPreferredSize(new Dimension(screenWidth, 50));
        scoreBoard.setParentWidth(screenWidth);
        setPanelCharacteristics();
        initComponents();
        start();
    }

    public void start() {
        if (!keyListener.isPause()) {
            executor = Executors.newScheduledThreadPool(3);
            executor.scheduleAtFixedRate(this::gameLoop, 0, 1000 / FPS, TimeUnit.MILLISECONDS);
            appleExecutor = executor.scheduleAtFixedRate(this::addApples, 1000, applePeriod, TimeUnit.MILLISECONDS);
            scheduleRandomBomb();
            scheduleRandomHeart();
        }
    }

    private void scheduleRandomBomb() {
        Random random = new Random();
        int delay = random.nextInt(5000) + 5000;
        executor.schedule(() -> {
            if (!keyListener.isPause()) {
                addBombs();
                scheduleRandomBomb();
            }
        }, delay, TimeUnit.MILLISECONDS);
    }

    private void scheduleRandomHeart() {
        Random random = new Random();
        int delay = random.nextInt(15000) + 10000;
        executor.schedule(() -> {
            if (!keyListener.isPause() && remainingLives < 3) {
                addHearts();
            }
            scheduleRandomHeart();
        }, delay, TimeUnit.MILLISECONDS);
    }

    private void gameLoop() {
        if (!keyListener.isPause() || remainingLives != 0) {
            try {
                update();
                shootingTask();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        repaint();
    }

    public void initComponents() {
        apples = new ArrayList<>();
        bombs = new ArrayList<>();
        explosions = new ArrayList<>();
        bullets = new ArrayList<>();
        hearts = new CopyOnWriteArrayList<>();
        backgroundImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("background.jpg"))).getImage();
        shooter = new Shooter(screenWidth, screenHeight, keyListener, 6, 3);
        shooter.setSize(70, 70);
        shooter.setLocationY(screenHeight - shooter.getHeight());
        remainingLives = shooter.getLives();
        scoreBoard.addHearts(remainingLives);
        sounds.loadShootingApple();
        sounds.loadShootingBomb();
        sounds.loadShootingHeart();
    }

    private void setPanelCharacteristics() {
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setDoubleBuffered(true);
        addKeyListener(keyListener);
        setFocusable(true);
        setLayout(new BorderLayout());
    }

    public void addApples() {
        Random random = new Random();
        Apple apple;
        if (!keyListener.isPause()) {
            apple = new Apple(screenWidth, screenHeight);
            apple.setSize(70, 70);
            int randomX = random.nextInt((screenWidth - apple.getWidth()));
            apple.setLocationX(randomX);
            apple.setVelocity(4);
            apples.add(apple);
        }
    }

    public void addBombs() {
        Random random = new Random();
        Bomb bomb;
        if (!keyListener.isPause()) {
            bomb = new Bomb(screenWidth, screenHeight);
            bomb.setSize(50, 50);
            int randomX = random.nextInt((screenWidth - bomb.getWidth()));
            bomb.setLocationX(randomX);
            bomb.setVelocity(4);
            bombs.add(bomb);
        }
    }

    public void addHearts(){
        Random random = new Random();
        Heart heart;
        if (!keyListener.isPause()){
            heart = new Heart(screenWidth,screenHeight);
            heart.setSize(70,70);
            int randomX = random.nextInt((screenWidth - heart.getWidth()));
            heart.setLocationX(randomX);
            heart.setVelocity(4);
            hearts.add(heart);
        }
    }

    private void shootingTask() {
        if (keyListener.isShoot()) {
            if (shotTime == 0) {
                bullet = new Bullets(shooter.getLocationX(), shooter.getLocationY(), 15, 15, shooter.getWidth(), shooter.getHeight());
                bullets.add(bullet);
            }
            shotTime++;
            if (shotTime == 10) {
                shotTime = 0;
            }
        } else {
            shotTime = 0;
        }
    }

    public synchronized void update() throws IOException {
        if (!keyListener.isPause()) {
            // update shooter
            shooter.update();
            // update apples
            for (int i = 0; i < apples.size(); i++) {
                Apple apple = apples.get(i);
                apple.update();
                if (apple.getIsRemoved()) {
                    apples.remove(i);
                    i--;
                }
            }
            // update bombs
            for (int i = 0; i < bombs.size(); i++) {
                Bomb bomb = bombs.get(i);
                bomb.update();
                if (bomb.getIsRemoved()) {
                    bombs.remove(i);
                    i--;
                }
            }
            // update explosions
            for (int i = 0; i < explosions.size(); i++) {
                Explosion explosion = explosions.get(i);
                explosion.update();
                if (explosion.isRemoved()) {
                    explosions.remove(i);
                    i--;
                }
            }
            // update bullets
            for (int i = 0; i < bullets.size(); i++) {
                bullet = bullets.get(i);
                bullet.update();
                if (bullet.isRemoved()) {
                    bullets.remove(i);
                    i--;
                }
            }
            //update hearts
            for(int i = 0; i < hearts.size(); i++){
                Heart heart = hearts.get(i);
                heart.update();
                if(heart.getIsRemoved()) {
                    hearts.remove(i);
                    i--;
                }
            }
            checkCollisions();
        }
    }

    public void checkCollisions() {
        for (int i = 0; i < apples.size(); i++) {
            Apple apple = apples.get(i);
            for (int j = 0; j < bullets.size(); j++) {
                Bullets bullet = bullets.get(j);
                if (apple != null && bullet != null && apple.getBoundingBox().intersects(bullet.getBoundingBox())) {
                    Explosion explosion = new Explosion(apple.getX(), apple.getY(), apple.getWidth() / 2, 50);
                    explosions.add(explosion);
                    handleCollisionBullet(apple, bullet);
                }
                for (int k = 0; k < bombs.size(); k++) {
                    Bomb bomb = bombs.get(k);
                    if (bomb != null && bullet != null && (bomb.getBoundingBox().intersects(bullet.getBoundingBox()))) {
                        Explosion explosion = new Explosion(bomb.getX(), bomb.getY(), bomb.getWidth() / 2, 50);
                        explosions.add(explosion);
                        handleCollisionBomb(bomb, explosion);
                    }
                }
                for (int l=0;l < hearts.size(); l++){
                    Heart heart = hearts.get(l);
                    if (bullet != null && heart != null && heart.getBoundingBox().intersects(bullet.getBoundingBox())){
                        handleCollisionBulletHeart(heart, bullet);
                    }
                }
            }
            if (apple != null && shooter != null && apple.getBoundingBox().intersects(shooter.getBoundingBox())) {
                Explosion explosion = new Explosion(apple.getX(), apple.getY(), apple.getWidth() / 2, 50);
                explosions.add(explosion);
                handleCollisionShooter(apple);
            }
        }
        for (int k = 0; k < bombs.size(); k++) {
            Bomb bomb = bombs.get(k);
            if (bomb != null && shooter != null && bomb.getBoundingBox().intersects(shooter.getBoundingBox())) {
                Explosion explosion = new Explosion(bomb.getX(), bomb.getY(), bomb.getWidth() / 2, 50);
                explosions.add(explosion);
                handleCollisionBomb(bomb, explosion);
            }
        }
    }

    private void handleCollisionBulletHeart(Heart heart, Bullets bullet) {
        scoreBoard.addMoreHearts();
        remainingLives++;
        shooter.setLives(remainingLives);
        hearts.remove(heart);
        bullets.remove(bullet);
        if(!keyListener.isMute()){
            sounds.playShootingHeart();
        }
    }

    private void handleCollisionShooter(Apple apple) {
        if (remainingLives > 0) {
            remainingLives = shooter.getLives() - 1;
            shooter.setLives(remainingLives);
            scoreBoard.updateHearts(remainingLives);
            if(!keyListener.isMute()) {
                sounds.playShootingBomb();
            }
            apples.remove(apple);
        } else {
            stop();
        }
    }

    public void handleCollisionBullet(Apple apple, Bullets bullet) {
        apples.remove(apple);
        bullets.remove(bullet);
        score += 100;
        if(!keyListener.isMute()) {
            sounds.playShootingApple();
        }
        if (applePeriod > 500) {
            applePeriod = applePeriod - 25;
            if (appleExecutor != null) {
                appleExecutor.cancel(false);
                appleExecutor = executor.scheduleAtFixedRate(this::addApples, 100, applePeriod, TimeUnit.MILLISECONDS);
            }
        } else {
            applePeriod = 1000;
        }
        scoreBoard.setScore(score);
    }

    public void handleCollisionBomb(Bomb bomb, Explosion explosion) {
        if (remainingLives > 0) {
            remainingLives = shooter.getLives() - 1;
            shooter.setLives(remainingLives);
            scoreBoard.updateHearts(remainingLives);
            if(!keyListener.isMute()) {
                sounds.playShootingBomb();
            }
            explosion.setBomb(true);
            bombs.remove(bomb);
        } else {
            stop();
        }
    }

    public void stop() {
        executor.shutdownNow();
        try {
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public void restart() {
        // Reset game state
        score = 0;
        remainingLives = 3; // Or however many lives the player starts with
        applePeriod = 3000;
        shooter.setLives(remainingLives);
        apples.clear();
        bombs.clear();
        bullets.clear();
        hearts.clear();
        explosions.clear();
        scoreBoard.getHearts().clear();
        scoreBoard.addHearts(remainingLives);
        scoreBoard.setScore(score);
        keyListener.setPause(false);
        // Restart executor service
        stop();
        // Start the game again
        executor = Executors.newScheduledThreadPool(3);
        executor.schedule(this::start,2000, TimeUnit.MILLISECONDS);
        // Redraw game panel
        repaint();
    }

    public void gameState(Graphics2D g) {
        //Draw game over or game paused
        g.setStroke(new BasicStroke(3));
        g.setColor(new Color(36, 36, 36));
        g.fillRect(screenWidth/2 - 200, screenHeight/2 - 70, 400, 200);
//        Image gameOver = new ImageIcon(Objects.requireNonNull(getClass().getResource("gameOver2.png"))).getImage();
        String gameState = "";
        String resume = "";
        if (keyListener.isPause()) {
            gameState = "PAUSED";
            resume = "ESC to resume";
        } else if (remainingLives == 0) {
            gameState = "GAME OVER";
//            g.drawImage(gameOver, (screenWidth/2 - 150), (screenHeight/2  - 200),300,300,null);
        }
        g.setColor(Color.WHITE);
        g.setFont(new Font("Public Pixel", Font.PLAIN, 35));
        FontMetrics fm = g.getFontMetrics();
        int xCord = (screenWidth / 2 - fm.stringWidth(gameState) / 2);
        int yCord = (screenHeight / 2 + fm.getAscent() / 2);
        g.drawString(gameState, xCord, yCord);
        // Draw restart & resume
        String restart = "R to start over";
        g.setColor(Color.WHITE);
        g.setFont(new Font("Public Pixel", Font.PLAIN, 15));
        FontMetrics restartFm = g.getFontMetrics();
        int restartXCord = (screenWidth /2 - restartFm.stringWidth(restart) / 2);
        int resumeXCord = (screenWidth /2 - restartFm.stringWidth(resume) / 2);
        g.drawString(resume, resumeXCord, (yCord + restartFm.getAscent() + 20));
        g.drawString(restart, restartXCord, (yCord + restartFm.getAscent() + 50));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if (keyListener.isPause() || remainingLives == 0) {
            g2d.setPaint(new Color(200, 200, 200));
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            gameState(g2d);
        } else {
            g2d.drawImage(backgroundImage, 0,0,screenWidth,screenHeight,null);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            shooter.drawImage(g2d, shooter.getWidth(), shooter.getHeight());
            for (int i = 0; i < apples.size(); i++) {
                Apple apple = apples.get(i);
                if (apple != null) {
                    apple.drawImage(g2d);
                }
            }
            for (int i = 0; i < bombs.size(); i++) {
                Bomb bomb = bombs.get(i);
                if (bomb != null) {
                    bomb.drawImage(g2d);
                }
            }
            for (int i = 0; i < bullets.size(); i++) {
                Bullets bullet = bullets.get(i);
                if (bullet != null) {
                    bullet.drawBullet(g2d);
                }
            }
            for (int i = 0; i < explosions.size(); i++) {
                Explosion explosion = explosions.get(i);
                if (explosion != null) {
                    explosion.drawImage(g2d, 50, 50);
                }
            }
            for (int i = 0; i < hearts.size(); i++) {
                Heart heart = hearts.get(i);
                if (heart != null) {
                    heart.drawImage(g2d);
                }
            }
        }
        g2d.dispose();
    }
}
