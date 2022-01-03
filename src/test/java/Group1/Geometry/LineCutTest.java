package Group1.Geometry;

import Interop.Geometry.Point;
import SimpleUnitTest.SimpleUnitTest;

public class LineCutTest extends SimpleUnitTest {

    public static void main(String[] args) {
        System.out.println("\n\n\nLineCut Tests\n");
        System.out.println("creating lineCuts:");
        creation();
        System.out.println("\nchecking intersections:");
        intersections();
        System.out.println("\nchecking points on line:");
        pointsOnLine();
        System.out.println("\nchecking points of line:");
        pointsOfLineCut();
    }

    static void creation() {
        it("returns lines are equal", () -> {
            LineCut l1 = new LineCut(new Point(1,2), new Point(3,6));
            LineCut l2 = new LineCut(new Point(1,2), new Point(3,6));
            assertTrue(l1.equals(l1), "should be equal");
            assertTrue(l1.equals(l2), "should be equal");
            assertTrue(l2.equals(l1), "should be equal");
        });

        it("returns lines are not equal", () -> {
            LineCut l1 = new LineCut(new Point(0,0), new Point(3,6));
            LineCut l2 = new LineCut(new Point(0,1), new Point(7,1));
            assertTrue(!l1.equals(l2), "should not be equal");
            assertTrue(!l2.equals(l1), "should not be equal");
        });
    }

    static void intersections() {
        /*
          |
        --o--
          |
         */
        it("returns correct intersection point - case: cross", () -> {
            LineCut l1 = new LineCut(new Point(3,0), new Point(3,6));
            LineCut l2 = new LineCut(new Point(0,1), new Point(7,1));
            Point pIntersect = l1.findIntersection(l2);

            assertEqual(pIntersect.getX(), 3, 0);
            assertEqual(pIntersect.getY(), 1, 0);
            assertTrue(l1.isLineIntersecting(l2), "should be true.");
        });

        /*
        |
        o--
         */
        it("returns correct intersection point - case: corner", () -> {
            LineCut l1 = new LineCut(new Point(3,1), new Point(3,6));
            LineCut l2 = new LineCut(new Point(3,1), new Point(7,1));
            Point pIntersect = l1.findIntersection(l2);

            assertEqual(pIntersect.getX(), 3, 0);
            assertEqual(pIntersect.getY(), 1, 0);
            assertTrue(l1.isLineIntersecting(l2), "should be true.");
        });

        /*
        |
        o--
        |
         */
        it("returns correct intersection point - case: junction", () -> {
            LineCut l1 = new LineCut(new Point(3,0), new Point(3,6));
            LineCut l2 = new LineCut(new Point(3,1), new Point(7,1));
            Point pIntersect = l1.findIntersection(l2);

            assertEqual(pIntersect.getX(), 3, 0);
            assertEqual(pIntersect.getY(), 1, 0);
            assertTrue(l1.isLineIntersecting(l2), "should be true.");
        });

        /*
        |
        |  ------
        |
         */
        it("returns intersection point of null - case: not attached line", () -> {
            LineCut l1 = new LineCut(new Point(3,0), new Point(3,6));
            LineCut l2 = new LineCut(new Point(4,1), new Point(7,1));
            Point pIntersect = l1.findIntersection(l2);

            assertTrue(pIntersect == null);
            assertTrue(!l1.isLineIntersecting(l2), "should be false.");
        });
        it("returns intersection point of null - case: not attached line (reversed method call)", () -> {
            LineCut l1 = new LineCut(new Point(3,0), new Point(3,6));
            LineCut l2 = new LineCut(new Point(4,1), new Point(7,1));
            Point pIntersect = l2.findIntersection(l1);

            assertTrue(pIntersect == null);
            assertTrue(!l1.isLineIntersecting(l2), "should be false.");
            assertTrue(!l2.isLineIntersecting(l1), "should be false.");
        });

        /*
        | l1  |
        ------------
            |  l2  |
         */
        it("returns intersection point of null - case: lineCuts extend each other", () -> {
            LineCut l1 = new LineCut(new Point(1,1), new Point(3,3));
            LineCut l2 = new LineCut(new Point(2,2), new Point(5,5));
            Point pIntersect = l1.findIntersection(l2);

            assertTrue(pIntersect == null);
            assertTrue(!l1.isLineIntersecting(l2), "should be false.");
        });
    }

    static void pointsOnLine() {
        it("returns points are on lineCut", () -> {
            LineCut l1 = new LineCut(new Point(1,1), new Point(2,1));

            assertTrue(l1.isPointOnLine(new Point(1,1)), "Point should be on line.");
            assertTrue(l1.isPointOnLine(new Point(1.3,1)), "Point should be on line.");
            assertTrue(l1.isPointOnLine(new Point(2,1)), "Point should be on line.");
        });

        it("returns points are not on segment of the lineCut while being on extended line", () -> {
            LineCut l1 = new LineCut(new Point(1,1), new Point(2,1));

            assertTrue(!(l1.isPointOnLine(new Point(0,1))), "Point should not be on line.");
            assertTrue(!(l1.isPointOnLine(new Point(3,1))), "Point should not be on line.");
        });

        it("returns points are not on lineCut", () -> {
            LineCut l1 = new LineCut(new Point(1,1), new Point(2,1));

            assertTrue(!(l1.isPointOnLine(new Point(1,2))), "Point should not be on line.");
            assertTrue(!(l1.isPointOnLine(new Point(3,-2))), "Point should not be on line.");
            assertTrue(!(l1.isPointOnLine(new Point(-1,-1))), "Point should not be on line.");
        });

        // points are on line, but not on segment of lineCut
        // points are not on line or lineCut
    }

    static void pointsOfLineCut() {
        it("returns the correct start and end points of lineCut", () -> {
            LineCut l1 = new LineCut(new Point(5,0), new Point(7,8));

            assertEqual(l1.getStart().getX(), 5, 0);
            assertEqual(l1.getStart().getY(), 0, 0);
            assertEqual(l1.getEnd().getX(), 7, 0);
            assertEqual(l1.getEnd().getY(), 8, 0);
        });

        // todo which behavior do we want with a lineCut of length zero?
        it("returns the correct start and end points of lineCut, which has length 0", () -> {
            LineCut l1 = new LineCut(new Point(0,0), new Point(0,0));

            assertEqual(l1.getStart().getX(), 0, 0);
            assertEqual(l1.getStart().getY(), 0, 0);
            assertEqual(l1.getEnd().getX(), 0, 0);
            assertEqual(l1.getEnd().getY(), 0, 0);
        });
    }
}
