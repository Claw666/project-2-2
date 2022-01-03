package Group1.States;

import Group1.FileReader.Scenario;
import Group1.GUI.Util;
import Group1.Geometry.Vector;
import Interop.Geometry.Angle;
import Interop.Geometry.Direction;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.Sound.SoundPercept;
import Interop.Percept.Sound.SoundPerceptType;
import Interop.Percept.Vision.ObjectPerceptType;
import Interop.Utils.Utils;

public class SoundState extends AgentDropItemState {

    private SoundPerceptType type;

    /**
     * Holds information regarding a sound made on the map.
     * A sound is represented as a point without direction and by a radius in which is is perceivable.
     * Moreover, a sound comes in two types: noise, yell.
     *
     * @param type
     * @param location
     * @param radius
     */
    public SoundState(SoundPerceptType type, Point location, double radius) {
        super(location, radius);
        this.type = type;
    }


    /**
     * A sound is heard by everyone, no matter if it's a guard or an intruder.
     * @param agentState
     * @return
     */
    public SoundPercept getSoundPerceptForAgentLocation(AgentState agentState) {

        // clockAngle of soundLocation in terms of agent's coordinate system.
        // Remember, it's the angle clockwise from 0.
        Direction soundDirectionFromOrigin = Direction.fromClockAngle(
                new Vector(this.getLocation())
                        .from(agentState.getLocation())
                        .toPoint()
        );

        // Get ClockAngle from agents ViewDirections
        // Angle grows clockwise from agent's ViewDirection.
        double signedDistance = Utils.getSignedDistanceBetweenAngles(
                agentState.getViewDirection().getRadians(),
                soundDirectionFromOrigin.getRadians());

        if (signedDistance < 0) {
            signedDistance = Utils.TAU + signedDistance;
        }

        return new SoundPercept(
                this.type,
                Direction.fromRadians(signedDistance));
    }


}
