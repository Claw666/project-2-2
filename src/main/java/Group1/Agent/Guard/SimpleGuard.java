package Group1.Agent.Guard;

import Interop.Action.GuardAction;
import Interop.Action.Move;
import Interop.Action.NoAction;
import Interop.Action.Rotate;
import Interop.Geometry.Angle;
import Interop.Percept.GuardPercepts;

import java.util.Random;

public class SimpleGuard implements Interop.Agent.Guard {

    public SimpleGuard() {

    }

    @Override
    public GuardAction getAction(GuardPercepts percepts) {

        // todo do not delete. Try as soon as rays are done and thus objectPercepts are filled
        // First implementation of a little smartness making the agent better than random
        // Using a Principle Component Analysis approach to decide whether to turn right or left.
        // Idea: By looking at the x-axis devided by the y-axis. The more points (hindering objects)
        // on either side of the y-axis are -> choose to turn the other way
        // BUT if equally distributed, there is a wall in front, turn as far as agent can.
        /*Set<ObjectPercept> objectPercepts = percepts.getVision().getObjects().getAll();
        int numOfPoints = objectPercepts.size();
        double[] x_values = new double[numOfPoints];
        double[] y_values = new double[numOfPoints];
        int i = 0;

        for (ObjectPercept objectPercept : objectPercepts) {
            x_values[i] = objectPercept.getPoint().getX();
            y_values[i] = objectPercept.getPoint().getY();
        }

        // priority 1: many points are very near to the agent so that with the next move it would stand and the wall
        double y_sum = DoubleStream.of(y_values).sum();
        if (y_sum / numOfPoints <= percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue()) {
            Rotate rotate = new Rotate(percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle());
            return rotate;
        }

        // priority 2: points are far enough away. We could make some moves and stay random
        // But still: In case we turn, we want to turn in the correct direction
        double[] x_values_pos = Arrays.stream(x_values).filter(x -> x >= 0).toArray();
        double[] x_values_neg = Arrays.stream(x_values).filter(x -> x < 0).toArray();
        Rotate rotate;
        double weight = Math.abs(x_values_pos.length - x_values_neg.length);
        int maxRotationAngleTotal = (int) Math.round(percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians());
        double maxRotationAngle = (maxRotationAngleTotal / 2) * weight;

        if (x_values_pos.length > x_values_neg.length) {
            // turn rather left
            // todo turn left by maxRotationAngle
            Angle angle = Angle.fromDegrees(maxRotationAngle);
            rotate = new Rotate(angle);
        } else {
            // turn rather right
            // todo turn right by maxRotationAngle
            Angle angle = Angle.fromDegrees(maxRotationAngle);
            rotate = new Rotate(angle);
        }*/







        int choice = (int) (Math.random() * 10);

        // turn around, if last action was not executed. You are probably stuck at a wall.
        if (!percepts.wasLastActionExecuted()) {
           // System.out.println(percepts.getVision().getObjects());

            return randomRotate(percepts);
        }

        if (choice < 7) {
            Move move = new Move(percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard());
            return move;
        }

        if (choice < 9) {
            return randomRotate(percepts);

        }
        return new NoAction();
    }

    private Rotate randomRotate(GuardPercepts percepts){
        int maxRotationAngleDegrees = (int) Math.round(percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getDegrees());
        int turningDegrees = new Random().nextInt(maxRotationAngleDegrees) - maxRotationAngleDegrees/2;
        Angle angle = Angle.fromDegrees(turningDegrees);
        return new Rotate(angle);
    }
}
