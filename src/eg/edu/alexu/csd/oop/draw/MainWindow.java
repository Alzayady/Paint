package eg.edu.alexu.csd.oop.draw;
import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public  class  MainWindow  {
    static JFrame mainFrame;
    static MouseMotionListener mouseMotionListener;
    static MouseListener mouseListener;
    private MouseListener MouseListenerSelect = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            Point PointClicked = mouseEvent.getPoint();
            mainFrame.removeMouseListener(MouseListenerSelect);
            Shape f ;
            try{
               f=(Shape) mainFrame.getComponentAt(PointClicked);
                f.setPosition(PointClicked);
                mainFrame.removeMouseListener(MouseListenerSelect);
            }catch (Exception e){
                System.out.println("fff");
            }

        }
        @Override
        public void mousePressed(MouseEvent mouseEvent) {
        }
        @Override
        public void mouseReleased(MouseEvent mouseEvent) {
        }

        @Override
        public void mouseEntered(MouseEvent mouseEvent) {
        }

        @Override
        public void mouseExited(MouseEvent mouseEvent) {
        }
    };

    public  MainWindow() {
        mainFrame = new JFrame("Draw");
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception e ){
            e.printStackTrace();
        }
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("file");
        JMenu editMenu = new JMenu("edit");
        JMenu shapesMenu = new JMenu("shapes");

        JMenuItem delete = new JMenuItem("delete");
        delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               // mainFrame.addMouseListener();
            }
        });
        JMenuItem resize = new JMenuItem("resize");
        JMenuItem changeColor = new JMenuItem("change color");
        JMenuItem rePosition = new JMenuItem("change position");
        rePosition.addActionListener(e->{
                mainFrame.addMouseListener(MouseListenerSelect);
        });
        JMenuItem save = new JMenuItem("save");
        JMenuItem load = new JMenuItem("open");
        JMenuItem circle = new JMenuItem("circle");
        circle.addActionListener(e -> {
            new Circle();
        });

        JMenuItem square = new JMenuItem("square");
        JMenuItem triangle = new JMenuItem("triangle");
        JMenuItem rectangle = new JMenuItem("rectangle");
        JMenuItem hexagonal = new JMenuItem("hexagonal");
        JMenuItem octagon = new JMenuItem("octagon");
        JMenuItem newFile = new JMenuItem("new");

        fileMenu.add(save);
        fileMenu.add(load);
        fileMenu.add(newFile);
        editMenu.add(delete);
        editMenu.add(resize);
        editMenu.add(rePosition);
        editMenu.add(changeColor);
        shapesMenu.add(circle);
        shapesMenu.add(square);
        shapesMenu.add(triangle);
        shapesMenu.add(rectangle);
        shapesMenu.add(hexagonal);
        shapesMenu.add(octagon);
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(shapesMenu);
        this.mainFrame.setJMenuBar(menuBar);
        mainFrame.setBackground(Color.white);
        mainFrame.setVisible(true);
    }



//    @Override
//    public void actionPerformed(ActionEvent e) {
//        String textOfComponent = e.getActionCommand();
//        if (textOfComponent.equals("circle")){
//            drawCircle();
//        } else if (textOfComponent.equals("new")){
//            this.mainFrame.dispose();
//            new MainWindow();
//        }
//    }


}


