package Model;

public class RevisedRunway
{
    private LogicalRunway revisedRunway1;
    private LogicalRunway revisedRunway2;
    private Obstacle obstacle;
    private Position position;
    private String revisedCalculationBreakdown1 = "";


    // Constants
    private static int RESA = 240;
    private static int ALS = 50;
    private static int TOCS = 50;
    private static int STRIP_END = 60;
    private static int BLAST_ALLOWANCE = 300;

    public RevisedRunway(Runway runway, Obstacle obstacle, Position position)
    {
        this.obstacle = obstacle;
        this.position = position;

        revisedCalculationBreakdown1 += "Runway: " + runway.getLogicalRunway1().getName() + "\n";
        revisedRunway1 = calculateValues(runway.getLogicalRunway1());


        revisedCalculationBreakdown1 += "Runway: " + runway.getLogicalRunway2().getName() + "\n";
        revisedRunway2 = calculateValues(runway.getLogicalRunway2());

    }

    public LogicalRunway getRevisedRunway1() { return revisedRunway1; }

    public LogicalRunway getRevisedRunway2() { return revisedRunway2; }

    public Obstacle getObstacle() { return obstacle; }

    public Position getPosition() { return position; }

    public String getRevisedCalculationBreakdown1(){ return this.revisedCalculationBreakdown1;}


    public String getResults()
    {
        String result = "";

        result += "REVISED RUNWAY" + "\n" + "" + "\n" +
                "Runway " + revisedRunway1.getName() + "/" + revisedRunway2.getName() + "\n";
        for (LogicalRunway revisedRunway : new LogicalRunway[] {revisedRunway1, revisedRunway2})
        {
            result += "Runway " + revisedRunway.getName() + ":" + "\n";
            result += "TORA: " + revisedRunway.getTora() + "\n";
            result += "TODA " + revisedRunway.getToda()+ "\n";
            result += "ASDA " + revisedRunway.getAsda()+ "\n";
            result += "LDA: " + revisedRunway.getLda() + "\n";

        }

        return result;
    }

    private LogicalRunway calculateValues(LogicalRunway runway)
    {
        Direction obstacleSide = (position.getDistLThresh() >= position.getDistRThresh()) ?
                Direction.LEFT : Direction.RIGHT;
        int distanceFromThreshold = (runway.getDirection() == Direction.LEFT) ?
                position.getDistLThresh() : position.getDistRThresh();
        boolean towardsObstacle = runway.getDirection() == obstacleSide;

        int lda = towardsObstacle ?
                calculateRevisedLDA(distanceFromThreshold) : calculateRevisedLDA(runway.getLda(), distanceFromThreshold);
        int tora = towardsObstacle ?
                calculateRevisedTORA(distanceFromThreshold, runway.getDisplacedThreshold()) :
                calculateRevisedTORA(runway.getTora(), distanceFromThreshold, runway.getDisplacedThreshold());
        int toda = towardsObstacle ?
                tora : tora + (runway.getToda() - runway.getTora());
        int asda = towardsObstacle ?
                tora : tora + (runway.getAsda() - runway.getTora());

        String toraBreakdown = towardsObstacle ? revisedToraBreakdown(true, tora, distanceFromThreshold, runway.getDisplacedThreshold(), runway.getTora()) : revisedToraBreakdown(false, tora, distanceFromThreshold, runway.getDisplacedThreshold(), runway.getTora());
        String ldaBreakdown = towardsObstacle ? revisedLDABreakdown(true, lda, distanceFromThreshold, runway.getLda()) : revisedLDABreakdown(false, lda, distanceFromThreshold, runway.getLda());
        String asdaBreakdown = towardsObstacle ? revisedASDABreakdown(true, asda, runway.getAsda(), runway.getTora()) : revisedASDABreakdown(false, asda, runway.getAsda(), runway.getTora());
        String todaBreakdown = towardsObstacle ? revisedTODABreakdown(true, toda, runway.getToda(), runway.getTora()) : revisedTODABreakdown(false, toda, runway.getToda(), runway.getTora());

        revisedCalculationBreakdown1 += toraBreakdown + "" + "\n" + "\n" + ldaBreakdown + "" + "\n" + "\n" + todaBreakdown + "" + "\n" + "\n" + asdaBreakdown + "" + "\n" + "\n";



        return new LogicalRunway(runway.getName(), tora, toda, asda, lda, runway.getDisplacedThreshold());
    }

    private int calculateRevisedLDA(int lda, int distanceFromThreshold)
    {
        // Ensure the largest of the possible two values is picked to enforce safety.
        int tempThreshold = (obstacle.getHeight() * ALS >= RESA) ?
                obstacle.getHeight() * ALS : RESA;
        int displacement = ((tempThreshold + STRIP_END) >= BLAST_ALLOWANCE) ?
                tempThreshold + STRIP_END : BLAST_ALLOWANCE;

        return lda - distanceFromThreshold - displacement;
    }

