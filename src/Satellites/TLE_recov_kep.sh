curl -c cookies.txt -b cookies.txt https://www.space-track.org/ajaxauth/login -d 'identity=eliotty77@hotmail.fr&password=730ElJ77976EbK77' 2>&1
echo "Connecting to spacetrack.org" >&2
echo "" >&2

curl --limit-rate 100K --cookie cookies.txt https://www.space-track.org/basicspacedata/query/class/gp/EPOCH/%3Enow-30/orderby/NORAD_CAT_ID,EPOCH/format/3le > 'src/TLE/spacetrackrecov.txt'

python3 src/TLE/3LE2TLE.py 'src/TLE/spacetrackrecov.txt' 'src/TLE/tle.csv'

python3 src/TLE/TLE2KEP.py 'src/TLE/tle.csv' 'src/TLE/spacetrackrecov.txt'

python3 src/TLE/setSM.py 'src/TLE/spacetrackrecov.txt' 'src/TLE/tle.csv'

rm 'src/TLE/spacetrackrecov.txt'
rm cookies.txt

echo "Data collected" >&2
