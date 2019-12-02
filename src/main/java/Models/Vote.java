package Models;

public class Vote {
    private static int count = 0;
    private int id;
    private int uid;
    private int pid;
    private String chosenTime;

    public Vote(){
        this.id = count++;
    }

    public int getId(){
        return id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getChosenTime() {
        return chosenTime;
    }

    public void setChosenTime(String chosenTime) {
        this.chosenTime = chosenTime;
    }



}
