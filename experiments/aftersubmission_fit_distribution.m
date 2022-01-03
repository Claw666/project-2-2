% Fit distribution to data
clc
clearvars
load('output_aftersubmission_exp4_smart_distribution_guards5_20_merged.mat')

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

%%

for num = 1:length(numGuardsConfig)
    figure
    hist(numTicksGuards{num});
    title("smart guard - " + numGuardsConfig(num) + " - " + chi2gof(numTicksGuards{num}));
end



%% choose data
% 5 guards
% ## smart guard
data = numTicksGuards{1};

%%

summary_statistics(data);

figure
dot_plot(data);

%%
figure
boxplot(data);

%%
figure
hist(data);

%%
figure
hold on;
scatter(data, zeros(length(data),1));
histogram(data,'Normalization','probability', 'FaceColor','#eeeeee');


%%
k=4;
theta=5;

% verifying with several runs of visually checking the
% Maximum-likelihood estimator (MLE)

figure
hold on;
scatter(data, zeros(length(data),1));
histogram(data,'Normalization','probability', 'FaceColor','#eeeeee');
for k=1:5
    x = 0:max(data);
    y = zeros(length(x),1);
    for i = 1:length(y)
        y(i) = gamma_pdf(x(i),k,theta);
    end
    plot(x,y);
end

%%

[ks_statistic, h0_rejected] = ks_test(data, 1.358);
ks_statistic
h0_rejected

%% todos
% check goodness of fit with fitdist

%% definition of functions


% #### task 1

function [] = summary_statistics(data)
% This function prints a summary of statistic values to give a first view
% on the data.

    data = sort(data);
    
    min = data(1);
    max = data(end);
    range = max - min;
    count = length(data);
    mean = sum(data) / count;
    median = get_median(data);
    var = variance(data);
    std = standard_deviation(data);
    
    percentile_2 = percentile(data, 0.02);
    percentile_9 = percentile(data, 0.09);
    percentile_25 = percentile(data, 0.25);
    percentile_50 = percentile(data, 0.50);
    percentile_75 = percentile(data, 0.75);
    percentile_91 = percentile(data, 0.91);
    percentile_98 = percentile(data, 0.98);
    
    iqr = percentile_75 - percentile_25;
    lower_whisker = percentile_25 - iqr * 1.5;
    upper_whisker = percentile_75 + iqr * 1.5;

    disp("# Summary statistics");
    disp("count:    " + count);
    disp("min:      " + min);
    disp("median:   " + median);
    disp("mean:     " + mean);
    disp("max:      " + max);
    disp("range:    " + range);
    disp("variance: " + var);
    disp("std:      " + std);
    
    disp("# Seven number summary");
    disp("percentile  2: " + percentile_2);
    disp("percentile  9: " + percentile_9);
    disp("percentile 25: " + percentile_25);
    disp("percentile 50: " + percentile_50);
    disp("percentile 75: " + percentile_75);
    disp("percentile 91: " + percentile_91);
    disp("percentile 98: " + percentile_98);
    
    disp("# Boxplot data");
    disp("iqr: " + iqr);
    disp("lower_whisker: " + lower_whisker);
    disp("upper_whisker: " + upper_whisker);
    
    
end


function [var] = variance(data)
% Calculating the sample variance
    x_bar = mean(data);
    n = length(data);
    var = sum((data - x_bar) .* (data - x_bar)) / (n-1);
end

function [std] = standard_deviation(data)
% Calculating the sample standard deviation based upon the function
% for variance above.
    std = sqrt(variance(data));
end


function [nth_percentile] = percentile(data, nth)
% Calculates the nth percentile of provided data.

    if nth < 0 || nth > 1
        error ("Please provide a percentile value between 0 and 1.");
    end
    
    % handling edge cases first
    data = sort(data);
    if nth == 0
        nth_percentile = data(1);
        return;
    end
    
    if nth == 1
        nth_percentile = data(end);
        return;
    end
    
    % calculate the percentile
    n = length(data);
    value_position = nth * (n - 1) + 1;
        % usage of -1 and +1 necessary, because I need the position
        % without counting the range from 0 to 1. Example:
        %   percentile 0.5 of two values should be position 1.5
        %   and not 1, as it would be with 2 * 0.5 = 1.
    
    if mod(value_position,1) == 0
        % integer position calculated
        nth_percentile = data(value_position);
    else
        % fractional one between two positions calculated
        value_position = floor(value_position);
        nth_percentile = (data(value_position) + data(value_position+1)) / 2;
    end
    
