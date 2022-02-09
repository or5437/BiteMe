package controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

import client.ChatClient;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.MsgToSupplier;
import logic.User;

/**
 * class for user's login .
 * 
 * @author Or Hilo
 * @version 1.0 Build 100 December 29, 2021
 */
public class ClientLoginController {

	/**
	 * mts: save data of supplier and date to send message of receipt of a monthly
	 * bill to the supplier
	 */
	public static MsgToSupplier mts = new MsgToSupplier(0, 0, 0, null, null, null, null, false);

	/**
	 * rand: a variable used to draw a number.
	 */
	Random rand = new Random();

	/**
	 * isConnected: check if the connection was successful.
	 */
	Stage primaryStage;

	@FXML
	private AnchorPane UserLoginBackground;

	@FXML
	private Text ContactUS;

	@FXML
	private Text UserLoginLabel;

	@FXML
	private Button btnExit;

	@FXML
	private Button btnLogin;

	@FXML
	private PasswordField textfieldPassword;

	@FXML
	private TextField textfieldUSerName;

	@FXML
	private Label LabelErrormsg;

	/**
	 * @return the typed user name
	 */
	private String getUserName() {
		return textfieldUSerName.getText();
	}

	/**
	 * @return the typed user password
	 */
	private String getUserPassword() {
		return textfieldPassword.getText();
	}

