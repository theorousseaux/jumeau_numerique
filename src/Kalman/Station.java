package src.Kalman;

import java.util.ArrayList;
import java.util.List;

import org.orekit.bodies.GeodeticPoint;
import org.orekit.estimation.measurements.GroundStation;
import org.orekit.frames.TopocentricFrame;

public class Station extends GroundStation {
	
	List<TelescopeAzEl> listTelescopes;
	// list radar
	// list laser
	
	// altitude a 0m
	public Station(String name, Double latitude, Double longitude) {
		super(new TopocentricFrame(constants.earthShape, new GeodeticPoint(latitude, longitude, 0.), name));
		this.listTelescopes = new ArrayList<>();
	}
	
	public Station(String name, Double latitude, Double longitude, Double altitude) {
		super(new TopocentricFrame(constants.earthShape, new GeodeticPoint(latitude, longitude, altitude), name));
		this.listTelescopes = new ArrayList<>();
	}
	
	public void addTelescope(TelescopeAzEl telescope) {
		this.listTelescopes.add(telescope);
		// telescope.updateStation(this);
	}
	
	public List<TelescopeAzEl> getListTelescope(){
		return this.listTelescopes;
	}
	
	public String getName() {
		return getBaseFrame().getName();
	}
	
	public Double getLongitude() {
		return getBaseFrame().getPoint().getLongitude();
	}
	
	public Double getLatitude() {
		return getBaseFrame().getPoint().getLatitude();
	}
	
	public Double getAltitude() {
		return getBaseFrame().getPoint().getAltitude();
	}
}
