%%
clc
clearvars

%% Setting the stage

% Run the setup only once
if exist('setupComplete','var') == 0
    disp("Setting up java simulation:");
    setupJavaSimulation();
    setupComplete = true;
end

%% (1) research question 1 - guards 1:20
% 20 configurations * 20runs * 12seconds ~ 80min

mapFile = "test_2.map";
maxNumTicks = 10000;
numIntruders = 1;
runs = 20;

numGuardsConfig = [1 2 5 10 15 20];
winnersData = strings(length(numGuardsConfig), runs);
numTicksData = zeros(length(numGuardsConfig), runs);
durationsData = zeros(length(numGuardsConfig), runs);

tic
for i = 1:length(numGuardsConfig)
    
    numGuards = numGuardsConfig(i);
    [winners, numTicks, durations] = runSimulation(mapFile, maxNumTicks, numGuards, numIntruders, runs);
    
    winnersData(i,:) = winners;
    numTicksData(i,:) = numTicks;
    durationsData(i,:) = durations;
    
end
toc

% output of collected data
save output_exp1_1-20_guards.mat numGuardsConfig winnersData numTicksData durationsData


%% (2) research question 3 - distribution for guards: 2 vs. 5 vs. 20
% ~60min

mapFile = "test_2.map";
maxNumTicks = 10000;
numIntruders = 1;
runs = 200;

numGuardsConfig = [2 5 20];
winnersData = strings(length(numGuardsConfig), runs);
numTicksData = zeros(length(numGuardsConfig), runs);
durationsData = zeros(length(numGuardsConfig), runs);

tic
for i = 1:length(numGuardsConfig)
    
    numGuards = numGuardsConfig(i);
    [winners, numTicks, durations] = runSimulation(mapFile, maxNumTicks, numGuards, numIntruders, runs);
    
    winnersData(i,:) = winners;
    numTicksData(i,:) = numTicks;
    durationsData(i,:) = durations;
    
end
toc

% output of collected data
save output_exp1_distribution_guards2_5_20.mat numGuardsConfig winnersData numTicksData durationsData

