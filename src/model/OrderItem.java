package model;

public class OrderItem {
    private String itemName;
    private int quantity;
    private double unitPrice;
    private String specialInstructions;
    private double lineTotal;

    // Constructor 1
    public OrderItem(String itemName, int quantity, double unitPrice, String specialInstructions) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;     // price snapshot at the time of ordering
        this.specialInstructions = specialInstructions;
        calculateLineTotal();
    }

    // Constructor 2 (overloading)
    public OrderItem(String itemName, int quantity, double unitPrice) {
        this(itemName, quantity, unitPrice, "");
    }

    public double calculateLineTotal() {
        lineTotal = unitPrice * quantity;
        return lineTotal;
    }

    public void changeQuantity(int newQuantity) {
        if (newQuantity > 0) {
            this.quantity = newQuantity;
            calculateLineTotal();
        }
    }

    public double getSubtotal() {
        return lineTotal;
    }

    public String getItemName() { return itemName; }
    public int getQuantity() { return quantity; }
    public double getUnitPrice() { return unitPrice; }
    public String getSpecialInstructions() { return specialInstructions; }
    public double getLineTotal() { return lineTotal; }
}
