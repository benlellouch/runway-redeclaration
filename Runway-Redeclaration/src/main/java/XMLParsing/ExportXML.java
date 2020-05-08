package XMLParsing;

import Model.*;
import Model.Obstacle;
import javafx.collections.ObservableList;
import org.w3c.dom.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;


public class ExportXML {

    private File file;
    private ObservableList<Airport> airports;
    private ObservableList<Obstacle> obstacles;

    public ExportXML(File file, ObservableList<Airport> airports, ObservableList<Obstacle> obstacles){
        this.file = file;
        this.airports = airports;
        this.obstacles = obstacles;
    }

    public void saveToXML(){
        try {
            //initialise
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.newDocument();

            //root element
            Element root = doc.createElement("Models");
            doc.appendChild(root);

            //prepare airport list for xml
            airportToXML(doc,root);

            //prepare obstacle list for xml
            obstacleToXML(doc,root);

            //create XML file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT,"yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "5");

            DOMSource domSource = new DOMSource(doc);
            StreamResult streamResult = new StreamResult(file);
            transformer.transform(domSource,streamResult);


        }catch (ParserConfigurationException | TransformerException e){
            e.printStackTrace();
        }
    }

    private void airportToXML(Document doc, Element root){

        for (Airport a : airports){
            //airport elements
            Element airport = doc.createElement("Airport");
            airport.setAttribute("name",a.getName());
            root.appendChild(airport);

            for(Runway r : a.getRunways()){
                //prepare each runway of an airport for xml
                runwayToXML(r,doc,airport);
            }
        }

    }

    private void runwayToXML(Runway runway, Document doc, Element airport){
        //runway element
        Element runwayElem = doc.createElement("Runway");
        airport.appendChild(runwayElem);

        //prepare logical runway 1 for xml
        LogicalRunway lr1 = runway.getLogicalRunway1();
        logicalToXML(lr1,doc,runwayElem);

        //prepare logical runway 2 for xml
        LogicalRunway lr2 = runway.getLogicalRunway2();
        logicalToXML(lr2,doc,runwayElem);
    }

    private void logicalToXML(LogicalRunway lr, Document doc, Element runway){
        //logical runway element
        Element lrElem = doc.createElement("LogicalRunway");
        lrElem.setAttribute("degree",lr.getDegreeString());
        runway.appendChild(lrElem);

        //direction element
        Element direction = doc.createElement("direction");
        direction.appendChild(doc.createTextNode(lr.getDesignation()));
        lrElem.appendChild(direction);

        //TORA element
        Element tora = doc.createElement("TORA");
        tora.appendChild(doc.createTextNode(Integer.toString(lr.getTora())));
        lrElem.appendChild(tora);

        //TODA element
        Element toda = doc.createElement("TODA");
        toda.appendChild(doc.createTextNode(Integer.toString(lr.getToda())));
        lrElem.appendChild(toda);

        //ASDA element
        Element asda = doc.createElement("ASDA");
        asda.appendChild(doc.createTextNode(Integer.toString(lr.getAsda())));
        lrElem.appendChild(asda);

        //LDA element
        Element lda = doc.createElement("LDA");
        lda.appendChild(doc.createTextNode(Integer.toString(lr.getLda())));
        lrElem.appendChild(lda);
    }

    private void obstacleToXML(Document doc, Element root){

        for(Obstacle o : obstacles){
            //obstacle element
            Element obstacleElem = doc.createElement("Obstacle");
            obstacleElem.setAttribute("name",o.getName());
            root.appendChild(obstacleElem);

            //obstacle's height element
            Element height = doc.createElement("height");
            height.appendChild(doc.createTextNode(Integer.toString(o.getHeight())));
            obstacleElem.appendChild(height);

            //obstacle's width element
            Element width = doc.createElement("width");
            width.appendChild(doc.createTextNode(Integer.toString(o.getWidth())));
            obstacleElem.appendChild(width);
        }
    }
}
