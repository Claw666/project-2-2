package Group1.States;

import Group1.FileReader.ScenarioObject;
import Interop.Geometry.Direction;
import Interop.Geometry.Point;
import Interop.Percept.Vision.ObjectPerceptType;

public abstract class AgentDropItemState {

    private Point location;
    private double radius;

    public AgentDropItemState(
            Point location,
            double radius
    ) {
        this.location = location;
        this.radius = radius;
    }

    public Point getLocation() {
        return location;
    }

    public double getRadius() {
        return radius;
    }
}