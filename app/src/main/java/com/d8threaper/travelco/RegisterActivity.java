package com.d8threaper.travelco;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.d8threaper.travelco.app.AppConfig;
import com.d8threaper.travelco.app.AppController;
import com.d8threaper.travelco.helper.SQLiteHandler;
import com.d8threaper.travelco.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;


public class RegisterActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    @Bind(R.id.btnRegister) Button btnRegister;
    @Bind(R.id.reg_password)
    EditText inputPassword;
    @Bind(R.id.reg_email)
    EditText inputEmail;
    @Bind(R.id.reg_fullname)
    EditText inputFullName;
    @Bind(R.id.reg_phone)
    EditText inputPhone;
    @Bind(R.id.link_to_login)
    TextView btnLinkToLogin;
    @Bind(R.id.reg_gender)
    RadioGroup inputGender;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(true);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegisterActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = inputFullName.getText().toString();
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();
                String phone = inputPhone.getText().toString();
                String gender;

                RadioButton radioSexButton;

                int selectID = inputGender.getCheckedRadioButtonId();
                if (selectID!= -1){
                    radioSexButton = (RadioButton) inputGender.findViewById(selectID);
                    gender = radioSexButton.getText().toString();
                } else {
                    gender = null;
                }

                registerUser(name, email, password, gender, phone);

            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        MainActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     */
    private void registerUser(final String name, final String email,
                              final String password, final String gender,
                              final String phone) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        if (!validate(name, email, password, gender, phone)) {
            onSignupFailed();
            return;
        }

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        session.setLogin(true);

                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String name = jObj.getString("name");
                        String email = jObj.getString("email");
                        String api = jObj.getString("api");
                        String gender = jObj.getString("gender");
                        String phone = jObj.getString("phone");
//                        String created_at = jObj
//                                .getString("created_at");
                        String created_at = "aj";

                        db.addUser(name, email, api, created_at, gender, phone);

                        // Launch camera activity
                        Intent intent = new Intent(
                                RegisterActivity.this,
                                FormActivity.class);
                        intent.putExtra("form",3);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "register");
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("gender", gender);
                params.put("phone", phone);

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

    public void onSignupSuccess() {
        btnRegister.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        btnRegister.setEnabled(true);
    }

    public boolean validate(final String name, final String email,
                            final String password, final String gender,
                            final String phone) {
        boolean valid = true;

        if (name.isEmpty() || name.length() < 3) {
            inputFullName.setError("at least 3 characters");
            valid = false;
        } else {
            inputFullName.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("enter a valid email address");
            valid = false;
        } else {
            inputEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            inputPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            inputPassword.setError(null);
        }

        if (phone.isEmpty() || phone.length() < 10) {
            inputPhone.setError("at least 10 characters");
            valid = false;
        } else {
            inputPhone.setError(null);
        }

        if (gender == null) {
            valid = false;
        }


            return valid;
    }
}

