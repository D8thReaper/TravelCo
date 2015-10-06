package com.d8threaper.travelco.Forms;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.d8threaper.travelco.Display;
import com.d8threaper.travelco.R;
import com.d8threaper.travelco.app.AppConfig;
import com.d8threaper.travelco.app.AppController;
import com.d8threaper.travelco.helper.SQLiteHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class addStay extends Fragment {

    EditText address, startDate, endDate,money, details;
    private Calendar myCalendar = Calendar.getInstance();
    SimpleDateFormat sdf;
    Button btnApply;
    CheckBox breakfast;
    ProgressDialog pDialog;
    String TAG = "Add Ride Tab";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.activity_add_stay,container,false);

        address = (EditText) v.findViewById(R.id.addressStay);
        startDate = (EditText) v.findViewById(R.id.dateStayStart);
        endDate = (EditText) v.findViewById(R.id.dateStayEnd);
        money = (EditText) v.findViewById(R.id.moneyStay);
        details = (EditText) v.findViewById(R.id.detailsStay);
        breakfast = (CheckBox) v.findViewById(R.id.breakfast);
        btnApply = (Button) v.findViewById(R.id.apply_stay);

        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yy"; //In which you need put here
                sdf = new SimpleDateFormat(myFormat, Locale.US);
                startDate.setText(sdf.format(myCalendar.getTime()));
            }

        };

        final DatePickerDialog.OnDateSetListener dat2 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yy"; //In which you need put here
                sdf = new SimpleDateFormat(myFormat, Locale.US);
                endDate.setText(sdf.format(myCalendar.getTime()));
            }

        };

        startDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), dat2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sAddress, sStartDate, sEndDate, sMoney, sDetails,bBreakfast;
                if (breakfast.isChecked()){
                    bBreakfast = "true";
                }else{
                    bBreakfast = "false";
                }
                sAddress = address.getText().toString();
                sStartDate = startDate.getText().toString();
                sEndDate = endDate.getText().toString();
                sMoney = money.getText().toString();
                sDetails = details.getText().toString();

                if (!sAddress.isEmpty() && !sStartDate.isEmpty() && !sEndDate.isEmpty() && !sDetails.isEmpty() &&
                        !sMoney.isEmpty() && !bBreakfast.isEmpty()){
                    AddStay(sAddress, sStartDate, sEndDate, sMoney, bBreakfast,sDetails);
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
        sdf = new SimpleDateFormat(myFormat, Locale.US);
    }

    private void AddStay(final String sAddress,final String sStartDate,final String sEndDate,
                         final String sMoney, final String bBreakfast,
                         final String sDetails) {

        // Tag used to cancel the request
        String tag_string_req = "req_add_stay";

        pDialog.setMessage("Adding new stay ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ADD_STAY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Add stay Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // Launch login activity
                        Intent intent = new Intent(getActivity(),Display.class);
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
                params.put("address", sAddress);
                params.put("availSeats", sStartDate);
                params.put("totSeats", sEndDate);
                params.put("AC",sDetails);
                params.put("money",sMoney);
                params.put("depart",bBreakfast);

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

