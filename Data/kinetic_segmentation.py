
date="22-11"
fov=30
f="k_30_r_andrew.txt"

def kinetic_segment(date,fov,f):
    out=320
    #in is out+30
    Event=date+"/Perimetry/"+f
    EEG=date+"/Ganglion/"+f
    output=date+"/Segmented/"+f
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
        eventstart=events[eventcounter].split(",")[0]
        eventend=0
        while eventcounter<len(events):
            if "SPACE" in events[eventcounter]:
                eventend=events[eventcounter].split(",")[0]
                break
            xDegree=float(events[eventcounter].split(",")[3][2:])
            yDegree=float(events[eventcounter].split(",")[4][1:-2])
            if fov*fov>=xDegree*xDegree+yDegree*yDegree:
                eventend=events[eventcounter].split(",")[0]
                break
            eventcounter+=1
        #now eventstart is timestamp when dot appears at corner, and eventend when the user should start seeing the dot
        eventremove=0
        while eventcounter<len(events):
            if "Remove" in events[eventcounter]:
                eventremove=events[eventcounter].split(",")[0]
                break
            eventcounter+=1
        #now eventremove is timestamp when dot is removed
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
            outputFile.write(str(out)+", "+eegs[eegcounter].split(",")[1]+", "+eegs[eegcounter].split(",")[2]+", "+eegs[eegcounter].split(",")[3]+"\n")
            eegcounter+=1
            eegend=eegs[eegcounter].split(",")[9]
        while eegcounter<len(eegs)-1:
            if int(eegend)>int(eventremove):
               break
            outputFile.write(str(out+30)+", "+eegs[eegcounter].split(",")[1]+", "+eegs[eegcounter].split(",")[2]+", "+eegs[eegcounter].split(",")[3]+"\n")
            eegcounter+=1
            eegend=eegs[eegcounter].split(",")[9]
        out+=1
        outputFile.close()

if __name__ == "__main__":
    kinetic_segment(date,fov,f)