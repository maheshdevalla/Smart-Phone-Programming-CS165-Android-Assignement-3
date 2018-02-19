package cs.dartmouth.edu.cs165.mahesh.myruns2;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/*
    * Reference taken from
    * https://thebhwgroup.com/blog/how-android-sqlite-onupgrade
    *
 */

public class MetaData extends SQLiteOpenHelper {
    private final static String dbname = "Profile";
    private final static int version = 1;
    private final String table = "ManualEntry";

    public MetaData(Context context){
        super(context,dbname,null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String dbsql = "create table if not exists " + table + " (ID text primary key,Date text,Time text,Duration real," +
                "Distance real, Calories integer, HeartRate integer, Comment text, InputType text, ActivityType text)";
        sqLiteDatabase.execSQL(dbsql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String dbsql = "DROP TABLE IF EXISTS " + table;
        sqLiteDatabase.execSQL(dbsql);
        onCreate(sqLiteDatabase);
    }

    /*
     * Inserting records into the database.
     */
    public void addRow(DatabaseList metadata){
        try {
            SQLiteDatabase dbsql = this.getWritableDatabase();
            String sqlAdd = "insert into " + table + " (ID, Date, Time, Duration, Distance, Calories, " +
                    "HeartRate, Comment, InputType, ActivityType) values ('" + (metadata.getId().equals("") ? "-1" : metadata.getId()) +"','"+
                    (metadata.getmDate().equals("") ? "0" : metadata.getmDate()) +"','"+
                    (metadata.getmTime().equals("") ? "0" : metadata.getmTime()) +"',"+
                    (metadata.getmDuration() < 0 ? 0 : metadata.getmDuration())  +","+
                    (metadata.getmDistance() < 0 ? 0 : metadata.getmDistance())  +","+
                    (metadata.getmCalories() < 0 ? 0 : metadata.getmCalories())  +","+
                    (metadata.getmHeartRate() < 0 ? 0 : metadata.getmHeartRate())+",'"+
                    (metadata.getmComment().equals("") ? "0" : metadata.getmComment()) + "','" +
                    (metadata.getmInputType().equals("") ? "0" : metadata.getmInputType()) +"','"+
                    (metadata.getmActivityType().equals("") ? "0" : metadata.getmActivityType()) +"')";
            dbsql.execSQL(sqlAdd);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*
     * Deleting records from the database.
     */
    public void deleteRow(String str){
        try{
            SQLiteDatabase dbsql = this.getWritableDatabase();
            dbsql.execSQL("delete from "+table+" where ID=?", new String[]{str});
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*
     * Getting the records from the database.
     */
    public List<DatabaseList> getData(){
        List<DatabaseList> rowdata = new ArrayList<>();
        try {
            SQLiteDatabase dbsql = this.getWritableDatabase();
            Cursor cursor = dbsql.rawQuery("select * from " + table + " order by ID ASC", new String[]{});
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                while(!cursor.isAfterLast()) {
                    DatabaseList thisItem = new DatabaseList(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3), cursor.getDouble(4),
                            cursor.getInt(5), cursor.getInt(6), cursor.getString(7), cursor.getString(8), cursor.getString(9));
                    rowdata.add(thisItem);
                    cursor.moveToNext();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return rowdata;
    }
}
