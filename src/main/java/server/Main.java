package server;

import java.io.IOException;
import server.application.RunContext;

public class Main {

	public static void main(String[] args) {
		
		//Listening port
		int port = 8001;
		
		try {
	            new RunContext().start(port);
	            System.out.println("************************************************");
	            System.out.println("Server started. Listening on port 8001...");
	            System.out.println("************************************************");
	        } catch (IOException e) {
	            System.out.println("Can't start the server.");
	        } 
	}
}
