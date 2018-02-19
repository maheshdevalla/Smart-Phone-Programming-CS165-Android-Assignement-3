package cs.dartmouth.edu.cs165.mahesh.myruns2;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;

/*
    Referecne taken from xd's code implemented in the class
 */

class Tabs extends FragmentPagerAdapter {

    private static final int start_tab_numbr = 0;
    private static final int history_tab_numbr = 1;
    private static final int settings_tab_numbr = 2;
    private static final String start_tab_name = "start";
    private static final String history_tab_name = "history";
    private static final String settings_tab_name = "settings";
    private ArrayList<Fragment> frag;

    public Tabs(FragmentManager fragment, ArrayList<Fragment> arraylistfragments){
        super(fragment);
        this.frag = arraylistfragments;
    }

    public Fragment getItem(int pos){
        return frag.get(pos);
    }

    public int getCount(){
        return frag.size();
    }

   public CharSequence getPageTitle(int position) {
        switch (position) {
            case start_tab_numbr:
                return start_tab_name;
            case history_tab_numbr:
                return history_tab_name;
            case settings_tab_numbr:
                return settings_tab_name;
            default:
                break;
        }
        return null;
    }
}

