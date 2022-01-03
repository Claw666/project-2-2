package Group1.Agent;

import java.util.Queue;
import java.util.Stack;
import java.util.LinkedList;

// Breadth First Search Class
public class Search {
    public static Stack<Node> findNearestNonCompleteArea(Node startNode){
        Queue<Node> queue = new LinkedList<Node>();
        startNode.marked = true;
        queue.offer(startNode);
        while(queue.size() != 0) {
            Node nextNode = queue.poll();
            if(nextNode.getEdges().size() < 8) {
                Stack<Node> s = new Stack<Node>();
                while(nextNode != null) {
                    s.push(nextNode);
                    nextNode = nextNode.getParent();
                }
                return s;
            }
            for(Edge edge : nextNode.getEdges()) {
                if(!edge.endNode.isMarked()) {
                    Node nextNextNode = edge.endNode;
                    if(checkNode(nextNextNode)) {
                        nextNextNode.setMarked(true);
                        nextNextNode.setParent(nextNode);
                        queue.offer(nextNextNode);
                    }
                }
            }
        }
        return null;
    }
    public static boolean checkNode(Node nextNode) {
        if((nextNode.getObjType() != ObjType.wallType) && (nextNode.getObjType() != ObjType.teleportType)) {
            for(Edge e : nextNode.getEdges()) {
                Node nextNextNode = e.getEndNode();
                if(!((nextNextNode.getObjType() != ObjType.wallType) && (nextNextNode.getObjType() != ObjType.teleportType))) {
                    return false;
                }
            }
            return true;
        }
        else {
            return false;
        }
    }

    public static Stack<Node> findPathToObjType(Node start, ObjType type){
        Queue<Node> queue = new LinkedList<Node>();
        start.marked = true;
        queue.offer(start);
        while(queue.size() != 0) {
            Node nextNode = queue.poll();
            if(nextNode.getObjType() == type) {
                Stack<Node> s = new Stack<Node>();
                while(nextNode != null) {
                    s.push(nextNode);
                    nextNode = nextNode.getParent();
                }
                return s;
            }
            for(Edge edge : nextNode.getEdges()) {
                if(!edge.endNode.isMarked()) {
                    Node nextNextNode = edge.endNode;
                    if(checkNode(nextNextNode)) {
                        nextNextNode.setMarked(true);
                        nextNextNode.setParent(nextNode);
                        queue.offer(nextNextNode);
                    }
                }
            }
        }
        return null;
    }

}
