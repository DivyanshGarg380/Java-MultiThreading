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
Design a menu-driven Java application to simulate a Bank Account Management System.

Requirements:

    . Create a class BankAccount with:
        - accountNumber (int)
        - holderName (String)
        - balance (double)
        - Methods:
            - deposit(double amount)
            - withdraw(double amount)
            - displayBalance()

    . Multiple customers (threads) may try to withdraw money at the same time
    . Ensure synchronization so balance never becomes negative
    . Use sleep() to simulate processing delay

    . One menu option should:
       - Save account details to a file bank.txt
       - Read and display file contents
    
*/

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;


class BankAccount {
    int accNo;
    String name;
    double balance;

    public BankAccount(int accNo, String name, double balance) {
        this.accNo = accNo;
        this.name = name;
        this.balance = balance;
    }

    synchronized void withdraw(double amt) {
        System.out.println(Thread.currentThread().getName() + " trying to withdraw " + amt);
        if(balance >= amt) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {}

            balance -= amt;
            System.out.println("Withdrawal Successful. Remaining: " + balance);
        }else {
            System.out.println("Insufficient balance");
        }
    }

    void deposit(double amt) {
        balance += amt;
        System.out.println("Deposit successful");
    }

    void display() {
        System.out.println("Balance : " + balance);
    }
}

class withdrawThread extends Thread {
    BankAccount acc;
    double amt;
    withdrawThread(BankAccount acc, double amt) {
        this.acc = acc;
        this.amt = amt;
    }

    public void run() {
        acc.withdraw(amt);
    }
}

public class Q1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BankAccount acc = new BankAccount(101, "Starman", 5000);

        while(true) {
            System.out.println("\n1.Deposit\n2.Withdraw (Threads)\n3.Display\n4.Save File\n5.Read File\n6.Exit");
            int ch = sc.nextInt();

            switch(ch) {
                case 1:
                        acc.deposit(sc.nextDouble());
                        break;
                
                case 2:
                        new withdrawThread(acc, 3000).start();
                        new withdrawThread(acc, 3000).start();
                        break;
                
                case 3:
                        acc.display();
                        break;

                case 4:
                        try {
                           FileWriter  fw = new FileWriter("bank.txt");
                            fw.write(acc.accNo + " " + acc.name + " " + acc.balance);
                            fw.close(); 
                        } catch (Exception e) {
                            System.out.println("Error while writing in Bank.txt: " + e);
                        }
                        break;
                
                case 5: 
                        try {
                            FileReader fr = new FileReader("bank.txt");
                            int c;
                            while((c = fr.read()) != -1) System.out.println((char)c);
                            fr.close();
                            
                        }catch(Exception e){
                            System.out.println("Error while reading bank.txt: " + e);
                        }
                        break;

                case 6: System.exit(0);
            }
        }
    }   
}
