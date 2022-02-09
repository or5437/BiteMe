package controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ChatClient;
import client.ClientUI;
import javafx.collections.FXCollections;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.MsgToHR;
import logic.User;

/**
 * Class that Describes screen of register business customer by BM manager
 * 
 * @author Tal Derai
 *
 */
public class RegisterNewBusinessCustomerController implements Initializable {
	/**
	 * searchIdBeforeRegisterBusinessController: save object for access to user for
	 * register
	 * 
	 */
	SearchIdBeforeRegisterBusinessCustomerController searchIdBeforeRegisterBusinessController = new SearchIdBeforeRegisterBusinessCustomerController();

	/**
	 * mth: a new instance of MsgToHR Class.
	 */
	public static MsgToHR mth = new MsgToHR(0, 0, null, null, null, null, false);
	/**
	 * branch: get the relvant branch
	 */
	public static String branch;

	@FXML
	private AnchorPane screenAnchor;

	@FXML
	private AnchorPane screenRegisterBusinessCustomer;

	@FXML
	private Label lblTitleRegister;

	@FXML
	private Label lblID;

	@FXML
	private TextField txtId;

	@FXML
	private Label lblFirstName;

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

	@FXML
	private Label lblLastName1;

	@FXML
	private Label lblBudget;

	@FXML
	private TextField txtBudget;

	@FXML
	private TextField txtFirstName;

	@FXML
	private ComboBox<String> cmbBusinessEmployer;

	/**
	 * When clicked on Back button, return to search business customer page
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
				.load(getClass().getResource("/controllers/SearchIdBeforeRegisterBusinessPage.fxml").openStream());
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		primaryStage.setTitle("Search Business customer");
		primaryStage.show();
	}

	/**
	 * Disable edit until enter credit card
	 * 
	 * @param event
	 */
	@FXML
	void InsertCreditCard(KeyEvent event) {
		btnRegister.setDisable(false);

	}

	/**
	 * Method that send to server the details of user for update DB in table waiting
	 * business customers and message to hr
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void Register(ActionEvent event) throws IOException {
		if (txtCreditCard.getText().trim().isEmpty())
			lblErrorMsg.setText("Please Enter Valid Credit Card Number!");
		else {
			setApprovedMsgToHRData();
			String msgToHR = "msgToHR:" + "\t" + String.valueOf(mth.getFromUserId()) + "\t"
					+ String.valueOf(mth.getToUserId()) + "\t" + mth.getDate() + "\t" + mth.getHour() + "\t"
					+ mth.getMessage() + "\t" + mth.getStatus() + "\t" + String.valueOf(mth.isRead());
			ClientUI.chat.accept(msgToHR);
			ClientUI.chat.accept(
					"InsertWaitingBusinessCus:" + "\t" + txtId.getText() + "\t" + cmbBusinessEmployer.getValue() + "\t"
							+ txtFirstName.getText() + "\t" + txtLastName.getText() + "\t" + txtEmail.getText() + "\t"
							+ txtPhoneNumber.getText() + "\t" + txtCreditCard.getText() + "\t" + txtBudget.getText());
			lblErrorMsg.setText("A requset sent to the hr manager of " + cmbBusinessEmployer.getValue());
			((Node) event.getSource()).getScene().getWindow().hide(); // hide window
			FXMLLoader loader = new FXMLLoader();
			Stage primaryStage = new Stage();
			Pane root = loader.load(getClass().getResource("/controllers/MsgPage.fxml").openStream());
			MsgController msgController = loader.getController();
			msgController.loadData("Request has been sent to HR!", 3);
			Scene scene = new Scene(root);
			primaryStage.setTitle("Message");
			primaryStage.setResizable(false);
			primaryStage.setOnCloseRequest(event1 -> event1.consume());
			primaryStage.setScene(scene);
			primaryStage.show();
		}
	}

	/**
	 * Method that set details of user for selected HR manager in his mailbox
	 * 
	 * @throws IOException
	 */
	public void setApprovedMsgToHRData() throws IOException {
		mth.setFromUserId(ChatClient.u1.getIdNumber());
		ClientUI.chat.accept("findHrByBusinessName:" + "\t" + cmbBusinessEmployer.getValue());
		mth.setToUserId(Integer.parseInt(ChatClient.findhrByBusinessName));
		String changeFormatDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		mth.setDate(changeFormatDate.toString());
		mth.setHour(LocalTime.now().getHour() + ":" + LocalTime.now().getMinute());
		mth.setMessage("user ID: " + txtId.getText() + " - " + txtFirstName.getText() + " " + txtLastName.getText()
				+ " wants to register under " + cmbBusinessEmployer.getValue() + " business");
		mth.setStatus("Waiting");
		mth.setRead(false);
	}

	/**
	 * Upload screen of Register Business Customer
	 * 
	 * @param primaryStage
	 * @throws Exception
	 */
	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/controllers/RegisterNewBusinessCustomerPage.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		primaryStage.setTitle("Register New Business Customer");
		primaryStage.show();
	}

	/**
	 * Initialize screen of register business customer
	 *
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		fillFields(searchIdBeforeRegisterBusinessController.getUser());
		try {
			ClientUI.chat.accept("getEmployers:");
			setComboBoxFirstRes();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		btnRegister.setDisable(true);
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
		txtBudget.setEditable(false);

	}

	/**
	 * Set monthly budget of employer
	 * 
	 * @param event
	 */
	@FXML
	void clickBusinessEmployer(ActionEvent event) {
		try {
			ClientUI.chat.accept("getBudget:" + "\t" + cmbBusinessEmployer.getValue());
			setComboBoxFirstRes();
		} catch (IOException e) {
			e.printStackTrace();
		}
		txtBudget.setText(String.valueOf(ChatClient.e1.getMonthlyBudget().toString()));
	}

	ObservableList<String> list;

	/**
	 * Set relevant employers in combo box
	 * 
	 * @throws IOException
	 */
	public void setComboBoxFirstRes() throws IOException {
		branch = ChatClient.u1.getRole().split(" ")[2];
		ArrayList<String> cmb = new ArrayList<String>();
		String[] details = (ChatClient.e1.getBusinessName()).split("\\t");
		for (String str : details)
			if (str.contains(branch))
				cmb.add(str);
		list = FXCollections.observableArrayList(cmb);
		cmbBusinessEmployer.setItems(list);
	}

}