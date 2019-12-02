package Models;

import java.sql.Timestamp;
import java.util.Date;

public class Meeting {

    private static int count = 0;
    private int id;
    private int roomNumber;
    private String startTime;
    private String finishTime;
    private String createTime;
    private String setTime;

    public enum Status {
        STALLED  (0),  //calls constructor with value 3
        SET(1),  //calls constructor with value 2
        CANCELLED   (-1)   //calls constructor with value 1
        ; // semicolon needed when fields / methods follow


        private int levelCode;

        Status(int levelCode) {
            this.levelCode = levelCode;
        }

        public void setStatus(int levelCode) {
            this.levelCode = levelCode;
        }
        public int getLevelCode() {
            return this.levelCode;
        }


    }
    Status status;

    public Meeting(){
        this.id = count++;
    }
    public Status getStatus(){
        return status;
    }
    public void setStatus(int levelCode){
        this.status.setStatus(levelCode);
    }
    public void setId(int id) {
        this.id = id;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSetTime() {
        return setTime;
    }

    public void setSetTime(String setTime) {
        this.setTime = setTime;
    }

}



