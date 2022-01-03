package Group1.States;

import Group1.FileReader.ScenarioObject;
import Group1.Geometry.Parallelogram;
import Interop.Geometry.Direction;
import Interop.Geometry.Point;
import Interop.Percept.Vision.ObjectPerceptType;

public abstract class AgentState extends ScenarioObject {

    private Point location;
    private Direction viewDirection;
    private boolean wasLastActionExecuted;
    private double radius;

    public AgentState(
            Point location,
            Direction viewDirection,
            double radius,
            ObjectPerceptType type
    ) {
        super(type);
        this.location = location;
        this.viewDirection = viewDirection;
        this.radius = radius;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public void setViewDirection(Direction viewDirection) {
        this.viewDirection = viewDirection;
    }

    public Point getLocation() {
        return location;
    }

    public Direction getViewDirection() {
        return viewDirection;
    }

    public double getRadius() {
        return radius;
    }

    public boolean wasLastActionExecuted() {
        return wasLastActionExecuted;
    }

    public void setWasLastActionExecuted(boolean wasLastActionExecuted) {
        this.wasLastActionExecuted = wasLastActionExecuted;
    }

    public Parallelogram getPath(Point b) {
        return new Parallelogram(location, b, radius);
    }
}
