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
    Design and implement a menu-driven Java application to simulate a Traffic Signal Management System at a road junction.
    . Multiple vehicles arrive at the junction and attempt to cross the signal simultaneously.
    . Create a class TrafficSignal with the following data members:
        - String signalName (e.g., North, South, East, West)
        - boolean green (true if signal is green)
        - int vehiclesCrossed
    . Include methods:
        - allowVehicle(String vehicleName)
        - changeSignal(boolean status)
        - displayStatus()
    . Junction has multiple traffic signals
    . Signals stored in an array
    . User selects signal using signal name
    . Multiple vehicle threads may try to cross the same signal at the same time
    . If signal is GREEN, vehicles may cross
    . If signal is RED, vehicles are blocked
    . Race condition risk:
        - Multiple threads updating vehiclesCrossed
    .REQUIREMENTS:
        - Use synchronized to:
        - Prevent incorrect vehicle count
        - Prevent simultaneous unsafe crossing
        - Use Thread.sleep() to simulate crossing time
    . Provide menu options to:
        - Display total vehicles crossed per signal
        - Display total vehicles crossed at junction

    . Include menu options to:
        - Save traffic statistics to traffic.txt
        - Read and display traffic data from file
*/

import java.io.*;
import java.util.Scanner;

class TrafficSignal {
    String name;
    boolean green = false;
    int vehiclesCrossed = 0;

    public TrafficSignal(String name) {
        this.name = name;
    }

    synchronized void allowVehicle(String vehicle) {
        if(!green) {
            System.out.println(vehicle + " stopped at RED signal: " + name);
            return;
        }

        System.out.println(vehicle + " crossing at " + name);
        try {
            Thread.sleep(500);
        } catch (Exception e) {}
        vehiclesCrossed++;
    }

    void changeSignal(boolean status) {
        green = status;
        System.out.println(name + " signal is now " + (green ? "GREEN" : "RED"));
    }

    void display() {
        System.out.println(name + " | " +
                (green ? "GREEN" : "RED") +
                " | Vehicles crossed: " + vehiclesCrossed);
    }
}

class VehicleThread extends Thread {
    TrafficSignal signal;
    String vehicle;

    public VehicleThread(TrafficSignal signal, String vehicle) {
        this.signal = signal;
        this.vehicle = vehicle;
    }

    public void run() {
        signal.allowVehicle(vehicle);
    }
}

public class Q6 {
    static TrafficSignal[] signals = new TrafficSignal[5];
    static int count = 0;

    static TrafficSignal findSignal(String name) {
        for (int i = 0; i < count; i++)
            if (signals[i].name.equalsIgnoreCase(name))
                return signals[i];
        return null;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n1.Add Signal");
            System.out.println("2.Change Signal");
            System.out.println("3.Vehicle Crossing (Threads)");
            System.out.println("4.Display Signals");
            System.out.println("5.Traffic Statistics");
            System.out.println("6.Save to File");
            System.out.println("7.Read from File");
            System.out.println("8.Exit");

            int ch = sc.nextInt();

            switch (ch) {

                case 1:
                    System.out.print("Signal Name: ");
                    signals[count++] = new TrafficSignal(sc.next());
                    break;

                case 2:
                    System.out.print("Signal Name: ");
                    TrafficSignal s = findSignal(sc.next());
                    if (s != null) {
                        System.out.print("1.Green 0.Red: ");
                        s.changeSignal(sc.nextInt() == 1);
                    }
                    break;

                case 3:
                    System.out.print("Signal Name: ");
                    s = findSignal(sc.next());
                    if (s != null) {
                        new VehicleThread(s, "Car-1").start();
                        new VehicleThread(s, "Car-2").start();
                        new VehicleThread(s, "Bike-1").start();
                    }
                    break;

                case 4:
                    for (int i = 0; i < count; i++)
                        signals[i].display();
                    break;

                case 5:
                    int total = 0;
                    for (int i = 0; i < count; i++)
                        total += signals[i].vehiclesCrossed;
                    System.out.println("Total Vehicles Crossed: " + total);
                    break;

                case 6:
                    try {
                        FileWriter fw = new FileWriter("traffic.txt");
                        for (int i = 0; i < count; i++)
                            fw.write(signals[i].name + " " +
                                     signals[i].vehiclesCrossed + "\n");
                        fw.close();
                    } catch (Exception e) {}
                    break;

                case 7:
                    try {
                        FileReader fr = new FileReader("traffic.txt");
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