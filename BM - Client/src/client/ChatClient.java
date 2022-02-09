package client;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import common.ChatIF;
import controllers.EnterIpController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import logic.Business;
import logic.BusinessCustomer;
import logic.Dish;
import logic.DishInOrder;
import logic.Employer;
import logic.MsgToCEO;
import logic.MsgToCustomer;
import logic.MsgToHR;
import logic.MsgToManager;
import logic.MsgToSupplier;
import logic.MyFile;
import logic.Order;
import logic.Refund;
import logic.Report;
import logic.Restaurant;
import logic.User;
import ocsf.client.AbstractClient;

/**
 * * This class overrides some of the methods defined in the abstract superclass
 * in order to give more functionality to the client.
 * 
 * @author Or Hilo, Michael Ravich, Raz Avraham, Amit Kempner, Tal Derai
 * @version 1.0 Build 100 December 29, 2021
 */

public class ChatClient extends AbstractClient {
	// ************************ Instance variables ************************

	/**
	 * The interface type variable. It allows the implementation of the display
	 * method in the client.
	 */
	ChatIF clientUI;
	/**
	 * b is a variable of Blob to get pdf file.
	 */
	public static SerialBlob b;
	/**
	 * isFileExist get true if the file exist in DB.
	 */
	public static boolean isFileExist = false;
	/**
	 * isMyFile get true if the message is file, else false
	 */
	public static boolean isMyFile = false;
	/**
	 * isResId get true if there is restaurant Id, else false
	 */
	public static boolean isResId;
	/**
	 * allSupplierId get all the id of suppliers
	 */
	public static ArrayList<Integer> allSupplierId = new ArrayList<Integer>();
	/**
	 * dataUsersNorth get data of users from north
	 */
	public static ObservableList<User> dataUsersNorth = FXCollections.observableArrayList();
	/**
	 * dataUsersCentral get data of users from centeral
	 */
	public static ObservableList<User> dataUsersCentral = FXCollections.observableArrayList();
	/**
	 * dataUsersSouth get data of users from south
	 */
	public static ObservableList<User> dataUsersSouth = FXCollections.observableArrayList();
	/**
	 * detailsForCEOReportOrders get details for ceo report
	 */
	public static HashMap<String, Integer> detailsForCEOReportOrders;
	/**
	 * detailsForCEOReportRevenue get details for ceo report revenue
	 */
	public static HashMap<String, Float> detailsForCEOReportRevenue;
	/**
	 * detailsForCEOReportOrders2 get details for ceo report
	 */
	public static HashMap<String, Integer> detailsForCEOReportOrders2;
	/**
	 * detailsForCEOReportRevenue2 get details for ceo report revenue
	 */
	public static HashMap<String, Float> detailsForCEOReportRevenue2;
	/**
	 * findBusinessNameByhr; get business name by HR
	 */
	public static String findBusinessNameByhr;
	/**
	 * findhrByBusinessName get the HR by business name
	 */
	public static String findhrByBusinessName;
	/**
	 * businessNameByCustomer get the business name
	 */
	public static String businessNameByCustomer;
	/**
	 * w4cCodeBusiness get the w4c code business
	 */
	public static String w4cCodeBusiness;
	/**
	 * w4cCodePrivate get the w4c code private
	 */
	public static String w4cCodePrivate;
	/**
	 * dueHour get the due hour of order
	 */
	public static String dueHour;
	/**
	 * countInDish get the count of the dishes from this dish
	 */
	public static int countInDish;

	/**
	 * dueDate get the due date of order
	 */
	public static String dueDate;
	/**
	 * isPreOrder get true if the customer wand preorder
	 */
	public static boolean isPreOrder = false;
	/**
	 * dishInOrder get the dish in order
	 */
	public static String dishInOrder = "";

	/**
	 * resID get the restaurant ID
	 */
	public static int resID;

	/**
	 * businessName get the business name
	 */
	public static String businessName;
	/**
	 * businessRegisterd get true if exist business registerd
	 */
	public static boolean businessRegisterd;

