package model;

/**
 * Abstract parent of CardPayment and WalletPayment.
 * Card numbers and CVV values are never stored (NFR-02).
 */
public abstract class Payment {
    private String paymentId;
    private double amount;
    private String status;
    private String transactionReference;

    public Payment(String paymentId) {
        this.paymentId = paymentId;
        this.amount = 0;
        this.status = "Pending";
        this.transactionReference = "";
    }

    // Abstract operations: every payment method does these differently
    public abstract boolean process(double amount);
    public abstract boolean verify();
    public abstract boolean refund(double amount);

    // Concrete operations shared by all payment methods
    public boolean isApproved() {
        return status.equals("Approved");
    }

    public String getReceipt() {
        return "Receipt " + paymentId + " | Amount: Rs. " + amount + " | Status: " + status
                + " | Ref: " + transactionReference;
    }

    public String getPaymentId() { return paymentId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getTransactionReference() { return transactionReference; }
    public void setTransactionReference(String transactionReference) { this.transactionReference = transactionReference; }
}
