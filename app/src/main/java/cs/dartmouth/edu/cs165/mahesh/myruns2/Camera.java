package cs.dartmouth.edu.cs165.mahesh.myruns2;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static cs.dartmouth.edu.cs165.mahesh.myruns2.R.id.btnChangePhoto;


public class Camera extends Activity {
    private Uri var_uri;
    public static final int gallery = 1;
    public static final int select_image = 0;
    private ImageView var_Image;
    private boolean bool_cam_action;
    private int crop = 1;
    private static final String var_uri_data = "uri_data";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myruns1_ui);
        grantAccess();
        var_Image = (ImageView) findViewById(R.id.imageProfile);
        if (savedInstanceState != null) {
            var_uri = savedInstanceState.getParcelable(var_uri_data);
            var_Image.setImageURI(var_uri);
            if (var_uri == null) {
                loadSnap();
                loadProfile();
            }
        } else {
            loadSnap();
            loadProfile();
        }
    }

    //This method cancels the save edits and closes the app if cancel button is used.
    public void onCancelClicked(View v) {
        Toast.makeText(getApplicationContext(), "Cancelled",
                Toast.LENGTH_SHORT).show();
        finish();
    }

    //This method infaltes the menu, if this method is not provided, then according to the style used the action bar doesn't appear.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * This is generic code available at https://developer.android.com/training/camera/photobasics.html to grant permission for access
     */
    private void grantAccess() {
        if (Build.VERSION.SDK_INT < 23)
            return;

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 0);
        }
    }

    @Override
    //This code has been taken from xd's camera app, as permission access in generic in any android app,so I used this code.
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
        } else if (grantResults[0] == PackageManager.PERMISSION_DENIED || grantResults[1] == PackageManager.PERMISSION_DENIED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) ||
                        shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Show an explanation to the user *asynchronously*
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("This permission is important for the app.")
                            .setTitle("Important permission required");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.CAMERA}, 0);
                            }
                        }
                    });
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 0);
                } else {

                    denyPermission();

                }
            }
        }
    }

    public void denyPermission() {
//        displayDialog(Dialog.dialog_hint);
        Button button = (Button) findViewById(btnChangePhoto);
        button.setClickable(false);
        Toast.makeText(getApplicationContext(), "Profile Image cannot be changed as you denied permission " +
                "for camera, reset permission in the settings to change image", Toast.LENGTH_LONG).show();
    }


    @Override
    /*
     To save the data before minimizing the application.
        https://developer.android.com/guide/topics/data/data-storage.html
        http://www.androidhive.info/2012/08/android-session-management-using-shared-preferences/
      */
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putParcelable(var_uri_data, var_uri);
    }

    public void onSaveClicked(View v) throws FileNotFoundException {
        saveSnap();
        saveProfile();
        finish();
    }

    public void onChangePhotoClicked(View v) {
        displayDialog(Dialog.dialog_hint);
    }

    /** get the real path from uri */
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = new String[] { android.provider.MediaStore.Images.ImageColumns.DATA };

        Cursor cursor = getContentResolver().query(contentUri, proj, null,
                null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        String filename = cursor.getString(column_index);
        cursor.close();
        return filename;
    }
    /*
        Reference taken from http://stackoverflow.com/questions/920306/sending-data-back-to-the-main-activity-in-android
        https://developer.android.com/reference/android/app/Activity.html
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case select_image:
                // Send image taken from camera for cropping
                beginCrop(var_uri);
                break;
            //select image from gallery case:
            case gallery:
                beginCrop(data.getData());
                break;
            case Crop.REQUEST_CROP:
                // Handling the crop from the third party library
                handleCrop(resultCode, data);
                // Delete temporary image taken by camera after crop.
                if (bool_cam_action) {
                    File f = new File(var_uri.getPath());
                    if (f.exists())
                        f.delete();
                }
                break;
        }
    }

    /*
      The below code is taken from xd's camera app, which is a common functionality and also referenced from
      http://stackoverflow.com/questions/10165302/dialog-to-pick-image-from-gallery-or-from-camera
   */
    public void displayDialog(int id) {
        DialogFragment fragment = Dialog.newInstance(id);
        fragment.show(getFragmentManager(),getString(R.string.dialog_fragment_tag_photo_picker));
    }

    // Reference taken from http://stackoverflow.com/questions/10165302/dialog-to-pick-image-from-gallery-or-from-camera and xd's code
    public void onPhotoPickerItemSelected(int item) {
        Intent intent;
        switch (item) {
            case Dialog.camera_hint:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                ContentValues values = new ContentValues(1);
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
                var_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,var_uri);
                intent.putExtra("return-data", true);
                try {
                    startActivityForResult(intent, select_image);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                bool_cam_action = true;
                break;
            case Dialog.gallery_hint:
                intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, gallery);
                break;
            default:
        }

    }


    private void loadSnap() {
        try {
            FileInputStream fis = openFileInput(getString(R.string.profile_photo_file_name));
            Bitmap bmap = BitmapFactory.decodeStream(fis);
            var_Image.setImageBitmap(bmap);
            fis.close();
        } catch (IOException e) {
            // Default myruns1_ui photo if no photo saved before.
            var_Image.setImageResource(R.drawable.myruns2);
        }
    }

    private void saveSnap() throws FileNotFoundException {
        // Commit all the changes into preferences file
        // Save myruns1_ui image into internal storage.
        var_Image.buildDrawingCache();
        Bitmap bmap = var_Image.getDrawingCache();
        try {
            //Mode give as private inorder to not access to other apps as security concern.
            FileOutputStream fos = openFileOutput(getString(R.string.profile_photo_file_name), MODE_PRIVATE);
            bmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            // Clear the existing data if any.
            fos.flush();
            //closing the file stream.
            fos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    // Crop function used to crop photo(A third part library).
    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        crop ^= 1;
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            var_Image.setImageURI(Crop.getOutput(result));
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Load the profile when app is loaded.
    private void loadProfile() {
        String load_key = getString(R.string.shared_pref);
        SharedPreferences shar_pref = getSharedPreferences(load_key, MODE_PRIVATE);

        load_key = getString(R.string.shared_pref_name);
        String load_value = shar_pref.getString(load_key, " ");
        if (load_value != " ")
            ((EditText) findViewById(R.id.name)).setText(load_value);

        load_key = getString(R.string.shared_pref_email);
        load_value = shar_pref.getString(load_key, " ");
        if (load_value != " ")
            ((EditText) findViewById(R.id.email)).setText(load_value);

        load_key = getString(R.string.shared_pref_phone);
        load_value = shar_pref.getString(load_key, " ");
        if (load_value != " ")
            ((EditText) findViewById(R.id.phone)).setText(load_value);

        load_key = getString(R.string.shared_pref_class);
        load_value = shar_pref.getString(load_key, " ");
        if (load_value != " ")
            ((EditText) findViewById(R.id.prof_class)).setText(load_value);

        load_key = getString(R.string.shared_pref_major);
        load_value = shar_pref.getString(load_key, " ");
        if (load_value != " ")
            ((EditText) findViewById(R.id.major)).setText(load_value);

        load_key = getString(R.string.shared_pref_gender);
        if ((shar_pref.getInt(load_key, -1)) >= 0) {
            RadioButton btn_radio = (RadioButton) ((RadioGroup) findViewById(R.id.gendergroup)).getChildAt(shar_pref.getInt(load_key, -1));
            btn_radio.setChecked(true);
        }
    }

    //save the form data in the app
    private void saveProfile() {
        // Getting the shared preferences editor
        String save_key = getString(R.string.shared_pref);
        SharedPreferences shar_pref = getSharedPreferences(save_key, MODE_PRIVATE);

        SharedPreferences.Editor editor = shar_pref.edit();
        editor.clear();

        //Adding Name to the Application
        save_key = getString(R.string.shared_pref_name);
        String mNameValue = (String) ((EditText) findViewById(R.id.name)).getText().toString();
        editor.putString(save_key, mNameValue);

        //Adding Name to the Application
        save_key = getString(R.string.shared_pref_email);
        String save_value_email = (String) ((EditText) findViewById(R.id.email)).getText().toString();
        editor.putString(save_key, save_value_email);

        //Adding phone number to the Application
        save_key = getString(R.string.shared_pref_phone);
        String save_value_phone = (String) ((EditText) findViewById(R.id.phone)).getText().toString();
        editor.putString(save_key, save_value_phone);

        //Adding gender to the Application
        save_key = getString(R.string.shared_pref_gender);
        RadioGroup save_btn_radio = (RadioGroup) findViewById(R.id.gendergroup);
        int temp = save_btn_radio.indexOfChild(findViewById(save_btn_radio.getCheckedRadioButtonId()));
        editor.putInt(save_key, temp);

        //Adding class to the Application
        save_key = getString(R.string.shared_pref_class);
        String save_value_class = (String) ((EditText) findViewById(R.id.prof_class)).getText().toString();
        editor.putString(save_key, save_value_class);

        //Adding Major to the Application
        save_key = getString(R.string.shared_pref_major);
        String save_value_major = (String) ((EditText) findViewById(R.id.major)).getText().toString();
        editor.putString(save_key, save_value_major);

        // Commit data to store in shaared preferences
        editor.apply();
        Toast.makeText(getApplicationContext(), "Saved Profile Of: " + mNameValue, Toast.LENGTH_SHORT).show();
    }
}


