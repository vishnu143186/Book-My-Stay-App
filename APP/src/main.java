import java.util.*;

// Reservation: Booking Request
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

// Inventory Service: Maintains room counts
class InventoryService {
    private Map<String, Integer> inventory;

    public InventoryService() {
        inventory = new HashMap<>();
    }

    public void addRoom(String type, int count) {
        inventory.put(type, count);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    // Decrement after allocation
    public void decrementRoom(String type) {
        inventory.put(type, inventory.get(type) - 1);
    }
}

// Booking Request Queue (FIFO)
class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll(); // dequeue
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// Booking Service: Handles allocation
class BookingService {

    private InventoryService inventoryService;

    // Map room type -> allocated room IDs
    private Map<String, Set<String>> allocatedRooms;

    // Global set to ensure uniqueness
    private Set<String> allAllocatedRoomIds;

    public BookingService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
        this.allocatedRooms = new HashMap<>();
        this.allAllocatedRoomIds = new HashSet<>();
    }

    // Generate unique room ID
    private String generateRoomId(String roomType) {
        String roomId;
        do {
            roomId = roomType.substring(0, 2).toUpperCase() + "_" + UUID.randomUUID().toString().substring(0, 5);
        } while (allAllocatedRoomIds.contains(roomId)); // ensure uniqueness

        return roomId;
    }

    // Process booking request
    public void processRequest(Reservation reservation) {

        String type = reservation.getRoomType();

        System.out.println("\nProcessing request for " + reservation.getGuestName() + " (" + type + ")");

        // Check availability
        if (inventoryService.getAvailability(type) <= 0) {
            System.out.println("Booking Failed: No rooms available for " + type);
            return;
        }

        // Generate unique room ID
        String roomId = generateRoomId(type);

        // Initialize set if not present
        allocatedRooms.putIfAbsent(type, new HashSet<>());

        // Add to both sets
        allocatedRooms.get(type).add(roomId);
        allAllocatedRoomIds.add(roomId);

        // Update inventory (atomic step)
        inventoryService.decrementRoom(type);

        // Confirm booking
        System.out.println("Booking Confirmed!");
        System.out.println("Guest: " + reservation.getGuestName());
        System.out.println("Room Type: " + type);
        System.out.println("Assigned Room ID: " + roomId);
        System.out.println("Remaining " + type + " Rooms: " + inventoryService.getAvailability(type));
    }

    // Process entire queue (FIFO)
    public void processAllRequests(BookingRequestQueue queue) {
        while (!queue.isEmpty()) {
            Reservation r = queue.getNextRequest();
            processRequest(r);
        }
    }
}

// Main Class
public class main {

    public static void main(String[] args) {

        // Step 1: Setup Inventory
        InventoryService inventory = new InventoryService();
        inventory.addRoom("Single", 2);
        inventory.addRoom("Double", 1);
        inventory.addRoom("Suite", 1);

        // Step 2: Setup Booking Queue (FIFO)
        BookingRequestQueue queue = new BookingRequestQueue();
        queue.addRequest(new Reservation("Alice", "Single"));
        queue.addRequest(new Reservation("Bob", "Suite"));
        queue.addRequest(new Reservation("Charlie", "Single"));
        queue.addRequest(new Reservation("David", "Single")); // should fail

        // Step 3: Booking Service
        BookingService bookingService = new BookingService(inventory);

        // Step 4: Process Requests
        bookingService.processAllRequests(queue);
    }
}