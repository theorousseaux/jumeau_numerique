curl -c cookies.txt -b cookies.txt https://www.space-track.org/ajaxauth/login -d 'identity=eliotty77@hotmail.fr&password=730ElJ77976EbK77' 2>&1
echo "Connecting to spacetrack.org" >&2
echo "" >&2

curl --limit-rate 100K --cookie cookies.txt https://www.space-track.org/basicspacedata/query/class/gp/EPOCH/%3Enow-30/orderby/NORAD_CAT_ID,EPOCH/format/3le > 'src/TLE/spacetrackrecov.txt'

python3 src/TLE/3LE2TLE.py 'src/TLE/spacetrackrecov.txt' 'src/TLE/TLEFile.txt'

python3 src/TLE/TLE2PV.py 'src/TLE/TLEFile.txt' 'src/TLE/PV.txt' 'src/TLE/orbParam.txt'

python3 src/TLE/setSM.py 'src/TLE/PV.txt' 'src/TLE/PVwSM.txt'

rm cookies.txt
rm src/TLE/spacetrackrecov.txt

echo "Data collected" >&2
