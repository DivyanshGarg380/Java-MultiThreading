/*
Author :

███████╗████████╗ █████╗ ██████╗  ███╗   ███╗ █████╗ ███╗   ██╗
██╔════╝╚══██╔══╝██╔══██╗██╔══██╗ ████╗ ████║██╔══██╗████╗  ██║
███████╗   ██║   ███████║██████╔╝ ██╔████╔██║███████║██╔██╗ ██║
╚════██║   ██║   ██╔══██║██║  ██║ ██║╚██╔╝██║██╔══██║██║╚██╗██║
███████║   ██║   ██║  ██║██║  ██║ ██║ ╚═╝ ██║██║  ██║██║ ╚████║
╚══════╝   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝ ╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═══╝  STARMAN248
*/


/*
    Design a Hotel Management System using Java that:
    -  Uses OOP concepts
    -  Uses Wrapper classes
    -  Uses Enum
    -  Uses Multithreading
    -  Uses Synchronization

    Scenario:
    Two guests try to book rooms at the same time.
    Each room has a RoomType (enum) and tariff.
    Bill should be calculated using Wrapper classes.
    Synchronization must prevent double booking.
*/

enum RoomType {
    STANDARD(2000),
    DELUXE(3500),
    SUITE(5000);

    private int tariff;

    RoomType(int tariff) {
        this.tariff = tariff;
    }

    public int getTariff(){
        return tariff;
    }
}

class Hotel {
    private int availableRooms = 1; // for race condition

    public synchronized void bookRoom(String guestName, RoomType type, Integer days){
        System.out.println("\n" + guestName + " trying to book " + type + " room...");

        if(availableRooms > 0){
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.println("Error in bookRoom function");
            }
            availableRooms--;
            Integer tariffPerDay = type.getTariff(); // autoboxing here
            Integer totalBill = tariffPerDay * days; // unboxing due to mulitplication

            System.out.println("Booking Success for " + guestName);
            System.out.println("Room Type: " + type);
            System.out.println("Days: " + days);
            System.out.println("Total Bill: " + totalBill);
        } else {
            System.out.println("No rooms available for " + guestName);
        }
        System.out.println("Rooms left: " + availableRooms);
    }
}

class Guest extends Thread {
    Hotel hotel;
    String name;
    RoomType type;
    Integer days;

    Guest(Hotel hotel, String name, RoomType type, Integer days) {
        this.hotel = hotel;
        this.name = name;
        this.type = type;
        this.days = days;
    }

    public void run() {
        hotel.bookRoom(name, type, days);
    }
}

public class Q1 {
    public static void main(String[] args) {

        Hotel hotel = new Hotel();

        Guest g1 = new Guest(hotel, "Divyansh", RoomType.DELUXE, 3);
        Guest g2 = new Guest(hotel, "Starman", RoomType.SUITE, 2);

        g1.start();
        g2.start();
    }
}