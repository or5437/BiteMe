package controllers;

import java.io.IOException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import client.ChatClient;
import client.ClientUI;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Description of Ceo Controller The class is designed to control the CEO's
 * page, and will operate the relevant functions according to her choices.
 * 
 * @author Michael Ravich
 * @version Version 1.0 Build 100 December 27, 2021
 */
public class CeoController {
	/**
	 * Description of userID - used to save the relevant user ID, for mail box
	 * implementation
	 */
	public static String userID;

	@FXML
	private Button btnViewReports;

	@FXML
	private Button Logout;

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
	 * Description of loadData (String userId)
	 * 
	 * @param userId - the id of the current logged user
	 * @throws IOException
	 */
	public void loadData(String userId) throws IOException {
		userID = userId;
		if (ChatClient.CeoMsgReadCount != 0)
			lblCountMsg.setText(ChatClient.CeoMsgReadCount + " new messages!");
		lblName.setText(ChatClient.u1.getFirstName() + "!");
		lblPermission.setText(ChatClient.u1.getType());
		lblStatus.setText(ChatClient.u1.getStatus());
		String changeFormatDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		lblDate.setText(changeFormatDate);
		initClock();

	}

	/**
	 * Description of initClock() set a Timeline to show the current time and date
	 * in this page. (for the ceo)
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
	 * Description of Mail_BJox(MouseEvent event) - the function opens the mail box
	 * page of the ceo when he clicks on it
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void Mail_Box(MouseEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide(); // hide window
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/controllers/CeoMailBoxPage.fxml").openStream());
		CeoMailBoxController ceoMailBoxController = new CeoMailBoxController();
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		primaryStage.setTitle("Mail Box CEO");
		ceoMailBoxController.loadData(Integer.parseInt(userID));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Description of Logout(ActionEvent event) - the function make logout process
	 * for the ceo, and updates this info in the DB (mysql)
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
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setTitle("User Login");
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		ClientUI.chat.accept("logout:" + "\t" + ChatClient.u1.getUserName());
	}

	/**
	 * Description of View_Quarterly_Reports(ActionEvent event)- the method opens
	 * the quarterly reports page
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void View_Quarterly_Reports(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide(); // hide window
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		Pane root = loader
				.load(getClass().getResource("/controllers/CeoPresentQuarterlyReportsPage.fxml").openStream());
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		primaryStage.setTitle("View quarterly reports");
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
