package Group1.Agent.Vision;

import Group1.Geometry.Vector;
import Interop.Geometry.Point;
import Interop.Percept.Vision.FieldOfView;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class ObjectPercepts {

    private Set<ObjectPercept> objectPercepts;

    public ObjectPercepts(Set<ObjectPercept> objectPercepts) {
        // the perception of agents must not be modified after construction!
        this.objectPercepts = Collections.unmodifiableSet(objectPercepts);
    }

    public Set<ObjectPercept> getAll() {
        return objectPercepts;
    }


    /**
     * Takes into account only the distance to that point, not the view angle.
     * @param point
     * @return
     */
    public ObjectPercepts visibleFrom(Point point) {
        if (opaque().getAll().isEmpty()) return this;
        Set<Point> points = opaque().points();

        double distance = Double.POSITIVE_INFINITY;
        Point closest = null;
        for (Point pnt : points) {
            double newDistance = point.getDistance(pnt).getValue();
            if (newDistance >= distance) continue;
            distance = newDistance;
            closest = pnt;
        }

        double distanceTo = point.getDistance(closest).getValue();
        // todo check filter
        return filter(
                objectPercept -> objectPercept
                        .getPoint()
                        .getDistance(point)
                        .getValue() <= distanceTo
        );
    }

    ObjectPercepts getInFieldOfView(FieldOfView fieldOfView) {
        return filter((ObjectPercept percept) -> fieldOfView.isInView(percept.getPoint()));
    }

    ObjectPercepts getNotInFieldOfView(FieldOfView fieldOfView) {
        return filter((ObjectPercept percept) -> !fieldOfView.isInView(percept.getPoint()));
    }

    public ObjectPercepts opaque() {
        return filter(
                objectPercept -> objectPercept
                        .getType()
                        .isOpaque()
        );
    }

    public ObjectPercepts filter(Predicate<? super ObjectPercept> predicate) {
        return new ObjectPercepts(objectPercepts.stream()
                .filter(predicate)
                .collect(Collectors.toSet()));
    }

    public ObjectPercepts from(Point point) {
        return new ObjectPercepts(
                objectPercepts.stream()
                        .map(objectPercept -> objectPercept.from(point))
                        .collect(Collectors.toSet())
        );
    }

    public Interop.Percept.Vision.ObjectPercepts toInterop() {
        return new Interop.Percept.Vision.ObjectPercepts(
                objectPercepts.stream()
                        .map(ObjectPercept::toInterop)
                        .collect(Collectors.toSet())
        );
    }

    public Set<Point> points() {
        return objectPercepts.stream()
                .map(ObjectPercept::getPoint)
                .collect(Collectors.toSet());
    }

    public String toString() {
        return objectPercepts.toString();
    }

}

