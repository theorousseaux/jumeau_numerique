package src.GroundStation;

import java.util.ArrayList;
import java.util.List;


import org.hipparchus.geometry.euclidean.threed.Vector3D;

import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;

import org.orekit.geometry.fov.DoubleDihedraFieldOfView;
import org.orekit.propagation.events.GroundFieldOfViewDetector;




public class simulation_main {
    
    public static void main(String[] args) throws Exception {

            Frame frame = FramesFactory.getEME2000();

            //cadrillage du ciel en azimuth,elevation
            /////////////////////////////////////////
            List<List<Integer>> azElskyCuadrilled  = new ArrayList<>();
            int cpt = 0;
            for (int elevation = 35; elevation <= 145; elevation+=10){
                cpt+=1;
                
                if (cpt%2 == 1){
                    
                    for (int azimuth = 5; azimuth <= 355; azimuth+=10) {
                        List<Integer> aePosition = new ArrayList<Integer>();
                        aePosition.add(azimuth);
                        aePosition.add(elevation);
                        azElskyCuadrilled.add(aePosition);
                    }
                }
    
                else {
                    for (int azimuth = 355; azimuth >= 5; azimuth-=10){
                        List<Integer> aePosition = new ArrayList<Integer>();
                        aePosition.add(azimuth);
                        aePosition.add(elevation);
                        azElskyCuadrilled.add(aePosition);
                    }
                }
            }
            System.out.println(azElskyCuadrilled);
    
            //Conversion avec vecteurs 
            //////////////////////////
            List<Vector3D> vectorSkyCuadrilled  = new ArrayList<>();
            
            for (int i = 0; i < azElskyCuadrilled.size(); i++){
                    
                List<Integer> aePosition = azElskyCuadrilled.get(i);
                int azimuth = aePosition.get(0);
                int elevation = aePosition.get(1);
    
                Vector3D vectorCenter = new Vector3D(azimuth*3.14/180, elevation*3.14/180);
                vectorSkyCuadrilled.add(vectorCenter);
            }
            System.out.println(vectorSkyCuadrilled);
            
            //Building Field of views
            Vector3D axis1 = new Vector3D(1,0,0);
            Vector3D axis2 = new Vector3D(0, Math.sqrt(2)/2, Math.sqrt(2)/2);

            List<DoubleDihedraFieldOfView> fovSkyCuadrilled  = new ArrayList<>();
            for (int i = 0; i < vectorSkyCuadrilled.size(); i++) {
                Vector3D vectorCenter = vectorSkyCuadrilled.get(i);
                DoubleDihedraFieldOfView fov = new DoubleDihedraFieldOfView(vectorCenter, axis1, 10*3.14/180, axis2, 10*3.14/180, 0.);
                fovSkyCuadrilled.add(fov);
            }
            System.out.println(fovSkyCuadrilled);

            //Building detectors
            List<GroundFieldOfViewDetector> fovDetectorsList  = new ArrayList<>();
            for (int i = 0; i < fovSkyCuadrilled.size(); i++) {
                DoubleDihedraFieldOfView fov = fovSkyCuadrilled.get(i);
                GroundFieldOfViewDetector fovDetector = new GroundFieldOfViewDetector(frame, fov);
                fovDetectorsList.add(fovDetector);
            }

            System.out.println(fovDetectorsList);
     }  
        
}

