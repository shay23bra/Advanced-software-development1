package test;
//Created by Shay Bratslavsky - I.D 313363541
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.stream.Stream;

public class Server {
	int port;
	ClientHandler handler;
//	void communicate(InputStream inFromClient, OutputStream outToClient){};

	public Server(int port, ClientHandler handler) {
		this.port = port;
		this.handler = handler;
		stop=false;
	}

	public interface ClientHandler{
		// define...
		public void execute(InputStream inFromClient, OutputStream outToClient);
	}

	volatile boolean stop;
	public Server() {
		stop=false;
	}
	
	
	private void startServer(int port, ClientHandler ch) {
		// implement here the server...
		try {
			ServerSocket server = new ServerSocket(port);
			server.setSoTimeout(1000);
			while (!stop) {
				try {
					Socket aClient = server.accept();
					ch.execute(aClient.getInputStream(), aClient.getOutputStream());
					aClient.close();
				}catch (SocketTimeoutException e){}
			}
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// runs the server in its own thread
	public void start(int port, ClientHandler ch) {
		new Thread(()->startServer(port,ch)).start();
	}
	
	public void stop() {
		stop=true;
	}
}
