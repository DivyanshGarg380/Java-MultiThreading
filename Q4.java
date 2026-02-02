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
Design a Java program for an Online Shopping System where:

- A customer places an order
- Payment is processed only after the order is placed
-Use:
  - OOP concepts
  - Wrapper classes for price calculation
  - Threads
  - wait() and notify() for coordination
*/

class Product {
    String name;
    Integer price;
    Product(String name, Integer price){
        this.name = name;
        this.price = price;
    }
}

class Order {
    private Product product;
    private Integer quantity;
    private boolean orderPlaced = false;

    public synchronized void placeOrder(Product product, Integer quantity){
        System.out.println("OrderThread: Placing order....");

        this.product = product;
        this.quantity = quantity;

        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println("Error in PlaceOrder");
        }

        orderPlaced = true;
        System.out.println("OrderThread: Order placed for " + quantity + " " + product.name);
        notify();
    }

    public synchronized void makePayment(){
        while(!orderPlaced){
            System.out.println("PaymentThread: Waiting for order....");
            try {
                wait();
            }catch(Exception e){
                System.out.println("Error in makePayment");
            }

            Integer totalAmount = product.price * quantity;
            System.out.println("PaymentThread: Processing payment of $" + totalAmount);
        }
    }
}

class OrderThread extends Thread {
    Order order;
    Product product;
    Integer quantity;

    OrderThread(Order order, Product product, Integer quantity){
        this.order = order;
        this.product = product;
        this.quantity = quantity;
    }

    public void run() {
        order.placeOrder(product, quantity);
    }
}

class PaymentThread extends Thread {
    Order order;

    PaymentThread(Order order){
        this.order = order;
    }

    public void run() {
        order.makePayment();
    }
}

public class Q4 {
    public static void main(String[] args) {
        Product p = new Product("Airbus A350", 50000000);
        Order order = new Order();

        OrderThread ot = new OrderThread(order, p, 2);
        PaymentThread pt = new PaymentThread(order);

        pt.start(); 
        ot.start(); 
    }    
}
