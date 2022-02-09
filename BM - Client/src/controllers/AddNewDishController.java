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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * class for dish adding page.
 * 
 * @author Raz Avraham
 * @version 1.0 Build 100 December 27, 2021
 */
public class AddNewDishController implements Initializable {
	boolean sizeFlag = false;
	boolean donenessFlag = false;
	int resID = SupplierController.resID;
	@FXML
	private ComboBox<String> cmbDishType;
	@FXML
	private TextField txtDishName;
	@FXML
	private Label lblErrorName;
	@FXML
	private Label lblErrorPrice;
	@FXML
	private TextField txtDishPrice;
	@FXML
	private Label lblDishSituation;

	private String getDishName() {
		return txtDishName.getText();
	}

	private String getDishPrice() {
		return txtDishPrice.getText();
	}

	/**
	 * Click On Doneness combo box, change doneness flag value.
	 * 
	 * @param event
	 */
	@FXML
	void clickDonenessCBox(ActionEvent event) {

		if (donenessFlag)
			donenessFlag = false;
		else
			donenessFlag = true;
	}

	/**
	 * Click On size combobox, change size flag value.
	 * 
	 * @param event
	 */
	@FXML
	void clickSizeCBox(ActionEvent event) {
		if (!sizeFlag)
			sizeFlag = true;
		else
			sizeFlag = false;
	}

	/**
	 * Click On back button, go back to the previous page.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void btnBack(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide(); // hide window
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/controllers/SupplierPage.fxml").openStream());
		Scene scene = new Scene(root);
		SupplierController supplierController = loader.getController();
		supplierController.loadData(String.valueOf(ChatClient.u1.getIdNumber()));
		primaryStage.setTitle("Supplier");
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * clear all the page labels.
	 * 
	 */
	public void clearLabels() {
		lblErrorPrice.setText("");
		lblErrorName.setText("");
		lblDishSituation.setText("");
	}

	ObservableList<String> list;

	/**
	 * Set values in the Dish type combo box.
	 * 
	 */
	private void setTypeOfDishComboBox() {
		ArrayList<String> cmb = new ArrayList<String>();
		cmb.add("Main Dish");
		cmb.add("First Dish");
		cmb.add("Salad");
		cmb.add("Dessert");
		cmb.add("Drink");
		list = FXCollections.observableArrayList(cmb);
		cmbDishType.setItems(list);
		cmbDishType.setValue("Main Dish");
	}

	/**
	 * Click On Add dish button. check that input is valid and insert dish to DB.
	 * 
	 * @param event
	 */
	@FXML
	void btnAddDish(ActionEvent event) {
		clearLabels();
		if (getDishName().trim().isEmpty()) {
			lblErrorName.setText("Enter dish name!");
		} else if (getDishPrice().trim().isEmpty()) {
			lblErrorPrice.setText("Enter dish price!");
		}

		else {
			try {
				ClientUI.chat.accept("AddDish:" + "\t" + getDishName() + "\t" + cmbDishType.getValue() + "\t"
						+ getDishPrice() + "\t" + donenessFlag + "\t" + sizeFlag + "\t" + resID); // add dish query
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (ChatClient.flag == 1) {
				lblDishSituation.setText("Dish already exist!");
				ChatClient.flag = 0;
			} else {
				lblDishSituation.setText("Dish added successfully!");
			}
		}
	}

	/**
	 * Start function. first function when enter page.
	 * 
	 * @param primaryStage
	 * @throws Exception
	 */
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/controllers/AddNewDishPage.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		primaryStage.setTitle("Add new dish");
		primaryStage.show();

	}

	/**
	 * initialize for combo box.
	 *
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setTypeOfDishComboBox();
	}

}
