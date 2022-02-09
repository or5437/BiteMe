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
import logic.MsgToHR;
import logic.MsgToManager;

/**
 * ManagerMailBoxController implements the MailBox of all Branch Manager,
 * North:Central:South.
 * 
 * @author Amit Kempner
 * @version 1.0 Build 100 December 27, 2021
 */
public class ManagerMailBoxController implements Initializable {

	/** userIdNumber: using to save the current userId. */
	private static int userIdNumber;
	/**
	 * MsgidNumber: using to save the cureent message that Branch Manager clicked.
	 */
	private static int MsgidNumber;
	/**
	 * index: using to save the index of cureent message that Branch Manager
	 * clicked.
	 */
	private static int index;
	/** isSelectd: using to save the status of click on message. */
	private static boolean isSelectd = false;
	/** mth: a new instance of MsgToHR Class. */
	private MsgToHR mth = new MsgToHR(0, 0, null, null, null, null, false);
	/** statusWaiting: String of messgae status. */
	String statusWaiting = "Waiting";
	/** statusApproved: String of messgae status. */
	String statusApproved = "Approved";
	/** statusFinish: String of messgae status. */
	String statusFinsh = "Finish";

	@FXML
	private Button btnConfirm;

	@FXML
	private Button btnBack;

	@FXML
	private Button btnClear;

	@FXML
	private TableColumn<MsgToManager, String> colDate;

	@FXML
	private TableColumn<MsgToManager, Integer> colFromHrId;

	@FXML
	private TableColumn<MsgToManager, String> colHour;

	@FXML
	private TableColumn<MsgToManager, String> colMessage;

	@FXML
	private TableColumn<MsgToManager, String> colStatus;

	@FXML
	private Label lblMsg;

	@FXML
	private TableView<MsgToManager> tblMailBox;

	@FXML
	private Text txtMailBox;

	/**
	 * loadData: load all information that needed at Initial opening of page.
	 * 
	 * @param userId get the userId of the current user.
	 * @throws IOException
	 */
	public void loadData(int userId) throws IOException {
		ClientUI.chat.accept("getMsgToManager:" + "\t" + String.valueOf(userId));
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
		ClientUI.chat.accept("getNumberManagerMsgNotRead:" + "\t" + String.valueOf(userIdNumber));
		ChatClient.MsgManagerList.clear();
		((Node) event.getSource()).getScene().getWindow().hide(); // hide window
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		primaryStage.setTitle("Branch Manager");
		Pane root = loader.load(getClass().getResource("/controllers/BmManagerPage.fxml").openStream());
		BmManagerController bmManagerController = loader.getController();
		bmManagerController.loadData(String.valueOf(userIdNumber));
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * getSelectd: get the number of messages that Branch Manager already read by
	 * click on row of message.
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
		MsgidNumber = colFromHrId.getCellData(index);
		ClientUI.chat.accept("msgManagerRead:" + "\t" + MsgidNumber);
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
		if (ChatClient.getMsgManagerList().isEmpty()) {
			lblMsg.setText("MailBox empty!");
			return;
		}
		if (index <= -1)
			return;
		ChatClient.getMsgManagerList().clear();
		ClientUI.chat.accept("removeMsgManager:");
	}

	/**
	 * Confirm: When Branch Manager accept the registration of new business to
	 * BiteMe system.
	 * 
	 * @param event If Branch Manager press on confirm button the business is
	 *              recognized and licensed in the system of BiteMe.
	 * @throws IOException
	 */
	@FXML
	void Confirm(ActionEvent event) throws IOException {
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
		setApprovedMsgToHRData();
		String msgToHR = "msgToHR:" + "\t" + String.valueOf(mth.getFromUserId()) + "\t"
				+ String.valueOf(mth.getToUserId()) + "\t" + mth.getDate() + "\t" + mth.getHour() + "\t"
				+ mth.getMessage() + "\t" + mth.getStatus() + "\t" + String.valueOf(mth.isRead());
		ClientUI.chat.accept(msgToHR);
		ClientUI.chat.accept("FindBusinessName:" + "\t" + mth.getToUserId());
		ClientUI.chat.accept("UpdateWaitingBusConfirm:" + "\t" + ChatClient.businessName);
		ClientUI.chat.accept("takeBusData:" + "\t" + ChatClient.businessName);
		ClientUI.chat.accept("BusinessRegistration:" + "\t" + ChatClient.b1.getBusinessName() + "\t"
				+ ChatClient.b1.getBusinessAddress() + "\t" + ChatClient.b1.getBusinessPhone() + "\t"
				+ ChatClient.b1.getMonthlyBudget());
		ClientUI.chat.accept("updateMsgManager:" + "\t" + String.valueOf(mth.getToUserId()) + "\t" + "Approved");
		lblMsg.setText("The business successfully registered, a message has been sent to HR!");
		ClientUI.chat.accept("getMsgToManager:" + "\t" + String.valueOf(userIdNumber));
		isSelectd = false;
	}

	/**
	 * setApprovedMsgToHRData: Set the message that the HR Manager will receive when
	 * Branch manager will accept the registration request.
	 * 
	 */
	public void setApprovedMsgToHRData() {
		mth.setFromUserId(userIdNumber);
		mth.setToUserId(colFromHrId.getCellData(index));
		String changeFormatDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		mth.setDate(changeFormatDate.toString());
		mth.setHour(LocalTime.now().getHour() + ":" + LocalTime.now().getMinute());
		mth.setMessage("The business successfully registered!");
		mth.setStatus("Finish");
		mth.setRead(false);
	}

	/**
	 * checkSelection: check if Branch Manager press on appropriate button, if not
	 * send a message into label accordingly.
	 * 
	 * @return true if the check pass sucessfuly, false if not.
	 */
	private boolean checkSelection() {
		lblMsg.setText("");
		if (ChatClient.getMsgManagerList().isEmpty()) {
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
		colFromHrId.setCellValueFactory(new PropertyValueFactory<MsgToManager, Integer>("fromUserId"));
		colDate.setCellValueFactory(new PropertyValueFactory<MsgToManager, String>("Date"));
		colHour.setCellValueFactory(new PropertyValueFactory<MsgToManager, String>("Hour"));
		colMessage.setCellValueFactory(new PropertyValueFactory<MsgToManager, String>("Message"));
		colStatus.setCellValueFactory(new PropertyValueFactory<MsgToManager, String>("Status"));
		tblMailBox.setItems(ChatClient.getMsgManagerList());
	}

}
