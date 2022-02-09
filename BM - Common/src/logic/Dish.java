package logic;

/**
 * Dish: The class represent the details of dish.
 * 
 * @author Raz Avraham.
 * @version 1.0 Build 100 December 29, 2021
 */
public class Dish {

	private String Name;
	private String Restaurant;
	private String Type;
	private float Price;
	private boolean isDoneness;
	private boolean isSize;

	public Dish(String name, String restaurant, String type, float price, boolean isDoneness, boolean isSize) {
		Name = name;
		Restaurant = restaurant;
		Type = type;
		Price = price;
		this.isDoneness = isDoneness;
		this.isSize = isSize;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getRestaurant() {
		return Restaurant;
	}

	public void setRestaurant(String restaurant) {
		Restaurant = restaurant;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public float getPrice() {
		return Price;
	}

	public void setPrice(float price) {
		Price = price;
	}

	public boolean isDoneness() {
		return isDoneness;
	}

	public void setDoneness(boolean isDoneness) {
		this.isDoneness = isDoneness;
	}

	public boolean isSize() {
		return isSize;
	}

	public void setSize(boolean isSize) {
		this.isSize = isSize;
	}

	@Override
	public String toString() {
		return "Dish [Name=" + Name + ", Restaurant=" + Restaurant + ", Type=" + Type + ", Price=" + Price
				+ ", isDoneness=" + isDoneness + ", isSize=" + isSize + "]";
	}

}
