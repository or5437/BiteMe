package controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import client.ChatClient;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.MsgToCustomer;
import logic.MsgToSupplier;
import logic.OrderTimeApprove;

/**
 * CustomerMailBoxController implements the MailBox of all kind of customers,
 * Private:Business.
 * 
 * @author Amit Kempner
 * @version 1.0 Build 100 December 27, 2021
 */
public class CustomerMailBoxController implements Initializable {

	/** userIdNumber: using to save the current userId. */
	private static int userIdNumber;
	/** customerType: using to save the type of customer : private or business. */
	private static String customerType;
	/**
	 * MsgidNumber: using to save the cureent messgae that Customer clicked.
	 */
	private static int MsgidNumber;
	/**
	 * index: using to save the index of cureent message that Customer clicked.
	 */
	private static int index;
	/** isSelectd: using to save the status of click on message. */
	private static boolean isSelectd = false;
	/** mts: a new instance of MsgToSupplier Class. */
	private MsgToSupplier mts = new MsgToSupplier(0, 0, 0, null, null, null, null, false);
	/** otr: a new instance of OrderTimeApprove Class. */
	public static OrderTimeApprove otr = new OrderTimeApprove(0, null, null, null);
	/** statusWaiting: String of messgae status. */
	String statusWaiting = "Waiting";
	/** statusApproved: String of messgae status. */
	String statusApproved = "Approved";
	/** statusFinish: String of messgae status. */
	String statusFinsh = "Finish";

	@FXML
	private Button btnArrived;

	@FXML
	private Button btnBack;

	@FXML
	private Button btnClear;

	@FXML
	private TableColumn<MsgToCustomer, String> colDate;

	@FXML
	private TableColumn<MsgToCustomer, Integer> colFromSupplierID;

	@FXML
	private TableColumn<MsgToCustomer, String> colHour;

	@FXML
	private TableColumn<MsgToCustomer, String> colMessage;

	@FXML
	private TableColumn<MsgToCustomer, Integer> colOrder;

	@FXML
	private TableColumn<MsgToCustomer, String> colStatus;

	@FXML
	private Label lblMsg;

	@FXML
	private TableView<MsgToCustomer> tblMailBox;

	@FXML
	private Text txtMailBox;