	/**
	 * isFirstSendBill get true if the supplier get the bill,else false
	 */
	public static boolean isFirstSendBill;
	/**
	 * supplierApproveTime get the time that the supplier approve the order
	 */
	public static String supplierApproveTime;
	/**
	 * customerApproveDate get the date that the customer approve the order
	 */
	public static String customerApproveDate;
	/**
	 * customerApproveTime get the time that the customer approve the order
	 */
	public static String customerApproveTime;
	/**
	 * supplierMsgReadCount get count of messages of supplier not read
	 */
	public static int supplierMsgReadCount;
	/**
	 * customerMsgReadCount get count of messages of customer not read
	 */
	public static int customerMsgReadCount;
	/**
	 * ManagerMsgReadCount get count of messages of manager not read
	 */
	public static int ManagerMsgReadCount;
	/**
	 * HrMsgReadCount get count of messages of HR not read
	 */
	public static int HrMsgReadCount;
	/**
	 * CeoMsgReadCount get count of messages of CEO not read
	 */
	public static int CeoMsgReadCount;
	/**
	 * resId save the restaurant ID
	 */
	public static int resId;
	/**
	 * supplierId get the supplier ID
	 */
	public static int supplierId;
	/**
	 * MsgCountSupplier get count of message that sent to supplier
	 */
	public static int MsgCountSupplier;
	/**
	 * MsgCountCustomer get count of message that sent to customer
	 */
	public static int MsgCountCustomer;
	/**
	 * MsgCountManager get count of message that sent to manager
	 */
	public static int MsgCountManager;
	/**
	 * MsgCountHr get count of message that sent to HR
	 */
	public static int MsgCountHr;
	/**
	 * MsgCountCeo get count of message that sent to CEO
	 */
	public static int MsgCountCeo;
	/**
	 * lastOrderNumber get the number of last order
	 */
	public static String lastOrderNumber;
	/**
	 * userInfo get customer details
	 */
	public static String userInfo;
	/**
	 * forBillToSupplier get the data of bill that the supplier get
	 */
	public static ArrayList<String> forBillToSupplier = new ArrayList<String>();
	/**
	 * dio get the dishes in order
	 */
	public static ArrayList<DishInOrder> dio = new ArrayList<DishInOrder>();
	/**
	 * isDoneness get true if the dish have doneness option, else false
	 */
	public static boolean isDoneness = false;
	/**
	 * isSize get true if the dish size option, else false
	 */
	public static boolean isSize = false;
	/**
	 * isExistPrivate get true if exist private customer, else false
	 */
	public static boolean isExistPrivate = false;
	/**
	 * isExistBusiness get true if exist business customer, else false
	 */
	public static boolean isExistBusiness = false;
	/**
	 * monthlyBudget get the monthly budget
	 */
	public static float monthlyBudget;
	/**
	 * e1 get the data of employer
	 */
	public static Employer e1 = new Employer(null, (float) 0);
	/**
	 * re1 get the data of report
	 */
	public static Report re1 = new Report(null, null, (float) 0, (float) 0, (float) 0, (float) 0, (float) 0, 0, 0, 0, 0,

			0, 0, 0);
	/**
	 * b1 get the the data of business
	 */
	public static Business b1 = new Business(null, null, null, 0);
	/**
	 * bc1 get the data of business customer
	 */
	public static BusinessCustomer bc1 = new BusinessCustomer(0, null, null, null, null, null, null, 0);
	/**
	 * u1 get the data of the user that enter to system
	 */
	public static User u1 = new User(0, null, null, null, null, null, false, null, null, null, null);
	/**
	 * r1 get the data of restaurant
	 */
	public static Restaurant r1 = new Restaurant(0, null, null);
	/**
	 * d1 get the data of dish
	 */
	public static Dish d1 = new Dish(null, null, null, 0, false, false);
	/**
	 * ref1 get the data of refund
	 */
	public static Refund ref1 = new Refund(0, null, null, 0);
	/**
	 * o1 get the data of order
	 */
	public static Order o1 = new Order(null, 0, null, null, null, null, null, null, false, null, null, 0, null, false);
	/**
	 * dataUsers get the data of all the users
	 */
	public static ObservableList<User> dataUsers = FXCollections.observableArrayList();
	/**
	 * MsgSupplierList get the data of the message that the supplier get
	 */
	public static ObservableList<MsgToSupplier> MsgSupplierList = FXCollections.observableArrayList();
	/**
	 * MsgCustomerList get the data of the message that the customer get
	 */
	public static ObservableList<MsgToCustomer> MsgCustomerList = FXCollections.observableArrayList();
	/**
	 * MsgManagerList get the data of the message that the manager get
	 */
	public static ObservableList<MsgToManager> MsgManagerList = FXCollections.observableArrayList();
	/**
	 * MsgHrList get the data of the message that the HR get
	 */
	public static ObservableList<MsgToHR> MsgHrList = FXCollections.observableArrayList();
	/**
	 * MsgCeoList get the data of the message that the CEO get
	 */
	public static ObservableList<MsgToCEO> MsgCeoList = FXCollections.observableArrayList();
	/**
	 * cmbRestaurant get the names of restaurant to show in the combo box
	 */
	public static ArrayList<String> cmbRestaurant = new ArrayList<String>();
	/**
	 * cmbItem get the names of items to show in the combo box
	 */
	public static ArrayList<String> cmbItem = new ArrayList<String>();
	/**
	 * cmbDish get the names of dishes to show in the combo box
	 */
	public static ArrayList<String> cmbDish = new ArrayList<String>();
	/**
	 * cmbDoneness get the options of doneness of dish to show in the combo box
	 */
	public static ArrayList<String> cmbDoneness = new ArrayList<String>();
	/**
	 * cmbSize get the sizes of dish to show in the combo box
	 */
	public static ArrayList<String> cmbSize = new ArrayList<String>();
	/**
	 * dishes get the dishes in order
	 */
	public static ArrayList<String> dishes = new ArrayList<String>();
	/**
	 * flag get 0 if the message from client belongs to specific case
	 */
	public static int flag = 0;
	/**
	 * awaitResponse use for request response communication (client-server).
	 */
	public static boolean awaitResponse = false;

