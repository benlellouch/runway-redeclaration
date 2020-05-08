package XMLParsing;

import Model.*;
import Model.Obstacle;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ModelFactory {

    private File file;
    private List<Airport> airports;
    private List<Obstacle> obstacles;

    public ModelFactory(File file) {
        this.file = file;
        airports = new ArrayList<>();
        obstacles = new ArrayList<>();
        this.readFile();
    }

    public List<Airport> getAirports() {
        return airports;
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    private void readFile(){
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();

            NodeList airportList = doc.getElementsByTagName("Airport");
            NodeList obstacleList = doc.getElementsByTagName("Obstacle");

            makeAirports(airportList);
            makeObstacle(obstacleList);


        }catch (ParserConfigurationException e){
            e.printStackTrace();
            System.err.println("Parse config error");
        }catch (SAXException e){
            e.printStackTrace();
            System.err.println("SAX error");
        }catch (IOException e){
            e.printStackTrace();
            System.err.println("IO error");
        }
    }

    private void makeAirports(NodeList a){
        for(int n = 0; n < a.getLength(); n++){
            Node nNode = a.item(n);


            if(nNode.getNodeType() == Node.ELEMENT_NODE){
                Element element = (Element) nNode;
                String name = element.getAttribute("name");
                Airport tempAirport = new Airport(name);
                airports.add(tempAirport);

                NodeList runways = element.getElementsByTagName("Runway");
                for(Runway r : makeRunways(runways)){
                    tempAirport.addRunway(r);
                }

            }
        }
    }

    private ArrayList<Runway> makeRunways(NodeList r){
        ArrayList<Runway> runwayList = new ArrayList<>();
        for(int n = 0; n< r.getLength(); n++){
            Node node = r.item(n);
            if(node.getNodeType() == Node.ELEMENT_NODE){
                Element e = (Element) node;
                NodeList logicRun = e.getElementsByTagName("LogicalRunway");
                Runway runway = new Runway(makeLogicals(logicRun.item(0)),makeLogicals(logicRun.item(1)));
                runwayList.add(runway);
            }
        }
        return runwayList;
    }

    private LogicalRunway makeLogicals(Node l){
        if(l.getNodeType() == Node.ELEMENT_NODE) {
            Element e = (Element) l;
            String degree = e.getAttribute("degree");
            String direction = e.getElementsByTagName("direction").item(0).getTextContent();
            int tora = Integer.parseInt(e.getElementsByTagName("TORA").item(0).getTextContent());
            int toda = Integer.parseInt(e.getElementsByTagName("TODA").item(0).getTextContent());
            int asda = Integer.parseInt(e.getElementsByTagName("ASDA").item(0).getTextContent());
            int lda = Integer.parseInt(e.getElementsByTagName("LDA").item(0).getTextContent());

            LogicalRunway logicalRunway = new LogicalRunway(degree + direction, tora, toda, asda, lda);
            return logicalRunway;
        }else return null;
    }

    private void makeObstacle(NodeList o){
        for(int n = 0; n < o.getLength(); n++){
            Node node = o.item(n);
            if(node.getNodeType() == Node.ELEMENT_NODE){
                Element e = (Element) node;
                String name = e.getAttribute("name");
                int height = Integer.parseInt(e.getElementsByTagName("height").item(0).getTextContent());
                int width = Integer.parseInt(e.getElementsByTagName("width").item(0).getTextContent());
                Obstacle obstacle = new Obstacle(name,height,width);
                obstacles.add(obstacle);
            }
        }
    }

}
