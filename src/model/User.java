package model;

/**
 * Abstract parent of every role in the system (Customer, RestaurantStaff,
 * DeliveryRider, Administrator).
 */
public abstract class User {
    private String userId;
    private String name;
    private String email;
    private String passwordHash;
    private String role;
    private boolean active;

    public User(String userId, String name, String email, String password, String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.passwordHash = hashPassword(password);
        this.role = role;
        this.active = true;
    }

    // Passwords are never stored as plain text (NFR-02).
    private String hashPassword(String raw) {
        return "H" + raw.hashCode();
    }

    // Concrete operation: every role logs in the same way.
    public boolean authenticate(String password) {
        return active && passwordHash.equals(hashPassword(password));
    }

    public void updateProfile(String newName) {
        if (newName != null && newName.length() > 0) {
            this.name = newName;
        }
    }

    public boolean changePassword(String oldPassword, String newPassword) {
        if (authenticate(oldPassword) && newPassword.length() >= 6) {
            this.passwordHash = hashPassword(newPassword);
            return true;
        }
        return false;
    }

    // Used by the administrator to reset a password (FR-14).
    public void resetPasswordTo(String newPassword) {
        this.passwordHash = hashPassword(newPassword);
    }

    // Abstract operation: the result differs for every role.
    public abstract String[] getPermissions();

    public String getSummary() {
        String state = "Active";
        if (!active) {
            state = "Deactivated";
        }
        return userId + " | " + name + " | " + role + " | " + email + " | " + state;
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
