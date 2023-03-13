package src.App.ParametersTab;

import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;

/**
 * This class is the controller of the parameter setting tab.
 * It will perform all the actions made by the user.
 */
public class ParametersController {

    // The parameters class used as model
    public Parameters model;

    public ParametersController ( ) {
        this.model = new Parameters ( );
    }

    public Parameters getModel ( ) {
        return model;
    }

    @Override
    public String toString ( ) {
        return "ParametersController [model=" + model + "]";
    }

    public AbsoluteDate getStartDate ( ) {
        return model.getStartDate ( );
    }

    public String getEndDate ( ) {
        return model.getEndDate ( ).toString ( );
    }


    public void setStartDate ( int year , int month , int day ) {
        model.setStartDate ( new AbsoluteDate ( year , month , day , TimeScalesFactory.getUTC ( ) ) );
    }

    public void setEndDate ( int year , int month , int day ) {
        model.setEndDate ( new AbsoluteDate ( year , month , day , TimeScalesFactory.getUTC ( ) ) );
    }
}

