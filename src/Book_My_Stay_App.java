import java.io.*;
import java.util.*;

// Reservation (Serializable)
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int counter = 1;
    private int reservationId;
    private String customerName;
    private String roomType;

    public Reservation(String customerName, String roomType) {
        this.reservationId = counter++;
        this.customerName = customerName;
        this.roomType = roomType;
    }

    public int getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Customer: " + customerName +
                ", Room: " + roomType;
    }
}

// Inventory (Serializable)
class RoomInventory implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Integer> rooms = new HashMap<>();

    public RoomInventory() {
        rooms.put("Standard", 2);
        rooms.put("Deluxe", 2);
        rooms.put("Suite", 1);
    }

    public boolean allocateRoom(String roomType) {
        int available = rooms.getOrDefault(roomType, 0);
        if (available > 0) {
            rooms.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : rooms.keySet()) {
            System.out.println(type + ": " + rooms.get(type));
        }
    }

    public Map<String, Integer> getRooms() {
        return rooms;
    }

    public void setRooms(Map<String, Integer> rooms) {
        this.rooms = rooms;
    }
}

// Wrapper for Persistence
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    List<Reservation> reservations;
    RoomInventory inventory;

    public SystemState(List<Reservation> reservations, RoomInventory inventory) {
        this.reservations = reservations;
        this.inventory = inventory;
    }
}

// Persistence Service
class PersistenceService {
    private static final String FILE_NAME = "system_state.dat";

    // Save state
    public static void save(SystemState state) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("\nSystem state saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving system state: " + e.getMessage());
        }
    }

    // Load state
    public static SystemState load() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("No previous state found. Starting fresh.");
            return null;
        }

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            SystemState state = (SystemState) ois.readObject();
            System.out.println("System state loaded successfully.");
            return state;

        } catch (Exception e) {
            System.out.println("Error loading state. Starting with clean state.");
            return null;
        }
    }
}

// Booking Service
class BookingService {
    private List<Reservation> history;
    private RoomInventory inventory;

    public BookingService(List<Reservation> history, RoomInventory inventory) {
        this.history = history;
        this.inventory = inventory;
    }

    public void createBooking(String customerName, String roomType) {
        if (inventory.allocateRoom(roomType)) {
            Reservation res = new Reservation(customerName, roomType);
            history.add(res);
            System.out.println("Booking Confirmed: " + res);
        } else {
            System.out.println("Booking Failed: No rooms available.");
        }
    }

    public void showBookings() {
        System.out.println("\n--- Booking History ---");
        for (Reservation r : history) {
            System.out.println(r);
        }
    }

    public List<Reservation> getHistory() {
        return history;
    }

    public RoomInventory getInventory() {
        return inventory;
    }
}

// Main Class
public class Book_My_Stay_App {
    public static void main(String[] args) {

        // Step 1: Load previous state (if exists)
        SystemState loadedState = PersistenceService.load();

        List<Reservation> history;
        RoomInventory inventory;

        if (loadedState != null) {
            history = loadedState.reservations;
            inventory = loadedState.inventory;
        } else {
            history = new ArrayList<>();
            inventory = new RoomInventory();
        }

        BookingService service = new BookingService(history, inventory);

        // Step 2: Perform operations
        service.createBooking("Arun", "Deluxe");
        service.createBooking("Meena", "Suite");

        service.showBookings();
        inventory.displayInventory();

        // Step 3: Save state before shutdown
        SystemState state = new SystemState(service.getHistory(), service.getInventory());
        PersistenceService.save(state);

        System.out.println("\n--- Restart the program to see recovery in action ---");
    }
}