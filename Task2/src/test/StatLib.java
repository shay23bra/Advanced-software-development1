package test;


public class StatLib {

	

	// simple average
	public static float avg(float[] x){
		int i = 0;
		float a = 0;
		while(i < x.length) {
			a += x[i];
			i++;
		}
		return (a/(float) x.length);
	}

	// returns the variance of X and Y
	public static float var(float[] x){
		float v =0;
		float a = avg(x);
		int i = 0;
		while (i<x.length){
			v += Math.pow(x[i], 2);
			i++;
		}
		v = v/(float) x.length;
		return (v - (float)Math.pow(a, 2));



	}

	// returns the covariance of X and Y
	public static float cov(float[] x, float[] y){
		//float vx = var(x);
		//float vy = var(y);
		float ax = avg(x);
		float ay = avg(y);
		//float axy = ((ax+ay)/2);
//		float c = vx*vy - ax*ay;
	//	float c = avg(((x.length-ax)*(y.length-ay)));
	//	return c;
		float sum = 0;
		for (int i = 0; i < x.length; i++) {
			sum += (x[i] - ax) * (y[i] - ay);
		}
		return sum / (float) x.length;
//		return 0;
	}


	// returns the Pearson correlation coefficient of X and Y
	public static float pearson(float[] x, float[] y){
		float c = cov(x, y);
		float sqx = (float)Math.sqrt(var(x));
		float sqy = (float)Math.sqrt(var(y));
//		float p = c/(sqx*sqy);
		return (c/(sqx*sqy));
//		return 0;
	}

	// performs a linear regression and returns the line equation
	public static Line linear_reg(Point[] points){
		float[] x = new float[points.length];
		float[] y = new float[points.length];
		for (int i=0;i< points.length;i++){
			x[i] = points[i].x;
			y[i] = points[i].y;
 		}
		float a = ((cov(x,y))/var(x));
		float b = ((avg(y)) - (a*(avg(x))));

		Line l = new Line(a,b);
		return l;

	}

	// returns the deviation between point p and the line equation of the points
	public static float dev(Point p,Point[] points){
		int i = 0;
		for(i =0; i< points.length; i++){
			if(p.x == points[i].x)
				break;
		}
		float d = Math.abs(points[i].y - p.x);

		return d;

	}

	// returns the deviation between point p and the line
	public static float dev(Point p,Line l){
		float y = (l.a * p.x) + l.b;
		float d=Math.abs(y-p.y);
		return d;

	}
	
}
