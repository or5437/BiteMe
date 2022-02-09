package controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import client.ChatClient;
import client.ClientUI;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import logic.User;

/**
 * Class that Describes BM Manager screen and his functionality
 * 
 * @author Tal Derai
 *
 */
public class BmManagerController implements Initializable {

	/**
	 * userID: connected Id user
	 */
	public static String userID;

	/**
	 * branch: North/South/Central
	 */
	public static String branch;

	@FXML
	private Label lblDate;

	@FXML
	private Label lblName;

	@FXML
	private Label lblPermission;

	@FXML
	private Label lblStatus;

	@FXML
	private Label lblTime;

	@FXML
	private AnchorPane PageBmManager;

	@FXML
	private Button btnUserDetails;

	@FXML
	private Button btnGenerateReport;

	@FXML
	private Button btnViewReport;

	@FXML
	private Button btnRegisterPrivate;

	@FXML
	private Button btnRegisterBussiness;

	@FXML
	private Button btnLogOut;

	@FXML
	private Button btnExit;

	@FXML
	private Button btnRegisterNewSup;

	@FXML
	private Button btnRegisterNewHR;

	@FXML
	private Label lblCountMsg;

	/**
	 * Initialize details in screen BmManager: date, time, permission, status
	 * 
	 * @param userId : id of user
	 * @throws IOException
	 */
	public void loadData(String userId) throws IOException {
		ImportUsers();
		userID = userId;
		if (ChatClient.ManagerMsgReadCount != 0)
			lblCountMsg.setText(ChatClient.ManagerMsgReadCount + " New message!");
		branch = ChatClient.u1.getRole().split(" ")[2];
		lblName.setText(ChatClient.u1.getFirstName() + "!");
		lblPermission.setText(ChatClient.u1.getType());
		lblStatus.setText(ChatClient.u1.getStatus());
		String changeFormatDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		lblDate.setText(changeFormatDate);
		initClock();

	}

	/**
	 * Initialize screen BM manager
	 *
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initClock();
	}

	/**
	 * Initialize clock on screen
	 * 
	 */
	private void initClock() {
		Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
			lblTime.setText(LocalDateTime.now().format(formatter));
		}), new KeyFrame(Duration.seconds(1)));
		clock.setCycleCount(Animation.INDEFINITE);
		clock.play();
	}

	/**
	 * When click on Logout, start Login Page
	 * 
	 * @param event
	 * @throws Exception
	 */
	@FXML
	void LogOut(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hide window
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/controllers/ClientLogInPage.fxml").openStream());
		Scene scene = new Scene(root);
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		primaryStage.setScene(scene);
		primaryStage.setTitle("User Login");
		primaryStage.show();
		ClientUI.chat.accept("logout:" + "\t" + ChatClient.u1.getUserName());
	}

	/**
	 * When click on button of change user details, start new page
	 * 
	 * @param event
	 * @throws Exception
	 */
	@FXML
	void Change_User_Details(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();

		ChangeUserDetailsController change_user_page_controller = new ChangeUserDetailsController();
		change_user_page_controller.start(primaryStage);
	}

	/**
	 * When click on button of Generate quarterly report, start new page
	 * 
	 * @param event
	 * @throws Exception
	 */
	@FXML
	void Generate_quarterly_report(ActionEvent event) throws Exception {

		((Node) event.getSource()).getScene().getWindow().hide(); // hide window
		Stage primaryStage = new Stage();
		GenerateQuarterlyReportController generateQuarterlyReportController = new GenerateQuarterlyReportController();
		generateQuarterlyReportController.start(primaryStage);
	}

	/**
	 * When click on button of Mail Box, start new page
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void Mail_Box(MouseEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide(); // hide window
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/controllers/ManagerMailBoxPage.fxml").openStream());
		ManagerMailBoxController managerMailBoxController = new ManagerMailBoxController();
		managerMailBoxController.loadData(Integer.parseInt(userID));
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		primaryStage.setTitle("Mail Box Branch Manager");
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * When click on button of Register new business customer, start new page
	 * 
	 * @param event
	 * @throws Exception
	 */
	@FXML
	void Register_new_bussiness_customer(ActionEvent event) throws Exception {
		ClientUI.chat.accept("get user details:");
		for (User user : ChatClient.dataUsers) {
			if (user.getRole().equals("Customer " + branch) && user.getType().equals("-")) {
				((Node) event.getSource()).getScene().getWindow().hide(); // hide window
				Stage primaryStage = new Stage();
				SearchIdBeforeRegisterBusinessCustomerController searchIdBeforeRegisterBusinessController = new SearchIdBeforeRegisterBusinessCustomerController();
				searchIdBeforeRegisterBusinessController.start(primaryStage);
				return;
			}
		}
		msgScreen(event);
	}

	/**
	 * When click on button of Register new private customer, start new page
	 * 
	 * @param event
	 * @throws Exception
	 */
	@FXML
	void Register_new_private_customer(ActionEvent event) throws Exception {
		ClientUI.chat.accept("get user details:");
		for (User user : ChatClient.dataUsers) {
			if (user.getRole().equals("Customer " + branch) && user.getType().equals("-")) {
				((Node) event.getSource()).getScene().getWindow().hide(); // hide window
				Stage primaryStage = new Stage();
				SearchIdBeforeRegisterPrivateController searchIdBeforeRegisterPrivateController = new SearchIdBeforeRegisterPrivateController();
				searchIdBeforeRegisterPrivateController.start(primaryStage);
				return;
			}
		}
		msgScreen(event);
	}

	/**
	 * When click on button of View Monthly Reports, start new page
	 * 
	 * @param event
	 * @throws Exception
	 */
	@FXML
	void View_Monthly_Reports(ActionEvent event) throws Exception {
		ClientUI.chat.accept("get user details:");
		((Node) event.getSource()).getScene().getWindow().hide(); // hide window
		Stage primaryStage = new Stage();
		SelectMonthForReportController selectMonthForReportController = new SelectMonthForReportController();
		selectMonthForReportController.start(primaryStage);
	}

	/**
	 * When click on button of Register new supplier, start new page
	 * 
	 * @param event
	 * @throws Exception
	 */
	@FXML
	void RegisterNewSup(ActionEvent event) throws Exception {
		ClientUI.chat.accept("get user details:");
		for (User user : ChatClient.dataUsers) {
			if (user.getRole().contains("Supplier") && user.getRole().contains(branch) && user.getType().equals("-")) {
				((Node) event.getSource()).getScene().getWindow().hide(); // hide window
				Stage primaryStage = new Stage();
				SearchIdBeforeRegisterSupplierController searchIdBeforeRegisterSupplierController = new SearchIdBeforeRegisterSupplierController();
				searchIdBeforeRegisterSupplierController.start(primaryStage);
				return;
			}
		}
		msgScreen(event);
	}

	/**
	 * Automatic upload screen if have error
	 * 
	 * @param event
	 * @throws IOException
	 */
	public void msgScreen(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide(); // hide window
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/controllers/MsgPage.fxml").openStream());
		MsgController msgController = loader.getController();
		msgController.loadData("There are no relevant users!", 3);
		Scene scene = new Scene(root);
		primaryStage.setTitle("Message");
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Update Users table of branch manager
	 * 
	 * @throws IOException
	 */
	private void ImportUsers() throws IOException {
		ClientUI.chat.accept("get user details:");

	}
}
