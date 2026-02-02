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
Write a Java program to simulate an Inventory System where:

- An Inventory class has a variable stock = 100
- One thread adds stock
- Another thread removes stock
- Only one of the methods is synchronized
- Show that inconsistent results occur
- Then fix the program by synchronizing both methods
*/

class Inventory {
    private int stock = 100;

    public synchronized void addStock(int qty) {
        stock += qty;
        System.out.println("Added: " + qty + " | Stock: " + stock);
    }

    public synchronized void removeStock(int qty){
        stock -= qty;
        System.out.println("Removed: " + qty + " | Stock: " + stock);
    }
}

class Adder extends Thread {
    Inventory inv;
    Adder(Inventory inv) { this.inv = inv; }

    public void run() {
        for(int i = 0; i < 5; i++){
            inv.addStock(10);
            try { Thread.sleep(100); } catch(Exception e) {}
        }
    }
}

class Remover extends Thread {
    Inventory inv;
    Remover(Inventory inv) { this.inv = inv; }

    public void run() {
        for(int i = 0; i < 5; i++){
            inv.removeStock(10);
            try { Thread.sleep(100); } catch(Exception e) {}
        }
    }

}

public class Q13 {
    public static void main(String[] args) {
        Inventory inv = new Inventory();

        new Adder(inv).start();
        new Remover(inv).start();
    }   
}
