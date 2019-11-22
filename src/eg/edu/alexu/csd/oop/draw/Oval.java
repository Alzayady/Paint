package eg.edu.alexu.csd.oop.draw;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.Map;

import static eg.edu.alexu.csd.oop.draw.MainWindow.mainFrame;
import static eg.edu.alexu.csd.oop.draw.MainWindow.shapes;

public class Oval extends JPanel implements Shape {
    private Point point1 = new Point(0, 0);
    private Point point2 = new Point(0, 0);
    private Point center = new Point(0, 0);
    private Oval oval;
    private Oval O = this;
    private int height = 0;
    private int width = 0;
    private int selectedPoint = 0;
    private Map<String, Double> properties = new HashMap<>();
    /*belongs to the change color menu
     */
    private Color fillColor;
    private Color fontColor;

    Oval() {

        this.DrawOval();
    }

    int min(int a, int b) {
        if (a < b) return a;
        return b;
    }

    int max(int a, int b) {
        if (a > b) return a;
        return b;
    }

    Oval(Point point1, Point point2, Color fillColor, Color fontColor) {
        this.fontColor = fontColor;
        this.fillColor = fillColor;
        this.point1.x = min(point1.x, point2.x);
        this.point1.y = min(point1.y, point2.y);
        this.point2.x = max(point1.x, point2.x);
        this.point2.y = max(point1.y, point2.y);
        height = Math.abs(point1.y - point2.y);
        width = Math.abs(point1.x - point2.x);
    }

    Oval(Point point1, Point point2) {
        this(point1, point2, new Color(0, 0, 0, 0), Color.BLACK);
    }

    public void paint(Graphics g) {
        if (fontColor != null) g.setColor(fontColor);
        g.drawOval(point1.x - 6, point1.y - 52, width, height);
        if (fillColor != null) {
            g.setColor(fillColor);
            g.fillOval(point1.x - 6 + 1, point1.y - 52 + 1, width - 2, height - 2);
        }
        mainFrame.setVisible(true);
        mainFrame.repaint();
    }

