import math

date="22-11"
fov=30
file="k_30_r_andrew.txt"

a=0.5 #constant for smoothening data

def kinetic_accelerometer(date,fov,file):
    out=320
    #in is out+30
    Event=date+"/Perimetry/"+file
    EEG=date+"/Ganglion/"+file
    output=date+"/Motion/"+file
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
        lastY=0
        lastX=0
        lastZ=0
        while eegcounter<len(eegs)-1:
            if int(eegend)>int(eventend):
               break
            lastX=a*float(eegs[eegcounter].split(",")[5])+(1-a)*lastX
            lastY=a*float(eegs[eegcounter].split(",")[6])+(1-a)*lastY
            lastZ=a*float(eegs[eegcounter].split(",")[7])+(1-a)*lastZ
            pitch=math.atan(lastY/math.sqrt(lastX*lastX+lastZ*lastZ))*180/math.pi
            roll=0
            outputFile.write(str(out)+", "+"%0.5f" %(pitch)+", "+"%0.5f" %(roll)+"\n")
            eegcounter+=20 #acceleromter is 10Hz, so changes once every 20 lines, cos eeg is 200Hz
            eegend=eegs[eegcounter].split(",")[9]
        while eegcounter<len(eegs)-1:
            if int(eegend)>int(eventremove):
               break
            lastX=a*float(eegs[eegcounter].split(",")[5])+(1-a)*lastX
            lastY=a*float(eegs[eegcounter].split(",")[6])+(1-a)*lastY
            lastZ=a*float(eegs[eegcounter].split(",")[7])+(1-a)*lastZ
            pitch=math.atan(lastY/math.sqrt(pow(lastX, 2)+pow(lastZ, 2)))*180/math.pi
            roll=0
            outputFile.write(str(out+30)+", "+"%0.5f" %(pitch)+", "+"%0.5f" %(roll)+"\n")
            eegcounter+=20 #acceleromter is 10Hz, so changes once every 20 lines, cos eeg is 200Hz
            eegend=eegs[eegcounter].split(",")[9]
        out+=1

if __name__ == "__main__":
    kinetic_accelerometer(date,fov,file)
