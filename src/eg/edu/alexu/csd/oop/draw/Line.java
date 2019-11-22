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

public class Line extends JPanel implements Shape {
    private Point point1 = new Point(0, 0);
    private Point point2 = new Point(0, 0);
    private final int PIXEL_ALLOWED_ERROR = 3;
    private final int infinity = 100000000;
    private Line line;
    private double slope;
    private double c;
    private Point center = new Point();
    private Line line1 = this;
    private Color color =Color.BLACK;
    private Color colorBACK= null;//new Color(0, 0, 0, 0);
    private Map<String, Double> properties = new HashMap<>();
    private boolean selectp2;

    private int min(int a, int b) {
        if (a < b) return a;
        return b;
    }

    private int max(int a, int b) {
        if (a > b) return a;
        return b;
    }

    Line() {
        this.DrawLine();
    }

    Line(Point point1, Point point2) {
        this.point1 = point1;
        this.point2 = point2;
    }

    Line(Point point1, Point point2, Color color, Color BG) {
        this.point1 = point1;
        this.point2 = point2;
        this.color = color;
        colorBACK = BG;
    }

    public void paint(Graphics g) {
       if(color!=null) g.setColor(color);
        g.drawLine(point1.x - 6, point1.y - 52, point2.x - 6, point2.y - 52);
    }

