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
Design and implement a menu-driven Java application to simulate a Bank Management System where multiple bank accounts are managed.
   . Create a class BankAccount with the following data members:
     - int accountNumber
     - String accountHolderName
     - double balance
   . Include:
     - Constructor to initialize account details
     - Getter methods
   . Methods:
     - deposit(double amount)
     - withdraw(double amount)
     - displayAccount()
     
   . The bank should support multiple accounts
   . Accounts should be stored in an array
   . User should be able to:
     - Create multiple accounts
     - Select an account using account number

   . Multiple customers (threads) may attempt to withdraw money from the SAME account
   . Ensure thread synchronization so:
     - Balance never becomes negative
     - Only one withdrawal happens at a time
     - Use Thread.sleep() to simulate transaction delay

   . Provide menu options to calculate:
     - Average balance of all accounts
     -Total bank balance

   . Include menu options to:
    - Save all account details to a file bank_data.txt
    - Read and display data from the file
*/

import java.io.*;
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

    synchronized void withdraw(double amount) {
        System.out.println(Thread.currentThread().getName() + " trying to withdraw " + amount);
        if(balance >= amount) {
            try {
                Thread.sleep(1000);   
            } catch (InterruptedException e) {}
            balance -= amount;
            System.out.println("Withdrawal successful. Remaining: " + balance);
        } else {
            System.out.println("Insufficient balance");
        }

    }

    void deposit(double amount) {
        balance += amount;
        System.out.println("Deposit succesfull");
    }

    void display() {
        System.out.println(accNo + " | " + name + " | Balance: " + balance);
    }
}

class WithdrawThread extends Thread {
    BankAccount account;
    double amount;

    public WithdrawThread(BankAccount acc, double amt) {
        this.account = acc;
        this.amount = amt;
    }

    public void run() {
        account.withdraw(amount);
    }
}

public class Q2 {
    static BankAccount[] accounts = new BankAccount[10];
    static int count = 0;
    
    static BankAccount findAccount(int accNo){ 
        for(int i = 0; i < count; i++) {
            if(accounts[i].accNo == accNo) return accounts[i];
        }

        return null;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while(true) {
            System.out.println("\n1.Create Account");
            System.out.println("2.Deposit");
            System.out.println("3.Withdraw (Threads)");
            System.out.println("4.Display All Accounts");
            System.out.println("5.Average Balance");
            System.out.println("6.Save to File");
            System.out.println("7.Read from File");
            System.out.println("8.Exit");


            int choice = sc.nextInt();

            switch(choice) {
                case 1: 
                    System.out.println("Account No: ");
                    int accNo  = sc.nextInt();
                    System.out.println("Name: ");
                    String name = sc.next();
                    System.out.print("Initial Balance: ");
                    double bal = sc.nextDouble();

                    accounts[count++] = new BankAccount(accNo, name, bal);
                    break;

                case 2: 
                    System.out.println("Account No: ");
                    accNo = sc.nextInt();
                    BankAccount acc = findAccount(accNo);
                    if(acc != null) {
                        System.out.println("Amount: ");
                        acc.deposit(sc.nextDouble());
                    }
                    break;
                
                case 3: 
                    System.out.println("Account No: ");
                    accNo = sc.nextInt();
                    acc = findAccount(accNo);
                    if(acc != null) {
                        System.out.println("Amount: ");
                        double amt = sc.nextDouble();
                        new WithdrawThread(acc, amt).start();
                        new WithdrawThread(acc, amt).start();
                    }
                    break;

                case 4: 
                    for (int i = 0; i < count; i++)
                        accounts[i].display();
                    break;
                
                case 5: 
                    double total = 0;
                    for (int i = 0; i < count; i++)
                        total += accounts[i].balance;
                    System.out.println("Average Balance: " + (total / count));
                    break;

                case 6: 
                    try {
                        FileWriter fw = new FileWriter("bank_data.txt");
                        for(int i = 0; i < count; i++) {
                            fw.write(accounts[i].accNo + " " + accounts[i].name + " " + accounts[i].balance + "\n");
                        }
    
                        fw.close();
                        System.out.println("Saved to file");
                    } catch (Exception e) {}
                    break;


                case 7: 
                    try {
                        FileReader fr = new FileReader("bank.txt");
                        int ch;
                        while((ch = fr.read()) != -1) System.out.println((char)ch);
                        fr.close();
                    } catch (Exception e) {}
                    break;

                case 8: 
                    System.exit(0);
            }
        }
    }
}
