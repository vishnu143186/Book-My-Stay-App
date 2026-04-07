import java.util.*;

// Custom Exception for Invalid Booking
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation Input
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
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

    public boolean isValidRoomType(String type) {
        return inventory.containsKey(type);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void decrementRoom(String type) throws InvalidBookingException {
        int available = getAvailability(type);

        if (available <= 0) {
            throw new InvalidBookingException("No available rooms for type: " + type);
        }

        inventory.put(type, available - 1);
    }
}

// Validator (Fail-Fast)
class BookingValidator {

    public static void validate(Reservation reservation, InventoryService inventory)
            throws InvalidBookingException {

        // Validate guest name
        if (reservation.getGuestName() == null || reservation.getGuestName().trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        // Validate room type
        if (reservation.getRoomType() == null || reservation.getRoomType().trim().isEmpty()) {
            throw new InvalidBookingException("Room type cannot be empty.");
        }

        // Check valid room type
        if (!inventory.isValidRoomType(reservation.getRoomType())) {
            throw new InvalidBookingException("Invalid room type: " + reservation.getRoomType());
        }

        // Check availability
        if (inventory.getAvailability(reservation.getRoomType()) <= 0) {
            throw new InvalidBookingException("Room not available for type: " + reservation.getRoomType());
        }
    }
}

// Booking Service
class BookingService {

    private InventoryService inventory;

    public BookingService(InventoryService inventory) {
        this.inventory = inventory;
    }

    public void processBooking(Reservation reservation) {
        try {
            // Step 1: Validate input (Fail-Fast)
            BookingValidator.validate(reservation, inventory);

            // Step 2: Allocate room (state change only after validation)
            inventory.decrementRoom(reservation.getRoomType());

            // Step 3: Confirm booking
            System.out.println("Booking Successful!");
            System.out.println("Guest: " + reservation.getGuestName());
            System.out.println("Room Type: " + reservation.getRoomType());

        } catch (InvalidBookingException e) {
            // Graceful error handling
            System.out.println("Booking Failed: " + e.getMessage());
        }
    }
}

// Main Class
public class main {

    public static void main(String[] args) {

        // Step 1: Setup Inventory
        InventoryService inventory = new InventoryService();
        inventory.addRoom("Single", 1);
        inventory.addRoom("Double", 0);

        // Step 2: Booking Service
        BookingService bookingService = new BookingService(inventory);

        // Step 3: Test Cases (Valid + Invalid)

        // Valid booking
        Reservation r1 = new Reservation("Alice", "Single");

        // Invalid room type
        Reservation r2 = new Reservation("Bob", "Suite");

        // No availability
        Reservation r3 = new Reservation("Charlie", "Double");

        // Empty guest name
        Reservation r4 = new Reservation("", "Single");

        // Process bookings
        bookingService.processBooking(r1);
        bookingService.processBooking(r2);
        bookingService.processBooking(r3);
        bookingService.processBooking(r4);
    }
}