package com.d8threaper.travelco.tabs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.d8threaper.travelco.CustomListView.CustomStayListAdapter;
import com.d8threaper.travelco.CustomListView.model.Stay;
import com.d8threaper.travelco.FormActivity;
import com.d8threaper.travelco.R;
import com.d8threaper.travelco.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by root on 11/9/15.
 */
public class Tab2 extends Fragment {

    // Log tag
    private static final String TAG = "Stays Tab";

    // Stays json url
    private static final String url = "http://api.androidhive.info/json/movies.json";
    private ProgressDialog pDialog;
    private List<Stay> stayList = new ArrayList<Stay>();
    private ListView listView;
    private CustomStayListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab2,container,false);

        FloatingActionButton addStay = (FloatingActionButton) v.findViewById(R.id.addStay);
        addStay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), FormActivity.class);
                i.putExtra("form",2);
                startActivity(i);
            }
        });

        listView = (ListView) v.findViewById(R.id.listStays);
        adapter = new CustomStayListAdapter(getActivity(), stayList);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        // Creating volley request obj
        JsonArrayRequest stayReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {


                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Stay stay = new Stay();

                                int cost=0;
                                Date startDate=null,endDate=null;
                                SimpleDateFormat sdf = new SimpleDateFormat("DD/MM/yy", Locale.US);
                                try {
                                    startDate = sdf.parse("09/12/15");
                                    endDate = sdf.parse("09/12/15");
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                String finalCost = "₹" + cost;
                                String duration = startDate + " - " + endDate;

                                stay.setAddress(obj.getString("title"));
                                stay.setThumbnailUrl(obj.getString("image"));
                                stay.setFinalCost(finalCost);
                                stay.setDuration(duration);

                                // adding stay to stay array
                                stayList.add(stay);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stayReq);

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
}
