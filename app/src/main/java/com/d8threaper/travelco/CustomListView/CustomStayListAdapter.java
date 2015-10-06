package com.d8threaper.travelco.CustomListView;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.d8threaper.travelco.CustomListView.model.Stay;
import com.d8threaper.travelco.R;
import com.d8threaper.travelco.app.AppController;

import java.util.List;

/**
 * Created by root on 13/9/15.
 */
public class CustomStayListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Stay> stayItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomStayListAdapter(Activity activity, List<Stay> stayItems) {
        this.activity = activity;
        this.stayItems = stayItems;
    }

    @Override
    public int getCount() {
        return stayItems.size();
    }

    @Override
    public Object getItem(int position) {
        return stayItems.get(position);
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
            convertView = inflater.inflate(R.layout.list_row_stays, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbnail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);
        TextView Address = (TextView) convertView.findViewById(R.id.addressText);
        TextView Duration = (TextView) convertView.findViewById(R.id.durationText);
        TextView Cost = (TextView) convertView.findViewById(R.id.costStayText);

        // getting stay data for the row
        Stay m = stayItems.get(position);

        // thumbnail image
        thumbnail.setImageUrl(m.getThumbnailUrl(), imageLoader);

        // Address
        Address.setText(m.getAddress());

        // Duration
        Duration.setText(m.getDuration());

        // cost
        Cost.setText(m.getFinalCost());

        return convertView;
    }
}
