package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import client.ChatClient;
import client.ClientUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * CreateAnOrderController implements the process of create an order.
 * 
 * @author Amit Kempner
 * @version 1.0 Build 100 December 27, 2021
 */
public class CreateAnOrderController {

	/** i: for index at HashMap. */
	private int i = 1;
	/** mapRes: get all restaurant by branch. */
	private Map<String, String> mapRes = new HashMap<String, String>();
	/** branch: ObservableList for all branchs to set at comboBox. */
	private ObservableList<String> branch;
	/** restaurant: ObservableList for all restuarants to set at comboBox. */
	private ObservableList<String> restaurant;
	/** item: ObservableList for all items to set at comboBox. */
	private ObservableList<String> item;
	/** dish: ObservableList for all dishes to set at comboBox. */
	private ObservableList<String> dish;
	/** doneness: ObservableList for all doneness to set at comboBox. */
	private ObservableList<String> doneness;
	/** size: ObservableList for all size to set at comboBox. */
	private ObservableList<String> size;
	/** tempOrderValues: ArrayList for saving all orederValues. */
	ArrayList<String> tempOrderValues;
	/** orderValues: ArrayList for saving all orederValues (for Confirm order). */
	public static ArrayList<String> orderValues = new ArrayList<String>();
	/** mapRes: save all dishes that customer take. */
	public static Map<Integer, ArrayList<String>> mapDish = new HashMap<Integer, ArrayList<String>>();
	/**
	 * flagSelectedAddNewDish: know that status of Add Dish button, true(selectd)
	 * other false.
	 */
	private boolean flagSelectedAddNewDish = false;
	/** userIdNumber: using to save the current userId. */
	private static String userIdNumber;
	/** customerType: using to save the type of customer : private or business. */
	private static String customerType;

	@FXML
	private AnchorPane anchorPane;

	@FXML
	private Button btnBack;

	@FXML
	private Button btnNext;

	@FXML
	private ComboBox<String> cmbDish;

	@FXML
	private ComboBox<String> cmbBranch;

	@FXML
	private ComboBox<String> cmbDoneness;

	@FXML
	private ComboBox<String> cmbItem;

	@FXML
	private ComboBox<String> cmbRestaurant;

	@FXML
	private ComboBox<String> cmbSize;

	@FXML
	private TextArea txtAreaAdditional;

	@FXML
	private Text txtDish;

	@FXML
	private Text txtItem;

	@FXML
	private Text txtRes;

	@FXML
	private Text txtDoneness;

	@FXML
	private Text txtSize;

	@FXML
	private Text txtAdd;

	@FXML
	private Text txtBranch;

	@FXML
	private Button btnAddDish;

	@FXML
	private Label lblMsgNext;

	/**
	 * loadData: load all information that needed at Initial opening of page.
	 * 
	 * @param typeOfCustomer get the type of customer : private or business.
	 * @param idNumber       get the userId of the current user.
	 */
	public void loadUserDetails(String typeOfCustomer, String idNumber) {
		userIdNumber = idNumber;
		customerType = typeOfCustomer;
		btnNext.setDisable(true);
	}

	/**
	 * loadBranchList: load all branch list to comboBox: north, central, south.
	 */
	public void loadBranchList() {
		ArrayList<String> b = new ArrayList<String>();
		b.add("North");
		b.add("Central");
		b.add("South");
		branch = FXCollections.observableArrayList(b);
		cmbBranch.setItems(branch);
		btnNext.setDisable(true);
		btnAddDish.setDisable(true);
		cmbRestaurant.setDisable(true);
		cmbItem.setDisable(true);
		cmbDish.setDisable(true);
		cmbDoneness.setDisable(true);
		cmbSize.setDisable(true);
		txtAreaAdditional.setDisable(true);
	}

	/**
	 * loadRestaurantList: load all restaurant list to comboBox.
	 * 
	 * @param r ArrayList for using FXCollections.observableArrayList.
	 */
	public void loadRestaurantList(ArrayList<String> r) {
		restaurant = FXCollections.observableArrayList(r);
		ChatClient.cmbRestaurant.clear();
		cmbRestaurant.setItems(restaurant);
	}

	/**
	 * loadItemList: load all items to comboBox.
	 * 
	 * @param i ArrayList for using FXCollections.observableArrayList.
	 */
	public void loadItemList(ArrayList<String> i) {
		item = FXCollections.observableArrayList(i);
		ChatClient.cmbItem.clear();
		cmbItem.setItems(item);
	}

	/**
	 * loadDishList: load all dishes to comboBox.
	 * 
	 * @param d ArrayList for using FXCollections.observableArrayList.
	 */
	public void loadDishList(ArrayList<String> d) {
		dish = FXCollections.observableArrayList(d);
		ChatClient.cmbDish.clear();
		cmbDish.setItems(dish);
	}

	/**
	 * loadDonenessList: load all doneness to comboBox.
	 * 
	 * @param don ArrayList for using FXCollections.observableArrayList.
	 */
	public void loadDonenessList(ArrayList<String> don) {
		doneness = FXCollections.observableArrayList(don);
		ChatClient.cmbDoneness.clear();
		cmbDoneness.setItems(doneness);
	}

