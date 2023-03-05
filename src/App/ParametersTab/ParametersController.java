package src.App.ParametersTab;

import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;

public class ParametersController {
    private Parameters model;
    public Parameters getModel() {
        return model;
    }

    private ParametersView view;

    public ParametersController() {
        this.model = new Parameters();
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

