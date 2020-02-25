package AirportItems;

public class RevisedRunway
{
    private LogicalRunway revisedRunway1;
    private LogicalRunway revisedRunway2;
    private Obstacle obstacle;
    private Position position;

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

        revisedRunway1 = calculateValues(runway.getLogicalRunway1());
        revisedRunway2 = calculateValues(runway.getLogicalRunway2());
    }

    public LogicalRunway getRevisedRunway1() { return revisedRunway1; }

    public LogicalRunway getRevisedRunway2() { return revisedRunway2; }

    public Obstacle getObstacle() { return obstacle; }

    public Position getPosition() { return position; }

    public void printResults()
    {
        System.out.println("*********************************************************************");
        System.out.println("Runway " + revisedRunway1.getName() + "/" + revisedRunway2.getName());
        System.out.println("*********************************************************************");

        for (LogicalRunway revisedRunway : new LogicalRunway[] {revisedRunway1, revisedRunway2})
        {
            System.out.println("Runway " + revisedRunway.getName() + ":");
            System.out.println("TORA: " + revisedRunway.getTora());
            System.out.println("TODA " + revisedRunway.getToda());
            System.out.println("ASDA " + revisedRunway.getAsda());
            System.out.println("LDA: " + revisedRunway.getLda() + "\n");
        }

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
}