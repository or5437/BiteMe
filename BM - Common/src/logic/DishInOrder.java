package logic;

/**
 * DishInOrder: The class represent the details of dish in order.
 * 
 * @author Raz Avraham.
 * @version 1.0 Build 100 December 29, 2021
 */
public class DishInOrder {
	private String Branch;
	private String Restaurant;
	private String Item;
	private String Dish;
	private float Price;
	private String Doneness;
	private String Size;
	private String OptionalComponents;
	private float Comission;

	public DishInOrder(String branch, String restaurant, String item, String dish, float price, String doneness,
			String size, String optionalComponents, float comission) {
		Branch = branch;
		Restaurant = restaurant;
		Item = item;
		Dish = dish;
		Price = price;
		Doneness = doneness;
		Size = size;
		OptionalComponents = optionalComponents;
		Comission = comission;
	}

	public String getBranch() {
		return Branch;
	}

	public void setBranch(String branch) {
		Branch = branch;
	}

	public String getRestaurant() {
		return Restaurant;
	}

	public void setRestaurant(String restaurant) {
		Restaurant = restaurant;
	}

	public String getItem() {
		return Item;
	}

	public void setItem(String item) {
		Item = item;
	}

	public String getDish() {
		return Dish;
	}

	public void setDish(String dish) {
		Dish = dish;
	}

	public float getPrice() {
		return Price;
	}

	public void setPrice(float price) {
		Price = price;
	}

	public String getDoneness() {
		return Doneness;
	}

	public void setDoneness(String doneness) {
		Doneness = doneness;
	}

	public String getSize() {
		return Size;
	}

	public void setSize(String size) {
		Size = size;
	}

	public String getOptionalComponents() {
		return OptionalComponents;
	}

	public void setOptionalComponents(String optionalComponents) {
		OptionalComponents = optionalComponents;
	}
	
	public float getComission() {
		return Comission;
	}

	public void setComission(float comission) {
		Comission = comission;
	}

	@Override
	public String toString() {
		String str;
		str = Item + "\t" + Dish + "\t" + Price * (1 - getComission()) + " NIS";
		return str;
	}
}
