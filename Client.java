
public class Client {
	// Attributes
	private String accNumber;
	private double balance;
	
	// Constructors
	
	Client(String accNumber, double balance) {
		this.accNumber = accNumber;
		this.balance = balance;
		}
	// Methods

	public String getAccNumber() {
		return accNumber;
	}

	public void setAccNumber(String accNumber) {
		this.accNumber = accNumber;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	
	
}
