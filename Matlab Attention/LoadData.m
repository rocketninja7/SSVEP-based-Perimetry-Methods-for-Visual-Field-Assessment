 clear;close all;clc;

data_dir='./data/';
addpath('_func');

%label id
s_within_miss = 0:79;
s_within_press = 80:159;
s_without_press = 160:239;
s_without_miss = 240:319;
s_id = [s_within_miss s_within_press s_without_press s_without_miss];
s_size = [length(s_within_miss) length(s_within_press) length(s_without_press) length(s_without_miss)];
k_without = 320:349;
k_within_press = 350:379;
k_id = [k_without k_within_press];
k_size = [length(k_without) length(k_within_press)];
 
dataFiles = dir([data_dir '*.txt']);
nUser = round(length(dataFiles)/2);
Data = [];
s_user=1;
k_user=1;
for iFile=1: length(dataFiles)
    fStr = strsplit(dataFiles(iFile).name,'_');
    expType = fStr{1};
    %load data
    fData =load(fullfile(dataFiles(iFile).folder, dataFiles(iFile).name));
    if strcmp(expType,'k')
        Data.kinetic(k_user) = SegmentKineticData(fData, k_id, k_size,dataFiles(iFile).name);
        k_user=k_user+1;
    elseif strcmp(expType,'s')
        Data.static(s_user) = SegmentStaticData(fData, s_id, s_size,dataFiles(iFile).name);
        s_user=s_user+1;
    end
end
save(fullfile(data_dir,'nush_data.mat'),'Data','-v7.3');
