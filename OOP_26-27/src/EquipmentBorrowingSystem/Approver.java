/**
    thi interface is implemented by any user type authorized to approve requests like custodian
    Demonstrates ABSTRACTION through an interface
 */
public interface Approver {
    void approve(BorrowRequest request);
    void deny(BorrowRequest request, String reason);
}
