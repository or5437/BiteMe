package server;

import controllers.ServerPageController;
import javafx.application.Application;
import javafx.stage.Stage;


//the class represnt user interface for server starting connection window.
public class ServerUI extends Application {
	final public static int DEFAULT_PORT = 5555;
	public static EchoServer sv;

	public static void main(String args[]) throws Exception {
		launch(args);
	} // end main

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		ServerPageController spc = new ServerPageController(); // create ServerPageController.
		spc.start(primaryStage);
	}

	/*
	 * @param String p
	 * This method listening for connections request.
	 */
	/*
	 * @param String p
	 * This method listening for connections request.
	 */
	public static void runServer(String p, String DBname, String DBuser,String DBpass) {
		
		int port = 0; // Port to listen on
		try {
			port = Integer.parseInt(p); // Set port to 5555
		} catch (Throwable t) {
			//System.out.println("ERROR - Could not connect!");
		}
		if(sv==null)
	        sv = new EchoServer(port);
		sv.setDBname(DBname);
		sv.setDBuser(DBuser);
		sv.setDBpass(DBpass);
		try {
			sv.listen(); // Start listening for connections
		} catch (Exception ex) {
			//System.out.println("ERROR - Could not listen for clients!");
		}
	}

}