	/**
	 * loadSizeList: load all sizes to comboBox.
	 * 
	 * @param s ArrayList for using FXCollections.observableArrayList.
	 */
	public void loadSizeList(ArrayList<String> s) {
		size = FXCollections.observableArrayList(s);
		ChatClient.cmbSize.clear();
		cmbSize.setItems(size);
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
	void Back(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hide window
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/controllers/W4CcardPage.fxml").openStream());
		W4CcardController w4cCardController = loader.getController();
		w4cCardController.loadData(customerType, userIdNumber);
		primaryStage.setTitle("W4C Card");
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Next: user moved by Next button to order time page.
	 * 
	 * @param event If user prees Next he move to the next page of order time page.
	 * @throws IOException
	 */
	@FXML
	void Next(ActionEvent event) throws IOException {
		if (!flagSelectedAddNewDish) {
			lblMsgNext.setText("Add new dish!");
			return;
		}
		((Node) event.getSource()).getScene().getWindow().hide(); // hide window
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/controllers/OrderTimePage.fxml").openStream());
		OrderTimeController orderTimeController = loader.getController();
		orderTimeController.loadData(orderValues.get(1));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Order Time");
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * selectAddDish: user press on select add new dish and can add more dishes.
	 * 
	 * @param event If user select add dish, he\she can add more dishes from the
	 *              same restaurnt to the order.
	 * @throws IOException
	 */
	@FXML
	void selectAddDish(ActionEvent event) throws IOException {
		flagSelectedAddNewDish = true;
		btnNext.setDisable(false);
		cmbBranch.setDisable(true);
		cmbRestaurant.setDisable(true);
		setValues(orderValues);
		tempOrderValues = new ArrayList<String>();
		for (String j : orderValues)
			tempOrderValues.add(j);
		mapDish.put(i++, tempOrderValues);
		cmbItem.valueProperty().set(null);
		cmbDish.valueProperty().set(null);
		cmbDoneness.valueProperty().set(null);
		cmbSize.valueProperty().set(null);
	}

	/**
	 * selectBranch: When user select a specific brnach at ComboBox.
	 * 
	 * @param event If user select branch, the restaurants from the appropriate
	 *              branch wiil open.
	 * @throws IOException
	 */
	@FXML
	void selectBranch(ActionEvent event) throws IOException {
		mapRes.clear();
		cmbRestaurant.valueProperty().set(null);
		cmbItem.valueProperty().set(null);
		cmbDish.valueProperty().set(null);
		cmbDoneness.valueProperty().set(null);
		cmbSize.valueProperty().set(null);
		cmbRestaurant.setDisable(false);
		ClientUI.chat.accept("cmbRestaurant:" + "\t" + cmbBranch.getValue() + "\t");
		for (int i = 0; i < ChatClient.cmbRestaurant.size() - 1; i += 2)
			mapRes.put(ChatClient.cmbRestaurant.get(i + 1), ChatClient.cmbRestaurant.get(i));
		loadRestaurantList(new ArrayList<String>(mapRes.keySet()));
	}

	/**
	 * selectRes: When user select a specific restaurant at ComboBox.
	 * 
	 * @param event If user select restaurant, all items will open.
	 * @throws IOException
	 */
	@FXML
	void selectRes(ActionEvent event) throws IOException {
		cmbItem.setDisable(false);
		cmbItem.valueProperty().set(null);
		cmbDish.valueProperty().set(null);
		cmbDoneness.valueProperty().set(null);
		cmbSize.valueProperty().set(null);
		ArrayList<String> i = new ArrayList<String>();
		i.add("First Dish");
		i.add("Main Dish");
		i.add("Salad");
		i.add("Dessert");
		i.add("Drink");
		loadItemList(i);

	}

	/**
	 * selectItem: When user select a specific item at ComboBox.
	 * 
	 * @param event If user select item, the dishes from the appropriate restaurant
	 *              wiil open accordingly to item.
	 * @throws IOException
	 */
	@FXML
	void selectItem(ActionEvent event) throws IOException {
		cmbDish.setDisable(false);
		cmbDish.valueProperty().set(null);
		cmbDoneness.valueProperty().set(null);
		cmbSize.valueProperty().set(null);
		loadValidDishes();
	}

	/**
	 * selectDish: When user select a dish at ComboBox.
	 * 
	 * @param event If user select dish, the size and doneness of dish will appear
	 *              accordingly to the dish.
	 * @throws IOException
	 */
	@FXML
	void selectDish(ActionEvent event) throws IOException {
		cmbDoneness.setDisable(false);
		cmbSize.setDisable(false);
		txtAreaAdditional.setDisable(false);
		loadValidDoneness();
		loadValidSize();
		if (ChatClient.isDoneness) {
			cmbDoneness.setDisable(false);
		} else {
			doneness.clear();
			cmbDoneness.setDisable(true);
		}
		if (ChatClient.isSize) {
			cmbSize.setDisable(false);
		} else {
			size.clear();
			cmbSize.setDisable(true);
		}
		if (!ChatClient.isDoneness && !ChatClient.isSize) {
			btnNext.setDisable(false);
			btnAddDish.setDisable(false);
		}
	}

	/**
	 * selectDoneness: When user select a doneness at ComboBox.
	 * 
	 * @param event If user select doneness, the button of add dish change to
	 *              enable.
	 * @throws IOException
	 */
	@FXML
	void selectDoneness(ActionEvent event) {
		if (ChatClient.isDoneness && !ChatClient.isSize) {
			btnAddDish.setDisable(false);
		}
	}

	/**
	 * selectSize: When user select a size at ComboBox.
	 * 
	 * @param event If user select size, the button of add dish change to enable.
	 * @throws IOException
	 */
	@FXML
	void selectSize(ActionEvent event) {
		if (ChatClient.isDoneness && ChatClient.isSize) {
			btnAddDish.setDisable(false);
		}
		if (!ChatClient.isDoneness && ChatClient.isSize) {
			btnAddDish.setDisable(false);
		}
	}

	/**
	 * loadValidDishes: load all valid dishs by restaurant and branch.
	 * 
	 * @throws IOExceptions
	 */
	private void loadValidDishes() throws IOException {
		if (cmbRestaurant.getSelectionModel().getSelectedItem() != null
				&& cmbItem.getSelectionModel().getSelectedItem() != null) {
			String resName = cmbRestaurant.getSelectionModel().getSelectedItem().toString();
			String itemName = cmbItem.getSelectionModel().getSelectedItem().toString();
			String resId = mapRes.get(resName);
			ClientUI.chat.accept("cmbDish:" + "\t" + resId + "\t" + itemName + "\t");
		}
		loadDishList(ChatClient.cmbDish);
		ChatClient.cmbDish.clear();
		btnAddDish.setDisable(true);
		cmbDoneness.setDisable(true);
		cmbSize.setDisable(true);
		txtAreaAdditional.clear();
	}

	/**
	 * loadValidDoneness: load all valid doneness.
	 * 
	 * @throws IOExceptions
	 */
	private void loadValidDoneness() throws IOException {
		String dishName = null;
		if (cmbDish.getSelectionModel().getSelectedItem() != null) {
			dishName = cmbDish.getSelectionModel().getSelectedItem().toString();
			dishName = dishName.substring(0, dishName.indexOf(','));
		}
		if (dishName != null) {
			ClientUI.chat.accept("donenessValid:" + "\t" + dishName);
			ArrayList<String> d = new ArrayList<String>();
			d.add("R");
			d.add("M");
			d.add("MW");
			d.add("W");
			d.add("WD");
			loadDonenessList(d);
		}
	}

	/**
	 * loadValidSize: load all valid sizes.
	 * 
	 * @throws IOExceptions
	 */
	private void loadValidSize() throws IOException {
		String dishName = null;
		if (cmbDish.getSelectionModel().getSelectedItem() != null) {
			dishName = cmbDish.getSelectionModel().getSelectedItem().toString();
			dishName = dishName.substring(0, dishName.indexOf(','));
		}
		if (dishName != null) {
			ClientUI.chat.accept("sizeValid:" + "\t" + dishName);
			ArrayList<String> s = new ArrayList<String>();
			s.add("S");
			s.add("M");
			s.add("L");
			loadSizeList(s);
		}
	}

	/**
	 * set all order values to the invoice of order.
	 * 
	 * @param oValues get order all order valuse.
	 * @return return ArrayList of all order details.
	 */
	private ArrayList<String> setValues(ArrayList<String> oValues) {
		oValues.clear();
		oValues.add(cmbBranch.getSelectionModel().getSelectedItem().toString());
		oValues.add(cmbRestaurant.getSelectionModel().getSelectedItem().toString());
		if (cmbItem.getSelectionModel().getSelectedItem() != null)
			oValues.add(cmbItem.getSelectionModel().getSelectedItem().toString());
		if (cmbDish.getSelectionModel().getSelectedItem() != null) {
			String dishName = cmbDish.getSelectionModel().getSelectedItem().toString();
			oValues.add(dishName.substring(0, dishName.indexOf(',')));
			int index = dishName.indexOf(',');
			index += 2;
			oValues.add(dishName.substring(index, dishName.lastIndexOf('S') + 1));
		}
		if (cmbDoneness.getSelectionModel().getSelectedItem() != null)
			oValues.add(cmbDoneness.getSelectionModel().getSelectedItem().toString());
		else
			oValues.add(null);
		if (cmbSize.getSelectionModel().getSelectedItem() != null)
			oValues.add(cmbSize.getSelectionModel().getSelectedItem().toString());
		else
			oValues.add(null);
		if (!txtAreaAdditional.getText().trim().isEmpty())
			oValues.add(txtAreaAdditional.getText());
		else
			oValues.add(null);
		return oValues;
	}
}
