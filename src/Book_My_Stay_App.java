import java.util.HashMap;
import java.util.Map;

public class Book_My_Stay_App {

    public static void main(String[] args) {

        System.out.println("===========================================");
        System.out.println("        HOTEL BOOKING MANAGEMENT SYSTEM");
        System.out.println("===========================================");

        System.out.println("Welcome to the Hotel Booking System!");
        System.out.println("Application is starting...\n");

        // Create inventory
        RoomInventory inventory = new RoomInventory();

        System.out.println("Available Rooms:");
        System.out.println("--------------------------------");

        for (Map.Entry<String, Integer> entry : inventory.getRoomAvailability().entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue() + " rooms available");
        }

        System.out.println("--------------------------------");

        // Create sample room
        Room room = new SingleRoom();
        room.displayRoomDetails();

        System.out.println("System initialized successfully.");
        System.out.println("Ready for further operations.");
    }
}

abstract class Room {

    protected int numberOfBeds;
    protected int squareFeet;
    protected double pricePerNight;

    public Room(int numberOfBeds, int squareFeet, double pricePerNight) {
        this.numberOfBeds = numberOfBeds;
        this.squareFeet = squareFeet;
        this.pricePerNight = pricePerNight;
    }

    public void displayRoomDetails() {
        System.out.println("Room Details:");
        System.out.println("Number of Beds: " + numberOfBeds);
        System.out.println("Room Size (sq ft): " + squareFeet);
        System.out.println("Price per Night: $" + pricePerNight);
        System.out.println("--------------------------------");
    }
}

// Example subclass
class SingleRoom extends Room {

    public SingleRoom() {
        super(1, 200, 50.0);
    }
}

// Inventory class (separate class)
class RoomInventory {

    private final Map<String, Integer> roomAvailability;

    public RoomInventory() {
        roomAvailability = new HashMap<>();
        initializeInventory();
    }

    private void initializeInventory() {
        roomAvailability.put("Single Room", 10);
        roomAvailability.put("Double Room", 8);
        roomAvailability.put("Deluxe Room", 5);
        roomAvailability.put("Suite Room", 3);
    }

    public Map<String, Integer> getRoomAvailability() {
        return roomAvailability;
    }

    public void updateAvailability(String roomType, int count) {
        roomAvailability.put(roomType, count);
        class RoomSearchService {

            public void searchAvailableRooms(
                    RoomInventory inventory,
                    Room singleRoom,
                    Room doubleRoom,
                    Room suiteRoom) {

                // Method implementation logic would go here

            }
        }
    }
}