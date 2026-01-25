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
Write a Java program to simulate an Airport Runway System where:

- Only one runway is available.
- Multiple Plane threads request landing.
- If runway is busy → plane must wait.
- After landing, runway becomes free and next plane is notified.
- Use enum PlaneType to calculate landing fees.
- Use Wrapper classes for fee calculation.
- Use synchronization + wait() + notifyAll()
*/

enum PlaneType {
    PASSENGER(5000),
    CARGO(8000),
    PRIVATE(3000);

    private int landingFee;

    PlaneType(int fee){
        this.landingFee = fee;
    }

    public int getLandingFee() {
        return landingFee;
    }
}

class Runway {
    private boolean isOccupied = false;

    public synchronized void requestLanding(String planeName, PlaneType type){
        while(isOccupied){
            System.out.println(planeName + ": Runway busy, circling in air....");
            try {
                wait();
            }catch (Exception e){
                System.out.println("Error in requestLanding");
            }
        }
        
        isOccupied = true;

        Integer fee = type.getLandingFee();

        System.out.println(planeName + " is landing. Type: " + type);
        System.out.println("Landing fee: $"+ fee);

        try {
            Thread.sleep(2000);
        }catch(Exception e){
            System.out.println("Error while landing");
        }

        System.out.println(planeName + " has landed successfully.\n");
        isOccupied = false;
        notifyAll();
    }
}

class Plane extends Thread {
    Runway runway;
    String name;
    PlaneType type;

    Plane(Runway runway, String name, PlaneType type){
        this.runway = runway;
        this.name = name;
        this.type = type;
    }

    public void run() {
        runway.requestLanding(name, type);
    }
}

public class Q7 {
    public static void main(String[] args) {
        Runway runway = new Runway();

        Plane p1 = new Plane(runway, "IGO687", PlaneType.PASSENGER);
        Plane p2 = new Plane(runway, "UAL82", PlaneType.PASSENGER);
        Plane p3 = new Plane(runway, "VT-ANB", PlaneType.PRIVATE);

        p1.start();
        p2.start();
        p3.start();
    }
}
