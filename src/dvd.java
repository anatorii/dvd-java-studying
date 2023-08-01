import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class dvd {
    public static void main(String[] args) throws IOException {
        new MainWindow();
    }
}

class MainWindow extends JFrame {

    MainPanel panel;

    public MainWindow () throws IOException {
        super("dvd");

        panel = new MainPanel();
        panel.addComponentListener(new MainPanelListener());

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setPreferredSize(new Dimension(800, 600));

        this.setLocation(100, 60);

        this.pack();

        this.setVisible(true);

        this.add(panel);

        Timer timer = new Timer(100, new PaintPanelListener(panel));
        timer.start();
    }
}

class MainPanel extends JPanel {
    BufferedImage image;
    boolean init;
    int x, y, dx, dy;
    int directionX, directionY;
    Color color;
    Color[] cArray;

    public MainPanel() {
        super();

        init = false;

        cArray = new Color[]{Color.YELLOW, Color.BLUE, Color.CYAN, Color.GRAY, Color.GREEN, Color.MAGENTA, Color.RED, Color.ORANGE};

        color = getRandomColor();

        try {
            image = ImageIO.read(new File("dvd.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected Color getRandomColor() {
        return cArray[(int)(Math.random() * cArray.length)];
    }

    public void initDimensions() {
        x = 100; y = 100;
        dx = getWidth() / 40;
        dy = getHeight() / 40;
        directionX = 1; directionY = 1;
        init = true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!init) {
            initDimensions();
        }

        x += dx * directionX; y += dy * directionY;

        buildPicture(g, x, y);
    }

    public void repaintImage() {
        repaint();
    }

    protected void buildPicture(Graphics g, int x, int y) {
        g.setColor(color);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, x, getHeight());
        g.fillRect(image.getWidth() + x, 0, getWidth(), getHeight());
        g.fillRect(0, 0, getWidth(), y);
        g.fillRect(0, image.getHeight() + y, getWidth(), getHeight());

        g.drawImage(image, x, y, this);
    }
}

class PaintPanelListener implements ActionListener {
    JPanel panel;

    public PaintPanelListener(JPanel panel) {
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ((MainPanel) panel).repaintImage();
    }
}

class MainPanelListener extends ComponentAdapter {
    @Override
    public void componentResized(ComponentEvent e) {
        super.componentResized(e);
        ((MainPanel) e.getComponent()).init = false;
    }
}
