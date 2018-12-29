
date="28-11"
fov=0
file="s_20_r_kyle.txt"

def static_events(date,fov,file):
    Event=date+"/Perimetry/"+file
    output=date+"/Events/"+file
    eventFile=open(Event,"r")
    outputFile=open(output,"w")
    events=eventFile.read().split(";")
    eventcounter=0
    outputFile.write(events[0].split(",")[0]+",0\n")
    while eventcounter<len(events)-1:
        eventcounter+=1
        if "Add" not in events[eventcounter]:
            continue
        addTime=events[eventcounter].split(",")[0]
        xDegree=float(events[eventcounter].split(",")[3][2:])
        yDegree=float(events[eventcounter].split(",")[4][1:-2])
        inFOV=fov*fov>=xDegree*xDegree+yDegree*yDegree
        eventstart=events[eventcounter].split(",")[0]
        eventend=0
        pressed=False
        removeTime=addTime
        while eventcounter<len(events):
            if "SPACE" in events[eventcounter]:
                pressed=True
            if "Remove" in events[eventcounter]:
                eventend=events[eventcounter].split(",")[0]
                removeTime=events[eventcounter].split(",")[0]
                break
            eventcounter+=1
        if inFOV or pressed:
            outputFile.write(addTime+",1\n")
            outputFile.write(removeTime+",0\n")

if __name__ == "__main__":
    static_events(date,fov,file)

