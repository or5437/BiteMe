package controllers;

import client.ClientController;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * class for enter IP.
 * 
 * @author Or Hilo
 * @version 1.0 Build 100 December 28, 2021
 */

public class EnterIpController {

	/**
	 * isConnected: check if the connection was successful.
	 */
	public static boolean isConnected = true;

	@FXML
	private Text txtEnterServerIP;

	@FXML
	private Text txtIP;

	@FXML
	private TextField txtEnterIP;

	@FXML
	private Button btnConfirm;

	@FXML
	private Label lblWrongIP;

	@FXML
	private Button btnExit;

	/**
	 * Exit: Sign out of the system
	 * 
	 * @param event-not used
	 */
	@FXML
	void Exit(ActionEvent event) {
		System.exit(0);
	}

	/**
	 * Confirm: if the user press on Confirm button, the system will check the typed
	 * id and if the IP is correct the system go to user's login page, else throws a
	 * "Wrong IP" error.
	 * 
	 * @param event hide the current page(Enter IP) and and goes to the user's login
	 *              screen when click on Confirm button.
	 * 
	 * @throws Exception
	 */
	@FXML
	void Confirm(ActionEvent event) throws Exception {
		if (txtEnterIP.getText().equals("")) {
			lblWrongIP.setText("Enter Server IP");
			return;
		}
		ClientUI.chat = new ClientController(txtEnterIP.getText(), ClientController.DEFAULT_PORT);
		ClientUI.chat.accept("is connected");
		if (!isConnected) {
			lblWrongIP.setText("Wrong IP");
			isConnected = true;
		} else {
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			Stage primaryStage = new Stage();
			ClientLoginController clientLoginController = new ClientLoginController();
			clientLoginController.start(primaryStage);
		}
	}

	/**
	 * Start: upload screen of Enter ip of the cimputer .
	 * 
	 * @param primaryStage
	 * 
	 * @throws Exception
	 */
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/controllers/EnterIpPage.fxml"));
		primaryStage.setTitle("Enter IP Server");
		Scene scene = new Scene(root);
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
