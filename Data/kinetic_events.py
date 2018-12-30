
date="28-11"
fov=0
file="k_20_r_kyle.txt"

def kinetic_events(date,fov,file):
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
        outputFile.write(str(int(eventend)-1)+",0\n")
        outputFile.write(eventend+",1\n")
        outputFile.write(str(int(eventremove)-1)+",1\n")
        outputFile.write(eventremove+",0\n")

if __name__ == "__main__":
    kinetic_events(date,fov,file)

