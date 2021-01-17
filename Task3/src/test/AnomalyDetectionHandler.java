package test;
//Created by Shay Bratslavsky - I.D 313363541

import test.Commands.DefaultIO;
import test.Server.ClientHandler;

import java.io.*;
import java.util.Scanner;

public class AnomalyDetectionHandler implements ClientHandler{

	@Override
	public void execute(InputStream inFromClient, OutputStream outToClient) {
		SocketIO sio = new SocketIO(inFromClient,outToClient);
		CLI cli = new CLI(sio);
		cli.start();
		sio.write("bye\n");
		sio.close();
	}

	public class SocketIO implements DefaultIO{
		Scanner in;
		PrintWriter out;
		public SocketIO(InputStream inFromClient, OutputStream outToClient){
			try {
				in=new Scanner(new BufferedInputStream(inFromClient));
				out=new PrintWriter(new BufferedOutputStream(outToClient));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		@Override
		public String readText() {
			return in.nextLine();
		}

		@Override
		public void write(String text) {
			out.print(text);
			out.flush();
		}

		@Override
		public float readVal() {
			return in.nextFloat();
		}

		@Override
		public void write(float val) {
			out.print(val);
			out.flush();
		}

		public void close() {
			in.close();
			out.close();
		}
	}


}
