package Group1.FileReader;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Group1.Environment.Area;
import Group1.Environment.Teleport;
import Interop.Percept.Scenario.GameMode;
import Interop.Percept.Vision.ObjectPerceptType;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

// todo test and prevent overlapping of objects
// todo test for variables being null

/**
 *
 * @author joel
 * please note: The code was further adapted by the group.
 */
public class Scenario  {

    protected String mapDoc;
    private final Path filePath;
    private final static Charset ENCODING = StandardCharsets.UTF_8;

    protected GameMode gameMode;
    protected int height;
    protected int width;
    protected int numGuards;
    protected int numIntruders;
    protected double captureDistance;
    protected int winConditionIntruderRounds;
    protected double maxRotationAngle;
    protected double maxMoveDistanceIntruder;
    protected double maxSprintDistanceIntruder;
    protected double maxMoveDistanceGuard;
    protected int sprintCooldown;
    protected int pheromoneCooldown;
    protected int pheromoneExpireRounds;
    protected double radiusPheromone;
    protected double slowDownModifierWindow;
    protected double slowDownModifierDoor;
    protected double slowDownModifierSentryTower;
    protected double viewAngle;
    protected int viewRays;
    protected double viewRangeIntruderNomal;
    protected double viewRangeIntruderShaded;
    protected double viewRangeGuardNomal;
    protected double viewRangeGuardShaded;
    protected double[] viewRangeSentry = new double[]{2}; // not visible short range; visible high range
    protected double yellSoundRadius;
    protected double maxMoveSoundRadius;
    protected double windowSoundRadius;
    protected double doorSoundRadius;
    protected Area targetArea;
    protected Area spawnAreaIntruders;
    protected Area spawnAreaGuards;
    protected ArrayList<Area> walls = new ArrayList<>();
    protected ArrayList<Teleport> teleports = new ArrayList<>();
    protected ArrayList<Area> shadedAreas = new ArrayList<>();
    protected ArrayList<Area> doors = new ArrayList<>();
    protected ArrayList<Area> windows = new ArrayList<>();
    protected ArrayList<Area> sentrys = new ArrayList<>();

    public Scenario(String mapFile){
        // set parameters
        mapDoc=mapFile;

        // read scenario
        filePath = Paths.get(mapDoc); // get path
        readMap();
    }

    public void readMap(){
        try (Scanner scanner = new Scanner(filePath, ENCODING.name())){
            while (scanner.hasNextLine()){
                parseLine(scanner.nextLine());
            }
        }
        catch(Exception e)
        {
            System.out.println("Some unexpected error occurred while parsing the map config file: " + filePath);
            System.exit(1);
        }
    }


    protected void parseLine(String line){
        //use a second Scanner to parse the content of each line
        try(Scanner scanner = new Scanner(line)){
            scanner.useDelimiter("=");
            if (scanner.hasNext()){
                // read id value pair
                String id = scanner.next();
                String value = scanner.next();
                // trim excess spaces
                value=value.trim();
                id=id.trim();
                try {
                switch(id)
                {
                    case "gameMode":
                        // Setting gameMode according to the victory conditions ignoring all other values than 0.
                        // So gameMode is either set to 0 and if not that it's interpreted as 1.
                        if (Integer.parseInt(value) == 0) {
                            gameMode = GameMode.CaptureAllIntruders;
                        } else {
                            gameMode = GameMode.CaptureOneIntruder;
                        }
                        break;
                    case "height":
                        height = Integer.parseInt(value);
                        break;
                    case "width":
                        width = Integer.parseInt(value);
                        break;
                    case "numGuards":
                        numGuards = Integer.parseInt(value);
                        break;
                    case "numIntruders":
                        numIntruders = Integer.parseInt(value);
                        break;
                    case "captureDistance":
                        captureDistance = Double.parseDouble(value);
                        break;
                    case "winConditionIntruderRounds":
                        winConditionIntruderRounds = Integer.parseInt(value);
                        break;
                    case "maxRotationAngle":
                        maxRotationAngle = Double.parseDouble(value);
                        break;
                    case "maxMoveDistanceIntruder":
                        maxMoveDistanceIntruder = Double.parseDouble(value);
                        break;
                    case "maxSprintDistanceIntruder":
                        maxSprintDistanceIntruder = Double.parseDouble(value);
                        break;
                    case "maxMoveDistanceGuard":
                        maxMoveDistanceGuard = Double.parseDouble(value);
                        break;
                    case "sprintCooldown":
                        sprintCooldown = Integer.parseInt(value);
                        break;
                    case "pheromoneCooldown":
                        pheromoneCooldown = Integer.parseInt(value);
                        break;
                    case "pheromoneExpireRounds":
                        pheromoneExpireRounds = Integer.parseInt(value);
                        break;
                    case "radiusPheromone":
                        radiusPheromone = Double.parseDouble(value);
                        break;
                    case "slowDownModifierWindow":
                        slowDownModifierWindow = Double.parseDouble(value);
                        break;
                    case "slowDownModifierDoor":
                        slowDownModifierDoor = Double.parseDouble(value);
                        break;
                    case "slowDownModifierSentryTower":
                        slowDownModifierSentryTower = Double.parseDouble(value);
                        break;
                    case "viewAngle":
                        viewAngle = Double.parseDouble(value);
                        break;
                    case "viewRays":
                        viewRays = Integer.parseInt(value);
                        break;
                    case "viewRangeIntruderNomal":
                        viewRangeIntruderNomal = Double.parseDouble(value);
                        break;
                    case "viewRangeIntruderShaded":
                        viewRangeIntruderShaded = Double.parseDouble(value);
                        break;
                    case "viewRangeGuardNomal":
                        viewRangeGuardNomal = Double.parseDouble(value);
                        break;
                    case "viewRangeGuardShaded":
                        viewRangeGuardShaded = Double.parseDouble(value);
                        break;
                    case "viewRangeSentry":
                        viewRangeSentry = Arrays.stream(value.split(",")).map(String :: trim).mapToDouble(Double::parseDouble).toArray();
                        break;
                    case "yellSoundRadius":
                        yellSoundRadius = Double.parseDouble(value);
                        break;
                    case "maxMoveSoundRadius":
                        maxMoveSoundRadius = Double.parseDouble(value);
                        break;
                    case "windowSoundRadius":
                        windowSoundRadius = Double.parseDouble(value);
                        break;
                    case "doorSoundRadius":
                        doorSoundRadius = Double.parseDouble(value);
                        break;
                    case "targetArea":
                        targetArea = Area.fromCoordinatesString(value, ObjectPerceptType.TargetArea);
                        break;
                    case "spawnAreaIntruders":
                        spawnAreaIntruders = Area.fromCoordinatesString(value, ObjectPerceptType.EmptySpace);
                        break;
                    case "spawnAreaGuards":
                        spawnAreaGuards = Area.fromCoordinatesString(value, ObjectPerceptType.EmptySpace);
                        break;
                    case "wall":
                        Area wall = Area.fromCoordinatesString(value, ObjectPerceptType.Wall);
                        walls.add(wall);
                        break;
                    case "teleport":
                        // todo improve class Teleport by inherit from Area
                        Teleport teleport = Teleport.fromCoordinatesString(value, ObjectPerceptType.Teleport);
                        teleports.add(teleport);
                        break;
                    case "shaded":
                        Area shaded = Area.fromCoordinatesString(value, ObjectPerceptType.ShadedArea);
                        shadedAreas.add(shaded);
                        break;
                    case "door":
                        Area door = Area.fromCoordinatesString(value, ObjectPerceptType.Door);
                        doors.add(door);
                        break;
                    case "window":
                        Area window = Area.fromCoordinatesString(value, ObjectPerceptType.Window);
                        windows.add(window);
                        break;
                    case "sentry":
                        Area sentry = Area.fromCoordinatesString(value, ObjectPerceptType.SentryTower);
                        sentrys.add(sentry);
                        break;
                    }
                }
                catch (NumberFormatException nfe) {
                    System.out.println(nfe.toString() + " -- for config id: " + id + ". Value: " + value);
                    System.exit(1);
                } catch (IllegalArgumentException iae) {
                    System.out.println(iae.getMessage() + " -- for config id: " + id + ". Value: " + value);
                    System.exit(1);
                }
            }
        }catch (Exception e) {
            throw e;
        }
    }

