interval=500;
period=1000;

addpath('_func');
data_dir='./newdata/';
sti_f=[5];             % stimulus frequencies

Fs=200;                 % sampling rate
N=2;                    % number of harmonics
t_length=3;  			% analysis window length
TW=0.5:0.5:t_length;
TW_p=round(TW*Fs);
%n_trial=1;
n_sti=length(sti_f);  
sc1=SinCos(sti_f(1),Fs,t_length*Fs,N);
fOrder = 4;               
[filtB, filtA] = butter(fOrder,[0.3 45]/(Fs/2),'bandpass'); 
addpath('_func');
dataFiles = dir([data_dir '*.txt']);
nUser = round(length(dataFiles)/2);
Data = [];
for iFile=1: length(dataFiles)
    Data.fname=dataFiles(iFile).name;
    fData =load(fullfile(dataFiles(iFile).folder, dataFiles(iFile).name));
    Data.eeg=fData(1:end,2:4);
    Data.timestamp=fData(1:end,10);
    ccc=[];
    stop=false;
    intervalNo=1;
    startIndex=1;
    while stop==false
        intervalStart=Data.timestamp(startIndex);
        index=startIndex+1;
        while (Data.timestamp(index)<intervalStart+period)
            index=index+1;
            if index>size(Data.timestamp)
                index=index-1;
                stop=true;
                break;
            end
        end
        eegRaw=Data.eeg(startIndex:index,1:2);
        %bandpass filtering of raw EEG
        if size(eegRaw)<24
            continue;
        end
        eegBp = filtfilt(filtB, filtA, eegRaw);
        %normalize eeg data
        eegNorm = (eegBp - mean(eegBp))./std(eegBp);
        [wx1,wy1,r1,u1,v1]=canoncorr(eegNorm, sc1(1:size(eegNorm,1))');
        ccc(intervalNo,1)=intervalStart;
        ccc(intervalNo,2)=max(r1);
        intervalNo=intervalNo+1;
        intervalStart=Data.timestamp(startIndex);
        index=startIndex+1;
        while (Data.timestamp(index)<intervalStart+interval)
            index=index+1;
            if index>size(Data.timestamp)
                index=index-1;
                stop=true;
                break;
            end
        end
        startIndex=index;
    end
    dlmwrite(fullfile(data_dir,"ccc\",Data.fname),ccc,'precision', 13);
end
fprintf("Finished\n");