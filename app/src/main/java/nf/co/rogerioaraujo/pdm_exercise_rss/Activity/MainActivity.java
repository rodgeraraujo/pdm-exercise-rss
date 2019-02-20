package nf.co.rogerioaraujo.pdm_exercise_rss.Activity;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Random;

import nf.co.rogerioaraujo.pdm_exercise_rss.Adapter.FeedAdapter;
import nf.co.rogerioaraujo.pdm_exercise_rss.Common.HTTPDataHandle;
import nf.co.rogerioaraujo.pdm_exercise_rss.Helper.DatabaseHelper;
import nf.co.rogerioaraujo.pdm_exercise_rss.Helper.NotificationHelper;
import nf.co.rogerioaraujo.pdm_exercise_rss.Model.RSSObject;
import nf.co.rogerioaraujo.pdm_exercise_rss.R;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    RSSObject rssObject;

    // RSS Link and Json API
    private final String RSS_link = "https://www.diariodosertao.com.br/feed/atom"; // feed from "Diário do Sertão"
    private final String RSS_to_Json_API = "https://api.rss2json.com/v1/api.json?rss_url=";

    // database helper
    DatabaseHelper databaseHelper;

    // notification helper
    NotificationHelper notificationHelper;

    // thread
    Thread t;
    int delay = 300000; // 5 minutes in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("News from Diário do Sertão");
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);

        databaseHelper = new DatabaseHelper(this);

        // update feed
        loadRSS();

        // every 5 min updates the timeline
        t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!t.isInterrupted()) {
                        Thread.sleep(delay);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int cont = 0;
                                if (cont >= 0) {
                                    loadRSS();
                                }
                                cont +=1;
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    // update the news o timeline on app
    private void loadRSS() {
        @SuppressLint("StaticFieldLeak") AsyncTask<String, String, String> loadRSSAsync
                = new AsyncTask<String, String, String>() {

            ProgressDialog mDialog = new ProgressDialog(MainActivity.this);

            @Override
            protected void onPreExecute() {
                mDialog.setMessage("Please wait...");
                mDialog.show();
            }

            @Override
            protected String doInBackground(String... params) {
                String result;
                HTTPDataHandle http = new HTTPDataHandle();
                result = http.GetHTTPData(params[0]);
                return result;

            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected void onPostExecute(String s) {
                mDialog.dismiss();
                rssObject = new Gson().fromJson(s, RSSObject.class);
                FeedAdapter adapter = new FeedAdapter(rssObject, getBaseContext());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();


                // verify data and notify user if necessary
                String last_date = rssObject.getItems().get(0).pubDate;
                AddData(last_date);
                Cursor data = databaseHelper.getAllData();
                data.moveToFirst();
                if (data.getCount() == 0) {
                    AddData(last_date);
                } else if (!data.getString(1).equals(last_date)) {
                    UpdateData(last_date);
                    NotifyUser();
                }
            }
        };

        StringBuilder url_get_data = new StringBuilder(RSS_to_Json_API);
        url_get_data.append(RSS_link);
        loadRSSAsync.execute(url_get_data.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_refresh)
            loadRSS();
        return true;
    }

    // method to notify user in tray
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void NotifyUser() {
        notificationHelper = new NotificationHelper(this);

        String nTitle = "RSS App";
        String nBody = "Something new is waiting for you.";
        Notification.Builder builder = notificationHelper.getRSSChannelNotification(nTitle, nBody);
        notificationHelper.getManager().notify(new Random().nextInt(), builder.build());
    }

    // method to add data on sqlite database
    public void AddData(String last_date) {
        boolean insertData = databaseHelper.insertData(last_date);

        if (insertData) {
            ToastMessage("Database succesfully updated!");
        } else ToastMessage("Something went wrong");
    }

    // method to updated data on sqlite database
    public void UpdateData(String last_date) {
        boolean isUpdated = databaseHelper.updateData("1", last_date);

        if (isUpdated == true) {
            ToastMessage("News are updated!");
        }
    }

    // method to show toast message
    private void ToastMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
