/* Copyright 2002-2022 CS GROUP
 * Licensed to CS GROUP (CS) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * CS licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package src.Observer;

import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.orekit.frames.Frame;
import org.orekit.geometry.fov.FieldOfView;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.events.AbstractDetector;
import org.orekit.propagation.events.VisibilityTrigger;
import org.orekit.propagation.events.handlers.EventHandler;
import org.orekit.propagation.events.handlers.StopOnIncreasing;
import org.orekit.time.TimeComponents;
import src.constants;

import java.util.LinkedHashMap;
import java.util.Map;


public class CustomGroundFieldOfViewDetector extends AbstractDetector<CustomGroundFieldOfViewDetector> {

    /**
     * the reference frame attached to the sensor.
     */
    private final Frame frame;

    /**
     * Field of view of the sensor.
     */
    private final LinkedHashMap<TimeComponents, FieldOfView> skyCoveringMap;


    public CustomGroundFieldOfViewDetector ( final Frame frame ,
                                             final LinkedHashMap<TimeComponents, FieldOfView> skyCoveringMap ) {
        this ( DEFAULT_MAXCHECK , DEFAULT_THRESHOLD , DEFAULT_MAX_ITER ,
                new StopOnIncreasing<CustomGroundFieldOfViewDetector> ( ) ,
                frame , skyCoveringMap );
    }


    private CustomGroundFieldOfViewDetector ( final double maxCheck ,
                                              final double threshold ,
                                              final int maxIter ,
                                              final EventHandler<? super CustomGroundFieldOfViewDetector> handler ,
                                              final Frame frame ,
                                              final LinkedHashMap<TimeComponents, FieldOfView> skyCoveringMap ) {
        super ( maxCheck , threshold , maxIter , handler );
        this.frame = frame;
        this.skyCoveringMap = skyCoveringMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected CustomGroundFieldOfViewDetector create ( final double newMaxCheck ,
                                                       final double newThreshold ,
                                                       final int newMaxIter ,
                                                       final EventHandler<? super CustomGroundFieldOfViewDetector> newHandler ) {
        return new CustomGroundFieldOfViewDetector ( newMaxCheck , newThreshold ,
                newMaxIter , newHandler , this.frame , this.skyCoveringMap );
    }

    /**
     * Get the sensor reference frame.
     *
     * @return the reference frame attached to the sensor.
     */
    public Frame getFrame ( ) {
        return this.frame;
    }

    /**
     * Get the Field Of View.
     *
     * @return Field Of View
     * @since 10.1
     */
    public LinkedHashMap<TimeComponents, FieldOfView> getskyCoveringMap ( ) {
        return skyCoveringMap;
    }

    /**
     * {@inheritDoc}
     *
     * <p> The g function value is the angular offset between the satellite and
     * the {@link FieldOfView#offsetFromBoundary(Vector3D , double , VisibilityTrigger)
     * Field Of View boundary}. It is negative if the satellite is visible within
     * the Field Of View and positive if it is outside of the Field Of View,
     * including the margin. </p>
     *
     * <p> As per the previous definition, when the satellite enters the Field
     * Of View, a decreasing event is generated, and when the satellite leaves
     * the Field Of View, an increasing event is generated. </p>
     */
    public double g ( final SpacecraftState s ) {

        // get line of sight in sensor frame
        final Vector3D los = s.getPVCoordinates ( this.frame ).getPosition ( );

        //---------
        FieldOfView fov = null;
        TimeComponents time = s.getDate ( ).getComponents ( constants.UTC ).getTime ( );
        for (Map.Entry mapEntry : this.skyCoveringMap.entrySet ( )) {
            fov = (FieldOfView) mapEntry.getValue ( );
            if (time.compareTo ( (TimeComponents) mapEntry.getKey ( ) ) < 0) {
                break;
            }
        }
        //---------

        return -fov.offsetFromBoundary ( los , 0.0 , VisibilityTrigger.VISIBLE_ONLY_WHEN_FULLY_IN_FOV ); // le "-" est TRES important -> positif quand dans fov
    }

}