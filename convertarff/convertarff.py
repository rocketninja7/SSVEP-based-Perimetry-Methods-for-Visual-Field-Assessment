import os

fileExt = "cccdata/"
for f in os.listdir(fileExt):
    outputFile = open(f[0:-5]+".arff", "w")
    outputFile.write("@relation CCC\n")
    outputFile.write("@attribute '0.5s' real\n")
    outputFile.write("@attribute '1.0s' real\n")
    outputFile.write("@attribute '1.5s' real\n")
    outputFile.write("@attribute '2.0s' real\n")
    outputFile.write("@attribute '2.5s' real\n")
    outputFile.write("@attribute '3.0s' real\n")
    outputFile.write("@attribute 'event' {1,2}\n")
    outputFile.write("@data\n")
    outputFile.write(open(fileExt+f, "r").read())
    outputFile.close()