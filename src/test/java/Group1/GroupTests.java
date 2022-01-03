package Group1;

import Group1.Agent.Smell.SmellTest;
import Group1.Agent.Sound.SoundTest;
import Group1.Agent.Vision.RayTest;
import Group1.Geometry.*;
import Group1.Agent.Vision.RaySystemTest;
import SimpleUnitTest.SimpleUnitTest;

public class GroupTests extends SimpleUnitTest {
    public static void main(String[] args) {
        /*
         *
         * Here you can write your own tests.
         *
         * @see InteropTests for examples of simple unit tests usage.
         * @see SimpleUnitTest for details on how simple unit tests are implemented.
         *
         * Please notice, tests of all groups will be run trough GitHub Actions.
         * This will allow to verify that all code is working correctly.
         *
         * @see AllTests
         * @see .github/workflows/maven.yml
         *
         * In order for you code to be accepted into GameInterop all tests must pass!
         * You are not required to write any tests, however it is highly recommended!
         *
         */
        LineCutTest.main(args);
        ParallelogramTest.main(args);
        VectorTest.main(args);
        RayTest.main(args);
        RaySystemTest.main(args);
        SoundTest.main(args);
        SmellTest.main(args);
    }

}
