package hannahmeharg.example.housearrest;

import java.util.Date;
import java.util.UUID;

public class Task {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private String mRoommate;

    public Task() {
//        mId = UUID.randomUUID();
//        mDate = new Date();
//        mTitle="";
        this(UUID.randomUUID());
    }
    public Task(UUID id) {
        mId = id;
        mDate = new Date();
    }


    public UUID getId() {
        return mId;
    }
    public String getTitle() {
        return mTitle;
    }
    public void setTitle(String title) {
        mTitle = title;
    }
    public Date getDate() {
        return mDate;
    }
    public void setDate(Date date) {
        mDate = date;
    }
    public boolean isSolved() {
        return mSolved;
    }
    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public String getRoommate(){
        return mRoommate;
    }

    public void setRoommate(String r){
        mRoommate = r;
    }
}
