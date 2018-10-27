package com.example.yashoda.bookfinderapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.yashoda.bookfinderapplication.CommonUtils.handleException;

public class RegisterActivity extends AppCompatActivity
{
    Connectivity connectivity = new Connectivity();

    Context context = RegisterActivity.this;

    ProgressDialog progressDialog;

    EditText etName;
    EditText etSurname;
    EditText etEmailAddress;
    EditText etPassword;
    EditText etCellNumber;
    Spinner spinCampusName;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher_weight);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        Button btnRegister= findViewById(R.id.btnRegisterOnRegister);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPref.edit();

        findViews();

        createRegisterButton(btnRegister, etName, etSurname, etEmailAddress, etPassword,  etCellNumber, spinCampusName);

    }

    private void findViews()
    {
        etName = findViewById(R.id.etRegistserName);
        etSurname = findViewById(R.id.etRegisterSurname);
        etEmailAddress = findViewById(R.id.etRegisterEmailAddress);
        etPassword = findViewById(R.id.etRegisterPassword);
        etCellNumber = findViewById(R.id.etRegisterCellNumber);
        spinCampusName = findViewById(R.id.spinnerRegisterCampusName);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item) {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = super.getView(position, convertView, parent);
            if (position == getCount()) {
                ((TextView)v.findViewById(android.R.id.text1)).setText("");
                ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
            }

            return v;
        }

        @Override
        public int getCount() {
            return super.getCount()-1; // you dont display last item. It is used as hint.
        }

    };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.add("UKZN Howard Campus");
        adapter.add("UKZN Westville Campus");
        adapter.add("UKZN PMB Campus");
        adapter.add("UKZN Edgewood Campus");
        adapter.add("UKZN Medical School");
        adapter.add("Select Campus"); //This is the text that will be displayed as hint.

        spinCampusName.setAdapter(adapter);
        spinCampusName.setSelection(adapter.getCount()); //set the hint the default selection so it appears on launch.
        spinCampusName.getOnItemSelectedListener();


    }

    private void createRegisterButton(Button btnReg, final EditText editTextName, final EditText editTextSurname, final EditText editTextEmailAddress,
                                      final EditText editTextPassword, final EditText editTextCellNumber, final Spinner spinCampusName)
    {
        btnReg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                final String name = editTextName.getText().toString();
                final String surname = editTextSurname.getText().toString();
                final String emailAddress = editTextEmailAddress.getText().toString();
                final String password = editTextPassword.getText().toString();
                final String cellNumber = editTextCellNumber.getText().toString();


                final String campusName = spinCampusName.getSelectedItem().toString();

                progressDialog = ProgressDialog.show(context,
                        "Saving Information",
                        "Please be patient....", false);

                new Thread(new Runnable() {
                    public void run()
                    {
                        try {
                            Register(emailAddress, password, name, surname, cellNumber, campusName);
                        }
                        catch (final Exception e)
                        {
                            progressDialog.cancel();
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    handleException(context, e, e.getMessage());
                                }
                            });
                        }
                    }
                }).start();
            }


        });
    }

    private Boolean Register(String emailAddress, String password, String name, String surname, String cellNumber, String campusName) throws SQLException
    {
        int rowsInserted = connectivity.insertUpdateOrDelete(getDetailsQuery(emailAddress, password, name, surname, cellNumber, campusName));
        editor.putString("campusNameDetails",campusName);
        editor.commit();
        progressDialog.cancel();
        startActivity(new Intent(context, ViewingActivity.class));
        return true;
    }

    private String getDetailsQuery(String emailAddress, String password, String name, String surname, String cellNumber, String campusName)
    {
        return "INSERT INTO DETAILS (EMAILADDRESS, PASSWORD, NAME, SURNAME, CELLNUMBER, CAMPUSNAME)" +
                "VALUES('"+ emailAddress + "','" + password+ "','" + name + "','" + surname + "','" + cellNumber  + "','" +  campusName + "')";
    }

}
