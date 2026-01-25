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
Write a Java program for a Hotel Management System where:

- Hotel has limited rooms of different RoomType (enum)
- Guests (threads) try to book rooms
- If no rooms are available → guest must wait
- Cleaner thread cleans rooms and makes them available
- Cleaner should notify waiting guests
- Bill should be calculated using Wrapper classes
- Use proper synchronization
*/

enum RoomType {
    STANDARD(2000),
    DELUXE(3500),
    SUITE(5000);

    private int tariff;

    RoomType(int tariff){
        this.tariff = tariff;
    }

    public int getTariff() {
        return tariff;
    }
}

class Hotel {
    private int availableRooms = 1;

    public synchronized void bookRoom(String guestName, RoomType type, Integer days){
        while(availableRooms == 0){
            System.out.println(guestName + ": No rooms available. Waiting...");
            try {
                wait();
            } catch (Exception e) {
                System.out.println("Error in BookRoom");
            }
        }

        availableRooms--;

        Integer totalBill = type.getTariff()*days;
        System.out.println("Room booked for " + guestName);
        System.out.println("Room Type: " + type);
        System.out.println("Days: " + days);
        System.out.println("Total Bill: $" + totalBill);
        System.out.println("Rooms left: " + availableRooms + "\n");
    }

    public synchronized void cleanRoom() {
        System.out.println("Cleaner: Cleaning room....");
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println("Error in cleanRoom");
        }

        availableRooms++;
        System.out.println("Cleaner: Room ready! Rooms available = " + availableRooms);

        notifyAll();
    }

}

class Guest extends Thread {
    Hotel hotel;
    String name;
    RoomType type;
    Integer days;

    Guest(Hotel hotel, String name, RoomType type, Integer days){
        this.hotel = hotel;
        this.name = name;
        this.type = type;
        this.days = days;
    }

    public void run() {
        hotel.bookRoom(name, type, days);
    }
}

class Cleaner extends Thread {
    Hotel hotel;

    Cleaner(Hotel hotel){
        this.hotel = hotel;
    }

    public void run(){
        hotel.cleanRoom();
    }
}

public class Q6 {
    public static void main(String[] args) {
        
        Hotel hotel = new Hotel();

        Guest g1 = new Guest(hotel, "Divyansh", RoomType.DELUXE, 3);
        Guest g2 = new Guest(hotel, "Starman", RoomType.SUITE, 2);

        Cleaner cleaner = new Cleaner(hotel);

        g1.start();
        g2.start();
        cleaner.start();

    }
}
