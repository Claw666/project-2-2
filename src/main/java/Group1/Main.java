package Group1;

import java.io.File;

public class Main {

    public static void main(String[] args) {

        // ++++++++++++++++++++++++++++

        // Set path to map file here:
        String mapFilePath = "Project2-2/map.txt";

        // ++++++++++++++++++++++++++++

        // only run simulation, if above file exists.
        File f = new File(mapFilePath);
        if(f.exists() && !f.isDirectory()) {
            GameController gameController = new GameController(mapFilePath);
        } else{
            System.out.println("Map file not found: " + mapFilePath);
            System.exit(1);
        }
    }
}