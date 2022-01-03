package Group1.Environment;

import Group1.FileReader.ScenarioObject;
import Interop.Geometry.Point;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.Arrays;

/**
 * Any area on the map being represented by its coordinates and type based upon
 * the ObjectPerceptType enum.
 */
public class Area extends ScenarioObject {

    private final Point topLeft;
    private final Point bottomRight;

    public Area(Point topLeft, Point bottomRight, ObjectPerceptType type) {
        super(type);
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    /**
     * Creates an area based upon an coordinateString from the map.txt file.
     * form: x1,y1,x2,y2,x3,y3,x4,y4 -> e.g. 20,20,40,20,20,40,40,40
     * form: x1,y1,x2,y2 -> e.g. 20,20,40,30
     * @param coordinatesString
     * @param type
     * @return Area
     */
    public static Area fromCoordinatesString(String coordinatesString, ObjectPerceptType type) {

        int[] coordinates = Arrays.stream(coordinatesString.split(",")).map(String :: trim).mapToInt(Integer::parseInt).toArray();

        // reduce detailed coordinates to top left point and bottom right point
        // e.g. x1,y1,x2,y2,x3,y3,x4,y4 -> x1,y1,x4,y4
        // also works for: x1,y1,x2,y2 -> x1,y1,x2,y2
        Point topLeft = new Point(coordinates[0], coordinates[1]);
        Point bottomRight = new Point(coordinates[coordinates.length - 2], coordinates[coordinates.length - 1]);

        // check if points really are top left and bottom right
        boolean isFirstPointAboveLeftFromSecondPoint = (topLeft.getX() < bottomRight.getX() & topLeft.getY() < bottomRight.getY());
        if (!isFirstPointAboveLeftFromSecondPoint) {
            throw new IllegalArgumentException("Given point >top left< is not located above and to the left of >bottom right<");
        }

        return new Area(topLeft, bottomRight, type);
    }

    public Point getTopLeft() {
        return topLeft;
    }

    public Point getBottomRight() {
        return bottomRight;
    }

    public boolean hasInside(Point point) {
        return false;
    }
}
