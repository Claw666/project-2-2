package Group1.Agent.Guard;

import Group1.AgentsGroup01.Geometry.LineCut;
import Group1.AgentsGroup01.Geometry.Precision;
import Group1.AgentsGroup01.Guards.MazeGuard;
import Group1.States.GuardState;
import Interop.Action.GuardAction;
import Interop.Action.NoAction;
import Interop.Action.Rotate;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.GuardPercepts;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;
import Interop.Percept.Vision.ObjectPercepts;
import Interop.Action.Move;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.OptionalDouble;


public class ChasingGuard implements Interop.Agent.Guard {

    // agents brain memory
    private int turns;
    private String lastAction;

    // GuardState stores the current strategy state of the guard. It's actions as dependent upon it's state.
    private GuardState guardState;


    public ChasingGuard() {
        turns = 1;
        lastAction = "none";
        guardState = GuardState.chase;


    }

    @Override
    public GuardAction getAction(GuardPercepts percepts) {
        System.out.println("Run: " + turns);
        turns++;

        if (turns == 2) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (turns >= 20) {
            System.exit(0);
        }

        ObjectPercepts objects = percepts.getVision().getObjects();
        ObjectPercepts intruder = objects.filter(percept -> percept.getType().equals(ObjectPerceptType.Guard));


        // ###########################################
        // starting the chase algorithm on intruder detected

        if (intruder.getAll().size() > 0) {
            //System.out.println(intruder[1]);
            //System.out.println("volg");
            // what you know:
            for (ObjectPercept percept : intruder.getAll()) {
                /*
                System.out.println("The type is intruder: " + percept.getType());
                System.out.println("The location of the intruder based on points: " + percept.getPoint());
                System.out.println("The distance from intruder to current guard: " + percept.getPoint().getDistanceFromOrigin());
                System.out.println("The clock direction to the intruder" + percept.getPoint().getClockDirection());
                /*
                clock direction to intruder:
                image a coordinate system.
                - the y-axis is the guard looking straight
                - angle increases to the right: 1,2,3,4 (but of type double with decimals)
                - angle decreases to the left: 360, 259, 258 (but of type double with decimals)
                 */
            }

        }

        if (guardState == GuardState.chase) {
            return nextOrientateActionChase(percepts);
        }


        // end of chase algorithm on intruder detected
        // ###########################################


        return new NoAction();
    }

    private GuardAction nextOrientateActionChase(GuardPercepts percepts) {
        ObjectPercepts visionObjects = percepts.getVision().getObjects();
        ObjectPercepts intruder = visionObjects.filter(percept -> percept.getType().equals(ObjectPerceptType.Guard));   //this needs to be intruder

        // quickly find a wall
        // need to have at least 2 wall points to interpolate line and another point to verify if it's on the line
        System.out.println(intruder.getAll().size());
        if (intruder.getAll().size() < 3) {
            return new Move(calculateFeasibleDistance(intruder,
                    new Distance(percepts.getVision().getFieldOfView().getRange().getValue() * 0.5),
                    percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard()));
        }

        // Todo orientate on intruder and turn accordingly
        /*
        LineCut wallLine;
        try {
            wallLine = findLine(intruder);
        } catch (Exception e) {
            // return rescueAgent(percepts);
            return new NoAction();
        }

        // calculate angle and turn accordingly
        Angle angleToXAxis = calculateAngleToXAxis(wallLine);
        Angle maxRotationAngle = percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle();

        // unless the agent is already standing perpendicular to the wall
        if (Math.abs(angleToXAxis.getRadians()) < Precision.threshold) {
            //guardState = ChasingGuard.GuardState.explore;
            return new NoAction();
        }

        double angleToRotate = angleToXAxis.getRadians() * (1);
        if (Math.abs(angleToRotate) >= maxRotationAngle.getRadians()){
            double rotate = ((int) Math.signum(angleToRotate) * maxRotationAngle.getRadians());
            return new Rotate(Angle.fromRadians(rotate));
        } else {
            return new Rotate(Angle.fromRadians(angleToRotate));
        } */
        return new NoAction();
    }

    private Distance calculateFeasibleDistance(ObjectPercepts objectPercepts, Distance distanceFromWall, Distance maxMove){
        OptionalDouble minDistance = objectPercepts.getAll().stream().mapToDouble(p -> p.getPoint().getDistanceFromOrigin().getValue()).min();
        double feasibleDistance = minDistance.orElse(distanceFromWall.getValue() * 2) - distanceFromWall.getValue();

        if (maxMove.getValue() <= feasibleDistance){
            return maxMove;
        }
        return new Distance(feasibleDistance);
    }

    //unused
    private Angle calculateAngleToXAxis(LineCut lineCut) {
        double x = lineCut.getEnd().getX() - lineCut.getStart().getX();
        double y = lineCut.getEnd().getY() - lineCut.getStart().getY();
        double theta = Math.atan2(y, x);
        return Angle.fromRadians(theta);
    }

    //unused
    /**
     * Given certain vision percepts, this functions tries to find a line that goes through these points.
     * @param objects
     * @return
     * @throws Exception
     */
    private LineCut findLine(ObjectPercepts objects) throws Exception {
        Point firstPoint;
        Point lastPoint = null;
        List<Point> points = new ArrayList<Point>();
        for (ObjectPercept percept: objects.getAll()){
            points.add(percept.getPoint());
        }
        points.sort(Comparator.comparing(Point::getX));


        for (int i = 0; i < points.size() - 2; i++) {
            firstPoint = points.get(i);
            LineCut lineCut = new LineCut(firstPoint, points.get(i+1));

            for (Point point: points.subList(i+2,points.size())){
                if (lineCut.isPointOnExtendedLine(point)) {
                    lastPoint = point;
                }
            }
            if (lastPoint != null) {
                return new  LineCut(firstPoint, lastPoint);
            }
        }

        throw new Exception("Implement rescue strategy!");
    }

    private enum GuardState {
        orientate, explore, chase
    }


}
