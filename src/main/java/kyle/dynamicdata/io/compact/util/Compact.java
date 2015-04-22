package kyle.dynamicdata.io.compact;


import kyle.dynamicdata.io.compact.engine.Action;
import kyle.dynamicdata.io.compact.engine.BoardView;
import kyle.dynamicdata.io.compact.engine.CollapseBoard;
import kyle.dynamicdata.io.compact.engine.Command;
import kyle.dynamicdata.io.compact.util.SystemUiHider;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;



/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class Compact extends Activity {
    BoardView boardView;
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_compact);

        AdView adView = new AdView(this);

        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(getString(R.string.banner_ad_unit_id));

        ((FrameLayout) findViewById(R.id.fView)).addView(adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        final View contentView = findViewById(R.id.fullscreen_content);


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);


        boardView = (BoardView) findViewById(R.id.surfaceView);
        boardView.setTextView(((TextView) findViewById(R.id.textView)));

        final Button[] buttons = {
                null,
                //(Button) findViewById(R.id.button),
                (Button) findViewById(R.id.button2),
                (Button) findViewById(R.id.button3)
        };


        new CollapseBoard(boardView, 8, 3, buttons, (TextView) findViewById(R.id.textView)).update(new Action(Command.RESTART));

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                boardView.draw();
            }
        };

        Handler handler = new Handler();

        handler.postDelayed(runnable,800);

    }

    @Override
    protected void onResume(){

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                boardView.draw();
            }
        };

        Handler handler = new Handler();

        handler.postDelayed(runnable,800);

        super.onResume();
    }

    @Override
    protected void onStart(){
//        boardView.draw();
        super.onStart();
    }
}
