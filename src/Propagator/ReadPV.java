package Propagator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReadPV {

    public List<String[]> readCSV() throws Exception {
        List<String[]> tleList = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader("TLE/pv.csv"));
        String line = null;
        while ((line = br.readLine()) != null)
        {
            // Retourner la ligne dans un tableau
            String[] data = line.split(",");
            tleList.add(data);
        }
        br.close();
        for (String[] tle : tleList){
            System.out.println(Arrays.toString(tle));
        }

        return tleList;
    }

    public static void main(String[] args) throws Exception
    {
        ReadPV readPV = new ReadPV();
        List<String[]> tle_list = readPV.readCSV();
        System.out.println(tle_list);
    }
}