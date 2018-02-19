package cs.dartmouth.edu.cs165.mahesh.myruns2;

public class DatabaseList {
    private String id = "";
    /*
        *
        *  Date and other data modified slightly to avoid runtime exceptions
        *  like ClassCastExceptions, NullPointerExceptions
        *  and IllegalStateExceptions.
     */
    private String mDate = "";
    private String mTime = "";
    private double mDuration = -1;
    private double mDistance = -1;
    private int mCalories = -1;
    private int mHeartRate = -1;
    private String mComment = "";
    private String mInputType = "";
    private String mActivityType = "";

    /*
       * Parameter constructor to set data to the instance variables.
       *
     */
    public DatabaseList(String id, String mDate, String mTime, double mDuration, double mDistance, int mCalories,
                        int mHeartRate, String mComment, String mInputType, String mActivityType){
        this.id = id;
        this.mDate = mDate;
        this.mTime = mTime;
        this.mDuration = mDuration;
        this.mDistance = mDistance;
        this.mCalories = mCalories;
        this.mHeartRate = mHeartRate;
        this.mComment = mComment;
        this.mInputType = mInputType;
        this.mActivityType = mActivityType;
    }

    // Default constructor just may be for future use and also for object initilization.
    public DatabaseList(){}


    /*
        * Respective getters and setters methods for all the instance variables.s
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public double getmDuration() {
        return mDuration;
    }

    public void setmDuration(double mDuration) {
        this.mDuration = mDuration;
    }

    public double getmDistance() {
        return mDistance;
    }

    public void setmDistance(double mDistance) {
        this.mDistance = mDistance;
    }

    public int getmCalories() {
        return mCalories;
    }

    public void setmCalories(int mCalories) {
        this.mCalories = mCalories;
    }

    public int getmHeartRate() {
        return mHeartRate;
    }

    public void setmHeartRate(int mHeartRate) {
        this.mHeartRate = mHeartRate;
    }

    public String getmComment() {
        return mComment;
    }

    public void setmComment(String mComment) {
        this.mComment = mComment;
    }

    public String getmInputType() {
        return mInputType;
    }

    public void setmInputType(String mInputType) {
        this.mInputType = mInputType;
    }

    public String getmActivityType() {
        return mActivityType;
    }

    public void setmActivityType(String mActivityType) {
        this.mActivityType = mActivityType;
    }



}
