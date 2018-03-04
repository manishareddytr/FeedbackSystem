package com.example.navya.feedbacksystem;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Main3Activity extends AppCompatActivity {

    private String TAG = Main3Activity.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get contacts JSON
    private static String url = "http://123.176.47.87:3002/retrieveFeedbacks";

    ArrayList<HashMap<String, String>> feedbackList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        feedbackList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.list);

        new GetRecords().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetRecords extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Main3Activity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray feedbacks = jsonObj.getJSONArray("feedbacks");

                    // looping through All Contacts
                    for (int i = 0; i < feedbacks.length(); i++) {
                        JSONObject c = feedbacks.getJSONObject(i);

                        String name1 = c.getString("name");
                        String subject1 = c.getString("subject");
                        String feedback1 = c.getString("feedback`");
                        // tmp hash map for single contact
                        HashMap<String, String> record = new HashMap<>();

                        // adding each child node to HashMap key => value
                        if(!subject1.equals("") && !feedback1.equals("") && !subject1.equals("undefined") && !feedback1.equals("undefined")) {
                            record.put("name", name1);
                            record.put("subject", subject1);
                            record.put("feedback", feedback1);
                            feedbackList.add(record);
                        }
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
                    }
                });

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

             //Updating parsed JSON data into ListView

            ListAdapter adapter = new SimpleAdapter(Main3Activity.this, feedbackList, R.layout.list_item, new String[]{"name", "subject", "feedback"}, new int[]{R.id.name, R.id.subject, R.id.feedback});
            lv.setAdapter(adapter);
        }

    }
}