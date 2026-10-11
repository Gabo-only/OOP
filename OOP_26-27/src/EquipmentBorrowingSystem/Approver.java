/**
 * Interface implemented by any user type authorized to approve requests.
 * Demonstrates ABSTRACTION through an interface contract.
 */
public interface Approver {
    void approve(BorrowRequest request);
    void deny(BorrowRequest request, String reason);
}
