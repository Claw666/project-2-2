package Group1.Agent.Guard;

import Group1.Agent.*;
import Interop.Action.*;
import Interop.Geometry.*;
import Interop.Percept.GuardPercepts;
import Interop.Percept.Vision.*;
import java.util.*;


public class GuardTwo implements Interop.Agent.Guard {

    // Init memory
    Graph mapMemory;
    // current pos
    Node currentPos;
    // last action by agent
    Action lastAction = null;

    double viewAngle;
    double radius;
    Angle angle;

    //For planning the path
    Queue<Action> listOfActions = new LinkedList<Action>();

    Queue<Double> distCount = new LinkedList<Double>();
    boolean targetLocated = false;
    GuardAction action = new NoAction();
    double maxMoveDist;


    public enum Action {
        Right,
        Left,
        Move
    }


    @Override
    public GuardAction getAction(GuardPercepts percepts) {
        if(this.mapMemory == null || percepts.getAreaPercepts().isJustTeleported()) {
            maxMoveDist = percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue();
            viewAngle = percepts.getVision().getFieldOfView().getViewAngle().getDegrees();
            radius = Math.sqrt(Math.pow(percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue(),2)/2)/2;
            angle = Angle.fromDegrees(0);
            this.mapMemory = new Graph();
            currentPos = new Node(ObjType.noType, new Point(0, 0), radius, new Integer[] {0,0});
            mapMemory.addNode(currentPos);
            lastAction = null;
            if(percepts.wasLastActionExecuted()) {
                listOfActions.offer(Action.Move);
            }
            else {
                listOfActions.offer(Action.Right);
            }
        }
        if(distCount.size() != 0) {
            return new Move(new Distance(distCount.poll()));
        }
        if(percepts.wasLastActionExecuted()) {
            if(lastAction != null) {
                switch(lastAction) {
                    case Left:
                        this.angle = Angle.fromDegrees(calcRealAngle(this.angle.getDegrees() +45));
                        break;
                    case Right:
                        this.angle = Angle.fromDegrees(calcRealAngle(this.angle.getDegrees() -45));
                        break;
                    case Move:
                        Integer[] newPosition = Graph.getCoordinate((int)angle.getDegrees(), currentPos.getCoordinates());
                        currentPos = mapMemory.getNode(newPosition);
                        break;
                }
            }
        }
        else {
            listOfActions = new LinkedList<Action>();
            listOfActions.offer(Action.Left);
            listOfActions.offer(Action.Move);
        }
        makeNewNodesThatAreInVision(percepts.getVision());
        addObjsInVisionToNodes(percepts.getVision());

        // if intruder located, try to catch it
        if(targetLocated) {
            targetLocated = false;
            return action;
        }
        // else explore further
        else if(listOfActions.size() == 0) {
            getNextMoveOfGuard(percepts);
            if(listOfActions.size() == 0) {
                listOfActions.offer(Action.Move);
            }
            return convertActiontoCont(listOfActions.poll(), percepts);
        }
        else {
            return convertActiontoCont(listOfActions.poll(), percepts);
        }
    }
    // Change discrete to continuous action
    private GuardAction convertActiontoCont(Action action, GuardPercepts percepts) {
        switch(action) {
            case Left:
                lastAction = Action.Left;
                return new Rotate(Angle.fromDegrees(-45));
            case Right:
                lastAction = Action.Right;
                return new Rotate(Angle.fromDegrees(+45));
            case Move:
                double maxMoveDist = percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue();
                if(percepts.getAreaPercepts().isInSentryTower()) {
                    maxMoveDist = maxMoveDist * percepts.getScenarioGuardPercepts().getScenarioPercepts().getSlowDownModifiers().getInSentryTower();
                }
                else if(percepts.getAreaPercepts().isInDoor()) {
                    maxMoveDist = maxMoveDist * percepts.getScenarioGuardPercepts().getScenarioPercepts().getSlowDownModifiers().getInDoor();
                }
                else if(percepts.getAreaPercepts().isInWindow()) {
                    maxMoveDist = maxMoveDist * percepts.getScenarioGuardPercepts().getScenarioPercepts().getSlowDownModifiers().getInWindow();
                }
                else {
                    maxMoveDist = percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue();
                }
                return forward(maxMoveDist);
            default:
                return new NoAction();
        }
    }

