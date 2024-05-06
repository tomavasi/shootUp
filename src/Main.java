import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Main {

    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            System.setProperty("sun.java2d.opengl", "True");
            try {
                new MainFrame();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}