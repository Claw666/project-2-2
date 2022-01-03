package Group1.Agent.Vision;

import Group1.Geometry.Vector;
import Interop.Geometry.Point;
import Interop.Percept.Vision.ObjectPerceptType;
import Interop.Utils.Require;

public final class ObjectPercept {

    private ObjectPerceptType type;
    private Point point;

    public ObjectPercept(ObjectPerceptType type, Point point) {
        Require.notNull(type);
        Require.notNull(point);
        Require.positive(
                point.getDistanceFromOrigin().getValue(),
                "The distance of percept to an agent must not be negative!\n" +
                        "Moreover, the agent can not perceive itself.\n" +
                        "Therefore, the distance to a percept must never be 0!"
        );
        this.type = type;
        this.point = point;
    }

    public ObjectPercept from(Point newP) {
        return new ObjectPercept(
                type,
                new Vector(point)
                        .from(newP)
                        .toPoint()
        );
    }

    public Interop.Percept.Vision.ObjectPercept toInterop() {
        return new Interop.Percept.Vision.ObjectPercept(type, point);
    }

    public ObjectPerceptType getType() {
        return type;
    }

    public Point getPoint() {
        return point;
    }

    public String toString() {
        return "ObjectPercept{" +
                "type=" + type +
                ", point=" + point +
                '}';
    }

}

