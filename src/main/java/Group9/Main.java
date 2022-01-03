package Group9;

import Group9.agent.factories.DefaultAgentFactory;
import Group9.map.GameMap;
import Group9.map.parser.Parser;


public class Main {

    public static void main(String[] args) {

        /*
        remark for running this class: Parameters in use:
        args[0] relative path to map file location
        args[1] maximum number of ticks to simulate
        args[2] number of guards to experiment with. Set to -1 to keep setting from file.
        args[3] number of intruders to experiment with. Set to -1 to keep setting from file.
        e.g. "./src/main/java/Group9/map/maps/test_2.map" 10000 4 1
        */

        
        

        // ####################################
        String mapFilePath = args[0];

        // experiments
        int maxNumTicks = Integer.parseInt(args[1]);
        int expNumGuards = Integer.parseInt(args[2]);
        int expNumIntruders = Integer.parseInt(args[3]);

        // enable change ingestion for experiments regarding gameSettings:
        GameMap gameMap = Parser.parseFile(mapFilePath);
        if (expNumGuards >= 0) {
            gameMap.getGameSettings().setNumGuards(expNumGuards);
        }
        if (expNumIntruders >= 0) {
            gameMap.getGameSettings().setNumIntruders(expNumIntruders);
        }

        // ####################################


        Game game = new Game(gameMap, new DefaultAgentFactory(), false, maxNumTicks);
        game.run();

        StringBuilder output = new StringBuilder();

        output.append(game.getWinner());
        output.append("|");

        output.append(game.getNumTicks());
        output.append("|");

        output.append(game.getEndTime() - game.getStartTime());

        //System.out.println("winner|ticks|milliseconds");
        System.out.println(output.toString());
    }


}
