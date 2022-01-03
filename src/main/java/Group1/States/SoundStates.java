package Group1.States;

import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.Sound.SoundPercept;
import Interop.Percept.Sound.SoundPerceptType;
import Interop.Percept.Sound.SoundPercepts;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SoundStates {

    private List<SoundState> soundStates = new ArrayList<>();

    private double yellRadius;
    private double doorRadius;
    private double windowRadius;
    private double maxMoveRadius;
    private double maxMoveDistance;

    public SoundStates(double yellRadius, double doorRadius, double windowRadius, double maxMoveRadius, double maxMoveDistance){
        this.yellRadius = yellRadius;
        this.doorRadius = doorRadius;
        this.windowRadius = windowRadius;
        this.maxMoveRadius = maxMoveRadius;
        this.maxMoveDistance = maxMoveDistance;

    }

    public void addYell(Point location){
        soundStates.add(new SoundState(SoundPerceptType.Yell, location, this.yellRadius));
    }

    public void addDoorNoise(Point location){
        soundStates.add(new SoundState(SoundPerceptType.Noise, location, this.doorRadius));
    }

    public void addWindowNoise(Point location){
        soundStates.add(new SoundState(SoundPerceptType.Noise, location, this.windowRadius));
    }

    public void addMoveNoise(Point location, Distance moveDistance){
        double radius = (moveDistance.getValue() / this.maxMoveDistance) * this.maxMoveRadius;
        soundStates.add(new SoundState(SoundPerceptType.Noise, location, radius));
    }

    /**
     * Sound states are only valid for
     *
     * My current idea would work, but is too complicated, still:
     * Save sounds incl. who produced it.
     * Let the sound be heard by everyone in this remaining round and by those not have heard them yet in the next round
     * As soon as it’s the agent’s turn, who produced the sound, this sound will be removed.
     */
    public void resetSoundStates() {
        this.soundStates.clear();
    }

    public List<SoundState> getAll() {
        return soundStates;
    }

}
