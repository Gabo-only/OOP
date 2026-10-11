/**
 * Faculty is-a User. Second concrete subclass — pairs with Student
 * to demonstrate POLYMORPHISM via two different overrides.
 */
public class Faculty extends User {

    private String department;

    public Faculty(String userId, String name, String contactInfo, String department) {
        super(userId, name, contactInfo);
        this.department = department;
    }

    public String getDepartment() { return department; }

    @Override
    public int getMaxBorrowDays() {
        return 14; // faculty get a longer borrowing window
    }

    @Override
    public String getRoleDescription() {
        return "Faculty - " + department;
    }
}
