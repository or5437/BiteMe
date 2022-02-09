package logic;

/**
 * MsgToCEO: The class represent the details of all messages to CEO.
 * 
 * @author Raz Avraham.
 * @version 1.0 Build 100 December 29, 2021
 */
public class MsgToCEO {

	private int fromManagerId;
	private int toUserId;
	private String Date;
	private String Hour;
	private String Message;
	private String Status;
	private boolean isRead;

	public MsgToCEO(int fromManagerId, int toUserId, String date, String hour, String message, String status,
			boolean isRead) {
		this.fromManagerId = fromManagerId;
		this.toUserId = toUserId;
		Date = date;
		Hour = hour;
		Message = message;
		Status = status;
		this.isRead = isRead;
	}

	public int getFromManagerId() {
		return fromManagerId;
	}

	public void setFromManagerId(int fromManagerId) {
		this.fromManagerId = fromManagerId;
	}

	public int getToUserId() {
		return toUserId;
	}

	public void setToUserId(int toUserId) {
		this.toUserId = toUserId;
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
		return "MsgToCEO [fromManagerId=" + fromManagerId + ", toUserId=" + toUserId + ", Date=" + Date + ", Hour="
				+ Hour + ", Message=" + Message + ", Status=" + Status + ", isRead=" + isRead + "]";
	}
}
