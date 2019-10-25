package eg.edu.alexu.csd.oop.draw;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Map;

public class Circle extends Shapes {


    private int curserX;
    private int curserY;
    private innerCircle circle;
    private int centerX;
    private int centerY;
    private double width, height, minx, miny;
   Circle(){
       new innerCircle();
   }
    private class innerCircle  extends JPanel{

        int r;
          innerCircle(Point center , int r){
                minx = center.x - r;
                miny = center.y - r;
                width = height = 2 * r;
            }
        innerCircle(){
            drawCircle();
        }
        innerCircle(Point center, Point point) {
            r= (int)Math.abs(Math.sqrt(Math.pow((center.x - point.x), 2) + Math.pow((center.y - point.y), 2)));
            minx = center.x - r;
            miny = center.y - r;
            width = height = 2 * r;
        }
        public void paint(Graphics g) {
            g.drawOval((int) minx, (int) miny - 25, (int) width, (int) height);
        }
    }

    private void drawCircle() {
        MainWindow.mouseMotionListener = new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (circle != null) {
                    MainWindow.mainFrame.remove(circle);
                    MainWindow.mainFrame.setVisible(true);
                    MainWindow.mainFrame.repaint();
                }
                curserX = e.getX();
                curserY = e.getY();
                circle = new innerCircle(new Point(centerX, centerY - 25), new Point(curserX, curserY - 25));
                MainWindow.mainFrame.add(circle);
                MainWindow.mainFrame.setVisible(true);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        };
        MainWindow.mouseListener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                centerX = e.getX();
                centerY = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                circle = new innerCircle(new Point(centerX, centerY - 25), new Point(curserX, curserY - 25));
                MainWindow.mainFrame.add(circle);
                MainWindow.mainFrame.removeMouseMotionListener(MainWindow.mouseMotionListener);
                MainWindow.mainFrame.removeMouseListener(MainWindow.mouseListener);
            }
        };
        MainWindow.mainFrame.addMouseListener(MainWindow.mouseListener);
        MainWindow.mainFrame.addMouseMotionListener(MainWindow.mouseMotionListener);
    }

    @Override
    public void setPosition(Point position) {
        int x=circle.r;
        MainWindow.mainFrame.remove(circle);
        MainWindow.mainFrame.add(new innerCircle(position,x));
    }

    @Override
    public Point getPosition() {
        return null;
    }

    @Override
    public void setProperties(Map<String, Double> properties) {

    }

    @Override
    public Map<String, Double> getProperties() {

        return null;
    }

    @Override
    public void setColor(Color color) {

    }

    @Override
    public Color getColor() {
        return null;
    }

    @Override
    public void setFillColor(Color color) {

    }

    @Override
    public Color getFillColor() {
        return null;
    }

    @Override
    public void draw(Graphics canvas) {

    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return null;
    }
}
