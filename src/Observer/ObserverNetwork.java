package src.Observer;

import java.util.ArrayList;
import java.util.List;


public class ObserverNetwork {

    private final String name;

    private final List<TelescopeAzEl> telescopesNetworkList;

    public ObserverNetwork ( String name , List<String> telescopesNames , List<TelescopeAzEl> telescopesList ) {

        this.name = name;

        List<TelescopeAzEl> telescopesNetworkList = new ArrayList<> ( ); // list of telescopes in the network

        for (String telescopeName : telescopesNames) {
            for (TelescopeAzEl telescope : telescopesList) {
                if (telescope.getID ( ).equals ( telescopeName )) {
                    telescopesNetworkList.add ( telescope );
                    break;
                }
            }
        }
        this.telescopesNetworkList = telescopesNetworkList;
    }


    public String getName ( ) {
        return this.name;
    }

    public List<TelescopeAzEl> getTelescopes ( ) {
        return this.telescopesNetworkList;
    }

    public void display ( ) {
        System.out.println ( "Network name: " + this.getName ( ) );
        System.out.println ( "Network telescopes: " );
        for (TelescopeAzEl telescope : this.getTelescopes ( )) {
            System.out.println ( "    - " + telescope.getID ( ) );
        }
    }

}
