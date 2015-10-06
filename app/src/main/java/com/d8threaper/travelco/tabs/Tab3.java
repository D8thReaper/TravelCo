package com.d8threaper.travelco.tabs;

import android.app.ProgressDialog;
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
import com.d8threaper.travelco.CustomListView.CustomCompanionListAdapter;
import com.d8threaper.travelco.CustomListView.model.Companion;
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

/**
 * Created by root on 11/9/15.
 */
public class Tab3 extends Fragment {

    // Log tag
    private static final String TAG = "Companions Tab";

    // Companions json url
    private static final String url = "http://api.androidhive.info/json/movies.json";
    private ProgressDialog pDialog;
    private List<Companion> companionList = new ArrayList<Companion>();
    private ListView listView;
    private CustomCompanionListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab3,container,false);

        FloatingActionButton addCompanion = (FloatingActionButton) v.findViewById(R.id.addCompanion);
        addCompanion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "wait beerey", Toast.LENGTH_SHORT).show();
            }
        });

        listView = (ListView) v.findViewById(R.id.listCompanion);
        adapter = new CustomCompanionListAdapter(getActivity(), companionList);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        // Creating volley request obj
        final JsonArrayRequest companionReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Companion companion = new Companion();

                                Date srcTime=null, destTime=null;
                                SimpleDateFormat sdf = new SimpleDateFormat("DD/MM/yy");
                                try {
                                    srcTime = sdf.parse("09/12/15");
                                    destTime = sdf.parse("09/12/15");
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                String duration = srcTime + " - " + destTime;

                                companion.setName(obj.getString("title"));
                                companion.setThumbnailUrl(obj.getString("image"));
                                companion.setDuration(duration);

                                // adding companion to companion array
                                companionList.add(companion);

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
        AppController.getInstance().addToRequestQueue(companionReq);

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
