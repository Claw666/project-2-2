package Group1.States;

import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.Smell.SmellPercept;
import Interop.Percept.Smell.SmellPerceptType;
import Interop.Percept.Sound.SoundPerceptType;

import java.util.*;

public class PheromoneStates {

    //private List<PheromoneState> pheromoneStates = new ArrayList<>();
    private HashMap<PheromoneState, Integer> pheromoneStates = new HashMap<PheromoneState, Integer>();
    private EnumMap<SmellPerceptType, Integer> remainingCoolDownRounds = new EnumMap<SmellPerceptType,Integer>(SmellPerceptType.class);

    private double radius;
    private int coolDownRounds;
    private int pheromoneExpireRounds;


    public PheromoneStates(double radius, int coolDownRounds, int pheromoneExpireRounds){
        this.radius = radius;
        this.coolDownRounds = coolDownRounds;
        this.pheromoneExpireRounds = pheromoneExpireRounds;

        for (SmellPerceptType type : SmellPerceptType.values()){
            this.remainingCoolDownRounds.put(type, 0);
        }

    }

    /**
     * Drops a pheromone.
     * @param type
     * @param location
     * @return
     */
    public boolean dropPheromone(SmellPerceptType type, Point location){

        if (isPheromoneDroppable(type)) {

            this.pheromoneStates.put(new PheromoneState(type, location, this.radius), this.pheromoneExpireRounds);
            this.remainingCoolDownRounds.put(type, this.coolDownRounds);
            return true;
        }

        return false;
    }


    public void validatePheromoneStateAfterRound(){
        validateCoolDownAfterRound();
        validatePheromoneExpireAfterRound();
    }

    /**
     * Reduce remaining coolDown counter by 1.
     */
    private void validateCoolDownAfterRound(){
        if (remainingCoolDownRounds.values().stream().mapToInt(Integer::intValue).sum() > 0) {

            for (Map.Entry<SmellPerceptType, Integer> entry : remainingCoolDownRounds.entrySet()) {
                if (entry.getValue() > 0) {
                    remainingCoolDownRounds.put(entry.getKey(), entry.getValue() - 1);
                }
            }
        }
    }

    /**
     * Remove pheromones if expired.
     */
    private void validatePheromoneExpireAfterRound(){
        for (Map.Entry<PheromoneState, Integer> entry : pheromoneStates.entrySet()) {
            if(entry.getValue() <= 1) {
                pheromoneStates.remove(entry.getKey());
            } else {
                pheromoneStates.put(entry.getKey(), entry.getValue() - 1);
            }
        }
    }

    /**
     * Validates if another pheromone can be dropped by checking the remaining coolDown rounds.
     * @param type
     * @return
     */
    private boolean isPheromoneDroppable(SmellPerceptType type){
        return !(remainingCoolDownRounds.get(type) > 0);
    }

    public List<PheromoneState> getAll() {
        return new ArrayList<>(pheromoneStates.keySet());
    }
}