	// ************************ Constructors ************************

	/**
	 * Constructs an instance of the chat client.
	 *
	 * @param host     The server to connect to.
	 * @param port     The port number to connect on.
	 * @param clientUI The interface type variable.
	 */
	public ChatClient(String host, int port, ChatIF clientUI) throws IOException {
		super(host, port); // Call the superclass constructor
		this.clientUI = clientUI;
	}

	/**
	 * getObservableList: get the data of all the users
	 * 
	 * @return data of all the users
	 */
	public static ObservableList<User> getObservableList() {
		return dataUsers;
	}

	/**
	 * getMsgSupplierList: get all the messages the send to supplier
	 * 
	 * @return list of all the messages the send to supplier
	 */
	public static ObservableList<MsgToSupplier> getMsgSupplierList() {
		return MsgSupplierList;
	}

	/**
	 * getMsgCustomerList: get all the message that send to customer
	 * 
	 * @return list of all the messages the send to customer
	 */
	public static ObservableList<MsgToCustomer> getMsgCustomerList() {
		return MsgCustomerList;
	}

	/**
	 * getMsgManagerList: get all the message that send to Manager
	 * 
	 * @return list of all the messages the send to Manager
	 */
	public static ObservableList<MsgToManager> getMsgManagerList() {
		return MsgManagerList;
	}

	/**
	 * getMsgHrList: get all the message that send to HR
	 * 
	 * @return list of all the messages the send to HR
	 */
	public static ObservableList<MsgToHR> getMsgHrList() {
		return MsgHrList;
	}

	/**
	 * getMsgHrList: get all the message that send to CEO
	 * 
	 * @return list of all the messages the send to CEO
	 */
	public static ObservableList<MsgToCEO> getMsgCeoList() {
		return MsgCeoList;
	}

