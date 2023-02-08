package src.Kalman;

import org.orekit.bodies.GeodeticPoint;
import org.orekit.estimation.measurements.GroundStation;
import org.orekit.frames.TopocentricFrame;

public class station {
	
	String name;
	
	Double latitude;
	
	Double longitude;
	
	Double altitude;
	
	GeodeticPoint geodeticPoint;
	
	TopocentricFrame topocentricFrame;
	
	GroundStation groundStation;
	
	public station(String name, Double latitude, Double longitude) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.altitude = 0.;
		this.geodeticPoint = new GeodeticPoint(this.latitude, this.longitude, this.altitude);
		this.topocentricFrame = new TopocentricFrame(constants.earthShape, this.geodeticPoint, this.name);
		this.groundStation = new GroundStation(this.topocentricFrame);
	}
	
	public GroundStation getGroundStation() {
		return this.groundStation;		
	}
	
	
	
	

}
