package AirportItems;

import java.util.ArrayList;

public class Airport {

    private String name;
    private ArrayList<Runways> runwayList;

    public Airport(String name){
        this.name = name;
        runwayList = new ArrayList<>();
    }

    public String getName(){
        return name;
    }

    public ArrayList getRunways(){
        return runwayList;
    }

    public void addRunway(Runways runway){
        runwayList.add(runway);
    }

}
