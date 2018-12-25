def prune_data(date,fov,f):
    segmented=date+"/Segmented/"+f
    motion=date+"/Motion/"+f
    
    segmentFile=open(segmented, "r")
    motionFile=open(motion, "r")
    stableFile=open(date+"/Prune/Stable"+f, "w")
    unstableFile=open(date+"/Prune/Unstable"+f, "w")    
    
    segments=segmentFile.readlines()
    motions=motionFile.readlines()
    
    motioncounter=0
    segmentcounter=0
    label=int(motions[motioncounter].split(",")[0])
    maxipitch=-91
    minipitch=91
    maxiroll=-91
    miniroll=91
    while motioncounter < len(motions):
        if int(motions[motioncounter].split(",")[0]) != label:
            while int(segments[segmentcounter].split(",")[0]) == label:
                if (maxipitch-minipitch)*(maxipitch-minipitch)+(maxiroll-miniroll)*(maxiroll-miniroll)>=2*2:
                    unstableFile.write(segments[segmentcounter])
                else:
                    stableFile.write(segments[segmentcounter])
                segmentcounter += 1
            label = int(motions[motioncounter].split(",")[0])
            maxipitch=-91
            minipitch=91
            maxiroll=-91
            miniroll=91
        maxipitch = max(maxipitch, float(motions[motioncounter].split(",")[1]))
        minipitch = min(minipitch, float(motions[motioncounter].split(",")[1]))
        maxiroll = max(maxiroll, float(motions[motioncounter].split(",")[2]))
        miniroll = min(miniroll, float(motions[motioncounter].split(",")[2]))
        motioncounter += 1
    while segmentcounter < len(segments) and int(segments[segmentcounter].split(",")[0]) == label:
        if (maxipitch-minipitch)*(maxipitch-minipitch)+(maxiroll-miniroll)*(maxiroll-miniroll)>=2*2:
            unstableFile.write(segments[segmentcounter])
        else:
            stableFile.write(segments[segmentcounter])
        segmentcounter += 1
    stableFile.close()
    unstableFile.close()