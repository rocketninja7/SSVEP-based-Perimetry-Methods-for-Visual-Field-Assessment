import os

from static_segmentation import static_segment
from kinetic_segmentation import kinetic_segment
from static_reaction import static_react
from static_accelerometer import static_accelerometer
from kinetic_accelerometer import kinetic_accelerometer
#all methods have three parameters(date,fov,filename)

date="22-11"
for file in os.listdir(date+"/Ganglion"):
    if file.endswith(".txt") and file.startswith("s"): #file.startswith("k")
        print(file)
        static_accelerometer(date, int(file[2:4]),file)

