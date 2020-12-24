package test;

import java.util.ArrayList;
import java.util.List;

public class SimpleAnomalyDetector implements TimeSeriesAnomalyDetector {
	public List<CorrelatedFeatures> FeaturesList;
	public List<AnomalyReport> reports;

//	private Object correlatedFeatures;

	@Override
	public void learnNormal(TimeSeries ts) {
		float trashHold = 0.9f;
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

//		System.out.println(FeaturesList);


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
//		for(AnomalyReport A: reports){
//			System.out.println(A);
//		}
		return reports;
	}
	
	public List<CorrelatedFeatures> getNormalModel(){
		return FeaturesList;
	}
//	public float getPearson(float[][] arr, int x, int y){
//		float pearsonRet;
//		float[] ax = new float[arr.length];
//		float[] ay = new float[arr.length];
//		for(int i = 0; i < arr.length; i++){
//			ax[i] = arr[i][x];
//			ay[i] = arr[i][y];
//		}
//		pearsonRet = StatLib.pearson(ax, ay);
////		System.out.println(pearsonRet);
//		return pearsonRet;
//	}

	public Point[] getPointsArr(float[][] arr, int x, int y) {
		Point[] points = new Point[arr[0].length];
		for (int i = 0; i < arr[0].length; i++) {
			points[i] = new Point(arr[x][i], arr[y][i]);
//			System.out.println(arr[x][i]+" "+arr[y][i]+ " " +i+ " " +points[i]);
		}
		return points;
	}

}
