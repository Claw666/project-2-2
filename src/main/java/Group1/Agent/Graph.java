package Group1.Agent;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Graph {



    HashMap<String, Node> Nodes;


    /* Contstructor */

    public Graph() {
        this.Nodes = new HashMap<String, Node>();
    }

    /* Getters*/

    public Node getNode(Integer[] position) {
        String nodeKey = position[0] + " " + position[1];
        return Nodes.get(nodeKey);
    }

    /* Setters */

    public void addNode(Node node) {
        // Check for new node in all direction
        for (int i = 0; i <= 315; i += 45) {
            Integer[] currentPos = getCoordinate(i, node.coordinates);
            if (nodeExists(currentPos)) {
                node.addEdge(getNode(currentPos), i);
            }

        }

        Integer[] currentPos = node.getCoordinates();
        String nodeKey = currentPos[0] + " " + currentPos[1];
        this.Nodes.put(nodeKey, node);
    }

    public boolean nodeExists(Integer[] coordinates) {
        String nodeKey = coordinates[0] + " " + coordinates[1];
        return Nodes.containsKey(nodeKey);
    }

    public static Integer[] getCoordinate(int degrees, Integer[] currentPos) {
        int x = currentPos[0];
        int y = currentPos[1];

        switch (degrees) {
            case 0:
                return new Integer[]{x, y + 1};
            case 45:
                return new Integer[]{x - 1, y + 1};
            case 90:
                return new Integer[]{x - 1, y};
            case 135:
                return new Integer[]{x - 1, y - 1};
            case 180:
                return new Integer[]{x, y - 1};
            case 225:
                return new Integer[]{x + 1, y - 1};
            case 270:
                return new Integer[]{x + 1, y};
            case 315:
                return new Integer[]{x + 1, y + 1};
            default:
                return null;
        }
    }

    /* unmark all nodes */
    public void unMark() {
        for (String nodeKey : Nodes.keySet()) {
            Node node = Nodes.get(nodeKey);
            node.setMarked(false);
            node.setParent(null);
        }
    }

    /* reset the graph */
    public void resetMemory() {
        for (String nodeKey : Nodes.keySet()) {
            Nodes.get(nodeKey).setObjcType(ObjType.noType);
        }
    }

    /* remove if it is intruder */
    public void removeIntruder() {
        for (String nodeKey : Nodes.keySet()) {
            if (Nodes.get(nodeKey).getObjType() == ObjType.intrType) {
                Nodes.get(nodeKey).setObjcType(ObjType.noType);
            }
        }
    }
}
