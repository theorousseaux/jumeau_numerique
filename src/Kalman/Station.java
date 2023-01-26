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
	
	// avec True, deja un telescope basique d'inclu (mean 0°, ecart type 0.3°, min elev 30°, fov (inutile pour l'instant) 10°, step 10s)
	public Station(String name, Double latitude, Double longitude, Double altitude, boolean telescope_) {
		super(new TopocentricFrame(constants.earthShape, new GeodeticPoint(latitude, longitude, altitude), name));
		this.listTelescopes = new ArrayList<>();
		if(telescope_) {
			TelescopeAzEl telescope = new TelescopeAzEl(new double[] {0., 0.}, new double[] {0.3*Math.PI/180, 0.3*Math.PI/180}, 30*Math.PI/180, 10*Math.PI/180, 10, this);
			this.listTelescopes.add(telescope);
		}
	}
	
	public void addTelescope(TelescopeAzEl telescope) {
		this.listTelescopes.add(telescope);
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