    private void addObjsInVisionToNodes(VisionPrecepts vision) {
        Set<ObjectPercept> objectPercepts = vision.getObjects().getAll();
        for(ObjectPercept perceptedObjs : objectPercepts) {
            double addX = currentPos.getCenter().getX();
            double addY = currentPos.getCenter().getY();
            double x = - perceptedObjs.getPoint().getX();
            double y =   perceptedObjs.getPoint().getY();
            double xCalculated = x*Math.cos(angle.getRadians()) - y * Math.sin(angle.getRadians());
            double yCalculated = y*Math.cos(angle.getRadians()) + x * Math.sin(angle.getRadians());
            Point shiftPoint = new Point(xCalculated, yCalculated);
            Point calcActualPoint = new Point(shiftPoint.getX() + addX, shiftPoint.getY() + addY);

            Node inNode = mapMemory.getNode(getNodeforPoint(calcActualPoint));
            switch(perceptedObjs.getType()) {
                case Door:
                    inNode.setObjcType(ObjType.doorType);
                    break;
                case EmptySpace:
                    inNode.setObjcType(ObjType.noType);
                    break;
                case SentryTower:
                    inNode.setObjcType(ObjType.sentTowType);
                    break;
                case ShadedArea:
                    inNode.setObjcType(ObjType.shadedAreaType);
                    break;
                case Teleport:
                    inNode.setObjcType(ObjType.teleportType);
                    break;
                case Wall:
                    inNode.setObjcType(ObjType.wallType);
                    break;
                case Window:
                    inNode.setObjcType(ObjType.windowType);
                    break;
                case Intruder:
                    mapMemory = null;
                    targetLocated = true;

                    //get direction to target
                    double degree = Math.atan2(perceptedObjs.getPoint().getY(), perceptedObjs.getPoint().getX());
                    Angle rotation = Angle.fromRadians(degree - Math.toRadians(90));

                    //If more than 13 degrees to target, rotate to target otherwise move forward
                    if(Math.abs(rotation.getDegrees()) > 13) {
                        if(rotation.getDegrees() > 0) {
                            action = new Rotate(Angle.fromDegrees(rotation.getDegrees() + 5));
                        }
                        else if(rotation.getDegrees() < 0) {
                            action = new Rotate(Angle.fromDegrees(rotation.getDegrees() + 5));
                        }
                    }
                    else {
                        double distToTarget = Math.sqrt(Math.pow(perceptedObjs.getPoint().getX(),2) + Math.pow(perceptedObjs.getPoint().getY(),2));
                        // If target further than maxmoveDist, move the max amount
                        if(distToTarget > maxMoveDist) {
                            action = new Move(new Distance(maxMoveDist));
                        }
                        else {
                            action = new Move(new Distance(distToTarget));
                        }
                    }
                    return;
                default:
                    inNode.setObjcType(ObjType.unknownType);
                    break;
            }
        }
    }

    private void getNextMoveOfGuard(GuardPercepts percepts) {
        if(targetLocated) {
            mapMemory.unMark();
            Stack<Node> target = Search.findPathToObjType(currentPos, ObjType.intrType);
            mapMemory.removeIntruder();
            targetLocated = false;
            mapMemory.resetMemory();
            if(target != null) {
                generateDiscActions(target);
                return;
            }
        }

        ArrayList<Action> actionSpace = new ArrayList<Action>();
        actionSpace.add(Action.Left);
        actionSpace.add(Action.Right);
        Integer[] nextPosition = Graph.getCoordinate((int)angle.getDegrees(), currentPos.getCoordinates());
        Node nextVertice = mapMemory.getNode(nextPosition);
        if(Search.checkNode(nextVertice)) {
            actionSpace.add(Action.Move);
        }

        double actionValue = 0;
        Action selectedAction = null;
        for(Action action : actionSpace) {
            Angle angle = Angle.fromRadians(this.angle.getRadians());
            Node currentPosition = this.currentPos;
            switch(action) {
                case Left:
                    angle = Angle.fromDegrees(calcRealAngle(this.angle.getDegrees() +45));
                    break;
                case Right:
                    angle = Angle.fromDegrees(calcRealAngle(this.angle.getDegrees() -45));
                    break;
                case Move:
                    Integer[] newPosition = Graph.getCoordinate((int)angle.getDegrees(), currentPosition.getCoordinates());
                    currentPosition = mapMemory.getNode(newPosition);
                    break;
            }
            double value = numberOfNewNodesInSight(angle, currentPosition,percepts.getVision());
            if(value > actionValue) {
                actionValue = value;
                selectedAction = action;
            }
        }

        if(selectedAction == null) {
            mapMemory.unMark();
            Stack<Node> path = Search.findNearestNonCompleteArea(currentPos);
            if(path == null) {
                mapMemory.unMark();
                path = Search.findPathToObjType(currentPos, ObjType.teleportType);
            }
            generateDiscActions(path);
        }
        else {
            listOfActions.offer(selectedAction);
        }
    }

    private void generateDiscActions(Stack<Node> path) {
        if(path == null) {
            listOfActions.add(Action.Move);
            return;
        }
        Node start = path.pop();
        int curDegrees = (int)angle.getDegrees();
        while(path.size() != 0) {
            Node end = path.pop();
            for(Edge e : start.getEdges()) {
                if(e.getEndNode() == end) {
                    int degrees = e.getDegrees();
                    double add;
                    if(curDegrees > 180) {
                        add = 360 - curDegrees;
                    }
                    else {
                        add = -curDegrees;
                    }
                    while(Math.abs(curDegrees - degrees)>2) {
                        if(calcRealAngle(degrees + add) < 180) {
                            listOfActions.add(Action.Left);
                            curDegrees = (int)calcRealAngle(curDegrees +45);
                        }
                        else {
                            listOfActions.add(Action.Right);
                            curDegrees = (int)calcRealAngle(curDegrees -45);
                        }
                    }
                    break;
                }
            }
            start = end;
            listOfActions.add(Action.Move);
        }
    }

