package controllers;

import java.io.IOException;
import java.util.ArrayList;

import client.ChatClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * class for create shipment of order .
 * 
 * @author Or Hilo
 * @version 1.0 Build 100 December 29, 2021
 */

public class OrderShipmentController {

	/**
	 * shipmentValues save the shipment Details
	 */
	public static ArrayList<String> shipmentValues = new ArrayList<String>();
	/**
	 * numberOfParticipants save the number of participants in order
	 */
	public static int numberOfParticipants;

	/**
	 * basic use to know if the user want basic shipment
	 */
	private String basic;
	/**
	 * shared use to know if the user want shared shipment
	 */
	private String shared;
	/**
	 * flag use to know if the user want robot shipment
	 */
	private boolean flag = false;

	@FXML
	private Button btnBack;

	@FXML
	private Button btnNext;

	@FXML
	private RadioButton radBtnBasic;

	@FXML
	private RadioButton radBtnShared;

	@FXML
	private ToggleGroup shipmentType;

	@FXML
	private TextField txtAdd;

	@FXML
	private TextField txtRecName;

	@FXML
	private TextField txtRecPhone;

	@FXML
	private Label lblMsgAddress;

	@FXML
	private Label lblMsgRecName;

	@FXML
	private Label lblMsgRecPhone;

	@FXML
	private CheckBox cboxRobot;

	@FXML
	private Text txtEnterNumber;

	@FXML
	private TextField txtNumber;

	@FXML
	private Label lblMsgNumber;

	/**
	 * loadData: load data of customer
	 */
	public void loadData() {
		String[] userDetails = ChatClient.userInfo.split("\\t");
		if (userDetails[0].equals("Private_Customer"))
			radBtnShared.setDisable(true);
		btnNext.setDisable(true);
		txtEnterNumber.setVisible(false);
		txtNumber.setVisible(false);
	}

	/**
	 * Back: if the user press on Back button, the system return to the page of
	 * order time
	 * 
	 * @param event hide the current page and and return to order time page when
	 *              click on Back button.
	 * @throws IOException
	 */

