package Model;

public class Runway {

    private LogicalRunway logicalRunway1;
    private LogicalRunway logicalRunway2;
    private String name;

    public Runway(String designator1, String designator2, int[] para1, int[] para2){
        /*para1 & 2 are necessary parameters for each logical runway
        * [0]=TORA, [1]=TODA, [2]= ASDA, [3]=LDA, [4]=Displaced Threshold*/

        logicalRunway1 = new LogicalRunway(designator1,para1[0],para1[1],para1[2],para1[3],para1[0]-para1[3]);
        logicalRunway2 = new LogicalRunway(designator2,para2[0],para2[1],para2[2],para2[3],para2[0]-para2[3]);

        this.name = designator1+designator2;
    }

    public LogicalRunway getLogicalRunway1() {
        return logicalRunway1;
    }

    public LogicalRunway getLogicalRunway2() {
        return logicalRunway2;
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