end

function [median] = get_median(data)
% calculate median using the percentile function
    median = percentile(data, 0.5);
end


function [] = dot_plot(data)
% Create a Dot Plot with jitter
% Source for jitter:
% http://undocumentedmatlab.com/articles/undocumented-scatter-plot-jitter
    jitter_size = 0.5;
    
    % plot data
    scatter(zeros(1,length(data)), data, 'jitter','on', 'jitterAmount',jitter_size);
    hold on;
    
    % plot horizontal line for median
    line_size = linspace(-jitter_size * 1.5, jitter_size * 1.5, 5);
    plot(line_size,ones(length(line_size),1) * median(data))
    xlim([-jitter_size*4 jitter_size*4])
end


function [y] = gamma_pdf(x,k,theta)
% Calculating the Probability density function of a gamma distribution.
    
    % parameters need to be positive
    if k <= 0 || theta <= 0
        error("Please provide k and theta > 0");
    end
        
    % Implemented according to the PDF definition:
    % https://en.wikipedia.org/wiki/Gamma_distribution
    fraction = 1 / (gamma(k) * theta ^ k);
    y = fraction .* x .^ (k-1) .* exp(-x ./ theta);

end



% #### task 2


function [z] = lcg(seed, size)
% A Linear Congruential Generator to generate "size" amount of random
% values starting with the "seed".

    a = 25214903917;
    c = 11;
    m = 2^48;
    
    z = [];
    
    % generate initial random number
    z_full = mod((a * seed + c), m);
    z(1) = lcg_extract_bits(z_full, 16, 47);
   
    
    i = 2;
    while i <= size
        z_full = mod((a * z(i-1) + c), m);
        z(i) = lcg_extract_bits(z_full, 16, 47);
        i = i + 1;
    end
    
    % Normalize the random values to U(0,1) based upon the extracted number
    % of bits: 47 - 16 = 31
    z = z ./ 2^31;
end


function [z_extract] = lcg_extract_bits(z_full, from, to)
% A helper function for the lcg method. My aim was to avoid too repeated
% code. It uses dec2bin to extract the necessary bits.

    z_full_binary = dec2bin(z_full,64);
    z_part_binary = z_full_binary(from:to);
    z_extract = bin2dec(z_part_binary);
    
end


function [ks_statistic, h0_rejected] = ks_test(x, critical_value)
% The Kolmogorov-Smirnov test for a uniform distribution.

    % setting the stage
    n = length(x);
    h0_rejected = false;  % assume, that the null hypothesis is not rejected
    x = sort(x);        % observed values have to be sorted
    
    % calculate D+
    D_plus_values = zeros(1,n); % preallocating array for speed
    i = 1;
    while i <= n
        D_plus_values(i) = abs(i/n - x(i));
        i = i + 1;
    end
    D_plus = max(D_plus_values);
    
    % calculate D-
    D_minus_values = zeros(1,n);
    i = 1;
    while i <= n
        D_minus_values(i) = abs(x(i) - (i-1)/n);
        i = i + 1;
    end
    D_minus = max(D_minus_values);
    
    % calculate D
    D = max(D_plus, D_minus);
    
    % calculate adjusted D according to uniform distribution
    ks_statistic = (sqrt(n) + 0.12 + 0.11/sqrt(n)) * D;
    
    % check if null hypothesis is rejected.
    % checked for alpha being 0.05
    if ks_statistic > critical_value
        h0_rejected = true;
    end
end


