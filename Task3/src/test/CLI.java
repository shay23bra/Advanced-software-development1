package test;

import java.util.ArrayList;

import test.Commands.Command;
import test.Commands.DefaultIO;

public class CLI {

	ArrayList<Command> commands;
	DefaultIO dio;
	Commands c;
	
	public CLI(DefaultIO dio) {
		this.dio=dio;
		c=new Commands(dio); 
		commands=new ArrayList<>();
		// example: commands.add(c.new ExampleCommand());
		// implement
		commands.add(c.new Menu());
		commands.add(c.new UploadFile());
		commands.add(c.new ThresholdC());
		commands.add(c.new Detect());
		commands.add(c.new Display());
		commands.add(c.new Analyze());
	}
	
	public void start() {
		// implement
		commands.get(0).execute();
		String option = dio.readText();
		int o = Integer.parseInt(option);
		while(1 <= o && o <= 6 ){
			if(o == 6)
				break;
			commands.get(o).execute();
			commands.get(0).execute();
//			if(o == 5)
//				break;
			option = dio.readText();
			while(option.equals("")|| option.equals("\n") || option.equals("done") || option.equals(" ")){
				option = dio.readText();
			}
			o = Integer.parseInt(option);
		}
//		commands.get(1).execute();
//		commands.get(0).execute();
//		commands.get(2).execute();
//		commands.get(0).execute();

	}
}