	@FXML
	void Back(ActionEvent event) throws IOException {
		OrderTimeController.orderTimeValues.clear();
		((Node) event.getSource()).getScene().getWindow().hide(); // hide window
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/controllers/OrderTimePage.fxml").openStream());
		OrderTimeController orderTimePage = loader.getController();
		orderTimePage.loadData(CreateAnOrderController.orderValues.get(1));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Order Time");
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Next: if the user press on Next button, the system go to payment page
	 * 
	 * @param event hide the current page and and go to payment page when click on
	 *              Next button.
	 * @throws IOException
	 */
	@FXML
	void Next(ActionEvent event) throws IOException {
		lblMsgAddress.setText(null);
		lblMsgRecName.setText(null);
		lblMsgRecPhone.setText(null);
		String[] userDetails = ChatClient.userInfo.split("\\t");
		if (checkIfAddressValid() && checkIfRecNameValid() && checkIfRecPhoneValid()) {
			setValues(shipmentValues);
			if (shared != null)
				if (!checkParticipants())
					return;
			if (userDetails[0].equals("Business_Customer")) {
				((Node) event.getSource()).getScene().getWindow().hide(); // hide window
				FXMLLoader loader = new FXMLLoader();
				Stage primaryStage = new Stage();
				Pane root = loader.load(getClass().getResource("/controllers/PaymentPage.fxml").openStream());
				PaymentController paymentController = loader.getController();
				paymentController.loadData();
				Scene scene = new Scene(root);
				primaryStage.setTitle("Payment method");
				primaryStage.setOnCloseRequest(event1 -> event1.consume());
				primaryStage.setResizable(false);
				primaryStage.setScene(scene);
				primaryStage.show();
				return;
			}
			((Node) event.getSource()).getScene().getWindow().hide(); // hide window
			FXMLLoader loader = new FXMLLoader();
			Stage primaryStage = new Stage();
			Pane root = loader.load(getClass().getResource("/controllers/OrderInvoicePage.fxml").openStream());
			OrderInvoiceController orderInvoiceController = loader.getController();
			orderInvoiceController.loadData();
			Scene scene = new Scene(root);
			primaryStage.setOnCloseRequest(event1 -> event1.consume());
			primaryStage.setTitle("Order Invoice");
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.show();
		}
	}

	/**
	 * checkParticipants: check the number of participants that the user typed
	 * 
	 * @return true if the number valid, and false else
	 */
	public boolean checkParticipants() {

		String textOfParticipants = txtNumber.getText();

		if (textOfParticipants.equals("")) {
			WrongParticipants("Enter a number!");
			return false;
		}
		if (textOfParticipants.length() > 2) {
			WrongParticipants("Enter valid number!");
			return false;
		}
		if (!textOfParticipants.matches("[0-9]+")) {
			WrongParticipants("must contain just numbers!");
			return false;
		}
		numberOfParticipants = Integer.parseInt(textOfParticipants);
		if (numberOfParticipants > CreateAnOrderController.mapDish.size()) {
			WrongParticipants("More participants than dishes!");
			return false;
		}
		if (numberOfParticipants <= 1) {
			WrongParticipants("At least 2 participants!");
			return false;
		}
		return true;
	}

	/**
	 * WrongParticipants: print the message if there is wrong in number of
	 * participants
	 * 
	 * @param str get the message to print if there are wrong participants
	 */
	private void WrongParticipants(String str) {
		lblMsgNumber.setText(str);
		radBtnBasic.setSelected(false);
		radBtnShared.setSelected(false);
		btnNext.setDisable(true);
	}

	@FXML
	void selectAddress(ActionEvent event) {

	}

	@FXML
	void selectRecName(ActionEvent event) {

	}

	@FXML
	void selectRecPhone(ActionEvent event) {

	}

	/**
	 * selectBasic: if the user click on Basic shipment the system set the data and
	 * allows him to continue in Ordering process
	 * 
	 * @param event- not used
	 */
	@FXML
	void selectBasic(ActionEvent event) {
		basic = "Basic";
		shared = null;
		btnNext.setDisable(false);
		txtNumber.setText("");
		lblMsgNumber.setText("");
		txtEnterNumber.setVisible(false);
		txtNumber.setVisible(false);
	}

	/**
	 * selectShared: if the user click on Shared shipment the system set the data
	 * and allows him to continue in Ordering process
	 * 
	 * @param event-not used
	 */
	@FXML
	void selectShared(ActionEvent event) {
		basic = null;
		shared = "Shared";
		txtNumber.setText("");
		lblMsgAddress.setText("");
		lblMsgRecName.setText("");
		lblMsgNumber.setText("");
		txtEnterNumber.setVisible(true);
		txtNumber.setVisible(true);
		btnNext.setDisable(false);
	}

	/**
	 * selectShared: if the user click on Robot shipment the flag changing to true
	 * 
	 * @param event-not used
	 */
	@FXML
	void selectRobot(ActionEvent event) {
		if (!flag)
			flag = true;
		else
			flag = false;
	}

	/**
	 * checkIfAddressValid: check that the address is valid
	 * 
	 * @return true if the address is valid, else return false
	 */
	private boolean checkIfAddressValid() {
		String address;
		address = txtAdd.getText();
		if (address.equals("")) {
			WrongValueAddress("Enter address!");
			return false;
		}
		if (address.length() > 25) {
			WrongValueAddress("Enter valid address!");
			return false;
		}
		if (!address.matches("[a-zA-Z0-9,/ ]+")) {
			WrongValueAddress("Enter valid address!");
			return false;
		}
		return true;
	}

	/**
	 * checkIfRecNameValid: check if the name of the receiver is valid
	 * 
	 * @return true if the name of the receiver is valid, else return false
	 */
	private boolean checkIfRecNameValid() {
		String recName;
		recName = txtRecName.getText();
		if (recName.equals("")) {
			WrongValueRecName("Enter receiver name!");
			return false;
		}
		if (recName.length() > 25) {
			WrongValueRecName("Enter valid receiver name!");
			return false;
		}
		if (!recName.matches("[a-zA-Z ]+")) {
			WrongValueRecName("Enter valid receiver name!");
			return false;
		}
		return true;
	}

	/**
	 * checkIfRecPhoneValid: check if the phone number of the receiver is valid
	 * 
	 * @return true if the phone number of the receiver is valid, else return false
	 */
	private boolean checkIfRecPhoneValid() {
		String recPhone;
		recPhone = txtRecPhone.getText();
		if (recPhone.equals("")) {
			WrongValueRecPhone("Enter receiver phone!");
			return false;
		}
		if (recPhone.length() > 10) {
			WrongValueRecPhone("Enter valid receiver phone!");
			return false;
		}
		if (!recPhone.matches("[0-9]+")) {
			WrongValueRecPhone("Enter valid receiver phone!");
			return false;
		}
		return true;
	}

	/**
	 * WrongValueAddress: set the messages and the battens if the address is wrong
	 * 
	 * @param str get the message to print if the address is wrong
	 */
	private void WrongValueAddress(String str) {
		lblMsgAddress.setText(str);
		btnNext.setDisable(true);
		radBtnBasic.setSelected(false);
		radBtnShared.setSelected(false);
		txtNumber.setText("");
		lblMsgNumber.setText("");
		txtEnterNumber.setVisible(false);
		txtNumber.setVisible(false);
	}

	/**
	 * WrongValueAddress: set the messages and the battens if the name of the
	 * receiver is wrong
	 * 
	 * @param str get the message to print if the name of the receiver is wrong
	 */
	private void WrongValueRecName(String str) {
		lblMsgRecName.setText(str);
		btnNext.setDisable(true);
		radBtnBasic.setSelected(false);
		radBtnShared.setSelected(false);
		txtNumber.setText("");
		lblMsgNumber.setText("");
		txtEnterNumber.setVisible(false);
		txtNumber.setVisible(false);
	}

	/**
	 * WrongValueAddress: set the messages and the battens if the phone number of
	 * the receiver is wrong
	 * 
	 * @param str get the message to print if the phone number of the receiver is
	 *            wrong
	 */
	private void WrongValueRecPhone(String str) {
		lblMsgRecPhone.setText(str);
		btnNext.setDisable(true);
		radBtnBasic.setSelected(false);
		radBtnShared.setSelected(false);
		txtNumber.setText("");
		lblMsgNumber.setText("");
		txtEnterNumber.setVisible(false);
		txtNumber.setVisible(false);
	}

	/**
	 * setValues: set and save the data that the user typed
	 * 
	 * @param sValues save the data that the user typed
	 */
	public void setValues(ArrayList<String> sValues) {
		sValues.add(txtAdd.getText());
		sValues.add(txtRecName.getText());
		sValues.add(txtRecPhone.getText());
		if (basic != null)
			sValues.add(basic);
		else
			sValues.add(shared);
		sValues.add(String.valueOf(flag));
	}

}
