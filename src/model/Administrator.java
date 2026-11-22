package model;

/** Child of User (hierarchical inheritance). Manages user accounts (UC-12). */
public class Administrator extends User {
    private String adminId;
    private String accessLevel;
    private String lastLogin;

    public Administrator(String userId, String name, String email, String password,
                         String adminId, String accessLevel) {
        super(userId, name, email, password, "Admin");
        this.adminId = adminId;
        this.accessLevel = accessLevel;
        this.lastLogin = "never";
    }

    // The array of users is passed to the method; it stores the user in the first free slot.
    public boolean createUser(User newUser, User[] users) {
        for (int i = 0; i < users.length; i++) {
            if (users[i] == null) {
                users[i] = newUser;
                return true;
            }
        }
        return false;
    }

    public User findUser(String userId, User[] users) {
        for (int i = 0; i < users.length; i++) {
            if (users[i] != null && users[i].getUserId().equals(userId)) {
                return users[i];
            }
        }
        return null;
    }

    public boolean deactivateUser(String userId, User[] users) {
        User user = findUser(userId, users);
        if (user == null || user.getUserId().equals(getUserId())) {
            return false;   // cannot find the user, or admin tries to deactivate himself
        }
        user.setActive(false);
        return true;
    }

    public boolean resetPassword(String userId, User[] users) {
        User user = findUser(userId, users);
        if (user == null) {
            return false;
        }
        user.resetPasswordTo("reset" + userId);
        return true;
    }

    @Override
    public String[] getPermissions() {
        return new String[] { "Create user", "Deactivate user", "Reset password" };
    }

    @Override
    public String getSummary() {
        return super.getSummary() + " | Access: " + accessLevel;
    }

    public String getAdminId() { return adminId; }
    public String getAccessLevel() { return accessLevel; }
    public void setAccessLevel(String accessLevel) { this.accessLevel = accessLevel; }
    public String getLastLogin() { return lastLogin; }
    public void setLastLogin(String lastLogin) { this.lastLogin = lastLogin; }
}
