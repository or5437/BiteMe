package controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.DishInOrder;
import logic.MsgToSupplier;
import logic.Order;
import logic.Shipment;

/**
 * OrderInvoiceController implements the Invoice of orders.
 * 
 * @author Amit Kempner
 * @version 1.0 Build 100 December 27, 2021
 */
public class OrderInvoiceController {

	/** o: instance of Order class. */
	private Order o;
	/** s: instance of Shipment class. */
	private Shipment s;
	/** mts: instance of MsgToSupplier class. */
	private MsgToSupplier mts;
	/** isRefund: true if there is a refund to customer, else false. */
	private boolean isRefund = false;
	/** dish: ArrayList of Dish in order, all dishes that customer selected. */
	private ArrayList<DishInOrder> Dish = new ArrayList<DishInOrder>();
	/** specificDish: ObservableList of all dishes. */
	private ObservableList<String> specificDish;

	@FXML
	private ComboBox<String> cmbSpecificDish;

	@FXML
	private Label lblShipmentMet;

	@FXML
	private Label lblRobot;

	@FXML
	private Text txtRobot;

	@FXML
	private Text txtShipmentMet;

	@FXML
	private Label lblAddress;

	@FXML
	private Label lblRecName;

	@FXML
	private Label lblRecPhone;

	@FXML
	private Button btnBack;

	@FXML
	private Button btnConfirm;

	@FXML
	private Label lblBranch;

	@FXML
	private Label lblAdd;

	@FXML
	private Label lblDate;

	@FXML
	private Label lblDish;

	@FXML
	private Label lblDoneness;

	@FXML
	private Label lblPayMet;

	@FXML
	private Label lblPrice;

	@FXML
	private Label lblHour;

	@FXML
	private Label lblSize;

	@FXML
	private Label lblItem;

	@FXML
	private Label lblResName;

	@FXML
	private Label lblTypeOfOrder;

	@FXML
	private Text txtAdd;

	@FXML
	private AnchorPane txtDate;

	@FXML
	private Text txtDish;

	@FXML
	private Text txtDoneness;

	@FXML
	private Text txtHour;

	@FXML
	private Text txtItem;

	@FXML
	private Text txtSize;

	@FXML
	private Text txtBranch;

	@FXML
	private Text txtTypeOrder;

	@FXML
	private Text txtTPrice;

	@FXML
	private Label lblTPrice;

	@FXML
	private Text txtNumberPart;

	@FXML
	private Label lblNumPar;

	@FXML
	private CheckBox chboxRefund;

	@FXML
	private Label lblRefundPrice;

	/**
	 * loadSpecificDishList: load all disehs that customer selected at Initial
	 * opening of page.
	 */
	public void loadSpecificDishList() {
		ArrayList<String> s = new ArrayList<String>();
		for (int i = 1; i < CreateAnOrderController.mapDish.size() + 1; i++)
			s.add("Dish" + i + ": " + CreateAnOrderController.mapDish.get(i).get(3));
		specificDish = FXCollections.observableArrayList(s);
		cmbSpecificDish.setItems(specificDish);
	}

	/**
	 * loadData: load all information that needed at Initial opening of page.
	 * @throws IOException
	 */
	public void loadData() throws IOException {
		o = new Order(null, 0, null, null, null, null, null, null, false, null, null, 0, null, false);
		s = new Shipment(0, null, 0, null, null, null, false);
		mts = new MsgToSupplier(0, 0, 0, null, null, null, null, false);
		loadSpecificDishList();
		chboxRefund.setVisible(false);
		lblRefundPrice.setVisible(false);
		setData(o, s);
		ClientUI.chat.accept(
				"getPriceRefundIfExist:" + "\t" + o.getUserID() + "\t" + o.getBranch() + "\t" + o.getRestaurant());
		if (ChatClient.ref1.getPriceRefund() != 0) {
			chboxRefund.setVisible(true);
			lblRefundPrice.setVisible(true);
			lblRefundPrice.setText("Your refund is: " + ChatClient.ref1.getPriceRefund() + " NIS");
		}
		if (o.isPreOrder())
			lblTPrice.setText(o.getFinalPrice() + " NIS - PreOrder (10% discount)");
		else
			lblTPrice.setText(o.getFinalPrice() + " NIS");
	}

