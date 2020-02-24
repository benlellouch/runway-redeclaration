package AirportItems;

import java.util.ArrayList;

public class Runways {

    private ArrayList<LogicalRunways> logicRunList;
    private String name;

    public Runways(String name){
        this.name = name;
    }

    public ArrayList<LogicalRunways> getLogicRunList() {
        return logicRunList;
    }

    public String getName() {
        return name;
    }
}
