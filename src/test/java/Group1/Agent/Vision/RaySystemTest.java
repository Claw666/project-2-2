package Group1.Agent.Vision;

import Group1.Agent.Guard.SimpleGuard;
import Group1.Geometry.LineCut;
import Group1.States.GuardState;
import Interop.Geometry.Angle;
import Interop.Geometry.Direction;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.Vision.FieldOfView;
import SimpleUnitTest.SimpleUnitTest;

import java.util.List;

public class RaySystemTest extends SimpleUnitTest {


    public static void main(String[] args) {

        System.out.println("\n\nRaySystem Test\n");
        System.out.println("\nGenerating rays test");
        generateRaysTests();

    }

    private static void generateRaysTests() {

        it("allows to generate rays 4 rays in all 4 directions", () -> {

            FieldOfView fieldOfView = new FieldOfView(
                    new Distance(10),
                    Angle.fromDegrees(360)
            );

            GuardState guardState = new GuardState(
                    new SimpleGuard(),
                    new Point(0, 0),
                    Direction.fromDegrees(0),
                    5
            );

            List<LineCut> rays = new RaySystem(guardState, fieldOfView, 4).toCuts();

            assertEqual(rays.size(), 4);
            assertEqual((long) rays.get(0).getStart().getX(), (long) new LineCut(new Point(0, 0), new Point(0, -10)).getStart().getX());
            assertEqual((long) rays.get(0).getStart().getY(), (long) new LineCut(new Point(0, 0), new Point(0, -10)).getStart().getY());
            assertEqual((long) rays.get(1).getStart().getX(), (long) new LineCut(new Point(0, 0), new Point(-10, 0)).getStart().getX());
//            assertEqual(rays.get(2), new LineCut(new Point(0, 0), new Point(0, 10)));
//            assertEqual(rays.get(3), new LineCut(new Point(0, 0), new Point(10, 0)));

        });

        it("allows to generate rays in all 4 direction when an agent is moved", () -> {

            FieldOfView fieldOfView = new FieldOfView(
                    new Distance(10),
                    Angle.fromDegrees(360)
            );

            GuardState guardState = new GuardState(
                    new SimpleGuard(),
                    new Point(7, 11),
                    Direction.fromDegrees(0),
                    5
            );

            List<LineCut> rays = new RaySystem(guardState, fieldOfView, 4).toCuts();

            assertEqual(rays.size(), 4);

//            assertEqual(
//                    rays.get(0),
//                    new LineCut(
//                            guardState.getLocation(),
//                            new Vector(new Point(0, -10)).add(guardState.getLocation()).toPoint()
//                    )
//            );
//
//            assertEqual(
//                    rays.get(1),
//                    new LineCut(
//                            guardState.getLocation(),
//                            new Point(-10, 0).add(guardState.getLocation()).toPoint()
//                    )
//            );
//
//            assertEqual(
//                    rays.get(2),
//                    new LineCut(
//                            guardState.getLocation(),
//                            new Point(0, 10).add(guardState.getLocation()).toPoint()
//                    )
//            );
//
//            assertEqual(
//                    rays.get(3),
//                    new LineCut(
//                            guardState.getLocation(),
//                            new Point(10, 0).add(guardState.getLocation()).toPoint()
//                    )
//            );
//
        });

        it("allows to generate rays in all 4 direction when an agent is rotated", () -> {

            FieldOfView fieldOfView = new FieldOfView(
                    new Distance(10),
                    Angle.fromDegrees(360)
            );

            GuardState guardState = new GuardState(
                    new SimpleGuard(),
                    new Point(0, 0),
                    Direction.fromDegrees(90),
                    5
            );

            List<LineCut> rays = new RaySystem(guardState, fieldOfView, 4).toCuts();

            assertEqual(rays.size(), 4);

//            assertEqual(
//                    rays.get(3),
//                    new LineCut(
//                            guardState.getLocation(),
//                            new Point(0, -10).add(guardState.getLocation()).toPoint()
//                    )
//            );
//
//            assertEqual(
//                    rays.get(0),
//                    new LineCut(
//                            guardState.getLocation(),
//                            new Point(-10, 0).add(guardState.getLocation()).toPoint()
//                    )
//            );
//
//            assertEqual(
//                    rays.get(1),
//                    new LineCut(
//                            guardState.getLocation(),
//                            new Point(0, 10).add(guardState.getLocation()).toPoint()
//                    )
//            );
//
//            assertEqual(
//                    rays.get(2),
//                    new LineCut(
//                            guardState.getLocation(),
//                            new Point(10, 0).add(guardState.getLocation()).toPoint()
//                    )
//            );

        });

    }

}
