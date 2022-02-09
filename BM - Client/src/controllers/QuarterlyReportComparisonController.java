package controllers;

import java.io.IOException;
import java.util.Set;

import com.itextpdf.text.DocumentException;

import client.ChatClient;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Description of QuarterlyReportComparisonController The class is designed to
 * generate comparison between 2 differnt Quarterly reports.
 * 
 * @author Michael Ravich
 * @version Version 1.0 Build 100 December 27, 2021
 */
public class QuarterlyReportComparisonController {

	@FXML
	private BarChart<String, Number> b;

	@FXML
	private CategoryAxis X;

	@FXML
	private NumberAxis Y;

	/**
	 * start: this method responsible to start the primary stage, and to launch the
	 * matching fxml file
	 * 
	 * @param s
	 * @throws IOException
	 * @throws DocumentException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void start(Stage s) throws IOException, DocumentException {

		ChatClient.detailsForCEOReportOrders.remove("Report1");
		ChatClient.detailsForCEOReportOrders.remove("ordersHashMap");
		Set<String> keySet = ChatClient.detailsForCEOReportOrders.keySet();
		Object[] restaurantsNames1 = keySet.toArray();
		s.setTitle("Quarterly Report");
		CategoryAxis x = new CategoryAxis();
		x.setLabel("Number of orders");
		NumberAxis y = new NumberAxis();
		y.setLabel("Restaurants revenue");
		b = new BarChart<String, Number>(x, y);
		XYChart.Series ds = new XYChart.Series();
		ds.setName("Branch: " + CeoPresentQuarterlyReportsController.getFirstBranch() + "- "
				+ CeoPresentQuarterlyReportsController.getFirstQuarter());
		for (int i = 0; i < restaurantsNames1.length; i++) {
			ds.getData()
					.add(new XYChart.Data(
							restaurantsNames1[i].toString() + "\nTotal Orders:"
									+ ChatClient.detailsForCEOReportOrders.get(restaurantsNames1[i].toString()),
							ChatClient.detailsForCEOReportRevenue.get(restaurantsNames1[i].toString())));
		}
		b.getData().add(ds);
		ChatClient.detailsForCEOReportOrders2.remove("Report2");
		ChatClient.detailsForCEOReportOrders2.remove("ordersHashMap");
		Set<String> keySet2 = ChatClient.detailsForCEOReportOrders2.keySet();
		Object[] restaurantsNames2 = keySet2.toArray();
		XYChart.Series ds2 = new XYChart.Series();
		ds2.setName("Branch: " + CeoPresentQuarterlyReportsController.getSecondBranch() + "- "
				+ CeoPresentQuarterlyReportsController.getSecondQuarter());

		for (int i = 0; i < restaurantsNames2.length; i++) {
			ds2.getData()
					.add(new XYChart.Data(
							restaurantsNames2[i].toString() + "\nTotal Orders:"
									+ ChatClient.detailsForCEOReportOrders2.get(restaurantsNames2[i].toString()),
							ChatClient.detailsForCEOReportRevenue2.get(restaurantsNames2[i].toString())));
		}
		b.getData().add(ds2);

		VBox vbox = new VBox(b);
		Scene sc = new Scene(vbox, 800, 600);
		s.setScene(sc);
		s.setHeight(600);
		s.setWidth(1200);
		s.show();

	}

}
