package ras.asu.com.letsmeet;



/**
 * Created by Aditya on 2/21/2016.
 */
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.app.Activity;
public class findfriends extends AppCompatActivity {

    ListView listImg;
    List<Bitmap> bitmap1 = new ArrayList<Bitmap>();
    Bitmap bitmap;
    ProgressDialog pDialog;

    List<List<Bitmap>> aList = new ArrayList<List<Bitmap>>();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);
        Intent intent = getIntent();
        String jsondata = intent.getStringExtra("jsondata");

        JSONArray friendslist;
        ArrayList<String> friends = new ArrayList<String>();
        ArrayList<String> id = new ArrayList<String>();




        //setContentView(R.layout.list_single);



        try {
            friendslist = new JSONArray(jsondata);
            for (int l=0; l < friendslist.length(); l++) {
                friends.add(friendslist.getJSONObject(l).getString("name"));
                id.add(friendslist.getJSONObject(l).getString("id"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        listImg = (ListView)findViewById(R.id.list);
        String[] url = new String[id.size()];
        for(int i=0;i<id.size();i++) {
            System.out.println("ID= " + id.get(i));
            url[i] = ("https://graph.facebook.com/" + id.get(i) + "/picture");
            //new LoadImage().execute(url[i]);

        }
        // TODO Auto-generated method stub
        //new LoadImage().execute("http://www.joomlaworks.net/images/demos/galleries/abstract/7.jpg");

        //ArrayAdapter adapter1 = new ArrayAdapter<String>(this, R.layout.activity_listview, url); // simple textview for list item

        //ListView listView1 = (ListView) findViewById(R.id.list);
        //listView1.setAdapter(adapter1);


       //new LoadImage().execute("https://graph.facebook.com/" + id.get(0) + "/picture");

      ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, friends); // simple textview for list item
      ListView listView = (ListView) findViewById(R.id.listView);
      listView.setAdapter(adapter);
        //new LoadImage().execute("http://www.joomlaworks.net/images/demos/galleries/abstract/7.jpg");

    }
    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(findfriends.this);
            pDialog.setMessage("Loading Image ....");
            pDialog.show();

        }
        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());

            } catch (Exception e) {
                e.printStackTrace();
            }
            bitmap1.add(bitmap);
            return bitmap;

        }

        protected void onPostExecute(Bitmap image) {
            ImageView img = new ImageView(getApplicationContext());

            if(image != null){


                img.setImageBitmap(image);
                   // listImg.addView(img);

                    pDialog.dismiss();


            }else{

                pDialog.dismiss();
                Toast.makeText(findfriends.this, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();

            }
        }
    }

}

