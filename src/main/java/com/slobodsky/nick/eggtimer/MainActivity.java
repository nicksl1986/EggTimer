package com.slobodsky.nick.eggtimer;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText etTime;

    Button button;

    boolean started;

    CountDownTimer countdownTimer;

    static final Handler handler = new Handler();

    MediaPlayer mp = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        etTime = findViewById(R.id.etTime);

        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (started)
                {
                    started = false;

                    button.setText(R.string.start);

                    etTime.setCursorVisible(true);

                    countdownTimer.cancel();
                }
                else
                {
                    started = true;

                    button.setText(R.string.stop);

                    etTime.setCursorVisible(false);

                    String rawTime = etTime.getText().toString();

                    String[] tmp = rawTime.split(":");

                    long time = 60 * 1000;

                    try
                    {
                        time = (Integer.parseInt(tmp[0]) * 60 + Integer.parseInt(tmp[1])) * 1000;
                    }
                    catch (Exception e)
                    {
                        etTime.setText(R.string.default_time);
                    }

                    countdownTimer = new CountDownTimer(time, 100) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                            long remainingSeconds = millisUntilFinished / 1000;

                            long minutes = remainingSeconds / 60;

                            long seconds = remainingSeconds % 60;

                            etTime.setText(minutes + ":" + (seconds < 10 ? "0" + seconds : seconds)
                            );
                        }

                        @Override
                        public void onFinish() {

                            etTime.setText(R.string.start_time);

                            button.setText(R.string.start);

                            new AlertDialog.Builder(MainActivity.this).
                                    setTitle(R.string.app_name).setMessage(R.string.lets_eat)
                                    .show();

                            mp = MediaPlayer.create(MainActivity.this, R.raw.electric_tine);

                            mp.start();

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    etTime.setText(R.string.default_time);
                                }
                            }, 2000);
                        }
                    };

                    countdownTimer.start();
                }
            }
        });
    }
}
