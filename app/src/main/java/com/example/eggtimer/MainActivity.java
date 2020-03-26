package com.example.eggtimer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity<countDownSeekBar> extends AppCompatActivity {

    //Declaring views
    private SeekBar countDownSeekBar;
    private ImageView eggImage;
    private TextView displayTime;
    private Button startButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Linking views
        countDownSeekBar = findViewById(R.id.timerSeekBar);
        eggImage = findViewById(R.id.eggIV);
        displayTime = findViewById(R.id.timerTV);
        startButton = findViewById(R.id.startButton);

        //Setting initial parameters
        int initialTime = 30, maximumTime = 600;
        countDownSeekBar.setMax(maximumTime);
        countDownSeekBar.setProgress(initialTime);

        //When seek bar is altered by user
        countDownSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int minutes = progress / 60; // Calculating minutes corresponding to progress
                int seconds = progress % 60; // Calculating seconds corresponding to progress

                if(seconds < 10)
                    displayTime.setText(minutes + ":0" + seconds);
                else
                    displayTime.setText(minutes + ":" + seconds);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
