package src.App.ParametersTab;

import org.orekit.time.AbsoluteDate;


/*
 * 
 */
public class Parameters {

    private double noiseLevel;
    private AbsoluteDate startDate;
    private AbsoluteDate endDate;


    @Override
    public String toString() {
        return "Parameters [noiseLevel=" + noiseLevel + ", startDate=" + startDate + ", endDate=" + endDate + "]";
    }

    public double getNoiseLevel() {
        return noiseLevel;
    }
    public void setNoiseLevel(double noiseLevel) {
        this.noiseLevel = noiseLevel;
    }
    public AbsoluteDate getStartDate() {
        return startDate;
    }
    public void setStartDate(AbsoluteDate startDate) {
        this.startDate = startDate;
    }

    public AbsoluteDate getEndDate() {
        return endDate;
    }

    public void setEndDate(AbsoluteDate endDate) {
        this.endDate = endDate;
    }

}

