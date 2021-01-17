package test;
//Created by Shay Bratslavsky - I.D 313363541
public class AnomalyReport {
	public final String description;
	public final  long timeStep;
	public AnomalyReport(String description, long timeStep){
		this.description=description;
		this.timeStep=timeStep;
	}

	@Override
	public String toString() {
		return "AnomalyReport{" +
				"description='" + description + '\'' +
				", timeStep=" + timeStep +
				'}';
	}
}
