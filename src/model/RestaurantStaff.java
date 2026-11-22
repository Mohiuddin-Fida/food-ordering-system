package model;

import service.OrderController;

/** Child of User (hierarchical inheritance). Implements Notifiable. */
public class RestaurantStaff extends User implements Notifiable {
    private String staffId;
    private String jobTitle;
    private String shiftStatus;

    public RestaurantStaff(String userId, String name, String email, String password,
                           String staffId, String jobTitle) {
        super(userId, name, email, password, "Staff");
        this.staffId = staffId;
        this.jobTitle = jobTitle;
        this.shiftStatus = "On Shift";
    }

    // action: "ADD", "REMOVE", "AVAILABLE" or "UNAVAILABLE"
    public boolean manageMenu(MenuItem item, String action, Menu menu) {
        switch (action) {
            case "ADD":
                return menu.addItem(item);
            case "REMOVE":
                return menu.removeItem(item.getItemId());
            case "AVAILABLE":
                item.setAvailability(true);
                return true;
            case "UNAVAILABLE":
                item.setAvailability(false);
                return true;
            default:
                return false;
        }
    }

    public boolean acceptOrder(Order order, OrderController controller) {
        boolean done = controller.processOrder(order, "Confirmed");
        if (done) {
            order.setProcessedBy(this);
        }
        return done;
    }

    public boolean rejectOrder(Order order, String reason, OrderController controller) {
        boolean done = controller.rejectOrder(order, reason);
        if (done) {
            order.setProcessedBy(this);
        }
        return done;
    }

    public boolean updateOrderStatus(Order order, String status, OrderController controller) {
        return controller.processOrder(order, status);
    }

    @Override
    public String[] getPermissions() {
        return new String[] { "Manage menu", "Accept order", "Reject order", "Update order status" };
    }

    @Override
    public String getSummary() {
        return super.getSummary() + " | " + jobTitle + " (" + shiftStatus + ")";
    }

    @Override
    public void receiveNotification(String message) {
        System.out.println("   [Kitchen screen - " + getName() + "] " + message);
    }

    @Override
    public String getNotificationName() { return getName(); }

    public String getStaffId() { return staffId; }
    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public String getShiftStatus() { return shiftStatus; }
    public void setShiftStatus(String shiftStatus) { this.shiftStatus = shiftStatus; }
}
