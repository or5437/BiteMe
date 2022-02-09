package server;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import com.itextpdf.text.DocumentException;

import controllers.DataBaseController;
import controllers.ServerPageController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import logic.ClientDetails;
import logic.MyFile;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the server.
 * 
 * @author Or Hilo, Michael Ravich, Raz Avraham, Amit Kempner, Tal Derai
 * @version Version 1.0 Build 100 December 29, 2021
 */
public class EchoServer extends AbstractServer {

	/**
	 * Clientlist: ObservableList of all connected clients.
	 */
	private static ObservableList<ClientDetails> Clientlist = FXCollections.observableArrayList();
	/**
	 * conn: variable of connection to DB.
	 */
	public static Connection conn; 
	/**
	 * DBname: Save the DataBase nane.
	 */
	private static String DBname;
	/**
	 * DBuser: Save the DataBase user.
	 */
	private static String DBuser;
	/**
	 * DBpass: Save the DataBase password. 
	 */
	private static String DBpass;

	// ************************ Constructors ************************

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port The port number to connect on.
	 * 
	 */
	public EchoServer(int port) {
		super(port);
	}

	/**
	 * setDBname: set DBname
	 * 
	 * @param dBname get the DB name
	 */
	public void setDBname(String dBname) {
		DBname = dBname;
	}

	/**
	 * setDBuser: set the DBuser
	 * 
	 * @param dBuser get the DB user
	 */
	public void setDBuser(String dBuser) {
		DBuser = dBuser;
	}

	/**
	 * setDBpass:set DBpass
	 * 
	 * @param dBpass get the DB password
	 */
	public void setDBpass(String dBpass) {
		DBpass = dBpass;
	}

	// ************************ Instance methods ************************

	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg    The message received from the client.
	 * @param client The connection from which the message originated.
	 */

	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		System.out.println("#Handle message from client#");
		int flag = 0;
		if (msg instanceof MyFile) {
			MyFile message = ((MyFile) msg);

			String LocalfilePath = message.getFileName();
			String[] a = LocalfilePath.split("\\\\");
			String strBranch = a[2].split("_")[0];
			String strQuarter = a[2].split("_")[1].split(" ")[0];
			MyFile new_file = new MyFile("new" + a[a.length - 1]);

			try {
				File newFile = new File(new_file.getFileName());
				FileOutputStream fos = new FileOutputStream(newFile);
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				InputStream is = new ByteArrayInputStream(((MyFile) msg).getMybytearray());
				DataBaseController.updateFile(conn, is, strBranch, strQuarter);
				bos.write(message.getMybytearray(), 0, message.getSize());
				client.sendToClient(new_file.getFileName());
				bos.close();
				fos.close();
				Path imagePath = Paths.get("C:\\CEOReports\\" + strBranch + "_" + strQuarter + " Quarter" + ".png");
				Files.deleteIfExists(imagePath);
				return;
			} catch (Exception e) {
				System.out.println("Error send (Files)msg) to Server");
			}
		}

		// System.out.println("Message received: " + msg + " from " + client);
		String[] details = ((String) msg).split("\\t"); // split by tab.
		String queryOutput = null;
		String ip; // for client IP.
		String[] clientDetails;

