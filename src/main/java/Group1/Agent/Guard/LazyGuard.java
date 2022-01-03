package Group1.Agent.Guard;

import Interop.Action.GuardAction;
import Interop.Action.NoAction;
import Interop.Percept.GuardPercepts;

public class LazyGuard implements Interop.Agent.Guard {

    public LazyGuard() {
        System.out.println("Lazy Guard created! Will keep standing.");
    }

    @Override
    public GuardAction getAction(GuardPercepts percepts) {
        NoAction nextAction = new NoAction();
        return nextAction;
    }
}
