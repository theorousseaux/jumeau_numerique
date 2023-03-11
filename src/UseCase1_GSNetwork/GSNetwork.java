package src.UseCase1_GSNetwork;

import src.Kalman.Station;
import src.Kalman.TelescopeAzEl;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.SortedSet;
import org.orekit.estimation.measurements.AngularAzEl;


import org.orekit.estimation.measurements.ObservedMeasurement;

import src.Data.GS.ReadGSFile;


public class GSNetwork {
    
    String Name;
    List<Station> Network;

    public GSNetwork() throws NumberFormatException, IOException{
        List<String> GSList = new ArrayList<>(); // list of stations in the network
        List<String> stationsList = new ArrayList<>();  //list of available stations in DB


        // creation of list of available stations
        BufferedReader br = new BufferedReader(new FileReader("src/Data/GS/GS.csv"));
        String line;
        while ((line = br.readLine()) != null)
        {
            // Retourner la ligne dans un tableau
            String[] data = line.split(",");
            if (!data[0].equals("name")){
                stationsList.add(data[0]);
            }
        }

        Collections.sort(stationsList);  
        br.close();

        Scanner scan = new Scanner(System.in);
        boolean end = false;
        boolean validName = false;
        String stationName = "";


        System.out.println("Enter the name of the network:");
        do{
            try{
                this.Name = scan.next();
                validName = true;
            }catch(InputMismatchException e){
                //executes when this exception occurs
                System.out.println("Invalid name");
            }
        }while(validName==false);
        do{
            System.out.println("Enter a Station name (type d to display all available stations, done to exit)");
            try{
                stationName = scan.next();//tries to get data. Goes to catch if invalid data
            }catch(InputMismatchException e){
                //executes when this exception occurs
                System.out.println("Invalid name");
            }
            if(stationName.equals("done")){
                end = true;
            }else if(stationName.equals("d")){
                for (String station : stationsList){
                    if (!(GSList.contains(station))){
                    System.out.println(station);
                    }
                }
            }else if(!stationsList.contains(stationName)){
                System.out.println("Invalid name");            
            }else{
                GSList.add(stationName);
            }
        }while(end==false);//loops until validData is true
        System.out.println("Stations saved!");
        
        ReadGSFile networkBuilder = new ReadGSFile();
        this.Network = networkBuilder.readStation("src/Data/GS/GS.csv", GSList);
        
    }

    public GSNetwork(String name, List<String> stations) throws NumberFormatException, IOException{
        List<String> GSList = new ArrayList<>(); // list of stations in the network
        List<String> stationsList = new ArrayList<>();  //list of available stations in DB


        // creation of list of available stations
        BufferedReader br = new BufferedReader(new FileReader("src/Data/GS/GS.csv"));
        String line;
        while ((line = br.readLine()) != null)
        {
            // Retourner la ligne dans un tableau
            String[] data = line.split(",");
            if (!data[0].equals("name")){
                stationsList.add(data[0]);
            }
        }

        Collections.sort(stationsList);  
        br.close();

        this.Name = name;
        ReadGSFile networkBuilder = new ReadGSFile();
        this.Network = networkBuilder.readStation("src/Data/GS/GS.csv", stations);
        this.addTelescopes();
    }

    public void display(){
        System.out.println("Network name: " + this.getName());
        System.out.println("Network stations: ");
        for (Station station : this.getNetwork()){
            System.out.println("    - " + station.getName());
        }
    }


    public String getName() {
        return Name;
    }


    public void setName(String name) {
        Name = name;
    }


    public List<Station> getNetwork() {
        return Network;
    }


    public void setNetwork(List<Station> network) {
        Network = network;
    }

    public void addTelescopes(){
        for (Station station : this.Network){

            station.addTelescope(new TelescopeAzEl("default",new double[]{0.,0.}, new double[]{0.3*Math.PI/180, 0.3*Math.PI/180}, 30*Math.PI/180, 119*Math.PI/180, 10, 10, station, true));

            //station.addTelescope(new TelescopeAzEl("ID", new double[]{0.,0.}, new double[]{0.3*Math.PI/180, 0.3*Math.PI/180}, 30*Math.PI/180, 119*Math.PI/180, 10, 10));

        }
    }

    public List<TelescopeAzEl> getTelescopes(){
        List<TelescopeAzEl> netTelescopes = new ArrayList<>();
        for (Station station : this.getNetwork()){
            for(TelescopeAzEl telescope : station.getListTelescope()){
                netTelescopes.add(telescope);
            }
        }
        return netTelescopes;

    }

    public int countObservations(List<SortedSet<ObservedMeasurement<?>>> observations){
        int i = 0;
        for (SortedSet<ObservedMeasurement<?>> object : observations){
            for (ObservedMeasurement<?> obs : object){
                if (this.getNetwork().contains(((AngularAzEl) obs).getStation())){
                    i+=1;
                }
            }
        }
        return i;
    }

    public List<String> getNames(){
        List<String> list = new ArrayList<String>();
        for (Station station : this.getNetwork()){
            list.add(station.getName());
        }
        return list;
    }

}
