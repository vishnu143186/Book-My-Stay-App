import java.io.*;
import java.util.*;

// Reservation (Serializable)
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public void display() {
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room: " + roomType);
    }
}

// Inventory (Serializable)
class InventoryService implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoom(String type, int count) {
        inventory.put(type, count);
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }

    public void display() {
        System.out.println("\nInventory State:");
        for (String type : inventory.keySet()) {
            System.out.println(type + ": " + inventory.get(type));
        }
    }
}

// System State (Wrapper for Persistence)
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Reservation> reservations;
    private InventoryService inventory;

    public SystemState(List<Reservation> reservations, InventoryService inventory) {
        this.reservations = reservations;
        this.inventory = inventory;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public InventoryService getInventory() {
        return inventory;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "system_state.dat";

    // Save state to file
    public static void save(SystemState state) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("\n✅ System state saved successfully.");

        } catch (IOException e) {
            System.out.println("❌ Error saving system state: " + e.getMessage());
        }
    }

    // Load state from file
    public static SystemState load() {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            SystemState state = (SystemState) ois.readObject();
            System.out.println("\n✅ System state loaded successfully.");
            return state;

        } catch (FileNotFoundException e) {
            System.out.println("\n⚠ No previous state found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("\n❌ Error loading state. Starting with clean system.");
        }

        return null;
    }
}

// Main Class
public class main {

    public static void main(String[] args) {

        // Step 1: Try loading previous state
        SystemState state = PersistenceService.load();

        List<Reservation> reservations;
        InventoryService inventory;

        if (state != null) {
            // Restore from file
            reservations = state.getReservations();
            inventory = state.getInventory();

            System.out.println("\n--- Restored Reservations ---");
            for (Reservation r : reservations) {
                r.display();
            }

            inventory.display();

        } else {
            // Fresh start
            reservations = new ArrayList<>();
            inventory = new InventoryService();

            inventory.addRoom("Single", 2);
            inventory.addRoom("Suite", 1);

            reservations.add(new Reservation("RES101", "Alice", "Single"));
            reservations.add(new Reservation("RES102", "Bob", "Suite"));

            System.out.println("\n--- New System Initialized ---");
        }

        // Step 2: Display current state
        System.out.println("\n--- Current Reservations ---");
        for (Reservation r : reservations) {
            r.display();
        }

        inventory.display();

        // Step 3: Save state before shutdown
        SystemState newState = new SystemState(reservations, inventory);
        PersistenceService.save(newState);
    }
}