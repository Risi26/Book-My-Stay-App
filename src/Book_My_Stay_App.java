import java.util.*;

// Custom Exception
class InvalidCancellationException extends Exception {
    public InvalidCancellationException(String message) {
        super(message);
    }
}

// Reservation Class
class Reservation {
    private static int counter = 1;
    private int reservationId;
    private String customerName;
    private String roomType;
    private boolean isCancelled;

    public Reservation(String customerName, String roomType) {
        this.reservationId = counter++;
        this.customerName = customerName;
        this.roomType = roomType;
        this.isCancelled = false;
    }

    public int getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void cancel() {
        isCancelled = true;
    }

    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Customer: " + customerName +
                ", Room: " + roomType +
                ", Status: " + (isCancelled ? "Cancelled" : "Confirmed");
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

    public void reserveRoom(String roomType) {
        rooms.put(roomType, rooms.get(roomType) - 1);
    }

    public void releaseRoom(String roomType) {
        rooms.put(roomType, rooms.get(roomType) + 1);
    }

    public int getAvailable(String roomType) {
        return rooms.getOrDefault(roomType, 0);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : rooms.keySet()) {
            System.out.println(type + ": " + rooms.get(type));
        }
    }
}

// Booking Service
class BookingService {
    private List<Reservation> history = new ArrayList<>();
    private RoomInventory inventory;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public Reservation createBooking(String customerName, String roomType) {
        if (inventory.getAvailable(roomType) <= 0) {
            System.out.println("Booking Failed: No rooms available.");
            return null;
        }

        inventory.reserveRoom(roomType);
        Reservation res = new Reservation(customerName, roomType);
        history.add(res);

        System.out.println("Booking Confirmed: " + res);
        return res;
    }

    public List<Reservation> getHistory() {
        return history;
    }
}

// Cancellation Service (Rollback using Stack)
class CancellationService {
    private Stack<String> rollbackStack = new Stack<>();
    private RoomInventory inventory;
    private List<Reservation> history;

    public CancellationService(RoomInventory inventory, List<Reservation> history) {
        this.inventory = inventory;
        this.history = history;
    }

    public void cancelBooking(int reservationId) {
        try {
            Reservation res = findReservation(reservationId);

            if (res == null) {
                throw new InvalidCancellationException("Reservation not found.");
            }

            if (res.isCancelled()) {
                throw new InvalidCancellationException("Reservation already cancelled.");
            }

            // Step 1: Push to rollback stack (LIFO tracking)
            rollbackStack.push(res.getRoomType());

            // Step 2: Restore inventory
            inventory.releaseRoom(res.getRoomType());

            // Step 3: Mark reservation as cancelled
            res.cancel();

            System.out.println("Cancellation Successful: " + res);

        } catch (InvalidCancellationException e) {
            System.out.println("Cancellation Failed: " + e.getMessage());
        }
    }

    private Reservation findReservation(int id) {
        for (Reservation r : history) {
            if (r.getReservationId() == id) {
                return r;
            }
        }
        return null;
    }

    public void showRollbackStack() {
        System.out.println("\nRollback Stack (Recent Releases): " + rollbackStack);
    }
}

// Main Class
public class Book_My_Stay_App {
    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingService bookingService = new BookingService(inventory);

        // Create Bookings
        Reservation r1 = bookingService.createBooking("Arun", "Deluxe");
        Reservation r2 = bookingService.createBooking("Meena", "Suite");

        // Cancellation Service
        CancellationService cancelService =
                new CancellationService(inventory, bookingService.getHistory());

        // Valid Cancellation
        cancelService.cancelBooking(r1.getReservationId());

        // Duplicate Cancellation
        cancelService.cancelBooking(r1.getReservationId());

        // Invalid ID
        cancelService.cancelBooking(999);

        // Cancel another booking
        cancelService.cancelBooking(r2.getReservationId());

        // Display final state
        System.out.println("\n--- Booking History ---");
        for (Reservation r : bookingService.getHistory()) {
            System.out.println(r);
        }

        inventory.displayInventory();
        cancelService.showRollbackStack();
    }
}