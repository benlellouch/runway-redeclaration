package Model;

public class Position {

    private int distCenter;
    private int distLThresh;
    private int distRThresh;

    public Position(int distCenter, int distLThresh, int distRThresh) {
        this.distCenter = distCenter;
        this.distLThresh = distLThresh;
        this.distRThresh = distRThresh;
    }

    public int getDistCenter() {
        return distCenter;
    }

    public int getDistLThresh() {
        return distLThresh;
    }

    public int getDistRThresh() {
        return distRThresh;
    }
}