    public String getMapDoc() {
        return mapDoc;
    }

    public Path getFilePath() {
        return filePath;
    }

    public static Charset getENCODING() {
        return ENCODING;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getNumGuards() {
        return numGuards;
    }

    public int getNumIntruders() {
        return numIntruders;
    }

    public double getCaptureDistance() {
        return captureDistance;
    }

    public int getWinConditionIntruderRounds() {
        return winConditionIntruderRounds;
    }

    public double getMaxRotationAngle() {
        return maxRotationAngle;
    }

    public double getMaxMoveDistanceIntruder() {
        return maxMoveDistanceIntruder;
    }

    public double getMaxSprintDistanceIntruder() {
        return maxSprintDistanceIntruder;
    }

    public double getMaxMoveDistanceGuard() {
        return maxMoveDistanceGuard;
    }

    public int getSprintCooldown() {
        return sprintCooldown;
    }

    public int getPheromoneCooldown() {
        return pheromoneCooldown;
    }

    public int getPheromoneExpireRounds() {
        return pheromoneExpireRounds;
    }

    public double getRadiusPheromone() {
        return radiusPheromone;
    }

    public double getSlowDownModifierWindow() {
        return slowDownModifierWindow;
    }

    public double getSlowDownModifierDoor() {
        return slowDownModifierDoor;
    }

    public double getSlowDownModifierSentryTower() {
        return slowDownModifierSentryTower;
    }

    public double getViewAngle() {
        return viewAngle;
    }

    public int getViewRays() {
        return viewRays;
    }

    public double getViewRangeIntruderNomal() {
        return viewRangeIntruderNomal;
    }

    public double getViewRangeIntruderShaded() {
        return viewRangeIntruderShaded;
    }

    public double getViewRangeGuardNomal() {
        return viewRangeGuardNomal;
    }

    public double getViewRangeGuardShaded() {
        return viewRangeGuardShaded;
    }

    public double[] getViewRangeSentry() {
        return viewRangeSentry;
    }

    public double getYellSoundRadius() {
        return yellSoundRadius;
    }

    public double getMaxMoveSoundRadius() {
        return maxMoveSoundRadius;
    }

    public double getWindowSoundRadius() {
        return windowSoundRadius;
    }

    public double getDoorSoundRadius() {
        return doorSoundRadius;
    }

    public Area getTargetArea() {
        return targetArea;
    }

    public Area getSpawnAreaIntruders() {
        return spawnAreaIntruders;
    }

    public Area getSpawnAreaGuards() {
        return spawnAreaGuards;
    }

    public ArrayList<Area> getWalls() {
        return walls;
    }

    public ArrayList<Teleport> getTeleports() {
        return teleports;
    }

    public ArrayList<Area> getShadedAreas() {
        return shadedAreas;
    }

    public ArrayList<Area> getDoors() {
        return doors;
    }

    public ArrayList<Area> getWindows() {
        return windows;
    }

    public ArrayList<Area> getSentrys() {
        return sentrys;
    }
}