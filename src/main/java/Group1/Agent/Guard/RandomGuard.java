package Group1.Agent.Guard;

import Group9.Game;
import Interop.Action.DropPheromone;
import Interop.Action.GuardAction;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Agent.Guard;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Percept.GuardPercepts;
import Interop.Percept.Smell.SmellPerceptType;

public class RandomGuard implements Guard {

    public RandomGuard() {}

    @Override
    public GuardAction getAction(GuardPercepts percepts) {

        if(false && Math.random() < 0.25)
        {
            return new DropPheromone(SmellPerceptType.values()[(int) (Math.random() * SmellPerceptType.values().length)]);
        }

        if(!percepts.wasLastActionExecuted())
        {
            return new Rotate(Angle.fromRadians(percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians() * Game._RANDOM.nextDouble()));
        }
        else
        {
            return new Move(new Distance(0.1));
        }
    }

}
