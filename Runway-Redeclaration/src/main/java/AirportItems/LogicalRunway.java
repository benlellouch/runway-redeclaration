package AirportItems;

public class LogicalRunway {

    private Integer lda;
    private Integer tora;
    private Integer toda;
    private Integer asda;
    private Integer displacedThreshold;
    private String name;
    private int degree;
    private String postion;

    public LogicalRunway(int degree, String position, Integer tora, Integer toda, Integer asda, Integer lda, Integer displacedThreshold){
        this.degree = degree;
        this.postion = position;
        this.asda = asda;
        this.toda = toda;
        this.tora = tora;
        this.lda = lda;
        this.displacedThreshold = displacedThreshold;
    }

    public Integer getLda() {
        return lda;
    }

    public Integer getTora() {
        return tora;
    }

    public Integer getToda() {
        return toda;
    }

    public Integer getAsda() {
        return asda;
    }

    public Integer getDisplacedThreshold() {
        return displacedThreshold;
    }

    public String getName() {
        return name;
    }
}
