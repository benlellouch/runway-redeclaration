package AirportItems;

public class Position {

    private Obstacle obstacle;
    private Integer distCenter;
    private Integer distLThresh;
    private Integer distRThresh;

    public Position(Obstacle obstacle, Integer distCenter, Integer distLThresh, Integer distRThresh) {
        this.obstacle = obstacle;
        this.distCenter = distCenter;
        this.distLThresh = distLThresh;
        this.distRThresh = distRThresh;
    }

    public Obstacle getObstacle() {
        return obstacle;
    }

    public Integer getDistCenter() {
        return distCenter;
    }

    public Integer getDistLThresh() {
        return distLThresh;
    }

    public Integer getDistRThresh() {
        return distRThresh;
    }
}
