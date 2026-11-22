# Food Ordering System (Java Console Version)

**Course:** Software Design & Analysis (SDA), BS (CS 9A), Fall 2026
**Assignment:** 4 - Java Implementation: Classes, Inheritance & Polymorphism
**Name:** Mohiuddin
**Registration No:** 24P-0588

A console version of the Food Ordering System designed in Assignments 1-3. Customers register, browse and search the menu, fill a cart, pay by card or wallet and track or cancel orders. Restaurant staff accept and prepare orders, riders deliver them, and the administrator manages user accounts.

## Packages and classes

| Package   | Classes |
|-----------|---------|
| `model`   | `User` (abstract), `Customer`, `RestaurantStaff`, `DeliveryRider`, `Administrator`, `Menu`, `MenuItem`, `Cart`, `CartItem`, `Order`, `OrderItem`, `Payment` (abstract), `CardPayment`, `WalletPayment`, `Notifiable` (interface) |
| `service` | `NotificationService`, `OrderController` |
| `main`    | `Main` (console menu, starts with `public static void main`) |

Inheritance (hierarchical): `User` -> `Customer`, `RestaurantStaff`, `DeliveryRider`, `Administrator`; `Payment` -> `CardPayment`, `WalletPayment`.
Composition: `Cart` has `CartItem[]`, `Order` has `OrderItem[]`.
Interface: `Notifiable` is implemented by `Customer`, `RestaurantStaff` and `DeliveryRider`.

## Use cases covered (from Assignment 1)

UC-01 Register, UC-02 Log In, UC-03 Browse Menu, UC-04 Search Food Items, UC-05 Place Order, UC-06 Make Payment, UC-07 Track Order, UC-08 Cancel Order, UC-09 Manage Menu, UC-10 Process Order, UC-11 Deliver Order, UC-12 Manage Users.

## How to run

**Eclipse:** File > New > Java Project, copy the `src` folder into the project (keep the `model`, `service` and `main` packages), then right-click `src/main/Main.java` > Run As > Java Application.

**Command line (JDK 8 or newer):**
```
javac -d bin src/model/*.java src/service/*.java src/main/*.java
java -cp bin main.Main
```

## Demo accounts

| Role | Email | Password |
|------|-------|----------|
| Customer | ali@mail.com | ali12345 |
| Restaurant staff | staff@food.pk | staff123 |
| Delivery rider | rider1@food.pk | rider123 |
| Administrator | admin@food.pk | admin123 |

New customers can register from the main menu.

## Quick demo flow

1. Register (or log in as the customer), add items to the cart, checkout and pay (card ending `0000` is declined on purpose).
2. Log out, log in as staff: view incoming orders, accept, set Preparing, set Ready for Delivery.
3. Log out, log in as the rider: accept the delivery, mark Out for Delivery and Delivered.
4. Log in as the customer again and track the order.

Main menu option 5 shows the polymorphism demo.

## Notes

- Payments are simulated; no real gateway is used. Card numbers and CVV are never stored (only the last 4 digits).
- Data is kept in memory (arrays) and is lost when the program ends.
