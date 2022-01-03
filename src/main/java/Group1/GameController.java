package Group1;


import Group1.Agent.Smell.SmellPerceptFactory;
import Group1.Agent.Sound.SoundPerceptFactory;
import Group1.Agent.Vision.RaySystem;
import Group1.Environment.Area;
import Group1.FileReader.Scenario;
import Group1.FileReader.ScenarioObjects;
import Group1.GUI.ViewController;
import Group1.Geometry.Parallelogram;
import Group1.Geometry.Precision;
import Group1.States.GuardState;
import Group1.States.IntruderState;
import Group1.States.PheromoneStates;
import Group1.States.SoundStates;
import Interop.Action.Action;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Agent.Guard;
import Interop.Geometry.Angle;
import Interop.Geometry.Direction;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.AreaPercepts;
import Interop.Percept.GuardPercepts;
import Interop.Percept.Scenario.ScenarioGuardPercepts;
import Interop.Percept.Scenario.ScenarioPercepts;
import Interop.Percept.Scenario.SlowDownModifiers;
import Interop.Percept.Smell.SmellPerceptType;
import Interop.Percept.Smell.SmellPercepts;
import Interop.Percept.Sound.SoundPercepts;
import Interop.Percept.Vision.FieldOfView;
import Interop.Percept.Vision.VisionPrecepts;

import java.util.ArrayList;
import java.util.List;

public class GameController{
    private Scenario scenario;
    private ScenarioGuardPercepts scenarioGuardPercepts;
    private List<GuardState> guardStates = new ArrayList<>();
    private List<IntruderState> intruderStates = new ArrayList<>();
    private ViewController viewController;
    private ScenarioObjects scenarioObjects;
    private SoundStates soundStates;
    private PheromoneStates guardPheromoneStates;
    private PheromoneStates intruderPheromoneStates;

    /*
    -- initialize game with map (read from file)
    -- initialize agents using AgentsFactory
      |-- for every agent: create percepts based on random position on map (?) - take care of walls and objects
    -- start gui based upon map and agent's position
    -- run game in loop by calling:
      |-- for every agent: agent.getAction(percepts)
      |-- check if legit action
      |-- execute actions
    */

    public GameController(String mapFilePath) {
        // Read map using the path to file (WHICH SHOULD BE INDEPENDENT FROM OS)
        this.scenario = ReadMapFile(mapFilePath);

        // initial setup
        SetupScenario();
        SetupSounds();
        SetupPheromones();
        SetupAgents();
        SetupGui();

        // start looping to run the simulation
        Start();
    }

    /**
     * Triggers to read the config map file.
     *
     * @param mapFilePath
     * @return
     */
    private Scenario ReadMapFile(String mapFilePath) {
        System.out.println("Reading map and scenario from file path: " + mapFilePath);
        return new Scenario(mapFilePath);
    }

    /**
     * Uses the config map file to create the scenarioGuardPercepts which hold
     * general configuration for guards.
     */
    private void SetupScenario() {
        // collecting single values to generate scenarioPercepts object
        Distance captureDistance = new Distance(this.scenario.getCaptureDistance());
        Angle maxRotationAngle = Angle.fromDegrees(this.scenario.getMaxRotationAngle());
        SlowDownModifiers slowDownModifiers = new SlowDownModifiers(
                this.scenario.getSlowDownModifierWindow(),
                this.scenario.getSlowDownModifierDoor(),
                this.scenario.getSlowDownModifierSentryTower());
        Distance radiusPheromone = new Distance(this.scenario.getRadiusPheromone());

        // collection values to generate scenarioGuardPercepts
        ScenarioPercepts scenarioPercepts = new ScenarioPercepts(
                this.scenario.getGameMode(),
                captureDistance,
                maxRotationAngle,
                slowDownModifiers,
                radiusPheromone,
                this.scenario.getPheromoneCooldown());
        Distance maxMoveDistanceGuard = new Distance(this.scenario.getMaxMoveDistanceGuard());

        // scenario
        this.scenarioGuardPercepts = new ScenarioGuardPercepts(
                scenarioPercepts,
                maxMoveDistanceGuard);

        List<Area> areas = new ArrayList<>();

        areas.addAll(scenario.getWalls());
        areas.addAll(scenario.getDoors());
        areas.addAll(scenario.getSentrys());
        areas.addAll(scenario.getWindows());
        areas.addAll(scenario.getShadedAreas());

        this.scenarioObjects = new ScenarioObjects(areas, guardStates, intruderStates);

    }

