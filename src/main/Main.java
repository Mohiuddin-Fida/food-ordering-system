package main;

import java.util.Scanner;

import model.Administrator;
import model.Cart;
import model.CardPayment;
import model.Customer;
import model.DeliveryRider;
import model.Menu;
import model.MenuItem;
import model.Notifiable;
import model.Order;
import model.Payment;
import model.RestaurantStaff;
import model.User;
import model.WalletPayment;
import service.NotificationService;
import service.OrderController;

/**
 * Console demo of the Food Ordering System.
 * Use cases covered: UC-01 Register, UC-02 Log In, UC-03 Browse Menu, UC-04 Search,
 * UC-05 Place Order, UC-06 Make Payment, UC-07 Track Order, UC-08 Cancel Order,
 * UC-09 Manage Menu, UC-10 Process Order, UC-11 Deliver Order, UC-12 Manage Users.
 */
public class Main {
    private static Scanner in = new Scanner(System.in);
    private static Menu menu;
    private static User[] users = new User[30];
    private static DeliveryRider[] riders = new DeliveryRider[2];
    private static NotificationService notifications;
    private static OrderController controller;
    private static int paymentCounter = 1;
    private static int userCounter = 100;
    private static int cartCounter = 1;

    public static void main(String[] args) {
        setupData();
        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("===== FOOD ORDERING SYSTEM =====");
            System.out.println("1. Register account (UC-01)");
            System.out.println("2. Log in (UC-02)");
            System.out.println("3. Browse menu (UC-03)");
            System.out.println("4. Search food items (UC-04)");
            System.out.println("5. Polymorphism demo");
            System.out.println("0. Exit");
            int choice = readInt("Choose an option: ");
            switch (choice) {
                case 1:
                    registerCustomer();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    menu.displayMenu();
                    break;
                case 4:
                    searchMenu();
                    break;
                case 5:
                    polymorphismDemo();
                    break;
                case 0:
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }

    // ---------------------------------------------------------------- setup
    private static void setupData() {
        menu = new Menu("M-1", "Main Menu");
        menu.addItem(new MenuItem("F1", "Zinger Burger", "Crispy chicken burger", 450, "Burger"));
        menu.addItem(new MenuItem("F2", "Beef Burger", "Grilled beef patty", 550, "Burger"));
        menu.addItem(new MenuItem("F3", "Chicken Tikka Pizza", "Medium pizza", 1200, "Pizza"));
        menu.addItem(new MenuItem("F4", "Fajita Pizza", "Large pizza", 1800, "Pizza"));
        menu.addItem(new MenuItem("F5", "Chicken Karahi", "Half karahi with naan", 1500, "Desi"));
        menu.addItem(new MenuItem("F6", "French Fries", 200));
        menu.addItem(new MenuItem("F7", "Cold Drink", 120));
        MenuItem brownie = new MenuItem("F8", "Brownie", "Chocolate brownie", 250, "Dessert");
        brownie.setAvailability(false);                 // example of an unavailable item
        menu.addItem(brownie);

        notifications = new NotificationService("NS-1", "Console");
        controller = new OrderController("OC-1", notifications);

        Administrator admin = new Administrator("U1", "System Admin", "admin@food.pk", "admin123", "A-01", "Full");
        RestaurantStaff staff = new RestaurantStaff("U2", "Chef Bilal", "staff@food.pk", "staff123", "S-01", "Head Chef");
        riders[0] = new DeliveryRider("U3", "Usman", "rider1@food.pk", "rider123", "R-01", "03111234567", "Bike");
        riders[1] = new DeliveryRider("U4", "Hamza", "rider2@food.pk", "rider123", "R-02", "03221234567", "Bike");
        Customer demo = new Customer("U5", "Ali Khan", "ali@mail.com", "ali12345", "03001234567", "Street 5, Hayatabad, Peshawar");

        admin.createUser(admin, users);
        admin.createUser(staff, users);
        admin.createUser(riders[0], users);
        admin.createUser(riders[1], users);
        admin.createUser(demo, users);
        notifications.subscribeStaff(staff);
    }

    // ------------------------------------------------------------ input helpers
    private static String readLine(String prompt) {
        System.out.print(prompt);
        return in.nextLine().trim();
    }

    // Returns -1 when the text is not a valid whole number
    private static int readInt(String prompt) {
        String text = readLine(prompt);
        if (text.length() > 0 && text.length() <= 9 && text.matches("[0-9]+")) {
            return Integer.parseInt(text);
        }
        return -1;
    }

    private static User findByEmail(String email) {
        for (int i = 0; i < users.length; i++) {
            if (users[i] != null && users[i].getEmail().equalsIgnoreCase(email)) {
                return users[i];
            }
        }
        return null;
    }

    private static Administrator getAdmin() {
        return (Administrator) users[0];            // reference type casting
    }

    // ------------------------------------------------- UC-01 Register Account
    private static void registerCustomer() {
        System.out.println("--- Register a new customer ---");
        String name = readLine("Full name: ");
        String email = readLine("Email: ");
        String phone = readLine("Phone (11 digits, starts with 03): ");
        String password = readLine("Password (min 6 characters): ");

        if (name.length() == 0) {
            System.out.println("Registration failed: name is required.");
        } else if (!email.contains("@") || !email.contains(".")) {
            System.out.println("Registration failed: invalid email.");
        } else if (findByEmail(email) != null) {
            System.out.println("Registration failed: this email is already registered.");
        } else if (phone.length() != 11 || !phone.startsWith("03") || !phone.matches("[0-9]+")) {
            System.out.println("Registration failed: invalid phone number.");
        } else if (password.length() < 6) {
            System.out.println("Registration failed: password is too short.");
        } else {
            userCounter++;
            Customer c = new Customer("U" + userCounter, name, email, password, phone);   // 5-arg constructor
            if (getAdmin().createUser(c, users)) {
                System.out.println("Account created. Your user ID is " + c.getUserId() + ". You can log in now.");
            } else {
                System.out.println("Registration failed: user limit reached.");
            }
        }
    }

    // ------------------------------------------------------- UC-02 Log In
    private static void login() {
        String email = readLine("Email: ");
        String password = readLine("Password: ");
        User user = findByEmail(email);
        if (user == null || !user.authenticate(password)) {
            System.out.println("Login failed: wrong email/password or account deactivated.");
            return;
        }
        System.out.println("Welcome, " + user.getName() + " (" + user.getRole() + ")");
        String role = user.getRole();
        if (role.equals("Customer")) {
            customerSession((Customer) user);
        } else if (role.equals("Staff")) {
            staffSession((RestaurantStaff) user);
        } else if (role.equals("Rider")) {
            riderSession((DeliveryRider) user);
        } else if (role.equals("Admin")) {
            adminSession((Administrator) user);
        }
    }

    // ------------------------------------------------- UC-03 / UC-04 helpers
    private static void searchMenu() {
        System.out.println("1. Search by name or category   2. Search by price range");
        int type = readInt("Choose: ");
        if (type == 1) {
            String q = readLine("Search text: ");
            Menu.printItems(menu.searchItems(q));                       // overloaded searchItems(String)
        } else if (type == 2) {
            int min = readInt("Minimum price: ");
            int max = readInt("Maximum price: ");
            if (min >= 0 && max >= min) {
                Menu.printItems(menu.searchItems((double) min, (double) max));   // searchItems(double, double)
            } else {
                System.out.println("Invalid price range.");
            }
        } else {
            System.out.println("Invalid option.");
        }
    }

    // ------------------------------------------------------ customer session
    private static void customerSession(Customer customer) {
        Cart cart = new Cart("C-" + cartCounter, customer.getUserId());
        cartCounter++;
        boolean active = true;
        while (active) {
            System.out.println();
            System.out.println("--- Customer menu: " + customer.getName() + " ---");
            System.out.println("1. Browse menu   2. Search food   3. Add item to cart   4. Remove item from cart");
            System.out.println("5. View cart     6. Checkout and pay (UC-05/06)   7. Track order (UC-07)");
            System.out.println("8. Cancel order (UC-08)   9. Order history   0. Log out");
            int choice = readInt("Choose: ");
            switch (choice) {
                case 1:
                    menu.displayMenu();
                    break;
                case 2:
                    searchMenu();
                    break;
                case 3:
                    addToCart(cart);
                    break;
                case 4:
                    String removeId = readLine("Item ID to remove: ");
                    if (cart.removeItem(removeId)) {
                        System.out.println("Item removed.");
                    } else {
                        System.out.println("Item not in cart.");
                    }
                    break;
                case 5:
                    cart.displayCart();
                    break;
                case 6:
                    checkout(customer, cart);
                    break;
                case 7:
                    String trackId = readLine("Order ID: ");
                    System.out.println("Status of " + trackId + ": " + customer.trackOrder(trackId));
                    break;
                case 8:
                    cancelOrder(customer);
                    break;
                case 9:
                    showHistory(customer);
                    break;
                case 0:
                    active = false;
                    System.out.println("Logged out.");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void addToCart(Cart cart) {
        String id = readLine("Item ID (e.g. F1): ");
        MenuItem item = menu.getItemById(id);
        if (item == null) {
            System.out.println("No such item.");
            return;
        }
        String qtyText = readLine("Quantity (press Enter for 1): ");
        boolean added;
        if (qtyText.length() == 0) {
            added = cart.addItem(item);                  // overloaded addItem(MenuItem)
        } else if (qtyText.matches("[0-9]+") && qtyText.length() <= 3) {
            added = cart.addItem(item, Integer.parseInt(qtyText));   // overloaded addItem(MenuItem, int)
        } else {
            added = false;
        }
        if (added) {
            System.out.println(item.getName() + " added to cart.");
        } else {
            System.out.println("Could not add item (unavailable, or quantity not between 1 and 20).");
        }
    }

    // ------------------------------------------- UC-05 Place Order + UC-06 Pay
    private static void checkout(Customer customer, Cart cart) {
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty. Add items from the menu first.");
            return;
        }
        System.out.println("--- Order summary ---");
        cart.displayCart();
        double subtotal = cart.calculateTotal();
        double tax = controller.calculateTax(subtotal);
        double delivery = controller.calculateDeliveryCharge(subtotal);
        System.out.println("   Tax (10%): Rs. " + tax);
        System.out.println("   Delivery charge: Rs. " + delivery);
        System.out.println("   TOTAL PAYABLE: Rs. " + controller.calculatePayable(subtotal));

        String address = readLine("Delivery address [" + customer.getDefaultAddress() + "] (Enter to keep): ");
        if (address.length() > 0) {
            customer.setDefaultAddress(address);
        }
        if (customer.getDefaultAddress().length() == 0) {
            System.out.println("Delivery address is required. Checkout cancelled (your cart is kept).");
            return;
        }
        String phone = readLine("Contact number [" + customer.getPhone() + "] (Enter to keep): ");
        if (phone.length() > 0) {
            customer.setPhone(phone);
        }

        System.out.println("Payment method: 1. Card   2. Wallet");
        int method = readInt("Choose: ");
        Payment payment = null;
        String paymentId = "PAY-" + paymentCounter;
        if (method == 1) {
            String last4 = readLine("Enter the LAST 4 digits of your card (0000 = declined card): ");
            if (!last4.matches("[0-9]{4}")) {
                System.out.println("Invalid card digits. Checkout cancelled.");
                return;
            }
            payment = new CardPayment(paymentId, "Visa", last4);        // parent reference holds a child
        } else if (method == 2) {
            System.out.println("Wallet: 1. JazzCash   2. Easypaisa");
            int w = readInt("Choose: ");
            if (w == 1) {
                payment = new WalletPayment(paymentId, "JazzCash");
            } else if (w == 2) {
                payment = new WalletPayment(paymentId, "Easypaisa");
            } else {
                System.out.println("Invalid wallet. Checkout cancelled.");
                return;
            }
        } else {
            System.out.println("Invalid payment method. Checkout cancelled.");
            return;
        }
        paymentCounter++;

        Order order = customer.placeOrder(cart, controller, payment);
        if (order == null) {
            System.out.println("ORDER NOT PLACED: " + controller.getLastMessage());
            System.out.println("   " + payment.getReceipt());
        } else {
            System.out.println("ORDER CONFIRMED! Your order ID is " + order.getOrderId());
            System.out.println("   " + payment.getReceipt());
        }
    }

    private static void cancelOrder(Customer customer) {
        String id = readLine("Order ID to cancel: ");
        Order order = customer.findOrder(id);
        if (order == null) {
            System.out.println("Order not found.");
        } else if (customer.cancelOrder(id)) {
            System.out.println("Order " + id + " cancelled. Payment status: " + order.getPayment().getStatus());
        } else {
            System.out.println("Order cannot be cancelled now (current status: " + order.getStatus() + ").");
        }
    }

    private static void showHistory(Customer customer) {
        Order[] history = customer.viewOrderHistory();
        if (customer.getOrderCount() == 0) {
            System.out.println("   You have no orders yet.");
        }
        for (int i = 0; i < customer.getOrderCount(); i++) {
            System.out.println("   " + history[i].getSummary());
        }
    }

    // ---------------------------------------------------------- staff session
    private static void staffSession(RestaurantStaff staff) {
        boolean active = true;
        while (active) {
            System.out.println();
            System.out.println("--- Staff menu: " + staff.getName() + " ---");
            System.out.println("1. View incoming orders   2. Accept order   3. Reject order");
            System.out.println("4. Update order status (Preparing / Ready)   5. Manage menu (UC-09)");
            System.out.println("6. Sales summary   0. Log out");
            int choice = readInt("Choose: ");
            switch (choice) {
                case 1:
                    Order[] incoming = controller.getOrdersByStatus("Placed");
                    if (incoming.length == 0) {
                        System.out.println("   No pending orders.");
                    }
                    for (int i = 0; i < incoming.length; i++) {
                        incoming[i].displayDetails();
                    }
                    break;
                case 2:
                    Order toAccept = controller.findOrder(readLine("Order ID: "));
                    if (toAccept == null) {
                        System.out.println("Order not found.");
                    } else if (staff.acceptOrder(toAccept, controller)) {
                        System.out.println("Order accepted.");
                    } else {
                        System.out.println("Cannot accept: " + controller.getLastMessage());
                    }
                    break;
                case 3:
                    Order toReject = controller.findOrder(readLine("Order ID: "));
                    String reason = readLine("Reason: ");
                    if (toReject == null) {
                        System.out.println("Order not found.");
                    } else if (staff.rejectOrder(toReject, reason, controller)) {
                        System.out.println("Order rejected and refund started.");
                    } else {
                        System.out.println("Cannot reject: " + controller.getLastMessage());
                    }
                    break;
                case 4:
                    updateStatus(staff);
                    break;
                case 5:
                    manageMenu(staff);
                    break;
                case 6:
                    salesSummary();
                    break;
                case 0:
                    active = false;
                    System.out.println("Logged out.");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void updateStatus(RestaurantStaff staff) {
        Order order = controller.findOrder(readLine("Order ID: "));
        if (order == null) {
            System.out.println("Order not found.");
            return;
        }
        System.out.println("Current status: " + order.getStatus());
        System.out.println("1. Preparing   2. Ready for Delivery");
        int s = readInt("Choose: ");
        String newStatus = "";
        if (s == 1) {
            newStatus = "Preparing";
        } else if (s == 2) {
            newStatus = "Ready for Delivery";
        } else {
            System.out.println("Invalid option.");
            return;
        }
        if (staff.updateOrderStatus(order, newStatus, controller)) {
            System.out.println("Status updated to " + newStatus);
            if (s == 2 && !controller.assignRider(order, riders)) {
                System.out.println("No rider is available right now.");
            }
        } else {
            System.out.println("Cannot update: " + controller.getLastMessage());
        }
    }

    private static void manageMenu(RestaurantStaff staff) {
        System.out.println("1. Add item  2. Remove item  3. Mark unavailable  4. Mark available");
        int c = readInt("Choose: ");
        if (c == 1) {
            String id = readLine("New item ID: ");
            String name = readLine("Name: ");
            String category = readLine("Category: ");
            int price = readInt("Price (whole rupees): ");
            if (id.length() == 0 || name.length() == 0 || price <= 0) {
                System.out.println("Invalid item details.");
                return;
            }
            MenuItem item = new MenuItem(id, name, "", (double) price, category);
            if (staff.manageMenu(item, "ADD", menu)) {
                System.out.println("Item added.");
            } else {
                System.out.println("Could not add item (duplicate ID or menu full).");
            }
        } else if (c >= 2 && c <= 4) {
            MenuItem item = menu.getItemById(readLine("Item ID: "));
            if (item == null) {
                System.out.println("No such item.");
                return;
            }
            String action = "REMOVE";
            if (c == 3) {
                action = "UNAVAILABLE";
            } else if (c == 4) {
                action = "AVAILABLE";
            }
            staff.manageMenu(item, action, menu);
            System.out.println("Done: " + action + " " + item.getName());
        } else {
            System.out.println("Invalid option.");
        }
    }

    private static void salesSummary() {
        Order[] all = controller.getOrders();
        double total = 0;
        int delivered = 0;
        for (int i = 0; i < controller.getOrderCount(); i++) {
            if (all[i].getStatus().equals("Delivered")) {
                total = total + all[i].getTotalAmount();
                delivered++;
            }
        }
        System.out.println("   Orders placed: " + controller.getOrderCount());
        System.out.println("   Delivered orders: " + delivered + " | Sales: Rs. " + total);
        if (delivered > 0) {
            double average = total / delivered;
            int rounded = (int) average;                      // type casting double -> int
            System.out.println("   Average order value (rounded down): Rs. " + rounded);
        }
    }

    // ---------------------------------------------------------- rider session
    private static void riderSession(DeliveryRider rider) {
        boolean active = true;
        while (active) {
            System.out.println();
            System.out.println("--- Rider menu: " + rider.getName() + " ---");
            System.out.println("1. View assigned deliveries   2. Accept delivery");
            System.out.println("3. Mark Out for Delivery   4. Mark Delivered   0. Log out");
            int choice = readInt("Choose: ");
            switch (choice) {
                case 1:
                    Order[] mine = rider.viewAssignedDeliveries();
                    if (rider.getAssignedCount() == 0) {
                        System.out.println("   No deliveries assigned.");
                    }
                    for (int i = 0; i < rider.getAssignedCount(); i++) {
                        System.out.println("   " + mine[i].getSummary() + " | " + mine[i].getDeliveryAddress());
                    }
                    break;
                case 2:
                    if (rider.acceptDelivery(readLine("Order ID: "))) {
                        System.out.println("Delivery accepted.");
                    } else {
                        System.out.println("This order is not assigned to you.");
                    }
                    break;
                case 3:
                    riderUpdate(rider, "Out for Delivery");
                    break;
                case 4:
                    riderUpdate(rider, "Delivered");
                    break;
                case 0:
                    active = false;
                    System.out.println("Logged out.");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void riderUpdate(DeliveryRider rider, String status) {
        String id = readLine("Order ID: ");
        if (rider.updateDeliveryStatus(id, status, controller)) {
            System.out.println("Delivery status updated to " + status);
        } else {
            System.out.println("Cannot update (not your order or wrong status order).");
        }
    }

    // ---------------------------------------------------------- admin session
    private static void adminSession(Administrator admin) {
        boolean active = true;
        while (active) {
            System.out.println();
            System.out.println("--- Admin menu: " + admin.getName() + " ---");
            System.out.println("1. List users   2. Deactivate user   3. Reset password   0. Log out");
            int choice = readInt("Choose: ");
            switch (choice) {
                case 1:
                    for (int i = 0; i < users.length; i++) {
                        if (users[i] != null) {
                            System.out.println("   " + users[i].getSummary());
                        }
                    }
                    break;
                case 2:
                    if (admin.deactivateUser(readLine("User ID: "), users)) {
                        System.out.println("User deactivated.");
                    } else {
                        System.out.println("User not found (or cannot deactivate yourself).");
                    }
                    break;
                case 3:
                    String id = readLine("User ID: ");
                    if (admin.resetPassword(id, users)) {
                        System.out.println("Password reset to: reset" + id);
                    } else {
                        System.out.println("User not found.");
                    }
                    break;
                case 0:
                    active = false;
                    System.out.println("Logged out.");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // ------------------------------------------------------ polymorphism demo
    private static void polymorphismDemo() {
        System.out.println("=== Runtime polymorphism 1: Payment[] (parent type, child objects) ===");
        Payment[] payments = new Payment[3];
        payments[0] = new CardPayment("DEMO-1", "Visa", "4242");
        payments[1] = new WalletPayment("DEMO-2", "JazzCash");
        payments[2] = new WalletPayment("DEMO-3", "Easypaisa", 1000);
        for (int i = 0; i < payments.length; i++) {
            boolean ok = payments[i].process(1500);      // same call, different behaviour
            System.out.println("   process(1500) -> " + ok + " | " + payments[i].getReceipt());
        }

        System.out.println("=== Runtime polymorphism 2: User[] and getPermissions() ===");
        for (int i = 0; i < users.length; i++) {
            if (users[i] != null) {
                String[] perms = users[i].getPermissions();
                String line = "";
                for (int j = 0; j < perms.length; j++) {
                    line = line + perms[j];
                    if (j < perms.length - 1) {
                        line = line + ", ";
                    }
                }
                System.out.println("   " + users[i].getRole() + " " + users[i].getName() + ": " + line);
            }
        }

        System.out.println("=== Interface: Notifiable[] ===");
        Notifiable[] receivers = new Notifiable[3];
        receivers[0] = (Customer) users[4];
        receivers[1] = (RestaurantStaff) users[1];
        receivers[2] = riders[0];
        for (int i = 0; i < receivers.length; i++) {
            receivers[i].receiveNotification("Test message for " + receivers[i].getNotificationName());
        }
    }
}
