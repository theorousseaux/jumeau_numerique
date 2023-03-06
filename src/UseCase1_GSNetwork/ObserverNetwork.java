package src.UseCase1_GSNetwork;

import src.Kalman.TelescopeAzEl;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import org.orekit.estimation.measurements.AngularAzEl;


import org.orekit.estimation.measurements.ObservedMeasurement;


public class ObserverNetwork {
	
	private String name;
	
	private List<TelescopeAzEl> telescopesNetworkList;

    public ObserverNetwork(String name, List<String> telescopesNames, List<TelescopeAzEl> telescopesList) {
    	
    	this.name = name;
    	 
        List<TelescopeAzEl> telescopesNetworkList = new ArrayList<>(); // list of telescopes in the network
        
        for (String telescopeName : telescopesNames) {
        	for (TelescopeAzEl telescope : telescopesList) {
        		if (telescope.getID().equals(telescopeName)) {
        			telescopesNetworkList.add(telescope);
        			break;
        		}
        	}
        }
        this.telescopesNetworkList = telescopesNetworkList;
    }


    public String getName() {
        return this.name;
    }

    public List<TelescopeAzEl> getNetwork() {
        return this.telescopesNetworkList;
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

}
