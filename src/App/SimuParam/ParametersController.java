package src.App.SimuParam;

import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;

public class ParametersController {
    private Parameters model;
    private ParametersFormView view;

    public ParametersController(Parameters model, ParametersFormView view) {
        this.model = model;
        this.view = view;
    }

    public String getNoiseLevel(){
        return String.valueOf(model.getNoiseLevel());
    }

    public String getStartDate(){
        return model.getStartDate().toString();
    }

    public String getEndDate(){
        return model.getEndDate().toString();
    }

    public void setNoiseLevel(double noiseLevel){
        model.setNoiseLevel(noiseLevel);
    }

    public void setStartDate(int year, int month, int day){
        model.setStartDate(new AbsoluteDate(year, month, day, TimeScalesFactory.getUTC()));
    }

    public void setEndDate(int year, int month, int day){
        model.setEndDate(new AbsoluteDate(year, month, day, TimeScalesFactory.getUTC()));
    }


}

