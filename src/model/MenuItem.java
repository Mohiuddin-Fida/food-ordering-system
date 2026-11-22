package model;

public class MenuItem {
    private String itemId;
    private String name;
    private String description;
    private double price;
    private String category;
    private boolean available;

    // Constructor 1
    public MenuItem(String itemId, String name, String description, double price, String category) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.available = true;
    }

    // Constructor 2 (overloading): short form
    public MenuItem(String itemId, String name, double price) {
        this(itemId, name, "", price, "General");
    }

    public void updatePrice(double newPrice) {
        if (newPrice > 0) {
            this.price = newPrice;
        }
    }

    public void setAvailability(boolean available) {
        this.available = available;
    }

    public boolean isAvailable() {
        return available;
    }

    public String getSummary() {
        String state = "Available";
        if (!available) {
            state = "Unavailable";
        }
        return itemId + " | " + name + " | " + category + " | Rs. " + price + " | " + state;
    }

    public String getItemId() { return itemId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
