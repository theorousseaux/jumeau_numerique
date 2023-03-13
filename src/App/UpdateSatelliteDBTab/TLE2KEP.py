import numpy as np
import matplotlib.pyplot as plt
from sympy.solvers import nsolve
from sympy import Symbol
from sympy import cos, sin, tan, sqrt
import math
import sys
from tqdm import tqdm

# Utility functions


def Kepler3(T):
    K = 4*np.pi**2/(G*M)
    a = (T**2/K)**(1/3)
    return a


G = 6.67430e-11  # gravitational constant
M = 5.9722e24  # earth mass
R = 6400  # earth radius (km)

sourcefile = open(sys.argv[1], 'r')
outputfile = open(sys.argv[2], 'w')
Lines = sourcefile.readlines()
N = len(Lines)  # number of objects

A = []  # list of semi major axis
E = []  # list of eccentricity
I = []  # list of inclinations
RAAN = []  # list of raan
ArgP = []  # list of perigee arguments
MA = []  # list of mean anomalies
Time = []  # list of epoch


# we fetch the orbital parameters from TLES


for j in tqdm(range(int(N/2))):
    line1 = Lines[2*j]
    line2 = Lines[2*j+1]
    e = float("."+line2[26:33])  # orbit exentricity
    T = 3600*24/float(line2[52:63])  # revolution period in s
    i = float(line2[8:16])*np.pi/180
    raan = float(line2[17:25])*np.pi/180
    argP = float(line2[17:25])*np.pi/180
    ma = float(line2[43:51])*np.pi/180
    time = float(line1[18:32])
    a = Kepler3(T)
    A.append(a)
    E.append(e)
    I.append(i)
    RAAN.append(raan)
    ArgP.append(argP)
    MA.append(ma)
    Time.append(time)
    outputfile.write(line1[2:7]+',')  # id_sat
    outputfile.write(str(a)+',')
    outputfile.write(str(e)+',')
    outputfile.write(str(i)+',')
    outputfile.write(str(raan)+',')
    outputfile.write(str(argP)+',')
    outputfile.write(str(ma)+',')
    outputfile.write(str(time))
    outputfile.write('\n')

outputfile.close()
sourcefile.close()
