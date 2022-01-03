package Group1.Agent.Vision;

import Group1.Environment.Area;
import Group1.FileReader.ScenarioObject;
import Group1.FileReader.ScenarioObjects;
import Group1.Geometry.LineCut;
import Group1.Geometry.Parallelogram;
import Interop.Geometry.Point;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.*;

/**
 * According to the GameInterop and the Guidelines, we are going to use rays in order to make
 * the agents perceive vision.
 *
 */
public class Ray {

    private Point agentCoords;
    private LineCut ray;

    public Ray(Point start, Point end) {
        this.agentCoords = start;
        this.ray = new LineCut(agentCoords, end);
    }

    public Point getAgentCoords() {
        return agentCoords;
    }

    public LineCut getCut() {
        return ray;
    }

    /**
     * TODO: complete
     * supposed to return all the percepts
     * @param scenarioObjects with all the areas and agent states
     *
     * @return set of percepts
     */
    public ObjectPercepts getPercepts(ScenarioObjects scenarioObjects) {

        Set<ObjectPercept> percepts = new HashSet<>();
        for (ScenarioObject scenarioObject : scenarioObjects.getAll()) {
            // todo check if conditions are correct
            if (scenarioObject.getType().isOpaque() || scenarioObject.getType().isSolid() || scenarioObject.getType().isOpaqueFromOutside()) {

                Parallelogram areaParallelogram = new Parallelogram((Area) scenarioObject);
                List<Point> points = areaParallelogram.getIntersections(ray);
                if (points.isEmpty()) {
                    continue;
                }
                for (Point p : points) {
                    percepts.add(new ObjectPercept(scenarioObject.getType(), p));
                }
            }
        }

        // todo handle rays no intersecting with areas: a) return empty collection or this:
        /*
        if (percepts.isEmpty()) {
            percepts.add(
                    new ObjectPercept(
                            ObjectPerceptType.EmptySpace,
                            ray.getEnd()
                    )
            );
        }
        */

        return new ObjectPercepts(percepts).visibleFrom(agentCoords);
    }
}
