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
Write a Java program to simulate a hotel housekeeping system where:
- A Cleaner thread cleans rooms
- An Assignment thread assigns cleaned rooms to guests
- If no rooms are cleaned, assignment thread must wait
- When a room is cleaned, cleaner should notify
- Use synchronization + wait() + notify()
*/

class HotelHouseKeeping {
    private int cleanedRooms = 0;

    public synchronized void cleanRoom() {
        System.out.println("Cleaner: Cleaning room....");
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println("Error in CleanRoom");
        }

        cleanedRooms++;
        System.out.println("Cleaner: Room cleaned! Total cleaned rooms = " + cleanedRooms);
        notify();
    }

    public synchronized void assignRoom() {
        while(cleanedRooms == 0){
            System.out.println("Manager: No cleaned rooms. Waiting....");
            try {
                wait();
            } catch (Exception e) {
                System.out.println("Error in assignRoom");
            }
        }
        cleanedRooms--;
        System.out.println("Manager: Assigned cleaned room to the guest. Room left = " + cleanedRooms);
    }
}

class Cleaner extends Thread {
    HotelHouseKeeping hotel;

    Cleaner(HotelHouseKeeping hotel){
        this.hotel = hotel;
    }

    public void run() {
        hotel.cleanRoom();
    }
}

class Manager extends Thread {
    HotelHouseKeeping hotel;

    Manager(HotelHouseKeeping hotel){
        this.hotel = hotel;
    }

    public void run() {
        hotel.assignRoom();
    }
}

public class Q5 {
    public static void main(String[] args) {
        HotelHouseKeeping hotel = new HotelHouseKeeping();

        Manager manager = new Manager(hotel);
        Cleaner cleaner = new Cleaner(hotel);

        manager.start();
        cleaner.start();
    }
}
