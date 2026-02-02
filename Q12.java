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
Write a Java program where:

- Three threads print numbers from 1 to 5
- Each thread has a different priority
- Use Thread.yield() inside the loop
- Display the thread name and priority during execution
- Observe and explain how thread scheduling behaves
*/

class NumberPrinter extends Thread {

    public NumberPrinter(String name, int priority) {
        super(name);
        setPriority(priority);
    }

    public void run() {
        for(int i = 1; i <= 5; i++){
            System.out.println("Thread " + getName() + " | Priority: " + getPriority() + " | Number: " + i);

            Thread.yield();

            try {
                Thread.sleep(200);
            } catch (Exception e) {
                System.out.println("Error in run function of NumberPrinter class");
            }
        }
    }
}

public class Q12 {
    public static void main(String[] args) {
        NumberPrinter t1 = new NumberPrinter("Low", Thread.MIN_PRIORITY);
        NumberPrinter t2 = new NumberPrinter("Medium", Thread.NORM_PRIORITY);
        NumberPrinter t3 = new NumberPrinter("High", Thread.MAX_PRIORITY);

        t1.start();
        t2.start();
        t3.start();
    }
}
