package Model;

public class LogicalRunway
{
    private int lda;
    private int tora;
    private int toda;
    private int asda;
    private int displacedThreshold;
    private int degree;
    private Direction direction;

    public LogicalRunway(String designator, int tora, int toda, int asda, int lda, int displacedThreshold){
        this.asda = asda;
        this.toda = toda;
        this.tora = tora;
        this.lda = lda;
        this.displacedThreshold = displacedThreshold;

        degree = Integer.parseInt(designator.substring(0, 2)) * 10;
        direction = (designator.substring(2, 3).equals("L")) ? Direction.LEFT : Direction.RIGHT;
    }

    public String getName() { return degree + ((direction == Direction.LEFT) ? "L" : "R"); }

    public int getDegree() { return degree; }

    public Direction getDirection() { return direction; }

    public int getLda() {
        return lda;
    }

    public int getTora() {
        return tora;
    }

    public int getToda() {
        return toda;
    }

    public int getAsda() {
        return asda;
    }

    public int getDisplacedThreshold() { return displacedThreshold; }
}
