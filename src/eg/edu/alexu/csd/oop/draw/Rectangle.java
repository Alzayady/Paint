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

public class Rectangle extends JPanel implements Shape, Cloneable {
    private final int PIXEL_REMOVED_VERTICALLY = 52;
    private final int PIXEL_REMOVED_HORIZONTALLY = 6;
    private final int PIXEL_ALLOWED_ERROR = 5;
    private Rectangle rectangle;
    private Point courser; // bottom right point of the rectangle
    private Point head; // upper left point in the rectangle
    private int width, height, minX, minY;
    private Rectangle rectangle1 = this;
    private boolean resizing = false;
    /*belongs to the change color menu
     */
    private Color fillColor;
    private Color fontColor;

    public Rectangle() {

        this.drawRectangle();
    }

    public Rectangle(Point head, Point courser, Color fillColor, Color fontColor) {
        this.fontColor = fontColor;
        this.fillColor = fillColor;
        setMinPoint(head, courser);
    }

    public void paint(Graphics g) {
        if (fontColor != null) g.setColor(fontColor);
        g.drawRect(minX, minY, width, height);
        if (fillColor != null) {
            g.setColor(fillColor);
            g.fillRect(minX + 1, minY + 1, width - 1, height - 1);
        }
        mainFrame.setVisible(true);
        mainFrame.repaint();
    }

