package Group1.AgentsGroup01.Intruders;

import Group1.AgentsGroup01.Geometry.LineCut;
import Group1.AgentsGroup01.Geometry.Precision;
import Interop.Action.IntruderAction;
import Interop.Action.Move;
import Interop.Action.NoAction;
import Interop.Action.Rotate;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.IntruderPercepts;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;
import Interop.Percept.Vision.ObjectPercepts;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

public class TargetIntruder implements Interop.Agent.Intruder {

    /**
     * AGENTS BRAIN
     */
    // increasing counter of current turn
    private int turnCount;
    // IntruderStrategy stores the current strategy state of the intruder. It's actions as dependent upon it's state.
    private IntruderStrategy intruderState;
    // schedules actions in advance
    private List<IntruderAction> queueNextActions;
    // turn number, when to discover the map
    private int nextDiscoveryJourney;
    // Defines the hand which "touches" the wall while following it.
    private HAND hand;
    
    public TargetIntruder() {
        turnCount = 1;
        intruderState = IntruderStrategy.orientate;
        queueNextActions = new ArrayList<>();
        nextDiscoveryScheduler();
        if(Math.random() > 0.5) {
            hand = HAND.LEFT;
        }else{
            hand = HAND.RIGHT;
        }


    }

    @Override
    public IntruderAction getAction(IntruderPercepts percepts) {

        turnCount++;

        if (!percepts.wasLastActionExecuted()) {
            queueNextActions.clear();
            return rescueAgent(percepts);
        }

        if (queueNextActions.size() > 0){
            return executeScheduledAction();
        }


        // todo how to get out of a window or door properly? // same for sentry tower
        if (percepts.getAreaPercepts().isInDoor() | percepts.getAreaPercepts().isInWindow()) {
            return new Move(percepts.getScenarioIntruderPercepts().getMaxMoveDistanceIntruder());
        }

        Angle leftorright = percepts.getTargetDirection().getDistance(Angle.fromDegrees(new Point(0,1).getClockDirection().getDegrees()));
        if (leftorright.getDegrees() < 90) {
            if (hand ==  HAND.LEFT){
                hand = HAND.RIGHT;
            }
        }
        else if (leftorright.getDegrees() > 270 ){
            if (hand == HAND.RIGHT)
            hand = HAND.LEFT;
        }



        if (intruderState == IntruderStrategy.orientate) {
            return nextOrientateAction(percepts);
        }

        if (intruderState == IntruderStrategy.explore) {
            // Agent should discover the map only while being in exploring state
            if (turnCount >= nextDiscoveryJourney) {
                nextDiscoveryScheduler();
                return rescueAgent(percepts);
            }
            return nextExploreAction(percepts);
        }

        return new NoAction();
    }




    /**
     * Uniform distribution to schedule agent's random map discoveries.
     */
    private void nextDiscoveryScheduler(){
        nextDiscoveryJourney = turnCount + 80 + (int) (Math.random() * 60);
    }

    /**
     * Uses vision to orientate on the map.
     * Main goal:
     *  - Go straight to find a wall.
     *  - Align agent perpendicular to the wall.
     * @param percepts
     * @return
     */
    private IntruderAction nextOrientateAction(IntruderPercepts percepts){
        ObjectPercepts visionObjects = percepts.getVision().getObjects();
        ObjectPercepts walls = visionObjects.filter(percept -> percept.getType().equals(ObjectPerceptType.Wall));

        // quickly find a wall
        // need to have at least 2 wall points to interpolate line and another point to verify if it's on the line
        if (walls.getAll().size() < 3) {

            Distance feasibleDistance = null;
            try {
                feasibleDistance = calculateFeasibleDistance(
                        walls,
                        new Distance(percepts.getVision().getFieldOfView().getRange().getValue() * 0.5),
                        percepts.getScenarioIntruderPercepts().getMaxMoveDistanceIntruder());
            } catch (Exception e) {
                return rescueAgent(percepts);
            }

            return new Move(feasibleDistance);


        }

        // orientate on wall, if possible
        LineCut wallLine;
        try {
            wallLine = findLine(walls);
        } catch (Exception e) {
            return rescueAgent(percepts);
        }

        // calculate angle and turn accordingly
        Angle angleToXAxis = calculateAngleToXAxis(wallLine);
        Angle maxRotationAngle = percepts.getScenarioIntruderPercepts().getScenarioPercepts().getMaxRotationAngle();

        // unless the agent is already standing perpendicular to the wall
        if (Math.abs(angleToXAxis.getRadians()) < Precision.threshold) {
            intruderState = IntruderStrategy.explore;
            return new NoAction();
        }

        double angleToRotate = angleToXAxis.getRadians() * (1);
        if (Math.abs(angleToRotate) >= maxRotationAngle.getRadians()){
            double rotate = ((int) Math.signum(angleToRotate) * maxRotationAngle.getRadians());
            return new Rotate(Angle.fromRadians(rotate));
        } else {

            return new Rotate(Angle.fromRadians(angleToRotate));
        }
    }


