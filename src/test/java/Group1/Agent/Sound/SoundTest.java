package Group1.Agent.Sound;

import Group1.Agent.Guard.SimpleGuard;
import Group1.States.GuardState;
import Group1.States.SoundState;
import Interop.Geometry.Direction;
import Interop.Geometry.Point;
import Interop.Percept.Sound.SoundPercept;
import Interop.Percept.Sound.SoundPerceptType;
import Interop.Percept.Sound.SoundPercepts;
import SimpleUnitTest.SimpleUnitTest;

import java.util.ArrayList;
import java.util.List;

public class SoundTest extends SimpleUnitTest {

    public static void main(String[] args) {
        System.out.println("\n\n\nSound Tests\n");
        System.out.println("generating sound percepts:");
        percepts();
        System.out.println("generating sound percepts by type:");
        perceptTypes();

    }

    static void percepts() {
        GuardState guardState = new GuardState(
                new SimpleGuard(),
                new Point(0, 0),
                Direction.fromDegrees(0),
                5
        );

        it("returns no percept - case: no sounds", () -> {
            guardState.setLocation(new Point(1,1));
            List<SoundState> soundStates = new ArrayList<>();

            SoundPercepts soundPercepts = SoundPerceptFactory.createSoundPercepts(guardState, soundStates);
            assertTrue(soundPercepts.getAll().isEmpty());
        });

        it("returns no percept - case: agent not in range", () -> {
            guardState.setLocation(new Point(1,1));
            List<SoundState> soundStates = new ArrayList<>();
            soundStates.add(new SoundState(SoundPerceptType.Noise, new Point(10,10), 1));

            SoundPercepts soundPercepts = SoundPerceptFactory.createSoundPercepts(guardState, soundStates);
            assertTrue(soundPercepts.getAll().isEmpty());
        });

        it("returns one percept - case: agent in range", () -> {
            guardState.setLocation(new Point(1,1));
            List<SoundState> soundStates = new ArrayList<>();
            soundStates.add(new SoundState(SoundPerceptType.Noise, new Point(2,2), 5));

            SoundPercepts soundPercepts = SoundPerceptFactory.createSoundPercepts(guardState, soundStates);
            assertEqual(soundPercepts.getAll().size(), 1, 0);
        });

        it("returns one percept with correct attributes - case: agent in range at angle of 90 degrees", () -> {
            guardState.setLocation(new Point(1,1));
            guardState.setViewDirection(Direction.fromDegrees(0));
            List<SoundState> soundStates = new ArrayList<>();
            soundStates.add(new SoundState(SoundPerceptType.Noise, new Point(2,1), 5));

            SoundPercepts soundPercepts = SoundPerceptFactory.createSoundPercepts(guardState, soundStates);
            assertEqual(soundPercepts.getAll().size(), 1, 0, "No percepts provided, can't validate.");
            for (SoundPercept soundPercept : soundPercepts.getAll()){
                assertTrue(soundPercept.getType() == SoundPerceptType.Noise, "should be noise");
                assertTrue(soundPercept.getDirection().getDegrees() == 90, "should be 90 degrees");
            }
        });

        it("returns one percept with correct attributes - case: agent in range at angle of 270 degrees", () -> {
            guardState.setLocation(new Point(1,1));
            guardState.setViewDirection(Direction.fromDegrees(0));
            List<SoundState> soundStates = new ArrayList<>();
            soundStates.add(new SoundState(SoundPerceptType.Noise, new Point(0,1), 5));

            SoundPercepts soundPercepts = SoundPerceptFactory.createSoundPercepts(guardState, soundStates);
            assertEqual(soundPercepts.getAll().size(), 1, 0, "No percepts provided, can't validate.");
            for (SoundPercept soundPercept : soundPercepts.getAll()){
                assertTrue(soundPercept.getDirection().getDegrees() == 270, "should be 270 degrees");
            }
        });

        it("returns one percept with correct attributes - case: agent in range at angle of 0 degrees", () -> {
            guardState.setLocation(new Point(1,1));
            guardState.setViewDirection(Direction.fromDegrees(90));
            List<SoundState> soundStates = new ArrayList<>();
            soundStates.add(new SoundState(SoundPerceptType.Noise, new Point(2,1), 5));

            SoundPercepts soundPercepts = SoundPerceptFactory.createSoundPercepts(guardState, soundStates);
            assertEqual(soundPercepts.getAll().size(), 1, 0, "No percepts provided, can't validate.");
            for (SoundPercept soundPercept : soundPercepts.getAll()){
                assertTrue(soundPercept.getDirection().getDegrees() == 0, "should be 0 degrees");
            }
        });

        it("returns one percept with correct attributes - case: agent in range at angle of 180 degrees", () -> {
            guardState.setLocation(new Point(1,1));
            guardState.setViewDirection(Direction.fromDegrees(90));
            List<SoundState> soundStates = new ArrayList<>();
            soundStates.add(new SoundState(SoundPerceptType.Noise, new Point(0,1), 5));

            SoundPercepts soundPercepts = SoundPerceptFactory.createSoundPercepts(guardState, soundStates);
            assertEqual(soundPercepts.getAll().size(), 1, 0, "No percepts provided, can't validate.");
            for (SoundPercept soundPercept : soundPercepts.getAll()){
                assertTrue(soundPercept.getDirection().getDegrees() == 180, "should be 90 degrees");
            }
        });

        it("returns one percept with correct attributes - case: agent in range at angle of 330 degrees", () -> {
            guardState.setLocation(new Point(1,1));
            guardState.setViewDirection(Direction.fromDegrees(300));
            List<SoundState> soundStates = new ArrayList<>();
            soundStates.add(new SoundState(SoundPerceptType.Noise, new Point(0,1), 5));

            SoundPercepts soundPercepts = SoundPerceptFactory.createSoundPercepts(guardState, soundStates);
            assertEqual(soundPercepts.getAll().size(), 1, 0, "No percepts provided, can't validate.");
            for (SoundPercept soundPercept : soundPercepts.getAll()){
                assertTrue(soundPercept.getType() == SoundPerceptType.Noise, "should be noise");
                assertTrue(soundPercept.getDirection().getDegrees() == 330, "should be 330 degrees");
            }
        });




        it("returns two percepts - case: agent in range of both", () -> {
            guardState.setLocation(new Point(1,1));
            List<SoundState> soundStates = new ArrayList<>();
            soundStates.add(new SoundState(SoundPerceptType.Noise, new Point(1,2), 5));
            soundStates.add(new SoundState(SoundPerceptType.Noise, new Point(2,1), 5));

            SoundPercepts soundPercepts = SoundPerceptFactory.createSoundPercepts(guardState, soundStates);
            assertEqual(soundPercepts.getAll().size(), 2, 0);
        });


    }



