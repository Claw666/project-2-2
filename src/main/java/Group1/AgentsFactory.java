package Group1;

import Group1.Agent.Guard.GuardTwo;
import Group1.Agent.Guard.SimpleGuard;
import Group1.Agent.Guard.SmarterGuard;
import Interop.Agent.Guard;
import Interop.Agent.Intruder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class provides common way to build agents for the competition.
 *
 * Sharing knowledge between agents is NOT ALLOWED.
 *
 * For example:
 * Agents must not hold ANY references to common objects or references to each other.
 */
public class AgentsFactory {
    static public List<Intruder> createIntruders(int number) {
        return Collections.emptyList();
    }

    static public List<Guard> createGuards(int number) {
        List<Guard> guardsList = new ArrayList<>();
        if (number <= 0) {
            return guardsList;
        }

        for (int i = 0; i < Math.floor(number/2); i++) {
            Guard guard = new GuardTwo();
            guardsList.add(guard);
        }


        for (int i = 0; i < Math.ceil(number/2); i++) {
            Guard simpleGuard = new SimpleGuard();
            guardsList.add(simpleGuard);
        }
        return guardsList;
    }
}