		switch (details[0]) {

		case "updateOrder:":
			DataBaseController.InsertRecordInOrderTbl(conn, details[1], details[2], details[3], details[4], details[5],
					details[6], details[7], details[8], details[9], details[10], details[11], details[12], details[13],
					details[14]);
			queryOutput = "true";
			try {
				client.sendToClient("update:" + "\t" + queryOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "updateShipment:":
			String shipmentOrderNumber = DataBaseController.RetrunLastRecordFromOrdersTbl(conn);
			DataBaseController.InsertRecordInShipmentTbl(conn, shipmentOrderNumber, details[1], details[2], details[3],
					details[4], details[5], details[6]);
			queryOutput = "true";
			try {
				client.sendToClient("update:" + "\t" + queryOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "connection details:":
			clientDetails = client.toString().split(" ");
			ip = clientDetails[1].substring(1, clientDetails[1].length() - 1);
			Clientlist.add(new ClientDetails(ip, clientDetails[0], "Connected"));
			try {
				client.sendToClient("Connected");
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "disconnected:":
			clientDetails = client.toString().split(" ");
			ip = clientDetails[1].substring(1, clientDetails[1].length() - 1);
			int index = findClientInListByIp(ip);
			Clientlist.remove(index);
			try {
				client.sendToClient("Disconnected");
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "cmbRestaurant:":
			queryOutput = DataBaseController.RetrunAllRestaurant(conn, details[1]);
			if (queryOutput != null) {
				// System.out.println("Server Found");
				try {
					client.sendToClient("cmbRestaurant:" + "\t" + queryOutput);
				} catch (IOException e) {
					e.printStackTrace();
				}
				flag = 1;
			}
			break;

		case "cmbDish:":
			queryOutput = DataBaseController.getDishByRestaurantAndByItem(conn, details[1], details[2]);
			if (queryOutput != null) {
				// System.out.println("Server Found");
				try {
					client.sendToClient("cmbDish:" + "\t" + queryOutput);
				} catch (IOException e) {
					e.printStackTrace();
				}
				flag = 1;
			}
			break;

		case "donenessValid:":
			queryOutput = DataBaseController.getValidDonenessByDish(conn, details[1]);
			if (queryOutput != null) {
				// System.out.println("Server Found");
				try {
					client.sendToClient("donenessValid:" + "\t" + queryOutput);
				} catch (IOException e) {
					e.printStackTrace();
				}
				flag = 1;
			}
			break;

		case "sizeValid:":
			queryOutput = DataBaseController.getValidSizeByDish(conn, details[1]);
			if (queryOutput != null) {
				// System.out.println("Server Found");
				try {
					client.sendToClient("sizeValid:" + "\t" + queryOutput);
				} catch (IOException e) {
					e.printStackTrace();
				}
				flag = 1;
			}
			break;

		case "login:":
			System.out.println(details[1] + " Connected to the system");
			queryOutput = DataBaseController.ReturnPermissionIfUserExist(conn, details[1], details[2]);
			if (queryOutput != null) {
				// User found
				try {
					DataBaseController.UpdateLoggedUsersTBL(conn, details[1], true); // update is_logged_in field
					client.sendToClient("login" + "\t" + queryOutput); // send permission of user to client
				} catch (IOException e) {
					e.printStackTrace();

				}
				flag = 1;
			}
			break;

		case "logout:":
			System.out.println(details[1] + " Disconnected from the system");
			DataBaseController.UpdateLoggedUsersTBL(conn, details[1], false);
			try {
				client.sendToClient("logout");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "update details:":
			if (details[2].equals("Remove")) {
				DataBaseController.RemoveUserTBL(conn, details[1], details[2], Integer.parseInt(details[3]),
						details[4]);
			} else
				DataBaseController.UpdateDetailsUserTBL(conn, details[1], details[2]);
			details[0] = "get user details:";
			flag = 1;

		case "get user details:":
			List<String> retList = new ArrayList<>();
			retList.add("get user details");
			retList.addAll(DataBaseController.getUserDetails(conn));
			try {
				client.sendToClient(retList);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			flag = 1;
			break;

		case "getResName:":
			List<String> restaurantsList = new ArrayList<>();
			restaurantsList.add("restaurantName");
			restaurantsList.add(DataBaseController.ReturnRestaurantName(conn, details[1]));
			try {
				client.sendToClient(restaurantsList);
			} catch (IOException e1) { // TODO Auto-generated catch block
				e1.printStackTrace();
			} // send permission of user to client
			flag = 1;
			break;

		case "BusinessRegistration:":
			if (DataBaseController.AddNewBusiness(conn, details[1], details[2], details[3], details[4])) {
				flag = 1;
				try {
					client.sendToClient("BusinessRegistration:");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			break;

		case "AddDish:":
			if (DataBaseController.AddNewDish(conn, details[1], details[2], Float.parseFloat(details[3]),
					Boolean.parseBoolean(details[4]), Boolean.parseBoolean(details[5]), Integer.parseInt(details[6]))) {
				flag = 1;
				try {
					client.sendToClient("AddDish:");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			break;

		case "getDishes:":
			queryOutput = DataBaseController.getDishByRestaurantAndByItem(conn, Integer.parseInt(details[1]),
					details[2]);
			if (queryOutput != null) {
				// System.out.println("Server Found");
				try {
					client.sendToClient("getDishes:" + "\t" + queryOutput);
				} catch (IOException e) {
					e.printStackTrace();
				}
				flag = 1;
			}
			break;

		case "getDishDetails:":
			queryOutput = DataBaseController.getDishDetailsByNameAndRes(conn, details[1], Integer.parseInt(details[2]));
			if (queryOutput != null) {
				// System.out.println("Server Found");
				try {
					client.sendToClient("getDishDetails:" + "\t" + queryOutput);
				} catch (IOException e) {
					e.printStackTrace();
				}
				flag = 1;
			}
			break;

		case "UpdateDish:":
			DataBaseController.UpdateDishDetails(conn, details[1], Integer.parseInt(details[2]),
					Float.parseFloat(details[3]), Boolean.parseBoolean(details[4]), Boolean.parseBoolean(details[5]));
			flag = 1;
			try {
				client.sendToClient("UpdateDish:");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;

		case "DeleteDish:":
			DataBaseController.DeleteDish(conn, details[1], Integer.parseInt(details[2]));
			flag = 1;
			try {
				client.sendToClient("DeleteDish");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;

		case "AddPrivateCustomer:":
			DataBaseController.UpdatePrivateCustomerDetails(conn, details[1], details[2], details[3], details[4],
					details[5], details[6]);
			flag = 1;
			try {
				client.sendToClient("PrivateCustomerAdded");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			break;
		case "AddBusinessCustomer:":
			DataBaseController.UpdateBusinessCustomerDetails(conn, details[1], details[2], details[3], details[4],
					details[5], details[6], details[7], Float.parseFloat(details[8]));
			flag = 1;
			try {
				client.sendToClient("BusinessCustomerAdded");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;

		case "AddSupplier:":
			DataBaseController.UpdateSupplierDetails(conn, details[1], details[2], details[3], details[4]);
			flag = 1;
			try {
				client.sendToClient("SupplierAdded");
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			break;

		case "getEmployers:":
			List<String> employersList = new ArrayList<>();
			employersList.add("employerName");
			employersList.add(DataBaseController.ReturnEmployersName(conn));
			try {
				client.sendToClient(employersList);
			} catch (IOException e1) { // TODO Auto-generated catch block
				e1.printStackTrace();
			} // send permission of user to client
			flag = 1;
			break;

		case "getBudget:":
			String budget = DataBaseController.ReturnBudget(conn, details[1]);
			try {
				client.sendToClient("updateBudget:" + "\t" + budget);
			} catch (IOException e1) { // TODO Auto-generated catch block
				e1.printStackTrace();
			} // send permission of user to client
			flag = 1;
			break;

		case "getResID:":
			String resID = DataBaseController.ReturnRestaurantID(conn, details[1], details[2]);
			try {
				client.sendToClient("updateResID:" + "\t" + resID);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			flag = 1;
			break;

		case "getReportDetails:":
			ArrayList<String> DataResult = new ArrayList<>();
			Float revfromMainDishs = (float) 0;
			Float revfromFirstDishs = (float) 0;
			Float revFromDrinks = (float) 0;
			Float revFromDesserts = (float) 0;
			Integer numOfMainDishs = 0;
			Integer numOfFirstDishs = 0;
			Integer numOfDrinks = 0;
			Integer numOfDesserts = 0;
			Integer totalNumOfOrders = 0;
			Integer totalNumOfDelays = 0;
			DataResult = DataBaseController.ReturnSumOfFinalPriceAndOrdersID(conn, details[1], details[2], details[3],
					details[4]);
			// DataResult[0] = SumOfFinalPrice,other index's: order IDS for relevant branch

			// sum all data by relevant order IDS
			for (int i = 1; i < DataResult.size(); i++) {
				revfromMainDishs += DataBaseController.ReturnRevFromItemType(conn, DataResult.get(i), "Main Dish");
				revfromFirstDishs += DataBaseController.ReturnRevFromItemType(conn, DataResult.get(i), "First Dish");
				revFromDrinks += DataBaseController.ReturnRevFromItemType(conn, DataResult.get(i), "Drink");
				revFromDesserts += DataBaseController.ReturnRevFromItemType(conn, DataResult.get(i), "Dessert");
				numOfMainDishs += DataBaseController.ReturnNumOfItemType(conn, DataResult.get(i), "Main Dish");
				numOfFirstDishs += DataBaseController.ReturnNumOfItemType(conn, DataResult.get(i), "First Dish");
				numOfDrinks += DataBaseController.ReturnNumOfItemType(conn, DataResult.get(i), "Drink");
				numOfDesserts += DataBaseController.ReturnNumOfItemType(conn, DataResult.get(i), "Dessert");
				totalNumOfDelays += DataBaseController.ReturnNumOfDelays(conn, DataResult.get(i));
			}

			totalNumOfOrders = DataBaseController.ReturnNumOfOrders(conn, details[1], details[2]);

			try {
				client.sendToClient("updateReportDetails:" + "\t" + DataResult.get(0) + "\t" + revfromMainDishs + "\t"
						+ revfromFirstDishs + "\t" + revFromDrinks + "\t" + revFromDesserts + "\t" + numOfMainDishs
						+ "\t" + numOfFirstDishs + "\t" + numOfDrinks + "\t" + numOfDesserts + "\t" + totalNumOfOrders
						+ "\t" + totalNumOfDelays);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			flag = 1;
			break;

		case "FindResID:":
			Integer resId = DataBaseController.findSupplierRestaurant(conn, Integer.parseInt(details[1]));
			flag = 1;
			try {
				client.sendToClient("FindResID:" + "\t" + resId);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			break;

		case "FindBusinessName:":
			String businessName = DataBaseController.findHrBusiness(conn, Integer.parseInt(details[1]));
			if (!businessName.equals(""))
				flag = 1;
			try {
				client.sendToClient("FindBusinessName:" + "\t" + businessName);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			break;

		case "CheckIfBusinessReg:":
			boolean businessFlag = DataBaseController.checkIfRecordExist(conn, details[1]);
			flag = 1;
			try {
				client.sendToClient("CheckIfBusinessReg:" + "\t" + businessFlag);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			break;

		case "getCustomerDetails:":
			queryOutput = DataBaseController.getCustomerDetails(conn, details[1]);
			try {
				client.sendToClient("getCustomerDetails:" + "\t" + queryOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "updateDishInOrder:":
			String DishInOrderNumber = DataBaseController.RetrunLastRecordFromOrdersTbl(conn);
			DataBaseController.InsertRecordToDishInOrderTbl(conn, DishInOrderNumber, details[1], details[2], details[3],
					details[4], details[5], details[6], details[7], details[8]);
			queryOutput = "true";
			try {
				client.sendToClient("updateDishInOrder:" + "\t" + queryOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "idNumberPcustomerExist:":
			String strMsgSuccessP = "false";
			queryOutput = DataBaseController.checkIdNumberExistInPrivateCustomerTable(conn, details[1]);
			if (queryOutput != "")
				strMsgSuccessP = "true";
			try {
				client.sendToClient("idNumberPcustomerExist:" + "\t" + strMsgSuccessP);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "idNumberBcustomerExist:":
			String strMsgSuccessB = "false";
			queryOutput = DataBaseController.checkIdNumberExistInBusinessCustomerTable(conn, details[1]);
			if (queryOutput != "")
				strMsgSuccessB = "true";
			try {
				client.sendToClient("idNumberBcustomerExist:" + "\t" + strMsgSuccessB);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "getMonthlyBudget:":
			queryOutput = DataBaseController.getMonthlyBudgetByIdNumberFromBusinessCustomerTable(conn, details[1]);
			try {
				client.sendToClient("getMonthlyBudget:" + "\t" + queryOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "updateMonthlyBudget:":
			DataBaseController.updateMonthlyBudgetByIdNumberFromBusinessCustomerTable(conn, details[1], details[2]);
			try {
				client.sendToClient("updateMonthlyBudget:" + "\t" + "true");
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "getLastOrderNumber:":
			queryOutput = DataBaseController.RetrunLastRecordFromOrdersTbl(conn);
			try {
				client.sendToClient("getLastOrderNumber:" + "\t" + queryOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "msgToSupplier:":
			queryOutput = DataBaseController.RetrunLastRecordFromOrdersTbl(conn);
			DataBaseController.InsertRecordMsgToSupplierTable(conn, queryOutput, details[1], details[2], details[3],
					details[4], details[5], details[6], details[7]);
			try {
				client.sendToClient("msgToSupplier:" + "\t" + "true");
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "msgToSupplierFromBiteMe:":
			DataBaseController.InsertRecordMsgToSupplierTable(conn, details[1], details[2], details[3], details[4],
					details[5], details[6], details[7], details[8]);
			try {
				client.sendToClient("msgToSupplierFromBiteMe:" + "\t" + "true");
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "checkIfAlreadyGotMonthlyBill:":
			queryOutput = DataBaseController.checkIfAlreadyGotMonthlyBill(conn, details[1], details[2]);
			if (queryOutput.equals(""))
				queryOutput = "true";
			else
				queryOutput = "false";
			try {
				client.sendToClient("checkIfAlreadyGotMonthlyBill:" + "\t" + queryOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "getRestaurantId:":
			queryOutput = DataBaseController.getRestaurantId(conn, details[1], details[2]);
			try {
				client.sendToClient("getRestaurantId:" + "\t" + queryOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "getSupplierId:":
			queryOutput = DataBaseController.getSupplierId(conn, details[1]);
			try {
				client.sendToClient("getSupplierId:" + "\t" + queryOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "getMsgToSupplier:":
			queryOutput = DataBaseController.getMsgToSupplier(conn, details[1]);
			try {
				client.sendToClient("getMsgToSupplier:" + "\t" + queryOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "updateMsgSupplier:":
			DataBaseController.updateSupplierMsg(conn, details[1], details[2], details[3]);
			try {
				client.sendToClient("updateMsgSupplier:" + "\t" + "true");
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "removeMsgSupplier:":
			DataBaseController.removeMsgSupplier(conn);
			try {
				client.sendToClient("removeMsgSupplier:" + "\t" + "true");
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "msgSupplierRead:":
			DataBaseController.setMsgSupplierRead(conn, details[1]);
			try {
				client.sendToClient("msgSupplierRead:" + "\t" + "true");
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "getNumberSupplierMsgNotRead:":
			queryOutput = DataBaseController.getCountMsgSupplierNotRead(conn, details[1]);
			try {
				client.sendToClient("getNumberSupplierMsgNotRead:" + "\t" + queryOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "msgToCustomer:":
			DataBaseController.InsertRecordMsgToCustomerTable(conn, details[1], details[2], details[3], details[4],
					details[5], details[6], details[7], details[8]);
			try {
				client.sendToClient("msgToCustomer:" + "\t" + "true");
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "getMsgToCustomer:":
			queryOutput = DataBaseController.getMsgToCustomer(conn, details[1]);
			try {
				client.sendToClient("getMsgToCustomer:" + "\t" + queryOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "removeMsgCustomer:":
			DataBaseController.removeMsgCustomer(conn);
			try {
				client.sendToClient("removeMsgCustomer:" + "\t" + "true");
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "msgCustomerRead:":
			DataBaseController.setMsgCustomerRead(conn, details[1]);
			try {
				client.sendToClient("msgCustomerRead:" + "\t" + "true");
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "getNumberCustomerMsgNotRead:":
			queryOutput = DataBaseController.getCountMsgCustomerNotRead(conn, details[1]);
			try {
				client.sendToClient("getNumberCustomerMsgNotRead:" + "\t" + queryOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "updateMsgCustomer:":
			DataBaseController.updateCustomerMsg(conn, details[1], details[2]);
			try {
				client.sendToClient("updateMsgCustomer:" + "\t" + "true");
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "InsertSupplierTimeApprove:":
			DataBaseController.InsertSupplierTimeApprove(conn, details[1], details[2]);
			try {
				client.sendToClient("InsertSupplierTimeApprove:" + "\t" + "true");
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "InsertCustomerTimeApprove:":
			DataBaseController.InsertCustomerTimeApprove(conn, details[1], details[2], details[3]);
			try {
				client.sendToClient("InsertCustomerTimeApprove:" + "\t" + "true");
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "getIsPreOrder:":
			queryOutput = DataBaseController.getIsPreOrder(conn, details[1]);
			try {
				client.sendToClient("getIsPreOrder:" + "\t" + queryOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "getSupplierApproveHour:":
			queryOutput = DataBaseController.getSupplierApproveHour(conn, details[1]);
			try {
				client.sendToClient("getSupplierApproveHour:" + "\t" + queryOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "getCustomerApproveHour:":
			queryOutput = DataBaseController.getCustomerApproveHour(conn, details[1]);
			try {
				client.sendToClient("getCustomerApproveHour:" + "\t" + queryOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "getBranchAndRestaurantAndFinalPriceAndUserId:":
			queryOutput = DataBaseController.getBranchAndRestaurantAndFinalPriceAndUserId(conn, details[1]);
			try {
				client.sendToClient("getBranchAndRestaurantAndFinalPriceAndUserId:" + "\t" + queryOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "UpdateRefund:":
			DataBaseController.UpdateRecordRefundTable(conn, details[1], details[2], details[3], details[4]);
			try {
				client.sendToClient("UpdateRefund:" + "\t" + queryOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "SetNewRefund:":
			DataBaseController.InsertRecordRefundTable(conn, details[1], details[2], details[3], details[4]);
			try {
				client.sendToClient("SetNewRefund:" + "\t" + queryOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;
		case "ConfirmOrder:":
			DataBaseController.setConfirmOrder(conn, details[1]);
			try {
				client.sendToClient("ConfirmOrder:" + "\t" + "true");
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "getDueHour:":
			queryOutput = DataBaseController.getDueHour(conn, details[1]);
			try {
				client.sendToClient("getDueHour:" + "\t" + queryOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "getDueDate:":
			queryOutput = DataBaseController.getDueDate(conn, details[1]);
			try {
				client.sendToClient("getDueDate:" + "\t" + queryOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "getCustomerApproveDate:":
			queryOutput = DataBaseController.getCustomerApproveDate(conn, details[1]);
			try {
				client.sendToClient("getCustomerApproveDate:" + "\t" + queryOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;
		case "getPriceRefundIfExist:":
			queryOutput = DataBaseController.getPriceRefundIfExist(conn, details[1], details[2], details[3]);
			try {
				client.sendToClient("getPriceRefundIfExist:" + "\t" + queryOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;
		case "removeRefund:":
			DataBaseController.removeRefund(conn, details[1], details[2], details[3]);
			try {
				client.sendToClient("removeRefund:" + "\t" + "true");
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;
		case "InsertOrderInRefund:":
			DataBaseController.InsertOrderInRefund(conn, details[1]);
			try {
				client.sendToClient("InsertOrderInRefund:" + "\t" + "false");
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;
		case "UpdateOrderInRefund:":
			DataBaseController.UpdateOrderInRefund(conn, details[1]);
			try {
				client.sendToClient("UpdateOrderInRefund:" + "\t" + "true");
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;
		case "presentDishes:":
			queryOutput = DataBaseController.getDishInOrder(conn, details[1]);
			try {
				client.sendToClient("presentDishes:" + "\t" + queryOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "countInDish:":
			queryOutput = DataBaseController.getCountDishInOrder(conn, details[1]);
			try {
				client.sendToClient("countInDish:" + "\t" + queryOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;
		case "updateSupplierMsgFromCustomer:":
			DataBaseController.updateSupplierMsgFromCustomer(conn, details[1], details[2], details[3], details[4]);
			try {
				client.sendToClient("updateSupplierMsgFromCustomer:" + "\t" + "true");
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "getW4CcodePrivate:":
			queryOutput = DataBaseController.getW4CcodePrivate(conn, details[1]);
			try {
				client.sendToClient("getW4CcodePrivate:" + "\t" + queryOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "getBusinessName:":
			queryOutput = DataBaseController.getBusinessName(conn, details[1]);
			try {
				client.sendToClient("getBusinessName:" + "\t" + queryOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "getW4CcodeBusiness:":
			queryOutput = DataBaseController.getW4CcodeBusiness(conn, details[1]);
			try {
				client.sendToClient("getW4CcodeBusiness:" + "\t" + queryOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "ChangeUserType:":
			DataBaseController.changeUserType(conn, details[1], details[2]);
			try {
				client.sendToClient("ChangeUserType:" + "\t" + "true");
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "AddHrManager:":
			DataBaseController.addNewHr(conn, details[1], details[2]);
			try {
				client.sendToClient("AddHrManager:" + "\t" + "true");
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "msgToManager:":
			DataBaseController.InsertRecordMsgToManagerTable(conn, details[1], details[2], details[3], details[4],
					details[5], details[6], details[7]);
			try {
				client.sendToClient("msgToManager:" + "\t" + "true");
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "msgToHR:":
			DataBaseController.InsertRecordMsgToHrTable(conn, details[1], details[2], details[3], details[4],
					details[5], details[6], details[7]);
			try {
				client.sendToClient("msgToHR:" + "\t" + "true");
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "getMsgToManager:":
			queryOutput = DataBaseController.getMsgToManager(conn, details[1]);
			try {
				client.sendToClient("getMsgToManager:" + "\t" + queryOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "getMsgToHr:":
			queryOutput = DataBaseController.getMsgToHr(conn, details[1]);
			try {
				client.sendToClient("getMsgToHr:" + "\t" + queryOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "getMsgToCeo:":
			queryOutput = DataBaseController.getMsgToCeo(conn, details[1]);
			try {
				client.sendToClient("getMsgToCeo:" + "\t" + queryOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "getNumberCeoMsgNotRead:":
			queryOutput = DataBaseController.getCountMsgCeoNotRead(conn, details[1]);
			try {
				client.sendToClient("getNumberCeoMsgNotRead:" + "\t" + queryOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "getNumberManagerMsgNotRead:":
			queryOutput = DataBaseController.getCountMsgManagerNotRead(conn, details[1]);
			try {
				client.sendToClient("getNumberManagerMsgNotRead:" + "\t" + queryOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "getNumberHrMsgNotRead:":
			queryOutput = DataBaseController.getCountMsgHrNotRead(conn, details[1]);
			try {
				client.sendToClient("getNumberHrMsgNotRead:" + "\t" + queryOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "msgManagerRead:":
			DataBaseController.setMsgManagerRead(conn, details[1]);
			try {
				client.sendToClient("msgManagerRead:" + "\t" + "true");
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "msgCeoRead:":
			DataBaseController.setMsgCeoRead(conn, details[1]);
			try {
				client.sendToClient("msgCeoRead:" + "\t" + "true");
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "removeMsgCeo:":
			DataBaseController.removeMsgCeo(conn);
			try {
				client.sendToClient("removeMsgCeo:" + "\t" + "true");
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "msgToCeo:":
			DataBaseController.InsertRecordMsgToCeoTable(conn, details[1], details[2], details[3], details[4],
					details[5], details[6], details[7]);
			try {
				client.sendToClient("msgToCeo:" + "\t" + "true");
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "msgHrRead:":
			DataBaseController.setMsgHrRead(conn, details[1]);
			try {
				client.sendToClient("msgHrRead:" + "\t" + "true");
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "removeMsgManager:":
			DataBaseController.removeMsgManager(conn);
			try {
				client.sendToClient("removeMsgManager:" + "\t" + "true");
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "removeMsgHr:":
			DataBaseController.removeMsgHr(conn);
			try {
				client.sendToClient("removeMsgHr:" + "\t" + "true");
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "updateHrMsgFromManager:":
			DataBaseController.updateHrMsgFromManager(conn, details[1], details[2], details[3], details[4]);
			try {
				client.sendToClient("updateHrMsgFromManager:" + "\t" + "true");
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "updateManagerMsgFromHr:":
			DataBaseController.updateManagerMsgFromHr(conn, details[1], details[2], details[3], details[4]);
			try {
				client.sendToClient("updateManagerMsgFromHr:" + "\t" + "true");
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "updateMsgManager:":
			DataBaseController.updateManagerMsg(conn, details[1], details[2]);
			try {
				client.sendToClient("updateMsgManager:" + "\t" + "true");
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;
		case "updateMsgHr:":
			DataBaseController.updateHrMsg(conn, details[1], details[2]);
			try {
				client.sendToClient("updateMsgHr:" + "\t" + "true");
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;
		case "getTypeOfOrder:":
			queryOutput = DataBaseController.getTypeOfOrder(conn, details[1]);
			try {
				client.sendToClient("getTypeOfOrder:" + "\t" + queryOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;
		case "WaitingBusinessRegistration:":
			if (DataBaseController.AddNewWaitingBusiness(conn, details[1], details[2], details[3], details[4])) {
				flag = 1;
				try {
					client.sendToClient("WaitingBusinessRegistration:");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			break;

		case "findBusinessNameByHr:":
			queryOutput = DataBaseController.findBusinessNameByHr(conn, details[1]);
			try {
				client.sendToClient("findBusinessNameByHr:" + "\t" + queryOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "UpdateWaitingBusConfirm:":
			DataBaseController.UpdateWaitingBusConfirm(conn, details[1]);
			try {
				client.sendToClient("UpdateWaitingBusConfirm:");
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "takeBusData:":
			queryOutput = DataBaseController.takeBusData(conn, details[1]);
			try {
				client.sendToClient("takeBusData:" + "\t" + queryOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "findHrByBusinessName:":
			queryOutput = DataBaseController.findHrByBusinessName(conn, details[1]);
			try {
				client.sendToClient("findHrByBusinessName:" + "\t" + queryOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "InsertWaitingBusinessCus:":
			DataBaseController.InsertWaitingBusinessCus(conn, details[1], details[2], details[3], details[4],
					details[5], details[6], details[7], details[8]);
			try {
				client.sendToClient("InsertWaitingBusinessCus:");
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "insertIntoBusinessCustomer:":
			DataBaseController.insertIntoBusinessCustomer(conn, details[1]);
			try {
				client.sendToClient("insertIntoBusinessCustomer:");
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "getOrderDetailsByMonth:":
			queryOutput = DataBaseController.getOrderDetailsByMonth(conn, details[1]);
			try {
				client.sendToClient("getOrderDetailsByMonth:" + "\t" + queryOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "getFromDishInOrderMonthDishes:":
			queryOutput = DataBaseController.getFromDishInOrderMonthDishes(conn, details[1]);
			try {
				client.sendToClient("getFromDishInOrderMonthDishes:" + "\t" + queryOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "setComission:":
			DataBaseController.setComission(conn, details[1]);
			try {
				client.sendToClient("setComission:");
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;
		case "getDetailsForCEOreport:":
			HashMap<String, Integer> restaurantsANDorders;
			HashMap<String, Float> restaurantsANDrevenues;
			try {

				restaurantsANDorders = DataBaseController.getDetailsForCEOreportOrders(conn, details[1], details[2],
						details[3]);
				restaurantsANDrevenues = DataBaseController.getDetailsForCEOreportRevenues(conn, details[1], details[2],
						details[3]);

				client.sendToClient(restaurantsANDorders);
				client.sendToClient(restaurantsANDrevenues);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "getAllSupplierId:":
			queryOutput = DataBaseController.getAllSupplierId(conn);
			try {
				client.sendToClient("getAllSupplierId:" + "\t" + queryOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "CheckIfFileExist:":
			queryOutput = DataBaseController.CheckIfFileExist(conn, details[1], details[2]);
			if (!queryOutput.equals(""))
				queryOutput = "true";
			else
				queryOutput = "false";
			try {
				client.sendToClient("CheckIfFileExist:" + "\t" + queryOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "getFile:":
			try {
				try {
					SerialBlob b = DataBaseController.getFile(conn, details[1], details[2]);
					client.sendToClient(b);
				} catch (DocumentException e) {
					e.printStackTrace();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			flag = 1;
			break;

		case "getRestaurantIdByBranch:":
			queryOutput = DataBaseController.getRestaurantIdByBranch(conn, details[1]);
			if (!queryOutput.equals(""))
				queryOutput = "true";
			else
				queryOutput = "false";
			try {
				client.sendToClient("getRestaurantIdByBranch:" + "\t" + queryOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;

		case "msgSupplierReadBill:":
			DataBaseController.msgSupplierReadBill(conn, details[1], details[2]);
			try {
				client.sendToClient("msgSupplierReadBill:" + "\t" + "true");
			} catch (IOException e) {
				e.printStackTrace();
			}
			flag = 1;
			break;
		}

		if (flag != 1)

		{
			try {
				client.sendToClient("-1"); // send -1 in case there is no appropriate message.
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * @param String ip This method return the index of client.
	 * 
	 * @return int index of client
	 */
	private int findClientInListByIp(String ip) {
		int index = 0;
		for (ClientDetails cd : Clientlist) {
			if ((cd.getIpAddress()).equals(ip))
				return index;
			index++;
		}
		return -1;
	}

	/**
	 * for pass the client Details to Connection table.
	 * 
	 * @return list of clients
	 */
	public static ObservableList<ClientDetails> getClientlist() {
		return Clientlist;
	}

	/**
	 * Connection to DataBase.
	 */
	public static void connectToDB() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			// System.out.println("Driver definition succeed");
		} catch (Exception ex) {
			// System.out.println("Driver definition failed");
		}
		try {
			conn = DriverManager.getConnection(DBname, DBuser, DBpass);
			ServerPageController.mainThread.interrupt();

			// System.out.println("SQL connection succeed");
		} catch (SQLException ex) {
			ServerPageController.mainThread.interrupt();

			System.out.println("There is Error in data");
			ServerUI.sv.stopListening();

			// System.out.println("SQLException: " + ex.getMessage());
			// System.out.println("SQLState: " + ex.getSQLState());
			// System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		connectToDB();
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {

	}
}