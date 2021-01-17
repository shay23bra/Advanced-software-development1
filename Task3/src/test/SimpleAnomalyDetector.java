package test;
////Created by Shay Bratslavsky - I.D 313363541
import java.util.ArrayList;
import java.util.List;

public class SimpleAnomalyDetector implements TimeSeriesAnomalyDetector {
	public List<CorrelatedFeatures> FeaturesList;
	public List<AnomalyReport> reports;
	float trashHold = 0.9f;

	public void setTreshold(float treshold) {
		this.trashHold = treshold;
	}

	public float getTreshold() {
		return trashHold;
	}

	@Override
	public void learnNormal(TimeSeries ts) {
		trashHold = 0.9f;
		FeaturesList = new ArrayList<>();
		float[][] corelations = new float[ts.getNumOfCol()][ts.getNumOfCol()];
		for(int i = 0; i < ts.getNumOfCol(); i++){
			for(int j = 0; j< ts.getNumOfCol(); j++){
				if(j == i)
					corelations[i][j] = 0;
				else
					corelations[i][j] = Math.abs(StatLib.pearson(ts.getData()[i], ts.getData()[j]));
			}
		}

		for(int i = 0; i < corelations.length; i++){
			for(int j = 0; j < corelations.length; j++){
				float max = 0;
				float temp;
				if(corelations[i][j] >= trashHold){
					corelations[j][i] = 0;
					Point[] pointArr = getPointsArr(ts.getData(),i,j);
					Line line = StatLib.linear_reg(pointArr);
					for (Point point : pointArr) {
						temp = Math.abs(StatLib.dev(point, line));
						if (temp > max)
							max = temp;
					}
					FeaturesList.add(new CorrelatedFeatures(ts.str[i],ts.str[j], corelations[i][j], line, max ));
				}
			}
		}

	}




	@Override
	public List<AnomalyReport> detect(TimeSeries ts) {
		reports = new ArrayList<>();
		int a,b;

		for(int i = 0; i < ts.getNumOfRow(); i++){
			for(CorrelatedFeatures cor : FeaturesList){
				a = ts.getIndex(cor.feature1);
				b = ts.getIndex(cor.feature2);
				Point np = new Point(ts.getData()[a][i], ts.getData()[b][i]);
				if(StatLib.dev(np,cor.lin_reg)>cor.threshold+0.1){
					reports.add(new AnomalyReport(cor.feature1+ "-" +cor.feature2, i+1/*(long)(ts.getData()[0][i])*/));
				}
			}
		}
		return reports;
	}
	
	public List<CorrelatedFeatures> getNormalModel(){
		return FeaturesList;
	}

	public Point[] getPointsArr(float[][] arr, int x, int y) {
		Point[] points = new Point[arr[0].length];
		for (int i = 0; i < arr[0].length; i++) {
			points[i] = new Point(arr[x][i], arr[y][i]);
		}
		return points;
	}

}
