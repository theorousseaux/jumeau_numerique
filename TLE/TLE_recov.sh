touch cookies.txt
curl -c cookies.txt -b cookies.txt https://www.space-track.org/ajaxauth/login -d 'identity=eliotty77@hotmail.fr&password=730ElJ77976EbK77'
echo "Connecting to spacetrack.org"
echo "Source file (will be removed)"
read sourceFile
echo "Destination file for TLE"
read TLEFile
echo "Destination file for PV"
read pvFile
curl --limit-rate 100K --cookie cookies.txt https://www.space-track.org/basicspacedata/query/class/gp/EPOCH/%3Enow-30/orderby/NORAD_CAT_ID,EPOCH/format/3le > $sourceFile

python3 3LE2TLE.py $sourceFile $TLEFile
rm $sourceFile
python3 TLE2PV.py $TLEFile $pvFile
rm cookies.txt
echo "Data collected"
