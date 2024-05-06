import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Test {

    public static void main(String[] args) {
        new Test();
    }

    public Test() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // If the assets fail to load, there's no point
                    // in doing anything else
                    Asset.INSTANCE.prepare();
                    // Decouple the model/state from the UI
                    Model model = new Model();
                    RenderPane renderPane = new RenderPane(model);
                    JFrame frame = new JFrame("Test");
                    frame.add(renderPane);
                    frame.pack();
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);

                    renderPane.start();
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, "Failed to load required assets", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public class RenderPane extends JPanel {
        private Timer timer;
        private Model model;

        public RenderPane(Model model) {
            this.model = model;
            timer = new Timer(5, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // This now allows the panel to be dynamically
                    // resizable, although if you make the window
                    // smaller, the apples which are now outside
                    // the viewable range will still be updated,
                    // but Swing's pretty well optimised for that
                    model.update(getSize());
                    repaint();
                }
            });
        }

        public void start() {
            timer.start();
        }

        public void stop() {
            timer.stop();
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(800, 600);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            List<Apple> apples = model.getApples();
            for (Apple apple : apples) {
                apple.paint(g2d);
            }
            String text = Integer.toString(apples.size());
            FontMetrics fm = g2d.getFontMetrics();
            g2d.setColor(Color.RED);
            g2d.drawString(text, getWidth() - fm.stringWidth(text) - 8 , getHeight() - fm.getHeight() + fm.getAscent() - 8);
            g2d.dispose();
        }
    }

    public class Model {
        private List<Apple> apples;
        private Instant lastAppleUpdate;

        public Model() {
            apples = new ArrayList<>(128);
        }

        public List<Apple> getApples() {
            return Collections.unmodifiableList(apples);
        }

        public void update(Dimension size) {
            // This will add a new apple approximately every
            // two seconds
            if (lastAppleUpdate == null || Duration.between(lastAppleUpdate, Instant.now()).getSeconds() >= 2) {
                apples.add(new Apple());
                lastAppleUpdate = Instant.now();
            }

            for (int index = apples.size() - 1; index >= 0; index--) {
                Apple apple = apples.get(index);
                apple.update(size);
                if (apple.isRemoved) {
                    apples.remove(index);
                }
            }
        }
    }

    // This is just an example, you don't need to do this,
    // but you might consider building some kind of asset
    // management system if you're dealing with multiple
    // assets
    enum Asset {
        INSTANCE;

        private BufferedImage apple;

        private Asset() {
        }

        public void prepare() throws IOException {

            apple = ImageUtilities.getScaledInstanceToFit(
                    ImageIO.read(new File("C:\\Users\\Gebruiker\\IdeaProjects\\AppleCatcher\\src\\apple.png")),
                    new Dimension(48, 48)
            );
        }

        public BufferedImage getApple() {
            return apple;
        }
    }

    // This demonstrates a "better quality" scaling
    // approach, as apposed to using `getScaledInstance`
    public class ImageUtilities {

        public static BufferedImage getScaledInstanceToFit(BufferedImage img, Dimension size) {
            float scaleFactor = getScaleFactorToFit(img, size);
            return getScaledInstance(img, scaleFactor);
        }

        protected static float getScaleFactorToFit(BufferedImage img, Dimension size) {
            float scale = 1f;
            if (img != null) {
                int imageWidth = img.getWidth();
                int imageHeight = img.getHeight();
                scale = getScaleFactorToFit(new Dimension(imageWidth, imageHeight), size);
            }
            return scale;
        }

        protected static float getScaleFactorToFit(Dimension original, Dimension toFit) {
            float scale = 1f;
            if (original != null && toFit != null) {
                float dScaleWidth = getScaleFactor(original.width, toFit.width);
                float dScaleHeight = getScaleFactor(original.height, toFit.height);
                scale = Math.min(dScaleHeight, dScaleWidth);
            }
            return scale;
        }

        protected static float getScaleFactor(int iMasterSize, int iTargetSize) {
            float scale = 1;
            if (iMasterSize > iTargetSize) {
                scale = (float) iTargetSize / (float) iMasterSize;
            } else {
                scale = (float) iTargetSize / (float) iMasterSize;
            }
            return scale;
        }

        public static BufferedImage getScaledInstance(BufferedImage img, double dScaleFactor) {
            BufferedImage imgBuffer = null;
            imgBuffer = getScaledInstance(img, dScaleFactor, RenderingHints.VALUE_INTERPOLATION_BILINEAR, true);
            return imgBuffer;
        }

        protected static BufferedImage getScaledInstance(BufferedImage img, double dScaleFactor, Object hint, boolean higherQuality) {

            int targetWidth = (int) Math.round(img.getWidth() * dScaleFactor);
            int targetHeight = (int) Math.round(img.getHeight() * dScaleFactor);

            int type = (img.getTransparency() == Transparency.OPAQUE)
                    ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;

            BufferedImage ret = (BufferedImage) img;

            if (targetHeight > 0 || targetWidth > 0) {
                int w, h;
                if (higherQuality) {
                    w = img.getWidth();
                    h = img.getHeight();
                } else {
                    w = targetWidth;
                    h = targetHeight;
                }

                do {
                    if (higherQuality && w > targetWidth) {
                        w /= 2;
                        if (w < targetWidth) {
                            w = targetWidth;
                        }
                    }

                    if (higherQuality && h > targetHeight) {
                        h /= 2;
                        if (h < targetHeight) {
                            h = targetHeight;
                        }
                    }

                    BufferedImage tmp = new BufferedImage(Math.max(w, 1), Math.max(h, 1), type);
                    Graphics2D g2 = tmp.createGraphics();
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
                    g2.drawImage(ret, 0, 0, w, h, null);
                    g2.dispose();

                    ret = tmp;
                } while (w != targetWidth || h != targetHeight);
            } else {
                ret = new BufferedImage(1, 1, type);
            }
            return ret;
        }
    }

    public class Apple {
        private static Random RANDOM = new Random();

        private int yVelocity = 1;
        private boolean isRemoved = false;
        private Rectangle bounds;

        public Apple() {
            // If you need the apple to be a different size
            // for each entity, then I would create a copy
            // of the image from the `Assets` here, scale
            // it as desired and assign it a property.
        }

        public Rectangle getBounds() {
            return bounds;
        }

        public void setVelocity(int yVelocity) {
            this.yVelocity = yVelocity;
        }

        public void paint(Graphics2D g) {
            if (bounds == null) {
                return;
            }
            Graphics g2D = (Graphics2D) g.create();
            g2D.drawImage(Asset.INSTANCE.getApple(), bounds.x, bounds.y, null);
            g2D.dispose();
        }

        public boolean getIsRemoved() {
            return isRemoved;
        }

        public void update(Dimension size) {
            if (bounds == null) {
                BufferedImage apple = Asset.INSTANCE.getApple();
                this.bounds = new Rectangle(RANDOM.nextInt(size.width - apple.getWidth()), -apple.getHeight(), apple.getWidth(), apple.getHeight());
            }
            if (bounds.getY() >= size.height) {
                this.isRemoved = true;
            }
            bounds.setLocation(bounds.x, bounds.y + yVelocity);
        }

    }
}
