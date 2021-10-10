import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;
import java.io.FileWriter;
import java.io.IOException;

public class AdminServer extends UnicastRemoteObject
        implements AdminClient {
    public AdminServer() throws RemoteException {
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

    public HashMap<String, String> createRoom(String roomNumber,
                                              String date,
                                              String timeSlotsString) {
        HashMap<String, String> request = new HashMap<String, String>();
        request.put("requestType", "Create Room");
        request.put("roomNumber", roomNumber);
        request.put("date", date);
        request.put("timeSlotsString", timeSlotsString);
        HashMap<String, String> response = Server.roomRecords.createRoom(roomNumber,
                                                                         date,
                                                                         timeSlotsString);
        writeToLog(request, response);
        return response;
    }

    public HashMap<String, String> deleteRoom(String roomNumber,
                                              String date,
                                              String timeSlotsString) {
        HashMap<String, String> request = new HashMap<String, String>();
        request.put("requestType", "Delete Room");
        request.put("roomNumber", roomNumber);
        request.put("date", date);
        request.put("timeSlotsString", timeSlotsString);
        HashMap<String, String> response = Server.roomRecords.deleteRoom(roomNumber,
                                                                         date,
                                                                         timeSlotsString);
        writeToLog(request, response);
        return response;
    }
}