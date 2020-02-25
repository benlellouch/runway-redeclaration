package Model;

import java.util.ArrayList;

public class Runway {

    private ArrayList<LogicalRunway> logicRunList;
    private String name;
    private int degree; //degree of direction
    private int degree2; //for opposite logical runway
    private String position; //L, R or C representing position of runway
    private String position2; //for opposite runway

    public Runway(String designator1, String designator2, int[] para1, int[] para2){
        /*para1 & 2 are necessary parameters for each logical runway
        * [0]=TORA, [1]=TODA, [2]= ASDA, [3]=LDA, [4]=Displaced Threshold*/

        logicRunList = new ArrayList<>();

        LogicalRunway logicRun1 = new LogicalRunway(designator1,para1[0],para1[1],para1[2],para1[3],para1[0]-para1[3]);
        LogicalRunway logicRun2 = new LogicalRunway(designator2,para2[0],para2[1],para2[2],para2[3],para2[0]-para2[3]);

        logicRunList.add(logicRun1);
        logicRunList.add(logicRun2);
        this.name = designator1+designator2;
    }

    public ArrayList<LogicalRunway> getLogicRunList() {
        return logicRunList;
    }

    public String getName() {
        return name;
    }


    private String degreeConversion(int d){
        if(d<100){
            return "0"+(Integer.toString(d));
        }else
            return Integer.toString(d/10);
    }
}
