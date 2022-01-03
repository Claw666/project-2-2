package Group1.Agent.Sound;

import Group1.Geometry.Vector;
import Group1.States.AgentState;
import Group1.States.SoundState;
import Interop.Percept.Sound.SoundPercept;
import Interop.Percept.Sound.SoundPercepts;

import java.util.*;

public class SoundPerceptFactory {

    /**
     * Returns sounds that are perceivable by the given agent as SoundPercepts from his perspective.
     * @param agentState
     * @param soundStates
     * @return
     */
    static public SoundPercepts createSoundPercepts(AgentState agentState, List<SoundState> soundStates){

        Set<SoundPercept> perceivableSoundPercepts = new HashSet<>();

        // Selects those sounds, which are perceivable by the current agent
        // This implies only being in range of the sound.
        for(SoundState soundState : soundStates){
            if (agentState.getLocation().getDistance(soundState.getLocation()).getValue() <= soundState.getRadius()){

                // transforming soundLocation to perception from agent
                perceivableSoundPercepts.add(soundState.getSoundPerceptForAgentLocation(agentState));
            }
        }

        return new SoundPercepts(perceivableSoundPercepts);
    }
}
