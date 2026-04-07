import java.util.*;

// Reservation Request
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

// Thread-safe Booking Queue
class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    // synchronized add
    public synchronized void addRequest(Reservation r) {
        queue.offer(r);
        System.out.println(Thread.currentThread().getName() +
                " added request for " + r.getGuestName());
    }

    // synchronized remove
    public synchronized Reservation getNextRequest() {
        return queue.poll();
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}

// Thread-safe Inventory
class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
    }

    public void addRoom(String type, int count) {
        inventory.put(type, count);
    }

    // Critical Section (synchronized)
    public synchronized boolean allocateRoom(String type) {
        int available = inventory.getOrDefault(type, 0);

        if (available <= 0) {
            return false;
        }

        // simulate delay (to expose race conditions if not synchronized)
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        inventory.put(type, available - 1);
        return true;
    }

    public synchronized int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }
}

// Booking Processor (Thread)
class BookingProcessor extends Thread {

    private BookingQueue queue;
    private InventoryService inventory;

    public BookingProcessor(String name, BookingQueue queue, InventoryService inventory) {
        super(name);
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        while (true) {

            Reservation r;

            // synchronized queue access
            synchronized (queue) {
                if (queue.isEmpty()) break;
                r = queue.getNextRequest();
            }

            if (r != null) {
                processBooking(r);
            }
        }
    }

    private void processBooking(Reservation r) {

        System.out.println(getName() + " processing " + r.getGuestName());

        boolean success = inventory.allocateRoom(r.getRoomType());

        if (success) {
            System.out.println("✅ " + getName() + ": Booking confirmed for "
                    + r.getGuestName() + " (" + r.getRoomType() + ")");
        } else {
            System.out.println("❌ " + getName() + ": Booking failed for "
                    + r.getGuestName() + " (No availability)");
        }
    }
}

// Main Class
public class main {

    public static void main(String[] args) {

        // Step 1: Shared Inventory
        InventoryService inventory = new InventoryService();
        inventory.addRoom("Single", 2); // limited rooms

        // Step 2: Shared Booking Queue
        BookingQueue queue = new BookingQueue();

        // Simulate multiple guest requests
        queue.addRequest(new Reservation("Alice", "Single"));
        queue.addRequest(new Reservation("Bob", "Single"));
        queue.addRequest(new Reservation("Charlie", "Single"));
        queue.addRequest(new Reservation("David", "Single"));

        // Step 3: Multiple Threads (Concurrent Guests)
        BookingProcessor t1 = new BookingProcessor("Thread-1", queue, inventory);
        BookingProcessor t2 = new BookingProcessor("Thread-2", queue, inventory);

        // Step 4: Start threads
        t1.start();
        t2.start();

        // Step 5: Wait for completion
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Step 6: Final Inventory
        System.out.println("\nFinal Availability (Single): " +
                inventory.getAvailability("Single"));
    }
}