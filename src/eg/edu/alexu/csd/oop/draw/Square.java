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

public class Square extends JPanel implements Shape, Cloneable {
    private final int PIXEL_REMOVED_VERTICALLY = 52;
    private final int PIXEL_REMOVED_HORIZONTALLY = 6;
    private final int PIXEL_ALLOWED_ERROR = 5;
    private Square square;
    private Point courser; // bottom right point of the square
    private Point head; // upper left point in the square
    private int width, height, minX, minY;
    private Square square1 = this;
    private boolean resizing = false;
    /*belongs to the change color menu
     */
    private Color fillColor;
    private Color fontColor;

    public Square() {
        this.drawSquare();
    }

    public Square(Point head, Point courser, Color fillColor, Color fontColor) {
        setMinPoint(head, courser);
        this.fillColor = fillColor;
        this.fontColor = fontColor;
    }

    public void paint(Graphics g) {
        if (fontColor != null) g.setColor(fontColor);
        g.drawRect(minX, minY, width, height);
        if (fillColor != null) {
            g.setColor(fillColor);
            g.fillRect(minX + 1, minY + 1, width - 1, height - 1);
        }
        mainFrame.repaint();
    }

    private void drawSquare() {
         boolean[] change = {false};
        MainWindow.mouseMotionListener = new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (square != null) {
                    mainFrame.remove(square);
                    mainFrame.repaint();
                    mainFrame.setVisible(true);
                }
                change[0] =true;
                courser = new Point(e.getX(), e.getY());
                System.out.println("Square.mouseDragged  = { x = " + courser.x + " , y = " + courser.y + " }");
                square = new Square(head, courser, fillColor, fontColor);
                mainFrame.add(square);
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
                System.out.println("Square.mousePressed  = { x = " + head.x + " , y = " + head.y + " }");
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!resizing) shapes.add(square1);
                mainFrame.setVisible(true);
                mainFrame.repaint();
                if(change[0]) MainWindow.saveToLog();

                System.out.println("Square.mouseReleased  = { x = " + courser.x + " , y = " + courser.y + " }");
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
        return this.square;
    }

    private void setMinPoint(Point head, Point courser) {
        width = height = Math.abs(head.x - courser.x);
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

    // position is the center
    @Override
    public void setPosition(Point position) {
        this.head = new Point(position.x - width / 2, position.y - height / 2);
        this.courser = new Point(position.x + width / 2, position.y + height / 2);
        if (square != null) {
            mainFrame.remove(square);
            mainFrame.setVisible(true);
            mainFrame.repaint();
        } else {
            mainFrame.remove(this);
            mainFrame.setVisible(true);
            mainFrame.repaint();
        }
        square = new Square(head, courser, this.fillColor, this.fontColor);
        mainFrame.add(square);
        mainFrame.setVisible(true);
        mainFrame.repaint();
        MainWindow.saveToLog();
    }

    public void resize() {
        this.resizing = true;
        drawSquare();
        mainFrame.setVisible(true);
        mainFrame.repaint();
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
        mainFrame.remove(square);
        square = new Square(head, courser, fillColor, fontColor);
        mainFrame.add(square);
        MainWindow.mainFrame.setVisible(true);
        MainWindow.mainFrame.repaint();
        MainWindow.saveToLog();
    }

    @Override
    public Color getColor() {
        return fontColor;
    }

    @Override
    public void setFillColor(Color color) {
        this.fillColor = color;
        if (square != null) {
            mainFrame.remove(square);
        }
        square = new Square(head, courser, fillColor, fontColor);
        mainFrame.add(square);
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

        if (square != null) mainFrame.remove(square);
        square = new Square(head, courser, fillColor, fontColor);
        mainFrame.add(square);
        MainWindow.mainFrame.setVisible(true);
        MainWindow.mainFrame.repaint();

        return square;
    }

    // returns another object of the same class not the same object as the original
    public Shape copy() {
        Shape newShape = new Square(head, courser, fillColor, fontColor);
//        mainFrame.remove((Component) ((Square) (newShape)).getShape());
        shapes.remove(newShape);
        MainWindow.mainFrame.setVisible(true);
        MainWindow.mainFrame.repaint();
        return newShape;
    }

    public void remove() {
        mainFrame.remove(square);
    }
}