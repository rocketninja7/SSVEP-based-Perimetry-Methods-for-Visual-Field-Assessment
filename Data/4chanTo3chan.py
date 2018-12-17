
date="22-11"
file="s_20_r_andrew_raw.txt"

EEG=date+"/Ganglion/"+file
output=date+"/Ganglion/"+file[:-8]+".txt"

eegFile=open(EEG,"r")
outputFile=open(output,"w")
eegFile=open(EEG,"r")
eegs=eegFile.readlines()
for i in range(0,6):
    outputFile.write(eegs[i])
for i in range(6,len(eegs)-1):
    foreheadLeft=float(eegs[i].split(",")[1])
    foreheadRight=float(eegs[i].split(",")[2])
    OLeft=eegs[i].split(",")[3]
    ORight=eegs[i].split(",")[4]
    outputFile.write(eegs[i].split(",")[0]+","+OLeft+","+ORight+", "+"%0.2f" %((foreheadLeft+foreheadRight)/2)+","+eegs[i].split(",")[4]+","+eegs[i].split(",")[5]+","+eegs[i].split(",")[6]+","+eegs[i].split(",")[7]+","+eegs[i].split(",")[8]+","+eegs[i].split(",")[9])