    /**
     * Given vision percepts, this function returns either a modified distance to the closed object,
     * or the maximum allowed distance, if the closed object is too far away.
     * @param objectPercepts
     * @param distanceFromWall
     * @param maxMove
     * @return
     */
    private Distance calculateFeasibleDistance(ObjectPercepts objectPercepts, Distance distanceFromWall, Distance maxMove) throws Exception {
        OptionalDouble minDistance = objectPercepts.getAll().stream().mapToDouble(p -> p.getPoint().getDistanceFromOrigin().getValue()).min();
        double feasibleDistance = minDistance.orElse(distanceFromWall.getValue() * 2) - distanceFromWall.getValue();

        if (maxMove.getValue() <= feasibleDistance){
            return maxMove;
        }

        if (feasibleDistance < 0) {
            throw new Exception("feasible distance is below zero.");
        }

        return new Distance(feasibleDistance);
    }


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


    /**
     * Calculating the angle between a line and the x-axis.
     * @param lineCut
     * @return
     */
    private Angle calculateAngleToXAxis(LineCut lineCut) {
        double x = lineCut.getEnd().getX() - lineCut.getStart().getX();
        double y = lineCut.getEnd().getY() - lineCut.getStart().getY();
        double theta = Math.atan2(y, x);
        return Angle.fromRadians(theta);
    }


    /**
     * Rescues the agent if stuck.
     * @param percepts
     * @return
     */
    private IntruderAction rescueAgent(IntruderPercepts percepts) {
        double randomRotate = (int) (180 * Math.random());

        if (randomRotate < 90) {
            scheduleRotate(Angle.fromDegrees(90 + randomRotate), percepts);
        }else{
            scheduleRotate(Angle.fromDegrees(-randomRotate), percepts);
        }
        scheduleMove(percepts.getScenarioIntruderPercepts().getMaxMoveDistanceIntruder(), percepts);

        intruderState = IntruderStrategy.orientate;
        return executeScheduledAction();
    }


    /**
     * Returns wall points which are directly in front of the agent.
     * @param walls
     * @return
     */
    private Point getWallStraightAhead(ObjectPercepts walls){
        List<ObjectPercept> sortedPoints = walls
                .getAll()
                .stream()
                .filter(p -> p.getPoint().getClockDirection().getDistance(Angle.fromDegrees(0)).getRadians() < 0.2)
                .sorted(Comparator.comparing(p -> p.getPoint().getClockDirection().getDistance(Angle.fromDegrees(0)).getRadians()))
                .collect(Collectors.toList());
        try{
            return sortedPoints.get(0).getPoint();
        } catch (Exception e){
            return null;
        }
    }