    private void DrawOval() {
         boolean[] change = {false};
        MainWindow.mouseMotionListener = new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (oval != null) {
                    MainWindow.mainFrame.remove(oval);
                    MainWindow.mainFrame.setVisible(true);
                    MainWindow.mainFrame.repaint();
                }
                change[0] =true;
                point2 = e.getPoint();
                oval = new Oval(point1, point2, fillColor, fontColor);
                MainWindow.mainFrame.add(oval);
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
                point1 = e.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                MainWindow.mainFrame.removeMouseMotionListener(MainWindow.mouseMotionListener);
                MainWindow.mainFrame.removeMouseListener(MainWindow.mouseListener);
                point2 = e.getPoint();
                CALCDETAILS();
                shapes.add(O);
               if(change[0]) MainWindow.saveToLog();
            }
        };
        MainWindow.mainFrame.addMouseListener(MainWindow.mouseListener);
        MainWindow.mainFrame.addMouseMotionListener(MainWindow.mouseMotionListener);
    }

    private void CALCDETAILS() {
        center.x = max(point1.x, point2.x) - min(point1.x, point2.x);
        center.x /= 2;
        center.x += min(point1.x, point2.x);
        center.y = max(point1.y, point2.y) - min(point1.y, point2.y);
        center.y /= 2;
        center.y += min(point1.y, point2.y);
    }

    public Shape getShape() {
        return oval;
    }

    public boolean coversPoint(Point point) {
        Point pointtemp1 = new Point(Math.abs(point1.x - point2.x) / 2 + min(point1.x, point2.x), min(point1.y, point2.y));
        Point pointtemp2 = new Point(Math.abs(point1.x - point2.x) / 2 + min(point1.x, point2.x), max(point1.y, point2.y));
        Point pointtemp3 = new Point(min(point1.x, point2.x), Math.abs(point1.y - point2.y) / 2 + min(point1.y, point2.y));
        Point pointtemp4 = new Point(max(point1.x, point2.x), Math.abs(point1.y - point2.y) / 2 + min(point1.y, point2.y));
        if (isPoint(point, pointtemp1)) {
            //top
            selectedPoint = 1;
            return true;
        }
        if (isPoint(point, pointtemp2)) {
            //down
            selectedPoint = 2;
            return true;
        }
        if (isPoint(point, pointtemp3)) {
            //left
            selectedPoint = 3;
            return true;
        }
        if (isPoint(point, pointtemp4)) {
            //right;
            selectedPoint = 4;
            return true;
        }
        return false;
    }

    private boolean isPoint(Point p1, Point p2) {
        return Math.abs(p1.x - p2.x) < 20 && Math.abs(p1.y - p2.y) < 10;
    }

    @Override
    public void setPosition(Point position) {
        int appendx = position.x - center.x;
        point1.x += appendx;
        point2.x += appendx;
        int appendy = position.y - center.y;
        point1.y += appendy;
        point2.y += appendy;
        center = position;
        mainFrame.remove(oval);
        oval = new Oval(point1, point2, fillColor, fontColor);
        mainFrame.add(oval);
        MainWindow.mainFrame.setVisible(true);
        MainWindow.mainFrame.repaint();
        MainWindow.saveToLog();
    }

    @Override
    public Point getPosition() {
        return center;
    }

    @Override
    public void setProperties(Map<String, Double> properties) {
        this.properties = properties;
        if (properties.containsKey("headX")) {
            double x = properties.get("headX");
            point1.x = (int) x;
        }
        if (properties.containsKey("headY")) {
            double x = properties.get("headY");
            point1.y = (int) x;
        }
        if (properties.containsKey("courserX")) {
            double x = properties.get("courserX");
            point2.x = (int) x;
        }
        if (properties.containsKey("courserY")) {
            double x = properties.get("courserY");
            point2.y = (int) x;
        }
        mainFrame.remove(oval);
        oval = new Oval(point1, point2);
        mainFrame.add(oval);
        MainWindow.mainFrame.setVisible(true);
        MainWindow.mainFrame.repaint();

    }

    @Override
    public Map<String, Double> getProperties() {
        properties = new HashMap<>();
        properties.put("headX", (double) point1.x);
        properties.put("headY", (double) point1.y);
        properties.put("courserX", (double) point2.x);
        properties.put("courserY", (double) point2.y);
        return properties;
    }

    @Override
    public void setColor(Color color) {
        this.fontColor = color;
        mainFrame.remove(oval);
        oval = new Oval(point1, point2, fillColor, fontColor);
        mainFrame.add(oval);
        mainFrame.setVisible(true);
        mainFrame.repaint();
        MainWindow.saveToLog();
    }

    @Override
    public Color getColor() {
        return this.fontColor;
    }

    @Override
    public void setFillColor(Color color) {
        this.fillColor = color;
        if (oval != null){
            mainFrame.remove(oval);
        }
        oval = new Oval(point1, point2, fillColor, fontColor);
        mainFrame.add(oval);
        mainFrame.setVisible(true);
        mainFrame.repaint();
    }

    @Override
    public Color getFillColor() {
        return this.fillColor;
    }

    public void resize() {
        MainWindow.mouseListener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                MainWindow.mainFrame.removeMouseMotionListener(MainWindow.mouseMotionListener);
                MainWindow.mainFrame.removeMouseListener(MainWindow.mouseListener);
                CALCDETAILS();
                MainWindow.saveToLog();
            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
            }
        };
        MainWindow.mouseMotionListener = new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectedPoint == 1 || selectedPoint == 2) {
                    int y = Math.abs(e.getY() - center.y);
                    point1.y = center.y - y;
                    point2.y = center.y + y;
                    if (oval != null) mainFrame.remove(oval);
                    oval = new Oval(point1, point2, fillColor, fontColor);
                    mainFrame.add(oval);
                    MainWindow.mainFrame.setVisible(true);
                    MainWindow.mainFrame.repaint();
                } else {
                    int x = Math.abs(e.getX() - center.x);
                    point1.x = center.x - x;
                    point2.x = center.x + x;
                    mainFrame.remove(oval);
                    oval = new Oval(point1, point2, fillColor, fontColor);
                    mainFrame.add(oval);
                    MainWindow.mainFrame.setVisible(true);
                    MainWindow.mainFrame.repaint();
                }
            }

            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
            }
        };
        MainWindow.mainFrame.addMouseListener(MainWindow.mouseListener);
        MainWindow.mainFrame.addMouseMotionListener(MainWindow.mouseMotionListener);
    }

    @Override
    public void draw(Graphics canvas) {
        paint(canvas);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        if (oval != null) mainFrame.remove(oval);
        oval = new Oval(point1, point2, fillColor, fontColor);
        mainFrame.add(oval);
        CALCDETAILS();
        mainFrame.setVisible(true);
        mainFrame.repaint();
        return oval;
    }

    // returns another object of the same class not the same object as the original
    public Shape copy() {
        Shape newShape = new Oval(point1, point2, fillColor, fontColor);
        MainWindow.mainFrame.setVisible(true);
        MainWindow.mainFrame.repaint();
        return newShape;
    }

    public void remove() {
        mainFrame.remove(oval);
    }
}
