import java.util.HashMap;
import java.util.Map;

/**
 * Hotel Booking Management System - Use Case 3
 * Demonstrates centralized inventory using HashMap
 *
 * @author YourName
 * @version 3.1
 */

// Inventory Class
class RoomInventory {

    private Map<String, Integer> inventory;

    // Constructor - initialize inventory
    public RoomInventory() {
        inventory = new HashMap<>();

        // Initialize room availability
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    // Get availability
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Update availability
    public void updateAvailability(String roomType, int newCount) {
        inventory.put(roomType, newCount);
    }

    // Display full inventory
    public void displayInventory() {
        System.out.println("Current Room Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " -> Available: " + entry.getValue());
        }
    }
}

// Main class
public class main {

    public static void main(String[] args) {

        System.out.println("===========================================");
        System.out.println("Hotel Booking Management System");
        System.out.println("Version: 3.1");
        System.out.println("===========================================\n");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Display initial inventory
        inventory.displayInventory();

        // Example: Update availability
        System.out.println("\nUpdating Single Room availability to 4...\n");
        inventory.updateAvailability("Single Room", 4);

        // Display updated inventory
        inventory.displayInventory();

        System.out.println("\nApplication finished.");
    }
}