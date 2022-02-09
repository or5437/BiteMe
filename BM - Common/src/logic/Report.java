package logic;

/**
 * Report: The class represent the reports of branch manager.
 * 
 * @author Raz Avraham.
 * @version 1.0 Build 100 December 29, 2021
 */
public class Report {

	private String branch;
	private String restaurant;
	private Float totalRevenue;
	private Float revMainDishs;
	private Float revFirstDishs;
	private Float revDrinks;
	private Float revDesserts;
	private int numOfMainDishs;
	private int numOfFirstDishs;
	private int numOfDrinks;
	private int numOfDesserts;
	private int totalNumOfOrders;
	private int totalNumOfDelays;
	private int totalNumOfValidOrders;

	public Report(String branch, String restaurant, Float totalRevenue, Float revMainDishs, Float revFirstDishs,
			Float revDrinks, Float revDesserts, int numOfMainDishs, int numOfFirstDishs, int numOfDrinks,
			int numOfDesserts, int totalNumOfOrders, int totalNumOfDelays, int totalNumOfValidOrders) {
		super();
		this.branch = branch;
		this.restaurant = restaurant;
		this.totalRevenue = totalRevenue;
		this.revMainDishs = revMainDishs;
		this.revFirstDishs = revFirstDishs;
		this.revDrinks = revDrinks;
		this.revDesserts = revDesserts;
		this.numOfMainDishs = numOfMainDishs;
		this.numOfFirstDishs = numOfFirstDishs;
		this.numOfDrinks = numOfDrinks;
		this.numOfDesserts = numOfDesserts;
		this.totalNumOfOrders = totalNumOfOrders;
		this.totalNumOfDelays = totalNumOfDelays;
		this.totalNumOfValidOrders = totalNumOfValidOrders;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(String restaurant) {
		this.restaurant = restaurant;
	}

	public Float getTotalRevenue() {
		return totalRevenue;
	}

	public void setTotalRevenue(Float totalRevenue) {
		this.totalRevenue = totalRevenue;
	}

	public Float getRevMainDishs() {
		return revMainDishs;
	}

	public void setRevMainDishs(Float revMainDishs) {
		this.revMainDishs = revMainDishs;
	}

	public Float getRevFirstDishs() {
		return revFirstDishs;
	}

	public void setRevFirstDishs(Float revFirstDishs) {
		this.revFirstDishs = revFirstDishs;
	}

	public Float getRevDrinks() {
		return revDrinks;
	}

	public void setRevDrinks(Float revDrinks) {
		this.revDrinks = revDrinks;
	}

	public Float getRevDesserts() {
		return revDesserts;
	}

	public void setRevDesserts(Float revDesserts) {
		this.revDesserts = revDesserts;
	}

	public int getNumOfMainDishs() {
		return numOfMainDishs;
	}

	public void setNumOfMainDishs(int numOfMainDishs) {
		this.numOfMainDishs = numOfMainDishs;
	}

	public int getNumOfFirstDishs() {
		return numOfFirstDishs;
	}

	public void setNumOfFirstDishs(int numOfFirstDishs) {
		this.numOfFirstDishs = numOfFirstDishs;
	}

	public int getNumOfDrinks() {
		return numOfDrinks;
	}

	public void setNumOfDrinks(int numOfDrinks) {
		this.numOfDrinks = numOfDrinks;
	}

	public int getNumOfDesserts() {
		return numOfDesserts;
	}

	public void setNumOfDesserts(int numOfDesserts) {
		this.numOfDesserts = numOfDesserts;
	}

	public int getTotalNumOfOrders() {
		return totalNumOfOrders;
	}

	public void setTotalNumOfOrders(int totalNumOfOrders) {
		this.totalNumOfOrders = totalNumOfOrders;
	}

	public int getTotalNumOfDelays() {
		return totalNumOfDelays;
	}

	public void setTotalNumOfDelays(int totalNumOfDelays) {
		this.totalNumOfDelays = totalNumOfDelays;
	}

	public int getTotalNumOfValidOrders() {
		return totalNumOfValidOrders;
	}

	public void setTotalNumOfValidOrders(int totalNumOfValidOrders) {
		this.totalNumOfValidOrders = totalNumOfValidOrders;
	}

	@Override
	public String toString() {
		return "Report [branch=" + branch + ", restaurant=" + restaurant + ", totalRevenue=" + totalRevenue
				+ ", revMainDishs=" + revMainDishs + ", revFirstDishs=" + revFirstDishs + ", revDrinks=" + revDrinks
				+ ", revDesserts=" + revDesserts + ", numOfMainDishs=" + numOfMainDishs + ", numOfFirstDishs="
				+ numOfFirstDishs + ", numOfDrinks=" + numOfDrinks + ", numOfDesserts=" + numOfDesserts
				+ ", totalNumOfOrders=" + totalNumOfOrders + ", totalNumOfDelays=" + totalNumOfDelays
				+ ", totalNumOfValidOrders=" + totalNumOfValidOrders + "]";
	}

}