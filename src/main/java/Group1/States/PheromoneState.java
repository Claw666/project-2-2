package Group1.States;

import Group1.Geometry.Vector;
import Interop.Geometry.Direction;
import Interop.Geometry.Point;
import Interop.Percept.Smell.SmellPercept;
import Interop.Percept.Smell.SmellPerceptType;
import Interop.Percept.Sound.SoundPercept;
import Interop.Percept.Sound.SoundPerceptType;
import Interop.Utils.Utils;

public class PheromoneState extends AgentDropItemState {

    private SmellPerceptType type;

    /**
     * Holds information regarding a sound made on the map.
     * A sound is represented as a point without direction and by a radius in which is is perceivable.
     * Moreover, a sound comes in two types: noise, yell.
     *
     * @param type
     * @param location
     * @param radius
     */
    public PheromoneState(SmellPerceptType type, Point location, double radius) {
        super(location, radius);
        this.type = type;
    }


    /**
     * A pheromone is perceived only by distance.
     * @param agentState
     * @return
     */
    public SmellPercept getSmellPerceptForAgentLocation(AgentState agentState) {

        return new SmellPercept(
                this.type,
                this.getLocation().getDistance(agentState.getLocation()));
    }


}
