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
import com.d8threaper.travelco.CustomListView.model.Companion;
import com.d8threaper.travelco.CustomListView.model.Ride;
import com.d8threaper.travelco.CustomListView.model.Stay;
import com.d8threaper.travelco.R;
import com.d8threaper.travelco.app.AppController;

import java.util.List;

/**
 * Created by root on 13/9/15.
 */
public class CustomCompanionListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Companion> companionItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomCompanionListAdapter(Activity activity, List<Companion> companionItems) {
        this.activity = activity;
        this.companionItems = companionItems;
    }

    @Override
    public int getCount() {
        return companionItems.size();
    }

    @Override
    public Object getItem(int position) {
        return companionItems.get(position);
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
            convertView = inflater.inflate(R.layout.list_row_companion, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbnail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnailCompanion);
        TextView Name = (TextView) convertView.findViewById(R.id.companionName);
        TextView Duration = (TextView) convertView.findViewById(R.id.companionDurationText);

        // getting stay data for the row
        Companion m = companionItems.get(position);

        // thumbnail image
        thumbnail.setImageUrl(m.getThumbnailUrl(), imageLoader);

        // Name
        Name.setText(m.getName());

        // Duration
        Duration.setText("Rating: " + String.valueOf(m.getDuration()));

        return convertView;
    }

}
