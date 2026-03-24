import java.util.*;

// Reservation class
class Reservation {
    private static int counter = 1;
    private int reservationId;
    private String customerName;
    private String roomType;
    private int nights;
    private double pricePerNight;

    public Reservation(String customerName, String roomType, int nights, double pricePerNight) {
        this.reservationId = counter++;
        this.customerName = customerName;
        this.roomType = roomType;
        this.nights = nights;
        this.pricePerNight = pricePerNight;
    }

    public double getTotalCost() {
        return nights * pricePerNight;
    }

    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Customer: " + customerName +
                ", Room: " + roomType +
                ", Nights: " + nights +
                ", Total Cost: ₹" + getTotalCost();
    }
}

// Booking History (Storage)
class BookingHistory {
    private List<Reservation> reservations = new ArrayList<>();

    // Add confirmed booking
    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    // Retrieve all bookings (read-only copy)
    public List<Reservation> getAllReservations() {
        return new ArrayList<>(reservations);
    }
}

// Reporting Service
class BookingReportService {

    public void generateSummary(List<Reservation> reservations) {
        System.out.println("\n--- Booking Summary Report ---");

        int totalBookings = reservations.size();
        double totalRevenue = 0;

        for (Reservation r : reservations) {
            totalRevenue += r.getTotalCost();
        }

        System.out.println("Total Bookings: " + totalBookings);
        System.out.println("Total Revenue: ₹" + totalRevenue);
    }

    public void showAllBookings(List<Reservation> reservations) {
        System.out.println("\n--- Booking History ---");

        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }
}

// Main Class
public class Book_My_Stay_App {
    public static void main(String[] args) {

        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        // Simulating confirmed bookings
        Reservation r1 = new Reservation("Arun", "Deluxe", 2, 2500);
        Reservation r2 = new Reservation("Meena", "Suite", 3, 4000);
        Reservation r3 = new Reservation("Raj", "Standard", 1, 1500);

        // Add to booking history
        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        // Admin retrieves data
        List<Reservation> storedReservations = history.getAllReservations();

        // Display history
        reportService.showAllBookings(storedReservations);

        // Generate report
        reportService.generateSummary(storedReservations);
    }
}