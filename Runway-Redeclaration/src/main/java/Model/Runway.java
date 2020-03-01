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

    public String getResults()
    {
        String result = "";

        result += "Runway " + logicalRunway1.getName() + "/" + logicalRunway2.getName() + "\n";
        for (LogicalRunway logicalRunway : new LogicalRunway[] {logicalRunway1, logicalRunway2})
        {
            result += "Runway " + logicalRunway.getName() + ":" + "\n";
            result += "TORA: " + logicalRunway.getTora() + "\n";
            result += "TODA " + logicalRunway.getToda()+ "\n";
            result += "ASDA " + logicalRunway.getAsda()+ "\n";
            result += "LDA: " + logicalRunway.getLda() + "\n";
        }

        return result;
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
