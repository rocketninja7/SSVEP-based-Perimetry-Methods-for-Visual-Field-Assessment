function data = SegmentKineticData(fData, k_id, k_size,fname)
    data.classes = {'OutsideFOV','WithinFOV'};
    data.fname=fname;
    data.stimId = [];
    data.classId = []; 
    eegIdx = 1;
    for id=1 : length(k_id)
        idx = find(fData(:,1)==k_id(id));
        if ~isempty(idx)
            if id <=  k_size(1)
                data.eegByClasses{1, id} = fData(idx,2:4);
                data.classId = [data.classId 1];
            else
                data.eegByClasses{2, id-k_size(1)} = fData(idx,2:4);
                data.classId = [data.classId 2];
            end
            data.eeg{ eegIdx } = fData(idx,2:4);
            data.stimId = [data.stimId id];
            eegIdx = eegIdx + 1;
        end
    end
end