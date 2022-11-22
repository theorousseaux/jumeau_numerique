import numpy as np
import matplotlib.pyplot as plt
from sympy.solvers import nsolve
from sympy import Symbol
from sympy import cos,sin,tan,sqrt
import math
import sys

### Utility functions


def Kepler3(T):
    K = 4*np.pi**2/(G*M)
    a = (T**2/K)**(1/3)
    return a


def Spherical2xyz(a, e, i, raan, ma, ap):
    p = a*(1-e**2)
    ea = M2E(ma, e)
    theta = E2nu(ea, e)
    r = p/(1+e*cos(theta))*np.array([[cos(theta)], [sin(theta)], [0]])
    v = sqrt(G*M/p)*np.array([[-sin(theta)], [e+cos(theta)], [0]])
    M1 = np.matrix([[cos(ap), -sin(ap), 0],
                   [sin(ap), cos(ap), 0],
                   [0, 0, 1]])
    M2 = np.matrix([[1, 0, 0],
                   [0, cos(i), -sin(i)],
                   [0, sin(i), cos(i)]])

    M3 = np.matrix([[cos(raan), -sin(raan), 0],
                   [sin(raan), cos(raan), 0],
                   [0, 0, 1]])

    X = np.dot(M3,np.dot(M2,np.dot(M1,r)))
    V = np.dot(M3,np.dot(M2,np.dot(M1,v)))
    return X,V


def M2E(AM, e):
    x = Symbol('x')
    sol = (nsolve(x - e*sin(x)-AM, x, 0))
    return float(sol)


def E2nu(AE, e):
    # return 2*np.arctan(tan(AE/2)*sqrt((1+e)/(1-e)))
    sol =  tan(AE/2)*sqrt((1+e)/(1-e))
    return 2*math.atan(sol)


G = 6.67430e-11  # gravitational constant
M = 5.9722e24  # earth mass
R = 6400  # earth radius (km)

sourcefile = open(sys.argv[1], 'r')
outputfile = open(sys.argv[2],'w')
Lines = sourcefile.readlines()
N= len(Lines) #number of objects

A = [] #list of semi major axis
E = [] #list of eccentricity
I = [] #list of inclinations
RAAN = [] #list of raan
ArgP = [] #list of perigee arguments
MA = [] #list of mean anomalies
Time = [] #list of epoch


### we fetch the orbital parameters from TLES


for j in range(int(N/2)):
    if (j%10 == 0):
        print('\r'+str(round(2*j/N*100,1))+'%')
    line1 = Lines[2*j]
    line2 = Lines[2*j+1]
    #line = list(map(float, line))
    e = float("."+line2[26:33]) #orbit exentricity
    T = 3600*24/float(line2[52:63]) #revolution period in s
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
    X,V = Spherical2xyz(a,e,i,raan,ma,argP)
    for x in X:
        outputfile.write(str(x[0,0])+',')
    for v in V:
        outputfile.write(str(v[0,0])+',')
    outputfile.write(str(time))
    outputfile.write('\n')

outputfile.close()
sourcefile.close()