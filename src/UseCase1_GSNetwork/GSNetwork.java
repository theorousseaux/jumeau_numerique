package src.UseCase1_GSNetwork;

import src.Kalman.Station;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;


public class GSNetwork {
    
    String Name;
    List<Station> Network;


    public GSNetwork() throws NumberFormatException, IOException{
        List<String> GSList = new ArrayList<>(); // list of stations in the network
        List<String> stationsList = new ArrayList<>();  //list of available stations in DB


        // creation of list of available stations
        BufferedReader br = new BufferedReader(new FileReader("/home/eliott/Documents/4A/COS/PIE/jumeau_numerique/src/UseCase1_GSNetwork/GS.csv"));
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
                    System.out.println(station);
                }
            }else if(!stationsList.contains(stationName)){
                System.out.println("Invalid name");            
            }else{
                GSList.add(stationName);
            }
        }while(end==false);//loops until validData is true

        System.out.println("Stations saved!");

        BufferedReader br2 = new BufferedReader(new FileReader("/home/eliott/Documents/4A/COS/PIE/jumeau_numerique/src/UseCase1_GSNetwork/GS.csv"));
        
        while ((line = br2.readLine()) != null)
        {
            // Retourner la ligne dans un tableau
            String[] data = line.split(",");
            if (GSList.contains(data[0])){
                this.Network.add(new Station(data[0], Double.parseDouble(data[1]), Double.parseDouble(data[2])));
            }
        }
        br2.close();
        scan.close();

    }


}
