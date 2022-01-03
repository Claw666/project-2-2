%%
clc
clearvars
load('output_smart_exp1_distribution_guards5_20.mat');

%%
% collect metrics
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
end
    
%% experiment 4
i = 1; % 1 -> 5 guards

a = 0.05;
t_confidence(a, numTicksGuards{1})
mean(numTicksGuards{i})


%% functions
% source inspired by Mathematical Simulation Projekt group 22

function [x] = t_confidence(alpha, Xi)
% Calculate the confidence interval according to alpha and data Xi.

    n = length(Xi);
    dof = n - 1;
    xbar = sum(Xi) / n;
    
    oneSide = tinv(1 - alpha/2, dof) * sqrt(sampleVariance(Xi) / n);
    x = [xbar-oneSide xbar+oneSide];
end

function var = sampleVariance(Xi)
% Calculation of the variance.

    n = length(Xi);
    xbar = sum(Xi) / n;
    var = sum((Xi - xbar).^2) / (n-1);

end