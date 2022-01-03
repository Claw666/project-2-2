package Group9.agent.factories;

import Group1.Agent.Guard.GuardTwo;
import Group1.AgentsGroup01.Guards.MazeGuard;
import Group1.AgentsGroup01.Intruders.TargetIntruder;
import Interop.Agent.Guard;
import Interop.Agent.Intruder;


import java.util.ArrayList;
import java.util.List;

/**
 * This class provides common way to build agents for the competition.
 *
 * Sharing knowledge between agents is NOT ALLOWED.
 *
 * For example:
 * Agents must not hold ANY references to common objects or references to each other.
 */
public class DefaultAgentFactory implements IAgentFactory {

    public List<Intruder> createIntruders(int number) {
        List<Intruder> intruders = new ArrayList<>();
        for(int i = 0; i < number; i++)
        {
            intruders.add(new Group1.Agent.Intruder());
        }
        return intruders;
    }

    public List<Guard> createGuards(int number) {
        List<Guard> guards = new ArrayList<>();
        for(int i = 0; i < number; i++)
        {
            guards.add(new Group1.Agent.Guard.GuardTwo());
        }
        return guards;
    }
}