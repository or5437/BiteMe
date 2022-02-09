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
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Description of SelectMonthForReportController The class is designed to let
 * the bm manager to choose month for the presentation of the monthly report.
 * 
 * @author Michael Ravich
 * @version Version 1.0 Build 100 December 27, 2021
 */
public class SelectMonthForReportController implements Initializable {
	/** Description of Months -Boot an array of year months for setting data */
	String[] Months = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
			"October", "November", "December" };
	/** Description of list - used to set months and restaurants combo box */
	ObservableList<String> list;
	@FXML
	private Label lblMonthlyReport;

	@FXML
	private Label lblMonth;

	@FXML
	private ComboBox<String> cmbMonth;

	@FXML
	private ComboBox<String> cmbRestaurant;

	@FXML
	private Label lblBranch;

	@FXML
	private Label lblErrorMsg;

	@FXML
	private TextField txtBranch;

	@FXML
	private Label lblYear;

	@FXML
	private TextField txtYear;
	@FXML
	private Label lblRes;

	@FXML
	private Button btnNext;

	@FXML
	private Button btnBack;

	@FXML
	private Button btnExit;

	private static String resName;

	public String getResName() {
		return resName;
	}

	/**
	 * Description of Back(ActionEvent event) This method allows to the bm manager
	 * to go back to the bm manager central page
	 * 
	 * @param event
	 * @throws Exception - checked exception
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
	 * Description of selectRestaurant(ActionEvent event) when bm manager selects a
	 * restaurant, save his choise in a resName variable (static string)
	 * 
	 * @param event
	 */
	@FXML
	void selectRestaurant(ActionEvent event) {
		resName = cmbRestaurant.getValue();
	}

	/**
	 * Description of Next(ActionEvent event) By clicking on next, this method will
	 * move on to the monthly reports view page
	 * 
	 * @param event
	 * @throws Exception
	 */
	@FXML
	void Next(ActionEvent event) throws Exception {
		Integer indexMonth = 0;
		String month = cmbMonth.getValue();
		for (int i = 0; i < Months.length; i++)
			if (Months[i].equals(month))
				indexMonth = i + 1;
		String year = txtYear.getText();
		if (year.trim().isEmpty())
			lblErrorMsg.setText("Please insert a valid year!");
		ClientUI.chat.accept("getReportDetails:" + "\t" + txtBranch.getText() + "\t" + cmbRestaurant.getValue() + "\t"
				+ txtYear.getText() + "\t" + String.valueOf(indexMonth));
		if (ChatClient.re1.getTotalRevenue() == (float) 0)
			lblErrorMsg.setText("There isn't a matching report to this date!");

		else {
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			Stage primaryStage = new Stage();
			MonthlyReportPageController monthlyReportPageController = new MonthlyReportPageController();
			monthlyReportPageController.start(primaryStage);
		}
	}

	/**
	 * Description of setMonthlyCmb - this method responsible to set Monthly combo
	 * box by yearly months
	 * 
	 */
	private void setMonthlyCmb() {
		cmbMonth.setValue("January");
		ArrayList<String> cmb = new ArrayList<String>();
		for (String date : Months)
			cmb.add(date);
		list = FXCollections.observableArrayList(cmb);
		cmbMonth.setItems(list);
	}

	/**
	 * Description of setRestaurantCmb - this method responsible to set Restaurant
	 * combo box by relevant Restaurants
	 * @throws IOException
	 */
	public void setRestaurantCmb() throws IOException {
		ArrayList<String> cmb = new ArrayList<String>();
		String[] details = (ChatClient.r1.getRestaurantName().split("\\t"));
		for (String str : details)
			cmb.add(str);
		list = FXCollections.observableArrayList(cmb);
		cmbRestaurant.setItems(list);
	}

	/**
	 * Description of returnBranchOfBM_Manager(String Role)- This method responsible
	 * to extract the bm manager branch from his role field
	 * 
	 * @param Role - whole role includes the branch name
	 * @return String - only the branch name
	 */
	private String returnBranchOfBM_Manager(String Role) {
		if (Role.contains("North"))
			return "North";
		else if (Role.contains("Central"))
			return "Central";
		else if (Role.contains("South"))
			return "South";
		return null;
	}

	/**
	 * Description of initialize(URL location, ResourceBundle resources) - The
	 * method Responsible for initializing this page and to send a message to the
	 * server, which will get the restaurants name from the DB for the Combo box
	 * initialization.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// btnNext.setDisable(true);
		try {
			ClientUI.chat.accept("getResName:" + "\t" + returnBranchOfBM_Manager(ChatClient.u1.getRole()));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		setMonthlyCmb();
		try {
			setRestaurantCmb();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		txtBranch.setText(returnBranchOfBM_Manager(ChatClient.u1.getRole()));
		txtBranch.setEditable(false);

	}

	/**
	 * Description of start(Stage primaryStage)- this method responsible to start
	 * the primary stage, and to launch the matching fxml file
	 * (SelectMonthForReportPage)
	 * 
	 * @param primaryStage
	 * @throws Exception
	 */
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/controllers/SelectMonthForReportPage.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		primaryStage.setTitle("View Monthly Reports");
		primaryStage.show();
	}

}
