package controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Inet4Address;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import logic.ClientDetails;
import server.EchoServer;
import server.ServerUI;

/**
 * The class represent the control in the server screen..
 * 
 * @author Or Hilo
 * @version 1.0 Build 100 December 29, 2021
 */

public class ServerPageController implements Initializable {

	/**
	 * mainThread save the main thread
	 */
	public static Thread mainThread;

	@FXML
	private TableView<ClientDetails> tblClient;

	@FXML
	private TableColumn<ClientDetails, String> colHostName;

	@FXML
	private TableColumn<ClientDetails, String> colIP;

	@FXML
	private TableColumn<ClientDetails, String> colStatus;

	@FXML
	private Button btnExit;

	@FXML
	private Button btnConnect;

	@FXML
	private Button btnDiscconect;

	@FXML
	private Button btnImportData;

	@FXML
	private TextArea textArea;

	@FXML
	private TextField txtIP;

	@FXML
	private TextField txtPort;

	@FXML
	private TextField txtDBname;

	@FXML
	private TextField txtDBuser;

	@FXML
	private Label lblPort;

	@FXML
	private Label lblIP;

	@FXML
	private Label lblDBuser;

	@FXML
	private Label lblDBpassword;

	@FXML
	private Label lblDBname;

	@FXML
	private PasswordField passwordField;

	/**
	 * ImportData: import data from import_users table to users table.
	 * 
	 * @param event
	 */
	@FXML
	void ImportData(ActionEvent event) {
		if (EchoServer.conn == null) {
			System.out.println("Server connection failed");
			return;
		}
		DataBaseController.importDataOfUsersToUsersTable(EchoServer.conn);
	}

	/**
	 * start: show the starting screen of server.
	 * 
	 * @param primaryStage
	 * @throws Exception
	 */
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/controllers/ServerPage.fxml"));
		primaryStage.setTitle("Bite Me Server");
		Scene scene = new Scene(root);
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event -> event.consume());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Connect: if the user click connect And put in correct details the system
	 * connect to server and DB, else print failed connection error
	 * 
	 * @param event
	 */
	@FXML

	void Connect(ActionEvent event) {
		mainThread = Thread.currentThread();
		ServerUI.runServer(txtPort.getText(), txtDBname.getText(), txtDBuser.getText(), passwordField.getText());
		while (!Thread.interrupted())
			;
		if (EchoServer.conn == null) {
			System.out.println("Server connection failed");
		} else {
			System.out.println("Server connected");
			btnConnect.setDisable(true);
			btnDiscconect.setDisable(false);
			btnImportData.setDisable(false);
		}

	}

	/**
	 * Disconnect: disconnect from the server
	 * 
	 * @param event
	 * @throws SQLException
	 */
	@SuppressWarnings("static-access")
	@FXML
	void Disconnect(ActionEvent event) throws SQLException {
		ServerUI.sv.stopListening();
		ServerUI.sv.conn = null;
		btnConnect.setDisable(false);
		btnDiscconect.setDisable(true);
		btnImportData.setDisable(false);
		System.out.println("Server disconnected");
	}

	/**
	 * Exit: exit from the system
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void Exit(ActionEvent event) throws Exception {
		System.exit(0);
	}

	/**
	 * initialize: Initializing the server
	 * 
	 * @param location
	 * @param resources
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		OutputStream out = new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				appendText(String.valueOf((char) b));
			}
		};
		System.setOut(new PrintStream(out, true));
		System.setErr(new PrintStream(out, true));
		;
		colIP.setCellValueFactory(new PropertyValueFactory<ClientDetails, String>("ipAddress"));
		colHostName.setCellValueFactory(new PropertyValueFactory<ClientDetails, String>("hostName"));
		colStatus.setCellValueFactory(new PropertyValueFactory<ClientDetails, String>("status"));
		tblClient.setItems(EchoServer.getClientlist()); // from List in EchoServer class.
		try {
			txtIP.setText(Inet4Address.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		txtPort.setText("5555");
		txtDBname.setText("jdbc:mysql://localhost/bm-db?serverTimezone=IST");
		txtDBuser.setText("root");
		passwordField.setText("Aa123456");
		txtIP.setEditable(false);
		btnImportData.setDisable(false);
		btnDiscconect.setDisable(true);
	}

	/**
	 * appendText: add text to console
	 * 
	 * @param valueOf get the string that need to add to console
	 */
	public void appendText(String valueOf) {
		Platform.runLater(() -> textArea.appendText(valueOf));
	}

}