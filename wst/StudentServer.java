import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;
import java.io.FileWriter;
import java.io.IOException;

public class StudentServer extends UnicastRemoteObject
        implements StudentClient {
    public StudentServer() throws RemoteException {
        super();
    }

    private void writeToLog(HashMap<String, String> request, 
                            HashMap<String, String> response) {
        try {
            FileWriter myWriter = new FileWriter("log.txt", true);
            myWriter.write("REQUEST: \r\n");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
            LocalDateTime now = LocalDateTime.now();  
            myWriter.write("Time: " + dtf.format(now) + "\r\n");
            for (String requestArg: request.keySet()) {
                myWriter.write(requestArg + ": " + request.get(requestArg) + "\r\n");
            }
            myWriter.write("\r\n");
            myWriter.write("RESPONSE:\r\n");
            for (String responseArg: response.keySet()) {
                myWriter.write(responseArg + ": " + response.get(responseArg) + "\r\n");
            }
            myWriter.write("\r\n");
            myWriter.write("***\r\n");
            myWriter.write("\r\n");
            myWriter.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public HashMap<String, String> bookRoom(String campusName,
                                            String roomNumber,
                                            String date,
                                            String timeslot,
                                            String studentID) {
        HashMap<String, String> request = new HashMap<String, String>();
        request.put("requestType", "Book Room");
        request.put("studentID", studentID);
        request.put("campusName", roomNumber);
        request.put("date", date);
        request.put("timeslot", timeslot);
        HashMap<String, String> response = Server.roomRecords.bookRoom(campusName,
                                                                       roomNumber,
                                                                       date,
                                                                       timeslot,
                                                                       studentID);
        writeToLog(request, response);
        return response;
    }

    public HashMap<String, String> cancelBooking(String bookingID, String studentID) {
        HashMap<String, String> request = new HashMap<String, String>();
        request.put("requestType", "Cancel Booking");
        request.put("studentID", studentID);
        request.put("bookingID", bookingID);
        HashMap<String, String> response = Server.roomRecords.cancelBooking(bookingID, studentID);
        writeToLog(request, response);
        return response;
    }
}