import java.rmi.Remote;
import java.util.HashMap;

public interface AdminClient extends Remote {
    // Returns {statusOfOperation: "success"}, 
    public HashMap<String, String> createRoom(String roomNumber,
                                              String date,
                                              String timeSlotsString) throws Exception;

    // Returns {statusOfOperation: "success"}, 
    public HashMap<String, String> deleteRoom(String roomNumber,
                                              String date,
                                              String timeSlotsString) throws Exception;
}