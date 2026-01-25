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
 Multiplex Booking System (Threads + Enum + Sync + wait/notify) but MENU DRIVEN
*/

import java.util.*;

enum SeatType {
    SILVER(150), GOLD(250), PLATINUM(400);
    private int price;

    SeatType(int price){
        this.price = price;
    }

    public int getPrice(){
        return price;
    }
}

class Multiplex {
    private int silver = 2, gold = 1, platinum = 1;

    public synchronized void book(String name, SeatType type, Integer qty){
        while(!available(type, qty)){
            System.out.println(name + " waiting for " + type + " seats..");
            try {
                wait();
            }catch(Exception e){}
        }

        allocate(type, qty);
        Integer bill = type.getPrice()*qty;
        System.out.println(name + " booked " + qty + " " + type + " seat(s). Bill = $" + bill);
        display();
    }

    public synchronized void cancel(SeatType type, Integer qty) {
        free(type, qty);
        System.out.println("Cancellation done for " + type);
        display();
        notifyAll();
    }

    private boolean available(SeatType t, int q){
        switch(t){
            case SILVER: return silver >= q;
            case GOLD: return gold >= q;
            case PLATINUM: return platinum >= q;
        }
        return false;
    }

    private void allocate(SeatType t, int q){
        switch(t){
            case SILVER: silver -= q; break;
            case GOLD: gold -= q; break;
            case PLATINUM: platinum -= q; break;
        }
    }

    private void free(SeatType t, int q){
        switch(t){
            case SILVER: silver += q; break;
            case GOLD: gold += q; break;
            case PLATINUM: platinum += q; break;
        }
    }

    private void display(){
        System.out.println("Seats SILVER:"+silver+" GOLD:"+gold+" PLATINUM:"+platinum+"\n");
    }
}

class BookingThread extends Thread {
    Multiplex multiplex;
    String name;
    SeatType type;
    Integer qty;

    BookingThread(Multiplex multiplex, String name, SeatType type, Integer qty){
        this.multiplex = multiplex;
        this.name = name;
        this.type =  type;
        this.qty = qty;
    }

    public void run() {
        multiplex.book(name, type, qty);
    }
}

class CancelThread extends Thread {
    Multiplex multiplex;
    SeatType type;
    Integer qty;

    CancelThread(Multiplex multiplex, SeatType type, Integer qty){
        this.multiplex = multiplex;
        this.type = type;
        this.qty = qty;
    }

    public void run() {
        multiplex.cancel(type, qty);
    }

}

public class Q10 {
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        Multiplex m=new Multiplex();

        while(true){
            System.out.println("\n1.Book Ticket\n2.Cancel Ticket\n3.Exit");
            int ch=sc.nextInt();

            switch(ch){
                case 1:
                    System.out.print("Name: ");
                    String n=sc.next();
                    System.out.print("Seat Type (SILVER/GOLD/PLATINUM): ");
                    SeatType t=SeatType.valueOf(sc.next().toUpperCase());
                    System.out.print("Quantity: ");
                    Integer q=sc.nextInt();
                    new BookingThread(m,n,t,q).start();
                    break;

                case 2:
                    System.out.print("Seat Type to cancel: ");
                    SeatType ct=SeatType.valueOf(sc.next().toUpperCase());
                    System.out.print("Quantity: ");
                    Integer cq=sc.nextInt();
                    new CancelThread(m,ct,cq).start();
                    break;

                case 3:
                    System.exit(0);
            }
        }
    }
}
