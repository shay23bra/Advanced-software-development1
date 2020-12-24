package test;

public class CorrelatedFeatures {
	public final String feature1,feature2;
	public final float corrlation;
	public final Line lin_reg;
	public final float threshold;
	
	public CorrelatedFeatures(String feature1, String feature2, float corrlation, Line lin_reg, float threshold) {
		this.feature1 = feature1;
		this.feature2 = feature2;
		this.corrlation = corrlation;
		this.lin_reg = lin_reg;
		this.threshold = threshold;
	}

	@Override
	public String toString() {
		return "CorrelatedFeatures{" +
				"feature1='" + feature1 + '\'' +
				", feature2='" + feature2 + '\'' +
				", corrlation=" + corrlation +
				", lin_reg=" + lin_reg +
				", threshold=" + threshold +
				'}';
	}
}
