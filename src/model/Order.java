package model;

public class Order {
    private String orderId;
    private String status;
    private String deliveryAddress;
    private double totalAmount;
    private Customer customer;              // the customer who placed the order
    private Payment payment;                // is paid through (CardPayment or WalletPayment)
    private RestaurantStaff processedBy;
    private DeliveryRider rider;
    private String rejectReason;
    private boolean saved;
    // COMPOSITION: the order owns its OrderItem objects
    private OrderItem[] items = new OrderItem[20];
    private int itemCount;

    // Constructor 1
    public Order(String orderId, Customer customer, String deliveryAddress, Payment payment) {
        this.orderId = orderId;
        this.customer = customer;
        this.deliveryAddress = deliveryAddress;
        this.payment = payment;
        this.status = "Pending";
        this.rejectReason = "";
        this.saved = false;
        this.itemCount = 0;
    }

    // Constructor 2 (overloading): payment attached later
    public Order(String orderId, Customer customer, String deliveryAddress) {
        this(orderId, customer, deliveryAddress, null);
    }

    public boolean addItem(OrderItem item) {
        if (itemCount < items.length) {
            items[itemCount] = item;
            itemCount++;
            return true;
        }
        return false;
    }

    // Sum of all order lines (before tax and delivery charge)
    public double calculateTotal() {
        double sum = 0;
        for (int i = 0; i < itemCount; i++) {
            sum = sum + items[i].getSubtotal();
        }
        return sum;
    }

    // Only the transitions of the Order state machine (Assignment 2) are allowed.
    public boolean updateStatus(String newStatus) {
        boolean allowed = false;
        switch (status) {
            case "Pending":
                allowed = newStatus.equals("Placed");
                break;
            case "Placed":
                allowed = newStatus.equals("Confirmed") || newStatus.equals("Rejected")
                        || newStatus.equals("Cancelled");
                break;
            case "Confirmed":
                allowed = newStatus.equals("Preparing") || newStatus.equals("Cancelled");
                break;
            case "Preparing":
                allowed = newStatus.equals("Ready for Delivery");
                break;
            case "Ready for Delivery":
                allowed = newStatus.equals("Out for Delivery");
                break;
            case "Out for Delivery":
                allowed = newStatus.equals("Delivered");
                break;
            default:
                allowed = false;
        }
        if (allowed) {
            status = newStatus;
        }
        return allowed;
    }

    // A customer may cancel only before preparation starts (FR-09)
    public boolean cancel() {
        if (status.equals("Placed") || status.equals("Confirmed")) {
            updateStatus("Cancelled");
            if (payment != null && payment.isApproved()) {
                payment.refund(totalAmount);
            }
            return true;
        }
        return false;
    }

    public boolean save() {
        if (orderId != null && itemCount > 0) {
            saved = true;
        }
        return saved;
    }

    public String getSummary() {
        return orderId + " | " + customer.getName() + " | " + status + " | Total: Rs. " + totalAmount;
    }

    public void displayDetails() {
        System.out.println("   Order " + orderId + " (" + status + ")");
        System.out.println("   Customer: " + customer.getName() + " | Address: " + deliveryAddress);
        for (int i = 0; i < itemCount; i++) {
            System.out.println("     - " + items[i].getItemName() + " x" + items[i].getQuantity()
                    + " = Rs. " + items[i].getLineTotal());
        }
        System.out.println("   Total payable: Rs. " + totalAmount);
    }

    public String getOrderId() { return orderId; }
    public String getStatus() { return status; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public Customer getCustomer() { return customer; }
    public Payment getPayment() { return payment; }
    public void setPayment(Payment payment) { this.payment = payment; }
    public RestaurantStaff getProcessedBy() { return processedBy; }
    public void setProcessedBy(RestaurantStaff processedBy) { this.processedBy = processedBy; }
    public DeliveryRider getRider() { return rider; }
    public void setRider(DeliveryRider rider) { this.rider = rider; }
    public String getRejectReason() { return rejectReason; }
    public void setRejectReason(String rejectReason) { this.rejectReason = rejectReason; }
    public boolean isSaved() { return saved; }
    public int getItemCount() { return itemCount; }
}
