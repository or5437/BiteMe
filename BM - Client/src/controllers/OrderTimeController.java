package controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import client.ChatClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * OrderTimeController implements the time that customer wants get the order.
 * 
 * @author Amit Kempner
 * @version 1.0 Build 100 December 27, 2021
 */
public class OrderTimeController {

	/** ta: string of "TakeAway" */
	private String ta;
	/** shipment: string of "Shipment" */
	private String shipment;
	/** isShipment: check if the order with shipment or not. */
	public static boolean isShipment = false;
	/** orderTimeValues: ArrayList of all values of Order Time. */
	public static ArrayList<String> orderTimeValues = new ArrayList<String>();

	@FXML
	private DatePicker datePickerDate;

	@FXML
	private Label labelRes;

	@FXML
	private Label lblMsgDate;

	@FXML
	private Label lblMsgTime;

	@FXML
	private RadioButton radBtnShip;

	@FXML
	private RadioButton radBtnTA;

	@FXML
	private TextField txtHours;

	@FXML
	private TextField txtMinutes;

	@FXML
	private Button btnNext;

	/**
	 * loadData: load all information that needed at Initial opening of page.
	 * 
	 * @param str get the name of restaurant that customer wants to order from
	 *            there.
	 */
	public void loadData(String str) {
		labelRes.setText(str);
		txtHours.setDisable(true);
		txtMinutes.setDisable(true);
		labelRes.setDisable(true);
		radBtnTA.setDisable(true);
		radBtnShip.setDisable(true);
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
	void Back(ActionEvent event) throws Exception {
		CreateAnOrderController.orderValues.clear();
		CreateAnOrderController.mapDish.clear();
		((Node) event.getSource()).getScene().getWindow().hide(); // hide window
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/controllers/CreateAnOrderPage.fxml").openStream());
		CreateAnOrderController CreateAnOrder = loader.getController();
		CreateAnOrder.loadBranchList();
		Scene scene = new Scene(root);
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		primaryStage.setTitle("Create an Order");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Next: user moved by Next button to page that match to his Type and match to
	 * his delivery type.
	 * 
	 * @param event If user prees Next he move to the next page accordingly.
	 * @throws IOException
	 */
	@FXML
	void Next(ActionEvent event) throws IOException {
		LocalTime currentTime = LocalTime.now();
		LocalDate currentDate = LocalDate.now();
		if (!checkIfTimeIsValid(currentTime, currentDate)) {
			orderTimeValues.clear();
			return;
		}
		setValues(orderTimeValues);
		String[] userDetails = ChatClient.userInfo.split("\\t");
		switch (userDetails[0]) {
		case "Private_Customer":
			if (!isShipment) {
				((Node) event.getSource()).getScene().getWindow().hide(); // hide window
				FXMLLoader loader = new FXMLLoader();
				Stage primaryStage = new Stage();
				Pane root = loader.load(getClass().getResource("/controllers/OrderInvoicePage.fxml").openStream());
				OrderInvoiceController orderInvoiceController = loader.getController();
				orderInvoiceController.loadData();
				Scene scene = new Scene(root);
				primaryStage.setTitle("Order Invoice");
				primaryStage.setResizable(false);
				primaryStage.setOnCloseRequest(event1 -> event1.consume());
				primaryStage.setScene(scene);
				primaryStage.show();
			}
			if (isShipment) {
				((Node) event.getSource()).getScene().getWindow().hide(); // hide window
				FXMLLoader loader = new FXMLLoader();
				Stage primaryStage = new Stage();
				Pane root = loader.load(getClass().getResource("/controllers/OrderShipmentPage.fxml").openStream());
				OrderShipmentController orderShipmentController = loader.getController();
				orderShipmentController.loadData();
				Scene scene = new Scene(root);
				primaryStage.setTitle("Order Shipment");
				primaryStage.setOnCloseRequest(event1 -> event1.consume());
				primaryStage.setResizable(false);
				primaryStage.setScene(scene);
				primaryStage.show();
			}
			break;

		case "Business_Customer":
			if (!isShipment) {
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
			}
			if (isShipment) {
				((Node) event.getSource()).getScene().getWindow().hide(); // hide window
				FXMLLoader loader = new FXMLLoader();
				Stage primaryStage = new Stage();
				Pane root = loader.load(getClass().getResource("/controllers/OrderShipmentPage.fxml").openStream());
				OrderShipmentController orderShipmentController = loader.getController();
				orderShipmentController.loadData();
				Scene scene = new Scene(root);
				primaryStage.setTitle("Order Shipment");
				primaryStage.setOnCloseRequest(event1 -> event1.consume());
				primaryStage.setResizable(false);
				primaryStage.setScene(scene);
				primaryStage.show();
			}
			break;
		}
	}

	/**
	 * checkIfTimeIsValid: func that check the entered time by customer.
	 * 
	 * @param currentTime get the currentTime
	 * @param currentDate get the currentDate
	 * @return return true if Entered time is valid, return false if the time is not
	 *         correct.
	 */
	private boolean checkIfTimeIsValid(LocalTime currentTime, LocalDate currentDate) {
		LocalDate date = datePickerDate.getValue();
		String hourValue = txtHours.getText();
		String MinuteValue = txtMinutes.getText();

		if (hourValue.equals("") || MinuteValue.equals("")) {
			wrongTime("Enter the time!");
			return false;
		}

		if (date.getDayOfMonth() == currentDate.getDayOfMonth() && date.getMonth() == currentDate.getMonth()
				&& date.getYear() == currentDate.getYear()) {

			if (!hourValue.matches("[0-9]+")) {
				wrongTime("must contain only numbers!");
				return false;
			}
			if (!MinuteValue.matches("[0-9]+")) {
				wrongTime("must contain only numbers!");
				return false;
			}
			if (hourValue.length() > 2) {
				wrongTime("contains up to 2 characters!");
				return false;
			}

			if (MinuteValue.length() > 2) {
				wrongTime("contains up to 2 characters!");
				return false;
			}

			int hour = Integer.parseInt(hourValue);
			int minute = Integer.parseInt(MinuteValue);

			if (hour < 0 || hour > 23) {
				wrongTime("Enter valid hour!");
				return false;
			}

			if ((minute - currentTime.getMinute() < 0 && hour == currentTime.getHour()) || minute > 59) {
				wrongTime("Enter valid Minutes!");
				return false;
			}

			if (hour < 8 || hour > 23) {
				wrongTime("Orders can be made between 8:00-23:00!");
				return false;
			}

			if (hour == 23 && minute > 0) {
				wrongTime("Orders can be made between 8:00-23:00!");
				return false;
			}

			if (hour - currentTime.getHour() < 0) {
				wrongTime("Enter valid hour!");
				return false;
			}
			correctTime();
		} else {
			if (!hourValue.matches("[0-9]+")) {
				wrongTime("must contains just numbers!");
				return false;
			}
			if (!MinuteValue.matches("[0-9]+")) {
				wrongTime("must contains just numbers!");
				return false;
			}
			if (hourValue.length() > 2) {
				wrongTime("contains up to 2 characters!");
				return false;
			}

			if (MinuteValue.length() > 2) {
				wrongTime("contains up to 2 characters!");
				return false;
			}

			int hour = Integer.parseInt(hourValue);
			int minute = Integer.parseInt(MinuteValue);

			if (hour < 8 || hour > 23) {
				wrongTime("Orders can be made between 8:00-23:00!");
				return false;
			}

			if (hour == 23 && minute > 0) {
				wrongTime("Orders can be made between 8:00-23:00!");
				return false;
			}

			if (minute < 0 || minute > 59) {
				wrongTime("Enter valid Minutes!");
				return false;
			}
			correctTime();
		}
		return true;
	}

	/**
	 * wrongTime: func that set the relevet message to label if time is not correct
	 * and disable Next button.
	 * 
	 * @param str get the relevant message to case that the time is not correct.
	 */
	private void wrongTime(String str) {
		lblMsgTime.setText(str);
		btnNext.setDisable(true);
		radBtnTA.setSelected(false);
		radBtnShip.setSelected(false);
	}

	/**
	 * correctTime: func that set null to label if time is correct and replact Next
	 * button to enable.
	 */
	private void correctTime() {
		lblMsgTime.setText(null);
		btnNext.setDisable(false);
		radBtnTA.setSelected(false);
		radBtnShip.setSelected(false);
	}

	/**
	 * selectDate: func that the user select date and check if the date is correct.
	 * 
	 * @param event If user select date, check if Wrong Date or Correct Date.
	 */
	@FXML
	void selectDate(ActionEvent event) {
		LocalDate current = LocalDate.now();
		LocalDate date = datePickerDate.getValue();
		if (date.getDayOfMonth() - current.getDayOfMonth() < 0 && date.getMonthValue() == current.getMonthValue()) {
			wrongDate("Enter valid day!");
			return;
		}
		if (date.getMonthValue() - current.getMonthValue() < 0 && date.getYear() == current.getYear()) {
			wrongDate("Enter valid month!");
			return;
		}
		if (date.getYear() - current.getYear() < 0) {
			wrongDate("Enter valid year!");
			return;
		}
		correctDate();
	}

	/**
	 * correctDate: func that set null to label if date is correct and replace Next
	 * button to enable.
	 */
	private void correctDate() {
		lblMsgDate.setText(null);
		txtHours.setDisable(false);
		txtMinutes.setDisable(false);
		labelRes.setDisable(false);
		radBtnTA.setDisable(false);
		radBtnShip.setDisable(false);
	}

	/**
	 * wrongDate: func that set the relevet message to label if date is not correct
	 * and disable Next button.
	 * 
	 * @param str get the relevant message to case that the date is not correct.
	 */
	private void wrongDate(String str) {
		lblMsgTime.setText(null);
		lblMsgDate.setText(str);
		txtHours.setDisable(true);
		txtHours.clear();
		txtMinutes.setDisable(true);
		txtMinutes.clear();
		labelRes.setDisable(true);
		radBtnTA.setDisable(true);
		radBtnTA.setSelected(false);
		radBtnShip.setDisable(true);
		radBtnShip.setSelected(false);
	}

	/**
	 * selectedHour: enterd hour by user.
	 * 
	 * @param event If use entered hour.
	 */
	@FXML
	void selectHour(ActionEvent event) {

	}

	/**
	 * selectMinute: enterd minutes by user.
	 * 
	 * @param event If user entered Minutes.
	 */
	@FXML
	void selectMinute(ActionEvent event) {
	}

	/**
	 * selectShipment: user select a shipment.
	 * 
	 * @param event If user press on shipment checkBox, enable Next button.
	 */
	@FXML
	void selectShipment(ActionEvent event) {
		ta = null;
		shipment = "Shipment";
		btnNext.setDisable(false);
		isShipment = true;
	}

	/**
	 * selectTakeAway: user select a takeAway.
	 * 
	 * @param event If user press on takeAway checkBox ,enable Next button.
	 */
	@FXML
	void selectTakeAway(ActionEvent event) {
		shipment = null;
		ta = "Take Away";
		btnNext.setDisable(false);
		isShipment = false;
	}

	/**
	 * setValues: func that set all values in Order Time page that user entered to
	 * arraylist.
	 * 
	 * @param otValues ArrayList of order Time values for Invoice.
	 */
	private void setValues(ArrayList<String> otValues) {
		LocalDate date = datePickerDate.getValue();
		String changeFormatDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		otValues.add(changeFormatDate);
		otValues.add(txtHours.getText());
		otValues.add(txtMinutes.getText());
		if (ta != null)
			otValues.add(ta);
		else
			otValues.add(shipment);
		isPreOrder();
	}

	/**
	 * isPreOrder: func that calc if the order is preorder by the values that the
	 * user entered.
	 * 
	 * @return if preOrder(true) else false.
	 */
	private boolean isPreOrder() {
		if (LocalDate.now().getDayOfMonth() != datePickerDate.getValue().getDayOfMonth()) {
			orderTimeValues.add("true");
			return true;
		}
		int enteredHour, enteredMinute;
		int currentHour, currentMinute;
		int calc;
		enteredHour = Integer.parseInt(orderTimeValues.get(1));
		enteredMinute = Integer.parseInt(orderTimeValues.get(2));
		currentHour = LocalTime.now().getHour();
		currentMinute = LocalTime.now().getMinute();
		calc = (enteredHour * 60 + enteredMinute) - (currentHour * 60 + currentMinute);
		if (calc > 119) {
			orderTimeValues.add("true");
			return true;
		}
		orderTimeValues.add("false");
		return false;
	}
}
