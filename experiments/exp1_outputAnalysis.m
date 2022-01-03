%%
clc
clearvars
load('output_exp1_1-20_guards.mat')

%% collect metrics
nConfigs = winnersData.size(1);
nRuns = winnersData.size(2);

% overview of how often a team has won
numWinGuards = zeros(1,nConfigs);
numWinIntruders = zeros(1,nConfigs);
numWinNone = zeros(1,nConfigs);

% number of ticks
numTicksGuards = cell(1, nConfigs);
numTicksIntruders = cell(1, nConfigs);

% calculate metrics
for configId = 1:nConfigs
    numTicksGuards{configId} = numTicksData(configId,find(strcmp(winnersData(configId,:),'GUARDS')));
    numTicksIntruders{configId} = numTicksData(configId,find(strcmp(winnersData(configId,:),'INTRUDERS')));
    numWinGuards(configId) = length(numTicksGuards{configId});
    numWinIntruders(configId) = length(numTicksIntruders{configId});
    numWinNone(configId) = nRuns - (numWinGuards(configId) + numWinIntruders(configId));
    
    % encoding loosing a run for guards
    numTicksGuards{configId}
    tmp = ones(1,nRuns) * 10000;
    tmp(find(strcmp(winnersData(configId,:),'GUARDS'))) = numTicksGuards{configId};
    numTicksGuards{configId} = tmp;
    numTicksGuards{configId}
end

%% (1) Overview of how often a team wins per numGuards configuration

% -- plot stacked bar chart
figure(1)
b = bar(numGuardsConfig, [numWinGuards; numWinIntruders; numWinNone], 0.5, 'stack')
b(1).FaceColor = '#0072BD';
b(2).FaceColor = '#77AC30';
b(3).FaceColor = '#D95319';

axis([0 21 0 nRuns])
set(gca, 'XTick', 1:20)
title('Teams winning per number of guards (simulation limit of 10,000 turns)')
xlabel('Number of guards')
ylabel('Number of runs')
legend('guards', 'intruders', 'none')
box on

%% (2) Overview of guard numTicks - linear regression
% -- calculate means and plot them
numTicksGuard_xbar = zeros(1,nConfigs);

figure(2)
hold on;
for i = 1:nConfigs
    numTicksGuard_xbar(i) = mean(numTicksGuards{i});
    p1 = scatter(ones(1,nRuns) * numGuardsConfig(i) ,numTicksGuards{i}, 'b');
end
p2 = plot(numGuardsConfig, numTicksGuard_xbar, '-sb');

% -- perform regression analysis (on means / on all points)
X = numGuardsConfig(1:end)
y = numTicksGuard_xbar(1:end)
linearModel = fitlm(transpose(X),y, 'linear', 'RobustOpts','on')
p3 = plot(1:20, predict(linearModel, transpose(1:20)), '-sr');

axis([0 21 0 12000])
set(gca, 'XTick', numGuardsConfig)
title('Number of turns the intruder was able to survive the maze guard')
xlabel('Number of guards')
ylabel('Number of turns')
legend([p1 p2 p3],'single run','mean runs per number of guards', 'regression line')
box on

linearModel.Formula
linearModel.Coefficients.Estimate


%% (2) Overview of guard numTicks - exponential regression
numTicksGuard_xbar = zeros(1,nConfigs);

figure
hold on;
for i = 1:nConfigs
    numTicksGuard_xbar(i) = mean(numTicksGuards{i});
    p1 = scatter(ones(1,nRuns) * numGuardsConfig(i) ,numTicksGuards{i}, 'b');
end
p2 = plot(numGuardsConfig, numTicksGuard_xbar, '-sb');

% -- perform regression analysis (on means / on all points)
x = transpose(numGuardsConfig(1:end));
y = transpose(numTicksGuard_xbar(1:end));

% source taken from:
% https://de.mathworks.com/matlabcentral/answers/91159-how-do-i-fit-an-exponential-curve-to-my-data#answer_100573
g = fittype('a-b*exp(-c*x)');
[f0,gof] = fit(x,y,g,'StartPoint',[[ones(size(x)), -exp(-x)]\y; 1]);
xx = linspace(1,20,20);
p3 = plot(xx,f0(xx),'-sr');

axis([0 21 0 12000])
set(gca, 'XTick', numGuardsConfig)
title('Number of turns the intruder was able to survive the smart guard')
xlabel('Number of guards')
ylabel('Number of turns')
legend([p1 p2 p3],'single run','mean runs per number of guards', 'regression line')
box on


coeffnames(f0)
coeffvalues(f0)
gof.rsquare





