%clc;clear;close all;
addpath('_func');

data_dir = 'data';
%load saved data
load(fullfile(data_dir, 'nush_data.mat'));

Fs=200;                 % sampling rate
N=2;                    % number of harmonics
t_length=3;  			% analysis window length
TW=0.5:0.5:t_length;
TW_p=round(TW*Fs);
%n_trial=1;
sti_f=[5];             % stimulus frequencies
n_sti=length(sti_f);  

sc1=SinCos(sti_f(1),Fs,t_length*Fs,N);

fOrder = 4;               
[filtB, filtA] = butter(fOrder,[0.3 45]/(Fs/2),'bandpass'); 

%static SSVEP recognition
for file=1 :length(Data.static)
    ccc = [];
    for iStim=1 : length(Data.static(file).stimId)
        eegRaw = Data.static(file).eeg{iStim}(:,1:2);
        %bandpass filtering of raw EEG
        eegBp = filtfilt(filtB, filtA, eegRaw);
        %normalize eeg data
        eegNorm = (eegBp - mean(eegBp))./std(eegBp);
        for iTw=1 : length(TW)
            fprintf('Static CCA Processing No.trial %d, TW %0.1fs, No samples %d\n', Data.static(file).stimId(iStim), TW(iTw), TW_p(iTw));
            if TW_p(iTw) > size(eegNorm, 1)
                [wx1,wy1,r1,u1,v1]=canoncorr(eegNorm, sc1(1:size(eegNorm,1))');
            else
                [wx1,wy1,r1,u1,v1]=canoncorr(eegNorm(1:TW_p(iTw),:), sc1(1:TW_p(iTw))');
            end
            ccc.static(iStim, iTw) = max(r1);
        end
        ccc.static(iStim, length(TW)+1) = Data.static(file).classId(iStim);
        %ccc.static(iStim, length(TW)+2) = Data.static(file).stimId(iStim);
    end
    csvwrite(fullfile('ccc',Data.static(file).fname),ccc.static);
end
for file=1 :length(Data.kinetic)
    for iStim=1 : length(Data.kinetic(file).stimId)
        eegRaw = Data.kinetic(file).eeg{iStim}(:,1:2);
        %bandpass filtering of raw EEG
        eegBp = filtfilt(filtB, filtA, eegRaw);
        %normalize eeg data
        eegNorm = (eegBp - mean(eegBp))./std(eegBp);
        for iTw=1 : length(TW)
            fprintf('Kinetic CCA Processing No.trial %d, TW %0.1fs, No samples %d\n', Data.kinetic(file).stimId(iStim), TW(iTw), TW_p(iTw));
            if TW_p(iTw) > size(eegNorm, 1)
                [wx1,wy1,r1,u1,v1]=canoncorr(eegNorm, sc1(1:size(eegNorm,1))');
            else
                [wx1,wy1,r1,u1,v1]=canoncorr(eegNorm(1:TW_p(iTw),:), sc1(1:TW_p(iTw))');
            end
            ccc.kinetic(iStim, iTw) = max(r1);
        end
        ccc.kinetic(iStim, length(TW)+1) = Data.kinetic(file).classId(iStim);
        %ccc.kinetic(iStim, length(TW)+2) = Data.kinetic(file).stimId(iStim);
    end
    csvwrite(fullfile('ccc',Data.kinetic(file).fname),ccc.kinetic);
end