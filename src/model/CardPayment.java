package model;

/** Child of Payment. Only the last 4 digits of the card are kept. */
public class CardPayment extends Payment {
    private String gatewayReference;
    private String authorizationStatus;
    private String providerName;
    private String processedAt;
    private String cardLast4;

    // Constructor 1
    public CardPayment(String paymentId, String providerName, String cardLast4) {
        super(paymentId);
        this.providerName = providerName;
        this.cardLast4 = cardLast4;
        this.gatewayReference = "";
        this.authorizationStatus = "None";
        this.processedAt = "";
    }

    // Constructor 2 (overloading)
    public CardPayment(String paymentId, String cardLast4) {
        this(paymentId, "Card Gateway", cardLast4);
    }

    // Overrides Payment.process(): simulated card authorization
    @Override
    public boolean process(double amount) {
        setAmount(amount);
        if (amount <= 0 || cardLast4.equals("0000")) {      // simulated decline
            authorizationStatus = "Declined";
            setStatus("Declined");
            return false;
        }
        int cents = (int) (amount * 100);                   // type casting double -> int
        gatewayReference = "CG-" + getPaymentId();
        setTransactionReference("TXN-C-" + cents);
        authorizationStatus = "Authorized";
        setStatus("Approved");
        processedAt = String.valueOf(System.currentTimeMillis());
        return true;
    }

    @Override
    public boolean verify() {
        return authorizationStatus.equals("Authorized") && isApproved();
    }

    @Override
    public boolean refund(double amount) {
        if (verify() && amount > 0 && amount <= getAmount()) {
            setStatus("Refunded");
            authorizationStatus = "Reversed";
            return true;
        }
        return false;
    }

    @Override
    public String getReceipt() {
        return super.getReceipt() + " | Method: " + providerName + " card ending " + cardLast4;
    }

    public String getGatewayReference() { return gatewayReference; }
    public String getAuthorizationStatus() { return authorizationStatus; }
    public String getProviderName() { return providerName; }
    public String getProcessedAt() { return processedAt; }
    public String getCardLast4() { return cardLast4; }
}
