package cs.dartmouth.edu.cs165.mahesh.myruns2;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class Dialog extends DialogFragment {
    public static final int dialog_hint = 1;
    public static final int camera_hint = 0;
    public static final String var_dialog = "dialog";
    public static final int gallery_hint = 1;

    public static Dialog newInstance(int dialog_id) {
        Dialog dialog = new Dialog();
        Bundle bundle = new Bundle();
        bundle.putInt(var_dialog, dialog_id);
        dialog.setArguments(bundle);
        return dialog;
    }
    @Override
    // Dialog to hint to open the camera app and save the data.
    // Reference taken from xd's app and http://stackoverflow.com/questions/4505845/concise-way-of-writing-new-dialogpreference-classes
    // Ref: http://developer.android.com/reference/android/app/DialogFragment.html
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        switch (getArguments().getInt(var_dialog)) {
            case dialog_hint:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.iv_hint_select_image);
                DialogInterface.OnClickListener dlistener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        ((Camera) getActivity()).onPhotoPickerItemSelected(item);
                    }
                };
                builder.setItems(R.array.pick_image_hint, dlistener);
                return builder.create();
            default:
                return null;
        }
    }
}
