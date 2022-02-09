package logic;

/**
 * MsgToSupplier: The class represent the details of all messages to supplier.
 * 
 * @author Raz Avraham.
 * @version 1.0 Build 100 December 29, 2021
 */
public class MsgToSupplier {
	private int OrderId;
	private int fromUserId;
	private int ToSupplierId;
	private String Date;
	private String Hour;
	private String Message;
	private String Status;
	private boolean isRead;

	public MsgToSupplier(int orderId, int fromUserId, int toSupplierId, String date, String hour, String message,
			String status, boolean isRead) {
		OrderId = orderId;
		this.fromUserId = fromUserId;
		ToSupplierId = toSupplierId;
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

	public int getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(int fromUserId) {
		this.fromUserId = fromUserId;
	}

	public int getToSupplierId() {
		return ToSupplierId;
	}

	public void setToSupplierId(int toSupplierId) {
		ToSupplierId = toSupplierId;
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
		return "MsgToSupplier [OrderId=" + OrderId + ", fromUserId=" + fromUserId + ", ToSupplierId=" + ToSupplierId
				+ ", Date=" + Date + ", Hour=" + Hour + ", Message=" + Message + ", Status=" + Status + ", isRead="
				+ isRead + "]";
	}
}
