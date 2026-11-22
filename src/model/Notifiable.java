package model;

/**
 * Interface: a capability that unrelated roles (Customer, RestaurantStaff,
 * DeliveryRider) share - they can all receive a notification message.
 */
public interface Notifiable {
    void receiveNotification(String message);

    String getNotificationName();
}
