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
Create a Java program to simulate a Bank Account where:
- The account has an initial balance (use Wrapper class)
- Two customer threads try to withdraw money at the same time
- Use synchronization to prevent overdrawing
- Display transaction messages and final balance
*/

class BankAccount {
    private Integer balance;

    public BankAccount(Integer balance) {
        this.balance = balance;
    }

    public synchronized void withdraw(String customer, Integer amount){
        System.out.println(customer + " is trying to withdraw $" + amount);
        if(balance >= amount){
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.println("Error inside withdraw");
            }

            balance -= amount;
            System.out.println("Withdrawal successful for " + customer);
        } else{
            System.out.println("Insufficient balance for " + customer);
        }

        System.out.println("Current balance: $ " + balance + "\n");
    }
}

class Customer extends Thread {
    BankAccount account;
    String name;
    Integer amount;

    Customer(BankAccount account, String name, Integer amount){
        this.account = account;
        this.name = name;
        this.amount = amount;
    }

    public void run(){
        account.withdraw(name, amount);
    }
}

public class Q2 {
    public static void main(String[] args) {
        BankAccount account = new BankAccount(5000);

        Customer c1 = new Customer(account, "Divyansh", 4000);
        Customer c2 = new Customer(account, "Starman", 2000);

        c1.run();
        c2.run();
    }
}
