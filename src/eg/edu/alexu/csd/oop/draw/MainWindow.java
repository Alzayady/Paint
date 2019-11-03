package eg.edu.alexu.csd.oop.draw;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MainWindow implements DrawingEngine {
    static JFrame mainFrame;
    static MouseMotionListener mouseMotionListener;
    static MouseListener mouseListener;
    static LinkedList<Shape> shapes = new LinkedList<>();
    private Shape selectedShape;
    public MouseListener MouseListenerSelect = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            selectedShape = select(mouseEvent.getPoint());
            System.out.println(shapes.size());
        }

        @Override
        public void mousePressed(MouseEvent mouseEvent) { }

        @Override
        public void mouseReleased(MouseEvent mouseEvent) { }

        @Override
        public void mouseEntered(MouseEvent mouseEvent) { }

        @Override
        public void mouseExited(MouseEvent mouseEvent) { }
    };
    private MouseListener mouseListenerRePosition;
    /*
     * belongs to the change color menu
     */
    JFrame colorChooserFrame;
    private JColorChooser colorFillChooser = new JColorChooser();
    private JColorChooser colorFontChooser = new JColorChooser();
    private Color chosenFillColor = Color.white;
    private Color chosenFontColor = Color.BLACK;
    private boolean TYPEISXML=false;
    public MainWindow() {
        mainFrame = new JFrame("Draw");
        mainFrame.setBackground(Color.white);
        mainFrame.setSize(800, 700);
        mainFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.addMouseListener(this.MouseListenerSelect);

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("file");
        JMenu editMenu = new JMenu("edit");
        JMenu shapesMenu = new JMenu("shapes");

        JMenuItem delete = new JMenuItem("delete");
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("delete")) {
                    if ((selectedShape != null)) {
                        if (selectedShape instanceof Circle) {
                            mainFrame.remove((Component) ((Circle) (selectedShape)).getShape());
                        } else if (selectedShape instanceof Oval) {
                            mainFrame.remove((Component) ((Oval) (selectedShape)).getShape());
                        } else if (selectedShape instanceof Line) {
                            ((Line) selectedShape).remove();
                           // mainFrame.remove((Component) ((Line) (selectedShape)).getShape());
                        } else if (selectedShape instanceof Rectangle) {
                            mainFrame.remove((Component) ((Rectangle) (selectedShape)).getShape());
                        } else if (selectedShape instanceof Square) {
                            mainFrame.remove((Component) ((Square) (selectedShape)).getShape());
                        } else if (selectedShape instanceof Triangle) {
                            ((Triangle) selectedShape).remove();
                        }
                        shapes.remove(selectedShape);
//                        try {
//                            mainFrame.remove((Component) (selectedShape));
//                        } catch (Exception e1) {
//                        }
                        System.out.println("Shape with center = " + selectedShape.getPosition().toString() + " was removed");
                        selectedShape = null;
                        mainFrame.repaint();
                        mainFrame.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(mainFrame, "no shape was selected");
                    }
                }
            }
        });
        JMenuItem resize = new JMenuItem("resize");
        resize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("resize")) {
                    if (selectedShape != null) {
                        // code for resize
                    } else {
                        JOptionPane.showMessageDialog(mainFrame, "no shape was selected");
                    }
                }
            }
        });
        JMenuItem changeColor = new JMenuItem("change color");
        changeColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("change color")) {
                    if (selectedShape != null) {
                        colorChooserFrame = new JFrame("choose color");
                        colorChooserFrame.setVisible(true);
                        colorChooserFrame.setBounds(500, 100, 500, 700);
                        colorChooserFrame.setLayout(new BorderLayout());
//                        colorChooserFrame.add(new JLabel("choose fill color"),BorderLayout.LINE_START);
                        colorChooserFrame.add(colorFillChooser, BorderLayout.PAGE_START);
                        colorChooserFrame.add(new JLabel("choose font color"), BorderLayout.CENTER);
                        colorChooserFrame.add(colorFontChooser, BorderLayout.PAGE_END);
                        ChangeListener changeListener = new ChangeListener() {
                            @Override
                            public void stateChanged(ChangeEvent e) {
                                chosenFillColor = colorFillChooser.getColor();
                                chosenFontColor = colorFontChooser.getColor();
                                System.out.println("Color fill Chosen = " + chosenFillColor + " and color boundries chosen + " + chosenFontColor);
                                //selectedShape.setPosition(selectedShape.getPosition());
                                selectedShape.setFillColor(chosenFillColor);
                                selectedShape.setColor(chosenFontColor);
                             //   selectedShape = shapes.getLast();
                            }
                        };
                        colorFillChooser.getSelectionModel().addChangeListener(changeListener);
                        colorFontChooser.getSelectionModel().addChangeListener(changeListener);
                        chosenFillColor = Color.WHITE;
                        chosenFontColor = Color.BLACK;
                        // code for change color
                    } else {
                        JOptionPane.showMessageDialog(mainFrame, "no shape was selected");
                    }
                }
            }
        });
        JMenuItem rePosition = new JMenuItem("change position");
        rePosition.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("change position")) {
                    if (selectedShape != null) {
                        JOptionPane.showMessageDialog(mainFrame, "select a new point");
                        mainFrame.removeMouseListener(MouseListenerSelect);
                        mouseListenerRePosition = new MouseListener() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                selectedShape.setPosition(e.getPoint());
                                selectedShape = null;
                                mainFrame.removeMouseListener(mouseListenerRePosition);
                                mainFrame.addMouseListener(MouseListenerSelect);
                            }
                            @Override
                            public void mousePressed(MouseEvent e) { }
                            @Override
                            public void mouseReleased(MouseEvent e) { }
                            @Override
                            public void mouseEntered(MouseEvent e) { }
                            @Override
                            public void mouseExited(MouseEvent e) { }
                        };
                        mainFrame.addMouseListener(mouseListenerRePosition);
                        mainFrame.repaint();
                        mainFrame.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(mainFrame, "no shape was selected");
                    }
                }
            }
        });
        JMenuItem save = new JMenuItem("save");
        JMenuItem XML = new JMenuItem("XML");

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.removeMouseMotionListener(mouseMotionListener);
                mainFrame.removeMouseListener(mouseListener);
                if (e.getActionCommand().equals("XML")) {
                    TYPEISXML=true;
                    JFileChooser fileChooser = new JFileChooser("f:");
                    int r = fileChooser.showSaveDialog(null);
                    if (r == JFileChooser.APPROVE_OPTION) {
                        //File file = new File(fileChooser.getSelectedFile().getAbsolutePath());

                        save(fileChooser.getSelectedFile().getAbsolutePath());
                    } else {
                        JOptionPane.showMessageDialog(mainFrame, "the user cancelled the operation");
                    }
                }

            }
        });

        JMenuItem JSON = new JMenuItem("JSON");

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.removeMouseMotionListener(mouseMotionListener);
                mainFrame.removeMouseListener(mouseListener);
                if (e.getActionCommand().equals("JSON")) {
                    TYPEISXML=false;
                    JFileChooser fileChooser = new JFileChooser("f:");
                    int r = fileChooser.showSaveDialog(null);
                    if (r == JFileChooser.APPROVE_OPTION) {
                        //File file = new File(fileChooser.getSelectedFile().getAbsolutePath());

                        save(fileChooser.getSelectedFile().getAbsolutePath());
                    } else {
                        JOptionPane.showMessageDialog(mainFrame, "the user cancelled the operation");
                    }
                }

            }
        });



        JMenuItem load = new JMenuItem("open");
        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                mainFrame.removeMouseMotionListener(mouseMotionListener);
                mainFrame.removeMouseListener(mouseListener);
                if (actionEvent.getActionCommand().equals("open")) {
                    JFileChooser fileChooser = new JFileChooser("f:");
                    int r = fileChooser.showSaveDialog(null);
                    if (r == JFileChooser.APPROVE_OPTION) {
                        //File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                        load(fileChooser.getSelectedFile().getAbsolutePath());
                    } else {
                        JOptionPane.showMessageDialog(mainFrame, "the user cancelled the operation");
                    }
                }

            }
        });
        JMenuItem undo = new JMenuItem("undo");
        JMenuItem redo = new JMenuItem("redo");
        // circle
        JMenuItem circle = new JMenuItem("circle");
        circle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.removeMouseMotionListener(mouseMotionListener);
                mainFrame.removeMouseListener(mouseListener);
                if (e.getActionCommand().equals("circle")) {
                    Circle circle1 = new Circle();
                }
            }
        });
        // square
        JMenuItem square = new JMenuItem("square");
        square.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.removeMouseMotionListener(mouseMotionListener);
                mainFrame.removeMouseListener(mouseListener);
                if (e.getActionCommand().equals("square")) {
                    Square square1 = new Square();
                }
            }
        });
        // triangle
        JMenuItem triangle = new JMenuItem("triangle");
        triangle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.removeMouseMotionListener(mouseMotionListener);
                mainFrame.removeMouseListener(mouseListener);
                if (e.getActionCommand().equals("triangle")) {
                    Triangle triangle1 = new Triangle();
                    if (triangle1.getShape() != null) {
                      //  shapes.add(triangle1);
                    }
                }
            }
        });
        // rectangle
        JMenuItem rectangle = new JMenuItem("rectangle");
        rectangle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.removeMouseMotionListener(mouseMotionListener);
                mainFrame.removeMouseListener(mouseListener);
                if (e.getActionCommand().equals("rectangle")) {
                    Rectangle rectangle1 = new Rectangle();
                    if (rectangle1.getShape() != null) {
                      //  shapes.add(rectangle1);
                    }
                }
            }
        });
        // ellipse
        JMenuItem oval = new JMenuItem("ellipse");
        oval.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.removeMouseMotionListener(mouseMotionListener);
                mainFrame.removeMouseListener(mouseListener);
                if (e.getActionCommand().equals("ellipse")) {
                    Oval oval1 = new Oval();
                }
            }
        });
        // line
        JMenuItem line = new JMenuItem("line");
        line.addActionListener(e -> {
            mainFrame.removeMouseMotionListener(mouseMotionListener);
            mainFrame.removeMouseListener(mouseListener);
            if (e.getActionCommand().equals("line")) {
                Line line1 = new Line();
            }
        });
        // new
        JMenuItem newFile = new JMenuItem("new");

        newFile.addActionListener(e -> {
            if (e.getActionCommand().equals("new")) {
                System.out.println("Shapes drawn count = " + shapes.size());
                mainFrame = new JFrame("Draw");
            }
        });
        JMenuItem close = new JMenuItem("close");
        close.addActionListener(e -> {
            mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
        });

        save.add(XML);
        save.add(JSON);
        fileMenu.add(save);
        fileMenu.add(load);
        fileMenu.add(newFile);
        fileMenu.add(close);
        fileMenu.add(undo);
        fileMenu.add(redo);
        editMenu.add(delete);
        editMenu.add(resize);
        editMenu.add(rePosition);
        editMenu.add(changeColor);
        shapesMenu.add(circle);
        shapesMenu.add(square);
        shapesMenu.add(triangle);
        shapesMenu.add(rectangle);
        shapesMenu.add(oval);
        shapesMenu.add(line);
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(shapesMenu);
        this.mainFrame.setJMenuBar(menuBar);

