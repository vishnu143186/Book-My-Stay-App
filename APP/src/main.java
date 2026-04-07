import java.util.*;

// Reservation: Represents booking intent
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

    public void display() {
        System.out.println("Guest: " + guestName + " | Requested Room: " + roomType);
    }
}

// Booking Request Queue (FIFO)
class BookingRequestQueue {
    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    // Add request (enqueue)
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Request added for " + reservation.getGuestName());
    }

    // View all requests (without removing)
    public void viewAllRequests() {
        if (queue.isEmpty()) {
            System.out.println("\nNo booking requests in queue.");
            return;
        }

        System.out.println("\nBooking Requests in Queue (FIFO Order):");
        for (Reservation r : queue) {
            r.display();
        }
    }

    // Peek next request (without removal)
    public Reservation peekNextRequest() {
        return queue.peek();
    }

    // Get queue size
    public int getQueueSize() {
        return queue.size();
    }
}

// Main Class
public class main {

    public static void main(String[] args) {

        // Step 1: Initialize Booking Queue
        BookingRequestQueue requestQueue = new BookingRequestQueue();

        // Step 2: Simulate Guest Booking Requests
        Reservation r1 = new Reservation("Alice", "Single");
        Reservation r2 = new Reservation("Bob", "Suite");
        Reservation r3 = new Reservation("Charlie", "Double");

        // Step 3: Add requests (FIFO order maintained)
        requestQueue.addRequest(r1);
        requestQueue.addRequest(r2);
        requestQueue.addRequest(r3);

        // Step 4: View all queued requests
        requestQueue.viewAllRequests();

        // Step 5: Peek next request (without removing)
        System.out.println("\nNext Request to Process:");
        Reservation next = requestQueue.peekNextRequest();
        if (next != null) {
            next.display();
        }

        // Step 6: Show queue size
        System.out.println("\nTotal Requests in Queue: " + requestQueue.getQueueSize());
    }
}