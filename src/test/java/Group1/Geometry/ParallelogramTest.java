package Group1.Geometry;

import Group1.Environment.Area;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.Vision.ObjectPerceptType;
import SimpleUnitTest.SimpleUnitTest;

import java.util.Arrays;
import java.util.List;

public class ParallelogramTest extends SimpleUnitTest {

    public static void main(String[] args) {
        System.out.println("\n\n\nParallelogram Tests\n");
        System.out.println("creating parallelograms:");
        creation();
        System.out.println("\nchecking intersecions:");
        intersections();
        System.out.println("\nchecking points related to parallelogram:");
        wherePointIs();
    }


    static void creation() {
        // create original parallelogram to check
        Point A = new Point(1,3);
        Point B = new Point(5,3);
        Point C = new Point(5,1);
        Point D = new Point(1,1);
        Point x = new Point(1,2);
        Point y = new Point(5,2);
        double radius = 1.0;
        Parallelogram para_original = new Parallelogram(A,B,C,D);

        it("returns true for equal parallelograms", () -> {
            Parallelogram para1 = new Parallelogram(A,B,C,D);
            assertTrue(para1.equals(para_original));
        });

        it("returns false for not equal parallelograms - case: different starting point, same order", () -> {
            Parallelogram para1 = new Parallelogram(B,C,D,A);
            assertTrue(!para1.equals(para_original));
        });

        it("returns false for not equal parallelograms - case: different order", () -> {
            Parallelogram para1 = new Parallelogram(C,A,D,B);
            assertTrue(!para1.equals(para_original));
        });

        it("returns false for not equal parallelograms - case: just a line", () -> {
            Parallelogram para1 = new Parallelogram(A,B,A,B);
            assertTrue(!para1.equals(para_original));
        });

        it("returns correct paralellogram by creating using two points", () -> {
            Parallelogram para1 = new Parallelogram(A,C);
            assertTrue(para1.equals(para_original));
        });

        it("returns correct paralellogram by creating using area", () -> {
            Area area = new Area(A,C, ObjectPerceptType.EmptySpace);
            Parallelogram para1 = new Parallelogram(area);
            assertTrue(para1.equals(para_original));
        });

        // todo this fails, pls fix
        it("returns correct paralellogram by creating using two points and radius", () -> {
            Parallelogram para1 = new Parallelogram(x, y, radius);
            assertTrue(para1.equals(para_original));
        });
    }

    static void intersections() {
        // create original parallelogram to check
        Point A = new Point(1,3);
        Point B = new Point(5,3);
        Point C = new Point(5,1);
        Point D = new Point(1,1);
        Parallelogram para_original = new Parallelogram(A,B,C,D);

        it("returns parallelograms intersects - case: corner point", () -> {
            Point A2 = new Point(5,4);
            Point C2 = new Point(6,3);
            Parallelogram para1 = new Parallelogram(A2,C2);

            assertTrue(para_original.isIntersecting(para1));
        });

        it("returns parallelograms intersects - case: two points", () -> {
            Point A2 = new Point(4,5);
            Point C2 = new Point(6,3);
            Parallelogram para1 = new Parallelogram(A2,C2);

            assertTrue(para_original.isIntersecting(para1));
        });

        it("returns parallelograms does not intersect - case: no points", () -> {
            Point A2 = new Point(10,10);
            Point C2 = new Point(11,9);
            Parallelogram para1 = new Parallelogram(A2,C2);

            assertTrue(!para_original.isIntersecting(para1));
        });

        it("returns correct points of parallelogram intersecting with line - case: one intersection", () -> {
            LineCut l1 = new LineCut(new Point(0,2), new Point(2,2));
            List<Point> pIntersections = para_original.getIntersections(l1);

            assertEqual(pIntersections.size(), 1, 0);

            assertEqual(pIntersections.get(0).getX(),1, 0);
            assertEqual(pIntersections.get(0).getY(),2, 0);
        });

        // todo discuss in team: does the order of points actually matter?
        it("returns correct points of parallelogram intersecting with line - case: two intersections", () -> {
            LineCut l1 = new LineCut(new Point(0,2), new Point(7,2));
            List<Point> pIntersections = para_original.getIntersections(l1);

            assertEqual(pIntersections.size(), 2, 0);
            assertTrue(pIntersections.toString() == "[Point{x=1.0, y=2.0}, Point{x=5.0, y=2.0}]",
                "should be [Point{x=1.0, y=2.0}, Point{x=5.0, y=2.0}], but is " + pIntersections.toString());
        });

        it("returns not points of parallelogram intersecting with line - case: zero intersections", () -> {
            LineCut l1 = new LineCut(new Point(10,10), new Point(11,9));
            List<Point> pIntersections = para_original.getIntersections(l1);

            assertEqual(pIntersections.size(), 0, 0);
        });



    }

