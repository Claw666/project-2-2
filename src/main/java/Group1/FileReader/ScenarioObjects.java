package Group1.FileReader;

import Group1.Environment.Area;
import Group1.States.GuardState;
import Group1.States.IntruderState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScenarioObjects {

    private List<GuardState> guardStates;
    private List<IntruderState> intruderStates;
    private List<Area> areas = new ArrayList<>();
    private List<ScenarioObject> scenarioObjects = new ArrayList<>();

    public ScenarioObjects(List<Area> areas, List<GuardState> guardStates, List<IntruderState> intruderStates) {
        this.guardStates = guardStates;
        this.intruderStates = intruderStates;
        this.areas = areas;

        this.scenarioObjects.addAll(areas);
        this.scenarioObjects.addAll(intruderStates);
        this.scenarioObjects.addAll(guardStates);

    }

    public ScenarioObjects(ScenarioObject ...scenarioObjects) {
        this.scenarioObjects.addAll(Arrays.asList(scenarioObjects));
    }

    public ScenarioObjects(List<ScenarioObject> scenarioObjects) {
        this.scenarioObjects.addAll(scenarioObjects);
    }


    public List<Area> getAreas() {
        return areas;
    }

    public List<GuardState> getGuardStates() {
        return guardStates;
    }

    public List<IntruderState> getIntruderStates() {
        return intruderStates;
    }

    public List<ScenarioObject> getAll() {
        return scenarioObjects;
    }
}
