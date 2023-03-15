curl -c cookies.txt -b cookies.txt https://www.space-track.org/ajaxauth/login -d 'identity=eliotty77@hotmail.fr&password=730ElJ77976EbK77' 2>&1
echo "Connecting to spacetrack.org" >&2
echo "" >&2

curl --limit-rate 100K --cookie cookies.txt https://www.space-track.org/basicspacedata/query/class/gp/EPOCH/%3Enow-30/orderby/NORAD_CAT_ID,EPOCH/format/3le > 'src/Satellites/spacetrackrecov.txt'

python3 src/Satellites/3LE2TLE.py 'src/Satellites/spacetrackrecov.txt' 'src/Satellites/tle.csv'

python3 src/Satellites/TLE2KEP.py 'src/Satellites/tle.csv' 'src/Satellites/spacetrackrecov.txt'

python3 src/Satellites/setSM.py 'src/Satellites/spacetrackrecov.txt' 'src/Satellites/tle.csv'

rm 'src/Satellites/spacetrackrecov.txt'
rm cookies.txt

echo "Data collected" >&2
