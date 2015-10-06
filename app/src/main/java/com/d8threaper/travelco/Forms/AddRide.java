package com.d8threaper.travelco.Forms;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.d8threaper.travelco.Display;
import com.d8threaper.travelco.LoginActivity;
import com.d8threaper.travelco.R;
import com.d8threaper.travelco.app.AppConfig;
import com.d8threaper.travelco.app.AppController;
import com.d8threaper.travelco.helper.PlacesAutoCompleteAdapter;
import com.d8threaper.travelco.helper.SQLiteHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddRide extends Fragment {

    EditText car, availSeats, totSeats, dates, money, deptTime, arrivTime;
    private Calendar myCalendar = Calendar.getInstance();
    Button btnSubmit;
    String description,mydescription;
    SharedPreferences sharedPreferences;
    CheckBox ac;
    ProgressDialog pDialog;
    String TAG = "Add Ride Tab";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.activity_add_ride,container,false);

        car = (EditText) v.findViewById(R.id.carModel);
        availSeats = (EditText) v.findViewById(R.id.availSeats);
        totSeats = (EditText) v.findViewById(R.id.totalSeats);
        money = (EditText) v.findViewById(R.id.money);
        dates = (EditText) v.findViewById(R.id.dateDepartureRide);
        btnSubmit = (Button) v.findViewById(R.id.submit_ride);
        deptTime = (EditText)v.findViewById(R.id.departureTime);
        arrivTime = (EditText) v.findViewById(R.id.estReachTime);
        ac = (CheckBox) v.findViewById(R.id.ac);
        sharedPreferences = getActivity().getSharedPreferences("travel",0);
        description = sharedPreferences.getString("dest", null);
        String departureDate = sharedPreferences.getString("date",null);
        mydescription = sharedPreferences.getString("src",null);

        dates.setText(departureDate);

        final AutoCompleteTextView autocompleteView = (AutoCompleteTextView) v.findViewById(R.id.destAutoComplete);
        final AutoCompleteTextView myautocompleteView = (AutoCompleteTextView) v.findViewById(R.id.sourceAutoComplete);
        autocompleteView.setAdapter(new PlacesAutoCompleteAdapter(getActivity(), R.layout.autocomplete_list_item));
        myautocompleteView.setAdapter(new PlacesAutoCompleteAdapter(getActivity(), R.layout.autocomplete_list_item));

        autocompleteView.setText(description);
        myautocompleteView.setText(mydescription);

        autocompleteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get data associated with the specified position
                // in the list (AdapterView)
                description = (String) parent.getItemAtPosition(position);
            }
        });

        myautocompleteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get data associated with the specified position
                // in the list (AdapterView)
                mydescription = (String) parent.getItemAtPosition(position);
            }
        });


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        dates.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        deptTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        deptTime.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        arrivTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        arrivTime.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sCar, sAvailSeats, sTotalSeats,sDate, sMoney, sDepartTime, sArriveTime,bAc;
                if (ac.isChecked()){
                    bAc = "true";
                }else{
                    bAc = "false";
                }
                sCar = car.getText().toString();
                sAvailSeats = availSeats.getText().toString();
                sTotalSeats = totSeats.getText().toString();
                sDate = dates.getText().toString();
                sMoney = money.getText().toString();
                sDepartTime = deptTime.getText().toString();
                sArriveTime = arrivTime.getText().toString();

                if (!sCar.isEmpty() && !sAvailSeats.isEmpty() && !sTotalSeats.isEmpty() && !sDate.isEmpty() &&
                        !sMoney.isEmpty() && !sDepartTime.isEmpty() && !bAc.isEmpty() &&
                        !sArriveTime.isEmpty() && !mydescription.isEmpty() && !description.isEmpty()){
                    addRide(sCar, sAvailSeats, sTotalSeats, sDate, sMoney, sDepartTime, bAc, sArriveTime
                    ,mydescription,description);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        return v;
    }

    private void updateLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dates.setText(sdf.format(myCalendar.getTime()));
    }

    private void addRide(final String sCar,final String sAvailSeats,final String sTotalSeats,
                         final String sDate,final String sMoney, final String sDepartTime,
                         final String bAc, final String sArriveTime, final String source,
                         final String destination) {

        // Tag used to cancel the request
        String tag_string_req = "req_add_ride";

        pDialog.setMessage("Adding new ride ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ADD_RIDE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // Launch login activity
                        Intent intent = new Intent(getActivity(),Display.class);
                        intent.putExtra("src",mydescription);
                        intent.putExtra("dest",description);
                        intent.putExtra("startDate",sDate);
                        startActivity(intent);
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getActivity().getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Add Ride Error: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url

                // SqLite database handler
                SQLiteHandler db = new SQLiteHandler(getActivity().getApplicationContext());

                // Fetching user details from sqlite
                HashMap<String, String> user = db.getUserDetails();
                String email = user.get("email");

                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "addRide");
                params.put("email",email);
                params.put("car", sCar);
                params.put("availSeats", sAvailSeats);
                params.put("totSeats", sTotalSeats);
                params.put("dep_date",sDate);
                params.put("AC",bAc);
                params.put("money",sMoney);
                params.put("depart",sDepartTime);
                params.put("arrive",sArriveTime);
                params.put("source",mydescription);
                params.put("dest",description);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
