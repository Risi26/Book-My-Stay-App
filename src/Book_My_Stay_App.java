import java.util.*;

// Custom Exception
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation Class
class Reservation {
    private static int counter = 1;
    private int reservationId;
    private String customerName;
    private String roomType;
    private int nights;

    public Reservation(String customerName, String roomType, int nights) {
        this.reservationId = counter++;
        this.customerName = customerName;
        this.roomType = roomType;
        this.nights = nights;
    }

    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Customer: " + customerName +
                ", Room: " + roomType +
                ", Nights: " + nights;
    }
}

// Inventory Management
class RoomInventory {
    private Map<String, Integer> rooms = new HashMap<>();

    public RoomInventory() {
        rooms.put("Standard", 2);
        rooms.put("Deluxe", 2);
        rooms.put("Suite", 1);
    }

    public boolean isValidRoomType(String roomType) {
        return rooms.containsKey(roomType);
    }

    public int getAvailableRooms(String roomType) {
        return rooms.getOrDefault(roomType, 0);
    }

    public void reserveRoom(String roomType) throws InvalidBookingException {
        int available = getAvailableRooms(roomType);

        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + roomType);
        }

        rooms.put(roomType, available - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Room Availability:");
        for (String type : rooms.keySet()) {
            System.out.println(type + ": " + rooms.get(type));
        }
    }
}

// Validator (Fail-Fast)
class BookingValidator {

    public static void validate(String customerName, String roomType, int nights, RoomInventory inventory)
            throws InvalidBookingException {

        if (customerName == null || customerName.trim().isEmpty()) {
            throw new InvalidBookingException("Customer name cannot be empty.");
        }

        if (nights <= 0) {
            throw new InvalidBookingException("Number of nights must be greater than zero.");
        }

        if (!inventory.isValidRoomType(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }

        if (inventory.getAvailableRooms(roomType) <= 0) {
            throw new InvalidBookingException("Selected room type is fully booked.");
        }
    }
}

// Booking Service
class BookingService {

    private RoomInventory inventory;
    private List<Reservation> history = new ArrayList<>();

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void createBooking(String customerName, String roomType, int nights) {
        try {
            // Step 1: Validate input (Fail-Fast)
            BookingValidator.validate(customerName, roomType, nights, inventory);

            // Step 2: Reserve room (state change)
            inventory.reserveRoom(roomType);

            // Step 3: Create reservation
            Reservation reservation = new Reservation(customerName, roomType, nights);
            history.add(reservation);

            System.out.println("Booking Confirmed: " + reservation);

        } catch (InvalidBookingException e) {
            // Graceful failure handling
            System.out.println("Booking Failed: " + e.getMessage());
        }
    }

    public void showBookings() {
        System.out.println("\n--- Booking History ---");
        for (Reservation r : history) {
            System.out.println(r);
        }
    }
}

// Main Class
public class Book_My_Stay_App {
    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingService service = new BookingService(inventory);

        // Valid Booking
        service.createBooking("Arun", "Deluxe", 2);

        // Invalid Room Type
        service.createBooking("Meena", "Premium", 2);

        // Invalid Nights
        service.createBooking("Raj", "Standard", 0);

        // Exhaust Inventory
        service.createBooking("John", "Suite", 1);
        service.createBooking("David", "Suite", 1); // should fail

        // Empty Name
        service.createBooking("", "Deluxe", 1);

        // Display Results
        service.showBookings();
        inventory.displayInventory();
    }
}