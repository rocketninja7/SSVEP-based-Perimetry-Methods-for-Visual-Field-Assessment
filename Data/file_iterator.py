import os

from kinetic_accelerometer import kinetic_accelerometer
from static_accelerometer import static_accelerometer
from static_segmentation import static_segment
from kinetic_segmentation import kinetic_segment
from reaction import react
from prune_data import prune_data
#all methods have three parameters(date,fov,filename)

date="06-12"
for f in os.listdir(date+"/Ganglion"):
    if f.endswith(".txt") and f.startswith("k"):
        kinetic_segment(date, int(f[2:4]), f)
        kinetic_accelerometer(date, int(f[2:4]), f)
    if f.endswith(".txt") and f.startswith("s"):
        static_segment(date, int(f[2:4]), f)
        static_accelerometer(date, int(f[2:4]), f)
    react(date, int(f[2:4]), f)
    prune_data(date, int(f[2:4]), f)