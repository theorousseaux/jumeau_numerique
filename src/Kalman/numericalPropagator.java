package src.Kalman;

import org.hipparchus.ode.nonstiff.DormandPrince853Integrator;
import org.orekit.orbits.Orbit;
import org.orekit.orbits.OrbitType;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.numerical.NumericalPropagator;

public class numericalPropagator {
	
	double minStep;
	double maxstep;
	double positionTolerance;
	OrbitType propagationType;
	NumericalPropagator numericalPropagator;
	
	public numericalPropagator(Orbit initialOrbit) {
		this.minStep = 0.001;
		this.maxstep = 1000.0;
		this.positionTolerance = 10.;
		this.propagationType = OrbitType.KEPLERIAN;
		double[][] tolerances = NumericalPropagator.tolerances(positionTolerance, initialOrbit, propagationType);
		DormandPrince853Integrator integrator = new DormandPrince853Integrator(minStep, maxstep, tolerances[0], tolerances[1]);
		this.numericalPropagator = new NumericalPropagator(integrator);
		this.numericalPropagator.setOrbitType(this.propagationType);
		this.numericalPropagator.setInitialState(new SpacecraftState(initialOrbit));
	}
	
	public NumericalPropagator getNumericalPropagator() {
		return this.numericalPropagator;
	}



}