	/**
	 * loadData: load all information that needed at Initial opening of page.
	 * 
	 * @param userId   get the userId of the current user.
	 * @param custType get the type of customer : private or business.
	 * @throws IOException
	 */
	public void loadData(int userId, String custType) throws IOException {
		ClientUI.chat.accept("getMsgToCustomer:" + "\t" + String.valueOf(userId));
		userIdNumber = userId;
		customerType = custType;
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
		ClientUI.chat.accept("getNumberCustomerMsgNotRead:" + "\t" + String.valueOf(userIdNumber));
		ChatClient.MsgCustomerList.clear();
		((Node) event.getSource()).getScene().getWindow().hide(); // hide window
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/controllers/CustomerPage.fxml").openStream());
		CustomerController customerController = loader.getController();
		customerController.loadData(customerType, String.valueOf(userIdNumber));
		primaryStage.setTitle(customerType.split("_")[0] + " " + customerType.split("_")[1]);
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * getSelectd: get the number of messages that Customer already read by click on
	 * row of message.
	 * 
	 * @param event if the user press on a row in table.
	 * @throws IOException
	 */
	@FXML
	void getSelected(MouseEvent event) throws IOException {
		isSelectd = true;
		index = tblMailBox.getSelectionModel().getSelectedIndex();
		if (index <= -1)
			return;
		MsgidNumber = colOrder.getCellData(index);
		ClientUI.chat.accept("msgCustomerRead:" + "\t" + MsgidNumber);
	}

	/**
	 * selectClear: remove all messages from Customer mailbox by click on button.
	 * 
	 * @param event if the user click on clear all messages button.
	 * @throws IOException
	 */
	@FXML
	void selectClear(ActionEvent event) throws IOException {
		lblMsg.setText("");
		if (ChatClient.getMsgCustomerList().isEmpty()) {
			lblMsg.setText("MailBox empty!");
			return;
		}
		if (index <= -1)
			return;
		ChatClient.getMsgCustomerList().clear();
		ClientUI.chat.accept("removeMsgCustomer:");
	}

	/**
	 * Arrived: When Customer approve that the order in his hand.
	 * 
	 * @param event If Customer press on arrived button the order status change to
	 *              finish and sent a message to supplier. In addition, the
	 *              reception time set.
	 * @throws IOException
	 */
	@FXML
	void Arrived(ActionEvent event) throws IOException {
		if (!checkSelection())
			return;
		if (colStatus.getCellData(index).equals(statusApproved)) {
			lblMsg.setText("Order status already approved!");
			return;
		}
		if (colStatus.getCellData(index).equals("-")) {
			lblMsg.setText("You cant accept this message!");
			return;
		}
		setApprovedMsgToSupplierData();
		String msgToSupplier = "updateSupplierMsgFromCustomer:" + "\t" + String.valueOf(mts.getOrderId()) + "\t"
				+ mts.getStatus() + "\t" + mts.getMessage() + "\t" + mts.isRead();
		ClientUI.chat.accept(msgToSupplier);

		otr.setOrderId(mts.getOrderId());
		otr.setCustomerTime(LocalTime.now().getHour() + ":" + LocalTime.now().getMinute());
		String changeFormatDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		otr.setCustomerDate(changeFormatDate);

		String orderTimeApprove = "InsertCustomerTimeApprove:" + "\t" + otr.getOrderId() + "\t" + otr.getCustomerTime()
				+ "\t" + otr.getCustomerDate();
		ClientUI.chat.accept(orderTimeApprove);
		calcIfRefund();

		ClientUI.chat.accept("updateMsgCustomer:" + "\t" + mts.getOrderId() + "\t" + statusApproved);
		lblMsg.setText("Thank you, a message has been sent to supplier!");
		ClientUI.chat.accept("getMsgToCustomer:" + "\t" + String.valueOf(userIdNumber));
		isSelectd = false;
	}

	/**
	 * setApprovedMsgToSupplierData: Set the message that Supplier will receive when
	 * Customer will approve that the order in his hand.
	 * 
	 */
	public void setApprovedMsgToSupplierData() {
		mts.setOrderId(colOrder.getCellData(index));
		mts.setFromUserId(userIdNumber);
		mts.setToSupplierId(colFromSupplierID.getCellData(index));
		String changeFormatDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		mts.setDate(changeFormatDate.toString());
		mts.setHour(LocalTime.now().getHour() + ":" + LocalTime.now().getMinute());
		mts.setMessage("order number " + mts.getOrderId() + " reached the customer successfully!");
		mts.setStatus("Finish");
		mts.setRead(false);
	}

	/**
	 * calcRefund: this func check if the customer need to get refund after he
	 * approved that order received. if pre order up to 20 min from the dueHour. if
	 * not pre order up to 60 min from the dueHour.
	 * 
	 * @throws IOException
	 */
	public void calcIfRefund() throws IOException {
		int calc;
		ClientUI.chat.accept("getIsPreOrder:" + "\t" + mts.getOrderId());
		ClientUI.chat.accept("InsertOrderInRefund:" + "\t" + mts.getOrderId() + "\t" + "false");
		if (!ChatClient.isPreOrder) {
			ClientUI.chat.accept("getSupplierApproveHour:" + "\t" + mts.getOrderId());
			ClientUI.chat.accept("getCustomerApproveHour:" + "\t" + mts.getOrderId());
			String SupHour = ChatClient.supplierApproveTime.substring(0, ChatClient.supplierApproveTime.indexOf(':'));
			String SupMinute = ChatClient.supplierApproveTime.substring(ChatClient.supplierApproveTime.indexOf(':') + 1,
					ChatClient.supplierApproveTime.length());
			String CusHour = ChatClient.customerApproveTime.substring(0, ChatClient.customerApproveTime.indexOf(':'));
			String CusMinute = ChatClient.customerApproveTime.substring(ChatClient.customerApproveTime.indexOf(':') + 1,
					ChatClient.supplierApproveTime.length());
			int SupIntHour = Integer.parseInt(SupHour);
			int SupIntMinute = Integer.parseInt(SupMinute);
			int CusIntHour = Integer.parseInt(CusHour);
			int CusIntMinute = Integer.parseInt(CusMinute);
			calc = (CusIntHour * 60 + CusIntMinute) - (SupIntHour * 60 + SupIntMinute);
			if (calc > 60) {
				getRefund();
			}

		} else {
			ClientUI.chat.accept("getDueHour:" + "\t" + mts.getOrderId());
			String OrderDueHour = ChatClient.dueHour.substring(0, ChatClient.dueHour.indexOf(':'));
			String OrderDueMin = ChatClient.dueHour.substring(ChatClient.dueHour.indexOf(':') + 1,
					ChatClient.dueHour.length());
			int DueHour = Integer.parseInt(OrderDueHour);
			int DueMin = Integer.parseInt(OrderDueMin);
			ClientUI.chat.accept("getCustomerApproveHour:" + "\t" + mts.getOrderId());
			String CusHour = ChatClient.customerApproveTime.substring(0, ChatClient.customerApproveTime.indexOf(':'));
			String CusMinute = ChatClient.customerApproveTime.substring(ChatClient.customerApproveTime.indexOf(':') + 1,
					ChatClient.customerApproveTime.length());
			int CusIntHour = Integer.parseInt(CusHour);
			int CusIntMinute = Integer.parseInt(CusMinute);
			ClientUI.chat.accept("getCustomerApproveDate:" + "\t" + mts.getOrderId());
			ClientUI.chat.accept("getDueDate:" + "\t" + mts.getOrderId());
			String CusDate = ChatClient.customerApproveDate;
			String[] CusDateDetails = CusDate.split("/");
			String DueDate = ChatClient.dueDate;
			String[] DueDateDetails = DueDate.split("/");
			if (CusDateDetails[0].equals(DueDateDetails[0]) && CusDateDetails[1].equals(DueDateDetails[1])
					&& CusDateDetails[2].equals(DueDateDetails[2])) {
				calc = (CusIntHour * 60 + CusIntMinute) - (DueHour * 60 + DueMin);
				if (calc > 20) {
					getRefund();
				}
			}
			if (Integer.parseInt(CusDateDetails[0]) > Integer.parseInt(DueDateDetails[0])
					&& CusDateDetails[1].equals(DueDateDetails[1]) && CusDateDetails[2].equals(DueDateDetails[2])) {
				getRefund();
			}
		}
	}

	/**
	 * getRefund: this func calc the refund after valid that customer need to get
	 * refund.
	 * 
	 * @throws IOException
	 */
	private void getRefund() throws IOException {
		float refund;
		ClientUI.chat.accept("getBranchAndRestaurantAndFinalPriceAndUserId:" + "\t" + mts.getOrderId());
		ClientUI.chat.accept("UpdateOrderInRefund:" + "\t" + mts.getOrderId() + "\t" + "true");
		String branch = ChatClient.o1.getBranch();
		String restaurat = ChatClient.o1.getRestaurant();
		float finalPrice = ChatClient.o1.getFinalPrice();
		int userIdNumber = ChatClient.o1.getUserID();
		ClientUI.chat.accept("getPriceRefundIfExist:" + "\t" + userIdNumber + "\t" + branch + "\t" + restaurat);
		float currentRefund = ChatClient.ref1.getPriceRefund();
		refund = (float) (finalPrice * 0.5);
		if (currentRefund != 0) {
			refund += currentRefund;
			ClientUI.chat
					.accept("UpdateRefund:" + "\t" + userIdNumber + "\t" + branch + "\t" + restaurat + "\t" + refund);
		} else
			ClientUI.chat
					.accept("SetNewRefund:" + "\t" + userIdNumber + "\t" + branch + "\t" + restaurat + "\t" + refund);
	}

	/**
	 * checkSelection: check if Customer press on appropriate button, if not send a
	 * message into label accordingly.
	 * 
	 * @return true if the check pass sucessfuly, false if not.
	 */
	private boolean checkSelection() {
		lblMsg.setText("");
		if (ChatClient.getMsgCustomerList().isEmpty()) {
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
	 * initialize: set all tabels column at Initial opening of page and get all
	 * messages from DB to screen.
	 *
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		colOrder.setCellValueFactory(new PropertyValueFactory<MsgToCustomer, Integer>("OrderId"));
		colFromSupplierID.setCellValueFactory(new PropertyValueFactory<MsgToCustomer, Integer>("fromSupplierId"));
		colDate.setCellValueFactory(new PropertyValueFactory<MsgToCustomer, String>("Date"));
		colHour.setCellValueFactory(new PropertyValueFactory<MsgToCustomer, String>("Hour"));
		colMessage.setCellValueFactory(new PropertyValueFactory<MsgToCustomer, String>("Message"));
		colStatus.setCellValueFactory(new PropertyValueFactory<MsgToCustomer, String>("Status"));
		tblMailBox.setItems(ChatClient.getMsgCustomerList());
	}

}
