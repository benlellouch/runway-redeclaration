package Model;

public class Runway {

    private final LogicalRunway logicalRunway1;
    private final LogicalRunway logicalRunway2;
    private final String name;

    public Runway(LogicalRunway logicalRunway1, LogicalRunway logicalRunway2) {
        this.logicalRunway1 = logicalRunway1;
        this.logicalRunway2 = logicalRunway2;

        this.name = logicalRunway1.getName()+"/"+logicalRunway2.getName();
    }

    @Override
    public String toString() {
        return this.name;
    }

    public String getResults() { return logicalRunway1.getResults() + logicalRunway2.getResults(); }

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
