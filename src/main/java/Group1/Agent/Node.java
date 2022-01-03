package Group1.Agent;
import java.util.ArrayList;
import Interop.Geometry.Point;

public class Node {
    ObjType typeOfObject;
    ArrayList<Edge> edges;
    Point center;
    double radius;
    Integer coordinates[];
    boolean marked = false;
    Node parentNode = null;

    /* Constructor */

    public Node(ObjType type, Point center, double radius, Integer[] coordinates) {
        this.center = center;
        this.radius = radius;
        this.typeOfObject = type;
        this.edges = new ArrayList<Edge>();
        this.coordinates = coordinates;
    }

    /* Getter functions*/

    public ObjType getObjType() {
        return typeOfObject;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public Point getCenter() {
        return center;
    }

    public Integer[] getCoordinates() {
        return coordinates;
    }

    public Node getParent() {
        return parentNode;
    }

    public boolean isMarked() {
        return marked;
    }


    /* Setter functions */
    public  void setObjcType(ObjType typeOfObject) {
        this.typeOfObject = typeOfObject;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public void setParent(Node parentNode) {
        this.parentNode = parentNode;
    }

    public void addEdge(Node endNode, int degrees) {
        Edge edge = new Edge(this, endNode, degrees);
        this.edges.add(edge);
        if(degrees >= 180) {
            degrees = degrees - 180;
        }
        else {
            degrees = degrees + 180;
        }
        Edge backEdge = new Edge(endNode, this, degrees);
        endNode.edges.add(backEdge);
    }



}


