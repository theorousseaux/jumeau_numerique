import sys

file = open(sys.argv[2], 'w')
sourcefile = open(sys.argv[1], 'r')
Lines = sourcefile.readlines()
sourcefile.close()

N = 23702
for i in range(N):
    line1 = Lines[3*i+1].split()
    line2 = Lines[3*i+2].split()
    l = len(line1[1])

    satellitteNumberIn = (5-l)*"0" + line1[1][0:5]
    classificationIN = line1[1][-1]
    launchYearIn = line1[2][0:2]
    launchNumberIn = line1[2][2:5]
    launchPieceIn = line1[2][5]
    ephemerisTypeIn = line1[7]
    elementNumberIn = line1[8][0:3]
    epochIn = line1[3]
    meanMotionIn = line2[7][0:11]
    meanMotionFirstDerivateIn = line1[4]
    meanMotionSecondDerivateIn = line1[5]
    e = line2[4]
    i = line2[2]
    paIn = line2[5]
    raanIn = line2[3]
    meanAnomalyIn = line2[6]
    revolutionNumberatEpochIn = line2[7][11:16]
    bStarIn = line1[6]
    file.write(satellitteNumberIn+',' +
               classificationIN+',' +
               launchYearIn+',' +
               launchNumberIn+',' +
               launchPieceIn+',' +
               ephemerisTypeIn+',' +
               elementNumberIn+',' +
               epochIn+',' +
               meanMotionIn+',' +
               meanMotionFirstDerivateIn+',' +
               meanMotionSecondDerivateIn+',' +
               e+',' +
               i+',' +
               paIn+',' +
               raanIn+',' +
               meanAnomalyIn+',' +
               revolutionNumberatEpochIn+',' +
               bStarIn+'\n')
file.close()
