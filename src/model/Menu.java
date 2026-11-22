package model;

public class Menu {
    private String menuId;
    private String name;
    private String category;
    private String lastUpdated;
    // Array of MenuItem objects (aggregation: items can exist without the menu)
    private MenuItem[] items = new MenuItem[30];
    private int itemCount;

    // Constructor 1
    public Menu(String menuId, String name, String category) {
        this.menuId = menuId;
        this.name = name;
        this.category = category;
        this.lastUpdated = "initial";
        this.itemCount = 0;
    }

    // Constructor 2 (overloading)
    public Menu(String menuId, String name) {
        this(menuId, name, "All Day");
    }

    public boolean addItem(MenuItem item) {
        if (itemCount >= items.length || getItemById(item.getItemId()) != null) {
            return false;
        }
        items[itemCount] = item;
        itemCount++;
        lastUpdated = "updated";
        return true;
    }

    public boolean removeItem(String itemId) {
        for (int i = 0; i < itemCount; i++) {
            if (items[i].getItemId().equals(itemId)) {
                for (int j = i; j < itemCount - 1; j++) {
                    items[j] = items[j + 1];      // shift the remaining items left
                }
                items[itemCount - 1] = null;
                itemCount--;
                lastUpdated = "updated";
                return true;
            }
        }
        return false;
    }

    // Overloaded search 1: by name or category text
    public MenuItem[] searchItems(String query) {
        String q = query.toLowerCase();
        int matches = 0;
        for (int i = 0; i < itemCount; i++) {
            if (items[i].getName().toLowerCase().contains(q)
                    || items[i].getCategory().toLowerCase().contains(q)) {
                matches++;
            }
        }
        MenuItem[] result = new MenuItem[matches];
        int pos = 0;
        for (int i = 0; i < itemCount; i++) {
            if (items[i].getName().toLowerCase().contains(q)
                    || items[i].getCategory().toLowerCase().contains(q)) {
                result[pos] = items[i];
                pos++;
            }
        }
        return result;
    }

    // Overloaded search 2: by price range (same name, different parameters)
    public MenuItem[] searchItems(double minPrice, double maxPrice) {
        int matches = 0;
        for (int i = 0; i < itemCount; i++) {
            if (items[i].getPrice() >= minPrice && items[i].getPrice() <= maxPrice) {
                matches++;
            }
        }
        MenuItem[] result = new MenuItem[matches];
        int pos = 0;
        for (int i = 0; i < itemCount; i++) {
            if (items[i].getPrice() >= minPrice && items[i].getPrice() <= maxPrice) {
                result[pos] = items[i];
                pos++;
            }
        }
        return result;
    }

    public MenuItem getItemById(String itemId) {
        for (int i = 0; i < itemCount; i++) {
            if (items[i].getItemId().equalsIgnoreCase(itemId)) {
                return items[i];
            }
        }
        return null;
    }

    // An array is passed to this method; it counts the available items in it.
    public static int countAvailable(MenuItem[] list) {
        int count = 0;
        for (int i = 0; i < list.length; i++) {
            if (list[i] != null && list[i].isAvailable()) {
                count++;
            }
        }
        return count;
    }

    // Prints any array of menu items (used for the full menu and for search results)
    public static void printItems(MenuItem[] list) {
        if (list.length == 0) {
            System.out.println("   (no items found)");
        }
        for (int i = 0; i < list.length; i++) {
            if (list[i] != null) {
                System.out.println("   " + list[i].getSummary());
            }
        }
    }

    public void displayMenu() {
        System.out.println("--- " + name + " (" + category + ") - "
                + countAvailable(items) + " of " + itemCount + " items available ---");
        for (int i = 0; i < itemCount; i++) {
            System.out.println("   " + items[i].getSummary());
        }
    }

    // Returns an array that holds exactly the stored items
    public MenuItem[] getItems() {
        MenuItem[] copy = new MenuItem[itemCount];
        for (int i = 0; i < itemCount; i++) {
            copy[i] = items[i];
        }
        return copy;
    }

    public String getMenuId() { return menuId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public String getLastUpdated() { return lastUpdated; }
    public int getItemCount() { return itemCount; }
}
