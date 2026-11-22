package service;

import model.DeliveryRider;
import model.Notifiable;
import model.Order;

/** Centralizes all notifications (FR-13). */
public class NotificationService {
    private String serviceId;
    private String channel;
    private String lastSentAt;
    private String deliveryStatus;
    private Notifiable[] staffSubscribers = new Notifiable[5];
    private int staffCount;

    // Constructor 1
    public NotificationService(String serviceId, String channel) {
        this.serviceId = serviceId;
        this.channel = channel;
        this.lastSentAt = "never";
        this.deliveryStatus = "Idle";
        this.staffCount = 0;
    }

    // Constructor 2 (overloading)
    public NotificationService(String serviceId) {
        this(serviceId, "Console");
    }

    public boolean subscribeStaff(Notifiable staff) {
        if (staffCount < staffSubscribers.length) {
            staffSubscribers[staffCount] = staff;
            staffCount++;
            return true;
        }
        return false;
    }

    public boolean notifyCustomer(Order order, String message) {
        Notifiable customer = order.getCustomer();
        if (customer == null) {
            deliveryStatus = "Failed";
            return false;
        }
        customer.receiveNotification(message);
        markSent();
        return true;
    }

    public boolean notifyStaff(Order order) {
        if (staffCount == 0) {
            deliveryStatus = "Failed";
            return false;
        }
        for (int i = 0; i < staffCount; i++) {
            staffSubscribers[i].receiveNotification("New order " + order.getOrderId()
                    + " placed by " + order.getCustomer().getName() + " (Rs. " + order.getTotalAmount() + ")");
        }
        markSent();
        return true;
    }

    public boolean notifyRider(Order order) {
        DeliveryRider rider = order.getRider();
        if (rider == null) {
            deliveryStatus = "Failed";
            return false;
        }
        rider.receiveNotification("Order " + order.getOrderId() + " is ready. Deliver to: "
                + order.getDeliveryAddress());
        markSent();
        return true;
    }

    private void markSent() {
        deliveryStatus = "Sent";
        lastSentAt = String.valueOf(System.currentTimeMillis());
    }

    public String getServiceId() { return serviceId; }
    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }
    public String getLastSentAt() { return lastSentAt; }
    public String getDeliveryStatus() { return deliveryStatus; }
}
