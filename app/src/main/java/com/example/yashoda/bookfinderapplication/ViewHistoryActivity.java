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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yashoda.bookfinderapplication.tables.Book;

import java.sql.ResultSet;
import java.util.ArrayList;

import static com.example.yashoda.bookfinderapplication.CommonUtils.handleException;

public class ViewHistoryActivity extends AppCompatActivity
{
    Connectivity connectivity = new Connectivity();
    Context context = ViewHistoryActivity.this;
    ProgressDialog progressDialog;

    ListView picList;
    ArrayList<String> bookDetails2;

    public static String bookTitle;
    public static ArrayList<String> pictureImage;

    SharedPreferences sharedPref;
    String emailAddressSP;
    SharedPreferences.Editor editor;
    public static int index;

    ArrayList<Book> details = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher_weight);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        bookDetails2 = new ArrayList<>();
        pictureImage = new ArrayList<>();

        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        emailAddressSP = sharedPref.getString("key1","No email defined");

        editor = sharedPref.edit();

        findViews();

        picList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                index = details.get(i).getBookID();
                editor.putInt("key2", index);
                editor.commit();
                startActivity(new Intent(context, Viewing2Activity.class));
            }
        });

        progressDialog = ProgressDialog.show(context,
                "Loading",
                "Please be patient....", false);
        new Thread(new Runnable()
        {
            public void run() {
                try {

                    ResultSet rs = connectivity.getResultSet(getBookViewingQuery(emailAddressSP));
                    populateViews(rs);
                    progressDialog.cancel();
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

        CustomAdapter lAdapt = new CustomAdapter();
        picList.setAdapter(lAdapt);
    }

    private void findViews()
    {
        picList = (ListView)findViewById(R.id.pictureListViewOnHistory);
    }

    class CustomAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            //return imgID.length;
            return pictureImage.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.custom_layout,null);
            if (!(pictureImage.get(i) == null)) {
                ImageView dImg = (ImageView) view.findViewById(R.id.imageViewCustom);
                TextView dText = (TextView) view.findViewById(R.id.textViewCustom);

                //dImg.setImageResource(imgID[0]);
                dText.setText(bookDetails2.get(i));

                /// dText.setText(advertDetails.get(i));
                // Toast.makeText(Main6Activity.this ,"view get view", Toast.LENGTH_LONG).show();
                byte[] decodeString = Base64.decode(pictureImage.get(i), Base64.DEFAULT);
                Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                dImg.setImageBitmap(decodebitmap);
                //errorMsg.setText(msg + " not empty");
                dImg.invalidate();

            }


            return view;
        }
    }

    private void populateViews(ResultSet rs) throws Exception
    {
        ArrayList<String>imageByte = new ArrayList<>();

        int i=0;
        while (rs.next())
        {
            String Imagetext = rs.getString(8);
            Book book = new Book(rs.getInt(1), rs.getString(2), rs.getString(3),
                    rs.getString(4), rs.getString(5), rs.getDouble(6), rs.getString(7),
                    Imagetext, rs.getString(9));
            bookTitle = rs.getString(3);
            details.add(book);
            details.size();
            bookDetails2.add(details.get(i).toString());

            imageByte.add(Imagetext);
            pictureImage.add(Imagetext);
            i++;
        }
    }

    private String getBookViewingQuery(String emailAddressSP) {
        return "SELECT * FROM BOOK B WHERE B.EMAILADDRESS ='"+emailAddressSP+ "'";
    }


}
