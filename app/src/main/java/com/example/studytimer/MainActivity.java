package com.example.studytimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Chronometer timerChronometer;
    boolean running;
    long pausedOffSet;

    TextView previousTimer;
    EditText inputType;
    SharedPreferences sharedPreferences;

    String Base = "Base";
    String Pause = "Pause";
    String previous_Timer = "previous_Timer";
    String timer = "Timer";
    String TimerRun = "TimerRun";

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.playButton:
                if (!running) {
                    timerChronometer.setBase(SystemClock.elapsedRealtime() - pausedOffSet);
                    timerChronometer.start();
                    running = true;
                }
                break;
            case R.id.pauseButton:
                if (running) {
                    timerChronometer.stop();
                    pausedOffSet = SystemClock.elapsedRealtime() - timerChronometer.getBase();
                    running = false;
                }
                break;
            case R.id.recordButton:
                CharSequence time = timerChronometer.getText();
                if (inputType.getText().toString().equals("")) {
                    previousTimer.setText("You spent " + time.toString() + " on last time.");
                }
                else {
                    previousTimer.setText("You spent " + time.toString() + " on " + inputType.getText().toString() + " last time.");
                }
                timerChronometer.stop();
                timerChronometer.setBase(SystemClock.elapsedRealtime());
                pausedOffSet = 0;
                running = false;


                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(previous_Timer, previousTimer.getText().toString());
                editor.apply();
                break;

        }
    }



    private void loadSharedPreferences() {
        String text = sharedPreferences.getString(previous_Timer, "previous_Timer");
        previousTimer.setText(text);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerChronometer = (Chronometer)findViewById(R.id.timerChronometer);
        previousTimer = findViewById(R.id.lastTimer);
        inputType = findViewById(R.id.inputType);
        pausedOffSet = SystemClock.elapsedRealtime() - timerChronometer.getBase();

        sharedPreferences = getSharedPreferences("com.example.studytimer", MODE_PRIVATE);
        loadSharedPreferences();

        if (savedInstanceState != null) {
            previousTimer.setText(savedInstanceState.getString(timer));
            pausedOffSet = savedInstanceState.getLong(Pause);
            timerChronometer.setBase(SystemClock.elapsedRealtime() + savedInstanceState.getLong(Base));
            if (savedInstanceState.getBoolean(TimerRun)) {
                timerChronometer.start();
                running = true;
            }
            else {
                timerChronometer.setBase(SystemClock.elapsedRealtime() - pausedOffSet);
            }
        }
        else {
            running = false;
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(timer, previousTimer.getText().toString());
        outState.putLong(Base, timerChronometer.getBase() - SystemClock.elapsedRealtime());
        outState.putBoolean(TimerRun, running);
        outState.putLong(Pause, pausedOffSet);
    }

}