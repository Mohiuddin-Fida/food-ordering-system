package model;

import service.OrderController;

/** Child of User (hierarchical inheritance). Implements Notifiable. */
public class DeliveryRider extends User implements Notifiable {
    private String riderId;
    private String phone;
    private String availabilityStatus;
    private String vehicleType;
    private Order[] assignedOrders = new Order[10];
    private int assignedCount;

    public DeliveryRider(String userId, String name, String email, String password,
                         String riderId, String phone, String vehicleType) {
        super(userId, name, email, password, "Rider");
        this.riderId = riderId;
        this.phone = phone;
        this.vehicleType = vehicleType;
        this.availabilityStatus = "Available";
        this.assignedCount = 0;
    }

    public Order[] viewAssignedDeliveries() {
        return assignedOrders;
    }

    public Order findAssigned(String orderId) {
        for (int i = 0; i < assignedCount; i++) {
            if (assignedOrders[i].getOrderId().equals(orderId)) {
                return assignedOrders[i];
            }
        }
        return null;
    }

    public boolean acceptDelivery(String orderId) {
        Order order = findAssigned(orderId);
        if (order == null) {
            return false;
        }
        availabilityStatus = "Busy";
        return true;
    }

    public boolean updateDeliveryStatus(String orderId, String status, OrderController controller) {
        Order order = findAssigned(orderId);
        if (order == null) {
            return false;
        }
        boolean done = controller.processOrder(order, status);
        if (done && status.equals("Delivered")) {
            availabilityStatus = "Available";
        }
        return done;
    }

    public void receiveAssignment(Order order) {
        if (assignedCount < assignedOrders.length) {
            assignedOrders[assignedCount] = order;
            assignedCount++;
            order.setRider(this);
        }
    }

    @Override
    public String[] getPermissions() {
        return new String[] { "View assigned deliveries", "Update delivery status" };
    }

    @Override
    public String getSummary() {
        return super.getSummary() + " | " + vehicleType + " (" + availabilityStatus + ")";
    }

    @Override
    public void receiveNotification(String message) {
        System.out.println("   [App alert to rider " + getName() + "] " + message);
    }

    @Override
    public String getNotificationName() { return getName(); }

    public String getRiderId() { return riderId; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAvailabilityStatus() { return availabilityStatus; }
    public void setAvailabilityStatus(String availabilityStatus) { this.availabilityStatus = availabilityStatus; }
    public String getVehicleType() { return vehicleType; }
    public int getAssignedCount() { return assignedCount; }
}
