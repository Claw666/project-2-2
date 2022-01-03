% Helper function for the output analysis of our project

function [] = setupJavaSimulation()
% Before being able to run the java simulation, all the java code has to be
% compiled first. This is done here.
% Disclaimer: This function is inspired by the group project Mathematical
% Simulation from group 22.

    sourcePath = "../src/main/java";
    simulationPath = "../src/main/java/Group9/Main.java";
    classPath = "./out/";
    
    % create java class directory
    disp("-- create java class directory");
    cmd = "mkdir -p " + classPath;
    system(cmd);

    % compile java code
    disp("-- compile java code");
    cmd = "javac " + simulationPath + " -d " + classPath + " -sourcepath " + sourcePath;
    [status,cmdout] = system(cmd);
    if (status == 1)
        disp(cmdout);
    end  
    
    disp("done");
end