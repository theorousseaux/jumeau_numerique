touch cookies.txt
curl -c cookies.txt -b cookies.txt https://www.space-track.org/ajaxauth/login -d 'identity=eliotty77@hotmail.fr&password=730ElJ77976EbK77'
echo "Connecting to spacetrack.org"
curl --limit-rate 100K --cookie cookies.txt https://www.space-track.org/basicspacedata/query/class/gp/EPOCH/%3Enow-30/orderby/NORAD_CAT_ID,EPOCH/format/3le > 'spacetrackrecov.txt'

python3 3LE2TLE.py 'spacetrackrecov.txt' 'TLEFile.txt'

python3 TLE2PV.py 'TLEFile.txt' 'PV.txt' 'orbParam.txt'

python3 setSM.py 'PV.txt' 'PVwSM.txt'
echo "Data collected"