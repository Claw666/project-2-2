package Group1.Agent.Vision;

import Group1.Environment.Area;
import Group1.FileReader.ScenarioObject;
import Group1.FileReader.ScenarioObjects;
import Group1.Geometry.LineCut;
import Interop.Geometry.Point;
import Interop.Percept.Vision.ObjectPerceptType;
import SimpleUnitTest.SimpleUnitTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class RayTest extends SimpleUnitTest {

    public static void main(String[] args) {
        System.out.println("\n\n\nRay Tests");
        System.out.println("Ray…when someone asks you if you are a god, you say yes! (Ghostbusters)\n");
        System.out.println("Checking percepts");
        percepts();
    }

    static void percepts() {
        it("returns no percepts at all, because no area given", () -> {
            Ray ray = new Ray(new Point(1,1), new Point(7,1));

            ScenarioObjects scenarioObjects = new ScenarioObjects(Collections.emptyList());
            ObjectPercepts objectPercepts = ray.getPercepts(scenarioObjects);
            assertTrue(objectPercepts.getAll().isEmpty(), "percepts should be empty but contains: "+ objectPercepts);
        });

        it("returns one percept", () -> {
            Ray ray = new Ray(new Point(1,1), new Point(7,1));

            List<ScenarioObject> scenarioObject_list = new ArrayList<>();
            scenarioObject_list.add(new Area(new Point(1.1,4), new Point(3,0), ObjectPerceptType.Wall));
            ScenarioObjects scenarioObjects = new ScenarioObjects(scenarioObject_list);
            ObjectPercepts objectPercepts = ray.getPercepts(scenarioObjects);

            assertTrue(!objectPercepts.getAll().isEmpty(), "percepts should not be empty");
        });

        it("returns one percept with certain Type", () -> {
            Ray ray = new Ray(new Point(1,1), new Point(7,1));

            List<ScenarioObject> scenarioObject_list = new ArrayList<>();
            scenarioObject_list.add(new Area(new Point(2,4), new Point(3,0), ObjectPerceptType.Wall));
            ScenarioObjects scenarioObjects = new ScenarioObjects(scenarioObject_list);
            ObjectPercepts objectPercepts = ray.getPercepts(scenarioObjects);

            assertTrue(!objectPercepts.getAll().isEmpty(), "cannot access percept, as it does not exist");

            for(ObjectPercept objectPercept : objectPercepts.getAll()){
                assertTrue(objectPercept.getType() == ObjectPerceptType.Wall);
            }
        });

        it("returns one percept with certain coordinate", () -> {
            Ray ray = new Ray(new Point(1,1), new Point(7,1));

            List<ScenarioObject> scenarioObject_list = new ArrayList<>();
            scenarioObject_list.add(new Area(new Point(2,4), new Point(3,0), ObjectPerceptType.Wall));
            ScenarioObjects scenarioObjects = new ScenarioObjects(scenarioObject_list);
            ObjectPercepts objectPercepts = ray.getPercepts(scenarioObjects);

            assertTrue(!objectPercepts.getAll().isEmpty(), "cannot access percept, as it does not exist");

            for(ObjectPercept objectPercept : objectPercepts.getAll()){
                assertEqual(objectPercept.getPoint().getX(), 2, 0);
                assertEqual(objectPercept.getPoint().getY(), 1, 0);
            }
        });

        it("returns one percept also when ray passes two walls", () -> {
            Ray ray = new Ray(new Point(1,1), new Point(7,1));

            List<ScenarioObject> scenarioObject_list = new ArrayList<>();
            scenarioObject_list.add(new Area(new Point(2,4), new Point(3,0), ObjectPerceptType.Wall));
            scenarioObject_list.add(new Area(new Point(4,4), new Point(5,0), ObjectPerceptType.Wall));
            ScenarioObjects scenarioObjects = new ScenarioObjects(scenarioObject_list);
            ObjectPercepts objectPercepts = ray.getPercepts(scenarioObjects);

            assertTrue(!objectPercepts.getAll().isEmpty(), "cannot access percept, as it does not exist");
            // returns only one percept, because other ones are not visible
            assertEqual(objectPercepts.getAll().size(), 1, 0);

            for(ObjectPercept objectPercept : objectPercepts.getAll()){
                assertEqual(objectPercept.getPoint().getX(), 2, 0);
                assertEqual(objectPercept.getPoint().getY(), 1, 0);
            }
        });

        // todo, this works with wall, but not with door. why not?
        it("returns one percept with certain Type: Door", () -> {
            Ray ray = new Ray(new Point(1,1), new Point(7,1));

            List<ScenarioObject> scenarioObject_list = new ArrayList<>();
            scenarioObject_list.add(new Area(new Point(2,4), new Point(3,0), ObjectPerceptType.Door));
            ScenarioObjects scenarioObjects = new ScenarioObjects(scenarioObject_list);
            ObjectPercepts objectPercepts = ray.getPercepts(scenarioObjects);

            assertTrue(!objectPercepts.getAll().isEmpty(), "cannot access percept, as it does not exist");
            // returns only one percept, because other ones are not visible
            assertEqual(objectPercepts.getAll().size(), 1, 0);

            for(ObjectPercept objectPercept : objectPercepts.getAll()){
                assertTrue(objectPercept.getType() == ObjectPerceptType.Door);
                assertEqual(objectPercept.getPoint().getX(), 2, 0);
                assertEqual(objectPercept.getPoint().getY(), 1, 0);
            }
        });
    }
}