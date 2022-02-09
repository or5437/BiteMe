package controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.sql.rowset.serial.SerialBlob;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import client.ChatClient;
import client.ClientUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import java.awt.Desktop;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Description of CeoPresentQuarterlyReportsController The class is designed to
 * control the presentation of the quarterly reports to the CEO, and to make a
 * comparison between 2 of them if necessary.
 * 
 * @author Michael Ravich
 * @version Version 1.0 Build 100 December 27, 2021
 */
public class CeoPresentQuarterlyReportsController implements Initializable {
	/**
	 * Description of FirstBranch,SecondBranch- used to save the relevant branch for
	 * the comparioson stage
	 */
	/**
	 * Description of FirstQuarter,SecondQuarter- used to save the relevant quarter
	 * for the comparioson stage
	 */
	private static String FirstBranch, SecondBranch, FirstQuarter, SecondQuarter;
	/**
	 * Description of CheckBoxStatus- used to know if we need to make a comparison
	 * between 2 reports or to show only 1
	 */
	private static Boolean CheckBoxStatus = false;
	/** Description of list - used to set quarter and branch combo boxes */
	ObservableList<String> list;

	@FXML
	private Button btnBack;

	@FXML
	private Button btnViewReports;

	@FXML
	private CheckBox checkBoxCompareToOther;

	@FXML
	private ComboBox<String> cmbFirstBranch;

	@FXML
	private ComboBox<String> cmbFirstQuarter;

	@FXML
	private ComboBox<String> cmbSecondBranch;

	@FXML
	private ComboBox<String> cmbSecondQuarter;

	@FXML
	private Label lblErrormsg;

	public static String getFirstBranch() {
		return FirstBranch;
	}

	public static String getSecondBranch() {
		return SecondBranch;
	}

	public static String getFirstQuarter() {
		return FirstQuarter;
	}

	public static String getSecondQuarter() {
		return SecondQuarter;
	}

	public static Boolean getCheckBoxStatus() {
		return CheckBoxStatus;
	}

	/**
	 * Description of Back(ActionEvent event) This method allows to the ceo to go
	 * back to the ceo central page.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void Back(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide(); // hide window
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/controllers/CeoPage.fxml").openStream());
		CeoController ceoController = loader.getController();
		ceoController.loadData(String.valueOf(ChatClient.u1.getIdNumber()));
		Scene scene = new Scene(root);
		primaryStage.setTitle("CEO");
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event1 -> event1.consume());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Description of compareToOtherReport(ActionEvent event) this method allows to
	 * the ceo to make a comparison between different reports if clicked - can
	 * choose 1 more report (for comparison of both) else - can view only 1 report.
	 * 
	 * @param event
	 */
	@FXML
	void compareToOtherReport(ActionEvent event) {
		CheckBoxStatus = checkBoxCompareToOther.isSelected();
		if (checkBoxCompareToOther.isSelected()) {
			cmbSecondBranch.setDisable(false);
			cmbSecondQuarter.setDisable(false);
		} else {
			cmbSecondBranch.setDisable(true);
			cmbSecondQuarter.setDisable(true);
		}
	}

	/**
	 * Description of setComboBoxes() This method responsible to set the combo boxes
	 * at the launching of this page
	 * 
	 * @throws IOException
	 */
	public void setComboBoxes() throws IOException {
		ArrayList<String> cmb = new ArrayList<String>();
		cmb.add("North");
		cmb.add("Central");
		cmb.add("South");
		list = FXCollections.observableArrayList(cmb);
		cmbFirstBranch.setItems(list);
		cmbSecondBranch.setItems(list);
		cmb.clear(); // for the quarter combo boxes
		cmb.add("First Quarter");
		cmb.add("Second Quarter");
		cmb.add("Third Quarter");
		cmb.add("Fourth Quarter");
		list = FXCollections.observableArrayList(cmb);
		cmbFirstQuarter.setItems(list);
		cmbSecondQuarter.setItems(list);
	}

