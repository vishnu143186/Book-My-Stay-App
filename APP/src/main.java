import java.util.*;

// Reservation (Confirmed Booking)
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;
    private boolean isCancelled;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.isCancelled = false;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void cancel() {
        isCancelled = true;
    }

    public void display() {
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room Type: " + roomType +
                " | Room ID: " + roomId +
                " | Status: " + (isCancelled ? "Cancelled" : "Active"));
    }
}

// Inventory Service
class InventoryService {
    private Map<String, Integer> inventory;

    public InventoryService() {
        inventory = new HashMap<>();
    }

    public void addRoom(String type, int count) {
        inventory.put(type, count);
    }

    public void incrementRoom(String type) {
        inventory.put(type, inventory.getOrDefault(type, 0) + 1);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }
}

// Booking History
class BookingHistory {
    private Map<String, Reservation> reservations;

    public BookingHistory() {
        reservations = new HashMap<>();
    }

    public void addReservation(Reservation r) {
        reservations.put(r.getReservationId(), r);
    }

    public Reservation getReservation(String id) {
        return reservations.get(id);
    }
}

// Cancellation Service
class CancellationService {

    private InventoryService inventory;
    private BookingHistory history;

    // Stack for rollback (LIFO)
    private Stack<String> rollbackStack;

    public CancellationService(InventoryService inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
        this.rollbackStack = new Stack<>();
    }

    public void cancelBooking(String reservationId) {

        System.out.println("\nProcessing cancellation for: " + reservationId);

        // Step 1: Validate existence
        Reservation r = history.getReservation(reservationId);

        if (r == null) {
            System.out.println("Cancellation Failed: Reservation does not exist.");
            return;
        }

        // Step 2: Prevent duplicate cancellation
        if (r.isCancelled()) {
            System.out.println("Cancellation Failed: Reservation already cancelled.");
            return;
        }

        // Step 3: Record room ID in rollback stack
        rollbackStack.push(r.getRoomId());

        // Step 4: Restore inventory
        inventory.incrementRoom(r.getRoomType());

        // Step 5: Update reservation state
        r.cancel();

        // Step 6: Confirmation
        System.out.println("Cancellation Successful!");
        r.display();
        System.out.println("Updated Availability (" + r.getRoomType() + "): " +
                inventory.getAvailability(r.getRoomType()));
    }

    // View rollback history
    public void viewRollbackStack() {
        System.out.println("\nRollback Stack (Recent Releases First):");
        for (int i = rollbackStack.size() - 1; i >= 0; i--) {
            System.out.println(rollbackStack.get(i));
        }
    }
}

// Main Class
public class main {

    public static void main(String[] args) {

        // Step 1: Setup Inventory
        InventoryService inventory = new InventoryService();
        inventory.addRoom("Single", 0); // already booked

        // Step 2: Booking History (Simulating confirmed bookings)
        BookingHistory history = new BookingHistory();

        Reservation r1 = new Reservation("RES101", "Alice", "Single", "SI_001");
        history.addReservation(r1);

        // Step 3: Cancellation Service
        CancellationService service = new CancellationService(inventory, history);

        // Step 4: Perform Cancellation
        service.cancelBooking("RES101");

        // Step 5: Attempt invalid cancellations
        service.cancelBooking("RES999"); // non-existent
        service.cancelBooking("RES101"); // already cancelled

        // Step 6: View rollback stack
        service.viewRollbackStack();
    }
}