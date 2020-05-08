package View;

import javafx.scene.paint.Color;

public class ColourMode
{
    private static final ColourMode COLOUR_MODE = new ColourMode();

    private Color backgroundColour;
    private Color clearAndGradedAreaColour;
    private Color obstacleColour;
    private Color slopeColour;

    private int colourModeIndex;

    public ColourMode()
    {
        normalMode();
    }

    public Color getBackgroundColour()
    {
        return backgroundColour;
    }

    public Color getClearAndGradedAreaColour()
    {
        return clearAndGradedAreaColour;
    }

    public Color getObstacleColour()
    {
        return obstacleColour;
    }

    public Color getSlopeColour()
    {
        return slopeColour;
    }

    public int getColourModeIndex()
    {
        return colourModeIndex;
    }

    public void normalMode()
    {
        this.backgroundColour = Color.LIMEGREEN;
        this.clearAndGradedAreaColour = Color.DARKBLUE;
        this.obstacleColour = Color.RED;
        this.slopeColour = Color.BLUE;
        colourModeIndex = 0;
    }

    public void protanopiaMode()
    {
        this.backgroundColour = Color.GOLD;
        this.clearAndGradedAreaColour = Color.DARKBLUE;
        this.obstacleColour = Color.DARKGOLDENROD;
        this.slopeColour = Color.BLUE;
        colourModeIndex = 1;
    }

    public void deuteranopiaMode()
    {
        this.backgroundColour = Color.NAVAJOWHITE;
        this.clearAndGradedAreaColour = Color.DARKBLUE;
        this.obstacleColour = Color.DARKGOLDENROD;
        this.slopeColour = Color.BLUE;
        colourModeIndex = 2;
    }

    public void tritanopiaMode()
    {
        this.backgroundColour = Color.FIREBRICK;
        this.clearAndGradedAreaColour = Color.TEAL;
        this.obstacleColour = Color.ORANGERED;
        this.slopeColour = Color.DARKCYAN;
        colourModeIndex = 3;
    }

    public static ColourMode getColourMode()
    {
        return COLOUR_MODE;
    }
}
