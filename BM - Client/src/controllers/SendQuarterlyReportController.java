package controllers;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.imageio.ImageIO;

import com.itextpdf.text.DocumentException;

import client.ChatClient;
import client.ClientUI;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Description of SendQuarterlyReportController The class is designed to let the
 * bm manager to create and send to the ceo the quarterly reports.
 * 
 * @author Michael Ravich
 * @version Version 1.0 Build 100 December 27, 2021
 */
public class SendQuarterlyReportController {
	/**
	 * Description of b - responsible to make a histogram, for quarterly report
	 * presentation
	 */
	@FXML
	private BarChart<String, Number> b;
	/** Description of X - Represents the X-axis of the histogram */
	@FXML
	private CategoryAxis X;
	/** Description of Y- Represents the Y-axis of the histogram */
	@FXML
	private NumberAxis Y;

	/**
	 * Description of saveAsPng(Scene scene, String path) the method makes a
	 * snapshot for the given scene, and saves it as a png file
	 * 
	 * @param scene
	 * @param path
	 */
	public void saveAsPng(Scene scene, String path) {
		WritableImage image = scene.snapshot(null);
		File file = new File(path);
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Description of start(Stage s) This method starts the quarterly reports stage
	 * page, by getting the relevant details from ChatClient (2 Hash maps) than is
	 * calls to save as png func and make isMyFile=true. than it sends a message to
	 * the server to take care for this file (insert into db)
	 * 
	 * @param s
	 * @throws IOException
	 * @throws DocumentException
	 * @throws InterruptedException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void start(Stage s) throws IOException, DocumentException, InterruptedException {
		ChatClient.detailsForCEOReportOrders.remove("Report1");
		ChatClient.detailsForCEOReportOrders.remove("ordersHashMap");
		Set<String> keySet = ChatClient.detailsForCEOReportOrders.keySet();
		Object[] restaurantsNames1 = keySet.toArray();
		s.setTitle("Quarterly Report");
		// x axis
		CategoryAxis x = new CategoryAxis();
		x.setLabel("Number of orders");
		// y axis
		NumberAxis y = new NumberAxis();
		y.setLabel("Restaurants revenue");
		// bar chart creation

		b = new BarChart<String, Number>(x, y);
		// add values
		XYChart.Series ds = new XYChart.Series();

		ds.setName("Branch: " + GenerateQuarterlyReportController.getBranch() + " "
				+ GenerateQuarterlyReportController.getQuarter());

		for (int i = 0; i < restaurantsNames1.length; i++) {
			ds.getData()
					.add(new XYChart.Data(
							restaurantsNames1[i].toString() + "\nTotal Orders:"
									+ ChatClient.detailsForCEOReportOrders.get(restaurantsNames1[i].toString()),
							ChatClient.detailsForCEOReportRevenue.get(restaurantsNames1[i].toString())));
		}

		b.setAnimated(false);
		b.getData().addAll(ds);

		// vertical box
		VBox vbox = new VBox(b);
		Scene sc = new Scene(vbox, 800, 600);
		s.setScene(sc);
		s.setHeight(500);
		s.setWidth(1200);
		s.show();

		File f = new File("C:\\CEOReports"); // create a new folder
		f.mkdir();

		saveAsPng(sc, "\\CEOReports\\" + GenerateQuarterlyReportController.getBranch() + "_"
				+ GenerateQuarterlyReportController.getQuarter() + ".png");

		ChatClient.isMyFile = true;
		ClientUI.chat.accept("\\CEOReports\\" + GenerateQuarterlyReportController.getBranch() + "_"
				+ GenerateQuarterlyReportController.getQuarter() + ".png");
	}

}