package eg.edu.alexu.csd.oop.draw;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.Map;

import static eg.edu.alexu.csd.oop.draw.MainWindow.*;

public class Triangle implements Shape {
    private inerLines l1, l2, l3;
    private Point p1, p2, p3;
    private final int infinty = 100000000;
    private final int PIXEL_ALLOWED_ERROR = 3;
    private double s1, s2, s3, c1, c2, c3;
    private Triangle r = this;
    private Color ccolor =Color.BLACK;
    private Color BBG ;
    private innerTraingle polygon;
    private Point center = new Point();
    private  Map<String, Double> properties=new HashMap<>();
    private boolean voidFirst=true;
    Triangle() {
        this.DrowTringe();
    }

    public Triangle(Point p1, Point p2, Point p3, Color ccolor, Color BBG) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.ccolor = ccolor;
        this.BBG = BBG;
        polygon=new innerTraingle(p1,p2,p3,ccolor,BBG);
    }
    public Triangle(Point p1, Point p2, Point p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        polygon=new innerTraingle(p1,p2,p3,ccolor,BBG);
    }


    private int min(int a, int b) { if (a < b) return a;return b; }

    private int max(int a, int b) { if (a > b) return a;return b; }

    private void DrowTringe() {
        MainWindow.mouseMotionListener = new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (l1 != null) {
                    MainWindow.mainFrame.remove(l1);
                }
                if (l2 != null) {
                    MainWindow.mainFrame.remove(l2);
                }
                if (l3 != null) {
                    MainWindow.mainFrame.remove(l3);
                }
                MainWindow.mainFrame.repaint();
                MainWindow.mainFrame.setVisible(true);
                MainWindow.mainFrame.removeMouseListener(MainWindow.mouseListener);
                MainWindow.mainFrame.removeMouseMotionListener(MainWindow.mouseMotionListener);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (l2 != null) {
                    MainWindow.mainFrame.remove(l2);
                    MainWindow.mainFrame.repaint();
                    MainWindow.mainFrame.setVisible(true);
                }
                if (l3 != null) {
                    MainWindow.mainFrame.remove(l3);
                    MainWindow.mainFrame.repaint();
                    MainWindow.mainFrame.setVisible(true);
                }
                if (p1 != null) {
                    l2 = new inerLines(p1, e.getPoint());
                    MainWindow.mainFrame.add(l2);
                    MainWindow.mainFrame.repaint();
                    MainWindow.mainFrame.setVisible(true);
                }
                if (p2 != null) {
                    l3 = new inerLines(p2, e.getPoint());
                    MainWindow.mainFrame.add(l3);
                    MainWindow.mainFrame.repaint();
                    MainWindow.mainFrame.setVisible(true);
                }
            }
        };
        MainWindow.mouseListener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) { }

            @Override
            public void mouseEntered(MouseEvent e) { }

            @Override
            public void mouseExited(MouseEvent e) { }

            @Override
            public void mousePressed(MouseEvent e) {
                if (p1 == null) {
                    p1 = e.getPoint();
                } else if (p2 == null) {
                    if (l2 != null) MainWindow.mainFrame.remove(l2);
                    l1 = new inerLines(p1, e.getPoint());
                    MainWindow.mainFrame.add(l1);
                    p2 = e.getPoint();
                    MainWindow.mainFrame.repaint();
                    MainWindow.mainFrame.setVisible(true);
                } else {
                    if (l2 != null) MainWindow.mainFrame.remove(l2);
                    if (l3 != null) MainWindow.mainFrame.remove(l3);
                    if (l1 != null) MainWindow.mainFrame.remove(l1);
                    p3 = e.getPoint();
                    polygon = new innerTraingle(p1, p2, p3);
                    mainFrame.add(polygon);
                    MainWindow.mainFrame.setVisible(true);
                    MainWindow.mainFrame.repaint();
                    CURRENTDETALIS();
                    shapes.add(r);
                    MainWindow.mainFrame.removeMouseListener(MainWindow.mouseListener);
                    MainWindow.mainFrame.removeMouseMotionListener(MainWindow.mouseMotionListener);
                    saveToLog();

                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }
        };
        MainWindow.mainFrame.addMouseListener(MainWindow.mouseListener);
        MainWindow.mainFrame.addMouseMotionListener(MainWindow.mouseMotionListener);
    }

    private void CURRENTDETALIS() {
        if (p1.x == p2.x) s1 = infinty;
        else {
            s1 = (double) (p1.y - p2.y) / (p1.x - p2.x);
            c1 = p1.y - s1 * p1.x;
        }
        if (p1.x == p3.x) s2 = infinty;
        else {
            s2 = (double) (p3.y - p1.y) / (p3.x - p1.x);
            c2 = p1.y - s2 * p1.x;
        }
        if (p3.x == p2.x) s3 = infinty;
        else {
            s3 = (double) (p3.y - p2.y) / (p3.x - p2.x);
            c3 = p3.y - s3 * p3.x;
        }
        center.x = (p1.x + p2.x + p3.x) / 3;
        center.y = (p1.y + p2.y + p3.y) / 3;
        mainFrame.removeMouseListener(mouseListener);
        mainFrame.removeMouseMotionListener(mouseMotionListener);

    }

    private class innerTraingle extends JPanel {
        int[] x = new int[3];
        int[] y = new int[3];

        innerTraingle(Point point1, Point point2, Point point3) {
            x[0] = point1.x - 6;
            x[1] = point2.x - 6;
            x[2] = point3.x - 6;
            y[0] = point1.y - 52;
            y[1] = point2.y - 52;
            y[2] = point3.y - 52;

        }

        innerTraingle(Point point1, Point point2, Point point3, Color color, Color BG) {
            x[0] = point1.x - 6;
            x[1] = point2.x - 6;
            x[2] = point3.x - 6;
            y[0] = point1.y - 52;
            y[1] = point2.y - 52;
            y[2] = point3.y - 52;

            ccolor = color;
            BBG = BG;

        }
        innerTraingle(Graphics canv){
            paint(canv);
        }

        public void paint(Graphics g) {
            if(ccolor!=null) g.setColor(ccolor);
            g.drawPolygon(x, y, 3);

            if(BBG!=null) {
                g.setColor(BBG);
                g.fillPolygon(x, y, 3);
            }
        }
    }

    private class inerLines extends JPanel {
        private Point p1, p2;

        inerLines(Point p1, Point p2) {
            this.p1 = p1;
            this.p2 = p2;
        }

        public void paint(Graphics g) {
            g.drawLine(p1.x - 6, p1.y - 52, p2.x - 6, p2.y - 52);
        }
    }


    public Shape getShape() {
        return r;
    }

    public boolean coversPoint(Point point) {
        if(voidFirst){
            voidFirst=false;
            return false;
        }
        return cover(point, p1, p2, s1, c1) || cover(point, p1, p3, s2, c2) || cover(point, p2, p3, s3, c3);
    }

    private boolean cover(Point p, Point p1, Point p2, double slope, double c) {
        double dis;
        if (slope == infinty) {
            dis = Math.abs(p.x - p1.x);
        } else {
            dis = Math.abs(Math.abs(p.y - slope * p.x - c) / slope);
        }
        return dis < 10 && bound(p, p1, p2);
    }

    boolean bound(Point p, Point p1, Point p2) {
        if (min(p1.x, p2.x) - p.x <= PIXEL_ALLOWED_ERROR) {
            if (p.x - max(p1.x, p2.x) <= PIXEL_ALLOWED_ERROR) {
                if (min(p1.y, p2.y) - p.y <= PIXEL_ALLOWED_ERROR) {
                    if (p.y - max(p1.y, p2.y) <= PIXEL_ALLOWED_ERROR) {
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public void remove() {
        mainFrame.remove(polygon);
    }

    @Override
    public void setPosition(Point position) {
        int appendx = position.x - center.x;
        int appendy = position.y - center.y;
        p1.x += appendx;
        p1.y += appendy;
        p2.x += appendx;
        p2.y += appendy;
        p3.x += appendx;
        p3.y += appendy;
        remove();
        polygon = new innerTraingle(p1, p2, p3);
        mainFrame.add(polygon);
        CURRENTDETALIS();
        MainWindow.mainFrame.setVisible(true);
        MainWindow.mainFrame.repaint();
        saveToLog();
    }

    @Override
    public Point getPosition() {
        return center;
    }

    private double distance(Point pp, Point ppp) {
        return Math.sqrt(Math.pow(pp.x - ppp.x, 2) + Math.pow(pp.y - ppp.y, 2));
    }

    public void resize(Point point) {
        if(voidFirst){
            voidFirst=false;
            return;
        }
        int type = 0;
        if (distance(p1, point) < distance(p2, point) && distance(p1, point) < distance(p3, point)) {
            type = 1;
        } else if (distance(p2, point) < distance(p1, point) && distance(p2, point) < distance(p3, point)) {
            type = 2;
        } else if (distance(p3, point) < distance(p1, point) && distance(p3, point) < distance(p2, point)) {
            type = 3;
        }
        s1(type);
    }

    private void s1(int type) {
         boolean[] change = {false};
        MainWindow.mouseListener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                CURRENTDETALIS();
                if(change[0])saveToLog();
                mainFrame.removeMouseMotionListener(mouseMotionListener);
                mainFrame.removeMouseListener(mouseListener);
            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
            }
        };
        mouseMotionListener = new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                change[0] =true;
                if (type == 1) {
                    p1 = e.getPoint();
                    mainFrame.remove(polygon);
                    polygon = new innerTraingle(p1, p2, p3);
                    mainFrame.add(polygon);
                    mainFrame.setVisible(true);
                    mainFrame.repaint();
                } else if (type == 2) {
                    p2 = e.getPoint();
                    mainFrame.remove(polygon);
                    polygon = new innerTraingle(p1, p2, p3);
                    mainFrame.add(polygon);
                    mainFrame.setVisible(true);
                    mainFrame.repaint();
                } else {
                    p3 = e.getPoint();
                    mainFrame.remove(polygon);
                    polygon = new innerTraingle(p1, p2, p3);
                    mainFrame.add(polygon);
                    mainFrame.setVisible(true);
                    mainFrame.repaint();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        };
        MainWindow.mainFrame.addMouseListener(MainWindow.mouseListener);
        MainWindow.mainFrame.addMouseMotionListener(MainWindow.mouseMotionListener);

    }


    @Override
    public void setProperties(Map<String, Double> properties) {
        this.properties=properties;
        if (properties.containsKey("point1x")) {
            double x = properties.get("point1x");
            p1.x = (int) x;
        }
        if (properties.containsKey("point1y")) {
            double y = properties.get("point1y");
            p1.y = (int) y;
        }
        if (properties.containsKey("point2x")) {
            double x = properties.get("point2x");
            p2.x = (int) x;
        }
        if (properties.containsKey("point2y")) {
            double y = properties.get("point2y");
            p2.y = (int) y;
        }
        if (properties.containsKey("point3x")) {
            double x = properties.get("point3x");
            p3.x = (int) x;
        }
        if (properties.containsKey("point3y")) {
            double y = properties.get("point3y");
            p3.y = (int) y;
        }

        mainFrame.remove(polygon);
        polygon = new innerTraingle(p1, p2,p3,ccolor,BBG);
        mainFrame.add(polygon);
        CURRENTDETALIS();
        mainFrame.setVisible(true);
        mainFrame.repaint();
    }

    @Override
    public Map<String, Double> getProperties() {
        properties=new HashMap<>();
        properties.put("point1x",(double)p1.x);
        properties.put("point1y",(double)p1.y);
        properties.put("point2x",(double)p2.x);
        properties.put("point2y",(double)p2.y);
        properties.put("point3x",(double)p3.x);
        properties.put("point3y",(double)p3.y);

        return properties;
    }

    @Override
    public void setColor(Color color) {
        ccolor = color;
        mainFrame.remove(polygon);
        polygon = new innerTraingle(p1, p2, p3, ccolor, BBG);
        mainFrame.add(polygon);
        mainFrame.setVisible(true);
        mainFrame.repaint();
        saveToLog();
    }

    @Override
    public Color getColor() {
        return ccolor;
    }

    @Override
    public void setFillColor(Color color) {
        BBG = color;
        if (polygon != null){
            mainFrame.remove(polygon);
        }
        polygon = new innerTraingle(p1, p2, p3, ccolor, BBG);
        mainFrame.add(polygon);
        mainFrame.setVisible(true);
        mainFrame.repaint();
    }

    @Override
    public Color getFillColor() {
        return BBG;
    }

    @Override
    public void draw(Graphics canvas) {
        new   innerTraingle(canvas);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        if(polygon!=null)mainFrame.remove(polygon);
        polygon=new innerTraingle(p1,p2,p3,ccolor,BBG);
        mainFrame.add(polygon);
        mainFrame.setVisible(true);
        mainFrame.repaint();
        CURRENTDETALIS();
        return polygon;
    }

    // returns another object of the same class not the same object as the original
    public Shape copy() {
        Shape newShape = new Triangle(p1,p2,p3,ccolor,BBG);
//        shapes.remove(newShape);
        MainWindow.mainFrame.setVisible(true);
        MainWindow.mainFrame.repaint();
        return newShape;
    }
}