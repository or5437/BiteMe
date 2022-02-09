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
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.Dish;

/**
 * class for update dish page
 * 
 * @author Raz Avraham
 * @version 1.0 Build 100 December 27, 2021
 */
public class UpdateDishController implements Initializable {
	/**
	 * the restaurant ID of the current supplier
	 */
	int resID = SupplierController.resID;
	/**
	 * observable list for types combo box
	 */
	ObservableList<String> types_list;
	/**
	 * observable list for dishes combo box
	 */
	ObservableList<String> dishes_list;
	/**
	 * flag for size optional component
	 */
	boolean isSize = false;
	/**
	 * flag for doneness optional component
	 */
	boolean isDoneness = false;
	@FXML
	private ComboBox<String> cmbDishType;

	@FXML
	private ComboBox<String> cmbDishName;

	@FXML
	private TextField txtDishPrice;

	@FXML
	private Label lblErrorName;

	@FXML
	private Label lblErrorPrice;

	@FXML
	private Label lblDishUpdate;

	@FXML
	private Label lblErrorDish;

	@FXML
	private Label lblDishDelete;

	@FXML
	private CheckBox cboxSize;

	@FXML
	private CheckBox cboxDoneness;

	/**
	 * click on doneness checkbox, change flag value.
	 * 
	 * @param event
	 */
	@FXML
	void clickDoneness(ActionEvent event) {
		if (!isDoneness)
			isDoneness = true;
		else
			isDoneness = false;
	}

	/**
	 * click on size checkbox, change flag value.
	 * 
	 * @param event
	 */
	@FXML
	void clickSize(ActionEvent event) {
		if (!isSize)
			isSize = true;
		else
			isSize = false;
	}

	/**
	 * click on back button, go back to the previous page.
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
		SupplierController supplierController = loader.getController();
		supplierController.loadData(String.valueOf(ChatClient.u1.getIdNumber()));
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		primaryStage.setTitle("Supplier");
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * get the dish price from text
	 * 
	 * @return text value in dish price text field
	 */
	public String getDishPrice() {
		return txtDishPrice.getText();
	}

	/**
	 * clear all the page labels
	 */
	public void clearLabels() {
		lblDishDelete.setText("");
		lblDishUpdate.setText("");
		lblErrorDish.setText("");
		lblErrorPrice.setText("");
	}

	/**
	 * set values of dish type in list
	 */
	private void setTypeOfDishComboBox() {
		ArrayList<String> types = new ArrayList<String>(); // todo: take types from db
		types.add("Main Dish");
		types.add("First Dish");
		types.add("Salad");
		types.add("Dessert");
		types.add("Drink");
		loadTypeComboBox(types);

	}

	/**
	 * load the list types into combo box
	 * 
	 * @param types get the list of values
	 */
	public void loadTypeComboBox(ArrayList<String> types) {
		types_list = FXCollections.observableArrayList(types);
		cmbDishType.setItems(types_list);

	}

	/**
	 * set values of dishes in list
	 * 
	 * @throws IOException
	 */
	private void setNameOfDishComboBox() throws IOException {
		ClientUI.chat.accept("getDishes:" + "\t" + resID + "\t" + cmbDishType.getValue());
		loadDishComboBox(ChatClient.dishes);

	}

	/**
	 * load the list values in combo box
	 * 
	 * @param dishes get the list of dishes
	 */
	public void loadDishComboBox(ArrayList<String> dishes) {
		dishes_list = FXCollections.observableArrayList(dishes);
		cmbDishName.setItems(dishes_list);
		ChatClient.dishes.removeAll(dishes);
	}

	/**
	 * click on type combo box, reset values.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void clickComboBoxType(ActionEvent event) throws IOException {
		setNameOfDishComboBox();
		txtDishPrice.setText("");
		cboxDoneness.setSelected(false);
		cboxSize.setSelected(false);

	}

	/**
	 * initialize for combo box.
	 *
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		SetStartScreen();

	}

	/**
	 * set the first screen when load page
	 * 
	 */
	public void SetStartScreen() {
		setTypeOfDishComboBox();
		txtDishPrice.setText("");
	}

	/**
	 * click on dish combo box call function to set the relevant dish details.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void clickComboBoxName(ActionEvent event) throws IOException {
		setDishDetails();
	}

	/**
	 * get dish details from DB.
	 * 
	 * @throws IOException
	 */
	public void setDishDetails() throws IOException {
		ClientUI.chat.accept("getDishDetails:" + "\t" + cmbDishName.getValue() + "\t" + resID);
		loadDishDetails(ChatClient.d1);
	}

	/**
	 * load dish details to text fields and check boxes
	 * 
	 * @param d get the dish details in dish entity
	 */
	public void loadDishDetails(Dish d) {
		txtDishPrice.setText(String.valueOf(d.getPrice()));
		if (d.isDoneness()) {
			cboxDoneness.setSelected(true);
			isDoneness = true;
		}
		if (d.isSize()) {
			cboxSize.setSelected(true);
			isSize = true;
		}
	}

	/**
	 * click on Update dish, check details validation and update dish in DB
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void btnUpdateDish(ActionEvent event) throws IOException {
		clearLabels();
		if (cmbDishName.getItems().isEmpty() || cmbDishName.getValue().equals("choose dish")
				|| cmbDishName.getValue().equals(""))
			lblErrorDish.setText("Plaese choose a dish");
		else if (getDishPrice().trim().isEmpty() || Float.parseFloat(getDishPrice()) < 0) {
			lblErrorPrice.setText("Enter valid dish price!");
		} else {
			ClientUI.chat.accept("UpdateDish:" + "\t" + cmbDishName.getValue() + "\t" + resID + "\t" + getDishPrice()
					+ "\t" + isSize + "\t" + isDoneness);
			lblDishUpdate.setText("Successfully updated");
		}
	}

	/**
	 * click on delete dish, check details validation and delete the relevant dish
	 * from DB
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void btnDeleteDish(ActionEvent event) throws IOException {
		clearLabels();
		if (cmbDishName.getItems().isEmpty() || cmbDishName.getValue().equals("choose dish")
				|| cmbDishName.getValue().equals(""))
			lblErrorDish.setText("Plaese choose a dish");
		else {
			ClientUI.chat.accept("DeleteDish:" + "\t" + cmbDishName.getValue() + "\t" + resID);
			lblDishDelete.setText("Deleted");
			SetStartScreen();
		}

	}

}