    /**
     * Using the Pledge Algorithm to explore the map.
     * @param percepts
     * @return
     */
    private IntruderAction nextExploreAction(IntruderPercepts percepts) {

        ObjectPercepts visionObjects = percepts.getVision().getObjects();
        ObjectPercepts walls = visionObjects.filter(percept -> percept.getType().equals(ObjectPerceptType.Wall));

        if (walls.getAll().size() == 0) {
            // get to viewRange
            scheduleMove(new Distance(percepts.getVision().getFieldOfView().getRange().getValue() * 1), percepts);

            // turn around
            scheduleRotate(Angle.fromDegrees(90 * hand.modifier * -1), percepts);

            return executeScheduledAction();
        }

        Point wallPointStraightAhead = getWallStraightAhead(walls);
        if (wallPointStraightAhead == null) {
            /*ObjectPercept mostLeftWall = getWallMostLeft(walls);
            if (mostLeftWall.getPoint().getClockDirection().getRadians() < 0.3){

            }*/
            return new Move(percepts.getScenarioIntruderPercepts().getMaxMoveDistanceIntruder());
        }

        double distanceToWall = wallPointStraightAhead.getDistanceFromOrigin().getValue();
        //double minDistance = percepts.getVision().getFieldOfView().getRange().getValue() * 0.5;
        double minDistance = percepts.getScenarioIntruderPercepts().getMaxMoveDistanceIntruder().getValue() * 1.5;
        if (distanceToWall > minDistance) {
            return new Move(new Distance(Math.min(distanceToWall - minDistance,
                    percepts.getScenarioIntruderPercepts().getMaxMoveDistanceIntruder().getValue())));
        }
        Angle leftorright = wallPointStraightAhead.getClockDirection().getDistance(percepts.getTargetDirection());
        if (leftorright.getDegrees() < 90) {
            // -1 is RIGHT HAND modifier, so turns right
            scheduleRotate(Angle.fromDegrees(90 * (-1)), percepts);
        }
        else if (leftorright.getDegrees() > 270 ){
            // 1 is LEFT HAND modifier, so turns left
            scheduleRotate(Angle.fromDegrees(90 * (1)), percepts);
        }
        else scheduleRotate(Angle.fromDegrees(90 * hand.modifier), percepts);

        return executeScheduledAction();
    }


    /**
     * Scheduling a series of moves by taking care of the max move distance.
     * @param distance
     * @param percepts
     */
    private void scheduleMove(Distance distance, IntruderPercepts percepts) {
        double remainingDistance = distance.getValue();
        double maxMove = percepts.getScenarioIntruderPercepts().getMaxMoveDistanceIntruder().getValue();

        while(remainingDistance > 0) {
            queueNextActions.add(new Move(new Distance(Math.min(remainingDistance, maxMove))));
            remainingDistance = remainingDistance - maxMove;
        }
    }


    /**
     * Scheduling a series of rotations by taking care of the max rotation angle.
     * @param angle
     * @param percepts
     */
    private void scheduleRotate(Angle angle, IntruderPercepts percepts) {
        double remainingAngle = angle.getDegrees();
        int sign = (int) Math.signum(remainingAngle);
        double maxRotate = percepts.getScenarioIntruderPercepts().getScenarioPercepts().getMaxRotationAngle().getDegrees();
        double rotateStep = 0;

        while(Math.abs(remainingAngle) > Precision.threshold) {

            if (Math.abs(remainingAngle) < maxRotate){
                rotateStep = remainingAngle;
            } else {
                rotateStep = maxRotate * sign;
            }
            queueNextActions.add(new Rotate(Angle.fromDegrees(rotateStep)));
            remainingAngle = (Math.abs(remainingAngle) - Math.abs(rotateStep)) * sign;

        }
    }


    /**
     * Executes the next scheduled action according to a FIFO queue.
     * @return
     */
    private IntruderAction executeScheduledAction(){
        IntruderAction nextAction = queueNextActions.get(0);
        queueNextActions.remove(0);
        return nextAction;
    }


    /**
     * IntruderStrategy stores the current strategy state of the intruder. It's actions as dependent upon it's state.
     */
    private enum IntruderStrategy {
        orientate, explore
    }

    /**
     * Defines the hand which "touches" the wall while following it.
     * Hence, this enum defines which way the agent turns.
     */
    private enum HAND {

        LEFT(1), RIGHT(-1);

        private final int modifier;

        HAND(final int newModifier){
            modifier = newModifier;
        }
    }



}