	/**
	 * Login: if the user press on Login button, the system will go to a
	 * user-enabled page according to the role of the user in the system.
	 * 
	 * @param event hide the current page and and goes to the user-enabled screen
	 *              when click on login button.
	 * @throws Exception
	 */
	@FXML
	void clickLogin(ActionEvent event) throws Exception {
		ChatClient.userInfo = null;
		LabelErrormsg.setText("");
		String UserName, Password;
		User u = null;
		boolean isLoggedIn = false;
		UserName = getUserName();
		Password = getUserPassword();
		if (UserName.trim().isEmpty()) {
			LabelErrormsg.setText("please enter UserName");
			return;
		}
		if (Password.trim().isEmpty()) {
			LabelErrormsg.setText("Please enter Password");
			return;
		}
		ClientUI.chat.accept("getCustomerDetails:" + "\t" + UserName);
		if (ChatClient.userInfo == null) {
			LabelErrormsg.setText("User is not registered");
			return;
		}
		String[] userDetails = ChatClient.userInfo.split("\\t");
		FXMLLoader loader = new FXMLLoader();
		Pane root;
		Scene scene;
		ClientUI.chat.accept("get user details:");
		for (User i : ChatClient.dataUsers)
			if (i.getIdNumber() == Integer.parseInt(userDetails[1])) {
				isLoggedIn = i.getIsLoggedIn();
				u = i;
			}
		if (isLoggedIn) {
			LabelErrormsg.setText("You are already logged in");
			return;
		}
		if (u.getType().equals("-")) {
			LabelErrormsg.setText("User is not registered");
			return;
		}
		// check for valid input (Username & Password)

		else {
			ClientUI.chat.accept("login:" + "\t" + UserName + "\t" + Password);

			/* User not found because userName incorrect or password incorrect */

			if (ChatClient.u1.getType() == null)
				LabelErrormsg.setText("User is not found");
			else if ((ChatClient.u1.getStatus().equals("Locked"))) {
				LabelErrormsg.setText("The user is locked");
				ClientUI.chat.accept("logout:" + "\t" + ChatClient.u1.getUserName());
			} else {
				switch (ChatClient.u1.getType()) {
				case "Private_Customer":
					ClientUI.chat.accept("getNumberCustomerMsgNotRead:" + "\t" + userDetails[1]);
					((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
					primaryStage = new Stage();
					loader = new FXMLLoader();
					root = loader.load(getClass().getResource("/controllers/CustomerPage.fxml").openStream());
					CustomerController pCustomerController = loader.getController();
					pCustomerController.loadData(userDetails[0], userDetails[1]);
					scene = new Scene(root);
					primaryStage.setResizable(false);
					primaryStage.setTitle("Private Customer");
					primaryStage.setScene(scene);
					primaryStage.setOnCloseRequest(event1 -> event1.consume());
					primaryStage.show();
					break;
				case "BM_Manager":
					ClientUI.chat.accept("getNumberManagerMsgNotRead:" + "\t" + userDetails[1]);
					((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
					loader = new FXMLLoader();
					primaryStage = new Stage();
					root = loader.load(getClass().getResource("/controllers/BmManagerPage.fxml").openStream());
					BmManagerController bmManagerController = loader.getController();
					bmManagerController.loadData(userDetails[1]);
					scene = new Scene(root);
					primaryStage.setTitle("Branch Manager");
					primaryStage.setResizable(false);
					primaryStage.setOnCloseRequest(event1 -> event1.consume());
					primaryStage.setScene(scene);
					primaryStage.show();
					break;
				case "Supplier":
					ClientUI.chat.accept("FindResID:" + "\t" + ChatClient.u1.getIdNumber());
					ClientUI.chat.accept("getNumberSupplierMsgNotRead:" + "\t" + userDetails[1]);
					((Node) event.getSource()).getScene().getWindow().hide(); // hide window
					primaryStage = new Stage();
					root = loader.load(getClass().getResource("/controllers/SupplierPage.fxml").openStream());
					SupplierController supplierController = loader.getController();
					supplierController.loadData(userDetails[1]);
					scene = new Scene(root);
					primaryStage.setTitle("Supplier");
					primaryStage.setResizable(false);
					primaryStage.setOnCloseRequest(event1 -> event1.consume());
					primaryStage.setScene(scene);
					primaryStage.show();
					break;
				case "CEO":
					ClientUI.chat.accept("getNumberCeoMsgNotRead:" + "\t" + userDetails[1]);
					((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
					loader = new FXMLLoader();
					primaryStage = new Stage();
					root = loader.load(getClass().getResource("/controllers/CeoPage.fxml").openStream());
					CeoController ceoController = loader.getController();
					ceoController.loadData(userDetails[1]);
					primaryStage.setTitle("CEO");
					scene = new Scene(root);
					primaryStage.setResizable(false);
					primaryStage.setOnCloseRequest(event1 -> event1.consume());
					primaryStage.setScene(scene);
					primaryStage.show();
					break;
				case "HR_Manager":
					ClientUI.chat.accept("FindBusinessName:" + "\t" + ChatClient.u1.getIdNumber());
					ClientUI.chat.accept("getNumberHrMsgNotRead:" + "\t" + userDetails[1]);
					((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
					primaryStage = new Stage();
					root = loader.load(getClass().getResource("/controllers/HrPage.fxml").openStream());
					HrController hrController = loader.getController();
					hrController.loadData(userDetails[1]);
					scene = new Scene(root);
					primaryStage.setResizable(false);
					primaryStage.setTitle("HR Manager");
					primaryStage.setOnCloseRequest(event1 -> event1.consume());
					primaryStage.setScene(scene);
					primaryStage.show();
					break;
				case "Business_Customer":
					ClientUI.chat.accept("getNumberCustomerMsgNotRead:" + "\t" + userDetails[1]);
					((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
					primaryStage = new Stage();
					root = loader.load(getClass().getResource("/controllers/CustomerPage.fxml").openStream());
					CustomerController bCustomerController = loader.getController();
					bCustomerController.loadData(userDetails[0], userDetails[1]);
					scene = new Scene(root);
					primaryStage.setTitle("Business Customer");
					primaryStage.setResizable(false);
					primaryStage.setOnCloseRequest(event1 -> event1.consume());
					primaryStage.setScene(scene);
					primaryStage.show();
					break;
				}
			}
		}
	}

	/**
	 * clickExit: if the user press on clickExit button, the system will
	 * user-enabled page according to the role of the user in the system.
	 * 
	 * @param event- not used
	 * 
	 * @throws Exception
	 */
	@FXML
	void clickExit(ActionEvent event) throws IOException {
		ClientUI.chat.accept("disconnected:");
		System.out.println("Exit button Click!");
		System.exit(0);

	}

	/**
	 * Start: open the user login screen
	 * 
	 * @param primaryStage
	 *
	 * @throws Exception
	 */
	public void start(Stage primaryStage) throws Exception {
		ClientUI.chat.accept("connection details:");
		Parent root = FXMLLoader.load(getClass().getResource("/controllers/ClientLogInPage.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("User Login");
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		primaryStage.show();
		checkIfNeedToSendBillToSupplier();

	}

	/**
	 * checkIfNeedToSendBillToSupplier: check if the supplier didn't get the bill in
	 * the day 1 and need to send to him.
	 * 
	 * @throws IOException
	 */
	public void checkIfNeedToSendBillToSupplier() throws IOException {
		ArrayList<Integer> saveAllSupplierId = new ArrayList<Integer>();
		int day = LocalDate.now().getDayOfMonth();
		String changeFormatDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		ClientUI.chat.accept("checkIfAlreadyGotMonthlyBill:" + "\t" + "0" + "\t" + changeFormatDate);
		if (day == 1 && ChatClient.isFirstSendBill) {
			float random = (float) (0.07 + rand.nextFloat() * (0.12 - 0.07));
			ClientUI.chat.accept("setComission:" + "\t" + String.valueOf(random));
			ClientUI.chat.accept("getAllSupplierId:");
			saveAllSupplierId = ChatClient.allSupplierId;
			for (int i : saveAllSupplierId) {
				setMsgToSupplierData(i);
				String msgToSupplier = "msgToSupplierFromBiteMe:" + "\t" + mts.getOrderId() + "\t"
						+ String.valueOf(mts.getFromUserId()) + "\t" + String.valueOf(mts.getToSupplierId()) + "\t"
						+ mts.getDate() + "\t" + mts.getHour() + "\t" + mts.getMessage() + "\t" + mts.getStatus() + "\t"
						+ String.valueOf(mts.isRead());
				ClientUI.chat.accept(msgToSupplier);
			}
		}

	}

	/**
	 * setMsgToSupplierData: send msg to supplier supplierID with the date and the
	 * message that he get the monthly bill from the system.
	 * 
	 * @param supplierID get the ID of the supplier
	 */
	public void setMsgToSupplierData(int supplierID) {
		mts.setOrderId(0);
		mts.setFromUserId(0);
		mts.setToSupplierId(supplierID);
		String changeFormatDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		mts.setDate(changeFormatDate.toString());
		mts.setHour(LocalTime.now().getHour() + ":" + LocalTime.now().getMinute());
		mts.setMessage("You recived The monthly bill from BiteMe system!");
		mts.setStatus("-");
		mts.setRead(false);
	}
}
