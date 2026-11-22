package service;

import model.Cart;
import model.CartItem;
import model.Customer;
import model.DeliveryRider;
import model.Order;
import model.OrderItem;
import model.Payment;

/** Coordinates the Place Order (UC-05) and Process Order (UC-10) workflows. */
public class OrderController {
    private String controllerId;
    private String sessionId;
    private String currentCustomerId;
    private String lastRequestTime;
    private String lastMessage;
    private NotificationService notificationService;   // uses (one-way association)
    private Order[] orders = new Order[50];
    private int orderCount;
    private int orderCounter = 1000;

    public OrderController(String controllerId, NotificationService notificationService) {
        this.controllerId = controllerId;
        this.notificationService = notificationService;
        this.sessionId = "S-1";
        this.currentCustomerId = "";
        this.lastRequestTime = "";
        this.lastMessage = "";
        this.orderCount = 0;
    }

    public boolean validateCart(Cart cart) {
        return cart.validate();
    }

    // UC-05 Place Order (follows the stepwise refinement from Assignment 3)
    public Order placeOrder(Customer customer, Cart cart, Payment payment) {
        lastRequestTime = String.valueOf(System.currentTimeMillis());
        currentCustomerId = customer.getUserId();

        if (!validateCart(cart)) {
            lastMessage = "Cart is empty or contains an invalid / unavailable item";
            return null;
        }
        String address = customer.getDefaultAddress();
        if (address == null || address.length() == 0) {
            lastMessage = "Delivery address required";
            return null;
        }
        if (orderCount >= orders.length) {
            lastMessage = "System is busy, please try again later";
            return null;
        }

        double subtotal = cart.calculateTotal();
        double total = calculatePayable(subtotal);

        boolean approved = payment.process(total);       // dynamic binding: Card or Wallet
        if (!approved) {
            lastMessage = "Payment failed; no order created";
            return null;
        }
        payment.verify();

        String orderId = generateOrderId();
        Order order = new Order(orderId, customer, address, payment);
        for (int i = 0; i < cart.getItemCount(); i++) {
            CartItem ci = cart.getItem(i);
            order.addItem(new OrderItem(ci.getMenuItem().getName(), ci.getQuantity(),
                    ci.getUnitPrice(), ci.getSpecialInstructions()));
        }
        order.updateStatus("Placed");
        order.setTotalAmount(total);

        if (!order.save()) {                              // reliability safeguard from Assignment 3
            payment.refund(total);
            lastMessage = "Order could not be saved; payment refunded";
            return null;
        }

        cart.clear();
        customer.addOrder(order);
        orders[orderCount] = order;
        orderCount++;
        notificationService.notifyStaff(order);
        notificationService.notifyCustomer(order, "Order " + orderId + " placed successfully.");
        lastMessage = "Order placed";
        return order;
    }

    // UC-10 Process Order: confirm / prepare / ready / out for delivery / delivered
    public boolean processOrder(Order order, String status) {
        if (!order.updateStatus(status)) {
            lastMessage = "Invalid status change from " + order.getStatus() + " to " + status;
            return false;
        }
        notificationService.notifyCustomer(order, "Order " + order.getOrderId()
                + " status changed to: " + status);
        return true;
    }

    public boolean rejectOrder(Order order, String reason) {
        if (!order.updateStatus("Rejected")) {
            lastMessage = "Only a Placed order can be rejected";
            return false;
        }
        order.setRejectReason(reason);
        if (order.getPayment() != null && order.getPayment().isApproved()) {
            order.getPayment().refund(order.getTotalAmount());
        }
        notificationService.notifyCustomer(order, "Order " + order.getOrderId()
                + " was rejected (" + reason + "). Your payment is refunded.");
        return true;
    }

    // An array of riders is passed in; the first available rider gets the delivery.
    public boolean assignRider(Order order, DeliveryRider[] riders) {
        for (int i = 0; i < riders.length; i++) {
            if (riders[i] != null && riders[i].getAvailabilityStatus().equals("Available")) {
                riders[i].receiveAssignment(order);
                notificationService.notifyRider(order);
                return true;
            }
        }
        return false;
    }

    // Returns an array that holds the orders with the given status
    public Order[] getOrdersByStatus(String status) {
        int matches = countOrdersWithStatus(orders, orderCount, status);
        Order[] result = new Order[matches];
        int pos = 0;
        for (int i = 0; i < orderCount; i++) {
            if (orders[i].getStatus().equals(status)) {
                result[pos] = orders[i];
                pos++;
            }
        }
        return result;
    }

    // An array is passed to this method
    public static int countOrdersWithStatus(Order[] list, int count, String status) {
        int total = 0;
        for (int i = 0; i < count; i++) {
            if (list[i].getStatus().equals(status)) {
                total++;
            }
        }
        return total;
    }

    public Order findOrder(String orderId) {
        for (int i = 0; i < orderCount; i++) {
            if (orders[i].getOrderId().equalsIgnoreCase(orderId)) {
                return orders[i];
            }
        }
        return null;
    }

    // Arithmetic operators: subtotal + tax + delivery charge
    public double calculatePayable(double subtotal) {
        return subtotal + calculateTax(subtotal) + calculateDeliveryCharge(subtotal);
    }

    public double calculateTax(double subtotal) {
        return subtotal * 0.10;                   // 10 % sales tax
    }

    public double calculateDeliveryCharge(double subtotal) {
        if (subtotal >= 2000) {
            return 0;                             // free delivery for big orders
        } else {
            return 100;
        }
    }

    private String generateOrderId() {
        orderCounter++;
        return "ORD-" + orderCounter;
    }

    public Order[] getOrders() { return orders; }
    public int getOrderCount() { return orderCount; }
    public String getLastMessage() { return lastMessage; }
    public String getControllerId() { return controllerId; }
    public String getSessionId() { return sessionId; }
    public String getCurrentCustomerId() { return currentCustomerId; }
    public String getLastRequestTime() { return lastRequestTime; }
}
