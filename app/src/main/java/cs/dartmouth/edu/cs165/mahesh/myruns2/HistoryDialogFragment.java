package cs.dartmouth.edu.cs165.mahesh.myruns2;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

public class HistoryDialogFragment extends DialogFragment{
    private DatabaseList metadata;

    public void onCancel() {
    }

    /*
     * saving the data to the database internally
     */
    public void onSave(String title, String data) throws NumberFormatException{
        int temp1 = 0;
        double temp2 = 0;
        if (title.equals("Comment")) ;
        else if (title.equals("Duration") || title.equals("Distance")) {
            if (data.isEmpty())
            {
                temp2 = 0.0;
            }
            else
                temp2 = Double.parseDouble(data);
        }
        else if (data.isEmpty())
        {
            temp1 = 0;
        }
        else if(Integer.parseInt(data)> Integer.MAX_VALUE)
        {
            temp1=Integer.MAX_VALUE;
        }
        else
            temp1 = Integer.parseInt(data);
        switch (title) {
            case "Duration":
                metadata.setmDuration(temp2);
                break;
            case "Distance":
                metadata.setmDistance(temp2);
                break;
            case "Calories":
                metadata.setmCalories(temp1);
                break;
            case "Heart Rate":
                metadata.setmHeartRate(temp1);
                break;
            case "Comment":
                metadata.setmComment(data);
        }
    }

    public HistoryDialogFragment newInstance(String str, DatabaseList metadata) {
        HistoryDialogFragment dialogfragment = new HistoryDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", str);
        dialogfragment.setArguments(bundle);
        dialogfragment.setDataItem(metadata);
        return dialogfragment;
    }

    public void setDataItem(DatabaseList metadata){
        this.metadata = metadata;
    }

    @Override
    /*
        *  References taken from
        *  http://stackoverflow.com/questions/24627757/edittext-setinputtypeinputtype-type-class-number-dont-work
        *  https://developer.android.com/reference/android/text/InputType.html
        *
    */
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        final String datatype = getArguments().getString("title");
        final EditText edittext = new EditText(getActivity());
        if(datatype.equals("Comment")) {
            edittext.setHint("How did it go? Notes here.");
            edittext.setHeight(400);
        }
        else if(datatype.equals("Duration") || datatype.equals("Distance"))
            edittext.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        else
            edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
        switch (datatype){
            case "Duration":
                edittext.setText(metadata.getmDuration() < 0 ? "" : (metadata.getmDuration()>Double.MAX_VALUE? Double.MAX_VALUE:metadata.getmDuration())+"");
                break;
            case "Distance":
                edittext.setText(metadata.getmDistance() < 0 ? "" : (metadata.getmDistance()>Double.MAX_VALUE?Double.MAX_VALUE:metadata.getmDistance())+"");
                break;
            case "Calories":
//                if (metadata.getmCalories() < 0 )
//                {
//                    edittext.setText(String.valueOf(0));
//                }
//                else{
//                    edittext.setText(metadata.getmCalories()+"");
//                }
                edittext.setText(metadata.getmCalories() < 0 ? "" :
                        (metadata.getmCalories()> Integer.MAX_VALUE ? Integer.MAX_VALUE:metadata.getmCalories())+"");
                break;
            case "Heart Rate":
                edittext.setText(metadata.getmHeartRate() < 0 ? "" :
                        (metadata.getmHeartRate()> Integer.MAX_VALUE ? Integer.MAX_VALUE:metadata.getmHeartRate())+"");
                break;
            case "Comment":
                edittext.setText(metadata.getmComment()+"");
        }

        return new AlertDialog.Builder(getActivity())
                .setTitle(datatype)
                .setView(edittext)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                onSave(datatype, edittext.getText().toString());
                            }
                        })
                .setNegativeButton("CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                onCancel();
                            }
                        }).create();
    }

}
