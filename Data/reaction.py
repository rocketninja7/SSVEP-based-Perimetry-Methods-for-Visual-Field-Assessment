import numpy as np

defdate="28-11"
deffov=40
deff="s_40_r_mathan.txt"

def react(date,fov,f):
    Event=date+"/Perimetry/"+f
    output=date+"/Reaction/"+f
    sork=f[0]
    
    miss=0
    pressOutFOV=0
    reactions=[]
    
    eventFile=open(Event,"r")
    outputFile=open(output,"w")
    events=eventFile.read().split(";")
    eventcounter=0
    while eventcounter<len(events)-1:
        eventcounter+=1
        if "Add" not in events[eventcounter]:
            continue
        xDegree=float(events[eventcounter].split(",")[3][2:])
        yDegree=float(events[eventcounter].split(",")[4][1:-2])
        inFOV=fov*fov>=xDegree*xDegree+yDegree*yDegree
        eventstart=(int)(events[eventcounter].split(",")[0])
        pressed=0
        while eventcounter<len(events):
            if "SPACE" in events[eventcounter]:
                pressed=(int)(events[eventcounter].split(",")[0])
                break
            if "Remove" in events[eventcounter]:
                break
            eventcounter+=1
        if sork == "s":
            if not pressed==0:
                reactions.append(pressed-eventstart)
                if not inFOV:
                    pressOutFOV+=1
            if inFOV and pressed==0:
                miss+=1
    if sork == "s":
        outputFile.write("Total points= 80\n")
        outputFile.write("Points pressed= "+str(len(reactions))+", of which "+str(pressOutFOV)+" are out of FOV\n")
        outputFile.write("Points in FOV but missed= "+str(miss)+"\n")
    if sork == "k":
        outputFile.write("Total points= 60\n")
    outputFile.write("Average reaction time= "+str(np.mean(reactions))+"ms\n\n")
    print("Average reaction time= "+str(np.mean(reactions))+"ms")
    for reaction in reactions:
        outputFile.write(str(reaction)+"\n")
    outputFile.close()
    
if __name__ == "__main__":
    static_react(defdate,deffov,deff)