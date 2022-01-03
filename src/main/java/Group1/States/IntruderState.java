package Group1.States;

import Interop.Agent.Intruder;
import Interop.Geometry.Direction;
import Interop.Geometry.Point;
import Interop.Percept.Vision.ObjectPerceptType;

public class IntruderState extends AgentState {

    Intruder intruder;

    public IntruderState(Intruder intruder, Point location, Direction viewDirection, double radius) {
        super(
                location,
                viewDirection,
                radius,
                ObjectPerceptType.Intruder
        );
        this.intruder = intruder;
    }

    public Intruder getIntruder() {
        return intruder;
    }

}
