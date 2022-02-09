package logic;

/**
 * Refund: The class represent the refund of customer.
 * 
 * @author Raz Avraham.
 * @version 1.0 Build 100 December 29, 2021
 */
public class Refund {
	private int userId;
	private String branch;
	private String restaurant;
	private float priceRefund;

	public Refund(int userId, String branch, String restaurant, float priceRefund) {
		this.userId = userId;
		this.branch = branch;
		this.restaurant = restaurant;
		this.priceRefund = priceRefund;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
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

	public float getPriceRefund() {
		return priceRefund;
	}

	public void setPriceRefund(float priceRefund) {
		this.priceRefund = priceRefund;
	}

	@Override
	public String toString() {
		return "Refund [userId=" + userId + ", branch=" + branch + ", restaurant=" + restaurant + ", priceRefund="
				+ priceRefund + "]";
	}
}
