package com.example.yashoda.bookfinderapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.ResultSet;

import javax.security.auth.login.LoginException;

import static com.example.yashoda.bookfinderapplication.CommonUtils.handleException;
import static java.lang.Thread.sleep;

public class LogInActivity extends AppCompatActivity
{

    Connectivity connectivity = new Connectivity();

    Context context = LogInActivity.this;

    ProgressDialog progressDialog;

    Button btnLogin;
    Button btnReg;
    EditText editTextEmailAddress;
    EditText editTextPassword;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        connectivity.execute("");
        setContentView(R.layout.activity_log_in);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPref.edit();

        btnLogin = findViewById(R.id.btnLoginOnLogIn);
        btnReg = findViewById(R.id.btnRegisterOnLogIn);
        editTextEmailAddress = findViewById(R.id.etLoginEmail);
        editTextPassword = findViewById(R.id.etLoginPassword);

        createLoginBtn(btnLogin, editTextEmailAddress, editTextPassword);
        createRegistrationBtn(btnReg);
    }

    private void createLoginBtn(Button btnLogin, final EditText editTextEmailAddress, final EditText editTextPassword)
    {
        btnLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                final String emailAddress = editTextEmailAddress.getText().toString();
                final String password = editTextPassword.getText().toString();

                progressDialog = ProgressDialog.show(context,
                        "Logging in",
                        "Please be patient....", false);
                new Thread(new Runnable() {
                    public void run()
                    {
                        try {
                            LogIn(emailAddress, password);
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

    private void createRegistrationBtn(Button btnReg) {
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegistration();
            }
        });
    }

    private void goToRegistration() {
        startActivity(new Intent(LogInActivity.this, RegisterActivity.class));
        finish();
    }

    private void LogIn(String emailAddress, String password) throws Exception {
        String loginQuery = getLoginQuery(emailAddress, password);
        ResultSet rs = connectivity.getResultSet(loginQuery);
        rs.next();
        if (rs.getInt("RECORDCOUNT") == 1) {
            editor.putString("key1",emailAddress);
            editor.commit();
            goToInfoView();
        } else {
            throw new LoginException("Incorrect Log In Details");
        }
    }

    private void goToInfoView() throws InterruptedException {
        startActivity(new Intent(LogInActivity.this, ViewingActivity.class));
        progressDialog.cancel();
        finish();
    }

    private String getLoginQuery(String emailAddress, String password) {
        return "SELECT COUNT(*) AS RECORDCOUNT FROM DETAILS D WHERE D.EMAILADDRESS = '" + emailAddress + "' AND D.PASSWORD = '" + password + "'";
    }
}
