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
 * CustomerController implements the HomePage of every customer.
 * 
 * @author Amit Kempner
 * @version 1.0 Build 100 December 27, 2021
 */
public class CustomerController implements Initializable {

	/** userIdNumber: using to save the current userId. */
	private static String userIdNumber;
	/** customerType: using to save the type of customer : private or business. */
	private static String customerType;

	@FXML
	private Button Logout;

	@FXML
	private Button btnCreateAnOrder;

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
	 * initialize: if user in "Frozen" status set the button of create an order
	 * disabled.
	 *
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (ChatClient.u1.getStatus().equals("Frozen")) {
			btnCreateAnOrder.setDisable(true);
		}
	}

	/**
	 * loadData: load all information that needed at Initial opening of page.
	 * 
	 * @param typeOfCustomer get the type of customer : private or business.
	 * @param idNumber       get the userId of the current user.
	 */
	public void loadData(String typeOfCustomer, String idNumber) {
		userIdNumber = idNumber;
		customerType = typeOfCustomer;
		if (ChatClient.customerMsgReadCount != 0)
			lblCountMsg.setText(ChatClient.customerMsgReadCount + " New message!");
		lblName.setText(ChatClient.u1.getFirstName() + "!");
		lblPermission.setText(ChatClient.u1.getType());
		lblStatus.setText(ChatClient.u1.getStatus());
		String changeFormatDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		lblDate.setText(changeFormatDate);
		initClock();
	}

	/** initClock: func that display the current time at Customer Page. */
	private void initClock() {
		Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
			lblTime.setText(LocalDateTime.now().format(formatter));
		}), new KeyFrame(Duration.seconds(1)));
		clock.setCycleCount(Animation.INDEFINITE);
		clock.play();
	}

	/**
	 * selectCreateAnOrder: func that move to W4C card page.
	 * 
	 * @param event If the user press on create an order button he \ she will move
	 *              to identify by W4C card.
	 * @throws IOException
	 */
	@FXML
	void selectCreateAnOrder(ActionEvent event) throws IOException {
		checkEmptyData();
		((Node) event.getSource()).getScene().getWindow().hide(); // hide window
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/controllers/W4CcardPage.fxml").openStream());
		W4CcardController w4cCardController = loader.getController();
		w4cCardController.loadData(customerType, userIdNumber);
		primaryStage.setTitle("W4C Card");
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	/**
	 * checkEmptyData: func that clear all the data structure that used before to
	 * other orders.
	 */
	private void checkEmptyData() {
		if (!CreateAnOrderController.mapDish.isEmpty()) {
			CreateAnOrderController.mapDish.clear();
		}
		if (!CreateAnOrderController.orderValues.isEmpty()) {
			CreateAnOrderController.orderValues.clear();
		}
		if (!OrderTimeController.orderTimeValues.isEmpty()) {
			OrderTimeController.orderTimeValues.clear();
		}
		if (!OrderShipmentController.shipmentValues.isEmpty()) {
			OrderShipmentController.shipmentValues.clear();
		}
	}

	/**
	 * selectMailBox: func that move the specific customer to his \ her MailBox.
	 * 
	 * @param event If the user press on selectMailBox he \ she will move to
	 *              Customer MailBox.
	 * @throws IOException
	 */
	@FXML
	void selectMailBox(MouseEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide(); // hide window
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/controllers/CustomerMailBoxPage.fxml").openStream());
		CustomerMailBoxController customerMailBoxController = new CustomerMailBoxController();
		customerMailBoxController.loadData(Integer.parseInt(userIdNumber), customerType);
		primaryStage.setTitle("Mail Box Customer");
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Logout: func that Logout the user and return the user to login page.
	 * 
	 * @param event If the user press on Logot, the system disconnect him / her from
	 *              BiteMe system.
	 * @throws IOException
	 */
	@FXML
	void Logout(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hide window
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/controllers/ClientLogInPage.fxml").openStream());
		Scene scene = new Scene(root);
		primaryStage.setTitle("User Login");
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		primaryStage.setScene(scene);
		primaryStage.show();
		ClientUI.chat.accept("logout:" + "\t" + ChatClient.u1.getUserName());
	}
}
