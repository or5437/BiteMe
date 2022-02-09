package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.User;

/**
 * Class that Describes screen of register private customer by BM manager
 * 
 * @author Tal Derai
 *
 */
public class RegisterNewPrivateCustomerController implements Initializable {
	/**
	 * searchIdBeforeRegisterPrivateController: save object for access to user for
	 * register
	 * 
	 */
	SearchIdBeforeRegisterPrivateController searchIdBeforeRegisterPrivateController = new SearchIdBeforeRegisterPrivateController();
	@FXML
	private AnchorPane screenAnchor;

	@FXML
	private AnchorPane screenRegisterPrivateCustomer;

	@FXML
	private Label lblTitleRegister;

	@FXML
	private Label lblID;

	@FXML
	private TextField txtId;

	@FXML
	private Label lblFirstName;

	@FXML
	private TextField txtFirstName;

	@FXML
	private TextField txtLastName;

	@FXML
	private Label lblLastName;

	@FXML
	private TextField txtEmail;

	@FXML
	private Label lblEmail;

	@FXML
	private Label lblPhoneNumber;

	@FXML
	private TextField txtPhoneNumber;

	@FXML
	private Label lblCreditCard;

	@FXML
	private TextField txtCreditCard;

	@FXML
	private Button btnRegister;

	@FXML
	private Button btnExit;

	@FXML
	private Button btnBack;

	@FXML
	private Label lblErrorMsg;

	/**
	 * When clicked on Back button, return to search private customer page
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
				.load(getClass().getResource("/controllers/SearchIdBeforeRegisterPrivatePage.fxml").openStream());
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		primaryStage.setTitle("Search private customer");
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Method that send to server the details of user for update in DB in table
	 * private customers
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void Register(ActionEvent event) throws IOException {
		if (txtCreditCard.getText().trim().isEmpty())
			lblErrorMsg.setText("Please Enter Valid Credit Card Number!");
		else {
			ClientUI.chat.accept("AddPrivateCustomer:" + "\t" + txtId.getText() + "\t" + txtFirstName.getText() + "\t"
					+ txtLastName.getText() + "\t" + txtEmail.getText() + "\t" + txtPhoneNumber.getText() + "\t"
					+ txtCreditCard.getText());
			ClientUI.chat.accept("ChangeUserType:" + "\t" + "Private_Customer" + "\t" + txtId.getText());
			((Node) event.getSource()).getScene().getWindow().hide(); // hide window
			FXMLLoader loader = new FXMLLoader();
			Stage primaryStage = new Stage();
			Pane root = loader.load(getClass().getResource("/controllers/MsgPage.fxml").openStream());
			MsgController msgController = loader.getController();
			msgController.loadData("Successfully registerd!", 3);
			Scene scene = new Scene(root);
			primaryStage.setResizable(false);
			primaryStage.setOnCloseRequest(event1 -> event1.consume());
			primaryStage.setTitle("Message");
			primaryStage.setScene(scene);
			primaryStage.show();
		}
	}

	/**
	 * Initialize screen of register private customer
	 *
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		fillFields(searchIdBeforeRegisterPrivateController.getUser());
	}

	/**
	 * Fill the screen with details of customer automatic
	 * 
	 * @param userResult: object user with all details
	 */
	private void fillFields(User userResult) {
		txtId.setText(userResult.getIdNumber().toString());
		txtId.setEditable(false);
		txtFirstName.setText(userResult.getFirstName());
		txtFirstName.setEditable(false);
		txtLastName.setText(userResult.getLastName());
		txtLastName.setEditable(false);
		txtEmail.setText(userResult.getEmail());
		txtEmail.setEditable(false);
		txtPhoneNumber.setText(userResult.getPhoneNumber());
		txtPhoneNumber.setEditable(false);

	}

	@FXML
	void InsertCreditCard(KeyEvent event) {

	}

	/**
	 * Upload screen of Register Private Customer
	 * 
	 * @param primaryStage
	 * @throws IOException
	 */
	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/controllers/RegisterNewPrivateCustomerPage.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		primaryStage.setTitle("Register New Private Customer");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
