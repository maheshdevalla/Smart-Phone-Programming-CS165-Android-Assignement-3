package cs.dartmouth.edu.cs165.mahesh.myruns2;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

public class MainActivity extends Activity {
    public ArrayList<Fragment> fragment;
    public Tabs tabs;
    public SlidingTabLayout slidingtablayout;
    public ViewPager viewpager;


	@Override
    //Initial fragment screen to save and sync
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
        /*
            * Sliding tab layout just for the UI
            * and the bar below the tabs
          */

        slidingtablayout = (SlidingTabLayout) findViewById(R.id.startmaintab);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        fragment = new ArrayList<>();

        /*
            Creating a fragment list.
         */
        fragment.add(new MainFragment());
        fragment.add(new HistoryTab());
        fragment.add(new SettingsTab());
        this.tabs =new Tabs(getFragmentManager(), fragment);
        viewpager.setAdapter(tabs);
        /*
               *References taken from
               * http://stackoverflow.com/questions/29847695/cannot-reslove-setdistributeevenly-in-fragment
               * http://stackoverflow.com/questions/27033292/align-center-slidingtablayout
               *
         */
        slidingtablayout.setDistributeEvenly(true);
        slidingtablayout.setViewPager(viewpager);
	}
}