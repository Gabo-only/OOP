/**
 * Abstract superclass for anyone who can use the system.
 * Demonstrates: ABSTRACTION (cannot be instantiated directly)
 * and is the base of the INHERITANCE hierarchy (Student, Faculty).
 */
public abstract class User {

    private String userId;
    private String name;
    private String contactInfo;

    public User(String userId, String name, String contactInfo) {
        this.userId = userId;
        this.name = name;
        this.contactInfo = contactInfo;
    }

    // Encapsulated access
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getContactInfo() { return contactInfo; }

    public void setContactInfo(String contactInfo) {
        if (contactInfo == null || contactInfo.isBlank()) {
            throw new IllegalArgumentException("Contact info cannot be empty.");
        }
        this.contactInfo = contactInfo;
    }

    /**
     * Abstract method — every subclass must define its own borrowing rules.
     * This is where POLYMORPHISM shows up: calling getMaxBorrowDays() on a
     * User reference will run the correct subclass version at runtime.
     */
    public abstract int getMaxBorrowDays();

    /**
     * Abstract method — each user type describes itself differently.
     */
    public abstract String getRoleDescription();

    @Override
    public String toString() {
        return String.format("[%s] %s (%s) - %s", userId, name, getRoleDescription(), contactInfo);
    }
}
