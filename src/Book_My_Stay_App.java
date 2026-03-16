import java.util.*;

/**
 * Main allocation logic for the Book_My_Stay_App.
 * This class handles the logic of assigning physical room IDs to reservations.
 */
public class Book_My_Stay_App {

    private Set<String> allocatedRoomIds;
    private Map<String, Set<String>> assignedRoomsByType;

    public Book_My_Stay_App() {
        this.allocatedRoomIds = new HashSet<>();
        this.assignedRoomsByType = new HashMap<>();
    }

    /**
     * Confirms a booking request within the Book_My_Stay_App ecosystem.
     */
    public void allocateRoom(Reservation reservation, RoomInventory inventory) {
        String type = reservation.getRoomType();

        if (inventory.isAvailable(type)) {
            String roomId = generateRoomId(type);

            allocatedRoomIds.add(roomId);
            assignedRoomsByType.computeIfAbsent(type, k -> new HashSet<>()).add(roomId);

            reservation.setRoomId(roomId);
            inventory.decrementInventory(type);

            System.out.println("[Book_My_Stay_App] Success: Room " + roomId + " assigned.");
        } else {
            System.out.println("[Book_My_Stay_App] Error: No " + type + " rooms available.");
        }
    }

    private String generateRoomId(String roomType) {
        String newId;
        Random rand = new Random();
        do {
            // Generates ID like "D101" for Deluxe or "S502" for Suite
            newId = roomType.substring(0, 1).toUpperCase() + (101 + rand.nextInt(899));
        } while (allocatedRoomIds.contains(newId));

        return newId;
    }
}

// --- Supporting Classes to make the code functional ---

class Reservation {
    private String roomType;
    private String roomId;

    public Reservation(String roomType) { this.roomType = roomType; }
    public String getRoomType() { return roomType; }
    public void setRoomId(String roomId) { this.roomId = roomId; }
    public String getRoomId() { return roomId; }
}

class RoomInventory {
    private Map<String, Integer> stock = new HashMap<>();

    public void addStock(String type, int count) { stock.put(type, count); }

    public boolean isAvailable(String type) {
        return stock.getOrDefault(type, 0) > 0;
    }

    public void decrementInventory(String type) {
        stock.put(type, stock.get(type) - 1);
    }
}
