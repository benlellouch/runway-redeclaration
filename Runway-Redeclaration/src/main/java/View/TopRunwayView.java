package View;


import Model.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class TopRunwayView extends AbstractRunwayView {
    protected boolean rotateView;
    private int bearing;

    private static final int Y_SCALE = 2;

    public TopRunwayView(Runway originalRunways, LogicalRunway revisedRunway, Position obstaclePosition, Obstacle obstacle, boolean rotateView) {
        super(originalRunways,revisedRunway, obstaclePosition,obstacle, Color.WHITE);
        this.rotateView = rotateView;
        this.bearing = originalRunways.getLogicalRunway1().getDegree();
    }



    private void renderBackground(GraphicsContext gc)
    {
        gc.setFill(Color.GREEN);
        gc.fillRect(0,0,getWidth(),getHeight());
    }

    private void renderMarkersTSH(GraphicsContext gc) {
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(y_scale(2));

        int i = 130;
        while (i < 175) {
            scaledStrokeLine(TORA / 30 + leftSpace, i, TORA * 3 / 30 + leftSpace, i);
            scaledStrokeLine(TORA * 27 / 30 + leftSpace, i, TORA * 29 / 30 + leftSpace, i);
            i += 5;
        }
    }

    private void renderClearedAndGradedArea(GraphicsContext gc, int offset) {
        gc.setFill(Color.DARKBLUE);

        gc.fillPolygon(getX_scale(offset), getY_scale(), 12);
    }


    public void renderObstacle() {
        try {
            render();

            int displacedThr = Math.max(originalRunways.getLogicalRunway1().getDisplacedThreshold(), originalRunways.getLogicalRunway2().getDisplacedThreshold());
            int obstacle_x;
            int obstacle_y;
            obstacle_x = obstaclePosition.getDistLThresh() + leftSpace + displacedThr;


            if (Integer.parseInt(runway.getName().substring(0, 2)) <= 18) {
                obstacle_y = 150 - (obstaclePosition.getDistCenter() / Y_SCALE);
            } else {
                obstacle_y = 150 + (obstaclePosition.getDistCenter() / Y_SCALE);
            }


            int obstacleLength = TORA - obstaclePosition.getDistRThresh() - obstaclePosition.getDistLThresh() - displacedThr;

            renderDistances(obstacleLength, 210, 110);

            //TODO add obstacle width

            gc.setFill(Color.RED);
            gc.setGlobalAlpha(0.5);
            scaledFillRect(obstacle_x, obstacle_y - 10, obstacleLength, 20);
            gc.setGlobalAlpha(1.0);

            gc.setFill(Color.BLACK);
            scaledStrokeRect(obstacle_x, obstacle_y - 10, obstacleLength, 20);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    private void renderLineCtr(GraphicsContext gc) {
        gc.setLineWidth(y_scale(1));
        gc.setLineDashes(30);
        scaledStrokeLine(TORA / 6 + leftSpace, 150, TORA * 5 / 6 + leftSpace, 150);
        gc.setLineDashes(0);
    }

    protected void render() {
        double width = getWidth();
        double height = getHeight();
        GraphicsContext gc = getGraphicsContext2D();

        gc.clearRect(0, 0, width, height);
        renderBackground(gc);

        if (rotateView) {
            this.setRotate(bearing - 90);
            this.setScaleX(0.0089*bearing+0.2);
        }
        renderClearedAndGradedArea(gc, leftSpace);

        gc.setFill(Color.web("333"));
        scaledFillRect(leftSpace, 125, TORA, 50);


        renderMarkersTSH(gc);
        renderLineCtr(gc);
        renderDesignator(150, Color.WHITE,-270,-90);
        renderStopWay();
        renderClearWay();

        renderDisplacedTSH();


    }


    private double[] getX_scale(int offset)
    {
        double[] x_scale = new double[]{
                x_scale(offset - 60),
                x_scale(150 + offset),
                x_scale(300 + offset),
                x_scale(TORA + offset - 300),
                x_scale(TORA + offset - 150),
                x_scale(TORA + offset + 60),
                x_scale(TORA + offset + 60),
                x_scale(TORA + offset - 150),
                x_scale(TORA + offset - 300),
                x_scale(300 + offset),
                x_scale(150 + offset),
                x_scale(offset - 60)};

        return x_scale;
    }

    private double[] getY_scale()
    {
        double[] y_scale = new double[]{
                y_scale(75),
                y_scale(75),
                y_scale(45),
                y_scale(45),
                y_scale(75),
                y_scale(75),
                y_scale(225),
                y_scale(225),
                y_scale(255),
                y_scale(255),
                y_scale(225),
                y_scale(225)};

        return y_scale;
    }
}