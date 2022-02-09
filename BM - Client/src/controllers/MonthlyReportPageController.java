package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import client.ChatClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Description of MonthlyReportPageController The class is designed to generate
 * the presentation of the monthly report for the bm manager by using data from
 * the DB.
 * 
 * @author Michael Ravich
 * @version Version 1.0 Build 100 December 27, 2021
 */
public class MonthlyReportPageController implements Initializable {
	/**
	 * Description of selectMonthController - used to get restaurants name to set
	 * data for this page
	 */
	SelectMonthForReportController selectMonthController = new SelectMonthForReportController();
	@FXML
	private Label lblMonthlyReport;

	@FXML
	private Label lblRevenueReport;

	@FXML
	private Label lblOrdersReport;

	@FXML
	private Label lblPerformanceReport;

	@FXML
	private Label lblRevenue;

	@FXML
	private Label lblResName;

	@FXML
	private Label lblBranch;

	@FXML
	private Label lblNumOfOrders;

	@FXML
	private Label lblnumOfMainDishs;

	@FXML
	private Label lblNumOfFirstDish;

	@FXML
	private Label lblNumOfDrinks;

	@FXML
	private Label lblNumOfDesserts;

	@FXML
	private Label lblNumOfDelays;

	@FXML
	private Label lblNumberOfvalidOrders;

	@FXML
	private Label lblRevMainDish;

	@FXML
	private Label lblRevFirstdishs;

	@FXML
	private Label lblRevfromDrinks;

	@FXML
	private Label lblRevFromDesserts;

	@FXML
	private TextField txtTotalRevenue;

	@FXML
	private TextField txtRevFromMainDishs;

	@FXML
	private TextField txtRevFromFirstDishs;

	@FXML
	private TextField txtRevFromDrinks;

	@FXML
	private TextField txtRevFromDesserts;

	@FXML
	private TextField txtNumOfMainDishs;

	@FXML
	private TextField txtNumberOfFirstDishs;

	@FXML
	private TextField txtNumberOfDrinks;

	@FXML
	private TextField txtNumberOfDesserts;

	@FXML
	private TextField txtnumberOfOrders;

	@FXML
	private TextField txtNumbersOFDelays;

	@FXML
	private TextField txtValidOrders;

	@FXML
	private Button btnExit;

	@FXML
	private Button btnBack;

	/**
	 * Description of Back(ActionEvent event) - This method allows to the bm manager
	 * to go back to the select month for report page
	 * 
	 * @param event
	 * @throws Exception
	 */
	@FXML
	void Back(ActionEvent event) throws Exception {
		ChatClient.re1.setTotalRevenue((float) 0);
		ChatClient.re1.setRevMainDishs((float) 0);
		ChatClient.re1.setRevFirstDishs((float) 0);
		ChatClient.re1.setRevDrinks((float) 0);
		ChatClient.re1.setRevDesserts((float) 0);
		ChatClient.re1.setNumOfMainDishs(0);
		ChatClient.re1.setNumOfFirstDishs(0);
		ChatClient.re1.setNumOfDrinks(0);
		ChatClient.re1.setNumOfDesserts(0);
		ChatClient.re1.setTotalNumOfOrders(0);

		((Node) event.getSource()).getScene().getWindow().hide(); // hide window
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/controllers/SelectMonthForReportPage.fxml").openStream());
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		primaryStage.setTitle("View Monthly Reports");
		primaryStage.show();
	}

	/**
	 * Description of initialize(URL location, ResourceBundle resources)- The method
	 * Responsible for initializing this page and to set all the relevant data to
	 * their fields by textFields
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		lblResName.setText(selectMonthController.getResName());
		lblBranch.setText(returnBranchOfBM_Manager(ChatClient.u1.getRole()));
		txtTotalRevenue.setText(ChatClient.re1.getTotalRevenue().toString());
		txtRevFromMainDishs.setText(ChatClient.re1.getRevMainDishs().toString());
		txtRevFromFirstDishs.setText(ChatClient.re1.getRevFirstDishs().toString());
		txtRevFromDrinks.setText(ChatClient.re1.getRevDrinks().toString());
		txtRevFromDesserts.setText(ChatClient.re1.getRevDesserts().toString());
		txtNumOfMainDishs.setText(String.valueOf(ChatClient.re1.getNumOfMainDishs()));
		txtNumberOfFirstDishs.setText(String.valueOf(ChatClient.re1.getNumOfFirstDishs()));
		txtNumberOfDrinks.setText(String.valueOf(ChatClient.re1.getNumOfDrinks()));
		txtNumberOfDesserts.setText(String.valueOf(ChatClient.re1.getNumOfDesserts()));
		txtnumberOfOrders.setText(String.valueOf(ChatClient.re1.getTotalNumOfOrders()));
		txtNumbersOFDelays.setText(String.valueOf(ChatClient.re1.getTotalNumOfDelays()));
		Integer ValidOrders = ChatClient.re1.getTotalNumOfOrders() - ChatClient.re1.getTotalNumOfDelays();
		txtValidOrders.setText(String.valueOf(ValidOrders));
		txtTotalRevenue.setEditable(false);
		txtRevFromMainDishs.setEditable(false);
		txtRevFromFirstDishs.setEditable(false);
		txtRevFromDrinks.setEditable(false);
		txtRevFromDesserts.setEditable(false);
		txtNumOfMainDishs.setEditable(false);
		txtNumberOfFirstDishs.setEditable(false);
		txtNumberOfDrinks.setEditable(false);
		txtNumberOfDesserts.setEditable(false);
		txtnumberOfOrders.setEditable(false);
		txtNumbersOFDelays.setEditable(false);
		txtValidOrders.setEditable(false);

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
	 * Description of start(Stage primaryStage)- this method responsible to start
	 * the primary stage, and to launch the matching fxml file (Monthly report page)
	 * 
	 * @param primaryStage
	 * @throws Exception
	 */
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/controllers/MonthlyReportPage.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		primaryStage.setTitle("Monthly Report");
		primaryStage.show();
	}
}
