import java.util.HashMap;
import java.util.Scanner;
import java.rmi.Naming;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;    

public class Client {
    
    public static String validateID(String id) {
        if (id.length() != 8 || 
            (!id.substring(0, 3).equals("DVL") && 
             !id.substring(0, 3).equals("KKL") &&
             !id.substring(0, 3).equals("WST")) ||
            (!id.substring(3, 4).equals("S") && !id.substring(3, 4).equals("A")) ||
            !isNumeric(id.substring(4, 8))
           ) {
            return "invalid";
        } else if (id.substring(3, 4).equals("S")) {
            return "student";
        } else {
            return "administrator";
        }
    }

    public static boolean isNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
    
    public static String getStubURL(String id) {
        String stubURL = "";
        
        String campusInitial = id.substring(0, 3);
        if (campusInitial.equals("DVL")) {
            stubURL += "rmi://localhost:5001/";
        } else if (campusInitial.equals("KKL")) {
            stubURL += "rmi://localhost:5002/";
        } else if (campusInitial.equals("WST")) {
            stubURL += "rmi://localhost:5003/";
        }
        
        String userType = id.substring(3, 4);
        if (userType.equals("S")) {
            stubURL += "studentserver";
        } else if (userType.equals("A")) {
            stubURL += "adminserver";
        }
        
        return stubURL;
    }

    public static void writeToLog(String id, 
                                  HashMap<String, String> request, 
                                  HashMap<String, String> response) {
        try {
            FileWriter myWriter = new FileWriter("./logs/log_" + id + ".txt", true);
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
    
    public static void studentOperations(String studentID,
                                         Scanner userInput) {
        System.out.println("What would you like to do?:");
        System.out.println("    1) Book a room.");
        System.out.println("    2) Cancel booking.");
        String choice = userInput.nextLine();
        if (choice.equals("1")) {
            studentStubBookRoom(studentID, userInput);
        } else if (choice.equals("2")) {
            studentStubCancelBooking(studentID, userInput);                           
        }
    }
    
    public static void studentStubBookRoom(String studentID,
                                           Scanner userInput) {
        HashMap<String, String> request = new HashMap<>();
        request.put("requestType", "Book Room");
        System.out.println("Enter campus name:");
        request.put("campusName", userInput.nextLine());
        System.out.println("Enter room number:");
        request.put("roomNumber", userInput.nextLine());
        System.out.println("Enter date:");
        request.put("date", userInput.nextLine());
        System.out.println("Enter timeslot. For example, '19h-20h':");
        request.put("timeslot", userInput.nextLine());        
        try {
            String stubURL = getStubURL(studentID);
            StudentClient stub = (StudentClient) Naming.lookup(stubURL);
            HashMap<String, String> response = stub.bookRoom(request.get("campusName"),
                                                             request.get("roomNumber"),
                                                             request.get("date"),
                                                             request.get("timeslot"),
                                                             studentID);
            writeToLog(studentID, request, response);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public static void studentStubCancelBooking(String studentID, 
                                                Scanner userInput) {
        HashMap<String, String> request = new HashMap<>();
        request.put("requestType", "Cancel Booking");
        System.out.println("Enter booking ID:");
        request.put("bookingID", userInput.nextLine());
        request.put("studentID", studentID);
        
        try {
            String stubURL = getStubURL(studentID);
            StudentClient stub = (StudentClient) Naming.lookup(stubURL);
            HashMap<String, String> response = stub.cancelBooking(request.get("bookingID"),
                                                                  request.get("studentID"));
            writeToLog(studentID, request, response);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public static void administratorOperations(String adminID,
                                               Scanner userInput) {
        System.out.println("What would you like to do?:");
        System.out.println("    1) Create a room.");
        System.out.println("    2) Delete a room.");
        String choice = userInput.nextLine();
        if (choice.equals("1")) {
            administratorStubCreateRoom(adminID, userInput);                            
        } else if (choice.equals("2")) {
            administratorStubDeleteRoom(adminID, userInput);
        }
    }

    public static void administratorStubCreateRoom(String adminID, 
                                                   Scanner userInput) {
        HashMap<String, String> request = new HashMap<>();
        request.put("requestType", "Create Room");
        System.out.println("Enter room number:");
        request.put("roomNumber", userInput.nextLine());
        System.out.println("Enter date. For example, '2021:10:05'");
        request.put("date", userInput.nextLine());
        System.out.println("Enter list of time slots. For example, '09h-10h,12h-13h':");
        request.put("timeSlotsString", userInput.nextLine());
        
        try {
            String stubURL = getStubURL(adminID);
            AdminClient stub = (AdminClient) Naming.lookup(stubURL);
            HashMap<String, String> response = stub.createRoom(request.get("roomNumber"), 
                                                               request.get("date"), 
                                                               request.get("timeSlotsString"));
            writeToLog(adminID, request, response);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public static void administratorStubDeleteRoom(String adminID, 
                                                   Scanner userInput) {
        HashMap<String, String> request = new HashMap<>();
        request.put("requestType", "Delete Room");
        System.out.println("Enter room number:");
        request.put("roomNumber", userInput.nextLine());
        System.out.println("Enter date. For example, '2021:10:05'");
        request.put("date", userInput.nextLine());
        System.out.println("Enter list of time slots. For example, '09h-10h,12h-13h':");
        request.put("timeSlotsString", userInput.nextLine());
        
        try {
            String stubURL = getStubURL(adminID);
            AdminClient stub = (AdminClient) Naming.lookup(stubURL);
            HashMap<String, String> response = stub.deleteRoom(request.get("roomNumber"), 
                                                               request.get("date"), 
                                                               request.get("timeSlotsString"));
            writeToLog(adminID, request, response);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public static void main(String[] args) {
        Scanner userInput = new Scanner(System.in);
        System.out.println("Enter your id. For example, 'DVLS1234':");
        String id = userInput.nextLine();

        String validationResponse = validateID(id);
        if (validationResponse.equals("invalid")) {
            System.out.println("Your id is invalid.");
            return;
        }

        try {
            File file = new File("./logs/log_" + id + ".txt");
            file.createNewFile();
            if (validationResponse.equals("student")) {
                do {
                    studentOperations(id, userInput);
                    System.out.println("Would you like to quit? (y/n): ");
                } while (!userInput.nextLine().equals("y"));
            } else {
                do {
                    administratorOperations(id, userInput);
                    System.out.println("Would you like to quit? (y/n): ");
                } while (!userInput.nextLine().equals("y"));
            }
            file.delete();
        } catch (IOException e) {
            System.out.println(e);
        }
        userInput.close();
    }
}
