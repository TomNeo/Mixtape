package com.heinousgames.game.shantelsmixtape.activities;

import android.content.Intent;
import android.content.SharedPreferences;

import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.SimpleBaseGameActivity;

/**
 * Created by shanus on 1/9/15.
 */
public class SelfDoubtActivity extends SimpleBaseGameActivity {

    SharedPreferences myPrefs;

    @Override
    protected void onCreateResources() {
        myPrefs = getSharedPreferences("levelCompletion", MODE_PRIVATE);
    }

    /**
     * Base Game Plan:
     * Simple tap screen anywhere to jump obstacle game. Obstacles can consist of pitfalls,
     * negative words, and possibly other dangers. Possibly use an entire sentence word for word
     * from that particular song to get that point across. Could spell out a sentence and at parts
     * with positive words have negative words be obstacles that you must jump over. They would
     * line up vertically with the positive words above them, this way the player must jump at
     * the right time to avoid the negative word from filling in the blank and to get the positive
     * word to fill in the blank. If can't find negative words, use spikes or pitfalls as usual.
     * Could be predetermined amount of spike hits before death. When you hit a spike, or just a
     * wall even, it should break apart and disappear (like walls from sonic?). 
     * @return
     */
    @Override
    protected Scene onCreateScene() {

        // when game is over, save the data in the shared pref, finish the activity, and
        // start the main activity again

        //if won
        myPrefs.edit().putBoolean("leftFinished", true).commit();
        // else
        myPrefs.edit().putBoolean("leftFinished", false).commit();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        return null;
    }

    @Override
    public EngineOptions onCreateEngineOptions() {



        return null;
    }
}
