package logic;

/**
 * BusinessCustomer: class for entity of business_cusotmer.
 * 
 * @author Raz Avraham.
 * @version 1.0 Build 100 December 29, 2021
 */
public class BusinessCustomer {
	private int userIdNumber;
	private String businessName;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private String creditCard;
	private float CurrentBudget;

	public BusinessCustomer(int userIdNumber, String businessName, String firstName, String lastName, String email,
			String phoneNumber, String creditCard, float currentBudget) {
		this.userIdNumber = userIdNumber;
		this.businessName = businessName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.creditCard = creditCard;
		CurrentBudget = currentBudget;
	}

	public int getUserIdNumber() {
		return userIdNumber;
	}

	public void setUserIdNumber(int userIdNumber) {
		this.userIdNumber = userIdNumber;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(String creditCard) {
		this.creditCard = creditCard;
	}

	public float getCurrentBudget() {
		return CurrentBudget;
	}

	public void setCurrentBudget(float currentBudget) {
		CurrentBudget = currentBudget;
	}

	@Override
	public String toString() {
		return "BusinessCustomer [userIdNumber=" + userIdNumber + ", businessName=" + businessName + ", firstName="
				+ firstName + ", lastName=" + lastName + ", email=" + email + ", phoneNumber=" + phoneNumber
				+ ", creditCard=" + creditCard + ", CurrentBudget=" + CurrentBudget + "]";
	}

}
