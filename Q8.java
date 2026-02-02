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
Design a Java program for a Multiplex Ticket Booking System where:

- Seats are of different types: SILVER, GOLD, PLATINUM (enum)
- Each type has limited seats
- Customers (threads) try to book seats
- If seats of that type are full → customer must wait
- A Cancellation thread cancels a booking and frees seats
- Waiting customers should be notified
- Bill must be calculated using Wrapper classes
- Use synchronization + wait() + notifyAll()
*/

enum SeatType {
    SILVER(150),
    GOLD(250),
    PLATINUM(400);

    private int price;

    SeatType(int price) {
        this.price = price;
    }

    public int getPrice(){
        return price;
    }
}

class Multiplex {
    private int silverSeats = 1;
    private int goldSeats = 1;
    private int platinumSeats = 1;

    private boolean isSeatAvailable(SeatType type, int qty){
        switch (type){
            case SILVER: return silverSeats >= qty;
            case GOLD: return goldSeats >= qty;
            case PLATINUM: return platinumSeats >= qty;
        }
        return false;
    }

    public synchronized void bookTicket(String name, SeatType type, Integer quantity){
        while(!isSeatAvailable(type, quantity)){
            System.out.println(name + ": Seats not available in " + type + ". Waiting....");
            try {
                wait();
            } catch (Exception e) {
                System.out.println("Error in bookTicker");
            }
        }

        allocateSeat(type, quantity);

        Integer totalCost = type.getPrice() * quantity;
        System.out.println("Booking SUCCESS for " + name);
        System.out.println("Seat Type: " + type);
        System.out.println("Quantity: " + quantity);
        System.out.println("Total Bill: $" + totalCost);
        displaySeats();
    }

    private void allocateSeat(SeatType type, int qty) {
        switch (type) {
            case SILVER: silverSeats -= qty; break;
            case GOLD: goldSeats -= qty; break;
            case PLATINUM: platinumSeats -= qty; break;
        }
    }

    private void displaySeats() {
        System.out.println("Available Seats: SILVER: " + silverSeats +
                ", GOLD: " + goldSeats +
                ", PLATINUM: " + platinumSeats + "\n");
    }

    private void freeSeat(SeatType type, int qty){
        switch(type){
            case SILVER: silverSeats += qty; break;
            case GOLD: goldSeats += qty; break;
            case PLATINUM: platinumSeats += qty; break;
        }
    }

    public synchronized void cancelTicket(SeatType type, Integer quantity){
        System.out.println("Cancellation: " + quantity + " " + type + " seat(s) freed.");
        freeSeat(type, quantity);
        displaySeats();
        notifyAll(); 
    }
}

class Customer extends Thread {
    Multiplex multiplex;
    String name;
    SeatType type;
    Integer quantity;

    Customer(Multiplex multiplex, String name, SeatType type, Integer quantity){
        this.multiplex = multiplex;
        this.name = name;
        this.type = type;
        this.quantity = quantity;
    }

    public void run() {
        multiplex.bookTicket(name, type, quantity);
    }
}

class Cancellation extends Thread {
    Multiplex multiplex;
    SeatType type;
    Integer quantity;

    Cancellation(Multiplex multiplex, SeatType type, Integer qty){
        this.multiplex = multiplex;
        this.type = type;
        this.quantity = qty;
    }

    public void run() {
        multiplex.cancelTicket(type, quantity);
    }
}

public class Q8 {
    public static void main(String[] args) {
        Multiplex multiplex = new Multiplex();

        Customer c1 = new Customer(multiplex, "Divyansh", SeatType.GOLD, 1);
        Customer c2 = new Customer(multiplex, "Starman", SeatType.GOLD, 1); 
        Customer c3 = new Customer(multiplex, "A380", SeatType.SILVER, 1);

        Cancellation cancel = new Cancellation(multiplex, SeatType.GOLD, 1);

        c1.start();
        c2.start();
        c3.start();
        cancel.start();
    }
}
