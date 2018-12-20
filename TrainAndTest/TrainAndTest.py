import os

def arff_header(outputFile):
    outputFile.write("@relation CCC\n")
    outputFile.write("@attribute '0.5s' real\n")
    outputFile.write("@attribute '1.0s' real\n")
    outputFile.write("@attribute '1.5s' real\n")
    outputFile.write("@attribute '2.0s' real\n")
    outputFile.write("@attribute '2.5s' real\n")
    outputFile.write("@attribute '3.0s' real\n")
    outputFile.write("@attribute 'event' {1,2}\n")
    outputFile.write("@data\n")

sData = []
kData = []
fileExt = "cccdata/"
for f in os.listdir(fileExt):
    if f[0] == "s":
        sData.append(open(fileExt+f, "r"))
        sData[-1]=sData[-1].read()
    if f[0] == "k":
        kData.append(open(fileExt+f, "r"))
        kData[-1]=kData[-1].read()

"""
counter=0
for i in sData:
    outputFile = open("staticTrain"+counter+".arff", "a")
    arff_header(outputFile)
    for j in sData:
        if i is not j:
            outputFile.write(j)
    outputFile.close()
    outputFile = open("staticTest"+counter+".arff", "a")
    arff_header(outputFile)
    outputFile.write(i)
    outputFile.close()
    counter += 1
counter=0
for i in kData:
    outputFile = open("kineticTrain"+counter+".arff", "a")
    arff_header(outputFile)
    for j in kData:
        if i is not j:
            outputFile.write(j)
    outputFile.close()
    outputFile = open("kineticTest"+counter+".arff", "a")
    arff_header(outputFile)
    outputFile.write(i)
    outputFile.close()
    counter += 1    
"""

outputFile = open("staticData"+".arff", "a")
arff_header(outputFile)
for i in sData:
    outputFile.write(i)
outputFile.close()
    
outputFile = open("kineticData"+".arff", "a")
arff_header(outputFile)
for i in kData:
    outputFile.write(i)    
outputFile.close()