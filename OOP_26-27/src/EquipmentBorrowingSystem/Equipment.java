/**
 * Represents a single borrowable item.
 * Demonstrates ENCAPSULATION: status can only change through
 * controlled methods that enforce business rules.
 */
public class Equipment {

    private String equipmentId;
    private String name;
    private String category;
    private EquipmentStatus status;

    public Equipment(String equipmentId, String name, String category) {
        this.equipmentId = equipmentId;
        this.name = name;
        this.category = category;
        this.status = EquipmentStatus.AVAILABLE;
    }

    public String getEquipmentId() { return equipmentId; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public EquipmentStatus getStatus() { return status; }

    public boolean isAvailable() {
        return status == EquipmentStatus.AVAILABLE;
    }

    /** Business rule: only an AVAILABLE item can be reserved. */
    public void reserve() {
        if (status != EquipmentStatus.AVAILABLE) {
            throw new IllegalStateException(name + " is not available to reserve.");
        }
        status = EquipmentStatus.RESERVED;
    }

    public void markBorrowed() {
        status = EquipmentStatus.BORROWED;
    }

    public void markReturned(boolean damaged) {
        status = damaged ? EquipmentStatus.UNDER_REPAIR : EquipmentStatus.AVAILABLE;
    }

    public void retire() {
        status = EquipmentStatus.RETIRED;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s) - %s", equipmentId, name, category, status);
    }
}
