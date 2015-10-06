package com.d8threaper.travelco.CustomListView;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.d8threaper.travelco.CustomListView.model.Ride;
import com.d8threaper.travelco.CustomListView.model.Stay;
import com.d8threaper.travelco.R;
import com.d8threaper.travelco.app.AppController;

import java.util.List;

/**
 * Created by root on 13/9/15.
 */
public class CustomRideListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Ride> rideItems;
    private String TAG = "Ride Adapter";
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomRideListAdapter(Activity activity, List<Ride> rideItems) {
        this.activity = activity;
        this.rideItems = rideItems;
    }

    @Override
    public int getCount() {
        return rideItems.size();
    }

    @Override
    public Object getItem(int position) {
        return rideItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row_rides, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        TextView sourceText = (TextView) convertView.findViewById(R.id.sourceText);
        TextView sourceTime = (TextView) convertView.findViewById(R.id.sourceTime);
        TextView destTime = (TextView) convertView.findViewById(R.id.destTime);
        TextView destText = (TextView) convertView.findViewById(R.id.destText);
        TextView vacancyText = (TextView) convertView.findViewById(R.id.vacancyText);
        TextView costText = (TextView) convertView.findViewById(R.id.costText);

        // getting ride data for the row
        Ride m = rideItems.get(position);

        Log.d(TAG,"Setting source " + m.getSource() + " " + position + " " + getCount());

        // source Text
        sourceText.setText(m.getSource());

        // source Time
        sourceTime.setText(m.getSourceTime());

        // destination Text
        destText.setText(m.getDestination());

        // destination Time
        destTime.setText(m.getDestinationTime());

        // vacancy
        vacancyText.setText(m.getVacancy());

        // cost
        costText.setText(m.getFinalCost());

        return convertView;
    }
}
