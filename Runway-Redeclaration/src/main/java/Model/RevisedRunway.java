package Model;

import Model.Runway;

public class RevisedRunway {

    LogicalRunway revRun0;
    LogicalRunway revRun1;

    public RevisedRunway(Runway runway, Obstacle obstacle, Position position) {

        revRun0 = runway.getLogicRunList().get(0);
        revRun1 = runway.getLogicRunList().get(1);

    }


}
