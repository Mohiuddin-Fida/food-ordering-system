package model;

public class Cart {
    private String cartId;
    private String customerId;
    private String createdAt;
    // COMPOSITION: the cart owns its CartItem objects (array of CartItem)
    private CartItem[] items = new CartItem[20];
    private int itemCount;

    // Constructor 1
    public Cart(String cartId, String customerId, String createdAt) {
        this.cartId = cartId;
        this.customerId = customerId;
        this.createdAt = createdAt;
        this.itemCount = 0;
    }

    // Constructor 2 (overloading)
    public Cart(String cartId, String customerId) {
        this(cartId, customerId, "now");
    }

    // Overloaded addItem 1: with quantity
    public boolean addItem(MenuItem item, int quantity) {
        if (item == null || !item.isAvailable() || quantity < 1 || quantity > 20) {
            return false;
        }
        for (int i = 0; i < itemCount; i++) {
            if (items[i].getMenuItem().getItemId().equals(item.getItemId())) {
                int newQty = items[i].getQuantity() + quantity;
                if (newQty > 20) {
                    return false;
                }
                items[i].changeQuantity(newQty);
                return true;
            }
        }
        if (itemCount >= items.length) {
            return false;
        }
        items[itemCount] = new CartItem(item, quantity);   // created inside the cart
        itemCount++;
        return true;
    }

    // Overloaded addItem 2: quantity defaults to 1
    public boolean addItem(MenuItem item) {
        return addItem(item, 1);
    }

    public boolean removeItem(String itemId) {
        for (int i = 0; i < itemCount; i++) {
            if (items[i].getMenuItem().getItemId().equalsIgnoreCase(itemId)) {
                for (int j = i; j < itemCount - 1; j++) {
                    items[j] = items[j + 1];
                }
                items[itemCount - 1] = null;
                itemCount--;
                return true;
            }
        }
        return false;
    }

    // Passes the array to a helper method
    public double calculateTotal() {
        return sumLineTotals(items, itemCount);
    }

    public static double sumLineTotals(CartItem[] list, int count) {
        double sum = 0;
        for (int i = 0; i < count; i++) {
            sum = sum + list[i].calculateLineTotal();
        }
        return sum;
    }

    public boolean validate() {
        if (itemCount == 0) {
            return false;
        }
        for (int i = 0; i < itemCount; i++) {
            if (!items[i].validateQuantity() || !items[i].getMenuItem().isAvailable()) {
                return false;
            }
        }
        return true;
    }

    public void clear() {
        for (int i = 0; i < itemCount; i++) {
            items[i] = null;
        }
        itemCount = 0;
    }

    public boolean isEmpty() {
        return itemCount == 0;
    }

    public void displayCart() {
        if (itemCount == 0) {
            System.out.println("   Your cart is empty.");
            return;
        }
        for (int i = 0; i < itemCount; i++) {
            CartItem c = items[i];
            System.out.println("   " + c.getMenuItem().getItemId() + " | " + c.getMenuItem().getName()
                    + " x" + c.getQuantity() + " @ Rs. " + c.getUnitPrice()
                    + " = Rs. " + c.getLineTotal());
        }
        System.out.println("   Subtotal: Rs. " + calculateTotal());
    }

    public CartItem getItem(int index) { return items[index]; }
    public int getItemCount() { return itemCount; }
    public String getCartId() { return cartId; }
    public String getCustomerId() { return customerId; }
    public String getCreatedAt() { return createdAt; }
}
