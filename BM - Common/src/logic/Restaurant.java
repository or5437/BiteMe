package logic;

/**
 * Restaurant: The class represent the restaurant details.
 * 
 * @author Raz Avraham.
 * @version 1.0 Build 100 December 29, 2021
 */
public class Restaurant {
	private int restaurantID;
	private String restaurantName;
	private String Branch;

	public Restaurant(int restaurantID, String restaurantName, String branch) {
		this.restaurantName = restaurantName;
		Branch = branch;
		this.restaurantID = restaurantID;
	}

	public int getRestaurantID() {
		return restaurantID;
	}

	public void setRestaurantID(int restaurantID) {
		this.restaurantID = restaurantID;
	}

	public String getRestaurantName() {
		return restaurantName;
	}

	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}

	public String getBranch() {
		return Branch;
	}

	public void setBranch(String branch) {
		Branch = branch;
	}

	@Override
	public String toString() {
		return "Restaurant [restaurantID=" + restaurantID + ", restaurantName=" + restaurantName + ", Branch=" + Branch
				+ "]";
	}
}
