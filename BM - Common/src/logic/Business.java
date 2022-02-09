package logic;

/**
 * Business: class for entity of business.
 * 
 * @author Raz Avraham.
 * @version 1.0 Build 100 December 29, 2021
 */
public class Business {
	private String businessName;
	private String businessAddress;
	private String businessPhone;
	private float monthlyBudget;

	public Business(String businessName, String businessAddress, String businessPhone, float monthlyBudget) {
		this.businessName = businessName;
		this.businessAddress = businessAddress;
		this.businessPhone = businessPhone;
		this.monthlyBudget = monthlyBudget;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getBusinessAddress() {
		return businessAddress;
	}

	public void setBusinessAddress(String businessAddress) {
		this.businessAddress = businessAddress;
	}

	public String getBusinessPhone() {
		return businessPhone;
	}

	public void setBusinessPhone(String businessPhone) {
		this.businessPhone = businessPhone;
	}

	public float getMonthlyBudget() {
		return monthlyBudget;
	}

	public void setMonthlyBudget(float monthlyBudget) {
		this.monthlyBudget = monthlyBudget;
	}

	@Override
	public String toString() {
		return "Business [businessName=" + businessName + ", businessAddress=" + businessAddress + ", businessPhone="
				+ businessPhone + ", monthlyBudget=" + monthlyBudget + "]";
	}
}
