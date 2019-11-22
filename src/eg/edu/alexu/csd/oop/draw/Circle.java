package eg.edu.alexu.csd.oop.draw;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.Map;

import static eg.edu.alexu.csd.oop.draw.MainWindow.*;

public class Circle extends JPanel implements Shape  {
    private final int PIXEL_ALLOWED_ERROR = 5;
    private int curserX;
    private int curserY;
    private int centerX;
    private int centerY;
    private Circle circle;
    private Circle rrr=this;
    private double width, height, minx, miny;
    private  int r;
    private  Map<String, Double> properties;
    private Circle circle1 = this;
    Color fillColor;
    Color fontColor;

    Circle (){
        this.drawCircle();
    }


    Circle(Point center, Point point) {
        r = (int)Math.abs(Math.sqrt(Math.pow((center.x - point.x), 2) + Math.pow((center.y - point.y), 2)));
        minx = center.x - r;
        miny = center.y - r;
        width = height = 2 * r;
    }

    public Circle(Point center, Point point, Color colorBACK, Color color) {
        centerX=center.x;
        centerY=center.y;
        curserX=point.x;
        curserY=point.y;
        this.fontColor=color;
        this.fillColor=colorBACK;
        r = (int)Math.abs(Math.sqrt(Math.pow((center.x - point.x), 2) + Math.pow((center.y - point.y), 2)));
        minx = center.x - r;
        miny = center.y - r;
        width = height = 2 * r;
    }
    public Circle(Point center,int r, Color colorBACK, Color color) {
        centerX=center.x;
        centerY=center.y;
        this.r=r;
        this.fontColor=color;
        this.fillColor=colorBACK;
        minx = (center.x - r);
        miny = (center.y - r);
        width = height =  r*2;
    }


    public void paint(Graphics g) {
        if(fontColor!=null)g.setColor(fontColor);
        g.drawOval((int) minx, (int) miny - 25, (int) width, (int) height);
        if(fillColor!=null){
            g.setColor(fillColor);
            g.fillOval((int) minx, (int) miny - 25, (int) width, (int) height);
        }
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
                circle = new Circle(new Point(centerX, centerY - 25), new Point(curserX, curserY - 25));
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
                centerX = e.getX();
                centerY = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                r=(int)Math.abs(Math.sqrt(Math.pow((centerX - curserX), 2) + Math.pow((centerY - curserY), 2)));
                shapes.add(circle1);
                saveToLog();
                mainFrame.removeMouseMotionListener(MainWindow.mouseMotionListener);
                mainFrame.removeMouseListener(MainWindow.mouseListener);

            }
        };
        mainFrame.addMouseListener(MainWindow.mouseListener);
        mainFrame.addMouseMotionListener(MainWindow.mouseMotionListener);
    }

    public boolean coversPoint(Point point){
        int newRadius = (int) Math.abs(Math.sqrt(Math.pow((centerX - point.x), 2) + Math.pow((centerY - point.y), 2)));
        if (Math.abs(newRadius - r) <= PIXEL_ALLOWED_ERROR){
            return true;
        }
        return false;
    }

    public Shape getShape() {
        return circle;
    }

    @Override
    public void setPosition(Point position) {
            mainFrame.remove(circle);
        mainFrame.repaint();
        mainFrame.setVisible(true);
        int appendx=position.x-centerX;
        int appendy=position.y-centerY;
        curserX+=appendx;
        curserY+=appendy;
        centerX=position.x;
        centerY=position.y;
        circle=new Circle(new Point(centerX,centerY-25), new Point(curserX,curserY-25), fillColor, fontColor);
        mainFrame.add(circle);
        mainFrame.repaint();
        mainFrame.setVisible(true);
        saveToLog();
    }

    @Override
    public Point getPosition() {
        return new Point(centerX,centerY);
    }

    @Override
    // r
    public void setProperties(Map<String, Double> properties) {
        this.properties=properties;
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
        this.fontColor=color;
        if(circle!=null)mainFrame.remove(circle);
        circle=new Circle(new Point(centerX,centerY-25), new Point(curserX,curserY-25), fillColor, fontColor);
        mainFrame.add(circle);
        mainFrame.repaint();
        mainFrame.setVisible(true);
        saveToLog();
    }

    @Override
    public Color getColor() {
        return fontColor;
    }

    @Override
    public void setFillColor(Color color) {
       this.fillColor=color;
        if(circle!=null)mainFrame.remove(circle);
        circle=new Circle(new Point(centerX,centerY-25), new Point(curserX,curserY-25), fillColor, fontColor);
        mainFrame.add(circle);
        mainFrame.repaint();
        mainFrame.setVisible(true);
    }

    @Override
    public Color getFillColor() {
        return fillColor;
    }

    @Override
    public void draw(Graphics canvas) {paint(canvas); }
    @Override



    public Object clone() throws CloneNotSupportedException {
        if(circle!=null)mainFrame.remove(circle);
        mainFrame.setVisible(true);
        mainFrame.repaint();
        circle=new Circle(new Point(centerX,centerY-25), r, fillColor, fontColor);
        mainFrame.add(circle);

        mainFrame.setVisible(true);
        mainFrame.repaint();
     return circle;
    }


    public void resize() {
        MainWindow.mouseMotionListener = new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (circle != null) {
                    mainFrame.remove(circle);
                    mainFrame.repaint();
                    mainFrame.setVisible(true);
                }
                curserX = e.getX();
                curserY = e.getY();
                circle = new Circle(new Point(centerX, centerY - 25), new Point(curserX, curserY - 25),fillColor,fontColor);
                mainFrame.add(circle);
                mainFrame.repaint();
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

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                r=(int)Math.abs(Math.sqrt(Math.pow((centerX - curserX), 2) + Math.pow((centerY - curserY), 2)));
                saveToLog();
                mainFrame.removeMouseMotionListener(MainWindow.mouseMotionListener);
                mainFrame.removeMouseListener(MainWindow.mouseListener);
            }
        };
        mainFrame.addMouseListener(MainWindow.mouseListener);
        mainFrame.addMouseMotionListener(MainWindow.mouseMotionListener);

    }


    public Shape copy() {
        Shape newShape = new Circle(new Point(centerX,centerY-25), new Point(curserX,curserY-25), fillColor, fontColor);
        MainWindow.mainFrame.setVisible(true);
        MainWindow.mainFrame.repaint();
        return newShape;
    }


    public void remove() {
        mainFrame.remove(circle);
    }
}