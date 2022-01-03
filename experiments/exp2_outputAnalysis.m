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


%% Dudewicz and Dalal approach for exploring the research question.
clc
mapFileConfig = ["open_space.map" "rooms.map" "spiral.map" "teleport.map"];
maxNumTicks = 10000;
numGuards = 5;
numIntruders = 1;

tic
[winGuards, winIntruders, N_guards, Xbar1_guards, Ssquared_guards, Xtilde, N_intruders, Xbar1_intruders, Ssquared_intruders] = exp2_dudewiczAndDalalTest (mapFileConfig, maxNumTicks, numGuards, numIntruders);
toc

disp("Dudewicz and Dalal output");
N_guards
winGuards
Xbar1_guards
Ssquared_guards
Xtilde

save output_exp2_dd_test_guards.mat N_guards winGuards Xbar1_guards Ssquared_guards Xtilde


disp("Further output of collected metrics");
disp("N_intruders was not used, because guards were in focus.");
N_intruders
winIntruders
Xbar1_intruders
Ssquared_intruders

save output_exp2_dd_test_intruders.mat N_intruders winIntruders Xbar1_intruders Ssquared_intruders


%% visualizing test output

% Generate a horizontal barchart to show the result of above D&D Test

load('output_maze_exp2_dd_test_guards.mat')
maps = ["open space", "rooms", "spiral" "teleport"];
Xtilde(isnan(Xtilde)) = 10000; %impute nans with maxNumTicks
maps = flip(maps);
Xtilde = flip(Xtilde);
figure
b = barh(Xtilde, 0.5)
axis([0 11000 0.25 4.75])

title('Dudewicz and Dalal result')
xlabel('Number of turns')
ylabel('Map')
box on
set(gca, 'YTick', 1:length(maps))
set(gca, 'YTickLabel', maps)

xtips = b(1).YEndPoints + 100;
ytips = b(1).XEndPoints;
labels = strings(1, length(b(1).YData));
for i = 1:length(b(1).YData)
    currentValue = round(b(1).YData(i));
    maxValue = max(b(1).YData);
    if currentValue < maxValue
        improvement = round((1 - currentValue / maxValue) * 100);
        labels(i) = string(currentValue) + " (-" + string(improvement) + "%)";
    else
        labels(i) = string(currentValue);
    end
end
text(xtips,ytips,labels,'VerticalAlignment','middle')

%%

figure
barh(winGuards);
