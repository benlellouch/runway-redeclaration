package Model;

public class LogicalRunway {

    private int lda;
    private int tora;
    private int toda;
    private int asda;
    private int displacedThreshold;
    private String designator;

    public LogicalRunway(String designator, int tora, int toda, int asda, int lda, int displacedThreshold){
        this.designator = designator;
        this.asda = asda;
        this.toda = toda;
        this.tora = tora;
        this.lda = lda;
        this.displacedThreshold = displacedThreshold;
    }


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

    public int getDisplacedThreshold() {
        return displacedThreshold;
    }
}
