import java.util.*;

// Booking Request
class BookingRequest {
    private String customerName;
    private String roomType;

    public BookingRequest(String customerName, String roomType) {
        this.customerName = customerName;
        this.roomType = roomType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Shared Inventory (Thread-Safe)
class RoomInventory {
    private Map<String, Integer> rooms = new HashMap<>();

    public RoomInventory() {
        rooms.put("Standard", 2);
        rooms.put("Deluxe", 1);
        rooms.put("Suite", 1);
    }

    // Critical Section (Synchronized)
    public synchronized boolean allocateRoom(String roomType) {
        int available = rooms.getOrDefault(roomType, 0);

        if (available > 0) {
            // Simulate delay (to expose race conditions if not synchronized)
            try { Thread.sleep(100); } catch (InterruptedException e) {}

            rooms.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    public void displayInventory() {
        System.out.println("\nFinal Inventory:");
        for (String type : rooms.keySet()) {
            System.out.println(type + ": " + rooms.get(type));
        }
    }
}

// Shared Booking Queue
class BookingQueue {
    private Queue<BookingRequest> queue = new LinkedList<>();

    // Thread-safe add
    public synchronized void addRequest(BookingRequest request) {
        queue.add(request);
    }

    // Thread-safe retrieval
    public synchronized BookingRequest getRequest() {
        return queue.poll();
    }
}

// Worker Thread (Concurrent Booking Processor)
class BookingProcessor extends Thread {
    private BookingQueue queue;
    private RoomInventory inventory;

    public BookingProcessor(String name, BookingQueue queue, RoomInventory inventory) {
        super(name);
        this.queue = queue;
        this.inventory = inventory;
    }

    public void run() {
        while (true) {
            BookingRequest request;

            // Critical section for queue access
            synchronized (queue) {
                request = queue.getRequest();
            }

            if (request == null) {
                break; // No more requests
            }

            processBooking(request);
        }
    }

    private void processBooking(BookingRequest request) {
        boolean success = inventory.allocateRoom(request.getRoomType());

        if (success) {
            System.out.println(Thread.currentThread().getName() +
                    " SUCCESS: " + request.getCustomerName() +
                    " booked " + request.getRoomType());
        } else {
            System.out.println(Thread.currentThread().getName() +
                    " FAILED: No availability for " +
                    request.getCustomerName() + " (" + request.getRoomType() + ")");
        }
    }
}

// Main Class
public class Book_My_Stay_App {
    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingQueue queue = new BookingQueue();

        // Simulate multiple guest requests (concurrent load)
        queue.addRequest(new BookingRequest("Arun", "Deluxe"));
        queue.addRequest(new BookingRequest("Meena", "Deluxe")); // should fail (only 1)
        queue.addRequest(new BookingRequest("Raj", "Suite"));
        queue.addRequest(new BookingRequest("John", "Suite"));   // should fail
        queue.addRequest(new BookingRequest("David", "Standard"));
        queue.addRequest(new BookingRequest("Priya", "Standard"));

        // Create multiple threads (simulating concurrent users)
        BookingProcessor t1 = new BookingProcessor("Thread-1", queue, inventory);
        BookingProcessor t2 = new BookingProcessor("Thread-2", queue, inventory);
        BookingProcessor t3 = new BookingProcessor("Thread-3", queue, inventory);

        // Start threads
        t1.start();
        t2.start();
        t3.start();

        // Wait for completion
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {}

        // Final state
        inventory.displayInventory();
    }
}