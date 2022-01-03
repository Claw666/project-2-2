clc
clearvars
%%
% merging data to get more data samples
before_submission = ["output_smart_exp1_distribution_guards5_20.mat" "output_maze_exp1_distribution_guards2_5_20.mat"];
after_submission = ["output_aftersubmission_exp4_smart_distribution_guards5_20" "output_aftersubmission_exp4_maze_distribution_guards5.mat"];

f = 1;    

% load previous runs
load(before_submission(f));
numTicksDataMerged = numTicksData;
durationsDataMerged = durationsData;
winnersDataMerged = winnersData;

% load after runs
load(after_submission(f));
numTicksDataMerged = [numTicksDataMerged numTicksData];
durationsDataMerged = [durationsDataMerged durationsData];
winnersDataMerged = [winnersDataMerged winnersData];

% export
numTicksData = numTicksDataMerged;
durationsData = durationsDataMerged;
winnersData = winnersDataMerged;

save output_aftersubmission_exp4_smart_distribution_guards5_20_merged.mat numGuardsConfig winnersData numTicksData durationsData


%%
% merging data to get more data samples
before_submission = ["output_smart_exp1_distribution_guards5_20.mat" "output_maze_exp1_distribution_guards2_5_20.mat"];
after_submission = ["output_aftersubmission_exp4_smart_distribution_guards5_20" "output_aftersubmission_exp4_maze_distribution_guards5.mat"];

f = 2;    

% load previous runs
load(before_submission(f));
numTicksDataMerged = numTicksData;
durationsDataMerged = durationsData;
winnersDataMerged = winnersData;

% load after runs
load(after_submission(f));
numTicksDataMerged = [numTicksDataMerged numTicksData];
durationsDataMerged = [durationsDataMerged durationsData];
winnersDataMerged = [winnersDataMerged winnersData];

% export
numTicksData = numTicksDataMerged;
durationsData = durationsDataMerged;
winnersData = winnersDataMerged;

save output_aftersubmission_exp4_maze_distribution_guards5_merged.mat numGuardsConfig winnersData numTicksData durationsData
