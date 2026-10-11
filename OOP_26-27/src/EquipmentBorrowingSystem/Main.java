import java.util.List;
import java.util.Scanner;

/**
 * Console entry point. Per project guidelines, main() only:
 * displays the menu, reads input, calls InventorySystem methods,
 * and prints results. All real logic lives in the other classes.
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final InventorySystem system = new InventorySystem();
    private static Custodian custodian; // sample authorized approver

    public static void main(String[] args) {
        seedSampleData();
        boolean running = true;

        while (running) {
            printMenu();
            int choice = readInt("Enter choice: ");

            try {
                switch (choice) {
                    case 1 -> registerUser();
                    case 2 -> addEquipment();
                    case 3 -> submitRequest();
                    case 4 -> approveRequest();
                    case 5 -> releaseRequest();
                    case 6 -> returnEquipment();
                    case 7 -> searchFilter();
                    case 8 -> system.printSummaryReport();
                    case 0 -> running = false;
                    default -> System.out.println("Invalid choice.");
                }
            } catch (IllegalStateException | IllegalArgumentException e) {
                // Required: handle invalid input / business rule violations gracefully
                System.out.println("Error: " + e.getMessage());
            }
        }
        System.out.println("Goodbye!");
    }

    private static void printMenu() {
        System.out.println("\n====================================");
        System.out.println("EQUIPMENT BORROWING & ACCOUNTABILITY SYSTEM");
        System.out.println("====================================");
        System.out.println("[1] Register User");
        System.out.println("[2] Add Equipment");
        System.out.println("[3] Submit Borrow Request");
        System.out.println("[4] Approve Request");
        System.out.println("[5] Release Equipment (hand over)");
        System.out.println("[6] Return Equipment");
        System.out.println("[7] Search / Filter");
        System.out.println("[8] Generate Summary Report");
        System.out.println("[0] Exit");
    }

    private static void registerUser() {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Contact: ");
        String contact = scanner.nextLine();
        System.out.print("Type (1-Student, 2-Faculty): ");
        int type = Integer.parseInt(scanner.nextLine());

        User user;
        if (type == 1) {
            System.out.print("Program: ");
            user = new Student("U" + (system.getUsers().size() + 1), name, contact, scanner.nextLine());
        } else {
            System.out.print("Department: ");
            user = new Faculty("U" + (system.getUsers().size() + 1), name, contact, scanner.nextLine());
        }
        system.addUser(user);
        System.out.println("Registered: " + user);
    }

    private static void addEquipment() {
        System.out.print("Equipment Name: ");
        String name = scanner.nextLine();
        System.out.print("Category: ");
        String category = scanner.nextLine();
        Equipment eq = new Equipment("EQ" + (system.getEquipmentList().size() + 1), name, category);
        system.addEquipment(eq);
        System.out.println("Added: " + eq);
    }

    private static void submitRequest() {
        listUsers();
        User borrower = pickUser();
        listEquipment();
        Equipment equipment = pickEquipment();
        BorrowRequest request = system.submitRequest(borrower, equipment);
        System.out.println("Submitted: " + request);
    }

    private static void approveRequest() {
        if (custodian == null) custodian = new Custodian("C1", "Default Custodian");
        listRequests();
        BorrowRequest request = pickRequest();
        system.approveRequest(custodian, request);
        System.out.println("Approved: " + request);
    }

    private static void releaseRequest() {
        listRequests();
        BorrowRequest request = pickRequest();
        system.releaseRequest(request);
        System.out.println("Released: " + request);
    }

    private static void returnEquipment() {
        listRequests();
        BorrowRequest request = pickRequest();
        System.out.print("Was it damaged? (y/n): ");
        boolean damaged = scanner.nextLine().equalsIgnoreCase("y");
        String description = "";
        if (damaged) {
            System.out.print("Describe damage: ");
            description = scanner.nextLine();
        }
        system.returnEquipment(request, damaged, description);
        System.out.println("Return processed.");
    }

    private static void searchFilter() {
        System.out.print("Filter by (1-Category, 2-Request Status): ");
        int type = readInt("");
        if (type == 1) {
            System.out.print("Category: ");
            List<Equipment> results = system.findByCategory(scanner.nextLine());
            results.forEach(System.out::println);
        } else {
            System.out.print("Status (PENDING/APPROVED/RELEASED/RETURNED/OVERDUE/DENIED/CLOSED): ");
            RequestStatus status = RequestStatus.valueOf(scanner.nextLine().toUpperCase());
            system.findByStatus(status).forEach(System.out::println);
        }
    }

    // ---------- Helper display/selection methods ----------
    private static void listUsers() { system.getUsers().forEach(System.out::println); }
    private static void listEquipment() { system.getEquipmentList().forEach(System.out::println); }
    private static void listRequests() { system.getRequests().forEach(System.out::println); }

    private static User pickUser() {
        System.out.print("Enter User ID: ");
        String id = scanner.nextLine();
        return system.getUsers().stream()
                .filter(u -> u.getUserId().equals(id)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
    }

    private static Equipment pickEquipment() {
        System.out.print("Enter Equipment ID: ");
        String id = scanner.nextLine();
        return system.getEquipmentList().stream()
                .filter(e -> e.getEquipmentId().equals(id)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Equipment not found."));
    }

    private static BorrowRequest pickRequest() {
        System.out.print("Enter Request ID: ");
        String id = scanner.nextLine();
        return system.getRequests().stream()
                .filter(r -> r.getRequestId().equals(id)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Request not found."));
    }

    private static int readInt(String prompt) {
        if (!prompt.isEmpty()) System.out.print(prompt);
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1; // triggers "Invalid choice" in the menu loop
        }
    }

    private static void seedSampleData() {
        system.addUser(new Student("U1", "Juan Dela Cruz", "juan@school.edu", "BSIT"));
        system.addUser(new Faculty("U2", "Prof. Santos", "santos@school.edu", "CCS"));
        system.addEquipment(new Equipment("EQ1", "Digital Multimeter", "Lab Equipment"));
        system.addEquipment(new Equipment("EQ2", "Projector", "AV Equipment"));
        custodian = new Custodian("C1", "Default Custodian");
    }
}
