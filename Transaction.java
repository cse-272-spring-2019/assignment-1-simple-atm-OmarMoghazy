
public class Transaction {
	// Attributes
	private double value;
	private String transactionType;

	// Constructors
	public Transaction() {
	}

	public Transaction(double value, String transactionType) {
		this.value = value;
		this.transactionType = transactionType;
	}

	// Methods
	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
}
