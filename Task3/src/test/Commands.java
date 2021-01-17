package test;
//Created by Shay Bratslavsky - I.D 313363541
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.RoundingMode;
import java.security.spec.RSAOtherPrimeInfo;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Commands {
	
	// Default IO interface
	public interface DefaultIO{
		public String readText();
		public void write(String text);
		public float readVal();
		public void write(float val);

		// you may add default methods here
	}
	
	// the default IO to be used in all commands
	DefaultIO dio;
	public Commands(DefaultIO dio) {
		this.dio=dio;
	}
	
	// you may add other helper classes here
	
	
	
	// the shared state of all commands
	private class SharedState{
		// implement here whatever you need
		List<AnomalyReport> reports;
		SimpleAnomalyDetector sad;
		TimeSeries tsTest ;
		TimeSeries tsTrain ;
	}
	
	private  SharedState sharedState=new SharedState();

	
	// Command abstract class
	public abstract class Command{
		protected String description;
		
		public Command(String description) {
			this.description=description;
		}
		
		public abstract void execute();
	}
	
	// Command class for example:
//	public class ExampleCommand extends Command{
//
//		public ExampleCommand() {
//			super("this is an example of command");
//		}
//
//		@Override
//		public void execute() { dio.write(description); }
//	}
	
	// implement here all other commands

	public class Menu extends Command{
		public Menu() {
			super("Welcome to the Anomaly Detection Server.\n" +
					"Please choose an option:\n" +
					"1. upload a time series csv file\n" +
					"2. algorithm settings\n" +
					"3. detect anomalies\n" +
					"4. display results\n" +
					"5. upload anomalies and analyze results\n" +
					"6. exit\n");
		}

		@Override
		public void execute() { dio.write(description); }
	}


	public void csvRW(String file){
		try {
			FileWriter fileWriter = new FileWriter(file);
			String t = dio.readText();
			while (!t.equals("done")) {
				fileWriter.write(t + "\n");
				fileWriter.flush();
				t = dio.readText();
			}
			fileWriter.close();
		}catch (IOException e){
			System.out.println("Error.\n");
			e.printStackTrace();
		}
	}

	public void createFile(String file){
		String csvF = file.toLowerCase() + ".csv";
		dio.write("Please upload your local " + file.toLowerCase() + " CSV file.\n");
		this.csvRW(csvF);
		dio.write("Upload complete.\n");
	}

	public class UploadFile extends Command{
		public UploadFile() {
			super("UploadFile\n");
		}

		@Override
		public void execute() {
			createFile("Train");
			createFile("Test");
		}
	}

	public class ThresholdC extends Command{
		public ThresholdC() {
			super("Threshold\n");
		}

		@Override
		public void execute() {
			sharedState.sad = new SimpleAnomalyDetector();
			dio.write("The current correlation threshold is " + sharedState.sad.getTreshold() + "\nType a new threshold\n");
			float f = dio.readVal();
			while(f>1 || f<0){
				dio.write("please choose a value between 0 and 1.\n");
				f = dio.readVal();
			}
			sharedState.sad.setTreshold(f);
		}
	}

	public class Detect extends Command{
		public Detect() {
			super("Detect\n");
		}

		@Override
		public void execute() {
			sharedState.tsTest = new TimeSeries("test.csv");
			sharedState.tsTrain = new TimeSeries("train.csv");
			sharedState.sad.learnNormal(sharedState.tsTrain);
			sharedState.reports = new ArrayList<>();
			sharedState.reports = sharedState.sad.detect(sharedState.tsTest);
			dio.write("anomaly detection complete.\n");
		}
	}

	public class Display extends Command{
		public Display() {
			super("Display results\n");
		}

		@Override
		public void execute() {
			for (AnomalyReport c : sharedState.reports){
				dio.write(c.timeStep + "\t" + c.description + "\n");
			}
			dio.write("Done.\n");

		}
	}

	public class Analyze extends Command {
		public Analyze() {
			super("Upload anomalies and analyze results");
		}

		@Override
		public void execute() {
			List<Integer> x = new ArrayList<Integer>();
			x.add(0);
			int TP = 0, flag = 0, FP = 0, p = 0, round = 0, roundR = 0, count = 0;
			long N, n = 0;
			dio.write("Please upload your local anomalies file.\n");
			dio.write("Upload complete.\n");
			String line = dio.readText();
			while (!line.equals("done")) {
				p++;
				long start = Long.parseLong(line.split(",")[0]);
				long finish = Long.parseLong(line.split(",")[1]);

				AnomalyReport r = sharedState.reports.get(0);
				while (sharedState.reports.indexOf(r) <= sharedState.reports.size() - 1) {
					if (r.timeStep >= start && r.timeStep <= finish) {
						flag = 1;
						TP++;
						x.add(roundR, 1);
						if (FP > 0)
							FP--;
					} else {
						if (x.get(roundR) == 0) {
							FP++;
							x.add(roundR, 1);
						}
					}
					String des = r.description;
					long tn = r.timeStep;
					long tt = r.timeStep - 1;
					while (r.description.equals(des) && (r.timeStep - tt == 1)) {
						if (sharedState.reports.indexOf(r) == sharedState.reports.size() - 1) {
							if (r.timeStep >= start && r.timeStep <= finish && flag == 0) {
								flag = 1;
								TP++;
								x.add(roundR, 1);
								FP--;
							}
							tt++;
							break;
						} else {
							if (r.timeStep >= start && r.timeStep <= finish && flag == 0) {
								flag = 1;
								TP++;
								x.add(roundR, 1);
								FP--;
							}
						}
						r = sharedState.reports.get(sharedState.reports.indexOf(r) + 1);
						tt++;

					}
					roundR++;
					x.add(0);
					if (round == 0)
						n += (tt - tn) + 1;
					if (sharedState.reports.indexOf(r) != sharedState.reports.size() - 1) {
						flag = 0;
					} else {
						break;
					}
				}
				round++;

				flag = 0;
				roundR = 0;
				line = dio.readText();
			}
			DecimalFormat decimalFormat = new DecimalFormat("#.###");
			decimalFormat.setRoundingMode(RoundingMode.FLOOR);
			N = sharedState.tsTest.numOfRow - n;
			float far = (float) FP / (float) N;
			float tpr = (float) TP / (float) p;
			dio.write("True Positive Rate: " + Float.valueOf(decimalFormat.format(tpr)) + "\n");
			dio.write("False Positive Rate: " + Float.valueOf(decimalFormat.format(far)) + "\n");


		}
	}
	}