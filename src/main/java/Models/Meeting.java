package Models;

import java.util.Date;

public class Meeting {

    private static int count = 0;
    private int roomNumber;
    private int id;
    private String startTime;
    private String finishTime;

    public Meeting(){
        this.id = count++;
    }
    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getId() {
        return id;
    }


    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }


}



