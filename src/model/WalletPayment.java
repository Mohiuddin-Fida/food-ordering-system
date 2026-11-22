package model;

/** Child of Payment. Payment through an online wallet provider. */
public class WalletPayment extends Payment {
    private String walletProvider;
    private String gatewayReference;
    private String authorizationStatus;
    private String processedAt;
    private double dailyLimit;

    // Constructor 1
    public WalletPayment(String paymentId, String walletProvider, double dailyLimit) {
        super(paymentId);
        this.walletProvider = walletProvider;
        this.dailyLimit = dailyLimit;
        this.gatewayReference = "";
        this.authorizationStatus = "None";
        this.processedAt = "";
    }

    // Constructor 2 (overloading): default wallet limit
    public WalletPayment(String paymentId, String walletProvider) {
        this(paymentId, walletProvider, 5000);
    }

    // Overrides Payment.process(): authorization through the wallet provider
    @Override
    public boolean process(double amount) {
        setAmount(amount);
        if (amount <= 0 || amount > dailyLimit) {           // simulated wallet limit check
            authorizationStatus = "Declined";
            setStatus("Declined");
            return false;
        }
        gatewayReference = "WG-" + getPaymentId();
        setTransactionReference("TXN-W-" + walletProvider + "-" + getPaymentId());
        authorizationStatus = "Approved by " + walletProvider;
        setStatus("Approved");
        processedAt = String.valueOf(System.currentTimeMillis());
        return true;
    }

    @Override
    public boolean verify() {
        return authorizationStatus.startsWith("Approved") && isApproved();
    }

    @Override
    public boolean refund(double amount) {
        if (verify() && amount > 0 && amount <= getAmount()) {
            setStatus("Refunded");
            authorizationStatus = "Credited back to wallet";
            return true;
        }
        return false;
    }

    @Override
    public String getReceipt() {
        return super.getReceipt() + " | Method: " + walletProvider + " wallet";
    }

    public String getWalletProvider() { return walletProvider; }
    public String getGatewayReference() { return gatewayReference; }
    public String getAuthorizationStatus() { return authorizationStatus; }
    public String getProcessedAt() { return processedAt; }
    public double getDailyLimit() { return dailyLimit; }
}
