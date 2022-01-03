package Group1.Geometry;

import Group1.Environment.Area;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parallelogram {

    private Point A;
    private Point B;
    private Point C;
    private Point D;
    private LineCut AB;
    private LineCut BC;
    private LineCut CD;
    private LineCut DA;
    private double minX = Double.POSITIVE_INFINITY;
    private double minY = Double.POSITIVE_INFINITY;
    private double maxX = Double.NEGATIVE_INFINITY;
    private double maxY = Double.NEGATIVE_INFINITY;

    /**
     * This Parallelogram will help us with the check, if an agent sees something.
     * In addition it helps with the collision detection.
     * @param A
     * @param B
     * @param C
     * @param D
     */
    public Parallelogram(Point A, Point B, Point C, Point D) {
        this.A = A;
        this.B = B;
        this.C = C;
        this.D = D;
        this.AB = new LineCut(A, B);
        this.BC = new LineCut(B, C);
        this.CD = new LineCut(C, D);
        this.DA = new LineCut(D, A);

        setMax();
    }

    /**
     * This method is very handy for generating an area along the agents path.
     * Provided this path area intersects with another area, the agent would collide on his way from
     * Point a to Point b. Hence, these walks can now be prevented.
     * @param a
     * @param b
     * @param radius
     */
    public Parallelogram(Point a, Point b, double radius) {

        Vector start = new Vector(a);
        Vector end = new Vector(b);
        Vector startToEnd = start.vectorTo(end);

        double direction = startToEnd.direction().getRadians();
        double perpendicularToDirection = Utils.mod(direction + Math.PI / 2, 2 * Math.PI);

        double perpCos = Math.cos(perpendicularToDirection);
        double perpSin = Math.sin(perpendicularToDirection);

        this.A = new Point(a.getX() + radius * perpCos, a.getY() + radius * perpSin);
        this.B = new Point(a.getX() - radius * perpCos, a.getY() - radius * perpSin);

        this.C = new Point(b.getX() - radius * perpCos, b.getY() - radius * perpSin);
        this.D = new Point(b.getX() + radius * perpCos, b.getY() + radius * perpSin);


        this.AB = new LineCut(this.A, this.B);
        this.BC = new LineCut(this.B, this.C);
        this.CD = new LineCut(this.C, this.D);
        this.DA = new LineCut(this.D, this.A);

        setMax();

    }

    /**
     *
     * @param a top left
     * @param b bottom right
     */
    public Parallelogram(Point a, Point b) {
        this(
                a,
                new Point(b.getX(), a.getY()),
                b,
                new Point(a.getX(), b.getY())
        );
    }

    /**
     * creates start parallelogram from the area
     *
     * @param area area to create from
     */
    public Parallelogram(Area area) {
        this(area.getTopLeft(), area.getBottomRight());

    }

    public void setMax() {
        for (Point point: getVertices()) {
            minX = Math.min(minX, point.getX());
            maxX = Math.max(maxX, point.getX());
            minY = Math.min(minY, point.getY());
            maxY = Math.max(maxY, point.getY());
        }
    }

    public List<Point> getVertices() {
        return Arrays.asList(A, B, C, D);
    }

    public List<LineCut> getEdges() {
        return Arrays.asList(AB, BC, CD, DA);
    }


    /* todo possible BUG
        [relevant for RaySystem -> getPercepts]
        This method check only, if a point is in distance range of the paralellogram's vertices
        But what about a point, which is too far from the vertices, but very close to the edge,
        e.g. the agent being the middle of a long wall.
        Need to check the distance to the lineCut (please note: distance to lineCUT not to extended line)
    */
    public boolean isInRange(Point point, Distance distance) {
        for (Point vertex : getVertices()) {
            if (point.getDistance(vertex).getValue() <= distance.getValue()) return true;
        }
        return false;
    }

    /**
     * Gathers all intersection points of a line with the parallelogram, if any.
     * If none exist, an empty list of points is returned.
     * @param cut
     * @return
     */
    public List<Point> getIntersections(LineCut cut) {
        List<Point> points = new ArrayList<>();

        /* todo handle case, if line intersects exactly at vertex point.
            In this case two equal points are returned due to this point being on two lines.*/
        for (LineCut edge : getEdges()) {

            if (edge.isLineIntersecting(cut)) {
                points.add(edge.findIntersection(cut));
            } else{
                continue;
            }
        }
        return points;
    }

    /**
     * Are two parallelograms intersecting?
     * @param p2
     * @return
     */
    public boolean isIntersecting(Parallelogram p2) {
        // first check for intersection
        for (LineCut edge2 : p2.getEdges()) {
            for(LineCut edge1 : this.getEdges()) {
                if (edge1.isLineIntersecting(edge2)) {
                    return true;
                }
            }
        }
        // check whether one of them is inside the other one
        return this.isPointInside(p2.A) || p2.isPointInside(this.A);
    }

    public Point getA() {
        return A;
    }

    public Point getB() {
        return B;
    }

    public Point getC() {
        return C;
    }

    public Point getD() {
        return D;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMaxY() {
        return maxY;
    }

    public double getMinX() {
        return minX;
    }

    public double getMinY() {
        return minY;
    }

    public LineCut getAB() {
        return AB;
    }

    public LineCut getCD() {
        return CD;
    }

    public LineCut getBC() {
        return BC;
    }

    public LineCut getDA() { return DA; }

    /**
     * Checks, if a point is within rectangle border of parallelogram.
     * Imagine drawing a rectangle around the most left to the most right point
     * and the most north and most south point.
     * @param point
     * @return
     */
    public boolean isWithinBorders(Point point) {
        return point.getX() >= minX && point.getX() <= maxX &&
                point.getY() >= minY && point.getY() <= maxY;
    }

    /**
     * Is point not only within above rectangle border, but also within
     * the edges of the parallelogram.
     * @param point
     * @return
     */
    public boolean isPointInside(Point point) {
        if (!isWithinBorders(point)) return false;

        int countIntersections = 0;

        LineCut ray = new LineCut(point, new Point(maxX + 1, maxY + 1));
        for (LineCut side: getEdges()) {

            if(!ray.isLineIntersecting(side)) {
                continue;
            }

            Point intersection = ray.findIntersection(side);

            // also helps with understanding:
            // https://en.wikipedia.org/wiki/Point_in_polygon
            // https://en.wikipedia.org/wiki/Ray_casting
            // https://www.youtube.com/watch?v=EPmAiJbpThE
            if(new Vector(intersection).equals(new Vector(side.getStart()))) {
                boolean isBelowRay = side.getEnd().getY() < intersection.getY();
                if(isBelowRay) countIntersections++;
            } else
            if(new Vector(intersection).equals(new Vector(side.getEnd()))) {
                boolean isBelowRay = side.getStart().getY() < intersection.getY();
                if(isBelowRay) countIntersections++;
            } else {
                countIntersections++;
            }

        }

        return countIntersections % 2 == 1;
    }


    public boolean equals(Parallelogram p2) {
        // check vertices
        List<Point> p1_vertices = this.getVertices();
        List<Point> p2_vertices = p2.getVertices();

        for (int i = 0; i < 4; i++) {
            if ( !(p1_vertices.get(i).getX() - p2_vertices.get(i).getX() <= Precision.THRESHOLD)) {return false;}
            if ( !(p1_vertices.get(i).getY() - p2_vertices.get(i).getY() <= Precision.THRESHOLD)) {return false;}
        }

        // check edges
        List<LineCut> p1_edges = this.getEdges();
        List<LineCut> p2_edges = p2.getEdges();

        for (int i = 0; i < 4; i++) {
            if ( !(p1_edges.get(i).equals(p2_edges.get(i)))) {return false;}
        }
        return true;
    }

}
