package controllers;

import java.io.IOException;
import javafx.util.Duration;

import client.ChatClient;
import client.ClientUI;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * W4CcardController implements the W4C Code for private and business customers.
 * 
 * @author Amit Kempner
 * @version 1.0 Build 100 December 27, 2021
 */
public class W4CcardController {

	/** userIdNumber: using to save the current userId. */
	private static String userIdNumber;
	/** customerType: using to save the type of customer : private or business. */
	private static String customerType;

	@FXML
	private Button btnBack;

	@FXML
	private Button btnNext;

	@FXML
	private Button btnQR;

	@FXML
	private Label lblMsg;

	@FXML
	private TextField txtW4C;

	/**
	 * loadData: load all information that needed at Initial opening of page.
	 * 
	 * @param typeOfCustomer get the type of customer : private or business.
	 * @param idNumber       get the userId of the current user.
	 */
	public void loadData(String typeOfCustomer, String idNumber) {
		userIdNumber = idNumber;
		customerType = typeOfCustomer;
		btnNext.setDisable(true);
	}

	/**
	 * Back: if the user press on Back button, the system will return to previous
	 * page.
	 * 
	 * @param event hide the current page and return to previous page when click on
	 *              back button.
	 * @throws IOException
	 */
	@FXML
	void Back(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide(); // hide window
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/controllers/CustomerPage.fxml").openStream());
		Scene scene = new Scene(root);
		if (ChatClient.u1.getType().equals("Private_Customer")) {
			CustomerController pCustomerController = loader.getController();
			pCustomerController.loadData(customerType, userIdNumber);
		} else {
			CustomerController bCustomerController = loader.getController();
			bCustomerController.loadData(ChatClient.u1.getType(), String.valueOf(ChatClient.u1.getIdNumber()));
		}
		primaryStage.setTitle(customerType.split("_")[0] + " " + customerType.split("_")[1]);
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * EnterW4C: When user enter his\her W4C Card manualy.
	 * 
	 * @param event If the user click on everyKey the Next button change to enable.
	 */
	@FXML
	void EnterW4C(KeyEvent event) {
		lblMsg.setText("");
		btnNext.setDisable(false);
	}

	/**
	 * Next: user moved by Next button to create an order page.
	 * 
	 * @param If user prees Next he move to the next page of create an order.
	 * @throws IOException
	 */
	@FXML
	void Next(ActionEvent event) throws IOException {
		lblMsg.setText("");
		if (customerType.equals("Private_Customer")) {
			ClientUI.chat.accept("getW4CcodePrivate:" + "\t" + userIdNumber);
			if (txtW4C.getText().equals(ChatClient.w4cCodePrivate))
				moveCreateAnOrder(event);
			else {
				lblMsg.setText("Wrong code!");
				return;
			}
		}
		if (customerType.equals("Business_Customer")) {
			ClientUI.chat.accept("getBusinessName:" + "\t" + userIdNumber);
			ClientUI.chat.accept("getW4CcodeBusiness:" + "\t" + ChatClient.businessNameByCustomer);
			if (txtW4C.getText().equals(ChatClient.w4cCodeBusiness))
				moveCreateAnOrder(event);
			else {
				lblMsg.setText("Wrong code!");
				return;
			}
		}
	}

	/**
	 * selectQR: userMoved to create an order page if QR code exist automatically.
	 * 
	 * @param event if the user press on selectQR button his\her code will load and
	 *              move to create an order page.
	 * @throws Exception
	 */
	@FXML
	void selectQR(ActionEvent event) throws Exception {
		lblMsg.setText("");
		PauseTransition pause = new PauseTransition(Duration.seconds(1));
		if (customerType.equals("Private_Customer")) {
			ClientUI.chat.accept("getW4CcodePrivate:" + "\t" + userIdNumber);
			if (ChatClient.w4cCodePrivate != null) {
				txtW4C.setText(ChatClient.w4cCodePrivate);
				pause.setOnFinished(f -> {
					try {
						moveCreateAnOrder(event);
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
				pause.play();
			}
		}
		if (customerType.equals("Business_Customer")) {
			ClientUI.chat.accept("getBusinessName:" + "\t" + userIdNumber);
			ClientUI.chat.accept("getW4CcodeBusiness:" + "\t" + ChatClient.businessNameByCustomer);
			if (ChatClient.w4cCodeBusiness != null)
				txtW4C.setText(ChatClient.w4cCodeBusiness);
			pause.setOnFinished(f -> {
				try {
					moveCreateAnOrder(event);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			pause.play();
		}
	}

	/**
	 * moveCreateAnOrder: move the user automatically to create an order page.
	 * 
	 * @param event get the event from selectQR and move automatically to create an
	 *              order page.
	 * @throws IOException
	 */
	private void moveCreateAnOrder(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide(); // hide window
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/controllers/CreateAnOrderPage.fxml").openStream());
		CreateAnOrderController createAnOrderController = loader.getController();
		createAnOrderController.loadBranchList();
		createAnOrderController.loadUserDetails(customerType, userIdNumber);
		primaryStage.setTitle("Create An Order");
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
