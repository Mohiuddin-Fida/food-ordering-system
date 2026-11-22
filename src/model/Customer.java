package model;

import service.OrderController;

/** Child of User (hierarchical inheritance). Implements Notifiable. */
public class Customer extends User implements Notifiable {
    private String phone;
    private String defaultAddress;
    private String createdAt;
    private Order[] orderHistory = new Order[20];
    private int orderCount;

    // Constructor 1
    public Customer(String userId, String name, String email, String password,
                    String phone, String defaultAddress) {
        super(userId, name, email, password, "Customer");
        this.phone = phone;
        this.defaultAddress = defaultAddress;
        this.createdAt = "registered-" + userId;
        this.orderCount = 0;
    }

    // Constructor 2 (overloading): address is added later at checkout
    public Customer(String userId, String name, String email, String password, String phone) {
        this(userId, name, email, password, phone, "");
    }

    public MenuItem[] browseMenu(Menu menu) {
        return menu.getItems();
    }

    public Order placeOrder(Cart cart, OrderController controller, Payment payment) {
        return controller.placeOrder(this, cart, payment);
    }

    public boolean addOrder(Order order) {
        if (orderCount < orderHistory.length) {
            orderHistory[orderCount] = order;
            orderCount++;
            return true;
        }
        return false;
    }

    public Order findOrder(String orderId) {
        for (int i = 0; i < orderCount; i++) {
            if (orderHistory[i].getOrderId().equals(orderId)) {
                return orderHistory[i];
            }
        }
        return null;
    }

    public String trackOrder(String orderId) {
        Order order = findOrder(orderId);
        if (order == null) {
            return "Order not found";
        }
        return order.getStatus();
    }

    public boolean cancelOrder(String orderId) {
        Order order = findOrder(orderId);
        if (order == null) {
            return false;
        }
        return order.cancel();
    }

    public Order[] viewOrderHistory() {
        return orderHistory;
    }

    // Overrides the abstract method of User
    @Override
    public String[] getPermissions() {
        return new String[] { "Browse menu", "Place order", "Track order", "Cancel own order" };
    }

    // Overrides User.getSummary() and reuses it through super
    @Override
    public String getSummary() {
        return super.getSummary() + " | Phone: " + phone;
    }

    @Override
    public void receiveNotification(String message) {
        System.out.println("   [SMS to customer " + getName() + "] " + message);
    }

    @Override
    public String getNotificationName() { return getName(); }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getDefaultAddress() { return defaultAddress; }
    public void setDefaultAddress(String defaultAddress) { this.defaultAddress = defaultAddress; }
    public String getCreatedAt() { return createdAt; }
    public int getOrderCount() { return orderCount; }
}
