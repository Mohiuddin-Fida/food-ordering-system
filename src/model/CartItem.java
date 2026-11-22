package model;

public class CartItem {
    private int quantity;
    private double unitPrice;
    private String specialInstructions;
    private double lineTotal;
    private MenuItem menuItem;      // association: the item that was selected

    // Constructor 1
    public CartItem(MenuItem menuItem, int quantity, String specialInstructions) {
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.unitPrice = menuItem.getPrice();
        this.specialInstructions = specialInstructions;
        calculateLineTotal();
    }

    // Constructor 2 (overloading)
    public CartItem(MenuItem menuItem, int quantity) {
        this(menuItem, quantity, "");
    }

    public void changeQuantity(int newQuantity) {
        this.quantity = newQuantity;
        calculateLineTotal();
    }

    public double calculateLineTotal() {
        lineTotal = unitPrice * quantity;
        return lineTotal;
    }

    // Relational + logical operators
    public boolean validateQuantity() {
        return quantity >= 1 && quantity <= 20;
    }

    public int getQuantity() { return quantity; }
    public double getUnitPrice() { return unitPrice; }
    public String getSpecialInstructions() { return specialInstructions; }
    public void setSpecialInstructions(String specialInstructions) { this.specialInstructions = specialInstructions; }
    public double getLineTotal() { return lineTotal; }
    public MenuItem getMenuItem() { return menuItem; }
}
