% Helper function for the output analysis of our project
% This funtion is related to experiment 1.

% Source disclaimer:
% Partly inspired by Mathematical Simulation group assignment of group 22.

function [winGuards, winIntruders, N, Xbar1, Ssquared, Xtilde, N_intruders, Xbar1_intruders, Ssquared_intruders] = exp1_dudewiczAndDalalTest (mapFileConfig, maxNumTicks, numGuards, numIntruders)
% Dudewicz and Dalal approach for exploring the research question.

% todo: take a look at student portal feedback. There might be an error
% regarding the variance in step 2.
    
    pstar = 0.90;
    dstar = 1000;
    n0 = 20;
    % Given is P*=0.90, n0=20, and number of system configurations is k=4
    h1squared = 2.583 ^2;
    
    % total number of observations needed per configuration
    N = zeros(1,4);
    N_intruders = zeros(1,4);
    % averages of the initial runs for each of the configurations
    Xbar1 = zeros(1,4);
    Xbar1_intruders = zeros(1,4);
    % sample variances of the initial runs for each of the configurations
    Ssquared = zeros(1,4);
    Ssquared_intruders = zeros(1,4);
    % weighted sample means
    Xtilde = zeros(1,4);
    
    % number of guards/ intruders won
    winGuards = zeros(1,4);
    winIntruders = zeros(1,4);
  

    % first stage - initial replications
    for j = 1:length(mapFileConfig)
        
        mapFile = mapFileConfig(j);
        disp("mapFile: " + mapFile);

        % execute simulation runs with initial n0
        [winners, numTicks, durations] = runSimulation(mapFile, maxNumTicks, numGuards, numIntruders, n0);

        % extract information from results
        numTicksGuards = numTicks(find(strcmp(winners,'GUARDS')));
        numTicksGuardsFilled = ones(1,length(winners)) * maxNumTicks;
        numTicksGuardsFilled(find(strcmp(winners,'GUARDS'))) = numTicksGuards;
        
        winGuards(j) = length(numTicksGuards);
        numTicksIntruders = numTicks(find(strcmp(winners,'INTRUDERS')));
        winIntruders(j) = length(numTicksIntruders);
        

        
        % calculate Xbar and sample variances
        Xbar1(j) = mean(numTicksGuardsFilled);
        Ssquared(j) = sampleVariance(numTicksGuardsFilled);

        % calculate the total sample size needed
        N(j) = max(n0 + 1, ceil( ( h1squared * Ssquared(j) ) / dstar^2 ));
        N_intruders(j) = max(n0 + 1, ceil( ( h1squared * Ssquared_intruders(j) ) / dstar^2 ));

    end
    
    disp("######################################");
    
    N
    Xbar1
    Ssquared
    
    disp("######################################");
    
    
    % second stage - additional replications
    for j = 1:length(mapFileConfig)

        mapFile = mapFileConfig(j);
        disp("mapFile: " + mapFile);

        % execute simulation runs with additional Ni - n0
        [winners, numTicks, durations] = runSimulation(mapFile, maxNumTicks, numGuards, numIntruders, N(j)-n0);
        
        % extract information from results
        numTicksGuards = numTicks(find(strcmp(winners,'GUARDS')));
        numTicksGuardsFilled = ones(1,length(winners)) * maxNumTicks;
        numTicksGuardsFilled(find(strcmp(winners,'GUARDS'))) = numTicksGuards;
        
        numTicksIntruders = numTicks(find(strcmp(winners,'INTRUDERS')));
        
        winGuards(j) = (winGuards(j) + length(numTicksGuards)) / N(j);
        winIntruders(j) = (winIntruders(j) + length(numTicksIntruders)) / N(j);
        
        % calculate Xbar and sample variances
        xbar2 = mean(numTicksGuardsFilled);
        
        % calculate weights
        w1 = (n0/N(j)) * (1 + sqrt(1 - (N(j) / n0) * (1 - ( ((N(j)-n0)*dstar^2) / (h1squared * Ssquared(j)) ))));
        w2 = 1 - w1;

        % calculated weighted sample means
        Xtilde(j) = w1 * Xbar1(j) + w2 * xbar2;

    end

end


function var = sampleVariance(Xi)
% Calculation of the variance.

    n = length(Xi);
    xbar = sum(Xi) / n;
    var = sum((Xi - xbar).^2) / (n-1);

end