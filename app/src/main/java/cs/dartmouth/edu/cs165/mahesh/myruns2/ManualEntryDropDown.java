package cs.dartmouth.edu.cs165.mahesh.myruns2;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;


public class ManualEntryDropDown extends ListActivity {
    static final String[] dropdown = new String[]{"Date", "Time", "Duration", "Distance", "Calories", "Heart Rate", "Comment"};
    Calendar calendar = Calendar.getInstance();
    public DatabaseList metadata;
    private MetaData database;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        metadata = new DatabaseList();
        setContentView(R.layout.manualentry_layout);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dropdown);
        setListAdapter(adapter);
        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String get_data = ((TextView) view).getText().toString();
                switch (get_data) {
                    case "Date":
                        onDateClicked();
                        break;
                    case "Time":
                        onTimeClicked();
                        break;
                    default:
                        getDialogs(get_data);
                }
            }
        };

        database = new MetaData(getApplicationContext());
        Intent intent = getIntent();
        metadata.setmActivityType(intent.getStringExtra("ActivityType"));
        metadata.setmInputType(intent.getStringExtra("InputType"));
        ListView listview = getListView();
        listview.setOnItemClickListener(listener);
    }


    /*
    Date button functions
    Reference take from
    https://developer.android.com/reference/android/app/DatePickerDialog.OnDateSetListener.html
     */
    private void onDateClicked() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
            }
        };

        new DatePickerDialog(ManualEntryDropDown.this, listener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    /*
        Time button functions
        Reference taken from
        https://developer.android.com/reference/android/app/TimePickerDialog.OnTimeSetListener.html
     */
    private void onTimeClicked() {

        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND,0);
            }
        };

        new TimePickerDialog(ManualEntryDropDown.this, listener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), true).show();
    }

    /**
        Dialog to display the infromation required when clicked.
     */
    private void getDialogs(String str) {
        HistoryDialogFragment dialog = new HistoryDialogFragment();
        DialogFragment fragment = dialog.newInstance(str,metadata);
        fragment.show(getFragmentManager(), "dialog");
    }


    /*
        *
        * Method to save the data provided.
     */
    public void setSave(View view) {
        metadata.setmDate(calendar.get(Calendar.YEAR) +"-"+ (calendar.get(Calendar.MONTH)+1) +"-"+ calendar.get(Calendar.DAY_OF_MONTH));
        metadata.setmTime(calendar.get(Calendar.HOUR_OF_DAY) +":"+ calendar.get(Calendar.MINUTE) +":"+
                (calendar.get(Calendar.SECOND) == 0 ? "00" : calendar.get(Calendar.SECOND)));
        metadata.setId(System.currentTimeMillis()+"-"+metadata.getmInputType()+"-"+metadata.getmActivityType());
        new MyAsyncTask(metadata).execute();
        Toast.makeText(getApplicationContext(), "Entry saved.",
                Toast.LENGTH_SHORT).show();
        finish();
    }

    //Cancel method to cancel the about to saved data.
    public void setCancel(View v) {
        Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();

        finish();
    }

    /*
     *  Async class to handle the Data in future also.
     */
    class MyAsyncTask extends AsyncTask<Void, Void, Void> {
        private DatabaseList metadata;

        public MyAsyncTask(DatabaseList metadata) {
            this.metadata = metadata;
        }

        @Override
        protected Void doInBackground(Void... params) {
            database.addRow(metadata);
            return null;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Void result) {
        }

    }



}
