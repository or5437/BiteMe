package controllers;

import java.io.IOException;

import client.ChatClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * MsgController implements all alerts at BiteMe System.
 * 
 * @author Amit Kempner
 * @version 1.0 Build 100 December 27, 2021
 */
public class MsgController {

	/** primaryStage: using to open new Stage. */
	Stage primaryStage;
	/** loader: using to open new FXMLLoader. */
	FXMLLoader loader;
	/** root: using to load the appropriate page. */
	Pane root;
	/** scene: using to open new scene. */
	Scene scene;
	/** id: to identify which message is it (switch case). */
	private static int id;

	@FXML
	private Button btnOK;

	@FXML
	private Text txtMsg;

	/**
	 * loadData: Initialize details.
	 * 
	 * @param str:   Appropriate message string.
	 * @param MsgId: Message id.
	 * @throws IOException
	 */
	public void loadData(String str, int MsgId) {
		txtMsg.setText(str);
		id = MsgId;
	}

	/**
	 * selectOK: func that move the user to the apprpriate page and present a
	 * messgae accordingly.
	 * 
	 * @param event If user press on OK button he move to the appropriate page by
	 *              switch case.
	 * @throws IOException
	 */
	@FXML
	void selectOK(ActionEvent event) throws IOException {
		switch (id) {
		case 1:
			((Node) event.getSource()).getScene().getWindow().hide(); // hide window
			loader = new FXMLLoader();
			primaryStage = new Stage();
			root = loader.load(getClass().getResource("/controllers/PaymentPage.fxml").openStream());
			PaymentController paymentController = loader.getController();
			paymentController.loadData();
			scene = new Scene(root);
			primaryStage.setTitle("Payment method");
			primaryStage.setOnCloseRequest(event1 -> event1.consume());
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.show();
			break;

		case 2:
			((Node) event.getSource()).getScene().getWindow().hide(); // hide window
			loader = new FXMLLoader();
			primaryStage = new Stage();
			root = loader.load(getClass().getResource("/controllers/CustomerPage.fxml").openStream());
			scene = new Scene(root);
			if (ChatClient.u1.getType().equals("Private_Customer")) {
				CustomerController pCustomerController = loader.getController();
				pCustomerController.loadData(ChatClient.u1.getType(), String.valueOf(ChatClient.u1.getIdNumber()));
			} else {
				CustomerController bCustomerController = loader.getController();
				bCustomerController.loadData(ChatClient.u1.getType(), String.valueOf(ChatClient.u1.getIdNumber()));
			}
			primaryStage.setTitle(ChatClient.u1.getType().split("_")[0] + " " + ChatClient.u1.getType().split("_")[1]);
			primaryStage.setResizable(false);
			primaryStage.setOnCloseRequest(event1 -> event1.consume());
			primaryStage.setScene(scene);
			primaryStage.show();
			break;

		case 3:
			((Node) event.getSource()).getScene().getWindow().hide(); // hide window
			loader = new FXMLLoader();
			primaryStage = new Stage();
			root = loader.load(getClass().getResource("/controllers/BmManagerPage.fxml").openStream());
			scene = new Scene(root);
			BmManagerController bmManagerController = loader.getController();
			primaryStage.setResizable(false);
			primaryStage.setOnCloseRequest(event1 -> event1.consume());
			bmManagerController.loadData(String.valueOf(ChatClient.u1.getIdNumber()));
			primaryStage.setTitle("Branch Manager");
			primaryStage.setScene(scene);
			primaryStage.show();
			break;
		case 4:
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			loader = new FXMLLoader();
			primaryStage = new Stage();
			primaryStage.setResizable(false);
			primaryStage.setOnCloseRequest(event1 -> event1.consume());
			primaryStage.setTitle("Generate Quarterly Report");
			root = loader.load(getClass().getResource("/controllers/GenerateQuarterlyReportPage.fxml").openStream());
			scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();
			break;
		}
	}

}