    private void SetupSounds() {
        // there are three different move speeds
        double maxMoveDistance = Math.max( scenario.getMaxMoveDistanceGuard(),
                Math.max(scenario.getMaxMoveDistanceIntruder(),
                        scenario.getMaxSprintDistanceIntruder()));
        this.soundStates = new SoundStates(
                scenario.getYellSoundRadius(),
                scenario.getDoorSoundRadius(),
                scenario.getWindowSoundRadius(),
                scenario.getMaxMoveSoundRadius(),
                maxMoveDistance
        );
    }

    private void SetupPheromones() {

        this.guardPheromoneStates = new PheromoneStates(
                scenario.getRadiusPheromone(),
                scenario.getPheromoneCooldown(),
                scenario.getPheromoneExpireRounds()
        );

        this.intruderPheromoneStates = new PheromoneStates(
                scenario.getRadiusPheromone(),
                scenario.getPheromoneCooldown(),
                scenario.getPheromoneExpireRounds()
        );
    }

    /**
     * Generates agents using the AgentFactory and distributes them randomly within the
     * agent spawn area.
     */
    private void SetupAgents() {
        // For the first period we will only need one type of agent -> exploring guy
        List<Guard> guards = AgentsFactory.createGuards(this.scenario.getNumGuards());


        for (Guard guard : guards) {
            Point location = SpawnAgentAtRandomLocation(this.scenario.getSpawnAreaGuards());
            Direction viewAngle = Direction.fromDegrees((int) (Math.random() * 360));

            // TODO: just assign?
            // according to guideslines agents have a radius of 0.5
            // https://docs.google.com/document/d/19VPAmK5cSvoFUul6Nyc6Vu3cOv1WtjqAiKKtceaNk_4/edit#heading=h.gl6l78ba0hol
            double guardRadius = 0.5;

            GuardState guardState = new GuardState(guard, location, viewAngle, guardRadius);
            guardStates.add(guardState);
        }
    }

    /**
     * Generating random position within spawn area.
     * This is an own method, so that we can use it for guards and intruders.
     *
     * @param spawnArea
     * @return
     */
    private Point SpawnAgentAtRandomLocation(Area spawnArea) {
        double spawnAreaWidth = spawnArea.getBottomRight().getX() - spawnArea.getTopLeft().getX();
        double spawnAreaHeight = spawnArea.getBottomRight().getY() - spawnArea.getTopLeft().getY();

        double x = spawnArea.getTopLeft().getX() + Math.random() * spawnAreaWidth;
        double y = spawnArea.getTopLeft().getY() + Math.random() * spawnAreaHeight;

        return new Point(x, y);
    }


    private void SetupGui() {
        viewController = new ViewController(this.scenario, this.guardStates);
    }

    /**
     * Containing the game loop, this method takes care of running the whole simulation.
     */
    private void Start() {
        // todo later on: while true with exit condition

//        for (int turn = 0; turn <= 20; turn++) {
        int turn = 0;
        while (turn < 10000) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                System.out.println("interrupted!");
            }

            // todo iterate intruderState

            for (GuardState guardState : guardStates) {
                //System.out.println("Guard: " + guardState + " at " + guardState.getLocation() + " looking " + guardState.getViewDirection().getDegrees());
                // update guard
                UpdateGuard(guardState);
                // todo update world for next agent to now the updated world -> see guidelines
                // update guardStates
                // todo update gui
//                 updateGUI(scenario, guardState)
            }

