package View;


import Model.*;
import javafx.beans.*;
import javafx.geometry.VPos;
import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;


public abstract class AbstractRunwayView extends javafx.scene.canvas.Canvas {

    protected static final int RESA = 240;

    protected int TORA, ASDA, TODA, LDA;
    protected int leftSpace;
    protected int margin;

    protected boolean leftRunway;

    protected Runway originalRunways;
    protected LogicalRunway runway;

    protected Position obstaclePosition;
    protected Obstacle obstacle;

    protected GraphicsContext gc;
    protected Color textColor;

    public AbstractRunwayView(Runway originalRunways, LogicalRunway revisedRunway, Position obstaclePosition, Obstacle obstacle, Color textColor) {

        runway = revisedRunway;
        this.originalRunways = originalRunways;
        this.obstaclePosition = obstaclePosition;
        this.obstacle = obstacle;
        this.textColor = textColor;

        if(runway == null){
            return;
        }
        

        if (runway.getDirection() == Direction.LEFT) {   // left virtual runway
            leftRunway = true;
            leftSpace = 0;
            this.TORA = originalRunways.getLogicalRunway1().getTora();
            this.ASDA = originalRunways.getLogicalRunway1().getAsda();
            this.TODA = originalRunways.getLogicalRunway1().getToda();
            this.LDA = originalRunways.getLogicalRunway1().getLda();
        } else {
            leftRunway = false;
            this.TORA = originalRunways.getLogicalRunway2().getTora();
            this.ASDA = originalRunways.getLogicalRunway2().getAsda();
            this.TODA = originalRunways.getLogicalRunway2().getToda();
            this.LDA = originalRunways.getLogicalRunway2().getLda();
            leftSpace = Math.max(TODA, ASDA) - TORA;   
        }

        margin = TORA / 10;

        gc = getGraphicsContext2D();

        widthProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable observable) {
                render();
                renderObstacle();
            }
        });
        heightProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable observable) {
                render();
                renderObstacle();
            }
        });
        render();
    }

    protected void renderDesignator(int y, Color txtCol, double designatorOrientationL, double designatorOrientationR) {
        gc.setFill(txtCol);
        gc.setFont(Font.font("Consolas", 24));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        renderDesignatorHelper(y, designatorOrientationR, designatorOrientationL);
    }

    private void renderDesignatorHelper(int y, double designatorOrientationR, double designatorOrientationL) {
        String designator1Name = originalRunways.getLogicalRunway1().getName();
        String designator1 = designator1Name.substring(0, 2) + "\n" + designator1Name.substring(2);
        String designator2Name = originalRunways.getLogicalRunway2().getName();
        String designator2  = designator2Name.substring(0,2) + "\n" + designator2Name.substring(2);

        gc.save();
        gc.translate( x_scale(TORA / 7 + leftSpace), y_scale(y));
        gc.rotate(designatorOrientationL);
        gc.fillText(designator1, 0,0);
        gc.restore();
        gc.save();
        gc.translate(x_scale(TORA * 6 / 7 + leftSpace), y_scale(y));
        gc.rotate(designatorOrientationR);
        gc.fillText(designator2, 0,0);
        gc.restore();
    }


    protected void renderStopWay() {
        int stopway = ASDA - TORA;

        if (stopway != 0) {

            if (leftRunway) {
                renderRuler(TORA, stopway, 80, "Stopway");
            } else {
                renderRuler(leftSpace - stopway, stopway, 80, "Stopway");
            }
        }
    }


    protected void renderClearWay() {
        int clearway = TODA - TORA;

        if (clearway != 0) {
            if (leftRunway) {
                renderRuler(TORA, clearway, 90, "Clearway");
            } else {
                renderRuler(leftSpace - clearway, clearway, 90, "Clearway");
            }
        }
    }

    protected void renderDisplacedTSH() {
        int displacedTsh = TORA - LDA;

        if (displacedTsh > 0) {
            if (leftRunway) {
                renderRuler(0, displacedTsh, 90, "Displaced TSH");
            } else {
                renderRuler(leftSpace + LDA, displacedTsh, 90, "Displaced TSH");
            }
        }
    }

    protected void renderTOLDirection(boolean imageBackground) {
        gc.setFill(imageBackground ? Color.WHITE : Color.BLACK);
        gc.setStroke(imageBackground ? Color.WHITE : Color.BLACK);
        gc.setLineWidth(y_scale(0.5));
        gc.setFont(Font.font("Consolas", 14));
        gc.setTextAlign(TextAlignment.RIGHT);
        if (!leftRunway) {
            gc.fillText("Take-off/landing direction: <-", getWidth() - x_scale(60), y_scale(290));
        } else {
            gc.fillText("Take-off/landing direction: ->", getWidth() - x_scale(60), y_scale(290));
        }
    }



    private void renderTORA_TODA_ASDA(int x, int y) {
        int tora = runway.getTora();
        int toda = runway.getToda();
        int asda = runway.getAsda();


        if (tora == toda && tora == asda)
        {
            renderRuler(x, tora, y, "TORA, TODA, ASDA " + tora + "m");
        }
        else if (tora == toda)
        {
            renderRuler(x, tora, y, "TORA, TODA " + tora + "m");
            renderRuler(x, asda, y - 10, "ASDA " + asda + "m");
        }
        else if (tora == asda)
        {
            renderRuler(x, tora, y, "TORA " + tora + "m");
            renderRuler(x, asda, y - 10, "ASDA, TODA " + asda + "m");

        }
        else
        {
            renderRuler(x, tora, y, "TORA " + tora + "m");
            renderRuler(x-(toda-tora), toda, y - 10, "TODA " + toda + "m");
            renderRuler(x, asda, y - 20, "ASDA " + asda + "m");
        }
    }


    protected void renderRuler(int x, int length, int y, String text) {
        gc.setFill(textColor);
        gc.setStroke(textColor);
        gc.setLineWidth(y_scale(0.5));
        gc.setFont(Font.font("Consolas", 14));
        gc.setTextAlign(TextAlignment.CENTER);

        scaledStrokeLine(x, y, x + length, y);
        scaledStrokeLine(x, y + 2, x, y - 2);
        scaledStrokeLine(x + length, y + 2, x + length, y - 2);
        gc.fillText(text, x_scale(x + (length >> 1)), y_scale(y - 5));
    }


    protected void renderDistances(int oLength, int yDistances, int yObstacle) {

        if(obstaclePosition == null){
            return;
        }

        int tora = runway.getTora();
        int lda = runway.getLda();
        int displacedThs = Math.max(originalRunways.getLogicalRunway1().getDisplacedThreshold(), originalRunways.getLogicalRunway2().getDisplacedThreshold());
        int slopeCalculation = Math.max((obstacle.getHeight() * 50), RESA);



        if (obstaclePosition.getDistLThresh() < obstaclePosition.getDistRThresh()) {
            if (obstaclePosition.getDistLThresh() > 0) {
                renderRuler(leftSpace + displacedThs, obstaclePosition.getDistLThresh(), yObstacle, Integer.toString(obstaclePosition.getDistLThresh()) + "m");
            }
            renderRuler(obstaclePosition.getDistLThresh() + leftSpace+displacedThs, oLength, yObstacle, "Obstacle");
            int endObstacle = leftSpace + obstaclePosition.getDistLThresh() + oLength + displacedThs;

            if (leftRunway) {
                renderRuler(endObstacle, 300, yDistances, "blast protection");
                renderTORA_TODA_ASDA(endObstacle + 300, yDistances);

                renderRuler(endObstacle, slopeCalculation + 60, yDistances + 10, "slope + strip end");
                renderRuler(endObstacle + slopeCalculation + 60, lda, yDistances + 10, "LDA " + lda + "m");
            } else {
                renderRuler(endObstacle, slopeCalculation + 60, yDistances, "slope + strip end");
                renderTORA_TODA_ASDA(endObstacle + slopeCalculation + 60, yDistances);

                renderRuler(endObstacle, 300, yDistances + 10, "RESA + strip end");
                renderRuler(endObstacle + 300, lda , yDistances + 10, "LDA " + lda + "m");
            }
        } else {
            int startObstacle = leftSpace + obstaclePosition.getDistLThresh() + displacedThs;

            if (leftRunway) {
                renderTORA_TODA_ASDA(0, yDistances);
                renderRuler(tora , slopeCalculation + 60, yDistances, "strip end + slope");

                renderRuler(displacedThs, lda, yDistances + 10, "LDA " + lda + "m");
                renderRuler(lda + displacedThs, 300, yDistances + 10, "strip end + RESA");
            } else {
                renderTORA_TODA_ASDA(leftSpace, yDistances);
                renderRuler(leftSpace + tora, 300, yDistances, "blast protection");

                renderRuler(leftSpace, lda, yDistances + 10, "LDA " + lda + "m");
                renderRuler(leftSpace + lda, slopeCalculation + 60, yDistances + 10, "strip end + slope");
            }

            renderRuler(startObstacle, oLength, yObstacle, "Obstacle");
            if (obstaclePosition.getDistRThresh() > 0) {
                renderRuler(startObstacle + oLength, obstaclePosition.getDistRThresh(), yObstacle, obstaclePosition.getDistRThresh() + "m");
            }
        }
    }

    protected void scaledStrokeLine(double x1, double y1, double x2, double y2) {
        gc.strokeLine(x_scale(x1), y_scale(y1), x_scale(x2), y_scale(y2));

    }

    protected void scaledFillRect(double x, double y, double w, double h) {
        gc.fillRect(x_scale(x), y_scale(y), x_scale(w - margin), y_scale(h));
    }


    protected void scaledStrokeRect(double x, double y, double w, double h) {
        gc.strokeRect(x_scale(x), y_scale(y), x_scale(w - margin), y_scale(h));
    }


    protected double x_scale(double length) {
        double maxWidth = Math.max(TODA, ASDA) + 2 * margin;
        return (length + margin) * getWidth() / maxWidth;
    }

    protected double y_scale(double length) {
        return length * getHeight() / 300;
    }


    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double prefWidth(double height) {
        return getWidth();
    }

    @Override
    public double prefHeight(double width) {
        return getHeight();
    }

    protected abstract void render();

    public abstract void renderObstacle();

}
