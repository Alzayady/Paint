package eg.edu.alexu.csd.oop.draw;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class Main {

    public static void main(String[] args) {

     new MainWindow();
        try {
            File inputFile = new File("ahmed.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
           // System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("Circle");
            System.out.println("----------------------------");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                System.out.println("\nCurrent Element :" + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    System.out.println("x :" +  eElement.getAttribute("x"));
                    System.out.println("x :" + eElement.getAttribute("y"));
                    System.out.println("x :" + eElement.getAttribute("r"));
                    if(eElement.getAttribute("color")!=""){
                        System.out.println("color :" + eElement.getAttribute("color"));
                    }
                    if(eElement.getAttribute("fillcolor")!=""){
                        System.out.println("fillcolor :" + eElement.getAttribute("color"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

