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
    Design and implement a menu-driven Java application to simulate a Shared Printer System in a computer lab.
    Multiple users send print jobs to ONE printer simultaneously.
    . Create a class Printer with the following data members:
        - boolean busy
        - String currentJob
    . Include methods:
        - submitJob(String jobName)
        - completeJob()
        - displayStatus()
    . Users submit multiple print jobs
    . Jobs are identified by job name
    . Jobs are processed one at a time
    . Multiple user threads may try to print at the same time
    . If printer is busy:
        - New job must WAIT
        - When current job finishes:
        - Waiting job must be NOTIFIED
    . IMPORTANT:
        - If synchronization is NOT used → race condition occurs
        - You must prevent multiple jobs printing simultaneously

    . Provide menu options to:
        - Count total jobs printed
        - Show printer idle/busy time
        - Save completed job history to print_log.txt
        -Read and display print history from file
*/

import java.io.*;
import java.util.Scanner;

class Printer {
    boolean busy = false;
    String currentJob;
    int totalJobs = 0;

    synchronized void submitJob(String jobName) {
        while(busy) {
            try {
                System.out.println(jobName + " waiting for printer...");
                wait();
            } catch(InterruptedException e){}
        }

        busy = true;
        currentJob = jobName;
        System.out.println("Printing started: " + jobName);
        try {
            Thread.sleep(1000);
        } catch (Exception e) {}
    }

    synchronized void completeJob() {
        if(busy) {
            System.out.println("Printing completed: " + currentJob);
            log(currentJob);
            totalJobs++;
            busy = false;
            currentJob = null;
            notify();
        }
    }

    void displayStatus() {
        if(busy) System.out.println("Printer Busy with Job: " + currentJob);
        else System.out.println("Printer Idle rn");
    }

    void log(String job) {
        try {
            FileWriter fw = new FileWriter("print_log.txt");
            fw.write(job + "\n");
        } catch (IOException e) {}
    }
}

class PrintJobThread extends Thread {
    Printer printer;
    String name;

    PrintJobThread(Printer printer, String name) {
        this.printer = printer;
        this.name = name;
    }

    public void run() {
        printer.submitJob(name);
    }
}

public class Q5 {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        Printer printer = new Printer();

        while (true) { 
            System.out.println("\n1.Submit Print Job");
            System.out.println("2.Complete Current Job");
            System.out.println("3.Display Printer Status");
            System.out.println("4.Total Jobs Printed");
            System.out.println("5.Save Print Log");
            System.out.println("6.Read Print Log");
            System.out.println("7.Exit");

            int ch = sc.nextInt();
            switch(ch) {
                case 1:
                    System.out.println("Enter Job Name: ");
                    String job = sc.next();
                    new PrintJobThread(printer, job).start();
                    new PrintJobThread(printer, job + "_COPY").start();
                    break;

                case 2:
                    printer.completeJob();
                    break;
                
                case 3:
                    printer.displayStatus();
                    break;
            
                case 4:
                    System.out.println("Total Jobs Printed: " + printer.totalJobs);
                    break;

                case 5:
                    System.out.println("Jobs automatically saved during printing.");
                    break;

                case 6:
                    FileReader fr = new FileReader("print_log.txt");
                    int c;
                    while ((c = fr.read()) != -1)
                        System.out.print((char) c);
                    fr.close();
                    break;

                case 7:
                    System.exit(0);
            }
        }

    }
}