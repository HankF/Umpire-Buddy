package edu.umkc.hfridell.umpirebuddy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    static final private String TAG = "Umpire Buddy";

    AtBat currentBatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // The following will print to LogCat.
        Log.i(TAG, "Starting onCreate...");
        setContentView(R.layout.activity_main);

        View strikeButton = findViewById(R.id.strikeButton);
        strikeButton.setOnClickListener(this);

        View ballButton = findViewById(R.id.ballButton);
        ballButton.setOnClickListener(this);

        currentBatter = new AtBat();

        updateDisplay();
    }

    private void updateDisplay() {
        TextView ballCount = (TextView)findViewById(R.id.ballCount);
        ballCount.setText(Integer.toString(currentBatter.getBallCount()));

        TextView strikeCount = (TextView)findViewById(R.id.strikeCount);
        strikeCount.setText(Integer.toString(currentBatter.getStrikeCount()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.strikeButton:
                strikeButtonAction();
                break;
            case R.id.ballButton:
                ballButtonAction();
                break;
        }
    }

    protected void strikeButtonAction() {
        currentBatter.strike();
        if (currentBatter.getStrikeCount() >= 3) {
            batterOut(getString(R.string.strikeout));
        }
        updateDisplay();
    }
    protected void batterOut(String cause) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(cause);
        alert.setCancelable(false);
        alert.setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentBatter = new AtBat();
                updateDisplay();
            }
        });
        alert.show();
    }

    protected void ballButtonAction() {
        currentBatter.ball();
        if (currentBatter.getBallCount() >= 4) {
            walkBatter();
        }
        updateDisplay();
    }

    protected void walkBatter() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(getString(R.string.Walk));
        alert.setCancelable(false);
        alert.setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentBatter = new AtBat();
                updateDisplay();
            }
        });
        alert.show();
    }
}
