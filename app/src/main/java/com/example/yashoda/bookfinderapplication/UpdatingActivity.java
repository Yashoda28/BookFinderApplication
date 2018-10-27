package com.example.yashoda.bookfinderapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import com.example.yashoda.bookfinderapplication.tables.Book;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.yashoda.bookfinderapplication.CommonUtils.handleException;

public class UpdatingActivity extends AppCompatActivity {

    Connectivity connectivity = new Connectivity();

    Context context = UpdatingActivity.this;

    ProgressDialog progressDialog;

    ImageView imagebox;
    EditText etTitle;
    EditText etAuthor;
    EditText etSummary;
    EditText etPrice;

    int bookID;

    SharedPreferences sharedPref;
    int index;

    Book book;

    RadioGroup radio_group;
    RadioButton rad_available;
    RadioButton rad_unavailable;
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updating);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher_weight);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        findViews();

        Button btnUpdate = findViewById(R.id.btnUpdateOnUpdateBooks);

        createUpdateButton(btnUpdate, etTitle, etAuthor, etSummary, etPrice);

        radio_group = findViewById(R.id.radiogroupUpdating);
        rad_available = findViewById(R.id.radio_availableUpdating);
        rad_unavailable = findViewById(R.id.radio_unavailableUpdating);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        index = sharedPref.getInt("key2", 0);

        progressDialog = ProgressDialog.show(context,
                "Loading",
                "Please be patient....", false);
        new Thread(new Runnable() {
            public void run() {
                try {
                    ResultSet rs = connectivity.getResultSet(getBookViewingQuery(index));
                    populateViews(index, rs);
                    progressDialog.cancel();

                    runOnUiThread(new Runnable() {
                        public void run() {

                            String title = book.getTitle();
                            etTitle.setText(title);
                            String author = book.getAuthor();
                            etAuthor.setText(author);
                            String summary = book.getSummary();
                            etSummary.setText(summary);
                            String price = book.getPrice().toString();
                            etPrice.setText(price);
                            String pic = book.getPicture();
                            imagebox.setImageBitmap(decodeToBase64(pic));

                            if ("Unavailable".equalsIgnoreCase(book.getStatus())) {
                                rad_unavailable.setSelected(true);
                                rad_available.setSelected(false);
                            } else {
                                rad_unavailable.setSelected(false);
                                rad_available.setSelected(true);
                            }

                            rad_available.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onRadioButtonClicked(v);
                                }
                            });

                            rad_unavailable.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onRadioButtonClicked(v);
                                }
                            });
                        }
                    });
                } catch (final Exception e) {
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

    public void onRadioButtonClicked(View view) {

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_availableUpdating:
                status = "Available";
                break;
            case R.id.radio_unavailableUpdating:
                status = "Unavailable";
                break;
        }


    }

    private void createUpdateButton(Button btnUpdate, final EditText etTitle,
                                    final EditText etAuthor, final EditText etSummary, final EditText etPrice) {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String title = etTitle.getText().toString();
                final String author = etAuthor.getText().toString();
                final String summary = etSummary.getText().toString();
                final Double price = Double.parseDouble(etPrice.getText().toString());
                progressDialog = ProgressDialog.show(context,
                        "Saving Information",
                        "Please be patient....", false);

                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Save(index, title, author, summary, price);
                        } catch (final Exception e) {
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

    private void populateViews(int index, ResultSet rs) throws Exception {
        rs.next();

        book = new Book(rs.getInt(1), rs.getString(2), rs.getString(3),
                rs.getString(4), rs.getString(5), rs.getDouble(6), rs.getString(7),
                rs.getString(8), rs.getString(9));

        bookID = book.getBookID();
    }

    public static Bitmap decodeToBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    private void findViews() {
        etTitle = findViewById(R.id.etUpdateBookTitle);
        etAuthor = findViewById(R.id.etUpdateBookAuthor);
        etSummary = findViewById(R.id.etUpdateBookSummary);
        etPrice = findViewById(R.id.etUpdateBookPrice);
        imagebox = (ImageView) findViewById(R.id.imageViewOnUpdating);
    }

    private Boolean Save(int index, String title, String author, String summary, Double price) throws SQLException {
        String bookUpdatingQuery = getBookUpdatingQuery(index, title, author, summary, price);
        connectivity.insertUpdateOrDelete(bookUpdatingQuery);
        progressDialog.cancel();
        startActivity(new Intent(context, Viewing2Activity.class));
        finish();
        return true;
    }

    private String getBookViewingQuery(int index) {
        return "SELECT * FROM BOOK B WHERE B.BOOKID =" + index;
    }

    private String getBookUpdatingQuery(int index, String title, String author, String summary, Double price) {
        return "UPDATE Book SET TITLE = '" + title +
                "', AUTHOR = '" + author + "', SUMMARY = '" + summary + "', PRICE = '" + price + "', status = '" + status +
                "' WHERE BOOKID = " + index;
    }
    //UPDATE Details SET Name = 'Hello' WHERE EmailAddress = '1'

    private String setStatusUpdatingQuery(int index, String status) {
        return "UPDATE Details SET STATUS = '" + status +
                "' WHERE BOOKID = '" + index + "'";
    }
}
