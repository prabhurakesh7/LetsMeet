package ras.asu.com.letsmeet;


import android.app.Fragment;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Aditya on 4/7/2016.
 */
public class FBCheckin extends Fragment implements View.OnClickListener {

    Button checkin;
    View view;
    EditText moodmessage;
    String message;
    Button logout;

    public String pageID;
    //String FACEBOOK_LINK = "https://www.facebook.com/pizzaguydelivery";
    String FACEBOOK_LINK =  "https://www.facebook.com/";
   // ArrayList<String> id;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //super.onCreateView(inflater, container, savedInstanceState);
        //final View rootview = inflater.inflate(R.layout.fblogin, container, false);
        //checkin.setOnClickListener(this);
        //Intent intent = getActivity().getIntent();
        FACEBOOK_LINK =FACEBOOK_LINK+ProjCostants.PNAME.replaceAll(" ","");

        view = inflater.inflate(R.layout.fblogin, container, false);
        checkin = (Button) view.findViewById(R.id.checkin);
       // moodmessage = (TextView)view.findViewById(R.id.message);
        moodmessage = (EditText)view.findViewById(R.id.message);
        checkin.setOnClickListener(this);
        //ImageTextListViewActivity g = new ImageTextListViewActivity();
       // id = ProjCostants.FB_ID;
       // System.out.println("Error:" + id.size());
       /* logout = (Button) view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (AccessToken.getCurrentAccessToken() == null) {
                    return; // already logged out
                }

                new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                        .Callback() {
                    @Override
                    public void onCompleted(GraphResponse graphResponse) {

                        LoginManager.getInstance().logOut();
                        Toast.makeText(getActivity(), "Logout Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);



                    }
                }).executeAsync();


            }
        });*/
        return view;


    }



    @Override
    public void onClick(View v) {



        LoginManager.getInstance().logInWithPublishPermissions(getActivity(), Arrays.asList("publish_actions"));
        pageID = FACEBOOK_LINK.substring(FACEBOOK_LINK.lastIndexOf("/") + 1, FACEBOOK_LINK.length());
        new GraphRequest(AccessToken.getCurrentAccessToken(), "/" + pageID, null, HttpMethod.GET, new GraphRequest.Callback() {
            public void onCompleted(GraphResponse response) {
                                    /* handle the result */

                try {
                    if (response.getError() == null) {
                        JSONObject obj = response.getJSONObject();
                        if (obj.has("id")) {
                            pageID = obj.getString("id");

                            Bundle params = new Bundle();
                            message = moodmessage.getText().toString();
                            params.putString("message", message);
                            params.putString("place", pageID);
                            //params.putString("link", "https://www.limelight.com/");
                            params.putString("tags",ProjCostants.OTHER_GUYS_FB_ID);
                            if (pageID == null) {

                                Toast.makeText(getActivity(), "Failed to check in!", Toast.LENGTH_SHORT).show();
                            } else {
                                new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/feed", params, HttpMethod.POST, new GraphRequest.Callback() {
                                    public void onCompleted(GraphResponse response) {

                                        if (response.getError() == null) {
                                            //success
                                            Toast.makeText(getActivity(), "CheckIn Successful!!", Toast.LENGTH_SHORT).show();


                                        } else {
                                            Toast.makeText(getActivity(), "Failed to check in!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).executeAsync();
                            }
                        }
                    }
                } catch (JSONException q) {
                    // TODO Auto-generated catch block
                    //Toast.makeText(getApplicationContext(),q.getMessage(),Toast.LENGTH_SHORT).show();
                    q.printStackTrace();
                }


            }


        }).executeAsync();
    }




}
