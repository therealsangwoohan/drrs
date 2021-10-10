import java.util.HashMap;

class RoomRecords {
    HashMap<String, HashMap<String, HashMap<String, String>>> date2RoomNumber2TimeSlots2StudentID;

    public RoomRecords() {
        date2RoomNumber2TimeSlots2StudentID = new HashMap<>();
    }

    public synchronized HashMap<String, String> createRoom(String roomNumber,
                                              String date,
                                              String timeSlotsString) {
        String[] timeSlots = timeSlotsString.split(",");

        // If date and roomNumber already exist...
        if (date2RoomNumber2TimeSlots2StudentID.containsKey(date) &&
            date2RoomNumber2TimeSlots2StudentID.get(date).containsKey(roomNumber)) {
            for (String timeSlot : timeSlots) {
                if (!date2RoomNumber2TimeSlots2StudentID.get(date).get(roomNumber).containsKey(timeSlot)) {
                    date2RoomNumber2TimeSlots2StudentID.get(date).get(roomNumber).put(timeSlot, null);
                }
            }
        // If date exists, but roomNumber doesn't exists...
        } else if (date2RoomNumber2TimeSlots2StudentID.containsKey(date) &&
                   !date2RoomNumber2TimeSlots2StudentID.get(date).containsKey(roomNumber)) {
            HashMap<String, String> timeSlots2StudentID = new HashMap<>();
            for (String timeSlot : timeSlots) {
                timeSlots2StudentID.put(timeSlot, null);
            }
            date2RoomNumber2TimeSlots2StudentID.get(date).put(roomNumber, timeSlots2StudentID);
        // If date and roomNumber don't exist...
        } else {
            HashMap<String, String> timeSlots2StudentID = new HashMap<>();
            for (String timeSlot : timeSlots) {
                timeSlots2StudentID.put(timeSlot, null);
            }
            HashMap<String, HashMap<String, String>> roomNumber2timeSlots2StudentID = new HashMap<>();
            roomNumber2timeSlots2StudentID.put(roomNumber, timeSlots2StudentID);
            date2RoomNumber2TimeSlots2StudentID.put(date, roomNumber2timeSlots2StudentID);
        }

        HashMap<String, String> response = new HashMap<>();
        response.put("statusOfOperation", "success");
        return response;
    }

    public synchronized HashMap<String, String> deleteRoom(String roomNumber,
                                              String date,
                                              String timeSlotsString) {
        String[] timeSlots = timeSlotsString.split(",");

        // if date and roomNumber exists, then delete all timeslots appearing in timeSlotsString.
        if (date2RoomNumber2TimeSlots2StudentID.containsKey(date) &&
            date2RoomNumber2TimeSlots2StudentID.get(date).containsKey(roomNumber)) {
            for (String timeSlot : timeSlots) {
                date2RoomNumber2TimeSlots2StudentID.get(date).get(roomNumber).remove(timeSlot);
            }
        }

        HashMap<String, String> response = new HashMap<>();
        response.put("statusOfOperation", "success");
        return response;
    }

    public synchronized HashMap<String, String> bookRoom(String campusName,
                                            String roomNumber,
                                            String date,
                                            String timeslot,
                                            String studentID) {
        HashMap<String, String> response = new HashMap<>();

        // If date exist and room exist and timeslot exist and timeslot unoccupied, then
        // add student id to timeslot and put the booking id to response.
        // Else, the statusOfOperation is a failure and return.
        if (date2RoomNumber2TimeSlots2StudentID.containsKey(date) &&
            date2RoomNumber2TimeSlots2StudentID.get(date).containsKey(roomNumber) &&
            date2RoomNumber2TimeSlots2StudentID.get(date).get(roomNumber).containsKey(timeslot) &&
            date2RoomNumber2TimeSlots2StudentID.get(date).get(roomNumber).get(timeslot) == null) {
            date2RoomNumber2TimeSlots2StudentID.get(date).get(roomNumber).put(timeslot, studentID);
            response.put("bookingID", campusName + "," + roomNumber + "," + date + "," + timeslot);
            response.put("statusOfOperation", "success");
            return response;
        }

        response.put("bookingID", null);
        response.put("statusOfOperation", "failure");
        return response;
    }

    public synchronized HashMap<String, String> cancelBooking(String bookingID, String studentID) {
        HashMap<String, String> response = new HashMap<>();

        String[] args = bookingID.split(",");
        String roomNumber = args[1];
        String date = args[2];
        String timeSlot = args[3];
        if (date2RoomNumber2TimeSlots2StudentID.containsKey(date) &&
            date2RoomNumber2TimeSlots2StudentID.get(date).containsKey(roomNumber) &&
            date2RoomNumber2TimeSlots2StudentID.get(date).get(roomNumber).containsKey(timeSlot) &&
            date2RoomNumber2TimeSlots2StudentID.get(date).get(roomNumber).get(timeSlot).equals(studentID)) {
            date2RoomNumber2TimeSlots2StudentID.get(date).get(roomNumber).put(timeSlot, null);
            response.put("statusOfOperation", "success");
            return response;
        }

        response.put("statusOfOperation", "failure");
        return response;
    }
}
