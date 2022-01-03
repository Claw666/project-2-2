package Group1.Agent.Vision;

import Group1.Environment.Area;
import Group1.FileReader.Scenario;
import Group1.FileReader.ScenarioObject;
import Group1.FileReader.ScenarioObjects;
import Group1.Geometry.LineCut;
import Group1.Geometry.Parallelogram;
import Group1.Geometry.Vector;
import Group1.States.AgentState;
import Interop.Percept.Vision.FieldOfView;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RaySystem {

    private AgentState agentState;
    private List<Ray> rays = new ArrayList<>();
    private FieldOfView fieldOfView;
    private int numOfRays;

    public RaySystem(AgentState agentState, FieldOfView fieldOfView, int numOfRays) {
        this.agentState = agentState;
        this.fieldOfView = fieldOfView;
        this.numOfRays = numOfRays;

        setRays();
    }

    public void setRays() {
        double range = fieldOfView.getRange().getValue();

        Vector directionVector = new Vector(
                Math.cos(agentState.getViewDirection().getRadians()),
                Math.sin(agentState.getViewDirection().getRadians())
        ).scale(range);

        double halfAngle = fieldOfView.getViewAngle().getRadians() / 2;
        double radiansBetweenRays = fieldOfView.getViewAngle().getRadians() / numOfRays;


        for (int i = 0; i < numOfRays; i++) {
            this.rays.add(new Ray(
                            agentState.getLocation(),
                            directionVector
                                    .rotate(-halfAngle + i * radiansBetweenRays)
                                    .add(agentState.getLocation())
                                    .toPoint()
                    )
            );
        }
    }

    public List<Ray> getRays() {
        return rays;
    }

    public List<LineCut> toCuts() {
        List<LineCut> cuts = new ArrayList<>();
        for (Ray ray : rays) {
            cuts.add(ray.getCut());
        }
        return cuts;
    }

    public ObjectPercepts getPercepts(ScenarioObjects scenarioObjects) {
        List<ScenarioObject> visibleAreas = new ArrayList<>();
        for (ScenarioObject scenarioObject : scenarioObjects.getAll()) {
            if (
                    scenarioObject.getType() != ObjectPerceptType.Guard &&
                    scenarioObject.getType() != ObjectPerceptType.Intruder
            ) {
                Parallelogram areaParallelogram = new Parallelogram((Area) scenarioObject);
                if (areaParallelogram
                        .isInRange(
                                agentState.getLocation(),
                                fieldOfView.getRange()
                        )
                ) {
                    visibleAreas.add(scenarioObject);
                }
            }
        }

        Set<ObjectPercept> percepts = new HashSet<>();

        for (Ray ray : rays) {
            percepts.addAll(ray.getPercepts(new ScenarioObjects(visibleAreas)).getAll());
        }

        return new ObjectPercepts(percepts)
                .from(agentState.getLocation())
                .getInFieldOfView(fieldOfView);
    }
}
