date="16-12"
fov=30
file="k_30_l_mathan.txt"

Event=date+"/Perimetry/"+file
EEG=date+"/Ganglion/"+file
output=date+"/Segmented/"+file

out=0
#in is out+30

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
    outeventend=0
    while eventcounter<len(events):
        if "SPACE" in events[eventcounter]:
            outeventend=events[eventcounter].split(",")[0]
            eventcounter+=1
            continue
        xDegree=float(events[eventcounter].split(",")[3][2:])
        yDegree=float(events[eventcounter].split(",")[4][1:-2])
        if fov*fov>=xDegree*xDegree+yDegree*yDegree:
            eventend=events[eventcounter].split(",")[0]
            break
        eventcounter+=1
    #now eventstart is timestamp when dot appears at corner, and eventend when the user should start seeing the dot
    eventremove=0
    ineventend=0
    while eventcounter<len(events):
        if "SPACE" in events[eventcounter] and ineventend==0:
            ineventend=events[eventcounter].split(",")[0]
        if "Remove" in events[eventcounter]:
            eventremove=events[eventcounter].split(",")[0]
            break
        eventcounter+=1
    if ineventend==0 and outeventend>0:
        eventend=outeventend
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