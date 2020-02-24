package AirportItems;

import java.util.ArrayList;

public class Runway {

    private ArrayList<LogicalRunway> logicRunList;
    private String name;
    private int degree; //degree of direction
    private int degree2; //for opposite logical runway
    private String position; //L, R or C representing position of runway
    private String position2; //for opposite runway

    public Runway(int degree, String position, int[] para1, int[] para2){
        /*para1 & 2 are necessary parameters for each logical runway
        * [0]=TORA, [1]=TODA, [2]= ASDA, [3]=LDA, [4]=Displaced Threshold*/
        this.degree = degree;
        this.position = position;
        if(degree <= 180){
            degree2 = degree + 180;
        } else degree2 = degree - 180;
        if(position.equals("L")){
            position2 = "R";
        } else if (position.equals("R")){
            position2 = "L";
        } else
            position2 = "C";

        LogicalRunway logicRun1 = new LogicalRunway(degree,position,para1[0],para1[1],para1[2],para1[3],para1[4]);
        LogicalRunway logicRun2 = new LogicalRunway(degree2,position2,para2[0],para2[1],para2[2],para2[3],para2[4]);

        logicRunList.add(logicRun1);
        logicRunList.add(logicRun2);
        this.name = runwayName();
    }

    public ArrayList<LogicalRunway> getLogicRunList() {
        return logicRunList;
    }

    public String getName() {
        return name;
    }

    private String runwayName(){ //generates the runway name format eg: 09L/27R
        String d1 = degreeConversion(degree);
        String d2 = degreeConversion(degree2);
        return d1+position+"/"+d2+position2;
    }

    private String degreeConversion(int d){
        if(d<100){
            return "0"+(Integer.toString(d));
        }else
            return Integer.toString(d/10);
    }
}
