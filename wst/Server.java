import java.rmi.registry.LocateRegistry;
import java.rmi.Naming;
import java.io.PrintWriter;

public class Server {
    public static RoomRecords roomRecords = new RoomRecords();

    public static void main(String args[]) throws Exception {
        new PrintWriter("log.txt").close();
        LocateRegistry.createRegistry(5003);
        AdminServer adminServerStub = new AdminServer();
        Naming.rebind("rmi://localhost:5003/adminserver", 
                      adminServerStub);
        StudentServer studentServerStub = new StudentServer();
        Naming.rebind("rmi://localhost:5003/studentserver", 
                      studentServerStub);
        System.out.println("Server started.");
    }
}
