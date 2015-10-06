package com.d8threaper.travelco;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.d8threaper.travelco.NavDrawer.Activity.FragmentAccount;
import com.d8threaper.travelco.NavDrawer.Activity.FragmentHistory;
import com.d8threaper.travelco.NavDrawer.Adapter.FragmentDrawer;
import com.d8threaper.travelco.helper.PlacesAutoCompleteAdapter;
import com.d8threaper.travelco.helper.SQLiteHandler;
import com.d8threaper.travelco.helper.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener{

    private EditText dateText;
    private Button btnGo;

    Toolbar toolbar;
    private SQLiteHandler db;
    private SessionManager session;
    private Calendar myCalendar = Calendar.getInstance();
    private String description,mydescription,travelStartDate=null;
    private boolean one=false,two=false,three=false;
    private Activity mActivity;
    private FragmentDrawer drawerFragment;
    private int REQUEST_CODE_GET_JSON = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainz);

        mActivity = this;
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerFragment.setDrawerListener(this);

        btnGo = (Button) findViewById(R.id.btnGo);;
        dateText = (EditText) findViewById(R.id.editText);


        final AutoCompleteTextView autocompleteView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        final AutoCompleteTextView myautocompleteView = (AutoCompleteTextView) findViewById(R.id.myautoCompleteTextView);
        autocompleteView.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.autocomplete_list_item));
        myautocompleteView.setAdapter(new PlacesAutoCompleteAdapter(this,R.layout.autocomplete_list_item));

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");

        if (!session.isLoggedIn()) {
            logoutUser();
        }



        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
                travelStartDate = dateText.getText().toString();
            }

        };

        dateText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(MainActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                one=true;
            }
        });



        autocompleteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get data associated with the specified position
                // in the list (AdapterView)
                description = (String) parent.getItemAtPosition(position);
                two=true;
            }
        });

        myautocompleteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get data associated with the specified position
                // in the list (AdapterView)
                mydescription = (String) parent.getItemAtPosition(position);
                three=true;
            }
        });

        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(one && two && three){
//                    SharedPreferences.Editor editor = getParent().getSharedPreferences("travel",0).edit();
//                    editor.putString("src",mydescription);
//                    editor.putString("dest",description);
//                    editor.putString("startDate", travelStartDate);
//                    editor.commit();
                    Intent intent = new Intent(MainActivity.this,Display.class);
                    intent.putExtra("src",mydescription);
                    intent.putExtra("dest",description);
                    intent.putExtra("startDate",travelStartDate);
                    startActivity(intent);
//                }else {
                    Toast.makeText(getApplicationContext(),"Please fill in the required text!",Toast.LENGTH_SHORT).show();
//                }
            }
        });

    }

    private void updateLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateText.setText(sdf.format(myCalendar.getTime()));
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new FragmentHistory();
                title = getString(R.string.title_history);
                break;
            case 1:
                fragment = new FragmentAccount();
                break;
            case 2:
                logoutUser();
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }
}



