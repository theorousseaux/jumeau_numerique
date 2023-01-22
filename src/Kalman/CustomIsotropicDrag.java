package src.Kalman;

import java.util.Collections;
import java.util.List;

import org.hipparchus.CalculusFieldElement;
import org.hipparchus.geometry.euclidean.threed.FieldRotation;
import org.hipparchus.geometry.euclidean.threed.FieldVector3D;
import org.hipparchus.geometry.euclidean.threed.Rotation;
import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.hipparchus.util.FastMath;
import org.orekit.forces.drag.DragSensitive;

import org.orekit.frames.Frame;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.FieldAbsoluteDate;
import org.orekit.utils.ParameterDriver;

public class CustomIsotropicDrag implements DragSensitive {
	
	private final double SCALE = FastMath.scalb(1.0, -3);
	
	/** Drivers for drag coefficient parameter. */
	private final ParameterDriver dragParametersDrivers;

	/** Constructor with drag coefficient min/max set to ±∞.
	 * @param crossSection Surface (m²) over Mass (kg)
	 * @param dragCoeff drag coefficient
	 */
	public CustomIsotropicDrag(final double crossSectionMass, final double dragCoeff) {
	    // Initial value of the CSM coefficient
	    final double csmInit = dragCoeff * crossSectionMass;
	    this.dragParametersDrivers = new ParameterDriver("drag CSM", csmInit, SCALE, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
	    //this.dragParametersDrivers.setSelected(true);
	}
	
	/** {@inheritDoc} */
	@Override
    public List<ParameterDriver> getDragParametersDrivers() {
        return Collections.singletonList(dragParametersDrivers);
    }
	
	/** {@inheritDoc} */
	@Override
	public Vector3D dragAcceleration(final AbsoluteDate date, final Frame frame, final Vector3D position,
			final Rotation rotation, final double mass,
			final double density, final Vector3D relativeVelocity,
            final double[] parameters) {
		final double csm = parameters[0];
		return new Vector3D(relativeVelocity.getNorm() * density * csm * 0.5, relativeVelocity);
	}
	
	/** {@inheritDoc} */
	@Override
	public <T extends CalculusFieldElement<T>> FieldVector3D<T> dragAcceleration(final FieldAbsoluteDate<T> date, final Frame frame,
			final FieldVector3D<T> position, final FieldRotation<T> rotation,
	        final T mass, final T density,
	        final FieldVector3D<T> relativeVelocity,
	        final T[] parameters) {
		final T csm = parameters[0];
		return new FieldVector3D<>(relativeVelocity.getNorm().multiply(density.multiply(csm.multiply(0.5))), relativeVelocity);
	}
}
