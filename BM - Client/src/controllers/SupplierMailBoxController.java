package controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;

import client.ChatClient;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.DishInOrder;
import logic.MsgToCustomer;
import logic.MsgToSupplier;

/**
 * @author Amit Kempner
 * @version 1.0 Build 100 December 27, 2021
 */
public class SupplierMailBoxController implements Initializable {

	/** MsgidNumber: using to save the cureent message that CEO clicked. */
	private static int MsgidNumber;
	/** userIdNumber: using to save the current userId. */
	private static int userIdNumber;
	/** index: using to save the index of cureent message that CEO clicked. */
	private static int index;
	/** isSelectd: using to save the status of click on message. */
	private static boolean isSelectd = false;
	/** mtc: a new instance of MsgToCustomer Class. */
	private MsgToCustomer mtc = new MsgToCustomer(0, 0, 0, null, null, null, null, false);
	/** statusWaiting: String of messgae status. */
	String statusWaiting = "Waiting";
	/** statusApproved: String of messgae status. */
	String statusApproved = "Approved";
	/** statusReady: String of messgae status. */
	String statusReady = "Ready";
	/** statusFinish: String of messgae status. */
	String statusFinish = "Finish";
	/**
	 * map to match between the supplier ID and its monthly orders
	 */
	public static Map<Integer, ArrayList<DishInOrder>> supplierMonthDishes = new HashMap<Integer, ArrayList<DishInOrder>>();

	@FXML
	private Button btnClear;

	@FXML
	private Button btnAccept;

	@FXML
	private Button btnBack;

	@FXML
	private Button btnReady;

	@FXML
	private TextArea txtDish;

	@FXML
	private Text txtEstimatedTime;

	@FXML
	private TextField txtHour;

	@FXML
	private Text txtMiddle;

	@FXML
	private TextField txtMin;

	@FXML
	private TableColumn<MsgToSupplier, String> colDate;

	@FXML
	private TableColumn<MsgToSupplier, Integer> colFromUserID;

	@FXML
	private TableColumn<MsgToSupplier, String> colHour;

	@FXML
	private TableColumn<MsgToSupplier, String> colMessage;

	@FXML
	private TableColumn<MsgToSupplier, Integer> colOrder;

	@FXML
	private TableColumn<MsgToSupplier, String> colStatus;

	@FXML
	private TableView<MsgToSupplier> tblMailBox;

	@FXML
	private Text txtMailBox;

	@FXML
	private Label lblMsg;

