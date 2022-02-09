package logic;

/**
 * ClientDetails: The class represent the details of the client - IP, host name, connection status.
 * 
 * @author Raz Avraham.
 * @version 1.0 Build 100 December 29, 2021
 */
public class ClientDetails {
	private String ipAddress;
	private String hostName;
	private String status;

	public ClientDetails(String ipAddress, String hostName, String status) {
		this.ipAddress = ipAddress;
		this.hostName = hostName;
		this.status = status;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
