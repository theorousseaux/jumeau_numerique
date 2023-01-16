package src.GroundStation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;



import org.orekit.data.DataContext;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;


import org.hipparchus.geometry.euclidean.threed.Vector3D;

import org.orekit.geometry.fov.DoubleDihedraFieldOfView;

import org.orekit.propagation.events.GroundFieldOfViewDetector;





public class simulation_main {
    
    public static void main(String[] args) throws Exception {

            File orekitData = new File("lib/orekit-data-master");
    	    DataProvidersManager manager = DataContext.getDefault().getDataProvidersManager();
    	    manager.addProvider(new DirectoryCrawler(orekitData));
            
            Frame frame = FramesFactory.getEME2000();

            //cadrillage du ciel en azimuth,elevation
            /////////////////////////////////////////
            List<List<Integer>> azElskyCuadrilled  = new ArrayList<>();
            int cpt = 0;
            for (int elevation = 35; elevation <= 145; elevation+=10){
                cpt+=1;
                
                if (cpt%2 == 1){
                    
                    for (int azimuth = 5; azimuth <= 175; azimuth+=10) {
                        List<Integer> aePosition = new ArrayList<Integer>();
                        aePosition.add(azimuth);
                        aePosition.add(elevation);
                        azElskyCuadrilled.add(aePosition);
                    }
                }
    
                else {
                    for (int azimuth = 175; azimuth >= 5; azimuth-=10){
                        List<Integer> aePosition = new ArrayList<Integer>();
                        aePosition.add(azimuth);
                        aePosition.add(elevation);
                        azElskyCuadrilled.add(aePosition);
                    }
                }
            }
            System.out.println("coucou c'est le cadrillage’”");
            System.out.println(azElskyCuadrilled);
    
            //Liste des groundFieldofViewDetector 
            //////////////////////////
            List<Vector3D> vectorSkyCuadrilled  = new ArrayList<>();
            List<GroundFieldOfViewDetector> fovDetectorsList  = new ArrayList<>();
            
            Vector3D axis1 = new Vector3D(1,0,0);
            Vector3D axis2 = new Vector3D(0, Math.sqrt(2)/2, Math.sqrt(2)/2);
            
            for (int i = 0; i < azElskyCuadrilled.size(); i++){
                    
                List<Integer> aePosition = azElskyCuadrilled.get(i);
                int azimuth = aePosition.get(0);
                int elevation = aePosition.get(1);
    
                Vector3D vectorCenter = new Vector3D(azimuth*Math.PI/180, elevation*Math.PI/180);
                DoubleDihedraFieldOfView fov = new DoubleDihedraFieldOfView(vectorCenter, axis1, 10*Math.PI/180, axis2, 10*Math.PI/180, 0.);
                GroundFieldOfViewDetector fovDetector = new GroundFieldOfViewDetector(frame, fov);

                vectorSkyCuadrilled.add(vectorCenter);
                fovDetectorsList.add(fovDetector);

            }
            System.out.println(vectorSkyCuadrilled);
            System.out.println(fovDetectorsList);
            

    	

     }  
        
}

