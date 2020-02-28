package Model;

public class Runway {

    private LogicalRunway logicalRunway1;
    private LogicalRunway logicalRunway2;
    private String name;

    public Runway(LogicalRunway logicalRunway1, LogicalRunway logicalRunway2) {
        this.logicalRunway1 = logicalRunway1;
        this.logicalRunway2 = logicalRunway2;

        this.name = logicalRunway1.getName()+"/"+logicalRunway2.getName();
    }

    @Override
    public String toString() {
        return this.name;
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

}
