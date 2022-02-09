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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * class for HR page
 * 
 * @author Raz
 * @version 1.0 Build 100 December 27, 2021
 */
public class HrController implements Initializable {

	/**
	 * String of business name.
	 */
	public static String businessName;

	/**
	 * String of current userID number
	 */
	public static String userID;

	@FXML
	private Label lblRegBlock;

	@FXML
	private Button btnMailBox;

	@FXML
	private Label lblCountMsg;

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

	/**
	 * first data load of the page, set everything for start.
	 * 
	 * @param userId get the current HR userID number
	 */
	public void loadData(String userId) {
		userID = userId;
		if (ChatClient.HrMsgReadCount != 0)
			lblCountMsg.setText(ChatClient.HrMsgReadCount + " new messages!");
		businessName = ChatClient.businessName;
		lblName.setText(ChatClient.u1.getFirstName() + "!");
		lblPermission.setText(ChatClient.u1.getType());
		lblStatus.setText(ChatClient.u1.getStatus());
		String changeFormatDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		lblDate.setText(changeFormatDate);
		initClock();
	}

	/**
	 * initialize clock
	 *
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initClock();
	}

	/**
	 * set clock function
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
	 * Click On register business, move to business registration page only if the
	 * business is not already registered.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void btnRegBusiness(ActionEvent event) throws IOException {
		ClientUI.chat.accept("CheckIfBusinessReg:" + "\t" + businessName);
		if (ChatClient.businessRegisterd) {
			lblRegBlock.setText("You already registerd your business!");
		} else {
			((Node) event.getSource()).getScene().getWindow().hide(); // hide window
			FXMLLoader loader = new FXMLLoader();
			Stage primaryStage = new Stage();
			Pane root = loader
					.load(getClass().getResource("/controllers/BusinessRegistrationHrPage.fxml").openStream());
			BusinessRegistrationHrController businessRegistrationHrController = loader.getController();
			businessRegistrationHrController.loadData(businessName);
			Scene scene = new Scene(root);
			primaryStage.setResizable(false);
			primaryStage.setOnCloseRequest(event1 -> event1.consume());
			primaryStage.setTitle("Business HR Registration");
			primaryStage.setScene(scene);
			primaryStage.show();
		}
	}

	/**
	 * Click On mail box, move to HR mail box page.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void Mail_Box(MouseEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide(); // hide window
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/controllers/HrMailBoxPage.fxml").openStream());
		HrMailBoxController hrMailBoxController = new HrMailBoxController();
		hrMailBoxController.loadData(Integer.parseInt(userID), businessName);
		Scene scene = new Scene(root);
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		primaryStage.setTitle("Mail Box HR");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * logout from HR user and go back to login screen
	 * 
	 * @param event
	 * @throws Exception
	 */
	@FXML
	void Logout(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hide window
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/controllers/ClientLogInPage.fxml").openStream());
		Scene scene = new Scene(root);
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		primaryStage.setTitle("User Login");
		primaryStage.setScene(scene);
		primaryStage.show();
		ClientUI.chat.accept("logout:" + "\t" + ChatClient.u1.getUserName());
	}
}
