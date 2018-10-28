package com.example.yashoda.bookfinderapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yashoda.bookfinderapplication.tables.Book;

import java.sql.ResultSet;

import static com.example.yashoda.bookfinderapplication.CommonUtils.handleException;

public class Viewing2Activity extends AppCompatActivity {
    Connectivity connectivity = new Connectivity();

    Context context = Viewing2Activity.this;

    ProgressDialog progressDialog;

    ImageView imagebox;
    TextView etTitle;
    TextView etAuthor;
    TextView etSummary;
    Button btnInterested;
    TextView etPrice;
    Button btnUpdate;

    SharedPreferences sharedPref;

    SharedPreferences.Editor editor;
    String emailAddressSP;
    int index;

    Book book;

    ResultSet rs;

//    RadioButton available;
//    RadioButton unavailable;

    CheckBox avail;
    CheckBox unavail;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewing2);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher_weight);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        Button btnBack = findViewById(R.id.btnBackOnViewing2);
        createViewMapButton(btnBack);
        btnInterested = findViewById(R.id.btnInterestedOnViewing2);
        createViewInterested(btnInterested);
        Button btnBackViewHistory = findViewById(R.id.btnBackToHistoryOnViewing2);
        createBackViewHistory(btnBackViewHistory);
        Button btnViewAllBooks = findViewById(R.id.btnBackToBooksViewing2);
        createViewAllBooks(btnViewAllBooks);

        btnUpdate = findViewById(R.id.btnUpdateOnViewing2);
        //createInterestedButton(btnInterested);

//        available = findViewById(R.id.radio_available);
//        unavailable = findViewById(R.id.radio_unavailable);

        avail = findViewById(R.id.checkbox_available);
        unavail = findViewById(R.id.checkbox_unavailable);
        //avail.setEnabled(false);
        unavail.setEnabled(false);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        emailAddressSP = sharedPref.getString("key1","No email defined");
        index = sharedPref.getInt("key2", 0);
        //index=5;
        findViews();

        progressDialog = ProgressDialog.show(context,
                "Loading",
                "Please be patient....", false);
        new Thread(new Runnable() {
            public void run() {
                try {
                    book = populateViews(index);
                    String bookLocationDetails = book.getLocationDetails();
                    editor = sharedPref.edit();
                    editor.putString("key3", bookLocationDetails);
                    editor.commit();
                } catch (final Exception e) {
                    progressDialog.cancel();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            handleException(context, e, e.getMessage());
                        }
                    });
                }
                runOnUiThread(new Runnable()
                {
                    public void run() {

                        if( emailAddressSP.equals(book.getEmailAddress()))
                        {
                            btnUpdate.setVisibility(View.VISIBLE);
                            btnUpdate.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v)
                                {
                                    startActivity(new Intent(context, UpdatingActivity.class));
                                    finish();
                                }
                            });
                        }
                        else
                        {
                            btnUpdate.setVisibility(View.GONE);
                        }

                        if("Available".equalsIgnoreCase(book.getStatus()))
                        {
                            avail.setChecked(true);
                            unavail.setChecked(false);
                            avail.setClickable(false);
                            unavail.setClickable(false);
                        }
                        else
                        {
                            unavail.setChecked(true);
                            avail.setChecked(false);
                            avail.setEnabled(false);
                            avail.setClickable(false);
                            unavail.setClickable(false);
                            btnInterested.setEnabled(false);
                            Toast.makeText(context,"Book is sold out",Toast.LENGTH_LONG).show();
                        }

//                        if("Unavailable".equalsIgnoreCase(book.getStatus()))
//                        {
//                            unavailable.setSelected(true);
//                            available.setSelected(false);
//                        }
//                        else{
//                            unavailable.setSelected(false);
//                            available.setSelected(true);
//                        }

                        String pic = book.getPicture();
                        imagebox.setImageBitmap(decodeToBase64(pic));
                        String title = book.getTitle();
                        etTitle.setText(title);
                        String author = book.getAuthor();
                        etAuthor.setText(author);
                        String summary = book.getSummary();
                        etSummary.setText(summary);
                        Double price = book.getPrice();
                        etPrice.setText(price.toString());

                        progressDialog.cancel();
                    }
                });
            }
        }).start();
    }

   /* private void createInterestedButton(Button btnInterested)
    {
        btnInterested.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Properties props = new Properties();
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.socketFactory.port", "465");
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.port", "465");

            }
        });
    }*/

    private Book populateViews(int index) throws Exception {
        rs = connectivity.getResultSet(getBookViewingQuery(index));
        rs.next();
        return new Book(rs.getInt(1), rs.getString(2), rs.getString(3),
                rs.getString(4), rs.getString(5), rs.getDouble(6), rs.getString(7),
                rs.getString(8), rs.getString(9));
    }

    public static Bitmap decodeToBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }


    private void findViews() {
        imagebox = (ImageView) findViewById(R.id.imageViewOnViewing2);
        etTitle = findViewById(R.id.etViewing2Title);
        etAuthor = findViewById(R.id.etViewing2Author);
        etSummary = findViewById(R.id.etViewing2Summary);
        etPrice = findViewById(R.id.etViewing2Price);
    }

    private String getBookViewingQuery(int index) {
        return "SELECT * FROM BOOK B WHERE B.BOOKID ='" + index + "'";
    }

    private void createViewInterested(Button btnInterested)
    {
        btnInterested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
//        if("Available".equalsIgnoreCase(book.getStatus()))
//        {

//        }
//        else
//        {
//            btnInterested.setEnabled(false);
//            btnInterested.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View v) {
//                    Toast.makeText(context,"Book is sold out, unable to send email",Toast.LENGTH_LONG).show();
//                }
//            });
//        }
    }

    private void createViewMapButton(Button btnBack) {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void sendEmail(){
        String i = emailAddressSP;
        Email email = new Email(context, book.getEmailAddress(), i + " has an interest in second hand textbook", "Hello\n\n "+i  +
                " is interested in purchasing your " +book.getTitle()+ " textbook. Contact them at their email address. \n\n Thanks ");
        email.execute();
    }

    private void createViewAllBooks(Button btnViewAllBooks)
    {
        btnViewAllBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ViewingActivity.class));
                finish();
            }
        });
    }

    private void createBackViewHistory(Button btnBackViewHistory)
    {
        btnBackViewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ViewHistoryActivity.class));
                finish();
            }
        });
    }
}
