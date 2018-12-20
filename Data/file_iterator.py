import os

from static_segmentation import static_segment
from kinetic_segmentation import kinetic_segment
from static_reaction import static_react
#all methods have three parameters(date,fov,filename)

date="28-11"
for f in os.listdir(date+"/Ganglion"):
    if f.endswith(".txt") and f.startswith("k"):
        kinetic_segment(date, int(f[2:4]), f)
    if f.endswith(".txt") and f.startswith("s"):
        static_segment(date, int(f[2:4]), f)

