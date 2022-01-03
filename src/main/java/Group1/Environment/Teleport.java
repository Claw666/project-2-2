package Group1.Environment;

import Interop.Geometry.Point;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.Arrays;

public class Teleport {

    private final Area entryArea;
    private final Area exitArea;

    public Teleport(Area entryArea, Area exitArea) {
        this.entryArea = entryArea;
        this.exitArea = exitArea;
    }

    public static Teleport fromCoordinatesString(String coordinatesString, ObjectPerceptType type) {

        int[] coordinates = Arrays.stream(coordinatesString.split(",")).map(String :: trim).mapToInt(Integer::parseInt).toArray();

        // reduce detailed coordinates to top left point and bottom right point
        // e.g. x1,y1,x2,y2,x3,y3,x4,y4 -> x1,y1,x4,y4
        // also works for: x1,y1,x2,y2 -> x1,y1,x2,y2
        Point topLeft = new Point(coordinates[0], coordinates[1]);
        Point bottomRight = new Point(coordinates[coordinates.length - 2], coordinates[coordinates.length - 1]);

        // check if points really are top left and bottom right
        boolean isFirstPointAboveLeftFromSecondPoint = (topLeft.getX() < bottomRight.getX() & topLeft.getY() < bottomRight.getY());
        if (!isFirstPointAboveLeftFromSecondPoint) {
            throw new IllegalArgumentException("Given points are not >top left< and >bottom right<");
        }

        Area entry = new Area(topLeft, bottomRight, type);
        Area exit = new Area(topLeft, bottomRight, type);
        return new Teleport(entry, exit);
    }

    public Area getEntryArea() {
        return entryArea;
    }

    public Area getExitArea() {
        return exitArea;
    }
}
