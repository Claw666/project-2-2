%%

clc
clearvars

%% Distribution for guards winning
% -- fit distribution to guards number of ticks

dataFile = ["output_smart_exp1_distribution_guards5_20.mat" "output_maze_exp1_distribution_guards2_5_20.mat"];

pdf_y = cell(1,2);
numGuardsPlot = cell(1,2);
numGuardsToPlot = 20;

for i = 1:length(dataFile)

    % load data
    load(dataFile(i));
    
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
    
    index = find(numGuardsConfig==numGuardsToPlot);
    x = 0:1:10000;
    
    pd = fitdist(transpose(numTicksGuards{index}),'normal');
    pdf_y{i} = pdf(pd,x);
    numGuardsPlot{i} = numTicksGuards{index};

end

figure
hold on;

% smarter guard
plot(x,pdf_y{1},'g', 'LineWidth',2)
%scatter(numGuardsPlot{1},zeros(1,length(numGuardsPlot{1})), 'g');

% maze guard
plot(x,pdf_y{2},'b', 'LineWidth',2)
%scatter(numGuardsPlot{2},zeros(1,length(numGuardsPlot{2})), 'b');

title("Distribution of steps to catch intruder.");
legend(["smart guard" "maze guard"]);
xlabel('Number of steps to catch intruder')
ylabel('Probability density')
box on


%% add statistical tests after submission:

figure
hist(numGuardsPlot{1});
title("smart guard - " + numGuardsToPlot + " - " + chi2gof(numGuardsPlot{1}));

figure
hist(numGuardsPlot{2});
title("maze guard - " + numGuardsToPlot + " - " + chi2gof(numGuardsPlot{2}));


%% more
% taken from assignment 2, mathematical simulation, arthur

% To calculate the 95% confidence interval on the difference in average
% profit of the first and second system configuration, I will use the
% modified two-sample-t confidence interval. Reason being, that this one
% can handle two systems with different number of observations.
% I further assume a) independence between system1 and system2 because of
% different random variables used in their runs and b) normal distributed
% observations, because the average is used, due to central limit theorem.

alpha = 0.05;

t_confidence_difference = modified_2sample_t_confidence(alpha, numGuardsPlot{1}, numGuardsPlot{2});
disp("confidence interval on the difference");
disp(t_confidence_difference);


n = [length(numGuardsPlot{1}) length(numGuardsPlot{2})];
xbar = [sum(numGuardsPlot{1})/n(1) sum(numGuardsPlot{2})/n(2)];
s2 = [sampleVariance(numGuardsPlot{1}) sampleVariance(numGuardsPlot{2})];
dof = estimate_dof_welch(s2(1), s2(2), n(1), n(2));
t_test_statistic = (xbar(1) - xbar(2)) / sqrt(s2(1)/n(1) + s2(2)/n(2));
p_value = (1 - tcdf(abs(t_test_statistic),dof)) * 2;
disp("t test on a significant difference of two systems:");
disp("test statistic: " + t_test_statistic);
disp("degrees of freedom: " + dof);
disp("p_value: " + p_value);



function [x] = t_confidence(alpha, Xi)
% Calculate the confidence interval according to alpha and data Xi.

    n = length(Xi);
    dof = n - 1;
    xbar = sum(Xi) / n;
    
    oneSide = tinv(1 - alpha/2, dof) * sqrt(sampleVariance(Xi) / n);
    x = [xbar-oneSide xbar+oneSide];
    
end


function [x] = modified_2sample_t_confidence(alpha, X1j, X2j)
% Calculate the confidence interval on the difference of two systems
% according to alpha and data of system 1: X1j and sysetm 2: X2j.

    n = [length(X1j) length(X2j)];
    xbar = [sum(X1j)/n(1) sum(X2j)/n(2)];
    s2 = [sampleVariance(X1j) sampleVariance(X2j)];
    dof = estimate_dof_welch(s2(1), s2(2), n(1), n(2));
    
    oneSide = tinv(1 - alpha/2, dof) * sqrt(s2(1) / n(1) + s2(2) / n(2));
    x = [xbar(1)-xbar(2)-oneSide xbar(1)-xbar(2)+oneSide];
    
end


function dof = estimate_dof_welch(s2_1, s2_2, n1, n2)
% Estimates degrees of freedom using Welch approach given the
% sampleVariance and the number of observations of two systems.
    
    nominator = (s2_1 / n1 + s2_2 / n2)^2;
    denominator = (s2_1 / n1)^2 / (n1 - 1) + (s2_2 / n2)^2 / (n2 - 1);
    dof = nominator / denominator;
end


function var = sampleVariance(Xi)
% Calculation of the variance.

    n = length(Xi);
    xbar = sum(Xi) / n;
    var = sum((Xi - xbar).^2) / (n-1);

end
