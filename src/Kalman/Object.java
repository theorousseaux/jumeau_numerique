package src.Kalman;

import org.orekit.estimation.measurements.ObservableSatellite;
import org.orekit.propagation.numerical.NumericalPropagator;

public class Object {
	
	private ObservableSatellite observableSatellite;
	private NumericalPropagator propagator;
	private String id;
	private double SM;
	
	public Object(ObservableSatellite observableSatellite, NumericalPropagator propagator, String id, double SM) {
		this.observableSatellite = observableSatellite;
		this.propagator = propagator;
		this.id = id;
		this.SM = SM;
	}
	
	public NumericalPropagator getPropagator() {
		return this.propagator;
	}
	
	public ObservableSatellite getObservableSatellite() {
		return this.observableSatellite;
	}
}