package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ChatClient;
import client.ClientUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.User;

/**
 * Class describes user details screen of BM manager and in specific branch
 * 
 * @author Tal Derai
 *
 */
public class ChangeUserDetailsController implements Initializable {

	/**
	 * userName: name of selected user. userType: type of selected user: for example
	 * Private/Business Customer and more options
	 * 
	 */
	private String userName, userType;

	/**
	 * userId: id of selected user
	 */
	private Integer userId;

	@FXML
	private AnchorPane TableViewUserDetails;

	@FXML
	private AnchorPane PageUserDetails;

	@FXML
	private Label lblTitleUserDetails;

	@FXML
	private TableView<User> TblUserDetails;

	@FXML
	private TableColumn<User, String> clmUserName;

	@FXML
	private TableColumn<User, String> clmPassword;

	@FXML
	private TableColumn<User, Integer> clmIdNumber;

	@FXML
	private TableColumn<User, String> clmFirstName;

	@FXML
	private TableColumn<User, String> clmLastName;

	@FXML
	private TableColumn<User, String> clmEmail;

	@FXML
	private TableColumn<User, String> clmPhoneNumber;

	@FXML
	private TableColumn<User, String> clmRole;

	@FXML
	private TableColumn<User, Boolean> clmLoggedIn;

	@FXML
	private TableColumn<User, String> clmType;

	@FXML
	private TableColumn<User, String> clmStatus;

	@FXML
	private Button btnBack;

	@FXML
	private Text txtUser;

	@FXML
	private Button btnExit;

	@FXML
	private ComboBox<String> cmbStatus;

	@FXML
	private Label lblStatus;

	@FXML
	private Label lblUserNameToChange;

	@FXML
	private Button btnUpdate;

	/**
	 * Initialize screen change user details
	 *
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			ClientUI.chat.accept("get user details:");
			UpdateTable();
			txtUser.setVisible(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * When clicked on Back button, return to BM manager page
	 * 
	 * @param event
	 * @throws Exception
	 */
	@FXML
	void Back(ActionEvent event) throws Exception {
		ClientUI.chat.accept("getNumberManagerMsgNotRead:" + "\t" + String.valueOf(ChatClient.u1.getIdNumber()));
		((Node) event.getSource()).getScene().getWindow().hide(); // hide window
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/controllers/BmManagerPage.fxml").openStream());
		BmManagerController bmManagerController = loader.getController();
		bmManagerController.loadData(ChatClient.u1.getUserName());
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		primaryStage.setTitle("Branch Manager");
		primaryStage.show();
	}

	/**
	 * When clicked on button Update, so update in DB and in table on screen
	 * 
	 * @param event
	 */
	@FXML
	void UpdateUserInTable(ActionEvent event) {
		if (userName == null) {
			lblUserNameToChange.setText("Please choose user!");
		} else {
			try {
				String status = cmbStatus.getValue();
				String sqlUpdate = "update details:" + "\t" + userName + "\t" + status + "\t" + userId.toString() + "\t"
						+ userType;
				ClientUI.chat.accept(sqlUpdate);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * When clicked on row in table, save details for change
	 * 
	 * @param event
	 */
	@FXML
	void getSelected(MouseEvent event) {
		txtUser.setVisible(true);
		int index = TblUserDetails.getSelectionModel().getSelectedIndex();
		if (index <= -1) {
			return;
		}
		userName = clmUserName.getCellData(index);
		if (clmType.getCellData(index).equals("-") || clmType.getCellData(index).equals("CEO")
				|| clmIdNumber.getCellData(index) == ChatClient.u1.getIdNumber()) {
			cmbStatus.setDisable(true);
			btnUpdate.setDisable(true);
		} else {
			cmbStatus.setDisable(false);
			btnUpdate.setDisable(false);
		}
		lblUserNameToChange.setText(userName);
		userType = clmType.getCellData(index);
		userId = clmIdNumber.getCellData(index);
		cmbStatus.setValue(clmStatus.getCellData(index).toString());
	}

	/**
	 * Initialize options of choose status for BM manager
	 * 
	 */
	private void setTypeOfStatusComboBox() {
		ObservableList<String> listStatus;
		ArrayList<String> cmb = new ArrayList<String>();
		cmb.add("Active");
		cmb.add("Locked");
		cmb.add("Frozen");
		cmb.add("Remove");
		listStatus = FXCollections.observableArrayList(cmb);
		cmbStatus.setItems(listStatus);
	}

	/**
	 * Initialize table of users
	 * 
	 */
	private void UpdateTable() {
		setTypeOfStatusComboBox();
		clmUserName.setCellValueFactory(new PropertyValueFactory<User, String>("userName"));
		clmPassword.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
		clmIdNumber.setCellValueFactory(new PropertyValueFactory<User, Integer>("idNumber"));
		clmFirstName.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
		clmLastName.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
		clmEmail.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
		clmPhoneNumber.setCellValueFactory(new PropertyValueFactory<User, String>("phoneNumber"));
		clmRole.setCellValueFactory(new PropertyValueFactory<User, String>("role"));

		clmLoggedIn.setCellValueFactory(new PropertyValueFactory<User, Boolean>("isLoggedIn"));

		clmType.setCellValueFactory(new PropertyValueFactory<User, String>("type"));
		clmStatus.setCellValueFactory(new PropertyValueFactory<User, String>("status"));

		if (ChatClient.u1.getRole().contains("North"))
			TblUserDetails.setItems(ChatClient.dataUsersNorth);
		else if (ChatClient.u1.getRole().contains("Central"))
			TblUserDetails.setItems(ChatClient.dataUsersCentral);
		else
			TblUserDetails.setItems(ChatClient.dataUsersSouth);
	}

	/**
	 * Upload screen of change user details
	 * 
	 * @param primaryStage
	 * @throws Exception
	 */
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/controllers/ChangeUserDetailsPage.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		primaryStage.setTitle("Change User Details");
		primaryStage.show();
	}
}
