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
Write a Java program to simulate a system where:

- There are two shared resources: Database and FileSystem
- Two user threads need both resources to complete their task
- Thread A locks Database first, then tries to lock FileSystem
- Thread B locks FileSystem first, then tries to lock Database
- Show that a deadlock occurs
- Then modify the program to prevent deadlock by using a consistent lock order
*/

// Key Trick is to lock the Resources in the same Order.

public class Q11 {
    public static void main(String[] args) {
        Object database = new Object();
        Object fileSystem = new Object();

        Thread userA = new Thread(() -> {
            synchronized (database) {
                System.out.println("UserA locked database");

                try { Thread.sleep(1000); } catch(Exception e) {}
                
                System.out.println("UserA waiting for fileSystem...");
                synchronized (fileSystem) {
                    System.err.println("UserA locked FileSystem");
                }
            }
        });

        Thread userB = new Thread(() -> {
            synchronized(database) {
                System.out.println("UserB locked Database");

                try { Thread.sleep(1000); } catch(Exception e) {}

                System.out.println("UserB waiting for FileSystem...");
                synchronized(fileSystem) {
                    System.out.println("UserB locked FileSystem");
                }
            }
        });

        userA.start();
        userB.start();
    }
}
