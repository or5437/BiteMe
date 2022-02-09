package logic;

/**
 * Employer: The class represent the details of Employer.
 * 
 * @author Raz Avraham.
 * @version 1.0 Build 100 December 29, 2021
 */
public class Employer {
	private String businessName;
	private Float MonthlyBudget;

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public Float getMonthlyBudget() {
		return MonthlyBudget;
	}

	public void setMonthlyBudget(Float monthlyBudget) {
		MonthlyBudget = monthlyBudget;
	}

	public Employer(String businessName, Float monthlyBudget) {
		this.businessName = businessName;
		MonthlyBudget = monthlyBudget;
	}

	@Override
	public String toString() {
		return "Employer [businessName=" + businessName + ", MonthlyBudget=" + MonthlyBudget + "]";
	}
}