	/**
	 * addUsersByBranch: add user by his branch to datausers list
	 */
	public void addUsersByBranch() {
		for (User user : dataUsers) {
			if (user.getRole().contains("CEO")) {
				dataUsersNorth.add(user);
				dataUsersCentral.add(user);
				dataUsersSouth.add(user);
			} else {
				if (user.getRole().contains("North"))
					dataUsersNorth.add(user);
				else if (user.getRole().contains("Central"))
					dataUsersCentral.add(user);
				else if (user.getRole().contains("South"))
					dataUsersSouth.add(user);
			}
		}
	}

	// ************************ Instance methods ************************

	/**
	 * This method handles all data that comes in from the server.
	 *
	 * @param msg The message from the server.
	 */
	@SuppressWarnings("unchecked")
	public void handleMessageFromServer(Object msg) {
		System.out.println("--> handleMessageFromServer");
		awaitResponse = false;

		if (msg instanceof SerialBlob) {
			b = (SerialBlob) msg;
		}

		if (msg instanceof ArrayList<?>) {
			List<String> arrayOfUsers = (ArrayList<String>) msg;
			switch (arrayOfUsers.get(0)) {

			case "get user details":
				dataUsers.clear();
				dataUsersNorth.clear();
				dataUsersCentral.clear();
				dataUsersSouth.clear();
				int index = 1;
				while (index < arrayOfUsers.size()) {
					String SaveUser = arrayOfUsers.get(index);
					String[] cellsdetails = SaveUser.split("\\t");
					dataUsers.add(new User(Integer.parseInt(cellsdetails[0]), cellsdetails[1], cellsdetails[2],
							cellsdetails[3], cellsdetails[4], cellsdetails[5], Boolean.parseBoolean(cellsdetails[6]),
							cellsdetails[7], (cellsdetails[8]), cellsdetails[9], cellsdetails[10]));
					index++;
				}
				addUsersByBranch();
				break;

			case "restaurantName":
				r1.setRestaurantName(arrayOfUsers.get(1));
				break;
			case "employerName":
				e1.setBusinessName(arrayOfUsers.get(1));
				break;

			}
		} else if (msg instanceof HashMap<?, ?>) {

			if (((HashMap<String, Integer>) msg).containsKey("ordersHashMap"))

			{// if orders
				if (((HashMap<String, Integer>) msg).containsKey("Report1"))
					detailsForCEOReportOrders = (HashMap<String, Integer>) msg; // orders Hashmap1
				else
					detailsForCEOReportOrders2 = (HashMap<String, Integer>) msg; // orders Hashmap2
			}

			else {
				if (((HashMap<String, Float>) msg).containsKey("Report1"))
					detailsForCEOReportRevenue = (HashMap<String, Float>) msg; // revenue HashMap
				else
					detailsForCEOReportRevenue2 = (HashMap<String, Float>) msg; // revenue HashMap
			}
		} else {
			int i = 1;
			String record;
			record = msg.toString();
			String[] result = record.split("\\t");

			switch (result[0]) {
			case "get:":
				flag = 0;
				break;
			case "updateOrder:":
				flag = 0;
				break;
			case "updateShipment:":
				flag = 0;
				break;
			case "updateDishInOrder:":
				flag = 0;
				break;
			case "cmbRestaurant:":
				flag = 0;
				while (result[i] != null)
					cmbRestaurant.add(result[i++]);
				break;
			case "cmbItem:":
				flag = 0;
				while (result[i] != null)
					cmbItem.add(result[i++]);
				break;
			case "cmbDish:":
				flag = 0;
				while (result[i] != null)
					cmbDish.add(result[i++]);
				break;
			case "cmbDoneness:":
				flag = 0;
				while (result[i] != null)
					cmbDoneness.add(result[i++]);
				break;
			case "donenessValid:":
				flag = 0;
				if (result[1].equals("true"))
					isDoneness = true;
				else
					isDoneness = false;
				break;
			case "cmbSize:":
				flag = 0;
				while (result[i] != null)
					cmbSize.add(result[i++]);
				break;
			case "sizeValid:":
				flag = 0;
				if (result[1].equals("true"))
					isSize = true;
				else
					isSize = false;
				break;
			case "login":
				u1.setIdNumber(Integer.parseInt(result[1]));
				u1.setFirstName(result[2]);
				u1.setLastName(result[3]);
				u1.setEmail(result[4]);
				u1.setPhoneNumber(result[5]);
				u1.setRole(result[6]);
				u1.setIsLoggedIn(true);
				u1.setUserName(result[8]);
				u1.setPassword(result[9]);
				u1.setType(result[10]);
				u1.setStatus(result[11]);
				flag = 0;
				break;

			case "logout":
				u1.setIdNumber(0);
				u1.setFirstName(null);
				u1.setLastName(null);
				u1.setEmail(null);
				u1.setPhoneNumber(null);
				u1.setRole(null);
				u1.setIsLoggedIn(false);
				u1.setUserName(null);
				u1.setPassword(null);
				u1.setType(null);
				u1.setStatus(null);
				flag = 0;
				break;

			case "getDishes:":
				for (String dish : result) {
					if (!dish.equals("getDishes:"))
						dishes.add(dish);
				}
				flag = 0;
				break;

			case "getDishDetails:":
				d1.setName(result[1]);
				d1.setType(result[2]);
				d1.setPrice(Float.parseFloat(result[3]));
				d1.setDoneness(Boolean.parseBoolean(result[4]));
				d1.setSize(Boolean.parseBoolean(result[5]));
				d1.setRestaurant(result[6]);
				flag = 0;
				break;

			case "updateBudget:":
				e1.setMonthlyBudget(Float.parseFloat(result[1]));
				flag = 0;
				break;

			case "updateResID:":
				r1.setRestaurantID(Integer.parseInt(result[1]));
				flag = 0;
				break;

			case "updateReportDetails:":
				re1.setTotalRevenue(Float.parseFloat(result[1]));
				re1.setRevMainDishs(Float.parseFloat(result[2]));
				re1.setRevFirstDishs(Float.parseFloat(result[3]));
				re1.setRevDrinks(Float.parseFloat(result[4]));
				re1.setRevDesserts(Float.parseFloat(result[5]));
				re1.setNumOfMainDishs(Integer.parseInt(result[6]));
				re1.setNumOfFirstDishs(Integer.parseInt(result[7]));
				re1.setNumOfDrinks(Integer.parseInt(result[8]));
				re1.setNumOfDesserts(Integer.parseInt(result[9]));
				re1.setTotalNumOfOrders(Integer.parseInt(result[10]));
				re1.setTotalNumOfDelays(Integer.parseInt(result[11]));
				flag = 0;
				break;

			case "getCustomerDetails:":
				flag = 0;
				userInfo = result[1] + "\t" + result[2] + "\t" + result[3];
				break;
			case "Connected:":
				flag = 0;
				break;
			case "Disconnected:":
				flag = 0;
				break;
			case "idNumberPcustomerExist:":
				if (result[1].equals("true"))
					isExistPrivate = true;
				flag = 0;
				break;
			case "idNumberBcustomerExist:":
				if (result[1].equals("true"))
					isExistBusiness = true;
				flag = 0;
				break;
			case "getMonthlyBudget:":
				monthlyBudget = Float.parseFloat(result[1]);
				flag = 0;
				break;
			case "updateMonthlyBudget:":
				flag = 0;
				break;
			case "getLastOrderNumber:":
				lastOrderNumber = result[1];
				flag = 0;
				break;
			case "msgToSupplier:":
				flag = 0;
				break;
			case "msgToCustomer:":
				flag = 0;
				break;
			case "getRestaurantId:":
				resId = Integer.parseInt(result[1]);
				flag = 0;
				break;
			case "getSupplierId:":
				supplierId = Integer.parseInt(result[1]);
				flag = 0;
				break;
			case "getMsgToSupplier:":
				MsgSupplierList.clear();
				MsgCountSupplier = result.length / 8;
				for (int j = 0; j < MsgCountSupplier; j++) {
					MsgSupplierList.add(new MsgToSupplier(Integer.parseInt(result[i++]), Integer.parseInt(result[i++]),
							Integer.parseInt(result[i++]), result[i++], result[i++], result[i++], result[i++],
							Boolean.parseBoolean(result[i++])));
				}
				flag = 0;
				break;
			case "getMsgToCustomer:":
				MsgCustomerList.clear();
				MsgCountCustomer = result.length / 8;
				for (int j = 0; j < MsgCountCustomer; j++) {
					MsgCustomerList.add(new MsgToCustomer(Integer.parseInt(result[i++]), Integer.parseInt(result[i++]),
							Integer.parseInt(result[i++]), result[i++], result[i++], result[i++], result[i++],
							Boolean.parseBoolean(result[i++])));
				}
				flag = 0;
				break;
			case "getMsgToManager:":
				MsgManagerList.clear();
				MsgCountManager = result.length / 7;
				for (int j = 0; j < MsgCountManager; j++) {
					MsgManagerList.add(new MsgToManager(Integer.parseInt(result[i++]), Integer.parseInt(result[i++]),
							result[i++], result[i++], result[i++], result[i++], Boolean.parseBoolean(result[i++])));
				}
				flag = 0;
				break;
			case "getMsgToHr:":
				MsgHrList.clear();
				MsgCountHr = result.length / 7;
				for (int j = 0; j < MsgCountHr; j++) {
					MsgHrList.add(new MsgToHR(Integer.parseInt(result[i++]), Integer.parseInt(result[i++]), result[i++],
							result[i++], result[i++], result[i++], Boolean.parseBoolean(result[i++])));
				}
				flag = 0;
				break;
			case "updateMsgSupplier:":
				flag = 0;
				break;
			case "updateMsgCustomer:":
				flag = 0;
				break;
			case "removeMsgSupplier":
				flag = 0;
				break;
			case "removeMsgCustomer":
				flag = 0;
				break;
			case "msgSupplierRead:":
				flag = 0;
				break;
			case "msgCustomerRead:":
				flag = 0;
				break;
			case "getNumberSupplierMsgNotRead:":
				supplierMsgReadCount = Integer.parseInt(result[1]);
				flag = 0;
				break;
			case "getNumberCustomerMsgNotRead:":
				customerMsgReadCount = Integer.parseInt(result[1]);
				flag = 0;
				break;
			case "getNumberManagerMsgNotRead:":
				ManagerMsgReadCount = Integer.parseInt(result[1]);
				flag = 0;
				break;
			case "getNumberHrMsgNotRead:":
				HrMsgReadCount = Integer.parseInt(result[1]);
				flag = 0;
				break;
			case "getNumberCeoMsgNotRead:":
				CeoMsgReadCount = Integer.parseInt(result[1]);
				flag = 0;
				break;
			case "getIsPreOrder:":
				isPreOrder = Boolean.parseBoolean(result[1]);
				flag = 0;
				break;
			case "getCustomerApproveHour:":
				customerApproveTime = result[1];
				flag = 0;
				break;
			case "getCustomerApproveDate:":
				customerApproveDate = result[1];
				flag = 0;
				break;
			case "getSupplierApproveHour:":
				supplierApproveTime = result[1];
				flag = 0;
				break;
			case "getBranchAndRestaurantAndFinalPriceAndUserId:":
				o1.setBranch(result[1]);
				o1.setRestaurant(result[2]);
				o1.setFinalPrice(Float.parseFloat(result[3]));
				o1.setUserID(Integer.parseInt(result[4]));
				flag = 0;
				break;
			case "getDueHour:":
				dueHour = result[1];
				flag = 0;
				break;
			case "getDueDate:":
				dueDate = result[1];
				flag = 0;
				break;
			case "getPriceRefundIfExist:":
				ref1.setPriceRefund(Float.parseFloat(result[1]));
				flag = 0;
				break;
			case "countInDish:":
				countInDish = Integer.parseInt(result[1]);
				flag = 0;
				break;
			case "presentDishes:":
				for (int j = 0; j < countInDish; j++)
					dishInOrder += "Dish " + (j + 1) + ":   " + result[i++] + "   " + result[i++] + "   " + result[i++]
							+ "   " + result[i++] + "   " + result[i++] + "\n";
				flag = 0;
				break;
			case "FindResID:":
				resID = Integer.parseInt(result[1]);
				flag = 0;
				break;
			case "FindBusinessName:":
				businessName = result[1];
				flag = 0;
				break;
			case "CheckIfBusinessReg:":
				businessRegisterd = Boolean.parseBoolean(result[1]);
				flag = 0;
				break;
			case "getW4CcodePrivate:":
				w4cCodePrivate = result[1];
				flag = 0;
				break;
			case "getBusinessName:":
				businessNameByCustomer = result[1];
				flag = 0;
				break;
			case "getW4CcodeBusiness:":
				w4cCodeBusiness = result[1];
				flag = 0;
				break;
			case "getTypeOfOrder:":
				o1.setTypeOfOrder(result[1]);
				flag = 0;
				break;
			case "findBusinessNameByHr:":
				findBusinessNameByhr = result[1];
				flag = 0;
				break;
			case "takeBusData:":
				b1.setBusinessName(result[1]);
				b1.setBusinessAddress(result[2]);
				b1.setBusinessPhone(result[3]);
				b1.setMonthlyBudget(Float.parseFloat(result[4]));
				flag = 0;
				break;
			case "findHrByBusinessName:":
				findhrByBusinessName = result[1];
				flag = 0;
				break;
			case "getOrderDetailsByMonth:":
				for (i = 1; i < result.length; i++)
					forBillToSupplier.add(result[i]);
				flag = 0;
				break;
			case "getFromDishInOrderMonthDishes:":
				int k = 1;
				for (int j = 0; j < result.length / 6; j++)
					dio.add(new DishInOrder(result[k++], result[k++], result[k++], result[k++],
							Float.parseFloat(result[k++]), null, null, null, Float.parseFloat(result[k++])));
				flag = 0;
				break;
			case "checkIfAlreadyGotMonthlyBill:":
				isFirstSendBill = Boolean.parseBoolean(result[1]);
				flag = 0;
				break;
			case "getMsgToCeo:":
				MsgCeoList.clear();
				MsgCountCeo = result.length / 7;
				for (int j = 0; j < MsgCountCeo; j++) {
					MsgCeoList.add(new MsgToCEO(Integer.parseInt(result[i++]), Integer.parseInt(result[i++]),
							result[i++], result[i++], result[i++], result[i++], Boolean.parseBoolean(result[i++])));
				}
				flag = 0;
				break;
			case "getAllSupplierId:":
				for (int j = 1; j < result.length; j++)
					allSupplierId.add(Integer.parseInt(result[j]));
				flag = 0;
				break;
			case "getRestaurantIdByBranch:":
				isResId = Boolean.parseBoolean(result[1]);
			case "CheckIfFileExist:":
				if (result[1].equals("true"))
					isFileExist = true;
				else
					isFileExist = false;
				flag = 0;
				break;
			case "msgSupplierReadBill:":
				flag = 0;
				break;
			case "-1":
				flag = 1;
				break;
			default:
				flag = 0;
			}
		}
	}

	/**
	 * This method handles all data coming from the UI
	 *
	 * @param message The message from the UI.
	 */
	public void handleMessageFromClientUI(String message) {
		try {
			openConnection();// in order to send more than one message
			awaitResponse = true;
			sendToServer(message);
			if (isMyFile) {
				MyFile msg = new MyFile(message);
				String LocalfilePath = message;

				try {
					File newFile = new File(LocalfilePath);

					byte[] mybytearray = new byte[(int) newFile.length()];
					FileInputStream fis = new FileInputStream(newFile);
					BufferedInputStream bis = new BufferedInputStream(fis);

					msg.initArray(mybytearray.length);
					msg.setSize(mybytearray.length);

					bis.read(msg.getMybytearray(), 0, mybytearray.length);
					sendToServer(msg);
					fis.close();
					bis.close();
				} catch (Exception e) {
					System.out.println("Error send (Files)msg) to Server");
				}
				isMyFile = false;
			}

			// wait for response
			while (awaitResponse) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			clientUI.display("Could not send message to server: Terminating client." + e);
			EnterIpController.isConnected = false;
		}
	}

	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
		}
		System.exit(0);
	}
}
//End of ChatClient class