import java.util.*;

// Domain Model: Room
class Room {
    private String type;
    private double price;
    private List<String> amenities;

    public Room(String type, double price, List<String> amenities) {
        this.type = type;
        this.price = price;
        this.amenities = amenities;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void displayDetails() {
        System.out.println("Room Type: " + type);
        System.out.println("Price: ₹" + price);
        System.out.println("Amenities: " + String.join(", ", amenities));
        System.out.println("-----------------------------");
    }
}

// Inventory: State Holder (READ-ONLY for this use case)
class Inventory {
    private Map<String, Integer> roomAvailability;

    public Inventory() {
        roomAvailability = new HashMap<>();
    }

    public void addRoom(String type, int count) {
        roomAvailability.put(type, count);
    }

    // Read-only access
    public int getAvailability(String type) {
        return roomAvailability.getOrDefault(type, 0);
    }

    public Set<String> getAllRoomTypes() {
        return roomAvailability.keySet();
    }
}

// Search Service: Handles only READ operations
class SearchService {
    private Inventory inventory;
    private Map<String, Room> roomData;

    public SearchService(Inventory inventory, Map<String, Room> roomData) {
        this.inventory = inventory;
        this.roomData = roomData;
    }

    public void searchAvailableRooms() {
        System.out.println("\nAvailable Rooms:\n");

        boolean found = false;

        for (String type : inventory.getAllRoomTypes()) {

            int availableCount = inventory.getAvailability(type);

            // Validation: show only available rooms
            if (availableCount > 0) {

                Room room = roomData.get(type);

                // Defensive Programming: null check
                if (room != null) {
                    room.displayDetails();
                    System.out.println("Available Count: " + availableCount);
                    System.out.println("=============================");
                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println("No rooms available at the moment.");
        }
    }
}

// Main Class
public class main {

    public static void main(String[] args) {

        // Step 1: Setup Inventory (State Holder)
        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 3);
        inventory.addRoom("Double", 0);
        inventory.addRoom("Suite", 2);

        // Step 2: Setup Room Domain Data
        Map<String, Room> roomData = new HashMap<>();

        roomData.put("Single", new Room(
                "Single",
                1500,
                Arrays.asList("WiFi", "TV", "AC")
        ));

        roomData.put("Double", new Room(
                "Double",
                2500,
                Arrays.asList("WiFi", "TV", "AC", "Mini Bar")
        ));

        roomData.put("Suite", new Room(
                "Suite",
                5000,
                Arrays.asList("WiFi", "TV", "AC", "Mini Bar", "Jacuzzi")
        ));

        // Step 3: Search Service (Read-Only)
        SearchService searchService = new SearchService(inventory, roomData);

        // Step 4: Guest initiates search
        searchService.searchAvailableRooms();
    }
}