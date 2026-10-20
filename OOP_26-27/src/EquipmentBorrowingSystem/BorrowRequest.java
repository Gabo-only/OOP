import java.time.LocalDate;

/**
    represents one borrowing transaction between a User and an Equipment item
    demonstrates ASSOCIATION (holds references to User and Equipment),
    state managing (RequestStatus lifecycle), and business rule enforcement
 */
public class BorrowRequest {

    private String requestId;
    private User borrower;
    private Equipment equipment;
    private LocalDate dateRequested;
    private LocalDate dueDate;
    private LocalDate dateReturned;
    private RequestStatus status;
    private String denyReason;

    public BorrowRequest(String requestId, User borrower, Equipment equipment) {
        this.requestId = requestId;
        this.borrower = borrower;
        this.equipment = equipment;
        this.dateRequested = LocalDate.now();
        this.status = RequestStatus.PENDING;
    }

    public String getRequestId() { return requestId; }
    public User getBorrower() { return borrower; }
    public Equipment getEquipment() { return equipment; }
    public RequestStatus getStatus() { return status; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getDateReturned() { return dateReturned; }
    public String getDenyReason() { return denyReason; }

    /** Business rule: cannot approve a request that isn't PENDING. */
    public void approve() {
        if (status != RequestStatus.PENDING) {
            throw new IllegalStateException("Only a PENDING request can be approved.");
        }
        equipment.reserve();
        // Polymorphic call: the number of days depends on the concrete User subclass.
        this.dueDate = dateRequested.plusDays(borrower.getMaxBorrowDays());
        status = RequestStatus.APPROVED;
    }

    public void deny(String reason) {
        if (status != RequestStatus.PENDING) {
            throw new IllegalStateException("Only a PENDING request can be denied.");
        }
        this.denyReason = reason;
        status = RequestStatus.DENIED;
    }

    public void release() {
        if (status != RequestStatus.APPROVED) {
            throw new IllegalStateException("Only an APPROVED request can be released.");
        }
        equipment.markBorrowed();
        status = RequestStatus.RELEASED;
    }

    /** Business rule: a request already CLOSED cannot be returned again. */
    public void returnItem(boolean damaged) {
        if (status != RequestStatus.RELEASED && status != RequestStatus.OVERDUE) {
            throw new IllegalStateException("Only a RELEASED or OVERDUE request can be returned.");
        }
        this.dateReturned = LocalDate.now();
        equipment.markReturned(damaged);
        status = RequestStatus.RETURNED;
    }

    public void close() {
        status = RequestStatus.CLOSED;
    }

    public void checkOverdue() {
        if (status == RequestStatus.RELEASED && dueDate != null && LocalDate.now().isAfter(dueDate)) {
            status = RequestStatus.OVERDUE;
        }
    }

    @Override
    public String toString() {
        String details = "";
        if (denyReason != null) {
            details += " | denied: " + denyReason;
        }
        if (dateReturned != null) {
            details += " | returned: " + dateReturned;
        }
        return String.format("Request[%s] %s -> %s | %s | due:%s%s",
                requestId, borrower.getName(), equipment.getName(), status, dueDate, details);
    }
}