function [x2, h0_rejected] = poker_test(x, critical_value)
% The Poker test to check for independence in an uniform distribution.

    % Check uniform distribution boundaries 0 and 1.
    if min(x) < 0 || max(x) > 1
        error ("Please provide a uniform distribution");
    end
    
    % set the stage
    frequencies = zeros(1,7); % seven different patterns exist
    probabilities = [0.3024 0.5040 0.1080 0.072 0.009 0.0045 0.0001];
    
    % Transform sequence of U(0,1) fractions to sequence of integers
    x = floor(10 * x);
    
    % Create groups of 5 integers
    % While there is still a full group inside x, do:
    while length(x) / 5 > 1 || length(x) == 5
    
        % Identify pattern per group and increase pattern frequency
        [pattern, id] = identify_poker_test_pattern(x(1:5));
        frequencies(id) = frequencies(id) + 1;
        
        x = x(6:end); % remove the first 5 elements from x
    end
    
    % Use chi squared test to test the distribution
    [x2, h0_rejected] = chi2_test(frequencies, probabilities, critical_value);
end


function [pattern, id] = identify_poker_test_pattern(x)
% Identify the poker patterns from 5 consecutive numbers.

% I assume the patterns to be in any order possible. Example:
% One pair is one of these: AABCD, ABBCD, ABCCD, ABCDD
%
% If this would not be the case, i.e. only AABCD is "one pair", then the
% pattern "ABBCD" would not fit to any pattern shown on the slide. Hence it
% would not be taken into account, which would manipulate the result.

% General concept of this function:
% 1. Normalize all possible numbers 0-9 down to the lowest possible values:
%    23357 -> 12234
%    88491 -> 33241
% 2. Sort these normalized values to get same numbers next to each other.
%    33231 -> 12333
% 3. Match possible patterns.
% 4. Provided none matches, then all symbols are different.

    % normalize
    items = sort(unique(x));
    for i = 1 : length(items)
        x(x==items(i)) = i*10; % avoid overwriting already processed numbers
    end
    
    % sort to get same numbers (symbols) next to each other
    x = sort(x);

    % match patterns
    if isequal(x(1), x(2), x(3), x(4), x(5))
        pattern = 'five of a kind';
        id = 7;
        return;
    end

    if isequal(x(1), x(2), x(3), x(4)) || isequal(x(2), x(3), x(4), x(5))
        pattern = 'four of a kind';
        id = 6;
        return;
    end

    if (isequal(x(1), x(2), x(3)) && isequal(x(4), x(5))) || ...
        (isequal(x(1), x(2)) && isequal(x(3), x(4), x(5)))
        pattern = 'full house';
        id = 5;
        return;
    end

    if isequal(x(1), x(2), x(3)) || ...
        isequal(x(2), x(3), x(4)) || ...
        isequal(x(2), x(3), x(4))
        pattern = 'three of a kind';
        id = 4;
        return;
    end
    
    if (isequal(x(1), x(2)) && isequal(x(3), x(4))) || ...
        (isequal(x(1), x(2)) && isequal(x(4), x(5))) || ...
        (isequal(x(2), x(3)) && isequal(x(4), x(5)))
        pattern = 'two pairs';
        id = 3;
        return;
    end
    
    if isequal(x(1), x(2)) || isequal(x(2), x(3)) || ...
        isequal(x(3), x(4)) || isequal(x(4), x(5))
        pattern = 'one pair';
        id = 2;
        return;
    end
    
    pattern = 'all different';
    id = 1;
    
end


function [x2, h0_rejected] = chi2_test(N_i_values, p_i_values, critical_value)
% Chi square test
% N_i_values: Number of observations by class.
% p_i_values: Probabilities by class.

    % check, if both variables have the same number of classes
    if (length(N_i_values) ~= length(p_i_values))
        error ("Please make sure to provide an equal number of classes in N and p.");
    end
    
    % setting the stage
    h0_rejected = false;

    % calculate expected number of observations by class
    n = sum(N_i_values);
    np_i_values = n * p_i_values;
    
    % calculate x squared to get the test statistic
    x2_values = zeros(1,n);
    i = 1;
    while i <= length(np_i_values)
        xp_values(i) = (N_i_values(i) - np_i_values(i)) ^ 2 / np_i_values(i);
        i = i + 1;
    end
    x2 = sum(xp_values);
    
    % check if null hypothesis is rejected.
    % checked for alpha being 0.05
    if x2 > critical_value
        h0_rejected = true;
    end
end