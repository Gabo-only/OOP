import java.util.ArrayList;
import java.util.List;

/**
    this part holds all the memory, and data that the project needed
 */
public class InventorySystem {

    private List<User> users = new ArrayList<>();
    private List<Equipment> equipmentList = new ArrayList<>();
    private List<BorrowRequest> requests = new ArrayList<>();
    private List<DamageReport> damageReports = new ArrayList<>();
    private int requestCounter = 1;
    private int reportCounter = 1;

    // ---------- Object creation ----------
    public void addUser(User user) { users.add(user); }
    public void addEquipment(Equipment eq) { equipmentList.add(eq); }

    // ---------- Business rule: submit a request ----------
    public BorrowRequest submitRequest(User borrower, Equipment equipment) {
        if (!equipment.isAvailable()) {
            throw new IllegalStateException(equipment.getName() + " is not available.");
        }
        boolean alreadyActive = requests.stream().anyMatch(r ->
                r.getBorrower().equals(borrower) &&
                r.getEquipment().equals(equipment) &&
                (r.getStatus() == RequestStatus.PENDING ||
                 r.getStatus() == RequestStatus.APPROVED ||
                 r.getStatus() == RequestStatus.RELEASED));
        if (alreadyActive) {
            throw new IllegalStateException(borrower.getName() + " already has an active request for this item.");
        }
        BorrowRequest request = new BorrowRequest("REQ" + requestCounter++, borrower, equipment);
        requests.add(request);
        return request;
    }

    public void approveRequest(Custodian custodian, BorrowRequest request) {
        custodian.approve(request);
    }

    public void releaseRequest(BorrowRequest request) {
        request.release();
    }

    public void returnEquipment(BorrowRequest request, boolean damaged, String damageDescription) {
        request.returnItem(damaged);
        if (damaged) {
            DamageReport report = new DamageReport("DMG" + reportCounter++, request,
                    damageDescription, "UNSPECIFIED");
            damageReports.add(report);
        }
        request.close();
    }

    // ---------- Searching / filtering ----------
    public List<Equipment> findByCategory(String category) {
        List<Equipment> result = new ArrayList<>();
        for (Equipment e : equipmentList) {
            if (e.getCategory().equalsIgnoreCase(category)) result.add(e);
        }
        return result;
    }

    public List<BorrowRequest> findByStatus(RequestStatus status) {
        List<BorrowRequest> result = new ArrayList<>();
        for (BorrowRequest r : requests) {
            if (r.getStatus() == status) result.add(r);
        }
        return result;
    }

    public List<BorrowRequest> findByBorrower(String userId) {
        List<BorrowRequest> result = new ArrayList<>();
        for (BorrowRequest r : requests) {
            if (r.getBorrower().getUserId().equals(userId)) result.add(r);
        }
        return result;
    }

    // ---------- Reporting ----------
    public void printSummaryReport() {
        System.out.println("\n=== SUMMARY REPORT ===");
        for (RequestStatus status : RequestStatus.values()) {
            long count = requests.stream().filter(r -> r.getStatus() == status).count();
            System.out.println(status + ": " + count);
        }
        long availableCount = equipmentList.stream().filter(Equipment::isAvailable).count();
        System.out.println("Available equipment: " + availableCount + " / " + equipmentList.size());
        System.out.println("Damage reports filed: " + damageReports.size());
    }

    // ---------- Getters for main() to display data ----------
    public List<User> getUsers() { return users; }
    public List<Equipment> getEquipmentList() { return equipmentList; }
    public List<BorrowRequest> getRequests() { return requests; }
    public List<DamageReport> getDamageReports() { return damageReports; }
}