    private void drawRectangle() {
         boolean[] change = {false};
        MainWindow.mouseMotionListener = new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (rectangle != null) {
                    mainFrame.remove(rectangle);
                    mainFrame.setVisible(true);
                    mainFrame.repaint();
                }
                change[0] =true;
                courser = new Point(e.getX(), e.getY());
                System.out.println("Rectangle.mouseDragged  = { x = " + courser.x + " , y = " + courser.y + " }");
                rectangle = new Rectangle(head, courser, fillColor, fontColor);
                mainFrame.add(rectangle);
                mainFrame.setVisible(true);
                mainFrame.repaint();
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
            public void mousePressed(MouseEvent e) {
                if (resizing) return;
                head = new Point(e.getX(), e.getY());
                courser = new Point(e.getX(), e.getY());
                System.out.println("Rectangle.mousePressed  = { x = " + head.x + " , y = " + head.y + " }");
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!resizing) shapes.add(rectangle1);
                mainFrame.setVisible(true);
                mainFrame.repaint();
               if(change[0]) MainWindow.saveToLog();

                System.out.println("Rectangle.mouseReleased  = { x = " + courser.x + " , y = " + courser.y + " }");
                mainFrame.removeMouseListener(MainWindow.mouseListener);
                mainFrame.removeMouseMotionListener(MainWindow.mouseMotionListener);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        };
        mainFrame.addMouseMotionListener(MainWindow.mouseMotionListener);
        mainFrame.addMouseListener(MainWindow.mouseListener);
    }

    public Shape getShape() {
        return rectangle;
    }

    private void setMinPoint(Point head, Point courser) {
        width = Math.abs(head.x - courser.x);
        height = Math.abs(head.y - courser.y);
        Point newHead = new Point(head.x, head.y);
        Point newCourser = new Point(courser.x, courser.y);
        if ((head.x > courser.x) && (head.y > courser.y)) {
            newHead = new Point(head.x - width, head.y - height);
            courser = new Point(head.x, head.y);
        } else if ((head.x < courser.x) && (head.y > courser.y)) {
            newHead = new Point(head.x, head.y - height);
            newCourser = new Point(head.x - width, head.y + height);
        } else if ((head.x > courser.x) && (head.y < courser.y)) {
            newHead = new Point(courser.x, head.y);
            newCourser = new Point(head.x + width, head.y);
        }
        this.head = newHead;
        this.courser = newCourser;
        minX = newHead.x - PIXEL_REMOVED_HORIZONTALLY;
        minY = newHead.y - PIXEL_REMOVED_VERTICALLY;
    }

    public boolean coversPoint(Point point) {
        point = new Point(point.x - PIXEL_REMOVED_HORIZONTALLY, point.y - PIXEL_REMOVED_VERTICALLY);
        setMinPoint(head, courser);
        Point center = new Point(minX + width / 2, minY + height / 2);
        if ((point.x >= (center.x - width / 2)) && (point.x <= (center.x + width / 2))) {
            if ((point.y >= center.y)) { // the point is down
                if (Math.abs((minY + height) - point.y) <= PIXEL_ALLOWED_ERROR) return true;
            } else { // the point is up
                if (Math.abs(minY - point.y) <= PIXEL_ALLOWED_ERROR) return true;
            }
        } else if ((point.y >= minY) && (point.y <= (minY + height))) {
            if (point.x <= center.x) { // this point is left
                if (Math.abs(minX - point.x) <= PIXEL_ALLOWED_ERROR) return true;
            } else { // the point is right
                if (Math.abs((minX + width) - point.x) <= PIXEL_ALLOWED_ERROR) return true;
            }
        }
        return false;
    }

    public void resize() {
        this.resizing = true;
        drawRectangle();
        mainFrame.repaint();
        mainFrame.setVisible(true);
    }

    // position is the center
    @Override
    public void setPosition(Point position) {
        this.head = new Point(position.x - width / 2, position.y - height / 2);
        this.courser = new Point(position.x + width / 2, position.y + height / 2);
        if (rectangle != null) {
            mainFrame.remove(rectangle);
            mainFrame.setVisible(true);
            mainFrame.repaint();
        }
        rectangle = new Rectangle(head, courser, fillColor, fontColor);
        mainFrame.add(rectangle);
        mainFrame.setVisible(true);
        mainFrame.repaint();
        MainWindow.saveToLog();
    }

    // position is the center
    @Override
    public Point getPosition() {
        return new Point(this.head.x + width / 2, this.head.y + height / 2);
    }

    @Override
    public void setProperties(Map<String, Double> properties) {
        for (String name : properties.keySet()) {
            if (name.equals("headX")) {
                this.head.x = Integer.parseInt(properties.get(name).toString());
            } else if (name.equals("headY")) {
                this.head.y = Integer.parseInt(properties.get(name).toString());
            } else if (name.equals("courserX")) {
                this.courser.x = Integer.parseInt(properties.get(name).toString());
            } else if (name.equals("courserY")) {
                this.courser.y = Integer.parseInt(properties.get(name).toString());
            }
        }
    }

    @Override
    public Map<String, Double> getProperties() {
        Map<String, Double> properties = new HashMap<>();
        properties.put("headX", (double) head.x);
        properties.put("headY", (double) head.y);
        properties.put("courserX", (double) courser.x);
        properties.put("courserY", (double) courser.y);
        return properties;
    }

    @Override
    public void setColor(Color color) {
        this.fontColor = color;
        mainFrame.remove(rectangle);
        rectangle = new Rectangle(head, courser, fillColor, fontColor);
        mainFrame.add(rectangle);
        MainWindow.mainFrame.setVisible(true);
        MainWindow.mainFrame.repaint();
        MainWindow.saveToLog();
    }

    @Override
    public Color getColor() {
        return this.fontColor;
    }

    @Override
    public void setFillColor(Color color) {
        this.fillColor = color;
        if (rectangle != null) {
            mainFrame.remove(rectangle);
        }
        rectangle = new Rectangle(head, courser, fillColor, fontColor);
        mainFrame.add(rectangle);
        MainWindow.mainFrame.setVisible(true);
        MainWindow.mainFrame.repaint();
    }

    @Override
    public Color getFillColor() {
        return this.fillColor;
    }

    @Override
    public void draw(Graphics canvas) {
paint(canvas);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        if (rectangle != null) mainFrame.remove(rectangle);
        MainWindow.mainFrame.setVisible(true);
        MainWindow.mainFrame.repaint();
        rectangle = new Rectangle(head, courser, fillColor, fontColor);
        mainFrame.add(rectangle);
        MainWindow.mainFrame.setVisible(true);
        MainWindow.mainFrame.repaint();
        return rectangle;
    }

    // returns another object of the same class not the same object as the original
    public Shape copy() {
        Shape newShape = new Rectangle(head, courser, fillColor, fontColor);
        MainWindow.mainFrame.setVisible(true);
        MainWindow.mainFrame.repaint();
        return newShape;
    }

    public void remove() {
        mainFrame.remove(rectangle);
    }
}