package canvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class myPanel extends JPanel {
    public int x0, y0, x1, y1;
    private Vector points = new Vector();
    private ServerSocket s;
    private Socket ss;
    private ObjectOutputStream output;
    private OutputStream os;
    private BufferedOutputStream b;
    private Graphics g;

    public myPanel() {
        setBounds(0, 0, 400, 300);
        setBackground(Color.white);

        addMouseMotionListener(new mouseAction());
        addMouseListener(new mousemovpress());

        this.validate();
        new myThread().start();
    }

    class mouseAction extends MouseMotionAdapter {
        public void mouseDragged(MouseEvent e) {
            g = getGraphics();
            g.setColor(Color.red);
            x1 = e.getX();
            y1 = e.getY();
            points.add(new Point(x1, y1));
            g.drawLine(x0, y0, x1, y1);
            x0 = x1;
            y0 = y1;
        }
    }

    class mousemovpress extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            x0 = e.getX();
            y0 = e.getY();
            points.add(new Point(x0, y0));
        }

        public void mouseReleased(MouseEvent e) {
            try {
                os = ss.getOutputStream();
                b = new BufferedOutputStream(os);
                output = new ObjectOutputStream(b);
                output.writeObject(points);
                output.flush();
                points.clear();
            } catch (IOException ex) {
            }
        }
    }

    class myThread extends Thread {
        public void run() {
            try {
                s = new ServerSocket(8008);
                ss = s.accept();
            } catch (IOException ex) {
            }
        }
    }
}

class myFrame extends JFrame {
    myPanel p = new myPanel();

    public myFrame() {
        super("Whiteboard Server");
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
        new myFrame();
    }
}

