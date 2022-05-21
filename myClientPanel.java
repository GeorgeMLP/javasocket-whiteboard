package canvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Vector;

public class myClientPanel extends JPanel {
    private final JFrame f;
    private Vector points = new Vector();
    private Socket ss;
    private InputStream is;
    private BufferedInputStream b;
    private ObjectInputStream input;
    private Graphics g;

    public myClientPanel(JFrame f) {
        this.f = f;
        setBounds(0, 0, 400, 300);
        setBackground(Color.white);
        this.validate();
        new myThread().start();
        try {
            ss = new Socket("127.0.0.1", 8008);
        } catch (IOException ex) {
            f.setTitle("error");
        }
    }

    public void repaint(Graphics g) {
        int x0, y0, x1, y1;
        Point start, end;
        Enumeration allPoints = points.elements();
        start = (Point) allPoints.nextElement();
        x0 = (int) start.getX();
        y0 = (int) start.getY();
        while (allPoints.hasMoreElements()) {
            end = (Point) allPoints.nextElement();
            x1 = (int) end.getX();
            y1 = (int) end.getY();
            g.drawLine(x0, y0, x1, y1);
            x0 = x1;
            y0 = y1;
        }
    }

    class myThread extends Thread {
        public void run() {
            while (true) {
                try {
                    is = ss.getInputStream();
                    b = new BufferedInputStream(is);
                    input = new ObjectInputStream(b);
                    points = (Vector) input.readObject();
                    g = getGraphics();
                    g.setColor(Color.red);
                    repaint(g);
                } catch (Exception ex) {
                }
            }
        }
    }
}

class myClientFrame extends JFrame {
    myClientPanel p = new myClientPanel(this);

    public myClientFrame() {
        super("Whiteboard Client");
        setLayout(null);
        setBounds(100, 100, 400, 300);
        add(p);
        setVisible(true);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public static void main(String[] ss) {
        new myClientFrame();
    }
}
