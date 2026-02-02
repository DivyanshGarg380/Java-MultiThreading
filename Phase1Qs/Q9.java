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
 Banking System (Threads + Sync + wait/notify) but MENU DRIVEN
*/

import java.util.*;

class BankAccount {
    private Integer balance;

    public BankAccount(Integer balance) {
        this.balance = balance;
    }

    public synchronized  void deposit(Integer amount) {
        balance += amount;
        System.out.println("Deposited $" + amount);
        System.out.println("Balance: $" + balance);
        notifyAll();
    }

    public synchronized void withdraw(Integer amount) {
        while(balance < amount) {
            System.out.println("Insufficient balance. Waiting for deposit...");
            try {
                wait();
            } catch (Exception e) {
                System.out.println("Error in WITHDRAW");
            }
        }

        balance -= amount;
        System.out.println("Withdrawal successful: $" + amount);
        System.out.println("Balance: $" + balance);
    }

    public synchronized void checkBalance(){
        System.out.println("Current balance: $" + balance);
    }
}

class withdrawThread extends Thread {
    BankAccount acc;
    Integer amount;
    
    withdrawThread(BankAccount acc, Integer amount){ 
        this.acc = acc;
        this.amount = amount;
    }

    public void run() {
        acc.withdraw(amount);
    }
}

class DepositThread extends Thread {
    BankAccount acc;
    Integer amount;

    DepositThread(BankAccount acc, Integer amount){
        this.acc = acc;
        this.amount = amount;
    }

    public void run() {
        acc.deposit(amount);
    }
}

public class Q9 {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        BankAccount acc = new BankAccount(2000);

        while(true){
            System.out.println("\n1. Deposit\n2. Withdraw\n3. Check Balance\n4. Exit");
            int ch = sc.nextInt();

            switch(ch) {
                case 1:
                    System.out.print("Enter deposit amount: ");
                    Integer d = sc.nextInt();
                    new DepositThread(acc, d).start();
                    break;
                case 2:
                    System.out.print("Enter withdrawal amount: ");
                    Integer w = sc.nextInt();
                    new withdrawThread(acc, w).start();
                    break;
                case 3:
                    acc.checkBalance();
                    break;
                case 4:
                    System.exit(0);
            }
        }
    }
}