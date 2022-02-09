package logic;

/**
 * Shipment: The class represent the shipment details.
 * 
 * @author Raz Avraham.
 * @version 1.0 Build 100 December 29, 2021
 */
public class Shipment {

	private int ShipmentOrderNunber;
	private String ShipmentType;
	private float ShipmentPrice;
	private String ReciverAddress;
	private String ReciverName;
	private String ReciverPhone;
	private boolean IsRobot;

	public Shipment(int shipmentOrderNunber, String shipmentType, float shipmentPrice, String reciverAddress,
			String reciverName, String reciverPhone, boolean isRobot) {
		ShipmentOrderNunber = shipmentOrderNunber;
		ShipmentType = shipmentType;
		ShipmentPrice = shipmentPrice;
		ReciverAddress = reciverAddress;
		ReciverName = reciverName;
		ReciverPhone = reciverPhone;
		IsRobot = isRobot;
	}

	public boolean isIsRobot() {
		return IsRobot;
	}

	public void setIsRobot(boolean isRobot) {
		IsRobot = isRobot;
	}

	public int getShipmentOrderNunber() {
		return ShipmentOrderNunber;
	}

	public void setShipmentOrderNunber(int shipmentOrderNunber) {
		ShipmentOrderNunber = shipmentOrderNunber;
	}

	public String getShipmentType() {
		return ShipmentType;
	}

	public void setShipmentType(String shipmentType) {
		ShipmentType = shipmentType;
	}

	public float getShipmentPrice() {
		return ShipmentPrice;
	}

	public void setShipmentPrice(float shipmentPrice) {
		ShipmentPrice = shipmentPrice;
	}

	public String getReciverAddress() {
		return ReciverAddress;
	}

	public void setReciverAddress(String reciverAddress) {
		ReciverAddress = reciverAddress;
	}

	public String getReciverName() {
		return ReciverName;
	}

	public void setReciverName(String reciverName) {
		ReciverName = reciverName;
	}

	public String getReciverPhone() {
		return ReciverPhone;
	}

	public void setReciverPhone(String reciverPhone) {
		ReciverPhone = reciverPhone;
	}

	@Override
	public String toString() {
		return "Shipment [ShipmentOrderNunber=" + ShipmentOrderNunber + ", ShipmentType=" + ShipmentType
				+ ", ShipmentPrice=" + ShipmentPrice + ", ReciverAddress=" + ReciverAddress + ", ReciverName="
				+ ReciverName + ", ReciverPhone=" + ReciverPhone + ", IsRobot=" + IsRobot + "]";
	}
}
