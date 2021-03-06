import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import Model.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculationsTest {

    LogicalRunway lRunway09R = new LogicalRunway("09R",3660,3660,3660,3353);
    LogicalRunway lRunway27L = new LogicalRunway("27L",3660,3660,3660,3660);
    Runway runway09R27L = new Runway(lRunway09R,lRunway27L);

    LogicalRunway lRunway09L = new LogicalRunway("09L", 3902,3902,3902,3595);
    LogicalRunway lRunway27R = new LogicalRunway("27R", 3884,3962,3884,3884);
    Runway runway09L27R = new Runway(lRunway09L,lRunway27R);

    @Test
    @DisplayName("TC1: Scenario 1 calculations test")
    public void testScenario1()
    {
        Obstacle obstacle = new Obstacle("test", 12,1);
        Position position = new Position(0,-50,3646);
        RevisedRunway revisedRunway = new RevisedRunway(runway09L27R, obstacle,position);

        assertEquals(3346,revisedRunway.getLogicalRunway1().getTora());
        assertEquals(3346,revisedRunway.getLogicalRunway1().getToda());
        assertEquals(3346,revisedRunway.getLogicalRunway1().getAsda());
        assertEquals(2986,revisedRunway.getLogicalRunway1().getLda());

        assertEquals(2986,revisedRunway.getLogicalRunway2().getTora());
        assertEquals(2986,revisedRunway.getLogicalRunway2().getToda());
        assertEquals(2986,revisedRunway.getLogicalRunway2().getAsda());
        assertEquals(3346,revisedRunway.getLogicalRunway2().getLda());
        
    }

    @Test
    @DisplayName("TC2: Scenario 2 calculations test")
    public void testScenario2()
    {
        Obstacle obstacle = new Obstacle("test", 25,1);
        Position position = new Position(0,2853,500);
        RevisedRunway revisedRunway = new RevisedRunway(runway09R27L,obstacle,position);

        assertEquals(1850,revisedRunway.getLogicalRunway1().getTora());
        assertEquals(1850,revisedRunway.getLogicalRunway1().getToda());
        assertEquals(1850,revisedRunway.getLogicalRunway1().getAsda());
        assertEquals(2553,revisedRunway.getLogicalRunway1().getLda());

        assertEquals(2860,revisedRunway.getLogicalRunway2().getTora());
        assertEquals(2860,revisedRunway.getLogicalRunway2().getToda());
        assertEquals(2860,revisedRunway.getLogicalRunway2().getAsda());
        assertEquals(1850,revisedRunway.getLogicalRunway2().getLda());
    }

    @Test
    @DisplayName("TC3: Scenario 3 calculations test")
    public void testScenario3()
    {
        Obstacle obstacle = new Obstacle("test", 15,1);
        Position position = new Position(0,150,3203);
        RevisedRunway revisedRunway = new RevisedRunway(runway09R27L,obstacle,position);

        assertEquals(2903,revisedRunway.getLogicalRunway1().getTora());
        assertEquals(2903,revisedRunway.getLogicalRunway1().getToda());
        assertEquals(2903,revisedRunway.getLogicalRunway1().getAsda());
        assertEquals(2393,revisedRunway.getLogicalRunway1().getLda());

        assertEquals(2393,revisedRunway.getLogicalRunway2().getTora());
        assertEquals(2393,revisedRunway.getLogicalRunway2().getToda());
        assertEquals(2393,revisedRunway.getLogicalRunway2().getAsda());
        assertEquals(2903,revisedRunway.getLogicalRunway2().getLda());
    }

    @Test
    @DisplayName("TC4: Scenario 4 calculations test")
    public void testScenario4()
    {
        Obstacle obstacle = new Obstacle("test", 20,1);
        Position position = new Position(0,3546,50);
        RevisedRunway revisedRunway = new RevisedRunway(runway09L27R, obstacle,position);

        assertEquals(2793,revisedRunway.getLogicalRunway1().getTora());
        assertEquals(2793,revisedRunway.getLogicalRunway1().getToda());
        assertEquals(2793,revisedRunway.getLogicalRunway1().getAsda());
        assertEquals(3246,revisedRunway.getLogicalRunway1().getLda());

        assertEquals( 3535,revisedRunway.getLogicalRunway2().getTora());
        assertEquals( 3613,revisedRunway.getLogicalRunway2().getToda());
        assertEquals(3535,revisedRunway.getLogicalRunway2().getAsda());
        assertEquals(2775,revisedRunway.getLogicalRunway2().getLda());
    }
}