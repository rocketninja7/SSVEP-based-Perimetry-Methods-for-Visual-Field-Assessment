function xmFeature = ExtractBandPowers(xm_filtered, para)
    assert(para.nband == size(xm_filtered,3), 'Mismatched in # bands');
    numChannel = size(xm_filtered,2);
    xmFeature = zeros(1,para.nband * numChannel);      %create 2D array    
%     fMax = max(max(rawEegData(:)));
%     if(fMax >para.EEGThresh)
%         return;
%     end
    %TODO Compute features into sub-segments for features aggregation
    xwinFeature1 = zeros(numChannel, para.nband);
    for iCh = 1: numChannel
        %TODO
        xwinFeature1(iCh,:) = reshape(sum(xm_filtered(:,iCh,:).^2), 1, size(xm_filtered,3)); 
        xwinFeature1(iCh,:) = xwinFeature1(iCh,:)/sum(squeeze(xwinFeature1(iCh,:)));  %CTG: relative power   
    end
    % Concatenation
    iFeat = 1;
    for j =1:numChannel
        for m=1:para.nband
            xmFeature(1,iFeat) = xwinFeature1(j,m);
            iFeat = iFeat + 1;
        end
    end
end