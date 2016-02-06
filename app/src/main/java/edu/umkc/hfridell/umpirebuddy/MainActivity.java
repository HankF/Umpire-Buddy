package edu.umkc.hfridell.umpirebuddy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    public final static String EXTRA_DATA = "Thank you for using Umpire Buddy";
    static final private String TAG = "Umpire Buddy";

    private SharedPreferences mPrefs;

    AtBat currentBatter;
    private int totalOuts;

    // Contextual action mode menu
    private ActionMode mActionMode;
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback(){
        //Called when started

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);
            return true;
        }

        //Called when actionMenu is shown

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // false if nothing done
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.contextStrike:
                    strikeButtonAction();

                    mode.finish(); // closes contextual actionBar
                    return true;
                case R.id.contextBall:
                    ballButtonAction();

                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        // Called when user exits action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // The following will print to LogCat.
        Log.i(TAG, "Starting onCreate...");
        setContentView(R.layout.activity_main);


        //Restore state
        mPrefs = getPreferences(MODE_PRIVATE);
        totalOuts = mPrefs.getInt("totalOuts", 0);
        currentBatter = new AtBat(mPrefs.getInt("strikeCount", 0)
                , mPrefs.getInt("ballCount", 0));


        // Setup contextual action mode menu (CAB)
        // Long clicking background of main activity will
        // bring up a contextual menu.
        GridLayout layout = (GridLayout)findViewById(R.id.grid_layout);
        layout.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View view) {
                // mActionMode is set back to null
                //    above when the context menu disappears.
                if (mActionMode != null) {
                    return false;
                }

                // Start the CAB using the ActionMode.Callback defined above
                mActionMode = startActionMode(mActionModeCallback);
                view.setSelected(true);
                return true;
            }
        });

        View strikeButton = findViewById(R.id.strikeButton);
        strikeButton.setOnClickListener(this);

        View ballButton = findViewById(R.id.ballButton);
        ballButton.setOnClickListener(this);


        updateDisplay();
    }

    private void updateDisplay() {
        TextView ballCount = (TextView)findViewById(R.id.ballCount);
        ballCount.setText(Integer.toString(currentBatter.getBallCount()));

        TextView strikeCount = (TextView)findViewById(R.id.strikeCount);
        strikeCount.setText(Integer.toString(currentBatter.getStrikeCount()));

        TextView outs = (TextView)findViewById(R.id.outs_number);
        outs.setText(Integer.toString(totalOuts));
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
                totalOuts += 1;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu;  add items to the action bar
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.

        switch (item.getItemId()) {
            case R.id.reset:
                currentBatter = new AtBat();
                totalOuts = 0;
                updateDisplay();
                return true;

            case R.id.about:
                Intent intent = new Intent(this, AboutActivity.class);
                intent.putExtra(EXTRA_DATA, "Thank You for using Umpire Buddy");
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    protected void onPause() {
        super.onPause();



        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putInt("totalOuts", totalOuts);
        ed.putInt("strikeCount", currentBatter.getStrikeCount());
        ed.putInt("ballCount", currentBatter.getBallCount());
        ed.apply();
    }

}


