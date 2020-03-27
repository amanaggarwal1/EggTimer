package com.amanaggarwal1.eggtimer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //Declaring views
    private SeekBar countDownSeekBar;
    private ImageView eggImage, finalEggImage;
    private TextView displayTime;
    private ImageButton startButton, deleteButton, plusOneButton;

    //Count down timer which controls the timer time set by user
    CountDownTimer countDownTimer;

    //Media player that will control times up tone
    private MediaPlayer mediaPlayer = new MediaPlayer();

    //Declaring and setting some constants
    final private int initialTime = 30, maximumTime = 600;
    private int timeSetByUser = -1;

    // boolean variable that will track if any timer is already present or not
    private boolean isTimerActive = false;

    //Function to start the timer
    private void startTimer(){

        plusOneButton.setEnabled(true); //Enabling plus one button
        plusOneButton.animate().alpha(1); //Setting opacity of enabled button to max

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
                isTimerActive = false; // Updating status of time activity
                playTimesUpRingtone(); // Play the times up tone
                startButton.setEnabled(false); // Disable start button
                startButton.animate().alpha(0.4f); // Lower the opacity of disabled button
                plusOneButton.setImageResource(R.drawable.ic_reset_image_button); // Setting plus one button image
            }
        }.start();
    }

    private void pauseTimer(){
        countDownTimer.cancel(); //Cancels the timer
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

    // Function to play the times up tone in loop
    private void playTimesUpRingtone(){
        mediaPlayer = MediaPlayer.create(this ,R.raw.times_up_ringtone);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
    }

    private  void increaseTimerDuration(){
        countDownTimer.cancel();
        int secondsLeft = countDownSeekBar.getProgress();
        if(secondsLeft > maximumTime - 60 ) countDownSeekBar.setMax(countDownSeekBar.getMax() + 60);
        //else
            secondsLeft += 60;

        finalEggImage.animate().alphaBy(timeSetByUser / (timeSetByUser + 60) ).setDuration(1000);
        updateTimer(secondsLeft);
        startTimer();
    }
    // Function to reset time to default configurations
    private void resetTimer(){
        mediaPlayer.stop(); //Stops the time up tone
        countDownTimer.cancel(); //Cancel the timer even if not finished by itself
        updateTimer(timeSetByUser); //Updates the timer to default configuration
        finalEggImage.animate().alpha(0).setDuration(1000); //Set visibility of final egg image back to zero
        startButton.setImageResource(R.drawable.ic_play_image_button); //Reset start button
        startButton.setEnabled(true); // Enabling play button;
        startButton.animate().alpha(1); //Set opacity of enabled button to max
        plusOneButton.setImageResource(R.drawable.ic_plus_one); //Reset plus one button
        plusOneButton.setEnabled(false); // Disable plus one button
        plusOneButton.animate().alpha(0.4f); // Lower the opacity of disabled button
        isTimerActive = false; // Updating the status of timer activity
    }

    // Function linked with button with id = "R.id.startButton"
    public void playButtonPressed(View view){
        Log.i("LOGCAT", "Button Pressed");

        //Check if a timer is currently active on not
        if(isTimerActive) {
            pauseTimer();
            startButton.setImageResource(R.drawable.ic_play_image_button);
            plusOneButton.setImageResource(R.drawable.ic_reset_image_button);
        }
        else{
            startTimer();
            startButton.setImageResource(R.drawable.ic_pause);
            plusOneButton.setImageResource(R.drawable.ic_plus_one);
        }

        //Update the timer run status
        isTimerActive ^= true;
    }

    public void deleteButtonPressed(View view) {
        countDownSeekBar.setEnabled(true);  //Disables the seek bar when the timer has reset
        mediaPlayer.stop(); //Stops the time up tone
        countDownTimer.cancel(); //Cancel the timer even if not finished by itself
        countDownSeekBar.setMax(maximumTime); //Resetting the seek bar maximum value at maximum time
        updateTimer(initialTime); //Updates the timer to default configuration
        finalEggImage.animate().alpha(0).setDuration(1000); //Set visibility of final egg image back to zero
        startButton.setImageResource(R.drawable.ic_play_image_button); //Reset start button
        startButton.setEnabled(true); // Enabling play button;
        startButton.animate().alpha(1); //Set opacity of enabled button to max
        plusOneButton.setImageResource(R.drawable.ic_plus_one); //Reset plus one button
        plusOneButton.setEnabled(false); // Disable +1 button
        plusOneButton.animate().alpha(0.4f); //Lower the opacity of disabled button
        isTimerActive = false; // Updating status of time activity
        timeSetByUser = -1; // Resetting variable
    }

    public void plusOneButtonPressed(View view){
        //Check if timer is active or not
        if(isTimerActive) increaseTimerDuration();
        else resetTimer();
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
        startButton = findViewById(R.id.startImageButton);
        deleteButton = findViewById(R.id.deleteImageButton);
        plusOneButton = findViewById(R.id.plusOneImageButton);

        //Setting initial parameters
        countDownSeekBar.setMax(maximumTime);
        countDownSeekBar.setProgress(initialTime);
        plusOneButton.setEnabled(false);
        plusOneButton.animate().alpha(0.4f);

        //When seek bar is altered by user
        countDownSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(timeSetByUser == -1) timeSetByUser = progress;
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
