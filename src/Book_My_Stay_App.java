import org.jetbrains.annotations.NotNull;

import java.util.*;

// Represents an optional add-on service
class AddOnService {
    private String serviceName;
    private double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return serviceName + " (₹" + cost + ")";
    }
}

// Manages mapping between reservation IDs and services
class AddOnServiceManager {

    // Map<ReservationID, List of Services>
    private Map<String, List<AddOnService>> reservationServicesMap = new HashMap<>();

    // Add a service to a reservation
    public void addService(String reservationId, AddOnService service) {
        reservationServicesMap
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);
    }

    // Get all services for a reservation
    public List<AddOnService> getServices(String reservationId) {
        return reservationServicesMap.getOrDefault(reservationId, new ArrayList<>());
    }

    // Calculate total cost of services
    public double calculateTotalServiceCost(String reservationId) {
        List<AddOnService> services = getServices(reservationId);
        double total = 0;

        for (AddOnService service : services) {
            total += service.getCost();
        }

        return total;
    }

    // Display services
    public void displayServices(String reservationId) {
        List<AddOnService> services = getServices(reservationId);

        if (services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        System.out.println("Selected Add-On Services:");
        for (AddOnService service : services) {
            System.out.println("- " + service);
        }
    }
}

// Main class (Use Case execution)
public class Book_My_Stay_App {

    public static void main(String[] args) {

        // Simulated existing reservation (core booking untouched)
        String reservationId = "RES123";

        // Create service manager
        AddOnServiceManager serviceManager = getAddOnServiceManager(reservationId);

        // Display selected services
        System.out.println("Reservation ID: " + reservationId);
        serviceManager.displayServices(reservationId);

        // Calculate additional cost
        double totalCost = serviceManager.calculateTotalServiceCost(reservationId);
        System.out.println("Total Add-On Cost: ₹" + totalCost);

        // Core booking remains unchanged (demonstration)
        System.out.println("\nNote: Core booking and inventory remain unaffected.");
    }

    private static @NotNull AddOnServiceManager getAddOnServiceManager(String reservationId) {
        AddOnServiceManager serviceManager = new AddOnServiceManager();

        // Guest selects add-on services
        AddOnService breakfast = new AddOnService("Breakfast", 500);
        AddOnService airportPickup = new AddOnService("Airport Pickup", 1200);
        AddOnService spa = new AddOnService("Spa Access", 2000);

        // Add services to reservation
        serviceManager.addService(reservationId, breakfast);
        serviceManager.addService(reservationId, airportPickup);
        serviceManager.addService(reservationId, spa);
        return serviceManager;
    }
}