    private void DrawLine() {
        MainWindow.mouseMotionListener = new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (line != null) {
                    MainWindow.mainFrame.remove(line);
                    MainWindow.mainFrame.setVisible(true);
                    MainWindow.mainFrame.repaint();
                }
                point2 = e.getPoint();
                line = new Line(point1, point2, color, new Color(0, 0, 0, 0));
                MainWindow.mainFrame.add(line);
                MainWindow.mainFrame.setVisible(true);
                MainWindow.mainFrame.repaint();
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
                CALCDETAILS();
                shapes.add(line1);
                MainWindow.saveToLog();
                MainWindow.mainFrame.removeMouseMotionListener(MainWindow.mouseMotionListener);
                MainWindow.mainFrame.removeMouseListener(MainWindow.mouseListener);
            }
        };
        MainWindow.mainFrame.addMouseListener(MainWindow.mouseListener);
        MainWindow.mainFrame.addMouseMotionListener(MainWindow.mouseMotionListener);
    }

    private void CALCDETAILS() {
        center.x = (max(point1.x, point2.x) - min(point1.x, point2.x)) / 2 + min(point1.x, point2.x);
        center.y = (max(point1.y, point2.y) - min(point1.y, point2.y)) / 2 + min(point1.y, point2.y);
        if (point1.x == point2.x) {
            slope = infinity;
            if (point1.y > point2.y) {
                Point temp = point1;
                point1 = point2;
                point2 = temp;
            }
        } else slope = (double) (point1.y - point2.y) / (point1.x - point2.x);
        c = point1.y - slope * point1.x;
        properties.put("point1x", (double) point1.x);
        properties.put("point1y", (double) point1.y);
        properties.put("point2x", (double) point2.x);
        properties.put("point2y", (double) point2.y);
    }

    private boolean bounding(Point point) {
        if ((min(point1.x, point2.x) - point.x) <= PIXEL_ALLOWED_ERROR)
            if ((point.x - max(point1.x, point2.x)) <= PIXEL_ALLOWED_ERROR)
                if ((min(point1.y, point2.y) - point.y) <= PIXEL_ALLOWED_ERROR)
                    if ((point.y - max(point1.y, point2.y)) <= PIXEL_ALLOWED_ERROR)
                        return true;
        return false;
    }

    public boolean coversPoint(Point point) {
        if (slope == infinity)
            if (Math.abs(point.x - point1.x) <= PIXEL_ALLOWED_ERROR)
                return bounding(point);
        if (!bounding(point)) return false;
        double die = Math.abs(point.y - slope * point.x - c) / slope;
        System.out.println(die);
        return (die < 7);

    }

    @Override
    public void setPosition(Point position) {
        center.x = (max(point1.x, point2.x) - min(point1.x, point2.x)) / 2 + min(point1.x, point2.x);
        center.y = (max(point1.y, point2.y) - min(point1.y, point2.y)) / 2 + min(point1.y, point2.y);
        int appendx = position.x - center.x;
        int appendy = position.y - center.y;
        point1.x += appendx;
        point2.x += appendx;
        point1.y += appendy;
        point2.y += appendy;
        MainWindow.mainFrame.remove(line);
        MainWindow.mainFrame.setVisible(true);
        line = new Line(point1, point2, color, new Color(0, 0, 0, 0));
        MainWindow.mainFrame.add(line);
        CALCDETAILS();
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
        if (properties.containsKey("point1x")) {
            double x = properties.get("point1x");
            point1.x = (int) x;
        }
        if (properties.containsKey("point1y")) {
            double y = properties.get("point1y");
            point1.y = (int) y;
        }
        if (properties.containsKey("point2x")) {
            double x = properties.get("point2x");
            point2.x = (int) x;
        }
        if (properties.containsKey("point2y")) {
            double y = properties.get("point2y");
            point2.y = (int) y;
        }
        mainFrame.remove(line);
        line = new Line(point1, point2, color, new Color(0, 0, 0, 0));
        mainFrame.add(line);
        CALCDETAILS();
        mainFrame.setVisible(true);
        mainFrame.repaint();
    }


    @Override
    public Map<String, Double> getProperties() {
        properties = new HashMap<>();
        properties.put("point1x", (double) point1.x);
        properties.put("point1y", (double) point1.y);
        properties.put("point2x", (double) point2.x);
        properties.put("point2y", (double) point2.y);
        return properties;
    }

    @Override
    public void setColor(Color color) {
        if(color.getBlue()==255&&color.getGreen()==255&color.getRed()==255)return;
        this.color = color;
        mainFrame.remove(line);
        line = new Line(point1, point2, color, null);
        mainFrame.add(line);
        mainFrame.setVisible(true);
        mainFrame.repaint();
        MainWindow.saveToLog();
    }

    @Override
    public Color getColor() {

        return color;
    }

    @Override
    public void setFillColor(Color color) {
        this.colorBACK = color;
    }

    @Override
    public Color getFillColor() {
        return colorBACK;
    }

    public void remove() {
        MainWindow.mainFrame.remove(line);
        MainWindow.mainFrame.setVisible(true);
        MainWindow.mainFrame.repaint();
    }

    @Override
    public void draw(Graphics canvas) {
        paint(canvas);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        if (line != null) mainFrame.remove(line);
        line = new Line(point1, point2,color,new Color(0, 0, 0, 0));
        mainFrame.add(line);
        MainWindow.mainFrame.setVisible(true);
        MainWindow.mainFrame.repaint();
        CALCDETAILS();
        return line;
    }

    // returns another object of the same class not the same object as the original
    public Shape copy() {
        Shape newShape = new Line(point1, point2,color,colorBACK);
        MainWindow.mainFrame.setVisible(true);
        MainWindow.mainFrame.repaint();
        return newShape;
    }

    public void resize(Point p) {
        if (Math.pow(p.x - point1.x, 2) + Math.pow(p.y - point1.y, 2) < Math.pow(p.x - point2.x, 2) + Math.pow(p.y - point2.y, 2)) {
            selectp2 = true;
            selectp2();
        } else {
            selectp2 = false;
            selectp1();
        }
    }

    private void selectp2() {
         boolean[] change = {false};
        MainWindow.mouseListener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {

            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                CALCDETAILS();
                if (selectp2&& change[0]) MainWindow.saveToLog();
                mainFrame.removeMouseListener(MainWindow.mouseListener);
                mainFrame.removeMouseMotionListener(MainWindow.mouseMotionListener);
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
                if (line != null) {
                    MainWindow.mainFrame.remove(line);
                    MainWindow.mainFrame.setVisible(true);
                    MainWindow.mainFrame.repaint();
                }
                change[0] =true;
                point1 = e.getPoint();
                line = new Line(point1, point2, color, new Color(0, 0, 0, 0));
                MainWindow.mainFrame.add(line);
                MainWindow.mainFrame.setVisible(true);
                MainWindow.mainFrame.repaint();

            }

            @Override
            public void mouseMoved(MouseEvent mouseEvent) {

            }
        };
        MainWindow.mainFrame.addMouseListener(MainWindow.mouseListener);
        MainWindow.mainFrame.addMouseMotionListener(MainWindow.mouseMotionListener);

    }

    private void selectp1() {
         boolean[] change = {false};
        MainWindow.mouseListener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {

            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                CALCDETAILS();
                if (!selectp2&& change[0]) MainWindow.saveToLog();
                mainFrame.removeMouseListener(MainWindow.mouseListener);
                mainFrame.removeMouseMotionListener(MainWindow.mouseMotionListener);
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
                if (line != null) {
                    MainWindow.mainFrame.remove(line);
                    MainWindow.mainFrame.setVisible(true);
                    MainWindow.mainFrame.repaint();
                }
                change[0] =true;
                point2 = e.getPoint();
                line = new Line(point1, point2, color, new Color(0, 0, 0, 0));
                MainWindow.mainFrame.add(line);
                MainWindow.mainFrame.setVisible(true);
                MainWindow.mainFrame.repaint();

            }

            @Override
            public void mouseMoved(MouseEvent mouseEvent) {

            }
        };
        MainWindow.mainFrame.addMouseListener(MainWindow.mouseListener);
        MainWindow.mainFrame.addMouseMotionListener(MainWindow.mouseMotionListener);
    }
}