package controllers;

import java.io.IOException;
import java.util.ArrayList;

import client.ChatClient;
import client.ClientUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * class for order payment page
 * 
 * @author Raz Avraham
 * @version 1.0 Build 100 December 27, 2021
 */
public class PaymentController {

	/**
	 * Observable list for payment combo box.
	 */
	private ObservableList<String> payment;
	/**
	 * selected payment String (business/private)
	 */
	public static String selectedPayment;

	/**
	 * first data load of page. set combo box.
	 * 
	 */
	public void loadData() {
		btnNext.setDisable(true);
		ArrayList<String> p = new ArrayList<String>();
		p.add("Bussines payment");
		p.add("Private payment");
		payment = FXCollections.observableArrayList(p);
		ChatClient.cmbRestaurant.clear();
		cmbPayment.setItems(payment);
	}

	@FXML
	private Button btnBack;

	@FXML
	private Button btnNext;

	@FXML
	private ComboBox<String> cmbPayment;

	/**
	 * Click On back button, go back to the previous page.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void Back(ActionEvent event) throws IOException {
		if (OrderTimeController.isShipment) {
			OrderShipmentController.shipmentValues.clear();
			((Node) event.getSource()).getScene().getWindow().hide(); // hide window
			FXMLLoader loader = new FXMLLoader();
			Stage primaryStage = new Stage();
			Pane root = loader.load(getClass().getResource("/controllers/OrderShipmentPage.fxml").openStream());
			OrderShipmentController orderShipmentController = loader.getController();
			orderShipmentController.loadData();
			Scene scene = new Scene(root);
			primaryStage.setOnCloseRequest(event1 -> event1.consume());
			primaryStage.setTitle("Order Shipment");
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.show();
		} else {
			OrderTimeController.orderTimeValues.clear();
			((Node) event.getSource()).getScene().getWindow().hide(); // hide window
			FXMLLoader loader = new FXMLLoader();
			Stage primaryStage = new Stage();
			Pane root = loader.load(getClass().getResource("/controllers/OrderTimePage.fxml").openStream());
			OrderTimeController orderTimePage = loader.getController();
			orderTimePage.loadData(CreateAnOrderController.orderValues.get(1));
			Scene scene = new Scene(root);
			primaryStage.setResizable(false);
			primaryStage.setOnCloseRequest(event1 -> event1.consume());
			primaryStage.setTitle("Order Time");
			primaryStage.setScene(scene);
			primaryStage.show();
		}
	}

	/**
	 * go to next page button, Order invoice page
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void Next(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide(); // hide window
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/controllers/OrderInvoicePage.fxml").openStream());
		OrderInvoiceController orderInvoiceController = loader.getController();
		orderInvoiceController.loadData();
		Scene scene = new Scene(root);
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		primaryStage.setTitle("Order Invoice");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * select payment method(business/private)
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void selectPayment(ActionEvent event) throws IOException {
		selectedPayment = cmbPayment.getValue().toString();
		String[] userDetails = ChatClient.userInfo.split("\\t");
		String idNumber = userDetails[1];
		if (selectedPayment.equals("Bussines payment")) {
			ClientUI.chat.accept("getMonthlyBudget:" + "\t" + idNumber);
		}
		btnNext.setDisable(false);
	}

}
