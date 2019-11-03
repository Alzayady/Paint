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

public class Circle extends JPanel implements Shape {
    private final int PIXEL_ALLOWED_ERROR = 5;
    private int curserX;
    private int curserY;
    private int centerX;
    private int centerY;
    private Circle circle;
    private boolean resize = false;
    private double width, height, minx, miny;
    private int r;
    private Circle c  = this;
    private Map<String, Double> properties = new HashMap<>();
    /*belongs to the change color menu
     */
    private Color fillColor;
    private Color fontColor;


    Circle() {
        this.drawCircle();
    }

    Circle(Point center, int r, Color fillColor, Color fontColor) {
        this.fillColor = fillColor;
        this.fontColor = fontColor;
        this.r = r;
        centerX = center.x;
        centerY = center.y;
        minx = center.x - r;
        miny = center.y - r;
        width = height = 2 * r;
    }
    Circle(Point center, int r) {
        this(center,r,new Color(0, 0, 0, 0),Color.BLACK);
    }

    Circle(Point center, Point point, Color fillColor, Color fontColor) {
        this.fillColor = fillColor;
        this.fontColor = fontColor;
        r = (int) Math.abs(Math.sqrt(Math.pow((center.x - point.x), 2) + Math.pow((center.y - point.y), 2)));
        minx = center.x - r;
        miny = center.y - r;
        width = height = 2 * r;
    }
    Circle(Point center, Point point, Color fillColor, Color fontColor,boolean x) {
        centerX=center.x;
        centerY=center.y;
        curserY=point.y;
        curserX=point.x;

        this.fillColor = fillColor;
        this.fontColor = fontColor;
        r = (int) Math.abs(Math.sqrt(Math.pow((center.x - point.x), 2) + Math.pow((center.y - point.y), 2)));
        minx = center.x - r;
        miny = center.y - r;
        width = height =  r;
        r/=2;
    }

    public void paint(Graphics g) {
        if(fontColor!=null) g.setColor(this.fontColor);
        g.drawOval((int) minx, (int) miny - 25, (int) width, (int) height);
        if(fillColor!=null) {
            g.setColor(this.fillColor);
            g.fillOval((int) minx + 1, (int) miny - 25 + 1, (int) width - 2, (int) height - 2);
        }
        mainFrame.setVisible(true);
        mainFrame.repaint();
    }

    private void drawCircle() {
        MainWindow.mouseMotionListener = new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (circle != null) {
                    mainFrame.remove(circle);
                    mainFrame.repaint();
                }
                curserX = e.getX();
                curserY = e.getY();
                circle = new Circle(new Point(centerX, centerY - 25), new Point(curserX, curserY - 25), fillColor, fontColor);
                mainFrame.add(circle);
                mainFrame.setVisible(true);
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
                if (resize) return;
                centerX = e.getX();
                centerY = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(!resize)shapes.add(c);
                mainFrame.removeMouseMotionListener(MainWindow.mouseMotionListener);
                mainFrame.removeMouseListener(MainWindow.mouseListener);
                r = (int) Math.abs(Math.sqrt(Math.pow((centerX - curserX), 2) + Math.pow((centerY - curserY), 2)));
                MainWindow.saveToLog();
            }
        };
        mainFrame.addMouseListener(MainWindow.mouseListener);
        mainFrame.addMouseMotionListener(MainWindow.mouseMotionListener);
    }

    public boolean coversPoint(Point point) {
        System.out.println(r);
        int newRadius = (int) Math.abs(Math.sqrt(Math.pow((centerX - point.x), 2) + Math.pow((centerY - point.y), 2)));
        System.out.println(newRadius);
        if (Math.abs(newRadius - r) <= PIXEL_ALLOWED_ERROR) {
            return true;
        }
        return false;
    }

    public Shape getShape() {
        return circle;
    }

    @Override
    public void setPosition(Point position) {
        if (circle != null) {
            mainFrame.remove(circle);
        } else {
            mainFrame.remove(this);
        }
        centerX=position.x;
        centerY=position.y;
        circle = new Circle(new Point(centerX, centerY - 25), r, fillColor, fontColor);
        mainFrame.add(circle);
        mainFrame.repaint();
        mainFrame.setVisible(true);
        MainWindow.saveToLog();
    }


    @Override
    public Point getPosition() {
        return new Point(centerX, centerY);
    }

    public void resize() {
        resize = true;
        drawCircle();
    }

    @Override
    // r
    public void setProperties(Map<String, Double> properties) {
        this.properties = properties;
        if (properties.containsKey("centerx")) {
            double x = properties.get("centerx");
            centerX = (int) x;
        }
        if (properties.containsKey("centery")) {
            double y = properties.get("centery");
            centerY = (int) y;
        }
        if (properties.containsKey("r")) {
            double y = properties.get("r");
            r = (int) y;
        }
        mainFrame.remove(circle);
        circle=new Circle(new Point(centerX,centerY),r);
        mainFrame.add(circle);
        mainFrame.setVisible(true);
        mainFrame.repaint();

    }

    @Override
    public Map<String, Double> getProperties() {
        properties=new HashMap<>();
        properties.put("x",(double)centerX);
        properties.put("y",(double)centerY);
        properties.put("r",(double)r);
        return properties;
    }

    @Override
    public void setColor(Color color) {
        this.fontColor = color;
        mainFrame.remove(circle);
        circle = new Circle(new Point(centerX, centerY), r, fillColor, fontColor);
        mainFrame.add(circle);
        mainFrame.setVisible(true);
        mainFrame.repaint();
        MainWindow.saveToLog();
    }

    @Override
    public Color getColor() {
        return fontColor;
    }

    public void remove(){
        mainFrame.remove(circle);
    }
    @Override
    public void setFillColor(Color color) {
        this.fillColor = color;
        if (circle != null){
            mainFrame.remove(circle);
        }
        circle = new Circle(new Point(centerX, centerY), r, fillColor, fontColor);
        mainFrame.add(circle);
        mainFrame.setVisible(true);
        mainFrame.repaint();
    }

    @Override
    public Color getFillColor() {
        return fillColor;
    }

    @Override
    public void draw(Graphics canvas) {paint(canvas);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        if(circle!=null) mainFrame.remove(circle);
        circle = new Circle(new Point(centerX, centerY - 25),r, fillColor, fontColor);
        mainFrame.add(circle);
        mainFrame.setVisible(true);
        mainFrame.repaint();
        return c;
    }

    // returns another object of the same class not the same object as the original
    public Shape copy(){
        Shape newShape = new Circle(new Point(centerX, centerY ),r, fillColor, fontColor);
//        mainFrame.remove((Component) ((Circle) (newShape)).getShape());
        shapes.remove(newShape);
        MainWindow.mainFrame.setVisible(true);
        MainWindow.mainFrame.repaint();
        return newShape;
    }
}