	/**
	 * setData: set Data into order Object and Shipment Order, all details of order.
	 * 
	 * @param o get instacne of Order Class.
	 * @param s get instance of Shipment Class.
	 * @throws IOException
	 */
	private void setData(Order o, Shipment s) throws IOException {
		float finalPrice;
		float shipmentPrice = 0;
		String[] userDetails = ChatClient.userInfo.split("\\t");
		o.setUserType(userDetails[0]);
		o.setUserID(Integer.parseInt(userDetails[1]));
		o.setPhoneNumber(userDetails[2]);

		o.setBranch(CreateAnOrderController.orderValues.get(0));
		o.setRestaurant(CreateAnOrderController.orderValues.get(1));
		o.setOrderDueDate(OrderTimeController.orderTimeValues.get(0));
		o.setOrderDueHour(
				OrderTimeController.orderTimeValues.get(1) + ":" + OrderTimeController.orderTimeValues.get(2));
		o.setPreOrder(Boolean.parseBoolean(OrderTimeController.orderTimeValues.get(4)));
		o.setTypeOfOrder(OrderTimeController.orderTimeValues.get(3));
		if (o.getTypeOfOrder().equals("Take Away")) {
			o.setOrderAddress("");
		} else {
			o.setOrderAddress(OrderShipmentController.shipmentValues.get(0));
			s.setReciverAddress(OrderShipmentController.shipmentValues.get(0));
			s.setReciverName(OrderShipmentController.shipmentValues.get(1));
			s.setReciverPhone(OrderShipmentController.shipmentValues.get(2));
			s.setShipmentType(OrderShipmentController.shipmentValues.get(3));
			s.setIsRobot(Boolean.parseBoolean(OrderShipmentController.shipmentValues.get(4)));
			shipmentPrice = calcShipmentPrice(s.getShipmentType());
			s.setShipmentPrice(shipmentPrice);
		}
		finalPrice = (float) calcDiscount(shipmentPrice);
		o.setConfirm(false);
		o.setFinalPrice(finalPrice);
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
		if (o.getUserType().equals("Business_Customer")) {
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
		if (o.getUserType().equals("Private_Customer")) {
			if (o.getTypeOfOrder().equals("Take Away")) {
				OrderTimeController.orderTimeValues.clear();
				((Node) event.getSource()).getScene().getWindow().hide(); // hide window
				FXMLLoader loader = new FXMLLoader();
				Stage primaryStage = new Stage();
				Pane root = loader.load(getClass().getResource("/controllers/OrderTimePage.fxml").openStream());
				OrderTimeController orderTimeController = loader.getController();
				orderTimeController.loadData(CreateAnOrderController.orderValues.get(1));
				Scene scene = new Scene(root);
				primaryStage.setTitle("Order Time");
				primaryStage.setOnCloseRequest(event1 -> event1.consume());
				primaryStage.setResizable(false);
				primaryStage.setScene(scene);
				primaryStage.show();
			} else {
				OrderShipmentController.shipmentValues.clear();
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
		}
	}

	/**
	 * selectSpecificDish: present the selected dish at invoice.
	 * 
	 * @param event If user select specific dish to view at the invoice page, the
	 *              details will appear.
	 * @throws IOException
	 */
	@FXML
	void selectSpecificDish(ActionEvent event) throws IOException {
		int index = cmbSpecificDish.getSelectionModel().getSelectedIndex();
		index++;
		lblBranch.setText(CreateAnOrderController.mapDish.get(index).get(0));
		lblResName.setText(CreateAnOrderController.mapDish.get(index).get(1));
		lblItem.setText(CreateAnOrderController.mapDish.get(index).get(2));
		lblDish.setText(CreateAnOrderController.mapDish.get(index).get(3));
		lblPrice.setText(CreateAnOrderController.mapDish.get(index).get(4));
		lblDoneness.setText(CreateAnOrderController.mapDish.get(index).get(5));
		lblSize.setText(CreateAnOrderController.mapDish.get(index).get(6));
		lblAdd.setText(CreateAnOrderController.mapDish.get(index).get(7));
		lblDate.setText(OrderTimeController.orderTimeValues.get(0));
		if (OrderTimeController.orderTimeValues.get(1) != null)
			lblHour.setText(
					OrderTimeController.orderTimeValues.get(1) + ":" + OrderTimeController.orderTimeValues.get(2));
		lblTypeOfOrder.setText(OrderTimeController.orderTimeValues.get(3));
		if (o.getTypeOfOrder().equals("Shipment")) {
			lblAddress.setText(OrderShipmentController.shipmentValues.get(0));
			lblRecName.setText(OrderShipmentController.shipmentValues.get(1));
			lblRecPhone.setText(OrderShipmentController.shipmentValues.get(2));
			lblShipmentMet.setText(OrderShipmentController.shipmentValues.get(3));
			if (s.getShipmentType().equals("Shared"))
				lblNumPar.setText(String.valueOf(OrderShipmentController.numberOfParticipants));
			if (s.isIsRobot())
				lblRobot.setText("Yes");
			else
				lblRobot.setText("No");
		}
		if (o.getUserType().equals("Business_Customer")) {
			lblPayMet.setText(PaymentController.selectedPayment);
		} else
			lblPayMet.setText("Private Payment");
	}

	/**
	 * calcDiscount: func that calc discount if preOrder.
	 * 
	 * @param shipmentPrice get the shipmentPrice.
	 * @return the final price of oredr after discount or not.
	 */
	public double calcDiscount(float shipmentPrice) {
		String Price;
		int index;
		float finalPrice = 0;
		for (int i = 1; i < CreateAnOrderController.mapDish.size() + 1; i++) {
			Price = CreateAnOrderController.mapDish.get(i).get(4);
			index = Price.indexOf('N');
			Price = Price.substring(0, index - 1);
			finalPrice += Float.parseFloat(Price);
		}
		finalPrice += shipmentPrice;
		if (o.isPreOrder()) {
			finalPrice = (float) (finalPrice * 0.9);
			return finalPrice;
		}
		return finalPrice;
	}

	/**
	 * calcPayment: Calc if monthly budget exist.
	 * 
	 * @param event If the monthly budget is done for business customer, appear new
	 *              message page.
	 * @return return true if monthly budget is not done, return false if done.
	 * @throws IOException
	 */
	public boolean calcPayment(ActionEvent event) throws IOException {
		float newMonthlyBudget;
		if (ChatClient.monthlyBudget >= o.getFinalPrice()) {
			newMonthlyBudget = ChatClient.monthlyBudget - o.getFinalPrice();
			ClientUI.chat.accept("updateMonthlyBudget:" + "\t" + String.valueOf(o.getUserID()) + "\t"
					+ String.valueOf(newMonthlyBudget));
			return true;
		} else {
			((Node) event.getSource()).getScene().getWindow().hide(); // hide window
			FXMLLoader loader = new FXMLLoader();
			Stage primaryStage = new Stage();
			Pane root = loader.load(getClass().getResource("/controllers/MsgPage.fxml").openStream());
			MsgController msgController = loader.getController();
			msgController.loadData(
					"You do not have enough money on your mounthly budget, please choose another payment method!", 1);
			Scene scene = new Scene(root);
			primaryStage.setResizable(false);
			primaryStage.setOnCloseRequest(event1 -> event1.consume());
			primaryStage.setTitle("Message");
			primaryStage.setScene(scene);
			primaryStage.show();
			return false;
		}

	}

	/**
	 * calcShipmentPrice: funv that calc the price of shipment.
	 * 
	 * @param s get the number of participants for shared delivery.
	 * @return return the price of shipment by te rules(switch case).
	 */
	public float calcShipmentPrice(String s) {
		switch (s) {
		case "Basic":
			return 25;
		case "Shared":
			switch (OrderShipmentController.numberOfParticipants) {
			case 2:
				return 20 * 2;
			default:
				return 15 * OrderShipmentController.numberOfParticipants;
			}
		default:
			return 0;
		}
	}

	/**
	 * setDishValues(): set all dish values to send the deatils to DB.
	 */
	public void setDishValues() {
		String Price;
		for (int i = 1; i < CreateAnOrderController.mapDish.size() + 1; i++) {
			DishInOrder dish = new DishInOrder(null, null, null, null, 0, null, null, null, 0);
			dish.setBranch(CreateAnOrderController.mapDish.get(i).get(0));
			dish.setRestaurant(CreateAnOrderController.mapDish.get(i).get(1));
			dish.setItem(CreateAnOrderController.mapDish.get(i).get(2));
			dish.setDish(CreateAnOrderController.mapDish.get(i).get(3));
			Price = CreateAnOrderController.mapDish.get(i).get(4);
			Price = Price.substring(0, Price.indexOf(" "));
			dish.setPrice(Float.parseFloat(Price));
			dish.setDoneness(CreateAnOrderController.mapDish.get(i).get(5));
			dish.setSize(CreateAnOrderController.mapDish.get(i).get(6));
			dish.setOptionalComponents(CreateAnOrderController.mapDish.get(i).get(7));
			Dish.add(dish);
		}
	}

	/**
	 * setMsgToSupplierData: Set the message that the Supplier will get when the
	 * customer confirmed the order.
	 * @throws IOException
	 */
	public void setMsgToSupplierData() throws IOException {
		mts.setFromUserId(o.getUserID());
		ClientUI.chat.accept("getRestaurantId:" + "\t" + o.getBranch() + "\t" + o.getRestaurant());
		ClientUI.chat.accept("getSupplierId:" + "\t" + ChatClient.resId);
		mts.setToSupplierId(ChatClient.supplierId);
		mts.setDate(o.getOrderConfirmDate());
		mts.setHour(o.getOrderConfirmHour());
		ClientUI.chat.accept("getLastOrderNumber:");
		mts.setMessage("order number " + ChatClient.lastOrderNumber + " wait to accaptence!");
		mts.setStatus("Waiting");
		mts.setRead(false);
	}

	/**
	 * selectRefund: Customer select refund option if exist.
	 * 
	 * @param event If there is a refund for user, when select using the refund, the
	 *              total price change accordingly.
	 */
	@FXML
	void selectRefund(ActionEvent event) {
		float finalPrice;
		if (!isRefund) {
			isRefund = true;
			if (ChatClient.ref1.getPriceRefund() > o.getFinalPrice()) {
				lblTPrice.setText(0 + " NIS");
				return;
			}
			finalPrice = o.getFinalPrice() - ChatClient.ref1.getPriceRefund();
			if (o.isPreOrder())
				lblTPrice.setText(finalPrice + " NIS - PreOrder (10% discount)");
			else
				lblTPrice.setText(finalPrice + " NIS");
		} else {
			isRefund = false;
			if (o.isPreOrder())
				lblTPrice.setText(o.getFinalPrice() + " NIS - PreOrder (10% discount)");
			else
				lblTPrice.setText(o.getFinalPrice() + " NIS");
		}
	}

	/**
	 * ConfirmOrder: Customer press on confirm button.
	 * 
	 * @param event If user press on Confirm button an appropriate message will send
	 *              to supplier. the order details will enterd to DB after all
	 *              updates of order at DB, the customer will move to message
	 *              screen.
	 * @throws IOException
	 */
	@FXML
	void ConfirmOrder(ActionEvent event) throws IOException {
		float existRefund;
		if (o.getUserType().equals("Business_Customer") && PaymentController.selectedPayment.equals("Bussines payment"))
			if (!calcPayment(event))
				return;
		String changeFormatDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		o.setOrderConfirmDate(changeFormatDate.toString());
		o.setOrderConfirmHour(LocalTime.now().getHour() + ":" + LocalTime.now().getMinute());
		if (isRefund) {
			if (ChatClient.ref1.getPriceRefund() > o.getFinalPrice()) {
				existRefund = ChatClient.ref1.getPriceRefund() - o.getFinalPrice();
				o.setFinalPrice(0);
				ClientUI.chat.accept("UpdateRefund:" + "\t" + o.getUserID() + "\t" + o.getBranch() + "\t"
						+ o.getRestaurant() + "\t" + existRefund);
			} else {
				o.setFinalPrice(o.getFinalPrice() - ChatClient.ref1.getPriceRefund());
				ClientUI.chat.accept(
						"removeRefund:" + "\t" + o.getUserID() + "\t" + o.getBranch() + "\t" + o.getRestaurant());
			}
		}
		String newOrder = "updateOrder:" + "\t" + o.getUserType() + "\t" + String.valueOf(o.getUserID()) + "\t"
				+ o.getPhoneNumber() + "\t" + o.getOrderConfirmDate() + "\t" + o.getOrderConfirmHour() + "\t"
				+ o.getOrderDueDate() + "\t" + o.getOrderDueHour() + "\t" + o.getOrderAddress() + "\t"
				+ String.valueOf(o.isPreOrder()) + "\t" + o.getBranch() + "\t" + o.getRestaurant() + "\t"
				+ String.valueOf(o.getFinalPrice()) + "\t" + o.getTypeOfOrder() + "\t" + String.valueOf(o.isConfirm());
		ClientUI.chat.accept(newOrder);
		if (o.getTypeOfOrder().equals("Shipment")) {
			String newOrderShipment = "updateShipment:" + "\t" + s.getShipmentType() + "\t"
					+ String.valueOf(s.getShipmentPrice()) + "\t" + s.getReciverAddress() + "\t" + s.getReciverName()
					+ "\t" + s.getReciverPhone() + "\t" + String.valueOf(s.isIsRobot());
			ClientUI.chat.accept(newOrderShipment);
		}
		setDishValues();
		for (int i = 0; i < Dish.size(); i++) {
			String NewDish = "updateDishInOrder:" + "\t" + Dish.get(i).getBranch() + "\t" + Dish.get(i).getRestaurant()
					+ "\t" + Dish.get(i).getItem() + "\t" + Dish.get(i).getDish() + "\t"
					+ String.valueOf(Dish.get(i).getPrice()) + "\t" + Dish.get(i).getDoneness() + "\t"
					+ Dish.get(i).getSize() + "\t" + Dish.get(i).getOptionalComponents();
			ClientUI.chat.accept(NewDish);
		}
		setMsgToSupplierData();
		String msgToSupplier = "msgToSupplier:" + "\t" + String.valueOf(mts.getFromUserId()) + "\t"
				+ String.valueOf(mts.getToSupplierId()) + "\t" + mts.getDate() + "\t" + mts.getHour() + "\t"
				+ mts.getMessage() + "\t" + mts.getStatus() + "\t" + String.valueOf(mts.isRead());
		ClientUI.chat.accept(msgToSupplier);

		((Node) event.getSource()).getScene().getWindow().hide(); // hide window
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/controllers/MsgPage.fxml").openStream());
		MsgController msgController = loader.getController();
		msgController.loadData("Thank you for choosing BiteMe, The order was moved to the restaurant!", 2);
		Scene scene = new Scene(root);
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		primaryStage.setTitle("Message");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
