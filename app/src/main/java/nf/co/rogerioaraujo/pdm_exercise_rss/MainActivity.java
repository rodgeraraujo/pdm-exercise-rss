package nf.co.rogerioaraujo.pdm_exercise_rss;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;

import nf.co.rogerioaraujo.pdm_exercise_rss.Adapter.FeedAdapter;
import nf.co.rogerioaraujo.pdm_exercise_rss.Common.HTTPDataHandle;
import nf.co.rogerioaraujo.pdm_exercise_rss.Model.Item;
import nf.co.rogerioaraujo.pdm_exercise_rss.Model.RSSObject;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    RSSObject rssObject;
    Handler handler;
    String lastUpdate = "2019-02-09T00:32:28Z";

    // RSS Link and Json API
    private final String RSS_link = "https://www.diariodosertao.com.br/feed/atom";
    private final String RSS_to_Json_API = "https://api.rss2json.com/v1/api.json?rss_url=";


    String msg = "News updates!";

    Thread thread = new Thread() {
        @Override
        public void run() {
            while (!isInterrupted()){
                try {
                    Thread.sleep(60000);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadRSS();
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("News");
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);

        loadRSS();


    }

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

            @Override
            protected void onPostExecute(String s) {
                mDialog.dismiss();
                rssObject = new Gson().fromJson(s, RSSObject.class);
                FeedAdapter adapter = new FeedAdapter(rssObject, getBaseContext());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                String nowUpdate = rssObject.getItems().get(0).pubDate;

                notification(nowUpdate);
            }
        };

        StringBuilder url_get_data = new StringBuilder(RSS_to_Json_API);
        url_get_data.append(RSS_link);
        loadRSSAsync.execute(url_get_data.toString());
    }

    private void notification(String nowUpdate) {
        if (nowUpdate != lastUpdate){
            String nTitle = "News RSS";
            String nSubject = "News...";
            String nBody = "You have a new notification!";

            NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notify=new Notification.Builder
                    (getApplicationContext()).setContentTitle(nTitle).setContentText(nBody).
                    setContentTitle(nSubject).setSmallIcon(R.drawable.ic_refresh).build();

            notify.flags |= Notification.FLAG_AUTO_CANCEL;
            notif.notify(0, notify);

        } else {
            lastUpdate = rssObject.getItems().get(0).pubDate;
        }
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

}
