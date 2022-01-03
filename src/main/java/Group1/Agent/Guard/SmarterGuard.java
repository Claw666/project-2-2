
package Group1.Agent.Guard;

import Interop.Action.GuardAction;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Geometry.Angle;
import Interop.Geometry.Point;
import Interop.Percept.GuardPercepts;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;
import java.util.Set;
import static java.lang.Math.abs;

public class SmarterGuard implements Interop.Agent.Guard {

    boolean intruderChasingMode = false;


    public SmarterGuard() {
        System.out.println("Smarterguard created!");
    }

/* public GuardAction getAction(GuardPercepts percepts) {
        System.out.println(percepts);

        ObjectsSeenByAgent.clear();
        ObjectsSeenByAgent.addAll(percepts.getVision().getObjects().getAll());

        if(ObjectsSeenByAgent.contains(ObjectPerceptType.Wall)) {}

        return new NoAction();

    }

    public GuardAction Rotate90minus(){
        System.out.println("Rotated 90 degrees clockwise");
        return new Rotate(Angle.fromDegrees(90));
    }

    public GuardAction Rotate90plus(   ){
        System.out.println("Rotated 90 degree anti-clockwise");
        return new Rotate(Angle.fromDegrees(270));
    }
        //todo figure out how to use filter
   *//*
     */
/* public static Predicate<ObjectPercept> isWall() {
        return p -> p.getType().equals(ObjectPerceptType.Wall);
    }*/
int counter =0;
    public GuardAction getAction(GuardPercepts percepts) {

        // todo do not delete. Try as soon as rays are done and thus objectPercepts are filled
        // First implementation of a little smartness making the agent better than random
        // Using a Principle Component Analysis approach to decide whether to turn right or left.
        // Idea: By looking at the x-axis divided by the y-axis. The more points (hindering objects)
        // on either side of the y-axis are -> choose to turn the other way
        // BUT if equally distributed, there is a wall in front, turn as far as agent can.
        Set<ObjectPercept> objectPercepts = percepts.getVision().getObjects().getAll();

        Set<ObjectPercept> objectPerceptsWithoutEmptySpace = null;
        for (ObjectPercept objectPercept : objectPercepts) {
            if (objectPercept.getType() != ObjectPerceptType.EmptySpace) {
                objectPerceptsWithoutEmptySpace.add(objectPercept);
                System.out.println("yes"+objectPerceptsWithoutEmptySpace);
            }


        }



        int numOfPoints = objectPercepts.size();
        double[] x_values = new double[numOfPoints];
        double[] y_values = new double[numOfPoints];
        int i = 0;

        double shortestDistance = Double.POSITIVE_INFINITY;
        double closestX = Double.POSITIVE_INFINITY;
        double closestY = Double.POSITIVE_INFINITY;
        Move move = new Move(percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard());


        if (!percepts.wasLastActionExecuted()) {
//            System.out.println(percepts.getVision().getObjects());

            Angle angle = Angle.fromDegrees(45+counter);
            Rotate rotate = new Rotate(angle);
//            System.out.println("turning manually");
            counter++;
            return rotate;
        }
        else if (objectPerceptsWithoutEmptySpace== null) {
            return move;
        }

        else{
            System.out.println("tes");
            for (ObjectPercept objectPercept : objectPerceptsWithoutEmptySpace) {

             //   System.out.println(objectPercept);

                if (abs(x_values[i]) + abs(y_values[i]) < shortestDistance) {
                    closestX = x_values[i];
                    closestY = y_values[i];
                }
                shortestDistance = Math.min(abs(x_values[i]) + abs(y_values[i]), shortestDistance);
                i++;

            }


            if (shortestDistance != Double.POSITIVE_INFINITY) {
                ObjectPercept closestObject = new ObjectPercept(ObjectPerceptType.Wall, new Point(closestX, closestY));
                Rotate rotate;

                // Guard turns 45 degrees away from closest wall


                // if x > 0, object is on right hand side, should turn left
                if (closestObject.getPoint().getX() > 0) {
                    rotate = new Rotate(Angle.fromDegrees(-45));
                }
                // if x <= 0 object is on right hand side, should turn right
                else {
                    rotate = new Rotate(Angle.fromDegrees(45));
                }
                return rotate;
            }
            return move;


        }
    }
}



