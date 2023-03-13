package src.Kalman;

import org.orekit.propagation.numerical.NumericalPropagator;

public class Object {

    private NumericalPropagator propagator;
    private String id;
    private double SM;

    public Object ( NumericalPropagator propagator , String id , double SM ) {
        this.propagator = propagator;
        this.id = id;
        this.SM = SM;
    }

    public NumericalPropagator getPropagator ( ) {
        return this.propagator;
    }

    public String getId ( ) {
        return this.id;
    }

    public double getSM ( ) {
        return this.SM;
    }
}