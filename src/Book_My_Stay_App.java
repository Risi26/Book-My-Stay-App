public class Book_My_Stay_App {

    public static void main(String[] args) {

        System.out.println("===========================================");
        System.out.println("   HOTEL BOOKING MANAGEMENT SYSTEM");
        System.out.println("===========================================");

        System.out.println("Welcome to the Hotel Booking System!");
        System.out.println("Application is starting...");

        System.out.println("System initialized successfully.");
        System.out.println("Ready for further operations.");

        System.out.println("-------------------------------------------");

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