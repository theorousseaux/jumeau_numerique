import random as rd
import sys

mu = 100
sigma = 10

sourcefile = open(sys.argv[1], 'r')
outputfile = open(sys.argv[2],'w')


for line in sourcefile.readlines():
    outputfile.write(line.replace('\n',', '+str(rd.gauss(mu,sigma))+'\n'))

sourcefile.close()
outputfile.close()