	/**
	 * Description of initialize(URL location, ResourceBundle resources) The method
	 * Responsible for initializing this page
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			setComboBoxes();
		} catch (IOException e) {
			e.printStackTrace();
		}
		cmbSecondBranch.setDisable(true);
		cmbSecondQuarter.setDisable(true);

	}

	/**
	 * Description of clickViewReports(ActionEvent event) The method responsible to
	 * show the ceo the quarterly reports (in PDF format if choosen 1), in case of
	 * comparison, to show histogram by GUI which includes both reports. sends
	 * messages to the server and request from the DB for the relevant report (by a
	 * blob file)
	 * 
	 * @param event
	 * @throws IOException
	 * @throws DocumentException
	 */
	@FXML
	void clickViewReports(ActionEvent event) throws IOException, DocumentException, SQLException {
		lblErrormsg.setText("");
		FirstBranch = cmbFirstBranch.getValue();
		FirstQuarter = cmbFirstQuarter.getValue();
		SecondBranch = cmbSecondBranch.getValue();
		SecondQuarter = cmbSecondQuarter.getValue();

		if (checkBoxCompareToOther.isSelected()) {
			Path pathToFirstReport = Paths
					.get("C:\\CEOReports\\" + FirstBranch + "_" + FirstQuarter.split(" ")[0] + ".pdf");
			Path pathToSecondReport = Paths
					.get("C:\\CEOReports\\" + SecondBranch + "_" + SecondQuarter.split(" ")[0] + ".pdf");
			if ((Files.exists(pathToFirstReport) && Files.exists(pathToSecondReport))) {
				ClientUI.chat.accept("getDetailsForCEOreport:" + "\t" + cmbFirstBranch.getValue() + "\t"
						+ cmbFirstQuarter.getValue() + "\t" + "Report1");
				ClientUI.chat.accept("getDetailsForCEOreport:" + "\t" + cmbSecondBranch.getValue() + "\t"
						+ cmbSecondQuarter.getValue() + "\t" + "Report2");
				Stage primaryStage = new Stage();
				QuarterlyReportComparisonController quarterlyReportController = new QuarterlyReportComparisonController();
				quarterlyReportController.start(primaryStage);

			} else
				lblErrormsg.setText("The branch manager didn't sent the requested reports");
		}
		if (!checkBoxCompareToOther.isSelected()) {
			ClientUI.chat.accept("CheckIfFileExist:" + "\t" + FirstBranch + "\t" + FirstQuarter.split(" ")[0]);
			if (ChatClient.isFileExist) {
				ClientUI.chat.accept("getFile:" + "\t" + FirstBranch + "\t" + FirstQuarter.split(" ")[0]);
				getPdfFile(ChatClient.b, FirstBranch, FirstQuarter.split(" ")[0]);
			} else
				lblErrormsg.setText("The branch manager didn't sent the requested report");

		}
	}

	/**
	 * getPdfFile: this method get blob from db, and take it to outputStream.
	 * 
	 * @param sb      SerialBlob of png from DB.
	 * @param branch  the relevant branch.
	 * @param quarter the relevant quarter.
	 * @throws IOException
	 * @throws DocumentException
	 * @throws SQLException
	 */
	public void getPdfFile(SerialBlob sb, String branch, String quarter)
			throws IOException, DocumentException, SQLException {
		File f = new File("C:\\CEOReports\\"); // create a new folder
		f.mkdir();
		File fileName = new File("C:\\CEOReports\\" + branch + "_" + quarter + ".png");
		OutputStream out = new FileOutputStream(fileName);
		byte[] buff = new byte[4096]; // how much of the blob to read/write at a time
		int len = 0;
		InputStream is = sb.getBinaryStream();
		while ((len = is.read(buff)) != -1)
			out.write(buff, 0, len);
		out.close();
		String destination = "C:\\CEOReports\\"; // report destination
		String name = branch + "_" + quarter; // report file name
		String sourcePath = "C:\\CEOReports\\" + branch + "_" + quarter + ".png";
		imagesToPdf(destination, name, sourcePath, branch, quarter);
		File fileToOpen = new File("C:\\CEOReports\\" + branch + "_" + quarter + ".pdf");
		Desktop.getDesktop().open(fileToOpen);
	}

	/**
	 * imagesToPdf: this method create a pdf file from png file.
	 * 
	 * @param destination    get the destination at CEO computer to save the pdf.
	 * @param pdfName        get the pdf name.
	 * @param imagFileSource get the source path of png.
	 * @param String         branch get the relevant branch.
	 * @param String         quarter get the relevant quarter.
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static void imagesToPdf(String destination, String pdfName, String imagFileSource, String branch,
			String quarter) throws IOException, DocumentException {

		Document document = new Document(PageSize.A4, 20.0f, 20.0f, 20.0f, 150.0f);
		String desPath = destination;

		File destinationDirectory = new File(desPath);
		if (!destinationDirectory.exists()) {
			destinationDirectory.mkdir();
			System.out.println("DESTINATION FOLDER CREATED -> " + destinationDirectory.getAbsolutePath());
		} else if (destinationDirectory.exists()) {
			System.out.println("DESTINATION FOLDER ALREADY CREATED!!!");
		} else {
			System.out.println("DESTINATION FOLDER NOT CREATED!!!");
		}

		File file = new File(destinationDirectory, pdfName + ".pdf");

		FileOutputStream fileOutputStream = new FileOutputStream(file);

		PdfWriter pdfWriter = PdfWriter.getInstance(document, fileOutputStream);
		document.open();

		System.out.println("CONVERTER START.....");

		String[] splitImagFiles = imagFileSource.split(",");

		for (String singleImage : splitImagFiles) {
			Image image = Image.getInstance(singleImage);
			document.setPageSize(image);
			document.newPage();
			image.setAbsolutePosition(0, 0);
			document.add(image);
		}
		document.close();
		pdfWriter.close();
		System.out.println("CONVERTER STOPTED.....");
		Path imagePath = Paths.get("C:\\CEOReports\\" + branch + "_" + quarter + ".png");
		Files.deleteIfExists(imagePath);
	}
}
