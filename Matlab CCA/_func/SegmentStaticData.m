function data = SegmentStaticData(fData, s_id, s_size,fname)
    data.classes = {'WithinFOV_miss', 'WithinFOV_press','OutsideFOV_press','OutsideFOV_miss'};
    data.fname=fname;
    data.stimId = [];
    data.classId = []; 
    eegIdx = 1;
    for id=1 : length(s_id)
        idx = find(fData(:,1)==s_id(id));
        if ~isempty(idx)
            if id <=  s_size(1)
                data.eegByClasses{1, id} = fData(idx,2:4);
                data.classId = [data.classId 1];
            elseif id > s_size(1) && id <= sum(s_size(1:2))
                data.eegByClasses{2, id-s_size(1)} = fData(idx,2:4);
                data.classId = [data.classId 2];
            elseif id > sum(s_size(1:2)) && id <= sum(s_size(1:3))
                data.eegByClasses{3, id-sum(s_size(1:2))} = fData(idx,2:4);
                data.classId = [data.classId 2];
            else
                data.eegByClasses{4, id-sum(s_size(1:3))} = fData(idx,2:4);
                data.classId = [data.classId 1];
            end
            data.eeg{eegIdx} = fData(idx,2:4);
            data.stimId = [data.stimId id];
            eegIdx = eegIdx + 1;
        end
    end
end