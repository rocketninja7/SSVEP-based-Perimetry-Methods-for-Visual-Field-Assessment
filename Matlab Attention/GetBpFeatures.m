clc;clear;close all;
%ArtifactRemoval and suplabel not used?
data_dir = './data/';
addpath(genpath('_func'));

para_featuresextraction;
%para_classifier;
%para_bciscoresmoothing;

Fs=200;                 % sampling rate
segSize = Fs*1;
stepSize = Fs*0.1;
fOrder = 4;               
[filtB, filtA] = butter(fOrder,[0.3 45]/(Fs/2),'bandpass'); 

dataFiles = dir([data_dir '*.txt']);
nUser = round(length(dataFiles)/2);

for iFile=1: length(dataFiles)
    fname=dataFiles(iFile).name;
    fprintf("Extracting from "+fname+"\n");
    fData =load(fullfile(dataFiles(iFile).folder, dataFiles(iFile).name));
    eeg=fData(1:end,2:4);
    timestamp=fData(1:end,10);
    eegRaw = eeg(:, 3);
    nSeg = floor((length(eegRaw)-segSize)/stepSize + 1);
    fXTest = []; startIdx=1;
    for iSeg=1 : nSeg       
        incSample = stepSize*(iSeg-1);
        endIdx = segSize + incSample;
        if endIdx > length(eegRaw)%append zero
            frprintf("what");
            eegRaw = [eegRaw; zeros(endIdx - length(eegRaw), 1)];
        end
        eegSeg = eegRaw( startIdx: endIdx);
        %bandpass filtering of raw EEG
        eegBp = filtfilt(filtB, filtA, eegSeg);
        %features extraction
        fb_filt_eeg = ApplyFilterBanks(eegBp, fb_para);%defined function
        fXTest(iSeg,2:7) = ExtractBandPowers(fb_filt_eeg, fb_para);%defined function
        fXTest(iSeg,1)=timestamp(startIdx);
        startIdx = endIdx-(segSize - stepSize);
    end
    dlmwrite(fullfile(data_dir,"bpFeatures\",fname),fXTest,'precision', 13);
end