    private int numberOfNewNodesInSight(Angle angle, Node currentPosition, VisionPrecepts percepts) {
        int count = 0;
        int samples = 10;
        double viewRange = percepts.getFieldOfView().getRange().getValue();
        double step = viewRange / samples;
        if(step > radius) {
            samples = (int)(viewRange / radius);
            step = radius;
        }
        double currentAngle = (viewAngle+4)/2;
        while(currentAngle >= -viewAngle/2) {
            double finalAngle = 0;
            if(angle.getDegrees() > 180) finalAngle = calcRealAngle(currentAngle + (angle.getDegrees()-360) + 90);
            else finalAngle = calcRealAngle(currentAngle + angle.getDegrees() + 90);

            Angle pAngle = Angle.fromDegrees(finalAngle);
            for(int i=1; i < samples+2; i++) {
                double x=i*step*Math.cos(pAngle.getRadians());
                double y=i*step*Math.sin(pAngle.getRadians());
                x += currentPosition.getCenter().getX();
                y += currentPosition.getCenter().getY();
                Integer[] position = getNodeforPoint(new Point(x,y));
                if(!mapMemory.nodeExists(position)) {
                    count ++;
                }
            }
            currentAngle = currentAngle -1;
        }
        return count;
    }

    private Move forward(double maxDistance) {
        this.lastAction = Action.Move;
        double distance;

        if(this.angle.getDegrees() % 90 == 0) {
            distance = 2*radius;
        }
        else {
            distance = Math.sqrt(2*Math.pow(2*radius, 2));
        }
        while(distance > maxDistance) {
            distCount.offer(maxDistance);
            distance = distance - maxDistance;
        }
        return new Move(new Distance(distance));
    }

    private void makeNewNodesThatAreInVision(VisionPrecepts percepts) {
        int samples = 10;
        double viewRange = percepts.getFieldOfView().getRange().getValue();
        double step = viewRange / samples;
        if(step > radius) {
            samples = (int)(viewRange / radius);
            step = radius;
        }
        double currentAngle = (viewAngle+4)/2;
        while(currentAngle >= -viewAngle/2) {
            double finalAngle = 0;
            if(angle.getDegrees() > 180) finalAngle = calcRealAngle(currentAngle + (angle.getDegrees()-360) + 90);
            else finalAngle = calcRealAngle(currentAngle + angle.getDegrees() + 90);

            Angle pAngle = Angle.fromDegrees(finalAngle);
            for(int i=1; i < samples+2; i++) {
                double x=i*step*Math.cos(pAngle.getRadians());
                double y=i*step*Math.sin(pAngle.getRadians());
                x += currentPos.getCenter().getX();
                y += currentPos.getCenter().getY();
                Integer[] position = getNodeforPoint(new Point(x,y));
                if(!mapMemory.nodeExists(position)) {
                    mapMemory.addNode(new Node(ObjType.noType, new Point(2*radius*position[0], 2*radius*position[1]), radius, position));
                }
            }
            if(radius < 0.05) {
                currentAngle = currentAngle -.1;
            }
            else {
                currentAngle = currentAngle -1;
            }
        }
    }

    private double calcRealAngle(double angle) {
        if(angle < 0) {
            angle = 360 + angle;
        }
        else if(angle > 360) {
            angle = 0 + (angle - 360);
        }
        else if(angle == 360) {
            angle = 0;
        }
        return angle;
    }

    private Integer[] getNodeforPoint(Point point) {
        double x = point.getX();
        double y = point.getY();
        int xNode = 0;
        int yNode = 0;
        if(!(Math.abs(x)<=radius)) {
            if(x<0) {
                xNode--;
                x = x + radius;
            }
            else {
                xNode++;
                x = x - radius;
            }
            while(Math.abs(x) >= 2*radius) {
                if(x<0) {
                    xNode--;
                    x = x + 2*radius;
                }
                else {
                    xNode++;
                    x = x - 2*radius;
                }
            }
        }
        if(!(Math.abs(y)<=radius)) {
            if(y<0) {
                yNode--;
                y = y + radius;
            }
            else {
                yNode++;
                y = y - radius;
            }
            while(Math.abs(y) >= 2*radius) {
                if(y<0) {
                    yNode--;
                    y = y + 2*radius;
                }
                else {
                    yNode++;
                    y = y - 2*radius;
                }
            }
        }
        return new Integer[]{xNode, yNode};

    }

}
