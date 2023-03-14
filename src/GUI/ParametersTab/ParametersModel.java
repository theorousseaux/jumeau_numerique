package src.GUI.ParametersTab;

import org.orekit.time.AbsoluteDate;

/**
 * This class contains the parameters of a simulation
 */

public class ParametersModel {

    // The noise of the propagation model

    // Beginning of propagation
    private AbsoluteDate startDate;

    // End of propagation
    private AbsoluteDate endDate;

    public ParametersModel ( AbsoluteDate initialDate , AbsoluteDate finalDate ) {
        this.startDate = initialDate;
        this.endDate = finalDate;
    }

    public ParametersModel ( ) {

    }

    @Override
    public String toString ( ) {
        return "Parameters [startDate=" + startDate + ", endDate=" + endDate + "]";
    }

    public AbsoluteDate getStartDate ( ) {
        return startDate;
    }

    public void setStartDate ( AbsoluteDate startDate ) {
        this.startDate = startDate;
    }

    public AbsoluteDate getEndDate ( ) {
        return endDate;
    }

    public void setEndDate ( AbsoluteDate endDate ) {
        this.endDate = endDate;
    }
}