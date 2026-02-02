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
Write a Java program to simulate a restaurant where:

-  A Chef thread prepares food

-  A Waiter thread serves food

-  Waiter must wait if food is not ready

-  Chef will notify the waiter once food is prepared

-  Use synchronization + wait() + notify()
*/

class Restaurant {
    private boolean foodReady = false;

    public synchronized void prepareFood() {
        System.out.println("Chef: Preparing food...");
        try {
            Thread.sleep(2000);
        }catch(Exception e){
            System.out.println("Error in Prepare Food");
        }
        
        foodReady = true;
        System.out.println("Chef: Food is ready!");
        notify();
    }

    public synchronized void serveFood() {
        while(!foodReady){
            System.out.println("Waiter: Waiting for food...");
            try {
                wait();
            }catch(Exception e){
                System.out.println("Error in servefood");
            }
            System.out.println("Waiter: Serving food :)");
        }
    }
}

class Chef extends Thread {
    Restaurant resto;

    Chef(Restaurant resto){
        this.resto = resto;
    }

    public void run() {
        resto.prepareFood();
    }
}

class Waiter extends Thread {
    Restaurant resto;

    Waiter(Restaurant resto){
        this.resto = resto;
    }

    public void run(){
        resto.serveFood();
    }
}

public class Q3 {
    public static void main(String[] args) {
        Restaurant resto = new Restaurant();

        Waiter waiter = new Waiter(resto);
        Chef chef = new Chef(resto);

        waiter.start();
        chef.start();
    }
}
