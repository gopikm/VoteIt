package bitterbug.hack.custom;



        import android.os.Parcel;
        import android.os.Parcelable;

        import java.io.Serializable;

/**
 * Created by User on 4/8/2016.
 */
public class ThreadModel implements Serializable {

    private  String threaddata="";
    private  int comments;
    private  int polls;
    private int id;


    public String getThreaddata() {
        return threaddata;
    }

    public void setThreaddata(String threaddata) {
        this.threaddata = threaddata;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getPolls() {
        return polls;
    }

    public void setPolls(int polls) {
        this.polls = polls;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
