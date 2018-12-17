import os

from static_segmentation import static_segment
from kinetic_segmentation import kinetic_segment
from static_reaction import static_react
#all methods have three parameters(date,fov,filename)

date="28-11"
for file in os.listdir(date+"/Ganglion"):
    if file.endswith(".txt") and file.startswith("k"): #file.startswith("k")
        print(file)
        kinetic_segment(date, int(file[2:4]),file)

