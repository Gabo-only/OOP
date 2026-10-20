/**
    personnel authorized to approve or deny borrow requests
    implements Approver — demonstrates ABSTRACTION
    and is a second source of POLYMORPHISM if other approver types are added later.
 */
public class Custodian implements Approver {

    private String custodianId;
    private String name;

    public Custodian(String custodianId, String name) {
        this.custodianId = custodianId;
        this.name = name;
    }

    public String getCustodianId() { return custodianId; }
    public String getName() { return name; }

    @Override
    public void approve(BorrowRequest request) {
        request.approve();
    }

    @Override
    public void deny(BorrowRequest request, String reason) {
        request.deny(reason);
    }
}
