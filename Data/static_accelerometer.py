import math

date="22-11"
fov=20
file="s_20_l_andrew.txt"

a=0.5 #constant for smoothening data

def static_accelerometer(date,fov,file):
    Event=date+"/Perimetry/"+file
    EEG=date+"/Ganglion/"+file
    output=date+"/Motion/"+file
    
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
        lastY=0
        lastX=0
        lastZ=0
        while eegcounter<len(eegs)-1:
            if int(eegend)>int(eventend):
                break
            lastX=a*float(eegs[eegcounter].split(",")[5])+(1-a)*lastX
            lastY=a*float(eegs[eegcounter].split(",")[6])+(1-a)*lastY
            lastZ=a*float(eegs[eegcounter].split(",")[7])+(1-a)*lastZ
            pitch=math.atan(lastY/math.sqrt(pow(lastX, 2)+pow(lastZ, 2)))
            yaw=0
            outputFile.write(label+", "+"%0.5f" %(pitch)+", "+"%0.5f" %(yaw)+"\n")
            eegcounter+=20 #acceleromter is 10Hz, so changes once every 20 lines, cos eeg is 200Hz
            eegend=eegs[eegcounter].split(",")[9]

if __name__ == "__main__":
    static_accelerometer(date,fov,file)