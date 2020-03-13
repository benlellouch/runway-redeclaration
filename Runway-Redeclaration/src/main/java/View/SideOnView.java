package View;

import Model.*;
import javafx.scene.paint.Color;

public class SideOnView extends RunwayView {

    private Obstacle obstacle;

    public SideOnView(Runway originalRunways, LogicalRunway revisedRunway, Position obstaclePosition, Obstacle obstacle, boolean rotateView) {
        super(originalRunways,revisedRunway,obstaclePosition, obstacle);
        this.obstacle = obstacle;
    }

    /**
     * Draw a side-on view of the runway.
     */
    protected void draw() {
        double width = getWidth();
        double height = getHeight();

        // Fill canvas with grey
        gc.setFill(Color.web("ddd"));
        gc.fillRect(0, 0, width, height);

        // Draw runway surface
        gc.setFill(Color.web("333"));
        scaledFillRect(leftSpace, 149, TORA, 2);

        drawDesignators(170, Color.BLACK, 0,0);
        drawStopway(80);
        drawClearway(90);

        drawDisplacedThreshold(80);
        drawMapScale();
        drawTakeOffLandingDirection();
    }

    /**
     * Draw an obstacle on the runway.
     */
    public void drawObstacle() {

            if(obstaclePosition == null || runway == null){
                return;
            }

            draw();

            int obstacleLength = TORA - obstaclePosition.getDistRThresh() - obstaclePosition.getDistLThresh();
            int oHeight = obstacle.getHeight();

            gc.setFill(Color.RED);
            gc.setGlobalAlpha(0.5);
            scaledFillRect(obstaclePosition.getDistLThresh() + leftSpace, 149 - oHeight, obstacleLength, oHeight);
            gc.setGlobalAlpha(1.0);

            gc.setFill(Color.BLACK);
            scaledStrokeRect(obstaclePosition.getDistLThresh() + leftSpace, 149 - oHeight, obstacleLength, oHeight);

            int slopecalc = 240;

            drawSlope(obstacleLength, slopecalc);

            drawBrokenDownDistances(obstacleLength, 200, 110);



    }

    /**
     * Draw the TOCS/ALS slope that the plane needs to ascend/descend to safely fly over the obstacle.
     */
    private void drawSlope(int oLength, int slopecalc) {
        gc.setFill(Color.BLUE);
        gc.setGlobalAlpha(0.5);

        int oHeight = this.obstacle.getHeight();

        if (obstaclePosition.getDistLThresh() < obstaclePosition.getDistRThresh()) {
            // __|=|________
            int endObstacle = leftSpace + obstaclePosition.getDistLThresh() + oLength;

            gc.fillPolygon(new double[]{scale_x(endObstacle), scale_x(endObstacle), scale_x(endObstacle + slopecalc)}, new double[]{scale_y(149 - oHeight), scale_y(149), scale_y(149)}, 3);
        } else {
            // ________|=|__
            int startObstacle = leftSpace + obstaclePosition.getDistLThresh();

            gc.fillPolygon(new double[]{scale_x(startObstacle - slopecalc), scale_x(startObstacle), scale_x(startObstacle)}, new double[]{scale_y(149), scale_y(149), scale_y(149 - oHeight)}, 3);
        }

        gc.setGlobalAlpha(1.0);
    }

}