//        mainFrame.add(new Circle(new Point(100,100), new Point(50,50)));
//        mainFrame.setVisible(true);
//        mainFrame.add(new Circle(new Point(200,200), new Point(50,50)));

        mainFrame.setBackground(Color.white);
        mainFrame.setVisible(true);
    }

    private Shape select(Point point) {
        for (Shape shape : shapes) {
            /*
             * belongs to the color chooser menu*/
            if (this.colorChooserFrame != null) {
                colorChooserFrame.dispatchEvent(new WindowEvent(colorChooserFrame, WindowEvent.WINDOW_CLOSING));
                this.colorChooserFrame = null;
            }
            //
            System.out.println(shape.getClass());
            if ((shape instanceof Circle) && ((Circle) shape).coversPoint(point)) {
                System.out.println("Circle is selected with center = " + (((Circle) shape).getPosition().toString()));
                ((Circle) shape).resize();
                return (Circle) shape;
            } else if ((shape instanceof Square) && ((Square) shape).coversPoint(point)) {
                System.out.println("Square is selected with center = " + (((Square) shape).getPosition().toString()));
                ((Square) shape).resize();
                return (Square) shape;
            } else if ((shape instanceof Rectangle) && ((Rectangle) shape).coversPoint(point)) {
                System.out.println("Rectangle is selected with center = " + (((Rectangle) shape).getPosition().toString()));
                ((Rectangle) shape).resize();
                return (Rectangle) shape;
            } else if ((shape instanceof Oval) && ((Oval) shape).coversPoint(point)) {
                System.out.println("Ellipse is selected with center = " + (((Oval) shape).getPosition().toString()));
                ((Oval) shape).resize();
                return (Oval) shape;
            } else if ((shape instanceof Line) && ((Line) shape).coversPoint(point)) {
                System.out.println("Line is selected with head = " + (((Line) shape).getPosition().toString()));
                ((Line) (shape)).resize(point);
                return (Line) shape;
            } else if ((shape instanceof Triangle) && ((Triangle) shape).coversPoint(point)) {
                System.out.println("Triangle is selected with center = " + (((Triangle) shape).getPosition().toString()));
                ((Triangle) (shape)).resize(point);
                return (Triangle) shape;
            }
        }
        return null;
    }


    @Override
    public void refresh(Graphics canvas) {

    }

    @Override
    public void addShape(Shape shape) {

    }

    @Override
    public void removeShape(Shape shape) {

    }

    @Override
    public void updateShape(Shape oldShape, Shape newShape) {

    }

    @Override
    public Shape[] getShapes() {
        return (Shape[]) shapes.toArray();
    }
    @Override
    public List<Class<? extends Shape>> getSupportedShapes() {
        return null;
    }

    @Override
    public void undo() {

    }

    @Override
    public void redo() {

    }

    @Override
    public void save(String path) {
        if (TYPEISXML) {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = null;
            try {
                dBuilder = dbFactory.newDocumentBuilder();

            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
            Document doc = dBuilder.newDocument();
            Element rrr = doc.createElement("shapes");
            doc.appendChild(rrr);
            for (int i = 0; i < shapes.size(); i++) {
                if (shapes.get(i) instanceof Circle) {
                    Element rootElement = doc.createElement("Circle");
                    rrr.appendChild(rootElement);
                    Map<String, Double> ma = shapes.get(i).getProperties();
                    Attr attr = doc.createAttribute("x");
                    String e = Double.toString(ma.get("x"));
                    attr.setValue(e);
                    rootElement.setAttributeNode(attr);
                    attr = doc.createAttribute("y");
                    e = Double.toString(ma.get("y"));
                    attr.setValue(e);
                    rootElement.setAttributeNode(attr);
                    attr = doc.createAttribute("r");
                    e = Double.toString(ma.get("r"));
                    attr.setValue(e);
                    rootElement.setAttributeNode(attr);
                    Color color = shapes.get(i).getColor();
                    if (color != null) {
                        attr = doc.createAttribute("color");
                        e = (color.getRed()) + "," + (color.getGreen()) + "," + (color.getBlue());
                        attr.setValue(e);
                        rootElement.setAttributeNode(attr);
                    }
                    color = shapes.get(i).getFillColor();
                    if (color != null) {
                        attr = doc.createAttribute("fillcolor");
                        e = (color.getRed()) + "," + (color.getGreen()) + "," + (color.getBlue());
                        attr.setValue(e);
                        rootElement.setAttributeNode(attr);
                    }

                } else if (shapes.get(i) instanceof Rectangle) {
                    Element rootElement = doc.createElement("Rectangle");
                    rrr.appendChild(rootElement);
                    Map<String, Double> ma = shapes.get(i).getProperties();
                    Attr attr = doc.createAttribute("headX");
                    String e = Double.toString(ma.get("headX"));
                    attr.setValue(e);
                    rootElement.setAttributeNode(attr);
                    attr = doc.createAttribute("headY");
                    e = Double.toString(ma.get("headY"));
                    attr.setValue(e);
                    rootElement.setAttributeNode(attr);
                    attr = doc.createAttribute("courserX");
                    e = Double.toString(ma.get("courserX"));
                    attr.setValue(e);
                    rootElement.setAttributeNode(attr);
                    attr = doc.createAttribute("courserY");
                    e = Double.toString(ma.get("courserY"));
                    attr.setValue(e);
                    rootElement.setAttributeNode(attr);
                    Color color = shapes.get(i).getColor();
                    if (color != null) {
                        attr = doc.createAttribute("color");
                        e = (color.getRed()) + "," + (color.getGreen()) + "," + (color.getBlue());
                        attr.setValue(e);
                        rootElement.setAttributeNode(attr);
                    }
                    color = shapes.get(i).getFillColor();
                    if (color != null) {
                        attr = doc.createAttribute("fillcolor");
                        e = (color.getRed()) + "," + (color.getGreen()) + "," + (color.getBlue());
                        attr.setValue(e);
                        rootElement.setAttributeNode(attr);
                    }
                } else if (shapes.get(i) instanceof Square) {
                    Element rootElement = doc.createElement("Square");
                    rrr.appendChild(rootElement);
                    Map<String, Double> ma = shapes.get(i).getProperties();
                    Attr attr = doc.createAttribute("headX");
                    String e = Double.toString(ma.get("headX"));
                    attr.setValue(e);
                    rootElement.setAttributeNode(attr);
                    attr = doc.createAttribute("headY");
                    e = Double.toString(ma.get("headY"));
                    attr.setValue(e);
                    rootElement.setAttributeNode(attr);
                    attr = doc.createAttribute("courserX");
                    e = Double.toString(ma.get("courserX"));
                    attr.setValue(e);
                    rootElement.setAttributeNode(attr);
                    attr = doc.createAttribute("courserY");
                    e = Double.toString(ma.get("courserY"));
                    attr.setValue(e);
                    rootElement.setAttributeNode(attr);
                    Color color = shapes.get(i).getColor();
                    if (color != null) {
                        attr = doc.createAttribute("color");
                        e = (color.getRed()) + "," + (color.getGreen()) + "," + (color.getBlue());
                        attr.setValue(e);
                        rootElement.setAttributeNode(attr);
                    }
                    color = shapes.get(i).getFillColor();
                    if (color != null) {
                        attr = doc.createAttribute("fillcolor");
                        e = (color.getRed()) + "," + (color.getGreen()) + "," + (color.getBlue());
                        attr.setValue(e);
                        rootElement.setAttributeNode(attr);
                    }
                } else if (shapes.get(i) instanceof Oval) {
                    Element rootElement = doc.createElement("Oval");
                    rrr.appendChild(rootElement);
                    Map<String, Double> ma = shapes.get(i).getProperties();
                    Attr attr = doc.createAttribute("headX");
                    String e = Double.toString(ma.get("headX"));
                    attr.setValue(e);
                    rootElement.setAttributeNode(attr);
                    attr = doc.createAttribute("headY");
                    e = Double.toString(ma.get("headY"));
                    attr.setValue(e);
                    rootElement.setAttributeNode(attr);
                    attr = doc.createAttribute("courserX");
                    e = Double.toString(ma.get("courserX"));
                    attr.setValue(e);
                    rootElement.setAttributeNode(attr);
                    attr = doc.createAttribute("courserY");
                    e = Double.toString(ma.get("courserY"));
                    attr.setValue(e);
                    rootElement.setAttributeNode(attr);
                    Color color = shapes.get(i).getColor();
                    if (color != null) {
                        attr = doc.createAttribute("color");
                        e = (color.getRed()) + "," + (color.getGreen()) + "," + (color.getBlue());
                        attr.setValue(e);
                        rootElement.setAttributeNode(attr);
                    }
                    color = shapes.get(i).getFillColor();
                    if (color != null) {
                        attr = doc.createAttribute("fillcolor");
                        e = (color.getRed()) + "," + (color.getGreen()) + "," + (color.getBlue());
                        attr.setValue(e);
                        rootElement.setAttributeNode(attr);
                    }
                } else if (shapes.get(i) instanceof Triangle) {
                    Element rootElement = doc.createElement("Triangle");
                    rrr.appendChild(rootElement);
                    Map<String, Double> ma = shapes.get(i).getProperties();
                    Attr attr = doc.createAttribute("point1x");
                    String e = Double.toString(ma.get("point1x"));
                    attr.setValue(e);
                    rootElement.setAttributeNode(attr);
                    attr = doc.createAttribute("point1y");
                    e = Double.toString(ma.get("point1y"));
                    attr.setValue(e);
                    rootElement.setAttributeNode(attr);
                    attr = doc.createAttribute("point2x");
                    e = Double.toString(ma.get("point2x"));
                    attr.setValue(e);
                    rootElement.setAttributeNode(attr);
                    attr = doc.createAttribute("point2y");
                    e = Double.toString(ma.get("point2y"));
                    attr.setValue(e);
                    rootElement.setAttributeNode(attr);
                    attr = doc.createAttribute("point3x");
                    e = Double.toString(ma.get("point3x"));
                    attr.setValue(e);
                    rootElement.setAttributeNode(attr);
                    attr = doc.createAttribute("point3y");
                    e = Double.toString(ma.get("point3y"));
                    attr.setValue(e);
                    rootElement.setAttributeNode(attr);
                    Color color = shapes.get(i).getColor();
                    if (color != null) {
                        attr = doc.createAttribute("color");
                        e = (color.getRed()) + "," + (color.getGreen()) + "," + (color.getBlue());
                        attr.setValue(e);
                        rootElement.setAttributeNode(attr);
                    }
                    color = shapes.get(i).getFillColor();
                    if (color != null) {
                        attr = doc.createAttribute("fillcolor");
                        e = (color.getRed()) + "," + (color.getGreen()) + "," + (color.getBlue());
                        attr.setValue(e);
                        rootElement.setAttributeNode(attr);
                    }
                } else if (shapes.get(i) instanceof Line) {
                    Element rootElement = doc.createElement("Line");
                    rrr.appendChild(rootElement);
                    Map<String, Double> ma = shapes.get(i).getProperties();
                    Attr attr = doc.createAttribute("point1x");
                    String e = Double.toString(ma.get("point1x"));
                    attr.setValue(e);
                    rootElement.setAttributeNode(attr);
                    attr = doc.createAttribute("point1y");
                    e = Double.toString(ma.get("point1y"));
                    attr.setValue(e);
                    rootElement.setAttributeNode(attr);
                    attr = doc.createAttribute("point2x");
                    e = Double.toString(ma.get("point2x"));
                    attr.setValue(e);
                    rootElement.setAttributeNode(attr);
                    attr = doc.createAttribute("point2y");
                    e = Double.toString(ma.get("point2y"));
                    attr.setValue(e);
                    rootElement.setAttributeNode(attr);
                    Color color = shapes.get(i).getColor();
                    if (color != null) {
                        attr = doc.createAttribute("color");
                        e = (color.getRed()) + "," + (color.getGreen()) + "," + (color.getBlue());
                        attr.setValue(e);
                        rootElement.setAttributeNode(attr);
                    }
                }

            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = null;
            try {
                transformer = transformerFactory.newTransformer();
            } catch (TransformerConfigurationException e) {
                e.printStackTrace();
            }
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(path+".xml"));
            try {
                transformer.transform(source, result);
            } catch (TransformerException e) {
                e.printStackTrace();
            }

            // Output to console for testing
            StreamResult consoleResult = new StreamResult(System.out);
            try {
                transformer.transform(source, consoleResult);
            } catch (TransformerException e) {
                e.printStackTrace();
            }
        } else {
       boolean f =true;
            String json = "{  \"Circle\": ";
            for (int i = 0; i < shapes.size(); i++) {
                Shape s = shapes.get(i);
                if (s instanceof Circle) {
                    if(f)json+="[";
                    f=false;
                    HashMap<String, Double> p = (HashMap<String, Double>) s.getProperties();
                    String a = "{";
                    a += "\"x\": " + p.get("x") + ",";
                    a += "\"y\": " + p.get("y") + ",";
                    a += "\"r\": " + p.get("r");
                    if (s.getColor() != null) {
                        a += ",\"color\": \"" + s.getColor().getRed() + "," + s.getColor().getGreen() + "," + s.getColor().getBlue() + "\"";
                    }
                    if (s.getFillColor() != null) {
                        a += ",\"fillcolor\": \"" + s.getFillColor().getRed() + "," + s.getFillColor().getGreen() + "," + s.getFillColor().getBlue() + "\"";
                    }
                    a += "},";
                    json += a;
                }
            }
            String t="";
            if(json.charAt(json.length()-1)==','){
                for(int i=0;i<json.length()-1;i++){
                    t+=json.charAt(i);
                }
                json=t;
            }
            if(f)json+="[";
            json+="]";
           f=true;
           json+= ",  \"Square\": ";
            for (int i = 0; i < shapes.size(); i++) {
                Shape s = shapes.get(i);
                if (s instanceof Square) {
                 //   if(!f)json+=",";
                    if(f)json+="[";
                    f=false;
                    String a = "{";
                    HashMap<String, Double> p = (HashMap<String, Double>) s.getProperties();
                            a+="\"headX\": "+p.get("headX")+",";
                            a+="\"headY\": "+p.get("headY")+",";
                            a+="\"courserX\": "+p.get("courserX")+",";
                            a+="\"courserY\": "+p.get("courserY");
                    if (s.getColor() != null) {
                        a += ",\"color\": \"" + s.getColor().getRed() + "," + s.getColor().getGreen() + "," + s.getColor().getBlue() + "\"";
                    }
                    if (s.getFillColor() != null) {
                        a += ",\"fillcolor\": \"" + s.getFillColor().getRed() + "," + s.getFillColor().getGreen() + "," + s.getFillColor().getBlue() + "\"";
                    }
                    a += "},";
                    json += a;
                }
            }
             t="";
            for(int i=0;i<json.length()-1;i++){
                t+=json.charAt(i);
            }
            json=t;
            if(f)json+="[";
            json+="]";

            f=true;
            json+= ",\"Rectangle\": ";
            for (int i = 0; i < shapes.size(); i++) {
                Shape s = shapes.get(i);
                if (s instanceof Rectangle) {
                   // if(!start&&f)json+=",";
                    if(f)json+="[";
                    f=false;
                    String a = "{";
                    HashMap<String, Double> p = (HashMap<String, Double>) s.getProperties();
                    a+="\"headX\": "+p.get("headX")+",";
                    a+="\"headY\": "+p.get("headY")+",";
                    a+="\"courserX\": "+p.get("courserX")+",";
                    a+="\"courserY\": "+p.get("courserY");
                    if (s.getColor() != null) {
                        a += ",\"color\": \"" + s.getColor().getRed() + "," + s.getColor().getGreen() + "," + s.getColor().getBlue() + "\"";
                    }
                    if (s.getFillColor() != null) {
                        a += ",\"fillcolor\": \"" + s.getFillColor().getRed() + "," + s.getFillColor().getGreen() + "," + s.getFillColor().getBlue() + "\"";
                    }
                    a += "},";
                    json += a;
                }
            }
            t="";
            for(int i=0;i<json.length()-1;i++){
                t+=json.charAt(i);
            }
            json=t;
            if(f)json+="[";
            json+="]";
            f=true;
            json+= ", \"Oval\": ";
            for (int i = 0; i < shapes.size(); i++) {
                Shape s = shapes.get(i);
                if (s instanceof Oval) {
                    if(f)json+="[";
                    f=false;
                    String a = "{";
                    HashMap<String, Double> p = (HashMap<String, Double>) s.getProperties();
                    a+="\"headX\": "+p.get("headX")+",";
                    a+="\"headY\": "+p.get("headY")+",";
                    a+="\"courserX\": "+p.get("courserX")+",";
                    a+="\"courserY\": "+p.get("courserY");
                    if (s.getColor() != null) {
                        a += ",\"color\": \"" + s.getColor().getRed() + "," + s.getColor().getGreen() + "," + s.getColor().getBlue() + "\"";
                    }
                    if (s.getFillColor() != null) {
                        a += ",\"fillcolor\": \"" + s.getFillColor().getRed() + "," + s.getFillColor().getGreen() + "," + s.getFillColor().getBlue() + "\"";
                    }
                    a += "},";
                    json += a;
                }
            }
            t="";
            for(int i=0;i<json.length()-1;i++){
                t+=json.charAt(i);
            }
            json=t;
            if(f)json+="[";
            json+="]";
            f=true;
            json+= ", \"Line\": ";
            for (int i = 0; i < shapes.size(); i++) {
                Shape s = shapes.get(i);
                if (s instanceof Line) {
                    //   if(!start&&f)json+=",";
                    if(f)json+="[";
                    f=false;
                    String a = "{";
                    HashMap<String, Double> p = (HashMap<String, Double>) s.getProperties();
                    a+="\"point1x\": "+p.get("point1x")+",";
                    a+="\"point1y\": "+p.get("point1y")+",";
                    a+="\"point2x\": "+p.get("point2x")+",";
                    a+="\"point2y\": "+p.get("point2y");
                    if (s.getColor() != null) {
                        a += ",\"color\": \"" + s.getColor().getRed() + "," + s.getColor().getGreen() + "," + s.getColor().getBlue() + "\"";
                    }

                    a += "},";
                    json += a;
                }
            }
            t="";
            for(int i=0;i<json.length()-1;i++){
                t+=json.charAt(i);
            }
            json=t;
            if(f)json+="[";
            json+="]";
            f=true;
            json+= ", \"Triangle\": ";
            for (int i = 0; i < shapes.size(); i++) {
                Shape s = shapes.get(i);
                if (s instanceof Triangle) {
                    if(f)json+="[";
                    f=false;
                    String a = "{";
                    HashMap<String, Double> p = (HashMap<String, Double>) s.getProperties();
                    a+="\"point1x\": "+p.get("point1x")+",";
                    a+="\"point1y\": "+p.get("point1y")+",";
                    a+="\"point2x\": "+p.get("point2x")+",";
                    a+="\"point2y\": "+p.get("point2y")+",";
                    a+="\"point3x\": "+p.get("point3x")+",";
                    a+="\"point3y\": "+p.get("point3y");
                    if (s.getColor() != null) {
                        a += ",\"color\": \"" + s.getColor().getRed() + "," + s.getColor().getGreen() + "," + s.getColor().getBlue() + "\"";
                    }
                    if (s.getFillColor() != null) {
                        a += ",\"fillcolor\": \"" + s.getFillColor().getRed() + "," + s.getFillColor().getGreen() + "," + s.getFillColor().getBlue() + "\"";
                    }
                    a += "},";
                    json += a;
                }
            }
            t="";
            for(int i=0;i<json.length()-1;i++){
                t+=json.charAt(i);
            }
            json=t;
            if(f)json+="[";
            json+="]";
            json+="}";
            System.out.println(json);

            PrintWriter printWriter  = null;
            try {
                printWriter = new PrintWriter(new File(path+".json"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            printWriter.println(json);
            printWriter.flush();
        }

    }
    @Override
    public void load(String path) {

        LinkedList<Shape> temps =new LinkedList<>();
        if(path.contains(".xml"))
        if(false)
       try {


            File inputFile = new File(path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = null;
            try {
                dBuilder = dbFactory.newDocumentBuilder();
            } catch (ParserConfigurationException ex) {
                ex.printStackTrace();
            }
            Document doc = null;
            try {
                doc = dBuilder.parse(inputFile);
            } catch (SAXException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            doc.getDocumentElement().normalize();
            // System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("Circle");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                System.out.println("\nCurrent Element :" + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    int x = (int)Double.parseDouble(eElement.getAttribute("x"));
                    int y = (int)Double.parseDouble(eElement.getAttribute("y"));
//                    int cx = (int)Double.parseDouble(eElement.getAttribute("cx"));
//                    int cy = (int)Double.parseDouble(eElement.getAttribute("cy"));
                    int r = (int)Double.parseDouble(eElement.getAttribute("r"));
                    System.out.println(x);
                    Color color = Color.BLACK;
                    Color colorBACK = new Color(0, 0, 0, 0);
                    if (!eElement.getAttribute("color").equals("")) {
                        // System.out.println("color :" + eElement.getAttribute("color"));
                        String[] c = eElement.getAttribute("color").split(",");
                        if (c.length == 3)
                            color = new Color(Integer.parseInt(c[0]), Integer.parseInt(c[1]), Integer.parseInt(c[2]));
                        else
                            color = new Color(Integer.parseInt(c[0]), Integer.parseInt(c[1]), Integer.parseInt(c[2]), Integer.parseInt(c[3]));

                    }
                    if (eElement.getAttribute("fillcolor") != "") {
                        //System.out.println("fillcolor :" + eElement.getAttribute("color"));
                        String[] c = eElement.getAttribute("fillcolor").split(",");
                        if (c.length == 3)
                            colorBACK = new Color(Integer.parseInt(c[0]), Integer.parseInt(c[1]), Integer.parseInt(c[2]));
                        else
                            colorBACK = new Color(Integer.parseInt(c[0]), Integer.parseInt(c[1]), Integer.parseInt(c[2]), Integer.parseInt(c[3]));
                    }
                    Circle circle = new Circle(new Point(x, y), r, colorBACK, color);
                    //mainFrame.add(circle);
                    temps.add(circle);
                }
            }

            nList = doc.getElementsByTagName("Square");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                System.out.println("\nCurrent Element :" + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    int x = (int)Double.parseDouble(eElement.getAttribute("headX"));
                    int y = (int)Double.parseDouble(eElement.getAttribute("headY"));
                    int cx = (int)Double.parseDouble(eElement.getAttribute("courserX"));
                    int cy = (int)Double.parseDouble(eElement.getAttribute("courserY"));
                    Color color = Color.BLACK;
                    Color colorBACK = new Color(0, 0, 0, 0);
                    if (eElement.getAttribute("color") != "") {
                        // System.out.println("color :" + eElement.getAttribute("color"));
                        String[] c = eElement.getAttribute("color").split(",");
                        if (c.length == 3)
                            color = new Color(Integer.parseInt(c[0]), Integer.parseInt(c[1]), Integer.parseInt(c[2]));
                        else
                            color = new Color(Integer.parseInt(c[0]), Integer.parseInt(c[1]), Integer.parseInt(c[2]), Integer.parseInt(c[3]));

                    }
                    if (eElement.getAttribute("fillcolor") != "") {
                        //System.out.println("fillcolor :" + eElement.getAttribute("color"));
                        String[] c = eElement.getAttribute("fillcolor").split(",");
                        if (c.length == 3)
                            colorBACK = new Color(Integer.parseInt(c[0]), Integer.parseInt(c[1]), Integer.parseInt(c[2]));
                        else
                            colorBACK = new Color(Integer.parseInt(c[0]), Integer.parseInt(c[1]), Integer.parseInt(c[2]), Integer.parseInt(c[3]));
                    }
                    Square square = new Square(new Point(x, y), new Point(cx, cy), colorBACK, color);
                    //mainFrame.add(circle);
                    temps.add(square);
                }
            }



            nList = doc.getElementsByTagName("Rectangle");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                System.out.println("\nCurrent Element :" + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    int x = (int)Double.parseDouble(eElement.getAttribute("headX"));
                    int y = (int)Double.parseDouble(eElement.getAttribute("headY"));
                    int cx = (int)Double.parseDouble(eElement.getAttribute("courserX"));
                    int cy = (int)Double.parseDouble(eElement.getAttribute("courserY"));
                    Color color = Color.BLACK;
                    Color colorBACK = new Color(0, 0, 0, 0);
                    if (eElement.getAttribute("color") != "") {
                        System.out.println(eElement.getAttribute("color"));
                        // System.out.println("color :" + eElement.getAttribute("color"));
                        String[] c = eElement.getAttribute("color").split(",");
                        if (c.length == 3)
                            color = new Color(Integer.parseInt(c[0]), Integer.parseInt(c[1]), Integer.parseInt(c[2]));
                        else
                            color = new Color(Integer.parseInt(c[0]), Integer.parseInt(c[1]), Integer.parseInt(c[2]), Integer.parseInt(c[3]));

                    }
                    if (eElement.getAttribute("fillcolor") != "") {
                        //System.out.println("fillcolor :" + eElement.getAttribute("color"));
                        String[] c = eElement.getAttribute("fillcolor").split(",");
                        if (c.length == 3)
                            colorBACK = new Color(Integer.parseInt(c[0]), Integer.parseInt(c[1]), Integer.parseInt(c[2]));
                        else
                            colorBACK = new Color(Integer.parseInt(c[0]), Integer.parseInt(c[1]), Integer.parseInt(c[2]), Integer.parseInt(c[3]));
                    }
                    Rectangle rectangle = new Rectangle(new Point(x, y), new Point(cx, cy), colorBACK, color);
                    //mainFrame.add(circle);
                    temps.add(rectangle);
                }
            }
            nList = doc.getElementsByTagName("Oval");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                System.out.println("\nCurrent Element :" + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    int x = (int)Double.parseDouble(eElement.getAttribute("headX"));
                    int y = (int)Double.parseDouble(eElement.getAttribute("headY"));
                    int cx = (int)Double.parseDouble(eElement.getAttribute("courserX"));
                    int cy = (int)Double.parseDouble(eElement.getAttribute("courserY"));
                    Color color = Color.BLACK;
                    Color colorBACK = new Color(0, 0, 0, 0);
                    if (eElement.getAttribute("color") != "") {
                        // System.out.println("color :" + eElement.getAttribute("color"));
                        String[] c = eElement.getAttribute("color").split(",");
                        if (c.length == 3)
                            color = new Color(Integer.parseInt(c[0]), Integer.parseInt(c[1]), Integer.parseInt(c[2]));
                        else
                            color = new Color(Integer.parseInt(c[0]), Integer.parseInt(c[1]), Integer.parseInt(c[2]), Integer.parseInt(c[3]));

                    }
                    if (eElement.getAttribute("fillcolor") != "") {
                        //System.out.println("fillcolor :" + eElement.getAttribute("color"));
                        String[] c = eElement.getAttribute("fillcolor").split(",");
                        if (c.length == 3)
                            colorBACK = new Color(Integer.parseInt(c[0]), Integer.parseInt(c[1]), Integer.parseInt(c[2]));
                        else
                            colorBACK = new Color(Integer.parseInt(c[0]), Integer.parseInt(c[1]), Integer.parseInt(c[2]), Integer.parseInt(c[3]));
                    }
                    Oval oval = new Oval(new Point(x, y), new Point(cx, cy), colorBACK, color);
                    //mainFrame.add(circle);
                    temps.add(oval);
                }
            }

            nList = doc.getElementsByTagName("Line");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                System.out.println("\nCurrent Element :" + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    int x = (int)Double.parseDouble(eElement.getAttribute("point1x"));
                    int y = (int)Double.parseDouble(eElement.getAttribute("point1y"));
                    int cx = (int)Double.parseDouble(eElement.getAttribute("point2x"));
                    int cy = (int)Double.parseDouble(eElement.getAttribute("point2y"));
                    Color color = Color.BLACK;
                    Color colorBACK = new Color(0, 0, 0, 0);
                    if (eElement.getAttribute("color") != "") {
                        // System.out.println("color :" + eElement.getAttribute("color"));
                        String[] c = eElement.getAttribute("color").split(",");
                        if (c.length == 3)
                            color = new Color(Integer.parseInt(c[0]), Integer.parseInt(c[1]), Integer.parseInt(c[2]));
                        else
                            color = new Color(Integer.parseInt(c[0]), Integer.parseInt(c[1]), Integer.parseInt(c[2]), Integer.parseInt(c[3]));

                    }
                    if (eElement.getAttribute("fillcolor") != "") {
                        //System.out.println("fillcolor :" + eElement.getAttribute("color"));
                        String[] c = eElement.getAttribute("fillcolor").split(",");
                        if (c.length == 3)
                            colorBACK = new Color(Integer.parseInt(c[0]), Integer.parseInt(c[1]), Integer.parseInt(c[2]));
                        else
                            colorBACK = new Color(Integer.parseInt(c[0]), Integer.parseInt(c[1]), Integer.parseInt(c[2]), Integer.parseInt(c[3]));
                    }
                    Line line = new Line(new Point(x, y), new Point(cx, cy), color, colorBACK);
                    //mainFrame.add(circle);
                    temps.add(line);
                }
            }
            nList = doc.getElementsByTagName("Triangle");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                System.out.println("\nCurrent Element :" + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    int x = (int)Double.parseDouble(eElement.getAttribute("point1x"));
                    int y = (int)Double.parseDouble(eElement.getAttribute("point1y"));
                    int cx = (int)Double.parseDouble(eElement.getAttribute("point2x"));
                    int cy = (int)Double.parseDouble(eElement.getAttribute("point2y"));
                    int cx3 = (int)Double.parseDouble(eElement.getAttribute("point3x"));
                    int cy3 = (int)Double.parseDouble(eElement.getAttribute("point3y"));

                    Color color = Color.BLACK;
                    Color colorBACK = new Color(0, 0, 0, 0);
                    if (eElement.getAttribute("color") != "") {
                        // System.out.println("color :" + eElement.getAttribute("color"));
                        String[] c = eElement.getAttribute("color").split(",");
                        if (c.length == 3)
                            color = new Color(Integer.parseInt(c[0]), Integer.parseInt(c[1]), Integer.parseInt(c[2]));
                        else
                            color = new Color(Integer.parseInt(c[0]), Integer.parseInt(c[1]), Integer.parseInt(c[2]), Integer.parseInt(c[3]));
                    }
                    if (eElement.getAttribute("fillcolor") != "") {
                        //System.out.println("fillcolor :" + eElement.getAttribute("color"));
                        String[] c = eElement.getAttribute("fillcolor").split(",");
                        if (c.length == 3)
                            colorBACK = new Color(Integer.parseInt(c[0]), Integer.parseInt(c[1]), Integer.parseInt(c[2]));
                        else
                            colorBACK = new Color(Integer.parseInt(c[0]), Integer.parseInt(c[1]), Integer.parseInt(c[2]), Integer.parseInt(c[3]));
                    }
                    Triangle triangle = new Triangle(new Point(x, y), new Point(cx, cy), new Point(cx3, cy3), color, colorBACK);
                    //mainFrame.add(circle);
                    temps.add(triangle);
                }
            }
            deleteAll();
             shapes=temps;
            for(Shape s :temps){
                try {
                    System.out.println(s.getProperties());
                    s.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                MainWindow.mainFrame.setVisible(true);
                MainWindow.mainFrame.repaint();
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(mainFrame, "file is Not allowed");
        }
        else {
             try{

            String data="";
            try {
                 data = new String(Files.readAllBytes(Paths.get(path)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(!path.contains(".json"))throw new Exception();
            String aa[] = data.split("\\[");
            System.out.println(data);
            for (int i = 1; i < aa.length; i++) {
                if (i == 1) { // cir
                    String x = aa[i];
                    String o[] = x.split("\\{");
                    for (int j = 1; j < o.length; j++) {
                        System.out.println("e"+o[j]);
                        String e = o[j];
                        String[] g = e.split(":");
                        int xx = 0, yy = 0, rr = 0;
                        xx = (int) Double.parseDouble(g[1].split(",")[0]);
                        yy = (int) Double.parseDouble(g[2].split(",")[0]);
                        rr = (int) Double.parseDouble(g[3].split(",")[0]);
                        Color color = Color.BLACK;
                        Color colorBACK = new Color(0, 0, 0, 0);

                        if(g.length!=3){
                            String h[]=g[4].split("\\\"")[1].split(",");
                            if(h.length==3){ // R G B
                                color=new Color(Integer.parseInt(h[0]),Integer.parseInt(h[1]),Integer.parseInt(h[2]));
                            }else {
                                color=new Color(Integer.parseInt(h[0]),Integer.parseInt(h[1]),Integer.parseInt(h[2]),Integer.parseInt(h[3]));

                            }
                        }
                        if(g.length!=4){
                            String h[]=g[5].split("\\\"")[1].split(",");
                            if(h.length==3){ // R G B
                                colorBACK=new Color(Integer.parseInt(h[0]),Integer.parseInt(h[1]),Integer.parseInt(h[2]));
                            }else {
                                colorBACK=new Color(Integer.parseInt(h[0]),Integer.parseInt(h[1]),Integer.parseInt(h[2]),Integer.parseInt(h[3]));

                            }
                        }
                        temps.add(new Circle(new Point(xx,yy),rr,colorBACK,color));
                        System.out.println(xx + " " + yy + " " + rr+ " "+color+" "+colorBACK);
                    }
                    System.out.println("------------------------");

                }
                else if (i == 2) { // squ
                    String x = aa[i];
                    String o[] = x.split("\\{");
                    for (int j = 1; j < o.length; j++) {
                        System.out.println("e"+o[j]);
                        String e = o[j];
                        String[] g = e.split(":");
                        int hx = 0, hy = 0, cx = 0,cy=0;
                        hx = (int) Double.parseDouble(g[1].split(",")[0]);
                        hy = (int) Double.parseDouble(g[2].split(",")[0]);
                        cx = (int) Double.parseDouble(g[3].split(",")[0]);
                        cy = (int) Double.parseDouble(g[4].split(",")[0]);
                        Color color = Color.BLACK;
                        Color colorBACK = new Color(0, 0, 0, 0);

                        if(g.length!=4){
                            String h[]=g[5].split("\\\"")[1].split(",");
                            if(h.length==3){ // R G B
                                color=new Color(Integer.parseInt(h[0]),Integer.parseInt(h[1]),Integer.parseInt(h[2]));
                            }else {
                                color=new Color(Integer.parseInt(h[0]),Integer.parseInt(h[1]),Integer.parseInt(h[2]),Integer.parseInt(h[3]));

                            }
                        }
                        if(g.length!=5){
                            String h[]=g[6].split("\\\"")[1].split(",");
                            if(h.length==3){ // R G B
                                colorBACK=new Color(Integer.parseInt(h[0]),Integer.parseInt(h[1]),Integer.parseInt(h[2]));
                            }else {
                                colorBACK=new Color(Integer.parseInt(h[0]),Integer.parseInt(h[1]),Integer.parseInt(h[2]),Integer.parseInt(h[3]));

                            }
                        }
                        //temps.add(new Circle(new Point(xx,yy),rr,colorBACK,color));
                        temps.add(new Square(new Point(hx,hy),new Point(cx,cy),colorBACK,color));
                        System.out.println("square");
                        System.out.println(hx + " " + hy + " " + cx+ " "+cy+" " +color+" "+colorBACK);
                    }
                    System.out.println("------------------------");

                }
                else if (i == 3) { // rec
                    String x = aa[i];
                    String o[] = x.split("\\{");
                    for (int j = 1; j < o.length; j++) {
                        System.out.println("e"+o[j]);
                        String e = o[j];
                        String[] g = e.split(":");
                        int hx = 0, hy = 0, cx = 0,cy=0;
                        hx = (int) Double.parseDouble(g[1].split(",")[0]);
                        hy = (int) Double.parseDouble(g[2].split(",")[0]);
                        cx = (int) Double.parseDouble(g[3].split(",")[0]);
                        cy = (int) Double.parseDouble(g[4].split(",")[0]);
                        Color color = Color.BLACK;
                        Color colorBACK = new Color(0, 0, 0, 0);

                        if(g.length!=4){
                            String h[]=g[5].split("\\\"")[1].split(",");
                            if(h.length==3){ // R G B
                                color=new Color(Integer.parseInt(h[0]),Integer.parseInt(h[1]),Integer.parseInt(h[2]));
                            }else {
                                color=new Color(Integer.parseInt(h[0]),Integer.parseInt(h[1]),Integer.parseInt(h[2]),Integer.parseInt(h[3]));

                            }
                        }
                        if(g.length!=5){
                            String h[]=g[6].split("\\\"")[1].split(",");
                            if(h.length==3){ // R G B
                                colorBACK=new Color(Integer.parseInt(h[0]),Integer.parseInt(h[1]),Integer.parseInt(h[2]));
                            }else {
                                colorBACK=new Color(Integer.parseInt(h[0]),Integer.parseInt(h[1]),Integer.parseInt(h[2]),Integer.parseInt(h[3]));

                            }
                        }
                        //temps.add(new Circle(new Point(xx,yy),rr,colorBACK,color));
                        temps.add(new Rectangle(new Point(hx,hy),new Point(cx,cy),colorBACK,color));
                        System.out.println("rec");
                        System.out.println(hx + " " + hy + " " + cx+ " "+cy+" " +color+" "+colorBACK);
                    }
                    System.out.println("------------------------");

                }else if (i == 4) { // oval
                    String x = aa[i];
                    String o[] = x.split("\\{");
                    for (int j = 1; j < o.length; j++) {
                        System.out.println("e"+o[j]);
                        String e = o[j];
                        String[] g = e.split(":");
                        int hx = 0, hy = 0, cx = 0,cy=0;
                        hx = (int) Double.parseDouble(g[1].split(",")[0]);
                        hy = (int) Double.parseDouble(g[2].split(",")[0]);
                        cx = (int) Double.parseDouble(g[3].split(",")[0]);
                        cy = (int) Double.parseDouble(g[4].split(",")[0]);
                        Color color = Color.BLACK;
                        Color colorBACK = new Color(0, 0, 0, 0);

                        if(g.length!=4){
                            String h[]=g[5].split("\\\"")[1].split(",");
                            if(h.length==3){ // R G B
                                color=new Color(Integer.parseInt(h[0]),Integer.parseInt(h[1]),Integer.parseInt(h[2]));
                            }else {
                                color=new Color(Integer.parseInt(h[0]),Integer.parseInt(h[1]),Integer.parseInt(h[2]),Integer.parseInt(h[3]));

                            }
                        }
                        if(g.length!=5){
                            String h[]=g[6].split("\\\"")[1].split(",");
                            if(h.length==3){ // R G B
                                colorBACK=new Color(Integer.parseInt(h[0]),Integer.parseInt(h[1]),Integer.parseInt(h[2]));
                            }else {
                                colorBACK=new Color(Integer.parseInt(h[0]),Integer.parseInt(h[1]),Integer.parseInt(h[2]),Integer.parseInt(h[3]));

                            }
                        }
                        //temps.add(new Circle(new Point(xx,yy),rr,colorBACK,color));
                        temps.add(new Oval(new Point(hx,hy),new Point(cx,cy),colorBACK,color));
                        System.out.println("oval");
                        System.out.println(hx + " " + hy + " " + cx+ " "+cy+" " +color+" "+colorBACK);
                    }
                    System.out.println("------------------------");

                }else if (i == 5) { // line
                    String x = aa[i];
                    String o[] = x.split("\\{");
                    for (int j = 1; j < o.length; j++) {
                        System.out.println("e"+o[j]);
                        String e = o[j];
                        String[] g = e.split(":");
                        int hx = 0, hy = 0, cx = 0,cy=0;
                        hx = (int) Double.parseDouble(g[1].split(",")[0]);
                        hy = (int) Double.parseDouble(g[2].split(",")[0]);
                        cx = (int) Double.parseDouble(g[3].split(",")[0]);
                        cy = (int) Double.parseDouble(g[4].split(",")[0]);
                        Color color = Color.BLACK;
                        Color colorBACK = new Color(0, 0, 0, 0);

                        if(g.length!=4){
                            String h[]=g[5].split("\\\"")[1].split(",");
                            if(h.length==3){ // R G B
                                color=new Color(Integer.parseInt(h[0]),Integer.parseInt(h[1]),Integer.parseInt(h[2]));
                            }else {
                                color=new Color(Integer.parseInt(h[0]),Integer.parseInt(h[1]),Integer.parseInt(h[2]),Integer.parseInt(h[3]));

                            }
                        }

                        //temps.add(new Circle(new Point(xx,yy),rr,colorBACK,color));
                        temps.add(new Line(new Point(hx,hy),new Point(cx,cy),colorBACK,color));
                        System.out.println("Line");
                        System.out.println(hx + " " + hy + " " + cx+ " "+cy+" " +color+" "+colorBACK);
                    }
                    System.out.println("------------------------");

                }else if (i == 6) { // triangle
                    String x = aa[i];
                    String o[] = x.split("\\{");
                    for (int j = 1; j < o.length; j++) {
                        System.out.println("e"+o[j]);
                        String e = o[j];
                        String[] g = e.split(":");
                        int hx = 0, hy = 0, cx = 0,cy=0,cxx=0,cyy=0;
                        hx = (int) Double.parseDouble(g[1].split(",")[0]);
                        hy = (int) Double.parseDouble(g[2].split(",")[0]);
                        cx = (int) Double.parseDouble(g[3].split(",")[0]);
                        cy = (int) Double.parseDouble(g[4].split(",")[0]);
                        cxx = (int) Double.parseDouble(g[5].split(",")[0]);
                        cyy= (int) Double.parseDouble(g[6].split(",")[0]);

                        Color color = Color.BLACK;
                        Color colorBACK = new Color(0, 0, 0, 0);

                        if(g.length!=6){
                            String h[]=g[7].split("\\\"")[1].split(",");
                            if(h.length==3){ // R G B
                                color=new Color(Integer.parseInt(h[0]),Integer.parseInt(h[1]),Integer.parseInt(h[2]));
                            }else {
                                color=new Color(Integer.parseInt(h[0]),Integer.parseInt(h[1]),Integer.parseInt(h[2]),Integer.parseInt(h[3]));

                            }
                        }
                        if(g.length!=7){
                            String h[]=g[8].split("\\\"")[1].split(",");
                            if(h.length==3){ // R G B
                                colorBACK=new Color(Integer.parseInt(h[0]),Integer.parseInt(h[1]),Integer.parseInt(h[2]));
                            }else {
                                colorBACK=new Color(Integer.parseInt(h[0]),Integer.parseInt(h[1]),Integer.parseInt(h[2]),Integer.parseInt(h[3]));

                            }
                        }

                        //temps.add(new Circle(new Point(xx,yy),rr,colorBACK,color));
                        temps.add(new Triangle(new Point(hx,hy),new Point(cx,cy),new Point(cxx,cyy),colorBACK,color));
                        System.out.println("Triangle");
                        System.out.println(hx + " " + hy + " " + cx+ " "+cy+" " +cxx+" "+cyy+" "+color+" "+colorBACK);
                    }
                    System.out.println("------------------------");

                }

            }

            }catch (Exception e){
                JOptionPane.showMessageDialog(mainFrame, "file is Not allowed");
            }
            deleteAll();
            shapes=temps;
            for(Shape s :temps){
                try {
                    System.out.println(s.getProperties());
                    s.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                MainWindow.mainFrame.setVisible(true);
                MainWindow.mainFrame.repaint();
            }

        }
        return;
    }

    private void deleteAll() {
        System.out.println(shapes.size());
        for(Shape s:shapes){
            if(s instanceof Circle){
                try {
                    ((Circle)s).remove();
                }catch (Exception e){
                    mainFrame.remove((Component)s);
                }
            }
            else if(s instanceof Square){
                try {
                    ((Square)s).remove();
                }catch (Exception e){
                    mainFrame.remove((Component)s);
                }
            }
            else if(s instanceof Rectangle){
                try {
                    ((Rectangle)s).remove();
                }catch (Exception e){
                    mainFrame.remove((Component)s);
                }
            }
            else if(s instanceof Oval){
                try {
                    ((Oval)s).remove();
                }catch (Exception e){
                    mainFrame.remove((Component)s);
                }
            }
            else if(s instanceof Triangle){
                try {
                    ((Triangle)s).remove();
                }catch (Exception e){
                    mainFrame.remove((Component)s);
                }
            }
            else if(s instanceof Line){
                try {
                    ((Line)s).remove();
                }catch (Exception e){
                    mainFrame.remove((Component)s);
                }
            }

        }

    }
}

