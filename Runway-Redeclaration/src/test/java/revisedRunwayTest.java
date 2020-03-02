import static org.junit.jupiter.api.Assertions.assertEquals;

import Model.*;
import org.junit.jupiter.api.Test;

public class revisedRunwayTest {

    LogicalRunway lRunway09R = new LogicalRunway("09R",3660,3660,3660,3353,3660-3353);
    LogicalRunway lRunway27L = new LogicalRunway("27L",3660,3660,3660,3353,0);
    Runway runway09R27L = new Runway(lRunway09R,lRunway27L);

    LogicalRunway lRunway09L = new LogicalRunway("09L", 3902,3902,3902,3595,3902-3595);
    LogicalRunway lRunway27R = new LogicalRunway("27R", 3884,3962,3884,3884,0);
    Runway runway09L27R = new Runway(lRunway09L,lRunway27R);

    @Test
    public void testScenario1()
    {
        Obstacle obstacle = new Obstacle("test", 12);
        Position position = new Position(0,-50,3646);
        RevisedRunway revisedRunway = new RevisedRunway(runway09L27R, obstacle,position);

        assertEquals(3345,revisedRunway.getRevisedRunway1().getTora());
        assertEquals(3345,revisedRunway.getRevisedRunway1().getToda());
        assertEquals(3345,revisedRunway.getRevisedRunway1().getAsda());
        assertEquals(2985,revisedRunway.getRevisedRunway1().getLda());

        assertEquals(2986,revisedRunway.getRevisedRunway2().getTora());
        assertEquals(2986,revisedRunway.getRevisedRunway2().getToda());
        assertEquals(2986,revisedRunway.getRevisedRunway2().getAsda());
        assertEquals(3346,revisedRunway.getRevisedRunway2().getLda());
    }

    @Test
    public void testScenario2()
    {
        Obstacle obstacle = new Obstacle("test", 25);
        Position position = new Position(0,2853,500);
        RevisedRunway revisedRunway = new RevisedRunway(runway09R27L,obstacle,position);

        assertEquals(1850,revisedRunway.getRevisedRunway1().getTora());
        assertEquals(1850,revisedRunway.getRevisedRunway1().getToda());
        assertEquals(1850,revisedRunway.getRevisedRunway1().getAsda());
        assertEquals(2553,revisedRunway.getRevisedRunway1().getLda());

        assertEquals(2860,revisedRunway.getRevisedRunway2().getTora());
        assertEquals(2860,revisedRunway.getRevisedRunway2().getToda());
        assertEquals(2860,revisedRunway.getRevisedRunway2().getAsda());
        assertEquals(1850,revisedRunway.getRevisedRunway2().getLda());
    }

    @Test
    public void testScenario3()
    {
        Obstacle obstacle = new Obstacle("test", 15);
        Position position = new Position(0,150,3203);
        RevisedRunway revisedRunway = new RevisedRunway(runway09R27L,obstacle,position);

        assertEquals(2903,revisedRunway.getRevisedRunway1().getTora());
        assertEquals(2903,revisedRunway.getRevisedRunway1().getToda());
        assertEquals(2903,revisedRunway.getRevisedRunway1().getAsda());
        assertEquals(2393,revisedRunway.getRevisedRunway1().getLda());

        assertEquals(2393,revisedRunway.getRevisedRunway2().getTora());
        assertEquals(2393,revisedRunway.getRevisedRunway2().getToda());
        assertEquals(2393,revisedRunway.getRevisedRunway2().getAsda());
        assertEquals(2903,revisedRunway.getRevisedRunway2().getLda());
    }

    @Test
    public void testScenario4()
    {
        Obstacle obstacle = new Obstacle("test", 20);
        Position position = new Position(0,3546,50);
        RevisedRunway revisedRunway = new RevisedRunway(runway09L27R, obstacle,position);

        assertEquals(2793,revisedRunway.getRevisedRunway1().getTora());
        assertEquals(2793,revisedRunway.getRevisedRunway1().getToda());
        assertEquals(2793,revisedRunway.getRevisedRunway1().getAsda());
        assertEquals(3246,revisedRunway.getRevisedRunway1().getLda());

        assertEquals( 3534,revisedRunway.getRevisedRunway2().getTora());
        assertEquals( 3612,revisedRunway.getRevisedRunway2().getToda());
        assertEquals(3534,revisedRunway.getRevisedRunway2().getAsda());
        assertEquals(2774,revisedRunway.getRevisedRunway2().getLda());
    }
}
