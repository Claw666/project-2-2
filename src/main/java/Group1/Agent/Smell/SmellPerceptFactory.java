package Group1.Agent.Smell;

import Group1.States.AgentDropItemState;
import Group1.States.AgentState;
import Group1.States.PheromoneState;
import Group1.States.SoundState;
import Interop.Percept.Smell.SmellPercept;
import Interop.Percept.Smell.SmellPercepts;
import Interop.Percept.Sound.SoundPercept;
import Interop.Percept.Sound.SoundPercepts;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SmellPerceptFactory {

    /**
     * Returns smells that are perceivable by the given agent as SmellPercepts from his perspective.
     * @param agentState
     * @param pheromoneStates
     * @return
     */
    static public SmellPercepts createSmellPercepts(AgentState agentState, List<PheromoneState> pheromoneStates){

        Set<SmellPercept> perceivableSmellPercepts = new HashSet<>();

        // Selects those sounds, which are perceivable by the current agent
        // This implies only being in range of the sound.
        for(PheromoneState pheromoneState : pheromoneStates){
            if (agentState.getLocation().getDistance(pheromoneState.getLocation()).getValue() <= pheromoneState.getRadius()){

                // transforming soundLocation to perception from agent
                perceivableSmellPercepts.add(pheromoneState.getSmellPerceptForAgentLocation(agentState));
            }
        }

        return new SmellPercepts(perceivableSmellPercepts);
    }
}