            // clean sounds
            // todo how long are sounds perceivable? This way the last guard is never heard.
            soundStates.resetSoundStates();

            // todo clean pheromones
            guardPheromoneStates.validatePheromoneStateAfterRound();
            intruderPheromoneStates.validatePheromoneStateAfterRound();

            turn++;
            viewController.updateState();
        }
    }


    /**
     * Triggers agents to update their position by asking them for their next move
     * and verifying if this will be a valid move.
     *
     * @param guardState
     */
    private void UpdateGuard(GuardState guardState) {
        // gather decision of guard for next action
        GuardPercepts guardPercepts = GenerateGuardPercepts(guardState);
        Action action = guardState.getGuard().getAction(guardPercepts);

//        System.out.println("-- action -> " + action);

        // todo check for validity of next action


        // todo perform update of guard position within GameController
        // somehow like this:
        switch (action.getClass().getSimpleName()) {
            // if the action is move
            case "Move":

                // ######################## long part 1 ########################
                // get the next position of the guard if he moves with speed 2
                // created just for the debugging purposes, delete when done with percepts.
                double speed = 2;
                double nextX = Math.cos(guardState.getViewDirection().getRadians()) * speed;
                double nextY = Math.sin(guardState.getViewDirection().getRadians()) * speed;
                Point nextP = new Point(nextX, nextY);
                /*
                TODO:
                    check what tf is going on.
                    the bottom print statement should print true, it is the next position(speed * direction)
                    of the guard, hence guard should see it, but sometimes it does not.
                 */
                // print the next position and whether the guard sees it
                //System.out.println("from (0,0): " + nextP + " " + guardPercepts.getVision().getFieldOfView().isInView(nextP));
//                    System.out.println("dir: " + guardState.getViewAngle().getDegrees());
//                    System.out.println("clock dir: " + nextP.getClockDirection().getDegrees());


                // ######################## long part 2 ########################
                // check if move is valid
                // check for the collision. Either using percepts to avoid it completely
                // without a need to redo the action
                // or with collision detection as stated in the guidelines

                /*
                    for action validity

                        for guard collision:

                            check for circle(guard with radius r) collision with rectangle(wall)

                            g.x - r < w.xR
                            ^
                            g.x + r > w.xL
                            ^
                            g.y + r > w.yT \
                            ^               (for y direction is down)
                            g.y - r < w.yB /

                        for path collision:

                            check for rectangle(path that circle draws) collision with rectangle(wall)

                            p.xL < w.xR
                            ^
                            p.xR > w.xL
                            ^
                            p.yB > w.yT \
                            ^            (for y direction is down)
                            p.yT < w.yB /

                 */
                boolean canDo = true;
                double newX = guardState.getLocation().getX() + ((Move) action).getDistance().getValue() * Math.cos(guardState.getViewDirection().getRadians());
                double newY = guardState.getLocation().getY() + ((Move) action).getDistance().getValue() * Math.sin(guardState.getViewDirection().getRadians());
                Point newLocation = new Point(newX, newY);
                for (Area wall : this.scenario.getWalls()) {
                    // todo filter for wall next to agent. canDo will get overwritten by ok wall

                    // guard
                    double r = guardState.getRadius();

                    // wall
                    double wxR = wall.getBottomRight().getX();
                    double wxL = wall.getTopLeft().getX();
                    double wyT = wall.getTopLeft().getY();
                    double wyB = wall.getBottomRight().getY();

                    //path
                    //Parallelogram path = new Parallelogram()


                    /*
                    TODO: add check for path collision
                        right now checks only for guard collision, explained above.
                         + tests(!)
                     */

                    if (((newX - r <= wxR) &&
                            (newX + r >= wxL) &&
                            (newY + r >= wyT) &&
                            (newY - r <= wyB))
                    ) {
                        canDo = false;
                    }

                    Parallelogram areaParallelogram = new Parallelogram(wall.getTopLeft(), wall.getBottomRight());
                    if (areaParallelogram.isIntersecting(guardState.getPath(newLocation))) canDo = false;

                }



                // ######################## long part 3 ########################
                //assign the next position to guard
                if (canDo) {
                    // sound at previous location
                    soundStates.addMoveNoise(guardState.getLocation(), guardState.getLocation().getDistance(newLocation));
                    guardState.setLocation(newLocation);
                    guardState.setWasLastActionExecuted(true);
                }else{
                    guardState.setWasLastActionExecuted(false);
                }
                break;

            case "Rotate":
                double newDirectionRadians = guardState.getViewDirection().getRadians() + ((Rotate) action).getAngle().getRadians();
                while (newDirectionRadians < 0 || newDirectionRadians > 2*Math.PI) {
                    // to prevent precision issues, provided the newDirectionRadians is Math.PI * 2 (happens sometimes)
                    if (Math.abs(newDirectionRadians - Math.PI * 2) < Precision.THRESHOLD ) {
                        newDirectionRadians = Math.PI * 2;
                    }
                    if (newDirectionRadians < 0) {
                        newDirectionRadians += Math.PI * 2 - Precision.THRESHOLD;
                    }
                    if (newDirectionRadians > 2*Math.PI) {
                        newDirectionRadians -= Math.PI * 2 + Precision.THRESHOLD;
                    }
                }

                try {
                    Direction newDirection = Direction.fromRadians(newDirectionRadians);
                    guardState.setViewDirection(newDirection);
                    guardState.setWasLastActionExecuted(true);
                } catch (Exception e) {
                    // precision error at angle Math.PI * 2 happens
                    guardState.setWasLastActionExecuted(false);
                }

                break;

            case "Yell":
                // example of usage
                soundStates.addYell(guardState.getLocation());
                guardState.setWasLastActionExecuted(true);

            case "DropPheromone":
                // example of usage
                if (guardPheromoneStates.dropPheromone(SmellPerceptType.Pheromone1, guardState.getLocation())){
                    guardState.setWasLastActionExecuted(true);
                } else{
                    guardState.setWasLastActionExecuted(false);
                }

        }


    }

    /**
     * Constructing percepts for agents to decide upon.
     *
     * @param guardState
     * @return
     */
    private GuardPercepts GenerateGuardPercepts(GuardState guardState) {

        // todo smartly put following blocks of percepts into own methods, if necessary
        // vision
        // initiate distance the guard can go in one turn
        // 7.5 for debugging purposes, should be read from the map file
        Distance distance = new Distance(7.5);
        // initiate angle of view(sector) to 45 degrees
        // 45 for debugging purposes, should be read from the map file
        Angle viewAngle = Angle.fromDegrees(45);

        FieldOfView fieldOfView = new FieldOfView(distance,viewAngle);

        VisionPrecepts vision = new VisionPrecepts(
                fieldOfView,
                new RaySystem(
                        guardState,
                        fieldOfView,
                        scenario.getViewRays()
                )
                        .getPercepts(scenarioObjects)
                        .toInterop()
        );

        // sound
        SoundPercepts sounds = SoundPerceptFactory.createSoundPercepts(guardState, soundStates.getAll());

        // smell
        SmellPercepts smells = SmellPerceptFactory.createSmellPercepts(guardState, guardPheromoneStates.getAll());

        // area
        boolean inWindow = false;
        boolean inDoor = false;
        boolean inSentryTower = false;
        boolean justTeleported = false;
        AreaPercepts areaPercepts = new AreaPercepts(inWindow, inDoor, inSentryTower, justTeleported);

        return new GuardPercepts(
                vision,
                sounds,
                smells,
                areaPercepts,
                this.scenarioGuardPercepts,
                guardState.wasLastActionExecuted());

    }

    public Scenario getScenario() {
        return scenario;
    }

    public List<GuardState> getGuardStates() {
        return guardStates;
    }

    public List<IntruderState> getIntruderStates() {
        return intruderStates;
    }

    public ScenarioObjects getScenarioObjects() {
        return scenarioObjects;
    }

}
