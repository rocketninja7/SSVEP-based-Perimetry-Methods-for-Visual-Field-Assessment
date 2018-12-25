clc;clear;close all;
%ArtifactRemoval and suplabel not used?
data_dir = 'data';
addpath(genpath('_func'));

para_featuresextraction;
%para_classifier;
%para_bciscoresmoothing;

%load saved data
load(fullfile(data_dir, 'nush_data.mat'));

Fs=200;                 % sampling rate
segSize = Fs*1;
stepSize = Fs*0.1;
fOrder = 4;               
[filtB, filtA] = butter(fOrder,[0.3 45]/(Fs/2),'bandpass'); 

fpBpFeat = []; 
%kinetic SSVEP recognition [Just change static for Static experiment]
figure;
for file=1 : length(Data.kinetic)
    for iStim=1 : length(Data.kinetic(file).stimId)%file needed?
        eeg = Data.kinetic(file).eeg{iStim};%file needed?
        %% Band power features Extraction
        eegRaw = eeg(:, 3);
        nSeg = floor(length(eegRaw-segSize)/stepSize + 1);
        fXTest = []; startIdx=1;
        for iSeg=1 : nSeg       
            incSample = stepSize*(iSeg-1);
            endIdx = segSize + incSample;
            if endIdx > length(eegRaw)%append zero           
                eegRaw = [eegRaw; zeros(endIdx - length(eegRaw), 1)];
            end
            eegSeg = eegRaw( startIdx: endIdx);
            %bandpass filtering of raw EEG
            eegBp = filtfilt(filtB, filtA, eegSeg);
            %features extraction
            fb_filt_eeg = ApplyFilterBanks(eegBp, fb_para);%defined function
            fXTest(iSeg,:) = ExtractBandPowers(fb_filt_eeg, fb_para);%defined function
            startIdx = endIdx-(segSize - stepSize);
        end
        fpBpFeat{iStim} = fXTest;    
        %{
        %% classification
        fYTest = zeros(size(fXTest,1), 1); %2-no of aggregated features: mean, variance
        [fClOut, fAcc, fClScore]=libsvmpredict(fYTest, fXTest, mdl, '-b 1');
        for iSeg=1 : nSeg
            rawScore(iSeg) = fClScore(iSeg, fClOut(iSeg)+1);
        end
        smoothScore = SmoothScore(rawScore, windowSize);
        attScore{iStim, 1} = rawScore;
        attScore{iStim, 2} = smoothScore';
        %}
        subplot(3,2,1), plot(fpBpFeat{iStim}(:,1)); title('Mean Delta Energy');
        subplot(3,2,2), plot(fpBpFeat{iStim}(:,2));title('Mean Theta Energy');
        subplot(3,2,3),  plot(fpBpFeat{iStim}(:,3));title('Mean Alpha Energy');
        subplot(3,2,4),  plot(fpBpFeat{iStim}(:,4));title('Mean Low Beta Energy');
        subplot(3,2,5),  plot(fpBpFeat{iStim}(:,5));title('Mean High Beta Energy');
        subplot(3,2,6),  plot(fpBpFeat{iStim}(:,6));title('Mean Gamma Energy');
        suplabel(strcat('Kinetic - ', num2str(iStim)),'t');
        pause;
    end
end%end here?

%Either using SVM Classifier or bandpower ratio
% Bandpower ratio: https://www.ncbi.nlm.nih.gov/pmc/articles/PMC4712412/
% https://www.ncbi.nlm.nih.gov/pmc/articles/PMC5534477/

%{
function outScore = SmoothScore(inScore, smoothscore)
    nNumTrial = size(inScore,2);
    fMAMat = zeros(nNumTrial,nNumTrial);
    kMAComp= smoothscore ; %10; % this parameter can be adjusted/optimised
    for k =1:nNumTrial-kMAComp
        fMAMat(k,k:k+kMAComp-1) = 1/kMAComp;
    end
    for k =kMAComp:-1:1
        fMAMat(nNumTrial-k+1,nNumTrial-k+1:nNumTrial) = 1/k;
    end
    outScore= fMAMat*inScore';      
end
%}
