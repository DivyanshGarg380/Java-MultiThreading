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
    Design and implement a menu-driven Java application to simulate a Hotel Room Management System.
    . Create a class Room with:
        - int roomNumber
        - String roomType
        - double pricePerNight
        - boolean isBooked

    . Include:
        - Constructor
        - Getter methods
    . Methods:
        - bookRoom()
        - checkoutRoom()
        - displayRoom()

    . The hotel should manage multiple rooms
    . Store rooms in an array
    . Allow booking using room number
    . Multiple guests (threads) may attempt to book the same room
    . Synchronize booking so:
        - Only one guest can book at a time
        - Use sleep() to simulate stay duration

    . Provide menu options to:
        - Calculate average room price
        - Count available rooms
    . Save all room details to rooms.txt
    . Read and display room details from file
*/

import java.io.*;
import java.util.Scanner;

class Room {
    int roomNo;
    String type;
    double price;
    boolean booked = false;

    Room(int roomNo, String type, double price) {
        this.roomNo = roomNo;
        this.type = type;
        this.price = price;
    }

    synchronized void book() {
        if(!booked) {
            booked = true;
            System.out.println(Thread.currentThread().getName() + " booked Room " + roomNo);
            try {
                Thread.sleep(1000);
            } catch (Exception e) {}
        } else {
            System.out.println("Room already booked");
        }
    }

    synchronized void checkout() {
        booked = false;
        System.out.println("Room " + roomNo + " is now available");
    }

    void display() {
        System.out.println(roomNo + " | " + type + " | " + price + " | " + (booked ? "Booked" : "Available"));
    }
}

class BookingThread extends Thread {
    Room room;

    public BookingThread(Room room) {
        this.room = room;
    }

    public void run() {
        room.book();
    }
}

public class Q3 {
    static Room[] rooms = new Room[10];
    static int count = 0;
    static Room findRoom(int roomNo) {
        for(int i = 0; i < count; i++){
            if(rooms[i].roomNo == roomNo) return rooms[i];
        }
        return null;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while(true) {
            System.out.println("\n1.Add Room\n2.Book Room (Threads)");
            System.out.println("3.Checkout Room\n4.Display Rooms");
            System.out.println("5.Average Price\n6.Save File");
            System.out.println("7.Read File\n8.Exit");

            int ch = sc.nextInt();

            switch(ch) {
                case 1:
                    rooms[count++] = new Room(sc.nextInt(), sc.next(), sc.nextDouble());
                    break;

                case 2: 
                    Room r = findRoom(sc.nextInt());
                    if(r != null) {
                        new BookingThread(r).start();
                        new BookingThread(r).start();
                    }
                    break;
                
                case 3: 
                    r = findRoom(sc.nextInt());
                    if(r != null) r.checkout();
                    break;

                case 4:
                    for(int i = 0; i < count; i++) rooms[i].display();
                    break;

                case 5:
                    double sum = 0;
                    for(int i = 0; i < count; i++) sum += rooms[i].price;
                    System.err.println("Average Price: " + (sum/count));
                    break;
                
                case 6:
                    try {
                        FileWriter fw = new FileWriter("rooms.txt");
                        for(int i = 0; i < count; i++) {
                            fw.write(rooms[i].roomNo + " " +
                                     rooms[i].type + " " +
                                     rooms[i].price + " " +
                                     rooms[i].booked + "\n");
                        }
                        fw.close();
                    } catch (Exception e) {}
                    break;

                case 7:
                    try {
                        FileReader fr = new FileReader("rooms.txt");
                        int c;
                        while ((c = fr.read()) != -1)
                            System.out.print((char) c);
                        fr.close();
                    } catch (Exception e) {}
                    break;

                case 8:
                    System.exit(0);    
            }
        }

    }
}
