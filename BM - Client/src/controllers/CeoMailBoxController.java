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
import logic.MsgToCEO;

/**
 * CeoMailBoxController implements the MailBox of the CEO.
 * 
 * @author Amit Kempner
 * @version 1.0 Build 100 December 27, 2021
 */
public class CeoMailBoxController implements Initializable {

	/** userIdNumber: using to save the current userId. */
	public static int userIdNumber;
	/** MsgidNumber: using to save the cureent message that CEO clicked. */
	private static int MsgidNumber;
	/** index: using to save the index of cureent message that CEO clicked. */
	private static int index;
	/** isSelectd: using to save the status of click on message. */
	private static boolean isSelectd = false;

	@FXML
	private Button btnBack;

	@FXML
	private Button btnClear;

	@FXML
	private TableColumn<MsgToCEO, String> colDate;

	@FXML
	private TableColumn<MsgToCEO, Integer> colFromManagerId;

	@FXML
	private TableColumn<MsgToCEO, String> colHour;

	@FXML
	private TableColumn<MsgToCEO, String> colMessage;

	@FXML
	private TableColumn<MsgToCEO, String> colStatus;

	@FXML
	private Label lblMsg;

	@FXML
	private TableView<MsgToCEO> tblMailBox;

	@FXML
	private Text txtMailBox;

	/**
	 * loadData: load all information that needed at Initial opening of page.
	 * 
	 * @param userId get the userId of the current user.
	 * @throws IOException
	 */
	public void loadData(int userId) throws IOException {
		ClientUI.chat.accept("getMsgToCeo:" + "\t" + String.valueOf(userId));
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
		ClientUI.chat.accept("getNumberCeoMsgNotRead:" + "\t" + String.valueOf(userIdNumber));
		ChatClient.MsgCeoList.clear();
		((Node) event.getSource()).getScene().getWindow().hide(); // hide window
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/controllers/CeoPage.fxml").openStream());
		CeoController ceoController = loader.getController();
		primaryStage.setTitle("CEO");
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		ceoController.loadData(String.valueOf(userIdNumber));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * getSelectd: get the number of messages that CEO already read by click on row
	 * of message.
	 * 
	 * @param event if the user press on a row in table.
	 * @throws IOException
	 */
	@FXML
	void getSelected(MouseEvent event) throws IOException {
		setSelectd(true);
		index = tblMailBox.getSelectionModel().getSelectedIndex();
		if (index <= -1)
			return;
		MsgidNumber = colFromManagerId.getCellData(index);
		ClientUI.chat.accept("msgCeoRead:" + "\t" + MsgidNumber);
	}

	/**
	 * selectClear: remove all messages from CEO mailbox by click on button.
	 * 
	 * @param event if the user click on clear all messages button.
	 * @throws IOException
	 */
	@FXML
	void selectClear(ActionEvent event) throws IOException {
		lblMsg.setText("");
		if (ChatClient.getMsgCeoList().isEmpty()) {
			lblMsg.setText("MailBox empty!");
			return;
		}
		if (index <= -1)
			return;
		ChatClient.getMsgCeoList().clear();
		ClientUI.chat.accept("removeMsgCeo:");
	}

	/**
	 * initialize: set all tabels column at Initial opening of page and get all
	 * messages from DB to screen.
	 *
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			loadData(ChatClient.u1.getIdNumber());
		} catch (IOException e) {
			e.printStackTrace();
		}
		colFromManagerId.setCellValueFactory(new PropertyValueFactory<MsgToCEO, Integer>("fromManagerId"));
		colDate.setCellValueFactory(new PropertyValueFactory<MsgToCEO, String>("Date"));
		colHour.setCellValueFactory(new PropertyValueFactory<MsgToCEO, String>("Hour"));
		colMessage.setCellValueFactory(new PropertyValueFactory<MsgToCEO, String>("Message"));
		colStatus.setCellValueFactory(new PropertyValueFactory<MsgToCEO, String>("Status"));
		tblMailBox.setItems(ChatClient.getMsgCeoList());
	}

	/**
	 * isSelected: return if the CEO select on row or not.
	 * 
	 * @return true, user press on row false, user not press on row.
	 */
	public static boolean isSelectd() {
		return isSelectd;
	}

	/**
	 * setSelectd: set the aprropriate value if user select row.
	 * 
	 * @param isSelectd get true if the CEO press on row.
	 */
	public static void setSelectd(boolean isSelectd) {
		CeoMailBoxController.isSelectd = isSelectd;
	}

}