	/**
	 * loadData: load all information that needed at Initial opening of page.
	 * 
	 * @param userId get the userId of the current user.
	 * @throws IOException
	 */
	public void loadData(int userId) throws IOException {
		ClientUI.chat.accept("getMsgToSupplier:" + "\t" + String.valueOf(userId));
		userIdNumber = userId;
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
		ClientUI.chat.accept("getNumberSupplierMsgNotRead:" + "\t" + String.valueOf(userIdNumber));
		ChatClient.MsgSupplierList.clear();
		((Node) event.getSource()).getScene().getWindow().hide(); // hide window
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/controllers/SupplierPage.fxml").openStream());
		SupplierController supplierController = loader.getController();
		primaryStage.setTitle("Supplier");
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		supplierController.loadData(String.valueOf(userIdNumber));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * checkSelection: check if Supplier press on appropriate button, if not send a
	 * message into label accordingly.
	 * 
	 * @return true if the check pass sucessfuly, false if not.
	 */
	private boolean checkSelection() {
		lblMsg.setText("");
		if (ChatClient.getMsgSupplierList().isEmpty()) {
			lblMsg.setText("MailBox empty!");
			return false;
		}
		if (!isSelectd) {
			lblMsg.setText("choose an orderID!");
			return false;
		}
		if (index <= -1)
			return false;
		return true;
	}

	/**
	 * Accept: When Supplier alert that order is approved by him to customer.
	 * 
	 * @param event If Supplier press on Accept button the customer get a message
	 *              that his\her order is confirmed by supplier.
	 * @throws IOException
	 */
	@FXML
	void Accept(ActionEvent event) throws IOException {
		lblMsg.setText("");
		if (!checkSelection())
			return;
		if (colStatus.getCellData(index).equals("-")) {
			lblMsg.setText("You cant accept this message!");
			return;
		}
		if (colStatus.getCellData(index).equals(statusApproved) || colStatus.getCellData(index).equals(statusReady)
				|| colStatus.getCellData(index).equals(statusFinish)) {
			lblMsg.setText("Order status already approved!");
			return;
		}
		setApprovedMsgToCustomerData();
		String msgToCustomer = "msgToCustomer:" + "\t" + mtc.getOrderId() + "\t"
				+ String.valueOf(mtc.getFromSupplierId()) + "\t" + String.valueOf(mtc.getToUserId()) + "\t"
				+ mtc.getDate() + "\t" + mtc.getHour() + "\t" + mtc.getMessage() + "\t" + mtc.getStatus() + "\t"
				+ String.valueOf(mtc.isRead());
		ClientUI.chat.accept(msgToCustomer);
		ClientUI.chat.accept("ConfirmOrder:" + "\t" + mtc.getOrderId());
		mtc.setMessage("order number " + mtc.getOrderId() + " wait to accaptence!");
		String orderTimeApprove = "InsertSupplierTimeApprove:" + "\t" + mtc.getOrderId() + "\t" + mtc.getHour();

		ClientUI.chat.accept(orderTimeApprove);

		ClientUI.chat
				.accept("updateMsgSupplier:" + "\t" + MsgidNumber + "\t" + statusApproved + "\t" + mtc.getMessage());
		lblMsg.setText("order approved, a message has been sent to customer!");
		ClientUI.chat.accept("getMsgToSupplier:" + "\t" + String.valueOf(userIdNumber));
		isSelectd = false;
	}

	/**
	 * setApprovedMsgToCustomerData: Set the message that the Supplier will receive
	 * when order is approved.
	 * 
	 */
	public void setApprovedMsgToCustomerData() {
		mtc.setOrderId(colOrder.getCellData(index));
		mtc.setFromSupplierId(userIdNumber);
		mtc.setToUserId(colFromUserID.getCellData(index));
		String changeFormatDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		mtc.setDate(changeFormatDate.toString());
		mtc.setHour(LocalTime.now().getHour() + ":" + LocalTime.now().getMinute());
		mtc.setMessage("order number " + mtc.getOrderId() + " approved by supplier!");
		mtc.setStatus("-");
		mtc.setRead(false);
	}

	/**
	 * Ready: When Supplier alert that order is ready to customer.
	 * 
	 * @param event If Supplier press on Ready button the customer get a message
	 *              that his\her order is ready.
	 * @throws IOException
	 */
	@FXML
	void Ready(ActionEvent event) throws IOException {
		lblMsg.setText("");
		if (!checkSelection())
			return;
		if (colStatus.getCellData(index).equals("-")) {
			lblMsg.setText("You cant accept this message!");
			return;
		}
		if (colStatus.getCellData(index).equals(statusWaiting)) {
			lblMsg.setText("Order stil not approved!");
			return;
		}
		if (colStatus.getCellData(index).equals(statusReady) || colStatus.getCellData(index).equals(statusFinish)) {
			lblMsg.setText("Order status already ready!");
			return;
		}
		setReadyMsgToCustomerData();
		ClientUI.chat.accept("getTypeOfOrder:" + "\t" + MsgidNumber);
		if (ChatClient.o1.getTypeOfOrder().equals("Shipment")) {
			if (txtHour.getText().equals("") || txtMin.getText().equals("")) {
				lblMsg.setText("Enter estimate arrival time of shipment!");
				return;
			}
			mtc.setMessage("order number " + mtc.getOrderId() + " is ready! estimated arrival time: "
					+ txtHour.getText() + ":" + txtMin.getText());
		}
		String msgToCustomer = "msgToCustomer:" + "\t" + mtc.getOrderId() + "\t"
				+ String.valueOf(mtc.getFromSupplierId()) + "\t" + String.valueOf(mtc.getToUserId()) + "\t"
				+ mtc.getDate() + "\t" + mtc.getHour() + "\t" + mtc.getMessage() + "\t" + mtc.getStatus() + "\t"
				+ String.valueOf(mtc.isRead());
		ClientUI.chat.accept(msgToCustomer);

		mtc.setMessage("order number " + mtc.getOrderId() + " wait to accaptence!");
		ClientUI.chat.accept("updateMsgSupplier:" + "\t" + MsgidNumber + "\t" + statusReady + "\t" + mtc.getMessage());
		lblMsg.setText("order is ready, a message has been sent to customer!");
		ClientUI.chat.accept("getMsgToSupplier:" + "\t" + String.valueOf(userIdNumber));
		isSelectd = false;
	}

	/**
	 * setReadyMsgToCustomerData: Set the message that the Supplier will receive
	 * when order is ready.
	 * 
	 */
	public void setReadyMsgToCustomerData() {
		mtc.setOrderId(colOrder.getCellData(index));
		mtc.setFromSupplierId(userIdNumber);
		mtc.setToUserId(colFromUserID.getCellData(index));
		String changeFormatDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		mtc.setDate(changeFormatDate.toString());
		mtc.setHour(LocalTime.now().getHour() + ":" + LocalTime.now().getMinute());
		mtc.setMessage("order number " + mtc.getOrderId() + " is ready!");
		mtc.setStatus(statusWaiting);
		mtc.setRead(false);
	}

	/**
	 * selectClear: remove all messages from Branch Manager mailbox by click on
	 * button.
	 * 
	 * @param event if the user click on clear all messages button.
	 * @throws IOException
	 */
	@FXML
	void selectClear(ActionEvent event) throws IOException {
		lblMsg.setText("");
		txtDish.setText("");
		if (ChatClient.getMsgSupplierList().isEmpty()) {
			lblMsg.setText("MailBox empty!");
			return;
		}
		if (index <= -1)
			return;
		ChatClient.getMsgSupplierList().clear();
		ClientUI.chat.accept("removeMsgSupplier:");
	}

	/**
	 * getSelectd: get the number of messages that Supplier already read by click on
	 * row of message.
	 * 
	 * @param event if the user press on a row in table.
	 * @throws IOException
	 */
	@FXML
	void getSelected(MouseEvent event) throws IOException {
		txtHour.setText("");
		txtMin.setText("");
		String dio = "";
		lblMsg.setText("");
		txtHour.setVisible(false);
		txtMin.setVisible(false);
		txtMiddle.setVisible(false);
		txtEstimatedTime.setVisible(false);
		txtDish.setEditable(false);
		txtDish.setText("");
		ChatClient.dishInOrder = "";
		isSelectd = true;
		index = tblMailBox.getSelectionModel().getSelectedIndex();
		if (index <= -1)
			return;
		MsgidNumber = colOrder.getCellData(index);
		if (MsgidNumber == 0) {
			float finalPriceBill = calcBill();
			ArrayList<DishInOrder> dioList = new ArrayList<DishInOrder>();
			dioList = supplierMonthDishes.get(userIdNumber);
			if (dioList == null)
				txtDish.setText("Total profit: 0 NIS");
			else {
				for (DishInOrder i : dioList) {
					dio += i;
					dio += "\n";
				}
				dio += "Total profit: " + finalPriceBill + " NIS";
				txtDish.setText(dio);
			}
			ClientUI.chat.accept("msgSupplierReadBill:" + "\t" + MsgidNumber + "\t" + ChatClient.u1.getIdNumber());
			ChatClient.forBillToSupplier.clear();
			ChatClient.dio.clear();
			return;
		}
		ClientUI.chat.accept("getTypeOfOrder:" + "\t" + MsgidNumber);
		ClientUI.chat.accept("msgSupplierRead:" + "\t" + MsgidNumber);
		ClientUI.chat.accept("countInDish:" + "\t" + MsgidNumber);
		ClientUI.chat.accept("presentDishes:" + "\t" + MsgidNumber);
		if (ChatClient.o1.getTypeOfOrder().equals("Shipment") && colStatus.getCellData(index).equals("Approved")) {
			txtHour.setVisible(true);
			txtMin.setVisible(true);
			txtMiddle.setVisible(true);
			txtEstimatedTime.setVisible(true);
		}
		String newDishInOrder = ChatClient.dishInOrder.replaceAll("null", "");
		txtDish.setText(newDishInOrder);
	}

	/**
	 * calculate the the relevant supplier profit and insert its month orders
	 * details into list.
	 * 
	 * @return the final month profit of the supplier.
	 * @throws IOException
	 */
	public float calcBill() throws IOException {
		int month = LocalDate.now().getMonthValue();
		int index = 0;
		int supplierId = 0;
		float finalPrice = 0;
		float finalPricePerSupplier = 0;
		String strMonth;
		String branch;
		String restaurant;

		ArrayList<ArrayList<DishInOrder>> ArrayListOfdioArray = new ArrayList<ArrayList<DishInOrder>>();
		if (month == 1)
			month = 13;
		if (month < 10)
			strMonth = "0" + month;
		else
			strMonth = "" + month;
		ClientUI.chat.accept("getOrderDetailsByMonth:" + "\t" + strMonth);
		ArrayList<String> monthOrderForBill = ChatClient.forBillToSupplier;
		HashSet<String> monthOrderForBillSet = new HashSet<String>();
		for (String s : monthOrderForBill) {
			s = s.replaceAll(" ", "\t");
			ClientUI.chat.accept("getFromDishInOrderMonthDishes:" + "\t" + s);
		}
		for (int i = 0; i < monthOrderForBill.size(); i++) {
			monthOrderForBill.set(i, monthOrderForBill.get(i).substring(monthOrderForBill.get(i).indexOf(" ") + 1));
		}
		monthOrderForBillSet.addAll(monthOrderForBill);
		ArrayList<DishInOrder> dioList = new ArrayList<>();
		dioList = ChatClient.dio;
		for (String s1 : monthOrderForBillSet) {
			ArrayList<DishInOrder> sendDishesToSupplierList = new ArrayList<DishInOrder>();
			finalPrice = 0;
			branch = s1.split(" ")[0];
			restaurant = s1.split(" ")[1];
			ClientUI.chat.accept("getRestaurantId:" + "\t" + branch + "\t" + restaurant);
			ClientUI.chat.accept("getSupplierId:" + "\t" + ChatClient.resId);
			supplierId = ChatClient.supplierId;
			for (DishInOrder d : dioList) {
				if (d.getBranch().equals(branch) && d.getRestaurant().equals(restaurant)
						&& !sendDishesToSupplierList.contains(d)) {
					finalPrice += d.getPrice() * (1 - d.getComission());
					sendDishesToSupplierList.add(d);
				}
			}
			if (supplierId == userIdNumber)
				finalPricePerSupplier = finalPrice;
			ArrayListOfdioArray.add(sendDishesToSupplierList);
			SupplierMailBoxController.supplierMonthDishes.put(supplierId, ArrayListOfdioArray.get(index++));
		}
		return finalPricePerSupplier;
	}

	/**
	 * Start: open the Mail Box of supplier.
	 * 
	 * @param primaryStage
	 *
	 * @throws Exception
	 */
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/controllers/SupplierMailBoxPage.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		loadData(ChatClient.u1.getIdNumber());
		primaryStage.setTitle("Mail Box Supplier");
		primaryStage.show();

	}

	/**
	 * initialize: set all tabels column at Initial opening of page and get all
	 * messages from DB to screen.
	 *
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		txtHour.setVisible(false);
		txtMin.setVisible(false);
		txtMiddle.setVisible(false);
		txtEstimatedTime.setVisible(false);
		colOrder.setCellValueFactory(new PropertyValueFactory<MsgToSupplier, Integer>("OrderId"));
		colFromUserID.setCellValueFactory(new PropertyValueFactory<MsgToSupplier, Integer>("fromUserId"));
		colDate.setCellValueFactory(new PropertyValueFactory<MsgToSupplier, String>("Date"));
		colHour.setCellValueFactory(new PropertyValueFactory<MsgToSupplier, String>("Hour"));
		colMessage.setCellValueFactory(new PropertyValueFactory<MsgToSupplier, String>("Message"));
		colStatus.setCellValueFactory(new PropertyValueFactory<MsgToSupplier, String>("Status"));
		tblMailBox.setItems(ChatClient.getMsgSupplierList());
	}
}
