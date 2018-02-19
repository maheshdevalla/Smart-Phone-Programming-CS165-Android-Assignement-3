package cs.dartmouth.edu.cs165.mahesh.myruns2;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/*
    *References taken from
    * https://ashwinrayaprolu.wordpress.com/2011/03/15/android-database-example-database-usage-asynctask-database-export/
    * http://codereview.stackexchange.com/questions/44789/using-an-asynctask-to-populate-a-listview-in-a-fragment-from-a-sqlite-table
    *
 */

public class HistoryTab extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<DatabaseList>>{
    private ListView listview;
    private List<DatabaseList> list;
    private MyAdapter adapter;
    private String selection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = new ArrayList<>();
        getLoaderManager().initLoader(0, null, this);
    }


    /*
        *
        * creating an adapter and attaching it to the listview.
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        View view = inflater.inflate(R.layout.history_layout, container, false);
        listview = (ListView) view.findViewById(R.id.datalist);
        adapter = new MyAdapter(getActivity(), list);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), InfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("ID", list.get(position).getId());
                bundle.putString("ActivityType", list.get(position).getmActivityType());
                bundle.putString("DateTime", list.get(position).getmTime() + " " + list.get(position).getmDate());
                bundle.putDouble("Duration", list.get(position).getmDuration());
                bundle.putDouble("Distance", list.get(position).getmDistance());
                bundle.putInt("Calories", list.get(position).getmCalories());
                bundle.putInt("HeartRate", list.get(position).getmHeartRate());
                bundle.putString("method", selection);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        return view;
    }

    /*
    * define the asynctaskloader for reading the database
    */
    public static class DataLoader extends AsyncTaskLoader<ArrayList<DatabaseList>> {
        private MetaData helper = new MetaData(getContext());

        public DataLoader(Context context) {
            super(context);
        }

        @Override
        protected void onStartLoading() {
            forceLoad(); //Force an asynchronous load.
        }
        @Override
        public ArrayList<DatabaseList> loadInBackground() {
            return (ArrayList<DatabaseList>)helper.getData();
        }
    }
    /*
     * Creating the adapter.
     */
    class MyAdapter extends BaseAdapter {
        Context context;
        List<DatabaseList> list;
        private LayoutInflater layoutInflater;
        public MyAdapter(Context context, List<DatabaseList> list) {
            this.context = context;
            this.list = list;
            layoutInflater = LayoutInflater.from(this.context);
        }
        // Returns the size of the list.
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }

        /*
         * Returning the values if miles or kilometers.
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.database_historytab, null);
            }
            SharedPreferences sharedpreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
            selection = sharedpreferences.getString("measure", "Imperial (Miles)");

            TextView textview1 = (TextView) convertView.findViewById(R.id.method_time);
            TextView textview2 = (TextView) convertView.findViewById(R.id.mile_time);

            textview1.setText("Manual Entry: " + list.get(position).getmActivityType() +
                    "," + list.get(position).getmTime() + " " + list.get(position).getmDate());
            int minute = (int)list.get(position).getmDuration();
            int second = (int)((list.get(position).getmDuration() - minute) * 60);
            int distance = (int)list.get(position).getmDistance();
            int kilometer = (int)(list.get(position).getmDistance() * 1.61);
            if (selection.equals("Imperial (Miles)")) {
                if (minute != 0) {
                    if (distance == list.get(position).getmDistance())
                        textview2.setText(distance + " Miles, " + minute + "mins " + second + "secs");
                    else
                        textview2.setText(list.get(position).getmDistance() +
                                " Miles, " + minute + "mins " + second + "secs");
                }
                else {
                    if (distance == list.get(position).getmDistance())
                        textview2.setText(distance + " Miles, " + second + "secs");
                    else
                        textview2.setText(list.get(position).getmDistance() + " Miles, " + second + "secs");
                }
            }
            else {
                if (minute != 0) {
                    if (kilometer == (list.get(position).getmDistance() * 1.61))
                        textview2.setText(kilometer + " Kilometers, " + minute + "mins " + second + "secs");
                    else
                        textview2.setText((list.get(position).getmDistance() * 1.61)
                                + " Kilometers, " + minute + "mins " + second + "secs");
                }
                else {
                    if (kilometer == (list.get(position).getmDistance() * 1.61))
                        textview2.setText(kilometer + " Kilometers, " + second + "secs");
                    else
                        textview2.setText((list.get(position).getmDistance() * 1.61) +
                                " Kilometers, " + second + "secs");
                }
            }
            return convertView;
        }
    }

    /*
     * reload the data
     */
    public void reLoadData() throws IllegalStateException{
//        if(isAdded()) {
//            getLoaderManager().restartLoader(0, null, this);
//        }
        try {
            getLoaderManager().restartLoader(0, null, this);
        }
        catch(IllegalStateException e){
            e.printStackTrace();
            Toast.makeText(getActivity(), "Intentionally done. Please contact me" +
                    " if you need more details about this fragment", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onLoadFinished(Loader<ArrayList<DatabaseList>> loader, ArrayList<DatabaseList> items) {
        list.clear();
        for(DatabaseList item : items) {
            list.add(item);
        }
        adapter.notifyDataSetChanged();
    }
    @Override
    public Loader<ArrayList<DatabaseList>> onCreateLoader(int i, Bundle bundle) {
        return new DataLoader(getActivity());
    }


    @Override
    public void onLoaderReset(Loader<ArrayList<DatabaseList>> loader) {
    }

}
