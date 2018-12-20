
date="22-11"
fov=20
file="s_20_l_andrew.txt"

def static_segment(date,fov,file):
    Event=date+"/Perimetry/"+file
    EEG=date+"/Ganglion/"+file
    output=date+"/Segmented/"+file
    
    inmiss=0
    inpress=80
    outpress=160
    outmiss=240
    
    eventFile=open(Event,"r")
    eegFile=open(EEG,"r")
    outputFile=open(output,"w")
    events=eventFile.read().split(";")
    eegs=eegFile.readlines();
    eventcounter=0
    eegcounter=6
    while eventcounter<len(events)-1:
        eventcounter+=1
        if "Add" not in events[eventcounter]:
            continue
        xDegree=float(events[eventcounter].split(",")[3][2:])
        yDegree=float(events[eventcounter].split(",")[4][1:-2])
        inFOV=fov*fov>=xDegree*xDegree+yDegree*yDegree
        eventstart=events[eventcounter].split(",")[0]
        eventend=0
        pressed=False
        while eventcounter<len(events):
            if "SPACE" in events[eventcounter]:
                pressed=True
            if "Remove" in events[eventcounter]:
                eventend=events[eventcounter].split(",")[0]
                break
            eventcounter+=1
        label="000"
        if inFOV:
            if pressed:
                label="%03d" % inpress
                inpress+=1
            else:
                label="%03d" % inmiss
                inmiss+=1
        else:
            if pressed:
                label="%03d" % outpress
                outpress+=1
            else:
                label="%03d" % outmiss
                outmiss+=1
        #outputFile.write("Add: "+eventstart+", Removed: "+eventend+"\n")
        eegstart=eegs[eegcounter].split(",")[9]
        while eegcounter<len(eegs)-1:
            if int(eegstart)>=int(eventstart):
                break
            eegcounter+=1
            eegstart=eegs[eegcounter].split(",")[9]
        eegend=eegstart
        while eegcounter<len(eegs)-1:
            if int(eegend)>int(eventend):
               break
            outputFile.write(label+", "+eegs[eegcounter].split(",")[1]+", "+eegs[eegcounter].split(",")[2]+", "+eegs[eegcounter].split(",")[3]+"\n")
            eegcounter+=1
            eegend=eegs[eegcounter].split(",")[9]
    outputFile.close()

if __name__ == "__main__":
    static_segment(date,fov,file)