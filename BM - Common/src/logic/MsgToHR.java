package logic;


/**
 * MsgToHR: The class represent the details of all messages to HR.
 * 
 * @author Raz Avraham.
 * @version 1.0 Build 100 December 29, 2021
 */
public class MsgToHR {

	private int fromUserId;
	private int ToUserId;
	private String Date;
	private String Hour;
	private String Message;
	private String Status;
	private boolean isRead;

	public MsgToHR(int fromUserId, int toUserId, String date, String hour, String message, String status,
			boolean isRead) {
		super();
		this.fromUserId = fromUserId;
		ToUserId = toUserId;
		Date = date;
		Hour = hour;
		Message = message;
		Status = status;
		this.isRead = isRead;
	}

	public int getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(int fromUserId) {
		this.fromUserId = fromUserId;
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
		return "msgToHR [fromUserId=" + fromUserId + ", ToUserId=" + ToUserId + ", Date=" + Date + ", Hour=" + Hour
				+ ", Message=" + Message + ", Status=" + Status + ", isRead=" + isRead + "]";
	}
}
