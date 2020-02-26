import Model.*;

public class Test
{
    public static void main(String[] args)
    {
        Runway runway = new Runway("09R", "27L", new int[]{3660, 3660, 3660, 3353}, new int[]{3660, 3660, 3660, 3660});
        Obstacle obstacle = new Obstacle("Test", 15);
        Position position = new Position(60, 3203, 150);

        RevisedRunway revisedRunway = new RevisedRunway(runway, obstacle, position);
        System.out.println(revisedRunway.getResults());
    }
}
