package controllers;

import java.io.IOException;
import java.net.URL;
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
import logic.MsgToHR;

/**
 * HrMailBoxController implements the MailBox of all HR at BiteMe.
 * 
 * @author Amit Kempner
 * @version 1.0 Build 100 December 27, 2021
 */
public class HrMailBoxController implements Initializable {

	/** businessName: using to save the current business Name of HR Manager. */
	public static String businessName;
	/** userIdNumber: using to save the current userId. */
	public static int userIdNumber;
	/** MsgidNumber: using to save the cureent message that HR Manager clicked. */
	private static int MsgidNumber;
	/**
	 * index: using to save the index of cureent message that HR Manager clicked.
	 */
	private static int index;
	/** isSelectd: using to save the status of click on message. */
	private static boolean isSelectd = false;

	/** statusWaiting: String of messgae status. */
	String statusWaiting = "Waiting";
	/** statusApproved: String of messgae status. */
	String statusApproved = "Approved";
	/** statusFinish: String of messgae status. */
	String statusFinish = "Finish";

	@FXML
	private Button btnBack;

	@FXML
	private Button btnClear;

	@FXML
	private Button btnConfirm;

	@FXML
	private TableColumn<MsgToHR, String> colDate;

	@FXML
	private TableColumn<MsgToHR, Integer> colFromManagerId;

	@FXML
	private TableColumn<MsgToHR, String> colHour;

	@FXML
	private TableColumn<MsgToHR, String> colMessage;

	@FXML
	private TableColumn<MsgToHR, String> colStatus;

	@FXML
	private Label lblMsg;

	@FXML
	private TableView<MsgToHR> tblMailBox;

	@FXML
	private Text txtMailBox;

	/**
	 * loadData: load all information that needed at Initial opening of page.
	 * 
	 * @param userId  get the userId of the current user.
	 * @param busName get the businessName of the current hr.
	 * @throws IOException
	 */
	public void loadData(int userId, String busName) throws IOException {
		ClientUI.chat.accept("getMsgToHr:" + "\t" + String.valueOf(userId));
		userIdNumber = userId;
		businessName = busName;
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
		ClientUI.chat.accept("getNumberHrMsgNotRead:" + "\t" + String.valueOf(userIdNumber));
		ChatClient.MsgHrList.clear();
		((Node) event.getSource()).getScene().getWindow().hide(); // hide window
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/controllers/HrPage.fxml").openStream());
		HrController hrController = loader.getController();
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		primaryStage.setTitle("HR Manager");
		hrController.loadData(String.valueOf(userIdNumber));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * getSelectd: get the number of messages that HR Manager already read by click
	 * on row of message.
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
		MsgidNumber = colFromManagerId.getCellData(index);
		ClientUI.chat.accept("msgHrRead:" + "\t" + MsgidNumber);
	}

	/**
	 * selectClear: remove all messages from HR Manager mailbox by click on button.
	 * 
	 * @param event if the user click on clear all messages button.
	 * @throws IOException
	 */
	@FXML
	void selectClear(ActionEvent event) throws IOException {
		lblMsg.setText("");
		if (ChatClient.getMsgHrList().isEmpty()) {
			lblMsg.setText("MailBox empty!");
			return;
		}
		if (index <= -1)
			return;
		ChatClient.getMsgHrList().clear();
		ClientUI.chat.accept("removeMsgHr:");
	}

	/**
	 * Confirm: When HR Manager accept the registration of new business customer to
	 * BiteMe system.
	 * 
	 * @param event If HR Manager press on confirm button the business customer can
	 *              start work with BiteMe system.
	 * @throws IOException
	 */
	@FXML
	void Confirm(ActionEvent event) throws IOException {
		if (!checkSelection())
			return;
		if (colStatus.getCellData(index).equals(statusApproved)) {
			lblMsg.setText("Order status already Approved!");
			return;
		}
		if (colStatus.getCellData(index).equals(statusFinish)) {
			lblMsg.setText("Order status already Finish!");
			return;
		}
		if (colStatus.getCellData(index).equals("-")) {
			lblMsg.setText("You cant accept this message!");
			return;
		}
		ClientUI.chat.accept("insertIntoBusinessCustomer:" + "\t" + colMessage.getCellData(index).split(" ")[2]);
		ClientUI.chat.accept(
				"ChangeUserType:" + "\t" + "Business_Customer" + "\t" + colMessage.getCellData(index).split(" ")[2]);
		ClientUI.chat.accept("updateMsgHr:" + "\t" + colMessage.getCellData(index) + "\t" + "Finish");
		lblMsg.setText(colMessage.getCellData(index).split(" ")[4] + " " + colMessage.getCellData(index).split(" ")[5]
				+ " registered successfuly!");
		ClientUI.chat.accept("getMsgToHr:" + "\t" + String.valueOf(userIdNumber));
		isSelectd = false;
	}

	/**
	 * checkSelection: check if HR Manager press on appropriate button, if not send
	 * a message into label accordingly.
	 * 
	 * @return true if the check pass sucessfuly, false if not.
	 */
	private boolean checkSelection() {
		lblMsg.setText("");
		if (ChatClient.getMsgHrList().isEmpty()) {
			lblMsg.setText("MailBox empty!");
			return false;
		}
		if (!isSelectd) {
			lblMsg.setText("choose a message!");
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
		colFromManagerId.setCellValueFactory(new PropertyValueFactory<MsgToHR, Integer>("fromUserId"));
		colDate.setCellValueFactory(new PropertyValueFactory<MsgToHR, String>("Date"));
		colHour.setCellValueFactory(new PropertyValueFactory<MsgToHR, String>("Hour"));
		colMessage.setCellValueFactory(new PropertyValueFactory<MsgToHR, String>("Message"));
		colStatus.setCellValueFactory(new PropertyValueFactory<MsgToHR, String>("Status"));
		tblMailBox.setItems(ChatClient.getMsgHrList());
	}

}
