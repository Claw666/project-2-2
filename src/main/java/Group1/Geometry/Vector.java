package Group1.Geometry;

import Group1.Agent.Node;
import Interop.Geometry.Angle;
import Interop.Geometry.Direction;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;

public class Vector {

    private double x;
    private double y;

    public Vector() {
        this(0, 0);
    }

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector(Point a, Direction d, Distance distance) {
        this.x = a.getX() + Math.cos(d.getRadians()) * distance.getValue();
        this.y = a.getY() + Math.sin(d.getRadians()) * distance.getValue();
    }

    public Vector(Direction d, Distance distance) {
        this.x = -Math.cos(d.getRadians()) * distance.getValue();
        this.y = Math.sin(d.getRadians()) * distance.getValue();
    }

    public Vector(Direction d) {
        this.x = Math.cos(d.getRadians());
        this.y = Math.sin(d.getRadians());
    }

    public Vector(Point point) {
        this(point.getX(), point.getY());
    }

    /*public Vector(Node node) {
        this(node.getLocation);
    }
*/
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Vector scale(double c) {
        return new Vector(x * c, y * c);
    }

    public Vector add(Vector other) {
        return new Vector(
                x + other.getX(),
                y + other.getY()
        );
    }

    /**
     * @param point
     * @return
     */
    public Vector add(Point point) {
        return new Vector(
                x + point.getX(),
                y + point.getY()
        );
    }

    public Vector vectorTo(Vector other) {
        return other.subtract(this);
    }

    public Vector subtract(Vector other) {
        return new Vector(
                x - other.getX(),
                y - other.getY()
        );
    }

    public Direction direction() {
        double radAngle = Math.atan2(y, x);
        while (radAngle < 0) radAngle += Math.PI * 2;
        while (radAngle > 2 * Math.PI) radAngle -= Math.PI * 2;
        return Direction.fromRadians(radAngle);
    }

    /**
     * rotate clockwise
     *
     * @param center around center
     * @param theta  for theta radians
     * @return new rotated vector
     */

    public Vector rotateAround(Vector center, double theta) {
        double cosTheta = Math.cos(-theta);
        double sinTheta = Math.sin(-theta);
        Vector centered = this.subtract(center);
        Vector rotated = new Vector(
                centered.x * cosTheta - centered.y * sinTheta,
                centered.x * sinTheta + centered.y * cosTheta
        );
        return rotated.add(center);
    }

    public Vector rotate(Angle angle) {
        return this.rotate(angle.getRadians());
    }

    public Vector rotate(double theta) {
        return this.rotateAround(new Vector(), theta);
    }

    public double length() {
        return Math.sqrt(
                Math.pow(x, 2.0) +
                        Math.pow(y, 2.0)
        );
    }

    public Vector unit() {
        double length = length();
        if (Math.abs(length) < Precision.THRESHOLD) {
            return new Vector();
        } else {
            return this.scale(1 / length);
        }
    }

    public Vector abs() {
        return new Vector(
                Math.abs(x),
                Math.abs(y)
        );
    }

    public Vector from(Vector vector) {
        return this.subtract(vector);
    }

    public Vector from(Point point) {
        return this.subtract(new Vector(point));
    }

    public Point toPoint() {
        return new Point(x, y);
    }

    public boolean equals(Vector vector) {
        Vector absDiff = this.subtract(vector).abs();
        return absDiff.x <= Precision.THRESHOLD
                && absDiff.y <= Precision.THRESHOLD;
    }


    public String toString() {
        return "vec:{x:" + x + " y:" + y + "}";
    }

}