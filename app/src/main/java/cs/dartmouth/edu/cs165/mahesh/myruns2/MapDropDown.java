package cs.dartmouth.edu.cs165.mahesh.myruns2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MapDropDown extends Activity {
    @Override


    /*
        Dropdown functions for GPS and Automatic.
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
    }

    public void setSave(View v) {
        Toast.makeText(getApplicationContext(), "GPS as of now not implemented",
                Toast.LENGTH_SHORT).show();

        finish();
    }


    public void setCancel(View v) {
        Toast.makeText(getApplicationContext(), "Canceled.",
                Toast.LENGTH_SHORT).show();

        finish();
    }

}
