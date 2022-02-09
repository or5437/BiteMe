package controllers;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.sql.rowset.serial.SerialBlob;

import com.itextpdf.text.DocumentException;

import server.EchoServer;

/**
 * Description of DataBaseController The purpose of this class is to centralize
 * all the queries that are in the project. also, to handle inquiries from Echo
 * server class to the database (MySQL).
 * 
 * @author Michael Ravich, Raz Avraham, Or hilo, Amit Kempner, Tal Derai
 * @version Version 1.0 Build 100 December 27, 2021
 */
public class DataBaseController {
	/**
	 * Description of stmt - The object used for executing a static SQL statement
	 * and returning the results it produces. Description of ps - An object that
	 * represents a precompiled SQL statement. Description of INSTANCE - Holding a
	 * class instance (implementaion of a singleton pattern)
	 */
	private static Statement stmt;
	private static PreparedStatement ps;

	private final static DataBaseController INSTANCE = new DataBaseController();

	private DataBaseController() // private constructor -(implementaion of a singleton pattern)
	{
	}

	public static DataBaseController getInstance() { // (implementaion of a singleton pattern)
		return INSTANCE;
	}

	/**
	 * Description of InsertRecordInOrderTbl() The purpose of the method is to enter
	 * the order details into the database after it has been executed by the
	 * customer
	 * 
	 * @param con                    - connection
	 * @param UserType               - which user did the order. business\private
	 * @param UserID                 - users id
	 * @param PhoneNumber            - users phone number
	 * @param OrderConfirmDate       - when he confirmed the order - DATE
	 * @param OrderConfirmHour       - when he confirmed the order - HOUR
	 * @param OrderDueDate           - Requested arrival date
	 * @param OrderDueHour-Requested arrival hour
	 * @param OrderAddress           - users order address
	 * @param isPreOrder             - if its a pre order (2 hours ahead)
	 * @param Branch                 - North\South\Central
	 * @param Restaurant             - restaurants name
	 * @param finalPrice             - final orders price
	 * @param TypeOfOrder            - Take Away\Shipment
	 * @param isConfirm              - if the order confirmed
	 */
	public static void InsertRecordInOrderTbl(Connection con, String UserType, String UserID, String PhoneNumber,
			String OrderConfirmDate, String OrderConfirmHour, String OrderDueDate, String OrderDueHour,
			String OrderAddress, String isPreOrder, String Branch, String Restaurant, String finalPrice,
			String TypeOfOrder, String isConfirm) {
		try {
			ps = con.prepareStatement(
					"insert into orders (userType, userIdNumber, phoneNumber, orderConfirmDate, orderConfirmHour, orderDueDate, orderDueHour, orderAddress, isPreOrder, branch, restaurant, finalPrice, typeOfOrder, isConfirm) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
			ps.setString(1, UserType);
			ps.setInt(2, Integer.parseInt(UserID));
			ps.setString(3, PhoneNumber);
			ps.setString(4, OrderConfirmDate);
			ps.setString(5, OrderConfirmHour);
			ps.setString(6, OrderDueDate);
			ps.setString(7, OrderDueHour);
			ps.setString(8, OrderAddress);
			ps.setBoolean(9, Boolean.parseBoolean(isPreOrder));
			ps.setString(10, Branch);
			ps.setString(11, Restaurant);
			ps.setFloat(12, Float.parseFloat(finalPrice));
			ps.setString(13, TypeOfOrder);
			ps.setBoolean(14, Boolean.parseBoolean(isConfirm));
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Description of RetrunLastRecordFromOrdersTbl(): The purpose of the method is
	 * to extract the record of the last order for the shipping stage
	 * 
	 * @param con - connection
	 * @return String - order number
	 */
	public static String RetrunLastRecordFromOrdersTbl(Connection con) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT orderNumber FROM `orders` WHERE orderNumber=(SELECT MAX(orderNumber) FROM `orders`);");
			while (rs.next()) {
				str.append(rs.getString(1));
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Description of InsertRecordInShipmentTbl(): The purpose of the method is to
	 * enter the shipping details into the shipments table
	 * 
	 * @param con                 - connection
	 * @param shipmentOrderNumber - order number for the shipment
	 * @param shipmentType        - Shared\robot etc
	 * @param shipmentPrice       - final price of the shipment
	 * @param reciverAddress      - reciver of the order address
	 * @param reciverName         - reciver full name
	 * @param reciverPhone        - reciver phone number
	 * @param isRobot             - mark if its a robot shipment or no
	 */
	public static void InsertRecordInShipmentTbl(Connection con, String shipmentOrderNumber, String shipmentType,
			String shipmentPrice, String reciverAddress, String reciverName, String reciverPhone, String isRobot) {
		try {
			ps = con.prepareStatement(
					"INSERT shipments SET orders_OrderNumber=?,shipmentType=?,shipmentPrice=?,receiverAddress=?,receiverName=?,receiverPhone=?,isRobot=?");
			ps.setInt(1, Integer.parseInt(shipmentOrderNumber));
			ps.setString(2, shipmentType);
			ps.setFloat(3, Float.parseFloat(shipmentPrice));
			ps.setString(4, reciverAddress);
			ps.setString(5, reciverName);
			ps.setString(6, reciverPhone);
			ps.setBoolean(7, Boolean.parseBoolean(isRobot));
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Description of RetrunAllRestaurant(): The purpose of the method is to get the
	 * restaurants details (id,name)
	 * 
	 * @param con-   connection
	 * @param branch - North\South\Central
	 * @return String - contains all restaurants (id,name)
	 */
	public static String RetrunAllRestaurant(Connection con, String branch) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM restaurants WHERE branch = '" + branch + "';");
			while (rs.next()) {
				str.append(rs.getInt(1) + "\t" + rs.getString(2) + "\t");
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Description of getDishByRestaurantAndByItem(): The purpose of the method is
	 * to extract the name of the dish and its price, when the restaurant id and
	 * item type are given
	 * 
	 * @param con          - connection
	 * @param restaurantId - restaurants id
	 * @param itemType     - item type Main dish\First Dish\Dessert..
	 * @return String - which contains dish name and dish price of all dishes
	 */
	public static String getDishByRestaurantAndByItem(Connection con, String restaurantId, String itemType) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT dishName,dishPrice FROM dishes WHERE restaurants_restaurantId='"
					+ Integer.parseInt(restaurantId) + "' AND itemType='" + itemType + "';");
			while (rs.next()) {
				str.append(rs.getString(1) + ", " + rs.getFloat(2) + " NIS" + "\t");
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Description of getValidDonenessByDish(): The purpose of the method is to
	 * return the level of readiness of the dish from the dishes table
	 * 
	 * @param con      - connection
	 * @param dishName - name of the dish
	 * @return string - which contains dish doneness ( done\not yet)
	 */
	public static String getValidDonenessByDish(Connection con, String dishName) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT isDoneness FROM dishes WHERE dishName='" + dishName + "';");
			while (rs.next()) {
				str.append(rs.getBoolean(1) + "\t");
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Description of getValidSizeByDish(): The purpose of the method is to get dish
	 * size when name is given
	 * 
	 * @param con      - connection
	 * @param dishName - name of the dish
	 * @return String - dish valid size
	 */
	public static String getValidSizeByDish(Connection con, String dishName) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT isSize FROM dishes WHERE dishName='" + dishName + "';");
			while (rs.next()) {
				str.append(rs.getBoolean(1) + "\t");
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Description of ReturnPermissionIfUserExist(): The purpose of the method is to
	 * return User's permission if its exists
	 * 
	 * @param con-     connection
	 * @param UserName - get the relevant user name.
	 * @param password - get the relevant password.
	 * @return String - contains user's permission
	 */
	public static String ReturnPermissionIfUserExist(Connection con, String UserName, String password) {
		try {
			String str;
			String temp = "SELECT * FROM users WHERE userName=? AND password=?";
			ps = con.prepareStatement(temp);
			ps.setString(1, UserName);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				str = (rs.getInt(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3) + "\t" + rs.getString(4) + "\t"
						+ rs.getString(5) + "\t" + rs.getString(6) + "\t" + rs.getBoolean(7) + "\t" + rs.getString(8)
						+ "\t" + rs.getString(9) + "\t" + rs.getString(10) + "\t" + rs.getString(11));
				rs.close();
				ps.close();
				return str;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Description of UpdateLoggedUsersTBL(): The purpose of the method is to update
	 * Logged in status of the user after he loggs in\out
	 * 
	 * @param con-     connection
	 * @param Username get the userName.
	 * @param ifLogged - true\false
	 */
	public static void UpdateLoggedUsersTBL(Connection con, String Username, Boolean ifLogged) {
		try {
			String temp = "UPDATE users SET isLoggedIn=? WHERE userName='" + Username + "';";
			ps = con.prepareStatement(temp);
			ps.setBoolean(1, ifLogged);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Description of getUserDetails(): The purpose of the method is to get all
	 * users details from the users table
	 * 
	 * @param con - get the connection to DB.
	 * @return List<String> - with all users table fields
	 */
	public static List<String> getUserDetails(Connection con) {
		List<String> retList = new ArrayList<>();
		try {
			String str;
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM users;");
			while (rs.next()) {
				str = (rs.getInt(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3) + "\t" + rs.getString(4) + "\t"
						+ rs.getString(5) + "\t" + rs.getString(6) + "\t" + rs.getBoolean(7) + "\t" + rs.getString(8)
						+ "\t" + rs.getString(9) + "\t" + rs.getString(10) + "\t" + rs.getString(11));
				retList.add(str);
			}
			rs.close();
			return retList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Description of UpdateDetailsUserTBL(): The purpose of the method is to update
	 * user's status in the system (Frozen\Locked\Active)
	 * 
	 * @param con      - connection
	 * @param Username - get the relevant user name.
	 * @param Status   - (Frozen\Locked\Active)
	 */
	public static void UpdateDetailsUserTBL(Connection con, String Username, String Status) {
		try {
			String temp = "UPDATE users SET status=? WHERE userName='" + Username + "';";
			ps = con.prepareStatement(temp);
			ps.setString(1, Status);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Description of ReturnRestaurantName(): The purpose of the method is to return
	 * restaurants name by given Branch
	 * 
	 * @param con    - connection
	 * @param Branch - get the relevant branch.
	 * @return String - contains all restaurants name of given branch
	 */
	public static String ReturnRestaurantName(Connection con, String Branch) {
		try {
			StringBuilder str = new StringBuilder();
			String temp = "SELECT restaurantName FROM restaurants WHERE branch='" + Branch + "';";
			ps = con.prepareStatement(temp);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				str.append(rs.getString(1) + "\t");
			rs.close();
			ps.close();
			return str.toString();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Description of randomCode(): The purpose of the method is to get random code
	 * for the QR code simulation
	 * 
	 * @param len - length of the code
	 * @return String - new code for QR code simulation
	 */
	public static String randomCode(int len) {
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvwxyz" + "0123456789" + "!@#$%^&*";

		StringBuilder b = new StringBuilder();

		for (int i = 0; i < len; i++) {
			int randIdx = new Random().nextInt(alphabet.length());
			char randChar = alphabet.charAt(randIdx);
			b.append(randChar);
		}

		return b.toString();
	}

	/**
	 * Description of AddNewBusiness(): The purpose of the method is to insert into
	 * registered_business table a new business
	 * 
	 * @param con           - connection
	 * @param businessName  - name of the business
	 * @param businessAdd   - address of the business
	 * @param businessPhone - phone of the business
	 * @param monthlyBudget - monthly budget for all business workers
	 * @return Boolean - true if Succeeded - otherwise: false
	 */
	public static boolean AddNewBusiness(Connection con, String businessName, String businessAdd, String businessPhone,
			String monthlyBudget) {
		try {
			ps = con.prepareStatement(
					"insert into registered_business (businessName, businessAddress, businessPhone, monthlyBudget) values(?,?,?,?);");
			ps.setString(1, businessName);
			ps.setString(2, businessAdd);
			ps.setString(3, businessPhone);
			ps.setString(4, monthlyBudget);
			ps.executeUpdate();
			ps.close();
		} catch (SQLIntegrityConstraintViolationException e) {
			e.printStackTrace();
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			ps = con.prepareStatement("INSERT IGNORE employer_w4c_card SET businessName=?,employerCodeNumber=?;");
			ps.setString(1, businessName);
			ps.setString(2, randomCode(4));
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * Description of AddNewWaitingBusiness(): The purpose of the method is to add a
	 * new Business to a Business waiting table, which describes that he is waiting
	 * for approval.
	 * 
	 * @param con            - connection
	 * @param businessName   - name of the business
	 * @param businessAdd    - address of the business
	 * @param businessPhone  - phone of the business
	 * @param monthlyBudget- monthly budget for all business workers
	 * @return boolean - which describes if the method succeeded or not
	 */
	public static boolean AddNewWaitingBusiness(Connection con, String businessName, String businessAdd,
			String businessPhone, String monthlyBudget) {
		try {
			ps = con.prepareStatement(
					"insert into waiting_business (businessName, businessAddress, businessPhone, monthlyBudget) values(?,?,?,?);");
			ps.setString(1, businessName);
			ps.setString(2, businessAdd);
			ps.setString(3, businessPhone);
			ps.setString(4, monthlyBudget);
			ps.executeUpdate();
			ps.close();
		} catch (SQLIntegrityConstraintViolationException e) {
			e.printStackTrace();
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * Description of AddNewDish(): The purpose of the method is to add a new dish
	 * to the dishes table when the supplier wants to update the menu.
	 * 
	 * @param con        - connection
	 * @param dishName   - the name of the dish
	 * @param itemType   - the type of the dish (Main\Dessert\First .. )
	 * @param dishPrice  - the price of the dish
	 * @param isDoneness - Doneness of the dish
	 * @param isSize     - size of the dish
	 * @param resID      - restaurants id
	 * @return Boolean - true if Succeeded - otherwise: false
	 */
	public static boolean AddNewDish(Connection con, String dishName, String itemType, float dishPrice,
			boolean isDoneness, boolean isSize, int resID) {
		try {
			ps = con.prepareStatement(
					"insert into dishes (dishName, itemType, dishPrice, isDoneness, isSize, restaurants_restaurantId) values(?,?,?,?,?,?);");
			ps.setString(1, dishName);
			ps.setString(2, itemType);
			ps.setFloat(3, dishPrice);
			ps.setBoolean(4, isDoneness);
			ps.setBoolean(5, isSize);
			ps.setInt(6, resID);
			ps.executeUpdate();
			ps.close();
		} catch (SQLIntegrityConstraintViolationException e) {
			e.printStackTrace();
			return false;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * Description of getDishDetailsByNameAndRes(): The purpose of the method is to
	 * get the dish details from the db, by given dish name and restaurants ID.
	 * 
	 * @param con       - connection
	 * @param dish_name - the name of the dish
	 * @param resID     - restaurants ID
	 * @return String - which includes all relevant dish details
	 */
	public static String getDishDetailsByNameAndRes(Connection con, String dish_name, int resID) {
		try {
			String str;
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM dishes WHERE restaurants_restaurantId='" + resID
					+ "' AND dishName='" + dish_name + "';");
			while (rs.next()) {
				str = (rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getFloat(3) + "\t" + rs.getBoolean(4) + "\t"
						+ rs.getBoolean(5) + "\t" + rs.getInt(6));
				return str;
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Description of UpdateDishDetails(): The purpose of the method is to update a
	 * dish in the dishes table when the supplier wants to update the menu.
	 * 
	 * @param con-       connection
	 * @param DishName   - the name of the dish
	 * @param resID      - restaurants ID
	 * @param DishPrice  - the price of the dish
	 * @param isSize     - the size of the dish
	 * @param isDoneness - Doneness of the dish
	 */
	public static void UpdateDishDetails(Connection con, String DishName, int resID, float DishPrice, boolean isSize,
			boolean isDoneness) {
		try {
			ps = con.prepareStatement("UPDATE dishes SET dishPrice=?, isSize=?, isDoneness=? WHERE DishName='"
					+ DishName + "'AND restaurants_restaurantId =  '" + resID + "';");
			ps.setFloat(1, DishPrice);
			ps.setBoolean(2, isSize);
			ps.setBoolean(3, isDoneness);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * delete specific dish from dishes table
	 * 
	 * @param con      get connection to DB
	 * @param DishName get the dish name
	 * @param resID    get restaurant ID the dish belong to
	 */
	public static void DeleteDish(Connection con, String DishName, int resID) {
		try {
			ps = con.prepareStatement("DELETE FROM dishes WHERE DishName='" + DishName
					+ "'AND restaurants_restaurantId =  '" + resID + "';");
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * get dishes names by restaurant ID and by dish type
	 * 
	 * @param con      get connection to DB
	 * @param resID    get res ID
	 * @param itemType get the type (first dish, main dish, drink...)
	 * @return the string of the dishes or null id doesn't exist
	 */
	public static String getDishByRestaurantAndByItem(Connection con, int resID, String itemType) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT dishName,dishPrice FROM dishes WHERE restaurants_restaurantId='"
					+ resID + "' AND itemType='" + itemType + "';");
			while (rs.next()) {
				str.append(rs.getString(1) + "\t");
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * get customer details by its user name
	 * 
	 * @param con      get connection to DB
	 * @param userName get relevant customer user name
	 * @return string of the customer details
	 */
	public static String getCustomerDetails(Connection con, String userName) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT type,userIdNumber,phoneNumber FROM users WHERE userName='" + userName + "';");
			while (rs.next()) {
				str.append(rs.getString(1) + "\t" + rs.getInt(2) + "\t" + rs.getString(3) + "\t");
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * insert order record to dish_in_order table in DB.
	 * 
	 * @param con               get connection to DB
	 * @param OrderNumebr       get relevant order number
	 * @param Branch            get order branch
	 * @param Restaurant        get order res
	 * @param Item              get order item
	 * @param Dish              get order dish
	 * @param Price             get order price
	 * @param Doneness          get order doneness
	 * @param Size              get order size
	 * @param additionalRequest get order additional request
	 */
	public static void InsertRecordToDishInOrderTbl(Connection con, String OrderNumebr, String Branch,
			String Restaurant, String Item, String Dish, String Price, String Doneness, String Size,
			String additionalRequest) {
		try {
			ps = con.prepareStatement(
					"insert into dish_in_order (orders_OrderNumber, branch, restaurant, item, dish, price, doneness, size, additionalRequest) values(?,?,?,?,?,?,?,?,?);");
			ps.setInt(1, Integer.parseInt(OrderNumebr));
			ps.setString(2, Branch);
			ps.setString(3, Restaurant);
			ps.setString(4, Item);
			ps.setString(5, Dish);
			ps.setFloat(6, Float.parseFloat(Price));
			ps.setString(7, Doneness);
			ps.setString(8, Size);
			ps.setString(9, additionalRequest);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * check if user ID exist in private_customer table in DB.
	 * 
	 * @param con      get connection to DB
	 * @param idNumber get private customer id number
	 * @return user id number if exist else return null
	 */
	public static String checkIdNumberExistInPrivateCustomerTable(Connection con, String idNumber) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT userIdNumber FROM private_customers WHERE userIdNumber='"
					+ Integer.parseInt(idNumber) + "';");
			while (rs.next()) {
				str.append(rs.getInt(1) + "\t");
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * check if user ID number exist in business_customer table in DB.
	 * 
	 * @param con      get connection to DB
	 * @param idNumber get business customer id number
	 * @return user if number if exist else return null
	 */
	public static String checkIdNumberExistInBusinessCustomerTable(Connection con, String idNumber) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT userIdNumber FROM business_customers WHERE userIdNumber='"
					+ Integer.parseInt(idNumber) + "';");
			while (rs.next()) {
				str.append(rs.getInt(1) + "\t");
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * get the monthly budget of business customer from business customer table by
	 * its id number
	 * 
	 * @param con      get connection to DB.
	 * @param idNumber get the business customer user ID.
	 * @return string of budget if exist else return null
	 */
	public static String getMonthlyBudgetByIdNumberFromBusinessCustomerTable(Connection con, String idNumber) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT currentBudget FROM business_customers WHERE userIdNumber='"
					+ Integer.parseInt(idNumber) + "';");
			while (rs.next()) {
				str.append(rs.getInt(1) + "\t");
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * set monthly monthly budget by id number in business customer table
	 * 
	 * @param con           get connection to DB
	 * @param idNumber      get business customer id number
	 * @param monthlyBudget get monthly budget to set
	 */
	public static void updateMonthlyBudgetByIdNumberFromBusinessCustomerTable(Connection con, String idNumber,
			String monthlyBudget) {
		try {
			String query = "UPDATE business_customers SET currentBudget=? WHERE userIdNumber='"
					+ Integer.parseInt(idNumber) + "';";
			ps = con.prepareStatement(query);
			ps.setFloat(1, Float.parseFloat(monthlyBudget));
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Insert a new message to supplier mail box table
	 * 
	 * @param con          get connection to DB
	 * @param orderId      get the order ID
	 * @param fromUserId   get the user ID that sent the msg
	 * @param toSupplierId get the supplierID that the msg is for him
	 * @param date         get date of mail
	 * @param hour         get hour of mail
	 * @param message      get the text of msg
	 * @param status       get first status of msg
	 * @param isRead       get string the says if the msg is read
	 */
	public static void InsertRecordMsgToSupplierTable(Connection con, String orderId, String fromUserId,
			String toSupplierId, String date, String hour, String message, String status, String isRead) {
		try {
			ps = con.prepareStatement(
					"insert into msg_to_supplier (orderId, fromUserId, toSupplierId, date, hour, message, status, isRead) values(?,?,?,?,?,?,?,?);");
			ps.setInt(1, Integer.parseInt(orderId));
			ps.setInt(2, Integer.parseInt(fromUserId));
			ps.setInt(3, Integer.parseInt(toSupplierId));
			ps.setString(4, date);
			ps.setString(5, hour);
			ps.setString(6, message);
			ps.setString(7, status);
			ps.setBoolean(8, Boolean.parseBoolean(isRead));
			ps.executeUpdate();
			ps.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * get restaurant ID by it's branch and name
	 * 
	 * @param con     get connection to DB
	 * @param branch  get the restaurant branch
	 * @param resName get the restaurant name
	 * @return string of restaurant ID if exist, null if not
	 */
	public static String getRestaurantId(Connection con, String branch, String resName) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT restaurantId FROM restaurants WHERE branch='" + branch
					+ "' AND restaurantName='" + resName + "';");
			while (rs.next()) {
				str.append(rs.getInt(1) + "\t");
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * get all the restaurants from a specific branch
	 * 
	 * @param con    get connection to DB
	 * @param branch get the branch
	 * @return string of all the restaurant names
	 */
	public static String getRestaurantIdByBranch(Connection con, String branch) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT restaurantId FROM restaurants WHERE branch='" + branch + "';");
			while (rs.next()) {
				str.append(rs.getInt(1) + "\t");
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * get supplier ID of restaurant by the restaurant name
	 * 
	 * @param con   get connection to DB
	 * @param resId get the restaurant ID
	 * @return string of the supplier ID if res exist, else null
	 */
	public static String getSupplierId(Connection con, String resId) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT userIdNumber FROM suppliers WHERE restaurants_restaurantId='"
					+ Integer.parseInt(resId) + "';");
			while (rs.next()) {
				str.append(rs.getInt(1) + "\t");
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * get all the suppliers ID from the suppliers table
	 * 
	 * @param con get connection to DB
	 * @return string of all the ID's
	 */
	public static String getAllSupplierId(Connection con) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT userIdNumber FROM suppliers;");
			while (rs.next()) {
				str.append(rs.getInt(1) + "\t");
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * get all the msg's of the specific supplier
	 * 
	 * @param con        get connection to DB
	 * @param supplierId get the relevant supplier ID
	 * @return return string of all the msgs of the supplier
	 */
	public static String getMsgToSupplier(Connection con, String supplierId) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT * FROM msg_to_supplier WHERE toSupplierId='" + Integer.parseInt(supplierId) + "';");
			while (rs.next()) {
				str.append(rs.getInt(2) + "\t" + rs.getInt(3) + "\t" + rs.getInt(4) + "\t" + rs.getString(5) + "\t"
						+ rs.getString(6) + "\t" + rs.getString(7) + "\t" + rs.getString(8) + "\t" + rs.getBoolean(9)
						+ "\t");
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * update a supplier msg in mail box
	 * 
	 * @param con     get connection to DB
	 * @param orderId get the order ID of the msg
	 * @param status  get the msg status to update
	 * @param message get the msg text to update
	 */
	public static void updateSupplierMsg(Connection con, String orderId, String status, String message) {
		try {
			String query = "UPDATE msg_to_supplier SET status=?, message =? WHERE orderId='" + Integer.parseInt(orderId)
					+ "';";
			ps = con.prepareStatement(query);
			ps.setString(1, status);
			ps.setString(2, message);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * remove a msg from supplier mail box
	 * 
	 * @param con get connection to DB
	 */
	public static void removeMsgSupplier(Connection con) {
		try {
			String query = "DELETE FROM msg_to_supplier;";
			ps = con.prepareStatement(query);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * set msg of supplier as read
	 * 
	 * @param con     get connection to DB
	 * @param orderId get order ID of msg
	 */
	public static void setMsgSupplierRead(Connection con, String orderId) {
		try {
			String query = "UPDATE msg_to_supplier SET isRead=? WHERE orderId='" + Integer.parseInt(orderId) + "';";
			ps = con.prepareStatement(query);
			ps.setBoolean(1, true);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * msgSupplierReadBill: set message of supplier as read (bill message).
	 * 
	 * @param con        get connection to DB
	 * @param orderId    get order ID of msg
	 * @param SupplierId get Supplier ID
	 */
	public static void msgSupplierReadBill(Connection con, String orderId, String SupplierId) {
		try {
			String query = "UPDATE msg_to_supplier SET isRead=? WHERE orderId='" + Integer.parseInt(orderId)
					+ "' AND toSupplierId='" + SupplierId + "';";
			ps = con.prepareStatement(query);
			ps.setBoolean(1, true);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * get all the unread msg if a specific supplier
	 * 
	 * @param con    get connection to DB
	 * @param userId get the user ID of supplier
	 * @return string of the unread msgs
	 */
	public static String getCountMsgSupplierNotRead(Connection con, String userId) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM msg_to_supplier WHERE isRead='0' AND toSupplierId='"
					+ Integer.parseInt(userId) + "';");
			while (rs.next()) {
				str.append(rs.getInt(1) + "\t");
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * check if a specific supplier got his monthly bill
	 * 
	 * @param con     get connection to DB
	 * @param orderId get the msg order ID
	 * @param date    get the date
	 * @return the msg if exist
	 */
	public static String checkIfAlreadyGotMonthlyBill(Connection con, String orderId, String date) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM msg_to_supplier WHERE orderId='" + Integer.parseInt(orderId)
					+ "' AND Date='" + date + "';");
			while (rs.next()) {
				str.append(rs.getInt(2) + "\t" + rs.getInt(3) + "\t" + rs.getInt(4) + "\t" + rs.getString(5) + "\t"
						+ rs.getString(6) + "\t" + rs.getString(7) + "\t" + rs.getString(8) + "\t" + rs.getBoolean(9)
						+ "\t");
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * insert msg to customer table
	 * 
	 * @param con            get connection to DB
	 * @param orderId        get the order ID
	 * @param fromSupplierId get the of the supplier that sent msg
	 * @param toUserId       get the user id that the msg is for
	 * @param date           get the date of msg
	 * @param hour           get the hour of msg
	 * @param message        get the text of msg
	 * @param status         get status of msg
	 * @param isRead         get string that indicates if msg is read
	 */
	public static void InsertRecordMsgToCustomerTable(Connection con, String orderId, String fromSupplierId,
			String toUserId, String date, String hour, String message, String status, String isRead) {
		try {
			ps = con.prepareStatement(
					"insert into msg_to_customer (orderId, fromSupplierId, toUserId, date, hour, message, status, isRead) values(?,?,?,?,?,?,?,?);");
			ps.setInt(1, Integer.parseInt(orderId));
			ps.setInt(2, Integer.parseInt(fromSupplierId));
			ps.setInt(3, Integer.parseInt(toUserId));
			ps.setString(4, date);
			ps.setString(5, hour);
			ps.setString(6, message);
			ps.setString(7, status);
			ps.setBoolean(8, Boolean.parseBoolean(isRead));
			ps.executeUpdate();
			ps.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * get all the msgs of specific customer
	 * 
	 * @param con        get connection to DB
	 * @param customerId get the relevant customer ID
	 * @return string of all the msgs
	 */
	public static String getMsgToCustomer(Connection con, String customerId) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT * FROM msg_to_customer WHERE toUserId='" + Integer.parseInt(customerId) + "';");
			while (rs.next()) {
				str.append(rs.getInt(2) + "\t" + rs.getInt(3) + "\t" + rs.getInt(4) + "\t" + rs.getString(5) + "\t"
						+ rs.getString(6) + "\t" + rs.getString(7) + "\t" + rs.getString(8) + "\t" + rs.getBoolean(9)
						+ "\t");
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * remove all msg of customer
	 * 
	 * @param con get connection to DB
	 */
	public static void removeMsgCustomer(Connection con) {
		try {
			String query = "DELETE FROM msg_to_customer;";
			ps = con.prepareStatement(query);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * set a specific msg of customer as read
	 * 
	 * @param con     get connection of DB
	 * @param orderId get order ID of msg
	 */
	public static void setMsgCustomerRead(Connection con, String orderId) {
		try {
			String query = "UPDATE msg_to_customer SET isRead=? WHERE orderId='" + Integer.parseInt(orderId) + "';";
			ps = con.prepareStatement(query);
			ps.setBoolean(1, true);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * update a specific customer msg
	 * 
	 * @param con     get connection to DB
	 * @param orderId get order id of msg
	 * @param status  get the msg status to set
	 */
	public static void updateCustomerMsg(Connection con, String orderId, String status) {
		try {
			String query = "UPDATE msg_to_customer SET status=? WHERE orderId='" + Integer.parseInt(orderId) + "';";
			ps = con.prepareStatement(query);
			ps.setString(1, status);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * get all the customer msgs the hasn't been read
	 * 
	 * @param con    get connection to DB
	 * @param userId get user ID of the msg
	 * @return all the msgs that hasn't been read
	 */
	public static String getCountMsgCustomerNotRead(Connection con, String userId) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM msg_to_customer WHERE isRead='0' AND toUserId='"
					+ Integer.parseInt(userId) + "';");
			while (rs.next()) {
				str.append(rs.getInt(1) + "\t");
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * InsertSupplierTimeApprove: set the time that the supplier approve the order
	 * in DB
	 * 
	 * @param con          get the connection of DB
	 * @param orderId      get order
	 * @param supplierTime get the time that the supplier approve the order
	 */
	public static void InsertSupplierTimeApprove(Connection con, String orderId, String supplierTime) {
		try {
			ps = con.prepareStatement("insert into order_time_approve (orderId, supplierApproveTime) values(?,?);");
			ps.setInt(1, Integer.parseInt(orderId));
			ps.setString(2, supplierTime);
			ps.executeUpdate();
			ps.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * InsertCustomerTimeApprove: set Time Approve of customer in DB
	 * 
	 * @param con          get the connection of DB
	 * @param orderId      get the order ID
	 * @param customerTime get the
	 * @param customerDate get the
	 */
	public static void InsertCustomerTimeApprove(Connection con, String orderId, String customerTime,
			String customerDate) {
		try {
			ps = con.prepareStatement(
					"UPDATE order_time_approve SET customerApproveTime=?,customerApproveDate=? WHERE orderId='"
							+ Integer.parseInt(orderId) + "';");
			ps.setString(1, customerTime);
			ps.setString(2, customerDate);
			ps.executeUpdate();
			ps.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * getIsPreOrder: get if the order was early order
	 * 
	 * @param con     get the connection of DB
	 * @param orderId get the order ID
	 * @return string with the answer of is early order?
	 */
	public static String getIsPreOrder(Connection con, String orderId) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT isPreOrder FROM orders WHERE orderNumber='" + Integer.parseInt(orderId) + "';");
			while (rs.next()) {
				str.append(rs.getBoolean(1) + "\t");
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * getSupplierApproveHour: get the hour that the supplier approve the order
	 * 
	 * @param con     get the connection of DB
	 * @param orderId get the order ID
	 * @return string with the hour that the supplier approve the order
	 */
	public static String getSupplierApproveHour(Connection con, String orderId) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT supplierApproveTime FROM order_time_approve WHERE orderId='"
					+ Integer.parseInt(orderId) + "';");
			while (rs.next()) {
				str.append(rs.getString(1) + "\t");
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * getCustomerApproveHour: get the hour that the customer approve the order
	 * 
	 * @param con     get the connection of DB
	 * @param orderId get the order ID
	 * @return string with the hour that the customer approve the order
	 */
	public static String getCustomerApproveHour(Connection con, String orderId) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT customerApproveTime FROM order_time_approve WHERE orderId='"
					+ Integer.parseInt(orderId) + "';");
			while (rs.next()) {
				str.append(rs.getString(1) + "\t");
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * getBranchAndRestaurantAndFinalPriceAndUserId: get the
	 * data(branch,Restaurant,final price,user ID) of specific order
	 * 
	 * @param con     get the connection of DB
	 * @param orderId get the order id
	 * @return string with all the data of the order and the userID of the users
	 *         that ordered the order
	 */
	public static String getBranchAndRestaurantAndFinalPriceAndUserId(Connection con, String orderId) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT branch,restaurant,finalPrice,userIdNumber FROM orders WHERE orderNumber='"
							+ Integer.parseInt(orderId) + "';");
			while (rs.next()) {
				str.append(
						rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getFloat(3) + "\t" + rs.getInt(4) + "\t");
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * UpdateRecordRefundTable: set refund to customer in specific branch and
	 * specific restaurant
	 * 
	 * @param con         get the connection of DB
	 * @param userId      get the user ID
	 * @param branch      get the branch
	 * @param restaurant  get the restaurant
	 * @param priceRefund get the price refund
	 */
	public static void UpdateRecordRefundTable(Connection con, String userId, String branch, String restaurant,
			String priceRefund) {
		try {
			ps = con.prepareStatement("UPDATE refund set priceRefund=? WHERE userId='" + Integer.parseInt(userId)
					+ "' AND branch='" + branch + "' AND restaurant='" + restaurant + "';");
			ps.setFloat(1, Float.parseFloat(priceRefund));
			ps.executeUpdate();
			ps.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * setConfirmOrder: set isConfirm to true in specific order in DB
	 * 
	 * @param con     get the connection of DB
	 * @param orderId get the order ID
	 */
	public static void setConfirmOrder(Connection con, String orderId) {
		try {
			String query = "UPDATE orders SET isConfirm=? WHERE orderNumber='" + Integer.parseInt(orderId) + "';";
			ps = con.prepareStatement(query);
			ps.setBoolean(1, true);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * getDueHour: get due hour time of specific order from DB
	 * 
	 * @param con     get the connection of DB
	 * @param orderId get the order ID
	 * @return string with due hour of specific order
	 */
	public static String getDueHour(Connection con, String orderId) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT orderDueHour FROM orders WHERE orderNumber='" + Integer.parseInt(orderId) + "';");
			while (rs.next()) {
				str.append(rs.getString(1) + "\t");
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * getDueDate: get due date time of order from DB
	 * 
	 * @param con     get the connection of DB
	 * @param orderId of order that need the due date order of it.
	 * @return string with the due date of the order.
	 */
	public static String getDueDate(Connection con, String orderId) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT orderDueDate FROM orders WHERE orderNumber='" + Integer.parseInt(orderId) + "';");
			while (rs.next()) {
				str.append(rs.getString(1) + "\t");
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * getCustomerApproveDate: get the date that the customer approve the order.
	 * 
	 * @param con     get the connection of DB
	 * @param orderId get the order ID
	 * @return string with the date that the customer approve the order.
	 */
	public static String getCustomerApproveDate(Connection con, String orderId) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT customerApproveDate FROM order_time_approve WHERE orderId='"
					+ Integer.parseInt(orderId) + "';");
			while (rs.next()) {
				str.append(rs.getString(1) + "\t");
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * getPriceRefundIfExist: get all the refund if exist of user in branch specific
	 * and restaurant specific
	 * 
	 * @param con        get the connection of DB
	 * @param UserId     get the user ID
	 * @param branch     get the branch of the refund
	 * @param restaurant get the restaurant that the user used the refund
	 * @return string that get all the refund of the user in branch specific and
	 *         restaurant specific
	 */
	public static String getPriceRefundIfExist(Connection con, String UserId, String branch, String restaurant) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT priceRefund FROM refund WHERE userId='" + Integer.parseInt(UserId)
					+ "' AND branch='" + branch + "' AND restaurant='" + restaurant + "';");
			while (rs.next()) {
				str.append(rs.getFloat(1) + "\t");
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * removeRefund: remove refund of customer
	 * 
	 * @param con        get the connection of DB
	 * @param UserId     get the user ID
	 * @param branch     get the branch of the refund
	 * @param restaurant get the restaurant that the user used the refund
	 */
	public static void removeRefund(Connection con, String UserId, String branch, String restaurant) {
		try {
			String query = "DELETE FROM refund WHERE userId='" + Integer.parseInt(UserId) + "' AND branch='" + branch
					+ "' AND restaurant='" + restaurant + "';";
			ps = con.prepareStatement(query);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * InsertRecordRefundTable: set the data of refund in DB
	 * 
	 * @param con         get the connection of DB
	 * @param userId      get the user ID
	 * @param branch      get the branch
	 * @param restaurant  get the restaurant where the customer received a refund
	 * @param priceRefund get the price of refund
	 */
	public static void InsertRecordRefundTable(Connection con, String userId, String branch, String restaurant,
			String priceRefund) {
		try {
			ps = con.prepareStatement("insert into refund (userId, branch, restaurant,priceRefund) values(?,?,?,?);");
			ps.setInt(1, Integer.parseInt(userId));
			ps.setString(2, branch);
			ps.setString(3, restaurant);
			ps.setFloat(4, Float.parseFloat(priceRefund));
			ps.executeUpdate();
			ps.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * InsertOrderInRefun: set isRefund to false in specific order
	 * 
	 * @param con     get the connection of DB
	 * @param orderId of order
	 */
	public static void InsertOrderInRefund(Connection con, String orderId) {
		try {
			ps = con.prepareStatement("insert into order_in_refund (orderId, isRefund) values(?,?);");
			ps.setInt(1, Integer.parseInt(orderId));
			ps.setBoolean(2, false);
			ps.executeUpdate();
			ps.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * UpdateOrderInRefund: set refund to true of specific order in DB
	 * 
	 * @param con     get the connection of DB
	 * @param orderId get the order ID
	 */
	public static void UpdateOrderInRefund(Connection con, String orderId) {
		try {
			ps = con.prepareStatement(
					"UPDATE order_in_refund SET isRefund=? WHERE orderId='" + Integer.parseInt(orderId) + "';");
			ps.setBoolean(1, true);
			ps.executeUpdate();
			ps.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * getDishInOrder: get the data (item,dish,size and additional request) of all
	 * the dishes in specific order
	 * 
	 * @param con     get the connection of DB
	 * @param orderId get the order ID
	 * @return string with the data(item,dish,size and additional request) of all
	 *         the dishes in specific order
	 */
	public static String getDishInOrder(Connection con, String orderId) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT item,dish,doneness,size,additionalRequest FROM dish_in_order WHERE orders_OrderNumber='"
							+ Integer.parseInt(orderId) + "';");
			while (rs.next()) {
				str.append(rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3) + "\t" + rs.getString(4)
						+ "\t" + rs.getString(5) + "\t");
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * getCountDishInOrder: get the count of dishes in the specific order.
	 * 
	 * @param con     get the connection of DB
	 * @param orderId get the order ID
	 * @return get string with count of dishes in specific order.
	 */
	public static String getCountDishInOrder(Connection con, String orderId) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT COUNT(*) FROM dish_in_order WHERE orders_OrderNumber='" + Integer.parseInt(orderId) + "';");
			while (rs.next()) {
				str.append(rs.getInt(1) + "\t");
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * updateSupplierMsgFromCustomer: set message of customer in supplier in DB
	 * 
	 * @param con     get the connection of DB
	 * @param orderId get the order ID
	 * @param status get the status of message.
	 * @param message get the message of the customer
	 * @param isReady get
	 */
	public static void updateSupplierMsgFromCustomer(Connection con, String orderId, String status, String message,
			String isReady) {
		try {
			String query = "UPDATE msg_to_supplier SET status=?, message=?, isRead=? WHERE orderId='"
					+ Integer.parseInt(orderId) + "';";
			ps = con.prepareStatement(query);
			ps.setString(1, status);
			ps.setString(2, message);
			ps.setBoolean(3, Boolean.parseBoolean(isReady));
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * UpdatePrivateCustomerDetails: insert new private customer with his data to
	 * private_customers table in DB
	 * 
	 * @param con         get the connection of DB
	 * @param UserID      get the user ID
	 * @param FirstName   get the first name of the customer
	 * @param LastName    get the last name of the customer
	 * @param Email       get the email of the customer
	 * @param PhoneNumber get the phone number of the customer
	 * @param CreditCard  get the credit card of the customer
	 */
	public static void UpdatePrivateCustomerDetails(Connection con, String UserID, String FirstName, String LastName,
			String Email, String PhoneNumber, String CreditCard) {
		try {
			ps = con.prepareStatement(
					"INSERT IGNORE private_customers SET userIdNumber=?,firstName=?,lastName=?,email=?,phoneNumber=?,creditCard=?;");
			ps.setString(1, UserID);
			ps.setString(2, FirstName);
			ps.setString(3, LastName);
			ps.setString(4, Email);
			ps.setString(5, PhoneNumber);
			ps.setString(6, CreditCard);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			ps = con.prepareStatement("INSERT IGNORE private_w4c_card SET userIdNumber=?,privateCodeNumber=?;");
			ps.setInt(1, Integer.parseInt(UserID));
			ps.setString(2, randomCode(4));
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * UpdateBusinessCustomerDetails: insert new business customer to
	 * business_customers table in DB
	 * 
	 * @param con           get the connection of DB
	 * @param UserID        get the user ID
	 * @param FirstName     get the first name of the business customer
	 * @param LastName      get the last name of the business customer
	 * @param Email         get the email of the business customer
	 * @param PhoneNumber   get the phone number of the business customer
	 * @param EmployerName  get the employer name.
	 * @param CreditCard    get the credit card of the business customer
	 * @param MonthlyBudget get the monthly budget of the business customer
	 */
	public static void UpdateBusinessCustomerDetails(Connection con, String UserID, String EmployerName,
			String FirstName, String LastName, String Email, String PhoneNumber, String CreditCard,
			Float MonthlyBudget) {
		try {
			ps = con.prepareStatement(
					"INSERT IGNORE business_customers SET userIdNumber=?,businessName=?,firstName=?,lastName=?,email=?,phoneNumber=?,creditCard=?,currentBudget=?;");
			ps.setString(1, UserID);
			ps.setString(2, EmployerName);
			ps.setString(3, FirstName);
			ps.setString(4, LastName);
			ps.setString(5, Email);
			ps.setString(6, PhoneNumber);
			ps.setString(7, CreditCard);
			ps.setFloat(8, MonthlyBudget);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * UpdateSupplierDetails: insert new restaurant to restaurants table in DB
	 * 
	 * @param con        get the connection of DB
	 * @param UserID     get the user ID of the supplier
	 * @param Branch     get the branch of the supplier
	 * @param Restaurant get the restaurant of the supplier
	 * @param ResAddress get the restaurant address of supplier
	 */
	public static void UpdateSupplierDetails(Connection con, String UserID, String Branch, String Restaurant,
			String ResAddress) {
		try {
			ps = con.prepareStatement("INSERT IGNORE restaurants SET restaurantName=?,branch=?,restaurantAddress=?;");
			ps.setString(1, Restaurant);
			ps.setString(2, Branch);
			ps.setString(3, ResAddress);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			ps = con.prepareStatement("INSERT IGNORE suppliers SET userIdNumber=?;");
			ps.setInt(1, Integer.parseInt(UserID));
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * ReturnEmployersName: get all the names of employers
	 * 
	 * @param con get the connection of DB
	 * @return string with the names of all employers
	 */
	public static String ReturnEmployersName(Connection con) {
		try {
			StringBuilder str = new StringBuilder();
			String temp = "SELECT businessName FROM registered_business;";
			ps = con.prepareStatement(temp);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				str.append(rs.getString(1) + "\t");
			rs.close();
			ps.close();
			return str.toString();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ReturnBudget: get the monthly budget of employer
	 * 
	 * @param con          get the connection of DB
	 * @param EmployerName get the name of the employer
	 * @return string with the monthly budget of the employer
	 */
	public static String ReturnBudget(Connection con, String EmployerName) {
		try {
			String str = null;
			String temp = "SELECT monthlyBudget FROM registered_business WHERE businessName='" + EmployerName + "';";
			ps = con.prepareStatement(temp);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				str = String.valueOf(rs.getFloat(1));
			rs.close();
			ps.close();
			return str;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ReturnRestaurantID: get the restaurant id of specific restaurant by the name
	 * and branch of it
	 * 
	 * @param con     get the connection of DB
	 * @param ResName get the restaurant name
	 * @param Branch  get the branch of the restaurant
	 * @return string with the restaurant ID
	 */
	public static String ReturnRestaurantID(Connection con, String ResName, String Branch) {
		try {
			String str = null;
			String temp = "SELECT restaurantId FROM restaurants WHERE restaurantName='" + ResName + "' AND branch='"
					+ Branch + "';";
			ps = con.prepareStatement(temp);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				str = String.valueOf(rs.getInt(1));
			rs.close();
			ps.close();
			return str;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * importDataOfUsersToUsersTable: import data from import_users table to users
	 * table
	 * 
	 * @param conn get the connection of DB
	 */
	public static void importDataOfUsersToUsersTable(Connection conn) {
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM import_users;");
			while (rs.next()) {
				PreparedStatement ps;
				ps = EchoServer.conn.prepareStatement(
						("insert IGNORE into users(useridNumber, firstName, lastName, email, phoneNumber, role, isLoggedIn, userName, password, type, status) values(?,?,?,?,?,?,?,?,?,?,?);"));
				ps.setInt(1, rs.getInt(1));
				ps.setString(2, rs.getString(2));
				ps.setString(3, rs.getString(3));
				ps.setString(4, rs.getString(4));
				ps.setString(5, rs.getString(5));
				ps.setString(6, rs.getString(6));
				ps.setBoolean(7, rs.getBoolean(7));
				ps.setString(8, rs.getString(8));
				ps.setString(9, rs.getString(9));
				ps.setString(10, rs.getString(10));
				ps.setString(11, rs.getString(11));
				ps.executeUpdate();
				ps.close();
			}
			rs.close();
			System.out.println("Import Data succeeded");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ReturnSumOfFinalPriceAndOrdersID: return the sum of FinalPrice per order ID
	 * for produce monthly reports.
	 * 
	 * @param con        get connection to DB.
	 * @param branch     get relevant branch.
	 * @param restuarant get relevant restuarant.
	 * @param year       get relevant year.
	 * @param month      get relevant month.
	 * @return ArrayList of the sum of final prices.
	 */
	public static ArrayList<String> ReturnSumOfFinalPriceAndOrdersID(Connection con, String branch, String restuarant,
			String year, String month) {
		ArrayList<String> RESULTS = new ArrayList<>();
		// int i = 1;
		// String[] RESULTS = {};
		Float sumOfFinalPrice = (float) 0;
		String date;
		String[] arrayOfDates;
		String temp = "SELECT * FROM orders WHERE branch='" + branch + "' AND restaurant='" + restuarant + "';";
		try {
			ps = con.prepareStatement(temp);
			ResultSet rs = ps.executeQuery();
			RESULTS.add(" ");
			while (rs.next()) {
				date = rs.getString(5); // get orderDate
				arrayOfDates = date.split("/");

				if (month.length() == 1) // pad with 0 the left side of day digit
					month = '0' + month;

				if ((arrayOfDates[1].equals(month)) && (arrayOfDates[2].equals(year)))//
				{
					sumOfFinalPrice += rs.getFloat(13);
					RESULTS.add(String.valueOf(rs.getInt(1)));
				}

			}
			RESULTS.set(0, String.valueOf(sumOfFinalPrice));
			rs.close();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return RESULTS;
	}

	/**
	 * ReturnRevFromItemType: get all main dishes prices for revenue reports.
	 * 
	 * @param con      get connection to DB.
	 * @param orderID  orderId get order id for revenue reports.
	 * @param itemType get the type of item for revenue reports.
	 * @return the sum of all main dishes price.
	 */
	public static Float ReturnRevFromItemType(Connection con, String orderID, String itemType) {

		Float sumOfMainDishsPrices = (float) 0;
		String temp = "SELECT * FROM dish_in_order WHERE orders_OrderNumber='" + orderID + "' AND item='" + itemType
				+ "';";
		try {
			ps = con.prepareStatement(temp);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				sumOfMainDishsPrices += rs.getFloat(7);

			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sumOfMainDishsPrices;

	}

	/**
	 * ReturnNumOfItemType: get the number of all main dishes.
	 * 
	 * @param con      get connection to DB.
	 * @param OrderId  get order id for revenue reports.
	 * @param itemType get the type of item for revenue reports.
	 * @return the number of main dishes that in dish_in_order tables.
	 */
	public static int ReturnNumOfItemType(Connection con, String OrderID, String itemType) {
		Integer NumberOfMainDishs = 0;
		String temp = "SELECT * FROM dish_in_order WHERE orders_OrderNumber='" + OrderID + "' AND item='" + itemType
				+ "';";
		try {
			ps = con.prepareStatement(temp);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				NumberOfMainDishs += 1;

			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return NumberOfMainDishs;

	}

	/**
	 * ReturnNumOfOrders: get the number of orders that customer confirmed.
	 * 
	 * @param con        get connection to DB.
	 * @param branch     get relevant branch.
	 * @param restaurnat get relevant restuarant.
	 * @return the number of orders that has in orders Table.
	 */
	public static int ReturnNumOfOrders(Connection con, String branch, String restaurnat) {
		Integer NumberOfOrders = 0;
		String temp = "SELECT * FROM orders WHERE branch='" + branch + "' AND restaurant='" + restaurnat + "';";
		try {
			ps = con.prepareStatement(temp);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				NumberOfOrders += 1;

			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return NumberOfOrders;

	}

	/**
	 * ReturnNumOfDelays: get all delays at delivery for reports.
	 * 
	 * @param con     get connection to DB.
	 * @param OrderID get order id for performance reports.
	 * @return the number of delays at delivery.
	 */
	public static Integer ReturnNumOfDelays(Connection con, String OrderID) {
		Integer NumberOfDelays = 0;
		String temp = "SELECT * FROM order_in_refund WHERE orderId='" + OrderID + "';";
		try {
			ps = con.prepareStatement(temp);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getBoolean(2)) // if isRefund equals true
					NumberOfDelays += 1;

			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return NumberOfDelays;

	}

	/**
	 * findSupplierRestaurant: get the restaurant ID by findig the supplier in
	 * suppliers table.
	 * 
	 * @param con    get connection to DB.
	 * @param userID get the relevant user Id.
	 * @return the restarund Id by supplier Id.
	 */
	public static int findSupplierRestaurant(Connection con, int userID) {
		int resID = 0;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT restaurants_restaurantId FROM suppliers WHERE userIdNumber='" + userID + "';");
			while (rs.next()) {
				resID = rs.getInt(1);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resID;
	}

	/**
	 * findHrBusiness: get the business name of hr by relevant userId.
	 * 
	 * @param con    get connection to DB.
	 * @param userID get the relevant user Id.
	 * @return the business name of the relevant hr.
	 */
	public static String findHrBusiness(Connection con, int userID) {
		String BusinessName = "";
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT role FROM users WHERE userIdNumber='" + userID + "';");
			while (rs.next()) {
				BusinessName = rs.getString(1).split(" ")[1] + " " + rs.getString(1).split(" ")[2];
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return BusinessName;
	}

	/**
	 * checkIfRecordExist: check if record exist at waiting_business table.
	 * 
	 * @param con          get connection to DB.
	 * @param businessName get the relevant business name.
	 * @return return Busines Phone if record exist at DB.
	 */
	public static boolean checkIfRecordExist(Connection con, String businessName) {
		try {
			String str = null;
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT businessPhone FROM waiting_business WHERE businessName='" + businessName + "';");
			while (rs.next()) {
				str = rs.getString(1);
			}
			rs.close();
			if (str != null) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * getW4CcodePrivate: get the W4C Code of specific private cusotmer.
	 * 
	 * @param con    get connection to DB.
	 * @param userId get the relevant userId.
	 * @return the W4C Code of this ptivate customer.
	 */
	public static String getW4CcodePrivate(Connection con, String userId) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT privateCodeNumber FROM private_w4c_card WHERE userIdNumber='"
					+ Integer.parseInt(userId) + "';");
			while (rs.next()) {
				str.append(rs.getString(1) + "\t");
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * getBusinessName: get the bussines name from busines_custoers table by user ID
	 * of business customer.
	 * 
	 * @param con    get connection to DB.
	 * @param userId get the relevant userId.
	 * @return the businessName by appropriate user ID.
	 */
	public static String getBusinessName(Connection con, String userId) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT businessName FROM business_customers WHERE userIdNumber='"
					+ Integer.parseInt(userId) + "';");
			while (rs.next()) {
				str.append(rs.getString(1) + "\t");
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * getW4CcodeBusiness: get the W4C Code of specific Business cusotmer.
	 * 
	 * @param con          get connection to DB.
	 * @param businessName get the relevant business name.
	 * @return the W4C Code of this business customer from employer_w4c_card table.
	 */
	public static String getW4CcodeBusiness(Connection con, String businessName) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT employerCodeNumber FROM employer_w4c_card WHERE businessName='" + businessName + "';");
			while (rs.next()) {
				str.append(rs.getString(1) + "\t");
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * changeUserType: change the permission of user at BiteMe system.
	 * 
	 * @param con    get connection to DB.
	 * @param type   get the type of user (Permission of user).
	 * @param userId get the relevatID
	 */
	public static void changeUserType(Connection con, String type, String userId) {
		try {
			String query = "UPDATE users SET type=? WHERE userIdNumber='" + userId + "';";
			ps = con.prepareStatement(query);
			ps.setString(1, type);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * addNewHr: insert new HR to hr table when the Branch manager confirmed his\her
	 * registration.
	 * 
	 * @param con          get connection to DB.
	 * @param UserId       get the relevat ID of user.
	 * @param businessName get the relevant business Name.
	 */
	public static void addNewHr(Connection con, String UserId, String businessName) {
		try {
			ps = con.prepareStatement("insert into hr (userIdNumber, businessName) values(?,?);");
			ps.setInt(1, Integer.parseInt(UserId));
			ps.setString(2, businessName);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * getTypeOfOrder: get the type of order from orders table.
	 * 
	 * @param con         get connection to DB.
	 * @param orderNumber get the relevant orderNumber.
	 * @return the type of order - TakeAway or Shipment.
	 */
	public static String getTypeOfOrder(Connection con, String orderNumber) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT typeOfOrder FROM orders WHERE orderNumber='" + Integer.parseInt(orderNumber) + "';");
			while (rs.next()) {
				str.append(rs.getString(1) + "\t");
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * insertIntoBusinessCustomer: get the details from waiting business customers
	 * table to business customers table that confirmed by branch manager. In
	 * addition, update the status of waiting business customer to confirmed
	 * (boolean true).
	 * 
	 * @param con    get connection to DB.
	 * @param userId get the relevat ID of user.
	 */
	public static void insertIntoBusinessCustomer(Connection con, String userId) {
		try {
			ArrayList<String> str = new ArrayList<String>();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT * FROM waiting_business_customer WHERE userIdNumber='" + Integer.parseInt(userId) + "';");
			while (rs.next()) {
				str.add(String.valueOf(rs.getInt(1)));
				str.add(rs.getString(2));
				str.add(rs.getString(3));
				str.add(rs.getString(4));
				str.add(rs.getString(5));
				str.add(rs.getString(6));
				str.add(rs.getString(7));
				str.add(String.valueOf(rs.getFloat(8)));
			}
			rs.close();

			ps = con.prepareStatement(
					"insert into business_customers (userIdNumber, businessName ,firstName, lastName, email, phoneNumber, creditCard, currentBudget) values(?,?,?,?,?,?,?,?);");
			ps.setInt(1, Integer.parseInt(str.get(0)));
			ps.setString(2, str.get(1));
			ps.setString(3, str.get(2));
			ps.setString(4, str.get(3));
			ps.setString(5, str.get(4));
			ps.setString(6, str.get(5));
			ps.setString(7, str.get(6));
			ps.setFloat(8, Float.parseFloat(str.get(7)));
			ps.executeUpdate();
			ps.close();

			String query = "UPDATE waiting_business_customer SET isConfirm=? WHERE userIdNumber='"
					+ Integer.parseInt(userId) + "';";
			ps = con.prepareStatement(query);
			ps.setBoolean(1, true);
			ps.executeUpdate();
			ps.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * findBusinessNameByHr: get the busines Name of HR from hr table.
	 * 
	 * @param con  con get connection to DB.
	 * @param hrId get the relevant Id of hr.
	 * @return the business name of this specific hr.
	 */
	public static String findBusinessNameByHr(Connection con, String hrId) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT businessName FROM hr WHERE userIdNumber='" + Integer.parseInt(hrId) + "';");
			while (rs.next()) {
				str.append(rs.getString(1) + "\t");
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * findHrByBusinessName: get the id of HR from users table.
	 * 
	 * @param con          get connection to DB.
	 * @param businessName get the relevant business name.
	 * @return the relevant hr by business name.
	 */
	public static String findHrByBusinessName(Connection con, String businessName) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT userIdNumber FROM users WHERE role='" + "HR " + businessName + "';");
			while (rs.next()) {
				str.append(rs.getString(1) + "\t");
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * UpdateWaitingBusConfirm: update waiting_business table to confirmed when
	 * branch manager confirmed the business.
	 * 
	 * @param con          get connection to DB.
	 * @param businessName get the relevant business name.
	 */
	public static void UpdateWaitingBusConfirm(Connection con, String businessName) {
		try {
			String query = "UPDATE waiting_business SET isConfirm=? WHERE businessName='" + businessName + "';";
			ps = con.prepareStatement(query);
			ps.setBoolean(1, true);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * takeBusData: take all data from waiting_business table by business name.
	 * 
	 * @param con          get connection to DB.
	 * @param businessName get the relevant business name.
	 * @return all data of business from waiting_business table.
	 */
	public static String takeBusData(Connection con, String businessName) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT businessName, businessAddress, businessPhone, monthlyBudget  FROM waiting_business WHERE businessName='"
							+ businessName + "';");
			while (rs.next()) {
				str.append(rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3) + "\t" + rs.getInt(4));
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Insert new business customer for waiting list (for confirm)
	 * 
	 * @param con           get connection to DB.
	 * @param userIdNumber  get user ID number.
	 * @param businessName  get the relevant business name.
	 * @param firstName     get the first name.
	 * @param lastName      get the last name.
	 * @param email         get the email.
	 * @param phoneNumber   get the phone.
	 * @param creditCard    get the credit card.
	 * @param currentBudget get the currently budget.
	 */
	public static void InsertWaitingBusinessCus(Connection con, String userIdNumber, String businessName,
			String firstName, String lastName, String email, String phoneNumber, String creditCard,
			String currentBudget) {
		try {
			ps = con.prepareStatement(
					"insert into waiting_business_customer (userIdNumber, businessName ,firstName, lastName, email, phoneNumber, creditCard, currentBudget) values(?,?,?,?,?,?,?,?);");
			ps.setInt(1, Integer.parseInt(userIdNumber));
			ps.setString(2, businessName);
			ps.setString(3, firstName);
			ps.setString(4, lastName);
			ps.setString(5, email);
			ps.setString(6, phoneNumber);
			ps.setString(7, creditCard);
			ps.setFloat(8, Float.parseFloat(currentBudget));
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Take from DB details order by specific month
	 * 
	 * @param con   get connection to DB.
	 * @param month get the specific month for order.
	 * @return string that contains order number, branch and restaurant of many
	 *         orders.
	 */
	public static String getOrderDetailsByMonth(Connection con, String month) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT orderNumber,branch,restaurant FROM orders WHERE SUBSTRING(orderConfirmDate, 4, 2) ='"
							+ month + "';");
			while (rs.next()) {
				str.append(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getString(3) + "\t");
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Take from DB details dish in order by order id
	 * 
	 * @param con     get connection to DB.
	 * @param orderId get the order Id.
	 * @return string that contains branch and restaurant, item, dish, price and
	 *         commission of many dish in orders
	 */
	public static String getFromDishInOrderMonthDishes(Connection con, String orderId) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT branch,restaurant,item,dish,price,commission FROM dish_in_order WHERE orders_orderNumber='"
							+ Integer.parseInt(orderId) + "' ;");
			while (rs.next()) {
				str.append(rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3) + "\t" + rs.getString(4)
						+ "\t" + rs.getFloat(5) + "\t" + rs.getFloat(6) + "\t");
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Update in table dish in order the commission
	 * 
	 * @param con       get connection to DB.
	 * @param comission get the comission for bill.
	 */
	public static void setComission(Connection con, String comission) {
		try {
			String query = "UPDATE dish_in_order SET commission=?;";
			ps = con.prepareStatement(query);
			ps.setFloat(1, Float.parseFloat(comission));
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Delete users from relevant tables and update type of in user table
	 * 
	 * @param con      get connection to DB.
	 * @param Username get the username to remove.
	 * @param status   get the status for reset.
	 * @param id       get the id number.
	 * @param type:    get type of user.
	 */
	public static void RemoveUserTBL(Connection con, String Username, String status, Integer id, String type) {
		try {
			String temp = "UPDATE users SET type=?,status=? WHERE userName='" + Username + "';";
			ps = con.prepareStatement(temp);
			ps.setString(1, "-");
			ps.setString(2, "Active");
			ps.executeUpdate();
			ps.close();
			if (type.equals("Private_Customer"))
				temp = "delete from private_customers where userIdNumber = ?";
			else if (type.equals("Business_Customer"))
				temp = "delete from business_customers where userIdNumber = ?";
			else if (type.equals("Supplier"))
				temp = "delete from suppliers where userIdNumber = ?";
			else if (type.equals("HR_Manager"))
				temp = "delete from hr where userIdNumber = ?";
			ps = con.prepareStatement(temp);
			ps.setInt(1, id);
			ps.executeUpdate();
			ps.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Insert appropriate message to table of msg to manager
	 * 
	 * @param con          get connection to DB.
	 * @param fromHrId:    source.
	 * @param toManagerId: destination.
	 * @param date         get the relevant date.
	 * @param hour         get the relevant hour.
	 * @param message:     content of message
	 * @param status       get the relevant status.
	 * @param isRead       get if the message is readen.
	 */
	public static void InsertRecordMsgToManagerTable(Connection con, String fromHrId, String toManagerId, String date,
			String hour, String message, String status, String isRead) {
		try {
			ps = con.prepareStatement(
					"insert into msg_to_manager (fromHrId, toUserId, date, hour, message, status, isRead) values(?,?,?,?,?,?,?);");
			ps.setInt(1, Integer.parseInt(fromHrId));
			ps.setInt(2, Integer.parseInt(toManagerId));
			ps.setString(3, date);
			ps.setString(4, hour);
			ps.setString(5, message);
			ps.setString(6, status);
			ps.setBoolean(7, Boolean.parseBoolean(isRead));
			ps.executeUpdate();
			ps.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Insert appropriate message to table of msg to HR
	 * 
	 * @param con            get connection to DB.
	 * @param fromManagerId: source Id.
	 * @param toHrId:        destination Id.
	 * @param date           get the date when message sent.
	 * @param hour           get the hour when message sent.
	 * @param message:       content of message
	 * @param status         get the status of message.
	 * @param isRead         get if the message is readen.
	 */
	public static void InsertRecordMsgToHrTable(Connection con, String fromManagerId, String toHrId, String date,
			String hour, String message, String status, String isRead) {
		try {
			ps = con.prepareStatement(
					"insert into msg_to_hr (fromManagerId, toUserId, date, hour, message, status, isRead) values(?,?,?,?,?,?,?);");
			ps.setInt(1, Integer.parseInt(fromManagerId));
			ps.setInt(2, Integer.parseInt(toHrId));
			ps.setString(3, date);
			ps.setString(4, hour);
			ps.setString(5, message);
			ps.setString(6, status);
			ps.setBoolean(7, Boolean.parseBoolean(isRead));
			ps.executeUpdate();
			ps.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get message from msg to manager by manager id
	 * 
	 * @param con        get connection to DB.
	 * @param ManagerId: get the id of manager.
	 * @return string of details message
	 */
	public static String getMsgToManager(Connection con, String ManagerId) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM msg_to_manager WHERE toUserId='" + Integer.parseInt(ManagerId) + "';");
			while (rs.next()) {
				str.append(rs.getInt(2) + "\t" + rs.getInt(3) + "\t" + rs.getString(4) + "\t" + rs.getString(5) + "\t"
						+ rs.getString(6) + "\t" + rs.getString(7) + "\t" + rs.getBoolean(8) + "\t");
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Get message from msg to hr by manager id
	 * 
	 * @param con        get connection to DB.
	 * @param managerId: id of user
	 * @return string of details message
	 */
	public static String getMsgToHr(Connection con, String managerId) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM msg_to_hr WHERE toUserId='" + Integer.parseInt(managerId) + "';");
			while (rs.next()) {
				str.append(rs.getInt(2) + "\t" + rs.getInt(3) + "\t" + rs.getString(4) + "\t" + rs.getString(5) + "\t"
						+ rs.getString(6) + "\t" + rs.getString(7) + "\t" + rs.getBoolean(8) + "\t");
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Count number of message that not read from msg_to_manager table
	 * 
	 * @param con    get connection to DB.
	 * @param userId get the id of user.
	 * @return string that contains the number message
	 */
	public static String getCountMsgManagerNotRead(Connection con, String userId) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM msg_to_manager WHERE isRead='0' AND toUserId='"
					+ Integer.parseInt(userId) + "';");
			while (rs.next()) {
				str.append(rs.getInt(1) + "\t");
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Count number of message that not read from msg_to_hr table
	 * 
	 * @param con    get connection to DB.
	 * @param userId get the id of user.
	 * @return string that contains the number message
	 */
	public static String getCountMsgHrNotRead(Connection con, String userId) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT COUNT(*) FROM msg_to_hr WHERE isRead='0' AND toUserId='" + Integer.parseInt(userId) + "';");
			while (rs.next()) {
				str.append(rs.getInt(1) + "\t");
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Update specific message that is read in msg_to_manager table
	 * 
	 * @param con       get connection to DB.
	 * @param fromHrId: id of Hr
	 */
	public static void setMsgManagerRead(Connection con, String fromHrId) {
		try {
			String query = "UPDATE msg_to_manager SET isRead=? WHERE fromHrId='" + Integer.parseInt(fromHrId) + "';";
			ps = con.prepareStatement(query);
			ps.setBoolean(1, true);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Update specific message that is read in msg_to_hr table
	 * 
	 * @param con            get connection to DB.
	 * @param fromManagerId: id of manager.
	 */
	public static void setMsgHrRead(Connection con, String fromManagerId) {
		try {
			String query = "UPDATE msg_to_hr SET isRead=? WHERE fromManagerId='" + Integer.parseInt(fromManagerId)
					+ "';";
			ps = con.prepareStatement(query);
			ps.setBoolean(1, true);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Remove messages from table msg_to_manager
	 * 
	 * @param con get connection to DB.
	 */
	public static void removeMsgManager(Connection con) {
		try {
			String query = "DELETE FROM msg_to_manager;";
			ps = con.prepareStatement(query);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Remove messages from table msg_to_hr
	 * 
	 * @param con get connection to DB.
	 */
	public static void removeMsgHr(Connection con) {
		try {
			String query = "DELETE FROM msg_to_hr;";
			ps = con.prepareStatement(query);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Update messages in msg_to_hr table (status, content message and isRead)
	 * 
	 * @param con           get connection to DB.
	 * @param fromManagerId get id of manager.
	 * @param status        get status of message.
	 * @param message       get the message.
	 * @param isReady       get if the message is readen.
	 */
	public static void updateHrMsgFromManager(Connection con, String fromManagerId, String status, String message,
			String isReady) {
		try {
			String query = "UPDATE msg_to_hr SET status=?, message=?, isRead=? WHERE fromManagerId='"
					+ Integer.parseInt(fromManagerId) + "';";
			ps = con.prepareStatement(query);
			ps.setString(1, status);
			ps.setString(2, message);
			ps.setBoolean(3, Boolean.parseBoolean(isReady));
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Update messages in msg_to_manager table (status, content message and isRead)
	 * 
	 * @param con      get connection to DB.
	 * @param fromHrId Id get id of hr.
	 * @param status   get status of message.
	 * @param message  get the message.
	 * @param isReady  get if the message is readen.
	 */
	public static void updateManagerMsgFromHr(Connection con, String fromHrId, String status, String message,
			String isReady) {
		try {
			String query = "UPDATE msg_to_manager SET status=?, message=?, isRead=? WHERE fromHrId='"
					+ Integer.parseInt(fromHrId) + "';";
			ps = con.prepareStatement(query);
			ps.setString(1, status);
			ps.setString(2, message);
			ps.setBoolean(3, Boolean.parseBoolean(isReady));
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Update status message in msg_to_manager table
	 * 
	 * @param con    get connection to DB.
	 * @param userId get the Id of hr.
	 * @param status get the status of message.
	 */
	public static void updateManagerMsg(Connection con, String userId, String status) {
		try {
			String query = "UPDATE msg_to_manager SET status=? WHERE fromHrId='" + Integer.parseInt(userId) + "';";
			ps = con.prepareStatement(query);
			ps.setString(1, status);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Update status message in msg_to_hr table
	 * 
	 * @param con     get connection to DB.
	 * @param message get the messgae.
	 * @param status  get the status of message.
	 */
	public static void updateHrMsg(Connection con, String message, String status) {
		try {
			String query = "UPDATE msg_to_hr SET status=? WHERE Message='" + message + "';";
			ps = con.prepareStatement(query);
			ps.setString(1, status);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Insert message to msg_to_ceo table
	 * 
	 * @param con            get connection to DB.
	 * @param fromManagerId: source Manager Id.
	 * @param toUserId:      destination to user Id.
	 * @param date           get the date of message.
	 * @param hour           get the hour of message.
	 * @param message        get the messgae.
	 * @param status         get the status of message.
	 * @param isRead         get if the message is readen.
	 */
	public static void InsertRecordMsgToCeoTable(Connection con, String fromManagerId, String toUserId, String date,
			String hour, String message, String status, String isRead) {
		try {
			ps = con.prepareStatement(
					"insert into msg_to_ceo (fromManagerId, toUserId, date, hour, message, status, isRead) values(?,?,?,?,?,?,?);");
			ps.setInt(1, Integer.parseInt(fromManagerId));
			ps.setInt(2, Integer.parseInt(toUserId));
			ps.setString(3, date);
			ps.setString(4, hour);
			ps.setString(5, message);
			ps.setString(6, status);
			ps.setBoolean(7, Boolean.parseBoolean(isRead));
			ps.executeUpdate();
			ps.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get message from table msg_to_ceo by ceo id
	 * 
	 * @param con    get connection to DB.
	 * @param CeoId: id of CEO.
	 * @return string that contains details of message
	 */
	public static String getMsgToCeo(Connection con, String CeoId) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM msg_to_ceo WHERE toUserId='" + Integer.parseInt(CeoId) + "';");
			while (rs.next()) {
				str.append(rs.getInt(2) + "\t" + rs.getInt(3) + "\t" + rs.getString(4) + "\t" + rs.getString(5) + "\t"
						+ rs.getString(6) + "\t" + rs.getString(7) + "\t" + rs.getBoolean(8) + "\t");
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Count number of message that not read in msg_to_ceo table
	 * 
	 * @param con   get connection to DB.
	 * @param ceoId id of CEO.
	 * @return string that contains the number
	 */
	public static String getCountMsgCeoNotRead(Connection con, String ceoId) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT COUNT(*) FROM msg_to_ceo WHERE isRead='0' AND toUserId='" + Integer.parseInt(ceoId) + "';");
			while (rs.next()) {
				str.append(rs.getInt(1) + "\t");
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Update message to read in msg_to_ceo table
	 * 
	 * @param con           get connection to DB.
	 * @param fromManagerId source Manager Id.
	 */
	public static void setMsgCeoRead(Connection con, String fromManagerId) {
		try {
			String query = "UPDATE msg_to_ceo SET isRead=? WHERE fromManagerId='" + Integer.parseInt(fromManagerId)
					+ "';";
			ps = con.prepareStatement(query);
			ps.setBoolean(1, true);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Delete messages from msg_to_ceo table
	 * 
	 * @param con get connection to DB.
	 */
	public static void removeMsgCeo(Connection con) {
		try {
			String query = "DELETE FROM msg_to_ceo;";
			ps = con.prepareStatement(query);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get details of orders by branch and quarter and report number(1/2)
	 * 
	 * @param con        get connection to DB.
	 * @param branch     get the relevant branch.
	 * @param Quarter    get the relevant quarter.
	 * @param ReportNum: get the id of report.
	 * @return HashMap that contains restaurant name and orders quantity
	 * @throws SQLException
	 */
	public static HashMap<String, Integer> getDetailsForCEOreportOrders(Connection con, String branch, String Quarter,
			String ReportNum) throws SQLException {
		String temp = "SELECT * FROM orders WHERE branch='" + branch + "';";
		String relevantQuarter = "";
		HashMap<String, Integer> restaurantsOrders = new HashMap<>();
		switch (Quarter) {
		case "First Quarter":
			relevantQuarter = "01/02/03";
			break;
		case "Second Quarter":
			relevantQuarter = "04/05/06";
			break;
		case "Third Quarter":
			relevantQuarter = "07/08/09";
			break;
		case "Fourth Quarter":
			relevantQuarter = "10/11/12";
			break;

		}
		String[] arrayOfDates;
		String date;
		ps = con.prepareStatement(temp);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			date = rs.getString(5); // get orderDate
			arrayOfDates = date.split("/");
			if (relevantQuarter.contains(arrayOfDates[1])) // check if the order date is matching the quarter
			{
				if (restaurantsOrders.containsKey(rs.getString(12)))// get restaurant name and num of orders
					restaurantsOrders.put(rs.getString(12), restaurantsOrders.get(rs.getString(12)) + 1);

				else
					restaurantsOrders.put(rs.getString(12), 1);
			}

		}
		restaurantsOrders.put("ordersHashMap", 0);
		restaurantsOrders.put(ReportNum, 0);

		rs.close();
		ps.close();
		return restaurantsOrders;
	}

	/**
	 * Get details of revenues by branch and quarter and report number(1/2)
	 * 
	 * @param con        get connection to DB.
	 * @param branch     get the relevant branch.
	 * @param Quarter    get the relevant quarter.
	 * @param ReportNum: get the id of report.
	 * @return HashMap that contains restaurant name and his revenue
	 * @throws SQLException
	 */
	public static HashMap<String, Float> getDetailsForCEOreportRevenues(Connection con, String branch, String Quarter,
			String ReportNum) throws SQLException {
		String temp = "SELECT * FROM orders WHERE branch='" + branch + "';";
		String relevantQuarter = "";
		HashMap<String, Float> restaurantsRevenue = new HashMap<>();
		switch (Quarter) {
		case "First Quarter":
			relevantQuarter = "01/02/03";
			break;
		case "Second Quarter":
			relevantQuarter = "04/05/06";
			break;
		case "Third Quarter":
			relevantQuarter = "07/08/09";
			break;
		case "Fourth Quarter":
			relevantQuarter = "10/11/12";
			break;

		}
		String[] arrayOfDates;
		String date;
		ps = con.prepareStatement(temp);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			date = rs.getString(5); // get orderDate
			arrayOfDates = date.split("/");
			if (relevantQuarter.contains(arrayOfDates[1])) // check if the order date is matching the quarter
			{
				if (restaurantsRevenue.containsKey(rs.getString(12)))// get restaurant name and sum of orders
					restaurantsRevenue.put(rs.getString(12),
							restaurantsRevenue.get(rs.getString(12)) + rs.getFloat(13));

				else
					restaurantsRevenue.put(rs.getString(12), rs.getFloat(13));
			}

		}
		restaurantsRevenue.put(ReportNum, (float) 0);
		rs.close();
		ps.close();
		return restaurantsRevenue;
	}

	/**
	 * The role of the method is to insert the report file into the DATABASE as a
	 * BLOB file. For future use by the CEO
	 * 
	 * @param con         get connection to DB.
	 * @param inputStream get the file input.
	 * @param branch      get the relevant branch.
	 * @param quarter     get the relevant quarter.
	 */
	public static void updateFile(Connection con, InputStream inputStream, String branch, String quarter) {
		String sql = "INSERT INTO pdf_report (upload_file,branch,quarter) values(?,?,?);";
		try {
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setBlob(1, inputStream);
			statement.setString(2, branch);
			statement.setString(3, quarter);
			statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * The role of the method is to check if the report already exist in the
	 * DataBase.
	 * 
	 * @param con     get connection to DB.
	 * @param branch  get the relevant branch.
	 * @param quarter get the relevant quarter.
	 * @return quarter if already exist.
	 */
	public static String CheckIfFileExist(Connection con, String branch, String quarter) {
		try {
			StringBuilder str = new StringBuilder();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT * FROM pdf_report WHERE branch='" + branch + "' AND quarter='" + quarter + "';");
			while (rs.next()) {
				str.append(rs.getString(3) + "\t");
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * The role of the method is to get the report file from the DATABASE For use by
	 * the CEO.
	 * 
	 * @param con     get connection to DB.
	 * @param branch  get the relevant branch.
	 * @param quarter get the relevant quarter.
	 * @return the SerialBlob of png.
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static SerialBlob getFile(Connection con, String branch, String quarter)
			throws IOException, DocumentException {
		SerialBlob b = null;
		String sql = "SELECT * FROM pdf_report WHERE branch='" + branch + "' AND quarter='" + quarter + "';";
		try {

			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				b = new SerialBlob(rs.getBlob(1));
			}
			rs.close();
			return b;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}
}