    private int calculateRevisedLDA(int distanceFromThreshold)
    {
        return distanceFromThreshold - RESA - STRIP_END;
    }

    private int calculateRevisedTORA(int distanceFromThreshold, int displacedThreshold)
    {
        // Ensure the largest of the possible two values is picked to enforce safety.
        int tempThreshold = (obstacle.getHeight() * TOCS >= RESA) ?
                obstacle.getHeight() * TOCS : RESA;

        return distanceFromThreshold + displacedThreshold - tempThreshold - STRIP_END;
    }

    private int calculateRevisedTORA(int tora, int distanceFromThreshold, int displacedThreshold)
    {
        return tora - BLAST_ALLOWANCE - distanceFromThreshold - displacedThreshold;
    }

    private String revisedToraBreakdown(Boolean towardsObstacle, int newTora, int distanceFromThreshold, int displacedThreshold, int oldTora) {

        String breakdown = "";

        int tempThreshold = (obstacle.getHeight() * TOCS >= RESA) ?
                obstacle.getHeight() * TOCS : RESA;

        if(towardsObstacle){
            breakdown += "TORA BREAKDOWN: " + "\n";
            breakdown += "TORA (" + newTora + ")" +
                    " = Distance from Threshold (" + distanceFromThreshold + ")" +
                    " + Displaced Threshold (" + displacedThreshold + ")" +
                    " - Temporary Threshold (" + tempThreshold + ")" +
                    " - Strip End (" + STRIP_END +")";

            return breakdown;
        }

        breakdown += "TORA BREAKDOWN: " + "\n";
        breakdown += "TORA (" + newTora + ")" +
                " = Old TORA (" + oldTora + ")" +
                " - Blast Allowance (" + BLAST_ALLOWANCE + ")" +
                " - Distance from Threshold (" + distanceFromThreshold + ")" +
                " - Displaced Threshold (" + displacedThreshold +")";

        return breakdown;
    }

    private String revisedLDABreakdown(Boolean towardsObstacle, int newLDA, int distanceFromThreshold, int oldLDA){
        String breakdown = "";

        int tempThreshold = (obstacle.getHeight() * ALS >= RESA) ?
                obstacle.getHeight() * ALS : RESA;
        int displacement = ((tempThreshold + STRIP_END) >= BLAST_ALLOWANCE) ?
                tempThreshold + STRIP_END : BLAST_ALLOWANCE;

        if(towardsObstacle){
            breakdown += "LDA BREAKDOWN: " + "\n";
            breakdown += "LDA (" + newLDA + ")" +
                    " = Distance from Threshold (" + distanceFromThreshold + ")" +
                    " - RESA (" + RESA + ")" +
                    " - Strip End (" + STRIP_END +")";

            return breakdown;
        }

        breakdown += "LDA BREAKDOWN: " + "\n";
        breakdown += "LDA (" + newLDA + ")" +
                " = Old LDA(" + oldLDA + ")" +
                " - Distance from Threshold (" + distanceFromThreshold + ")" +
                " - Displacement(" + displacement +")";

        return breakdown;
    }

    private String revisedTODABreakdown(Boolean towardsObstacle, int newToda, int oldToda, int oldTora){
        String breakdown = "";

        if(towardsObstacle){
            breakdown += "TODA BREAKDOWN: " + "\n";
            breakdown += "TORA (" + newToda + ")" +
                    " = Old TODA (" + oldToda + ")";

            return breakdown;
        }

        breakdown += "TODA BREAKDOWN: " + "\n";
        breakdown += "TODA (" + newToda + ")" +
                " = Old TORA(" + oldTora + ")" +
                " + (Old TODA (" + oldToda + ")" +
                " - Old Tora (" + oldTora +"))";
        return breakdown;
    }

    private String revisedASDABreakdown(Boolean towardsObstacle, int newAsda, int oldAsda, int oldTora){
        String breakdown = "";

        if(towardsObstacle){
            breakdown += "ASDA BREAKDOWN: " + "\n";
            breakdown += "ASDA (" + newAsda + ")" +
                    " = Old TORA (" + oldTora + ")";

            return breakdown;
        }

        breakdown += "ASDA BREAKDOWN: " + "\n";
        breakdown += "ASDA (" + newAsda + ")" +
                " = Old TORA(" + oldTora + ")" +
                " + (Old ASDA (" + oldAsda + ")" +
                " - Old Tora (" + oldTora +"))";
        return breakdown;
    }

}