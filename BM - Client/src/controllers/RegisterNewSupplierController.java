package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import client.ChatClient;
import client.ClientUI;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

//import logic.User;
/**
 * Class that Describes screen of register supplier by BM manager
 * 
 * @author Tal Derai
 *
 */
public class RegisterNewSupplierController implements Initializable {
	/**
	 * searchIdBeforeRegisterSupplierController: save object for access to user for
	 * register
	 * 
	 */
	SearchIdBeforeRegisterSupplierController searchIdBeforeRegisterSupplierController = new SearchIdBeforeRegisterSupplierController();

	@FXML
	private Label lblRegisterNewsup;

	@FXML
	private Label lblID;

	@FXML
	private TextField txtID;

	@FXML
	private Label lblBranch;

	@FXML
	private Label lblRes;

	@FXML
	private TextField txtBranch;

	@FXML
	private Button btnRegister;

	@FXML
	private Button btnBack;

	@FXML
	private Button btnExit;

	@FXML
	private Label lblFirstName;

	@FXML
	private Label lblLastName;

	@FXML
	private TextField txtFirstName;

	@FXML
	private TextField txtLastName;

	@FXML
	private TextField txtRestaurant;

	@FXML
	private Label lblResAdd;

	@FXML
	private TextField txtResAdd;

	@FXML
	private Label lblResType;

	@FXML
	private ComboBox<String> cmbResType;
	@FXML
	private Label lblErrorMsg;

	ObservableList<String> restaurants_types;

	/**
	 * When clicked on Back button, return to search supplier page
	 * 
	 * @param event
	 * @throws Exception
	 */
	@FXML
	void Back(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hide window
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		Pane root = loader
				.load(getClass().getResource("/controllers/SearchIdBeforeRegisterSupplierPage.fxml").openStream());
		Scene scene = new Scene(root);
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		primaryStage.setTitle("Search supplier");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Upload screen of Register Supplier
	 * 
	 * @param primaryStage
	 * @throws Exception
	 */
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/controllers/RegisterNewSupplierPage.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		primaryStage.setTitle("Register Supplier Frame");
		primaryStage.show();

	}

	/**
	 * Method that send to server the details of user for update in DB in table
	 * suppliers and restaurants
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void Register(ActionEvent event) throws IOException {
		ClientUI.chat.accept("AddSupplier:" + "\t" + txtID.getText() + "\t" + txtBranch.getText() + "\t"
				+ txtRestaurant.getText() + "\t" + txtResAdd.getText());
		ClientUI.chat.accept("ChangeUserType:" + "\t" + "Supplier" + "\t" + txtID.getText());
		((Node) event.getSource()).getScene().getWindow().hide(); // hide window
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/controllers/MsgPage.fxml").openStream());
		MsgController msgController = loader.getController();
		msgController.loadData("Successfully registerd!", 3);
		Scene scene = new Scene(root);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		primaryStage.setTitle("Message");
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@FXML
	void SelectRes(KeyEvent event) {
		btnRegister.setDisable(false);
	}

	/**
	 * Indicate the branch of user
	 * 
	 * @param Role: role of user
	 * @return
	 */
	private String returnBranchOfBM_Manager(String Role) {
		if (Role.contains("North"))
			return "North";
		else if (Role.contains("Central"))
			return "Central";
		else if (Role.contains("South"))
			return "South";
		return null;
	}

	/**
	 * Initialize screen of register supplier
	 *
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		txtFirstName.setText(searchIdBeforeRegisterSupplierController.getUser().getFirstName());
		txtLastName.setText(searchIdBeforeRegisterSupplierController.getUser().getLastName());
		txtID.setText(String.valueOf(searchIdBeforeRegisterSupplierController.getUser().getIdNumber())); // cast ID to
																											// string
		txtRestaurant.setText(searchIdBeforeRegisterSupplierController.getUser().getRole().split(" ")[1]);
		txtID.setEditable(false);
		txtRestaurant.setEditable(false);
		txtFirstName.setEditable(false);
		txtLastName.setEditable(false);

		String Branch = returnBranchOfBM_Manager(ChatClient.u1.getRole());
		txtBranch.setText(Branch);
		txtBranch.setEditable(false);
		try {
			ClientUI.chat.accept("getResName:" + "\t" + Branch);
		} catch (IOException e) {
			e.printStackTrace();
		}
		btnRegister.setDisable(true);
	}

}