    static void wherePointIs() {
        // create original parallelogram to check
        Point A = new Point(1,3);
        Point B = new Point(5,3);
        Point C = new Point(5,1);
        Point D = new Point(1,1);
        Parallelogram para_original = new Parallelogram(A,B,C,D);
        Distance range = new Distance(0.5);

        it("returns is in range of vertex", () -> {
            Point p1 = new Point(0.5, 3);   // first vertex, maximum distance
            Point p2 = new Point(1, 0.7);   // second vertex
            Point p3 = new Point(5.2, 0.7); // third vertex
            Point p4 = new Point(5.35, 3.35); // fourth vertex, abount maximum distance

            assertTrue(para_original.isInRange(p1, range));
            assertTrue(para_original.isInRange(p2, range));
            assertTrue(para_original.isInRange(p3, range));
            assertTrue(para_original.isInRange(p4, range));
        });

        // todo pls fix, see comment above this method in parallelogram.java
        it("returns is in range of edge (not vertex, but still should be true)", () -> {
            Point p1 = new Point(3, 3.2);   // first edge
            Point p2 = new Point(5.3, 2.1);   // second edge
            Point p3 = new Point(2, 0.7); // third edge
            Point p4 = new Point(0.5, 2); // fourth edge, maximum distance

            assertTrue(para_original.isInRange(p1, range));
            assertTrue(para_original.isInRange(p2, range));
            assertTrue(para_original.isInRange(p3, range));
            assertTrue(para_original.isInRange(p4, range));
        });

        it("returns is out of range", () -> {
            Point p1 = new Point(10, 10);
            Point p2 = new Point(-1, -1);
            Point p3 = new Point(1, 0.4);
            Point p4 = new Point(0.4, 2);

            assertTrue(!para_original.isInRange(p1, range));
            assertTrue(!para_original.isInRange(p2, range));
            assertTrue(!para_original.isInRange(p3, range));
            assertTrue(!para_original.isInRange(p4, range));
        });

        // todo discuss with team about this behavior: is a point on vertex inRange?
        it("returns is in range - case: on corner point", () -> {
            Point p1 = new Point(1, 1);

            assertTrue(para_original.isInRange(p1, range));
        });

        // todo discuss with team about this behavior: is a point inside the parallelogram inRange?
        it("returns is in range - case: inside of parallelogram", () -> {
            Point p1 = new Point(1.2, 1.2);

            assertTrue(para_original.isInRange(p1, range));
        });

        // todo pls fix, this point is inside, but method says no
        it("returns is inside of parallelogram (1.3,1.3)", () -> {
            Point p1 = new Point(1.3, 1.3);

            assertTrue(para_original.isPointInside(p1));
        });

        it("returns is inside of parallelogram (4,2)", () -> {
            Point p2 = new Point(4, 2);

            assertTrue(para_original.isPointInside(p2));
        });


        it("returns is not inside of parallelogram", () -> {
            Point p1 = new Point(0, 0);
            Point p2 = new Point(6, 6);

            assertTrue(!para_original.isPointInside(p1));
            assertTrue(!para_original.isPointInside(p2));
        });

        // todo discuss with team is a point inside a paralellogram, if it is on the edge or a vertex?
        it("returns is inside of parallelogram - case: on vertex or edge - vertex bottom left", () -> {
            Point p1 = new Point(1, 1);

            assertTrue(para_original.isPointInside(p1));
        });

        it("returns is inside of parallelogram - case: on vertex or edge - vertex top right", () -> {
            Point p3 = new Point(5, 3);

            assertTrue(para_original.isPointInside(p3));
        });

        // todo pls fix, why does this fail? Point is on edge. Inconsistent with below test of edge right.
        it("returns is inside of parallelogram - case: on vertex or edge - edge bottom", () -> {
            Point p2 = new Point(3, 1);

            assertTrue(para_original.isPointInside(p2));
        });

        it("returns is inside of parallelogram - case: on vertex or edge - edge right", () -> {
            Point p2 = new Point(5, 2);

            assertTrue(para_original.isPointInside(p2));
        });

        it("returns point is in the borders of parallelogram", () -> {
            Point p1 = new Point(1, 1);
            Point p2 = new Point(3, 3);
            double r = 2;

            Parallelogram para1 = new Parallelogram(p1, p2, r);

            Point pIn = new Point(2, 2);

            assertTrue(para1.isWithinBorders(pIn));

        });

    }

}
