/*This activity is only for test*/

package nf.co.rogerioaraujo.pdm_exercise_rss.Activity;

import android.app.Notification;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Random;

import nf.co.rogerioaraujo.pdm_exercise_rss.Helper.NotificationHelper;
import nf.co.rogerioaraujo.pdm_exercise_rss.R;

public class TestNotification extends AppCompatActivity {

    NotificationHelper nHelper;
    Button btnSend;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_notification);

        // notification from button click
        nHelper = new NotificationHelper(this);
        btnSend = findViewById(R.id.btnSend);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String nTitle = "RSS App";
                String nBody = "Something new is waiting for you.";
                Notification.Builder builder = nHelper.getRSSChannelNotification(nTitle, nBody);
                nHelper.getManager().notify(new Random().nextInt(), builder.build());
            }
        });

    }

}