    static void perceptTypes() {
        GuardState guardState = new GuardState(
                new SimpleGuard(),
                new Point(0, 0),
                Direction.fromDegrees(0),
                5
        );

        it("returns one percept with correct type NOISE", () -> {
            guardState.setLocation(new Point(1,1));
            List<SoundState> soundStates = new ArrayList<>();
            soundStates.add(new SoundState(SoundPerceptType.Noise, new Point(2,1), 5));

            SoundPercepts soundPercepts = SoundPerceptFactory.createSoundPercepts(guardState, soundStates);
            assertEqual(soundPercepts.getAll().size(), 1, 0, "No percepts provided, can't validate.");
            for (SoundPercept soundPercept : soundPercepts.getAll()){
                assertTrue(soundPercept.getType() == SoundPerceptType.Noise, "should be noise");
            }
        });

        it("returns one percept with correct type YELL", () -> {
            guardState.setLocation(new Point(1,1));
            List<SoundState> soundStates = new ArrayList<>();
            soundStates.add(new SoundState(SoundPerceptType.Yell, new Point(2,1), 5));

            SoundPercepts soundPercepts = SoundPerceptFactory.createSoundPercepts(guardState, soundStates);
            assertEqual(soundPercepts.getAll().size(), 1, 0, "No percepts provided, can't validate.");
            for (SoundPercept soundPercept : soundPercepts.getAll()){
                assertTrue(soundPercept.getType() == SoundPerceptType.Yell, "should be yell");
            }
        });
    }

}
