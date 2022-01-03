package Group1.Agent.Smell;

import Group1.Agent.Guard.SimpleGuard;
import Group1.States.GuardState;
import Group1.States.PheromoneState;
import Interop.Geometry.Direction;
import Interop.Geometry.Point;
import Interop.Percept.Smell.SmellPercept;
import Interop.Percept.Smell.SmellPerceptType;
import Interop.Percept.Smell.SmellPercepts;
import SimpleUnitTest.SimpleUnitTest;

import java.util.ArrayList;
import java.util.List;

public class SmellTest extends SimpleUnitTest {

    public static void main(String[] args) {
        System.out.println("\n\n\nSmell Tests\n");
        System.out.println("generating smell percepts:");
        percepts();
        System.out.println("generating smell percepts by type:");
        perceptTypes();

    }

    static void percepts() {
        GuardState guardState = new GuardState(
                new SimpleGuard(),
                new Point(0, 0),
                Direction.fromDegrees(0),
                5
        );



        it("returns one percept with correct attributes - case: agent in distance of 1", () -> {
            guardState.setLocation(new Point(1,1));
            List<PheromoneState> pheromoneStates = new ArrayList<>();
            pheromoneStates.add(new PheromoneState(SmellPerceptType.Pheromone1, new Point(2,1), 5));

            SmellPercepts smellPercepts = SmellPerceptFactory.createSmellPercepts(guardState, pheromoneStates);

            assertEqual(smellPercepts.getAll().size(), 1, 0, "No percepts provided, can't validate.");
            for (SmellPercept smellPercept : smellPercepts.getAll()){
                assertEqual(smellPercept.getDistance().getValue(), 1, 0);
            }
        });

        it("returns one percept with correct attributes - case: agent in distance of 5", () -> {
            guardState.setLocation(new Point(1,1));
            List<PheromoneState> pheromoneStates = new ArrayList<>();
            pheromoneStates.add(new PheromoneState(SmellPerceptType.Pheromone1, new Point(1,6), 5));

            SmellPercepts smellPercepts = SmellPerceptFactory.createSmellPercepts(guardState, pheromoneStates);

            assertEqual(smellPercepts.getAll().size(), 1, 0, "No percepts provided, can't validate.");
            for (SmellPercept smellPercept : smellPercepts.getAll()){
                assertEqual(smellPercept.getDistance().getValue(), 5, 0);
            }
        });



    }



    static void perceptTypes() {
        GuardState guardState = new GuardState(
                new SimpleGuard(),
                new Point(0, 0),
                Direction.fromDegrees(0),
                5
        );

        it("returns one percept with correct type pheromone 1", () -> {
            guardState.setLocation(new Point(1,1));
            List<PheromoneState> pheromoneStates = new ArrayList<>();
            pheromoneStates.add(new PheromoneState(SmellPerceptType.Pheromone1, new Point(2,1), 5));

            SmellPercepts smellPercepts = SmellPerceptFactory.createSmellPercepts(guardState, pheromoneStates);

            assertEqual(smellPercepts.getAll().size(), 1, 0, "No percepts provided, can't validate.");
            for (SmellPercept smellPercept : smellPercepts.getAll()){
                assertTrue(smellPercept.getType() == SmellPerceptType.Pheromone1, "should be pheromone1");
            }
        });

    }

}
