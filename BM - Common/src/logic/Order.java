package logic;

/**
 * Order: The class represent the order details - OrderNumber, Restaurant,
 * OrderTime, PhoneNumber, TypeOfOrder, OrderAddress.
 * 
 * @author Raz Avraham.
 * @version 1.0 Build 100 December 29, 2021
 */
public class Order {

	private String UserType;
	private int UserID;
	private String PhoneNumber;
	private String OrderConfirmDate;
	private String OrderConfirmHour;
	private String OrderDueDate;
	private String OrderDueHour;
	private String OrderAddress;
	private boolean isPreOrder;
	private String Branch;
	private String Restaurant;
	private float finalPrice;
	private String TypeOfOrder;
	private boolean isConfirm;

	public Order(String userType, int userID, String phoneNumber, String orderConfirmDate, String orderConfirmHour,
			String orderDueDate, String orderDueHour, String orderAddress, boolean isPreOrder, String branch,
			String restaurant, float finalPrice, String typeOfOrder, boolean isConfirm) {
		super();
		UserType = userType;
		UserID = userID;
		PhoneNumber = phoneNumber;
		OrderConfirmDate = orderConfirmDate;
		OrderConfirmHour = orderConfirmHour;
		OrderDueDate = orderDueDate;
		OrderDueHour = orderDueHour;
		OrderAddress = orderAddress;
		this.isPreOrder = isPreOrder;
		Branch = branch;
		Restaurant = restaurant;
		this.finalPrice = finalPrice;
		TypeOfOrder = typeOfOrder;
		this.isConfirm = isConfirm;
	}

	public String getUserType() {
		return UserType;
	}

	public void setUserType(String userType) {
		UserType = userType;
	}

	public int getUserID() {
		return UserID;
	}

	public void setUserID(int userID) {
		UserID = userID;
	}

	public String getPhoneNumber() {
		return PhoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		PhoneNumber = phoneNumber;
	}

	public String getOrderConfirmDate() {
		return OrderConfirmDate;
	}

	public void setOrderConfirmDate(String orderConfirmDate) {
		OrderConfirmDate = orderConfirmDate;
	}

	public String getOrderConfirmHour() {
		return OrderConfirmHour;
	}

	public void setOrderConfirmHour(String orderConfirmHour) {
		OrderConfirmHour = orderConfirmHour;
	}

	public String getOrderDueDate() {
		return OrderDueDate;
	}

	public void setOrderDueDate(String orderDueDate) {
		OrderDueDate = orderDueDate;
	}

	public String getOrderDueHour() {
		return OrderDueHour;
	}

	public void setOrderDueHour(String orderDueHour) {
		OrderDueHour = orderDueHour;
	}

	public String getOrderAddress() {
		return OrderAddress;
	}

	public void setOrderAddress(String orderAddress) {
		OrderAddress = orderAddress;
	}

	public boolean isPreOrder() {
		return isPreOrder;
	}

	public void setPreOrder(boolean isPreOrder) {
		this.isPreOrder = isPreOrder;
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

	public float getFinalPrice() {
		return finalPrice;
	}

	public void setFinalPrice(float finalPrice) {
		this.finalPrice = finalPrice;
	}

	public String getTypeOfOrder() {
		return TypeOfOrder;
	}

	public void setTypeOfOrder(String typeOfOrder) {
		TypeOfOrder = typeOfOrder;
	}

	public boolean isConfirm() {
		return isConfirm;
	}

	public void setConfirm(boolean isConfirm) {
		this.isConfirm = isConfirm;
	}

	@Override
	public String toString() {
		return "Order [UserType=" + UserType + ", UserID=" + UserID + ", PhoneNumber=" + PhoneNumber
				+ ", OrderConfirmDate=" + OrderConfirmDate + ", OrderConfirmHour=" + OrderConfirmHour
				+ ", OrderDueDate=" + OrderDueDate + ", OrderDueHour=" + OrderDueHour + ", OrderAddress=" + OrderAddress
				+ ", isPreOrder=" + isPreOrder + ", Branch=" + Branch + ", Restaurant=" + Restaurant + ", finalPrice="
				+ finalPrice + ", TypeOfOrder=" + TypeOfOrder + ", isConfirm=" + isConfirm + "]";
	}
}