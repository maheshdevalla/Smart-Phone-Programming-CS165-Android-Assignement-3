package cs.dartmouth.edu.cs165.mahesh.myruns2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.v4.view.ViewPager;


public class SettingsTab extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        setRetainInstance(true);
        /*
            * when settings tab(fragment) is generated
            * saving the results further.
         */
        PreferenceScreen preferencescreen = getPreferenceScreen();
        ListPreference listpreference = (ListPreference) preferencescreen.findPreference("Unit Preference");
        listpreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SharedPreferences sharedpreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("measure", newValue.toString());
                /*
                    * Sometimes it saving slowly insted of commit
                    *But it increases speed of app.
                  */
                editor.apply();
                ViewPager viewpager = (ViewPager)getActivity().findViewById(R.id.viewpager);
                Tabs tabs = (Tabs)viewpager.getAdapter();
                HistoryTab history_tab = (HistoryTab)tabs.getItem(1);
//                setRetainInstance(true);
                history_tab.reLoadData();
                return true;
            }
        });
    }
}

