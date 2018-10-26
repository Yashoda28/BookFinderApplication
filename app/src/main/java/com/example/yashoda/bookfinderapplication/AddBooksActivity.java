package com.example.yashoda.bookfinderapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AndroidRuntimeException;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOError;
import java.io.InputStream;
import java.sql.SQLException;

import static com.example.yashoda.bookfinderapplication.CommonUtils.handleException;

public class AddBooksActivity extends AppCompatActivity
{

    private static final int RESULT_LOAD_IMAGE = 1;

    Connectivity connectivity = new Connectivity();
    Context context = AddBooksActivity.this;
    ProgressDialog progressDialog;

    // Declaring connection variables and array,string to store data in them
    byte[] byteArray;
    String encodedImage;
    SharedPreferences sharedPref;
    String emailAddressSP;

    Button btnUploadImage;
    ImageView imagebox;
    EditText etTitle;
    EditText etAuthor;
    EditText etSummary;
    EditText etPrice;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_books);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        emailAddressSP = sharedPref.getString("key1","No email defined");

        btnSave = (Button) findViewById(R.id.btnSaveOnAddBooks);
        btnUploadImage = (Button) findViewById(R.id.btnUploadImageOnAddBooks);
        btnUploadImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Opening the Gallery and selecting media
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)&& !Environment.getExternalStorageState().equals(Environment.MEDIA_CHECKING))
                {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent,RESULT_LOAD_IMAGE );
                    // this will jump to onActivity Function after selecting image
                }
                else
                {
                    Toast.makeText(context, "No activity found to perform this task", Toast.LENGTH_SHORT).show();
                }
                // End Opening the Gallery and selecting media


            }
        });
        findViews();

        createSaveButton(etTitle, etAuthor, etSummary,etPrice,btnSave);
    }

    private void createSaveButton(final EditText etTitle,
                                  final EditText etAuthor, final EditText etSummary, final EditText etPrice, Button btnSave)
    {
        btnSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final String title = etTitle.getText().toString();
                final String author = etAuthor.getText().toString();
                final String summary = etSummary.getText().toString();
                final Double price = Double.parseDouble(etPrice.getText().toString());
                final String status = "Available";
                final String locationDetails = "Available";

                progressDialog = ProgressDialog.show(context,
                        "Saving Information",
                        "Please be patient....", false);

                new Thread(new Runnable() {
                    public void run()
                    {
                        try {
                            Save(emailAddressSP, title, author, summary, price, status, encodedImage, locationDetails);
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

    private void findViews()
    {
        imagebox = (ImageView) findViewById(R.id.imageView2);
        etTitle = findViewById(R.id.etAddBookTitle);
        etAuthor = findViewById(R.id.etAddBookAuthor);
        etSummary = findViewById(R.id.etAddBookSummary);
        etPrice = findViewById(R.id.etAddBookPrice);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK  && null != data)
        {
            // getting the selected image, setting in imageview and converting it to byte and base 64
            Bitmap originBitmap = null;
            Uri selectedImage = data.getData();
            Toast.makeText(context, selectedImage.toString(), Toast.LENGTH_LONG).show();
            InputStream imageStream;
            try
            {
                imageStream = getContentResolver().openInputStream(selectedImage);
                originBitmap = BitmapFactory.decodeStream(imageStream);
            }
            catch (FileNotFoundException e)
            {
                System.out.println(e.getMessage().toString());
            }
            if (originBitmap != null)
            {
                this.imagebox.setImageBitmap(originBitmap);
                Log.w("Image Setted in", "Done Loading Image");
                try
                {
                    Bitmap image = ((BitmapDrawable) imagebox.getDrawable()).getBitmap();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                    byteArray = byteArrayOutputStream.toByteArray();
                    encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                }
                catch (Exception e)               {
                    Log.w("OOooooooooo","exception");
                }
                Toast.makeText(context, "Conversion Done",Toast.LENGTH_SHORT).show();
            }
            // End getting the selected image, setting in imageview and converting it to byte and base 64
        }
        else
        {
            System.out.println("Error Occured");
        }
    }

    private Boolean Save(String email, String title, String author, String summary, Double price, String status, String encodedImage, String locationDetails) throws SQLException {
        int rowsInserted = connectivity.insertUpdateOrDelete(getDetailsQuery(email, title, author, summary, price, status, encodedImage, locationDetails));
        progressDialog.cancel();
        startActivity(new Intent(context, ViewingActivity.class));
        return true;
    }

    private String getDetailsQuery(String emailAddress, String title, String author, String summary, Double price, String status, String encodedImage, String locationDetails)
    {
        return "INSERT INTO BOOK (EMAILADDRESS, TITLE, AUTHOR, SUMMARY, PRICE, STATUS, PICTURE, LOCATIONDETAILS)" +
                "VALUES('"+ emailAddress + "','" + title+ "','" + author + "','" + summary + "','" + price  + "','" + status  + "','" + encodedImage  + "','" +  locationDetails + "')";
    }
}
