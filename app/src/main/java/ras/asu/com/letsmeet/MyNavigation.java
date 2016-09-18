package ras.asu.com.letsmeet;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MyNavigation extends Service {
    public MyNavigation() {
    }
String fbId;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startid) {
        try {

            String myLat = intent.getStringExtra("myLat");
            String myLong = intent.getStringExtra("myLong");
            String pLat = intent.getStringExtra("pLat");
            String pLong = intent.getStringExtra("pLong");
            fbId = intent.getStringExtra("fbId");
            new DownloadTask().execute(getDirectionsUrl(myLat, myLong, pLat, pLong));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return 0;
    }
    private String getDirectionsUrl(String mLat,String mLong,String pLat,String pLong){
        String str_origin = "origin="+mLat+","+mLong;
        String str_dest = "destination="+pLat+","+pLong;
        String sensor = "sensor=false";
        String parameters = str_origin+"&"+str_dest+"&"+sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
        return url;}
    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb  = new StringBuffer();
            String line = "";
            while( ( line = br.readLine())!= null){
                sb.append(line);}

            data = sb.toString();

            br.close();

        }catch(Exception e){
            e.printStackTrace();
           // Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
e.printStackTrace();            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration = "";

            if(result.size()<1){
                Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    if(j==0){    // Get distance from the list
                        distance = (String)point.get("distance");
                        continue;
                    }else if(j==1){ // Get duration from the list
                        duration = (String)point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
break;
                }

            }

         //   tvDistanceDuration.setText("Distance:"+distance + ", Duration:"+duration);

            Toast.makeText(getBaseContext(),"Distance:"+distance + ", Duration:"+duration, Toast.LENGTH_SHORT).show();
            sendInvite(fbId, distance, duration);
           // duration.split()
           dura=duration;
        }
    }
    public static String dura="5 minute";
    private void sendInvite(String fbId,String dist,String time) {
        Map<String , String > jobj = new HashMap<String,String>();
        jobj.put("message",ProjCostants.INVITE_TOKEN);
        jobj.put("fbId",fbId);
        jobj.put("dist",dist);
        jobj.put("time",time);
        jobj.put("action", "locUpdates");
        jobj.put("myFbId", ProjCostants.FB_ID);
        StoreUserDataAsyncTask asyn = new StoreUserDataAsyncTask(jobj);
        asyn.execute();
    }

    public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, Void> {
        Map <String,String> paramsMap= new HashMap<String,String>();

        public StoreUserDataAsyncTask(Map <String,String> p) {
            paramsMap = p;
            // this.userCallBack = userCallBack;
        }

        @Override
        protected Void doInBackground(Void... params) {
            String result = "";

            URL serverUrl = null;
            try {
                serverUrl = new URL(ProjCostants.SERVER_ADDRESS);
                Log.d("URL PROBLEM", serverUrl.toString());
            } catch (MalformedURLException e) {
                Log.e("AppUtil", "URL Connection Error: "
                        + ProjCostants.SERVER_ADDRESS, e);
                result = ProjCostants.SERVER_ADDRESS;
            }

            StringBuilder postBody = new StringBuilder();
            Iterator<Map.Entry<String, String>> iterator = paramsMap.entrySet()
                    .iterator();

            while (iterator.hasNext()) {
                Map.Entry<String, String> param = iterator.next();
                postBody.append(param.getKey()).append('=')
                        .append(param.getValue());
                if (iterator.hasNext()) {
                    postBody.append('&');
                }
            }
            String body = postBody.toString();
            byte[] bytes = body.getBytes();
            HttpURLConnection httpCon = null;
            try {
                httpCon = (HttpURLConnection) serverUrl.openConnection();
                httpCon.setDoOutput(true);
                httpCon.setUseCaches(false);
                httpCon.setFixedLengthStreamingMode(bytes.length);
                httpCon.setRequestMethod("GET");
                httpCon.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded;charset=UTF-8");
                OutputStream out = httpCon.getOutputStream();
                out.write(bytes);
                out.close();

                int status = httpCon.getResponseCode();
                if (status == 200) {
                    result =  "Success";
                } else {
                    result = "Post Failure." + " Status: " + status;
                }
            }
            catch (Exception e){
                Log.d("SOME ERROR", e.toString());
                httpCon.disconnect();
            }
            return null;


            //return null;
        }



        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

    }

}
