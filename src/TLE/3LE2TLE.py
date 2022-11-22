import sys

file = open(sys.argv[2], 'w')
sourcefile = open(sys.argv[1], 'r')
Lines = sourcefile.readlines()


N = len(Lines)
for i in range(int(N/3)):
    line1 = Lines[3*i+1]
    line2 = Lines[3*i+2]
    file.write(line1)
    file.write(line2)
file.close()
sourcefile.close()