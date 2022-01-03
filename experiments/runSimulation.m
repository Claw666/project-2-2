% Helper function for the output analysis of our project

function [winners, numTicks, durations] = runSimulation(mapFile, maxNumTicks, numGuards, numIntruders, runs)
% This function performs the java simulation run with the given arguments
% to the java simulation. This is done "runs" amount of times. The results
% are returned in several vectors.

    disp("Start simulation runs for " + mapFile + " and numGuards: " + numGuards);

    % initializing the vectors
    winners = strings(1,runs);
    numTicks = zeros(1,runs);
    durations = zeros(1,runs);
    
    % concatinate command with given arguments.
    % definitions for running the simulation
    baseMapFilePath = "../src/main/java/Group9/map/maps/";
    mapFilePath = baseMapFilePath + mapFile;
    command = "java -cp ./out/ Group9.Main";
    fullCommand = join([command, mapFilePath, maxNumTicks, numGuards, numIntruders], " ");

    % perform "runs" amount of runs
    for run = 1:runs
        disp("run: " + run);
    
        [status,cmdout] = system(fullCommand);
        if status == 1
            disp("ERROR:");
            disp(cmdout);
        end

        % the java simulation outputs a one-liner to standard out, which is
        % read by matlab. Data is divided by pipes.
        output = split(strtrim(cmdout),"|");

        % storing results
        winners(run) = string(output(1));
        numTicks(run) = str2double(output(2));
        durations(run) = str2double(output(3));

    end
    disp("Done with simulation.");
end