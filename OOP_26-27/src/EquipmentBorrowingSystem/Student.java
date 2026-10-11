/**
 * Student is-a User. Demonstrates INHERITANCE and overrides
 * (POLYMORPHISM) with a shorter borrowing window.
 */
public class Student extends User {

    private String program;

    public Student(String userId, String name, String contactInfo, String program) {
        super(userId, name, contactInfo);
        this.program = program;
    }

    public String getProgram() { return program; }

    @Override
    public int getMaxBorrowDays() {
        return 3; // students get a shorter borrowing window
    }

    @Override
    public String getRoleDescription() {
        return "Student - " + program;
    }
}
