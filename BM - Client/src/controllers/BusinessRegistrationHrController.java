package controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import client.ChatClient;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.MsgToManager;

/**
 * class for business registration by HR.
 * 
 * @author Raz Avraham
 * @version 1.0 Build 100 December 27, 2021
 */
public class BusinessRegistrationHrController {

	private MsgToManager mtm = new MsgToManager(0, 0, null, null, null, null, false);

	@FXML
	private Button btnBack;

	@FXML
	private Label lblErrorReg;

	@FXML
	private TextField txtBusinessAdd;

	@FXML
	private TextField txtBusinessPhone;

	@FXML
	private TextField txtBusinessBudget;

	@FXML
	private TextField txtBusinessName;

	private String getBusinessAdd() {
		return txtBusinessAdd.getText();
	}

	private String getBusinessPhone() {
		return txtBusinessPhone.getText();
	}

	private String getBusinessBudget() {
		return txtBusinessBudget.getText();
	}

	/**
	 * first data load for page
	 * 
	 * @param businessName get the business name of the current HR.
	 */
	public void loadData(String businessName) {
		txtBusinessName.setText(businessName);
		txtBusinessName.setDisable(true);
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
		Pane root = loader.load(getClass().getResource("/controllers/HrPage.fxml").openStream());
		HrController hrController = loader.getController();
		hrController.loadData(String.valueOf(ChatClient.u1.getIdNumber()));
		Scene scene = new Scene(root);
		primaryStage.setTitle("HR Manager");
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Click On Submit button, check input validation and send registration request
	 * to the branch manager.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void btnSubmitBusinessRegistration(ActionEvent event) throws IOException {
		if (getBusinessAdd().equals(""))
			lblErrorReg.setText("Enter business address");
		else if (getBusinessPhone().equals(""))
			lblErrorReg.setText("Enter business phone");
		else if (getBusinessBudget().equals(""))
			lblErrorReg.setText("Enter business budget");
		else if (!getBusinessBudget().matches("[0-9]+")) {
			lblErrorReg.setText("must contain just numbers!");
		}

		else {
			ClientUI.chat
					.accept("WaitingBusinessRegistration:" + "\t" + HrController.businessName + "\t" + getBusinessAdd()

							+ "\t" + getBusinessPhone() + "\t" + getBusinessBudget());

			if (ChatClient.flag == 1) {
				lblErrorReg.setText("Business allready registered!");
				ChatClient.flag = 0;
			} else {
				lblErrorReg.setText("Request has been sent to branch manager!");

				setApprovedMsgToManagerData();
				String msgToManager = "msgToManager:" + "\t" + String.valueOf(mtm.getFromUserId()) + "\t"
						+ String.valueOf(mtm.getToUserId()) + "\t" + mtm.getDate() + "\t" + mtm.getHour() + "\t"
						+ mtm.getMessage() + "\t" + mtm.getStatus() + "\t" + String.valueOf(mtm.isRead());
				ClientUI.chat.accept(msgToManager);
			}
		}

	}

	/**
	 * set a massage to the branch manager.
	 * 
	 */
	public void setApprovedMsgToManagerData() {
		mtm.setFromUserId(Integer.parseInt(HrController.userID));
		if (txtBusinessName.getText().contains("North"))
			mtm.setToUserId(101);
		else if (txtBusinessName.getText().contains("Central"))
			mtm.setToUserId(102);
		else if (txtBusinessName.getText().contains("South"))
			mtm.setToUserId(103);
		String changeFormatDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		mtm.setDate(changeFormatDate.toString());
		mtm.setHour(LocalTime.now().getHour() + ":" + LocalTime.now().getMinute());
		mtm.setMessage("The HR of " + HrController.businessName + " request register the business!");
		mtm.setStatus("Waiting");
		mtm.setRead(false);
	}
}
