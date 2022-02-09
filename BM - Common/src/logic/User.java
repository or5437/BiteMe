package logic;

/**
 * User: The class represents the login details.
 * 
 * @author Raz Avraham.
 * @version 1.0 Build 100 December 29, 2021
 */
public class User {
	private Integer idNumber;
	private String userName;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private String role;
	private Boolean isLoggedIn;
	private String password;
	private String type;
	private String status;

	public User(int idNumber, String firstName, String lastName, String email, String phoneNumber, String role,
			Boolean isLoggedIn, String userName, String password, String type, String status) {
		this.userName = userName;
		this.password = password;
		this.idNumber = idNumber;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.role = role;
		this.isLoggedIn = isLoggedIn;
		this.type = type;
		this.status = status;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Boolean getIsLoggedIn() {
		return isLoggedIn;
	}

	public void setIsLoggedIn(Boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(Integer idNumber) {
		this.idNumber = idNumber;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "User [idNumber=" + idNumber + ", userName=" + userName + ", firstName=" + firstName + ", lastName="
				+ lastName + ", email=" + email + ", phoneNumber=" + phoneNumber + ", role=" + role + ", isLoggedIn="
				+ isLoggedIn + ", password=" + password + ", type=" + type + ", status=" + status + "]";
	}

}
