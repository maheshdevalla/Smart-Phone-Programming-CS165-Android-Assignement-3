package cs.dartmouth.edu.cs165.mahesh.myruns2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

public class InfoActivity extends Activity {

    private EditText type, date, eDuration, eDistance, eCalories, eHeartRate;
    private String id;
    private MetaData metadata;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_layout);
        /*
            * Getting the values based upon the input in the History Tab.
            * Later setting the required parameters.
         */
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id = bundle.getString("ID");
        String activity = bundle.getString("ActivityType");
        String datetime = bundle.getString("DateTime");
        Double duration = bundle.getDouble("Duration");
        Double distance = bundle.getDouble("Distance");
        int calories = bundle.getInt("Calories");
        int heartrate = bundle.getInt("HeartRate");
        type = (EditText) findViewById(R.id.info_type);
        type.setText(activity);
        date = (EditText) findViewById(R.id.info_datetime);
        date.setText(datetime);
        eDuration = (EditText) findViewById(R.id.info_duration);
        int minute = duration.intValue();
        int second = (int)((duration - minute) * 60);
        if (minute != 0)
            eDuration.setText(minute + "mins " + second + "secs");
        else
            eDuration.setText(second + "secs");
        String str = bundle.getString("method");
        eDistance = (EditText) findViewById(R.id.info_distance);
        int temp_dis = distance.intValue();
        int temp_km = (int)(distance * 1.61);
        if (str.equals("Imperial (Miles)")) {
            if (temp_dis == distance)
                eDistance.setText(temp_dis + " Miles");
            else
                eDistance.setText(distance + " Miles");
        }
        else {
            if (temp_km == distance * 1.61)
                eDistance.setText(temp_km + " Kilometers");
            else
                eDistance.setText((distance * 1.61) + " Kilometers");
        }
        eCalories = (EditText) findViewById(R.id.info_calories);
        eCalories.setText(calories + " cals");

        eHeartRate = (EditText) findViewById(R.id.info_heartrate);
        eHeartRate.setText(heartrate + " bpm");

        metadata = new MetaData(getApplicationContext());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                myThread thread = new myThread(id);
                thread.start();
                finish();
                return true;
        }
        return false;
    }

/*
    * Deleting the thread to delete the records in the history tab.
 */
    class myThread extends Thread {

        private String ID;
        public myThread(String ID) {
            this.ID = ID;
        }

        @Override
        public void run() {
            super.run();
            metadata.deleteRow(ID);
        }
    }
}
