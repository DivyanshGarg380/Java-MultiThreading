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
    Design and implement a menu-driven Java application to simulate a Hospital Bed Management System where multiple patients try to get admitted concurrently.
     . Create a class HospitalBed with the following data members:
        - int bedNumber
        - boolean occupied
        - String patientName
     . Include methods:
        - admitPatient(String name)
        - dischargePatient()
        - displayBedStatus()
     . Hospital should manage multiple beds
     . Store beds in an array
     . User selects bed using bed number
     . Multiple patient threads may try to admit to the SAME bed at the same time
     . This can cause a race condition
     . You must:
        - Use synchronized
        - Use wait() if bed is already occupied
        - Use notify() when bed becomes free
     . RULES:
        - If bed is occupied → patient thread must WAIT
        - When patient is discharged → waiting patient should be NOTIFIED
        - Use Thread.sleep() to simulate treatment time

     . Provide menu options to:
        - Count total occupied beds
        - Count available beds

     . Include menu options to:
        - Save bed status to hospital.txt
        - Read and display bed data from file
*/

import java.io.*;
import java.util.Scanner;

class HospitalBed {
    int bedNo;
    boolean occupied = false;
    String patient;

    public HospitalBed(int bedNo) {
        this.bedNo = bedNo;
    }

    synchronized void admitPatient(String name) {
        while (occupied) {
            try {
                System.out.println(name + " waiting for Bed " + bedNo);
                wait();
            } catch (InterruptedException e){}

            occupied = true;
            patient = name;
            System.out.println(name + " admitted to Bed " + bedNo);
            try {
                Thread.sleep(1000);
            } catch (Exception e) {}
        }
    }

    synchronized void dischargePatient() {
        if(occupied) {
            System.out.println(patient + " discharged from Bed " + bedNo);
            occupied = false;
            patient = null;
            notify();
        }
    }
    
    void display() {
        System.out.println("Bed " + bedNo + " : " + (occupied ? "Occupied by " + patient : "Available"));
    }
}

class PatientThread extends Thread {
    HospitalBed bed;
    String name;

    public PatientThread(HospitalBed bed, String name) {
        this.bed = bed;
        this.name = name;
    }

    public void run() {
        bed.admitPatient(name);
    }
}

public class Q4 {
    static HospitalBed[] beds = new HospitalBed[10];
    static int count = 0;

    static HospitalBed findBed(int bedNo) {
        for (int i = 0; i < count; i++)
            if (beds[i].bedNo == bedNo)
                return beds[i];
        return null;
    }
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        while (true) { 
            System.out.println("\n1.Add Bed");
            System.out.println("2.Admit Patient (Threads)");
            System.out.println("3.Discharge Patient");
            System.out.println("4.Display Bed Status");
            System.out.println("5.Count Beds");
            System.out.println("6.Save to File");
            System.out.println("7.Read from File");
            System.out.println("8.Exit");

            int ch = sc.nextInt();

            switch(ch) {
                case 1: 
                    System.out.println("Bed Number: ");
                    beds[count++] = new HospitalBed(sc.nextInt());
                    break;

                case 2: 
                    System.out.println("Bed Number: ");
                    int bno = sc.nextInt();
                    HospitalBed bed = findBed(bno);
                    if(bed != null) {
                        new PatientThread(bed, "Patient-A").start();
                        new PatientThread(bed, "Patient-B").start();
                    }
                    break;

                case 3 :
                    System.out.println("Bed Number");
                    bed = findBed(sc.nextInt());
                    if(bed != null) {
                        bed.dischargePatient();
                    }
                    break;

                case 4: 
                    for(int i = 0; i < count; i++) {
                        beds[i].display();
                    }
                    break;

                case 5 : 
                    int occupied = 0;
                    for(int i  = 0; i < count; i++) {
                        if(beds[i].occupied) occupied++;
                    }
                    System.out.println("Occupied Beds: " + occupied);
                    System.out.println("Available Beds: " + (count - occupied));
                    break;

                case 6: 
                    try {
                        FileWriter fw = new FileWriter("hospital.txt");
                        for (int i = 0; i < count; i++) {
                            fw.write(beds[i].bedNo + " " +
                                    beds[i].occupied + " " +
                                    beds[i].patient + "\n");
                        }
                        fw.close();
                    } catch (Exception e) {}
                    break;

                case 7: 
                    try {
                        FileReader fr = new FileReader("hospital.txt");
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
