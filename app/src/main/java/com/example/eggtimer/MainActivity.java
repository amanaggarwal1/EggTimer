package com.example.eggtimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //Declaring views
    private SeekBar countDownSeekBar;
    private ImageView eggImage, finalEggImage;
    private TextView displayTime;
    private ImageButton startButton;

    //Count down timer which controls the timer time set by user
    CountDownTimer countDownTimer;

    //Media player that will control times up tone
    private MediaPlayer mediaPlayer = new MediaPlayer();

    //Declaring and setting some constants
    final int initialTime = 30, maximumTime = 600;

    // boolean variable that will track if any timer is already present or not
    private boolean isTimerActive = false;

    //Function to start the timer
    private void startTimer(){

        //Updates egg image over a period of time
        finalEggImage.animate().alpha(1).setDuration(countDownSeekBar.getProgress() * 1000);

        //Disables the seek bar when a timer is started
        countDownSeekBar.setEnabled(false);

        countDownTimer = new CountDownTimer(countDownSeekBar.getProgress()*1000 + 100, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateTimer((int) millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                Log.i("LOGCAT", "Finished");
                playTimesUpRingtone();
            }
        }.start();
    }

    //Function to update views for timer
    private void updateTimer(int secondsLeft){
        int minutes = secondsLeft / 60; // Calculating minutes corresponding to progress
        int seconds = secondsLeft % 60; // Calculating seconds corresponding to progress

        //Check if "seconds" variable is a single digit number or not
        //and if yes then add a "zero" before it in display time
        if(seconds < 10) displayTime.setText(minutes + ":0" + seconds);
        else displayTime.setText(minutes + ":" + seconds);

        //Update the progress in timer seek bar
        countDownSeekBar.setProgress(secondsLeft);
    }

    // Function to reset time to default configurations
    private void resetTimer(){
        countDownSeekBar.setEnabled(true);  //Disables the seek bar when the timer has reset
        mediaPlayer.stop(); //Stops the time up tone
        countDownTimer.cancel(); //Cancel the timer even if not finished by itself
        updateTimer(initialTime); //Updates the timer to default configuration
        finalEggImage.animate().alpha(0).setDuration(1000); //Set visibility of final egg image back to zero
    }

    // Function to play the times up tone in loop
    private void playTimesUpRingtone(){
        mediaPlayer = MediaPlayer.create(this ,R.raw.times_up_ringtone);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
    }

    // Function linked with button with id = "R.id.startButton"
    public void triggerCountDown(View view){
        Log.i("LOGCAT", "Button Pressed");

        //Check if a timer is currently active on not
        if(isTimerActive) {
            resetTimer();
            startButton.setImageResource(R.drawable.ic_play_image_button);
        }
        else{
            startTimer();
            startButton.setImageResource(R.drawable.ic_reset_image_button);
        }

        //Update the timer run status
        isTimerActive ^= true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Linking views
        countDownSeekBar = findViewById(R.id.timerSeekBar);
        eggImage = findViewById(R.id.eggIV);
        finalEggImage = findViewById(R.id.finalEggIV);
        displayTime = findViewById(R.id.timerTV);
        startButton = findViewById(R.id.startButton);

        //Setting initial parameters
        countDownSeekBar.setMax(maximumTime);
        countDownSeekBar.setProgress(initialTime);

        //When seek bar is altered by user
        countDownSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateTimer(progress);
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
