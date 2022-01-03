package Group1.States;

import Interop.Agent.Guard;
import Interop.Geometry.Direction;
import Interop.Geometry.Point;
import Interop.Percept.Vision.ObjectPerceptType;

public class GuardState extends AgentState {

    Guard guard;

    public GuardState(Guard guard, Point location, Direction viewDirection, double radius) {
        super(
                location,
                viewDirection,
                radius,
                ObjectPerceptType.Guard
        );
        this.guard = guard;
    }

    public Guard getGuard() { return guard; }

}
