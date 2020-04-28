package View;

import Model.*;
import javafx.scene.paint.Color;

public class SideRunwayView extends AbstractRunwayView {

    private Obstacle obstacle;

    public SideRunwayView(Runway originalRunways, LogicalRunway revisedRunway, Position obstaclePosition, Obstacle obstacle) {
        super(originalRunways,revisedRunway,obstaclePosition, obstacle, Color.BLACK);
        this.obstacle = obstacle;
    }

    public void renderObstacle() {

        if(obstaclePosition == null || runway == null){
            return;
        }
        int displacedThr =
                Math.max(originalRunways.getLogicalRunway1().getDisplacedThreshold(), originalRunways.getLogicalRunway2().getDisplacedThreshold());
        int obstacleLength =
                TORA - obstaclePosition.getDistRThresh() - obstaclePosition.getDistLThresh() - displacedThr;
        int obstacleHeight = obstacle.getHeight();

        render();

        int obstacle_x = obstaclePosition.getDistLThresh() + leftSpace + displacedThr;

        gc.setFill(Color.RED);
        gc.setGlobalAlpha(0.5);
        scaledFillRect(obstacle_x, 149 - obstacleHeight, obstacleLength, obstacleHeight);
        gc.setGlobalAlpha(1.0);

        gc.setFill(Color.BLACK);
        scaledStrokeRect(obstacle_x, 149 - obstacleHeight, obstacleLength, obstacleHeight);

        int slopeCalculation = Math.max((obstacle.getHeight() * 50), RESA);

        renderSlope(obstacleLength, slopeCalculation);

        renderDistances(obstacleLength, 200, 110);
    }

    private void renderSlope(int obstacleLength, int slopeCalculation) {

        int displacedThr = Math.max(originalRunways.getLogicalRunway1().getDisplacedThreshold(), originalRunways.getLogicalRunway2().getDisplacedThreshold());
        int obstacleHeight = this.obstacle.getHeight();

        gc.setFill(Color.BLUE);
        gc.setGlobalAlpha(0.5);

        if (obstaclePosition.getDistLThresh() >= obstaclePosition.getDistRThresh()) {
            int startObstacle = leftSpace + obstaclePosition.getDistLThresh() + displacedThr;

            gc.fillPolygon(new double[]{x_scale(startObstacle - slopeCalculation), x_scale(startObstacle), x_scale(startObstacle)}, new double[]{y_scale(149), y_scale(149), y_scale(149 - obstacleHeight)}, 3);
        } else {
            int endObstacle = leftSpace + obstaclePosition.getDistLThresh() + obstacleLength + displacedThr;

            gc.fillPolygon(new double[]{x_scale(endObstacle), x_scale(endObstacle), x_scale(endObstacle + slopeCalculation)}, new double[]{y_scale(149 - obstacleHeight), y_scale(149), y_scale(149)}, 3);
        }

        gc.setGlobalAlpha(1.0);
    }

    protected void render() {
        double width = getWidth();
        double height = getHeight();

        gc.setFill(Color.web("ddd"));
        gc.fillRect(0, 0, width, height);

        gc.setFill(Color.web("333"));
        scaledFillRect(leftSpace, 149, TORA, 2);

        renderDesignator(170, Color.BLACK, 0,0);
        renderStopWay();
        renderClearWay();

        renderDisplacedTSH();
        renderTOLDirection();
    }

}