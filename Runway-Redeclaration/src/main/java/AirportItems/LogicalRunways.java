package AirportItems;

public class LogicalRunways {

    private Integer lda;
    private Integer tora;
    private Integer toda;
    private Integer asda;
    private Integer displacedThreshold;
    private String name;

    public LogicalRunways(String name, Integer lda, Integer tora, Integer toda, Integer asda, Integer displacedThreshold){
        this.name = name;
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
