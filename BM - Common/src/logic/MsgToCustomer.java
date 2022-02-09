package logic;

/**
 * MsgToCustomer: The class represent the details of all messages to Customers.
 * 
 * @author Raz Avraham.
 * @version 1.0 Build 100 December 29, 2021
 */
public class MsgToCustomer {
	private int OrderId;
	private int fromSupplierId;
	private int ToUserId;
	private String Date;
	private String Hour;
	private String Message;
	private String Status;
	private boolean isRead;

	public MsgToCustomer(int orderId, int fromSupplierId, int toUserId, String date, String hour, String message,
			String status, boolean isRead) {
		OrderId = orderId;
		this.fromSupplierId = fromSupplierId;
		ToUserId = toUserId;
		Date = date;
		Hour = hour;
		Message = message;
		Status = status;
		this.isRead = isRead;
	}

	public int getOrderId() {
		return OrderId;
	}

	public void setOrderId(int orderId) {
		OrderId = orderId;
	}

	public int getFromSupplierId() {
		return fromSupplierId;
	}

	public void setFromSupplierId(int fromSupplierId) {
		this.fromSupplierId = fromSupplierId;
	}

	public int getToUserId() {
		return ToUserId;
	}

	public void setToUserId(int toUserId) {
		ToUserId = toUserId;
	}

	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}

	public String getHour() {
		return Hour;
	}

	public void setHour(String hour) {
		Hour = hour;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	@Override
	public String toString() {
		return "MsgToCustomer [OrderId=" + OrderId + ", fromSupplierId=" + fromSupplierId + ", ToUserId=" + ToUserId
				+ ", Date=" + Date + ", Hour=" + Hour + ", Message=" + Message + ", Status=" + Status + ", isRead="
				+ isRead + "]";
	}

}
