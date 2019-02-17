/*This activity is only for test*/

package nf.co.rogerioaraujo.pdm_exercise_rss.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import nf.co.rogerioaraujo.pdm_exercise_rss.R;

public class TestThread extends AppCompatActivity {

    Thread t;
    int delay = 60000; // 1 minute in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_thread);

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
                                    TextView textView = findViewById(R.id.textView);
                                    textView.setText(cont + "change, after 1 minute.");
                                }
                                cont += 1;
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

}
