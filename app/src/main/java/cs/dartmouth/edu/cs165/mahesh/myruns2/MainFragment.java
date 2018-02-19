package cs.dartmouth.edu.cs165.mahesh.myruns2;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


public class MainFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.start, container, false);
        Button btn_start = (Button) view.findViewById(R.id.start_start);
        Button btn_sync = (Button) view.findViewById(R.id.start_sync);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onStart(v);
            }
        });
        btn_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onSync(v);
            }
        });
        return view;
    }

    /*
        * method to save the data to
        * the data when save button clicked.
        *
     */
    public void onStart(View v) {
        Spinner spinner = (Spinner)getActivity().findViewById(R.id.spinnerInputType);
        Spinner spinner_act = (Spinner)getActivity().findViewById(R.id.spinnerActivityType);
        String str = spinner.getSelectedItem().toString();
        String str_act = spinner_act.getSelectedItem().toString();
        Intent intent;
        switch(str) {
            case "Manual Entry":
                intent = new Intent(getActivity(),
                        ManualEntryDropDown.class);
                intent.putExtra("InputType", "ManualEntry");
                intent.putExtra("ActivityType", str_act);
                startActivity(intent);
                break;
            case "GPS":
                intent = new Intent(getActivity(),
                        MapDropDown.class);
                startActivity(intent);
                break;
            case "Automatic":
                intent = new Intent(getActivity(),
                        MapDropDown.class);
                startActivity(intent);
                break;
        }
    }

    /*
     *  Method to Sync the data.
     * */
    public void onSync(View v) {
        Toast.makeText(getActivity(), "Synchronization done", Toast.LENGTH_SHORT).show();
    }

}
