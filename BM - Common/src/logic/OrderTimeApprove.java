package logic;

/**
 * OrderTimeApprove: The class represent the approval time of orders.
 * 
 * @author Raz Avraham.
 * @version 1.0 Build 100 December 29, 2021
 */
public class OrderTimeApprove {

	private int OrderId;
	private String supplierTime;
	private String customerTime;
	private String customerDate;

	public OrderTimeApprove(int orderId, String supplierTime, String customerTime, String customerDate) {
		super();
		OrderId = orderId;
		this.supplierTime = supplierTime;
		this.customerTime = customerTime;
		this.customerDate = customerDate;
	}

	public int getOrderId() {
		return OrderId;
	}

	public void setOrderId(int orderId) {
		OrderId = orderId;
	}

	public String getSupplierTime() {
		return supplierTime;
	}

	public void setSupplierTime(String supplierTime) {
		this.supplierTime = supplierTime;
	}

	public String getCustomerTime() {
		return customerTime;
	}

	public void setCustomerTime(String customerTime) {
		this.customerTime = customerTime;
	}

	public String getCustomerDate() {
		return customerDate;
	}

	public void setCustomerDate(String customerDate) {
		this.customerDate = customerDate;
	}

	@Override
	public String toString() {
		return "OrderTimeApprove [OrderId=" + OrderId + ", supplierTime=" + supplierTime + ", customerTime="
				+ customerTime + ", customerDate=" + customerDate + "]";
	}

}
