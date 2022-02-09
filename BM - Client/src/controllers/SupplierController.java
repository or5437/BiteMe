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
 * class for supplier page
 * 
 * @author Raz Avraham
 * @version 1.0 Build 100 December 27, 2021
 */
public class SupplierController implements Initializable {

	/**
	 * user ID number of current supplier
	 */
	@SuppressWarnings("unused")
	private static int userIdNumber;

	/**
	 * restaurant ID of current supplier
	 */
	public static int resID;

	@FXML
	private Button btnMailBox;

	@FXML
	private Label lblCountMsg;

	@FXML
	private Button btnLogout;

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
	 * first data load of page. set variables.
	 * 
	 * @param userId get user id of the current supplier.
	 */
	public void loadData(String userId) {
		userIdNumber = Integer.parseInt(userId);
		if (ChatClient.supplierMsgReadCount != 0)
			lblCountMsg.setText(ChatClient.supplierMsgReadCount + " new messages!");
		lblName.setText(ChatClient.u1.getFirstName() + "!");
		lblPermission.setText(ChatClient.u1.getType());
		lblStatus.setText(ChatClient.u1.getStatus());
		String changeFormatDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		lblDate.setText(changeFormatDate);
		resID = ChatClient.resID;
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
	 * clock initialization
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
	 * click on mail box, move to supplier mailbox page.
	 * 
	 * @param event
	 * @throws Exception
	 */
	@FXML
	void Mail_Box(MouseEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();
		SupplierMailBoxController supplierMailBoxController = new SupplierMailBoxController();
		supplierMailBoxController.start(primaryStage);
	}

	/**
	 * click on add new dish button, move to add new dish page.
	 * 
	 * @param event
	 * @throws Exception
	 */
	@FXML
	void btnAddNewDish(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();
		AddNewDishController addNewDishController = new AddNewDishController();
		addNewDishController.start(primaryStage);
	}

	/**
	 * click on update dish button, move to update dish page.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void btnUpdateDish(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide(); // hide window
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		primaryStage.setTitle("Update a dish");
		Pane root = loader.load(getClass().getResource("/controllers/UpdateDishPage.fxml").openStream());
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * logout from Supplier user and go back to login screen.
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
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		primaryStage.setResizable(false);
		ClientUI.chat.accept("logout:" + "\t" + ChatClient.u1.getUserName());
	}

}
