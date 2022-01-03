package Group1.Geometry;

import Interop.Geometry.Point;
import SimpleUnitTest.SimpleUnitTest;

public class VectorTest extends SimpleUnitTest {

    public static void main(String[] args) {
        System.out.println("\n\n\nVector Tests\n");
        transformation();
    }

    static void transformation() {
        System.out.println("Vector transformation:");
        it("returns correct transformed vectors", () -> {
            Vector v1 = new Vector(new Point(50,50));
            Vector v2 = new Vector(new Point(60,40));

            Vector v2_tranformed = v2.vectorTo(v1);
            Vector v1_transformed = v1.from(v2);
            Vector v_result = new Vector(new Point(-10, 10));
            
            assertTrue(v2_tranformed.equals(v1_transformed));
            assertTrue(v_result.equals(v1_transformed));

        });
    }
}
