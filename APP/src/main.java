import java.util.*;

// Reservation (Confirmed Booking)
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private double totalCost;

    public Reservation(String reservationId, String guestName, String roomType, double totalCost) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.totalCost = totalCost;
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

    public double getTotalCost() {
        return totalCost;
    }

    public void display() {
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room: " + roomType +
                " | Total Cost: ₹" + totalCost);
    }
}

// Booking History (State Holder)
class BookingHistory {

    // List maintains insertion order (chronological)
    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    // Add confirmed booking
    public void addReservation(Reservation reservation) {
        history.add(reservation);
        System.out.println("Reservation stored: " + reservation.getReservationId());
    }

    // Read-only access
    public List<Reservation> getAllReservations() {
        return Collections.unmodifiableList(history);
    }
}

// Reporting Service
class BookingReportService {

    private BookingHistory bookingHistory;

    public BookingReportService(BookingHistory bookingHistory) {
        this.bookingHistory = bookingHistory;
    }

    // Display all bookings
    public void displayAllBookings() {
        List<Reservation> reservations = bookingHistory.getAllReservations();

        if (reservations.isEmpty()) {
            System.out.println("\nNo booking history available.");
            return;
        }

        System.out.println("\n--- Booking History ---");
        for (Reservation r : reservations) {
            r.display();
        }
    }

    // Generate summary report
    public void generateSummaryReport() {
        List<Reservation> reservations = bookingHistory.getAllReservations();

        int totalBookings = reservations.size();
        double totalRevenue = 0;

        Map<String, Integer> roomTypeCount = new HashMap<>();

        for (Reservation r : reservations) {
            totalRevenue += r.getTotalCost();

            roomTypeCount.put(
                    r.getRoomType(),
                    roomTypeCount.getOrDefault(r.getRoomType(), 0) + 1
            );
        }

        System.out.println("\n--- Booking Summary Report ---");
        System.out.println("Total Bookings: " + totalBookings);
        System.out.println("Total Revenue: ₹" + totalRevenue);

        System.out.println("\nBookings by Room Type:");
        for (String type : roomTypeCount.keySet()) {
            System.out.println(type + ": " + roomTypeCount.get(type));
        }
    }
}

// Main Class
public class main {

    public static void main(String[] args) {

        // Step 1: Booking History
        BookingHistory history = new BookingHistory();

        // Step 2: Simulate Confirmed Reservations
        Reservation r1 = new Reservation("RES101", "Alice", "Single", 2000);
        Reservation r2 = new Reservation("RES102", "Bob", "Suite", 6000);
        Reservation r3 = new Reservation("RES103", "Charlie", "Single", 2200);

        // Step 3: Store in history (chronological order)
        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        // Step 4: Reporting Service
        BookingReportService reportService = new BookingReportService(history);

        // Step 5: Admin views history
        reportService.displayAllBookings();

        // Step 6: Generate summary report
        reportService.generateSummaryReport();
    }
}