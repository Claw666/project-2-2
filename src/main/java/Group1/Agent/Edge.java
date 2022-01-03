package Group1.Agent;

public class Edge {
    Node startNode;
    Node endNode;
    int degrees;

    /* Constructor - directed edge */

    public Edge (Node nodeOne, Node nodeTwo, int degrees) {
        this.startNode = nodeOne;
        this.endNode = nodeTwo;
        this.degrees = degrees;
    }

    /* getters */
    public Node getStartNode() {
        return startNode;
    }

    public Node getEndNode() {
        return endNode;
    }

    public int getDegrees() {
        return degrees;
    }

    /* Setters */
    public void setStartNode(Node newStartNode) {
        this.startNode = newStartNode;
    }

    public void setEndNode(Node newEndNode) {
        this.endNode = newEndNode;
    }

    public  void setDegrees(int newDegrees) {
        this.degrees = newDegrees;
    }
}
