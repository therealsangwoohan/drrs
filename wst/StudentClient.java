import java.rmi.Remote;
import java.util.HashMap;

public interface StudentClient extends Remote {
    // Returns {statusOfOperation: "success", bookingID: "1234"}
    public HashMap<String, String> bookRoom(String campusName,
                                            String roomNumber,
                                            String date,
                                            String timeslot,
                                            String studentID) throws Exception;

    // Returns {statusOfOperation: "success"}
    public HashMap<String, String> cancelBooking(String bookingID, String studentID) throws Exception;
}