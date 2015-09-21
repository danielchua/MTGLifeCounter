package at.daniel.mtglifecounter;

import java.util.Random;

import Classes.MTGPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/*
 * TODO: 
 * - implement coin toss animation
 * - implement: light up screen onTouchEvent of buttons (currently working for non-button areas)
 * - multilingual texts
 */

public class LifeCounterActivity extends Activity {
	
	/* CONSTANTS */
	private static final int screenIlluminateTime = 3000; // screen flash up time "OnTouch" in milliseconds
	
	/* VARIABLES */
	private MTGPlayer playerOne, playerTwo; // Player One = Bottom Player; Player Two = Top Player
	private AlertDialog.Builder alertDialog; // Pop-up Dialog Builder
	private Random randGen; // Pseudo Random Number Generator
	private Handler mHandler;
	
	/* Buttons */
	private Button topPlPlusOne, topPlMinusOne, topPlPlusFive, topPlMinusFive; // "TopPlayerPlusOne"
	private Button botPlPlusOne, botPlMinusOne, botPlPlusFive, botPlMinusFive; // "BottomPlayerPlusOne"
	private ImageButton resetLife, tossCoin;
	/* Text Views */
	private TextView topPlLife;
	private TextView botPlLife;
	
	Runnable lifeCountUpdater = new Runnable() { // Updates life count TextView every few msecs 
	    @Override 
	    public void run() {
	    	topPlLife.setText(Integer.valueOf(playerTwo.getLife()).toString());
	    	botPlLife.setText(Integer.valueOf(playerOne.getLife()).toString());
	    	mHandler.postDelayed(lifeCountUpdater, 100); // msecs
	    }
	  };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_life_counter);
		
		playerOne = new MTGPlayer(this);
		playerTwo = new MTGPlayer(this);
		
		/* Connect UI-Elements to Variables */
		topPlPlusOne = (Button) findViewById(R.id.topPlPlusOne);
		topPlMinusOne = (Button) findViewById(R.id.topPlMinusOne);
		topPlPlusFive = (Button) findViewById(R.id.topPlPlusFive);
		topPlMinusFive = (Button) findViewById(R.id.topPlMinusFive);
		
		botPlPlusOne = (Button) findViewById(R.id.botPlPlusOne);
		botPlMinusOne = (Button) findViewById(R.id.botPlMinusOne);
		botPlPlusFive = (Button) findViewById(R.id.botPlPlusFive);
		botPlMinusFive = (Button) findViewById(R.id.botPlMinusFive);
		
		resetLife = (ImageButton) findViewById(R.id.resetLife);
		tossCoin = (ImageButton) findViewById(R.id.flipCoin);
		
		topPlLife = (TextView) findViewById(R.id.topPlLife);
		botPlLife = (TextView) findViewById(R.id.botPlLife);
		
		randGen = new Random(); 
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // stops lockscreen
		setScrnBrightness(WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE);
		
		/* Click Actions */
		// reset life/toss coin dialog
		resetLife.setOnClickListener(new OnClickListener() {
            public void onClick(View v) { 
        		alertDialog = new AlertDialog.Builder(LifeCounterActivity.this);
            	alertDialog.setTitle(R.string.resetLifeDialog);
        		alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                       // Reset Scores
                    	playerOne.resetLife();
                    	playerTwo.resetLife();
                    }
                }
        		);
        		alertDialog.setNegativeButton("Cancel", null);
        		alertDialog.show(); 
        		}
        });
		tossCoin.setOnClickListener(new OnClickListener() {
            public void onClick(View v) { 
        		alertDialog = new AlertDialog.Builder(LifeCounterActivity.this);
            	alertDialog.setIcon(0);
            	alertDialog.setCancelable(true); // tap to make the dialog disappear
        		alertDialog.setTitle(" "); // required to display arrow in a symmetric way
            	if(randGen.nextBoolean() == true){
            		alertDialog.setIcon(getResources().getDrawable(R.drawable.arrowup));
            	}else{
            		alertDialog.setIcon(getResources().getDrawable(R.drawable.arrowdown));
            	}
            	alertDialog.show();
            }
        });
		// top player / player two
		topPlPlusOne.setOnClickListener(new OnClickListener() {
            public void onClick(View v) { playerTwo.gainLife(1);}
        });
		topPlMinusOne.setOnClickListener(new OnClickListener() {
            public void onClick(View v) { playerTwo.loseLife(1); }
        });
		topPlPlusFive.setOnClickListener(new OnClickListener() {
            public void onClick(View v) { playerTwo.gainLife(5); }
        });
		topPlMinusFive.setOnClickListener(new OnClickListener() {
            public void onClick(View v) { playerTwo.loseLife(5); }
        });
		// bottom player / player one
		botPlPlusOne.setOnClickListener(new OnClickListener() {
            public void onClick(View v) { playerOne.gainLife(1); }
        });
		botPlMinusOne.setOnClickListener(new OnClickListener() {
            public void onClick(View v) { playerOne.loseLife(1); }
        });
		botPlPlusFive.setOnClickListener(new OnClickListener() {
            public void onClick(View v) { playerOne.gainLife(5); }
        });
		botPlMinusFive.setOnClickListener(new OnClickListener() {
            public void onClick(View v) { playerOne.loseLife(5); }
        });
		
		mHandler = new Handler();
		lifeCountUpdater.run();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent motEvent) { // onTouchEvent of Non-Button Areas: Illuminate Screen 
		    Thread t = new Thread(){
				@Override
		    	public void run(){ // illuminate screen for a short period of time
					setScrnBrightness(WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL);
			        try {
			            Thread.sleep(screenIlluminateTime);
			        } catch (InterruptedException e) {
			            e.printStackTrace();
			        }
					setScrnBrightness(WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE);
				}
		    };
		    t.start();
	    return true;
	  }
	
	public void setScrnBrightness(final float brightness){ // change Screen brightness (WindowManager.LayoutParams)
		runOnUiThread(new Runnable() {// This code will always run on the UI thread, therefore is safe to modify UI elements.
            @Override
            public void run() {
        		WindowManager.LayoutParams layoutParams;
        		layoutParams = getWindow().getAttributes(); 
        		layoutParams.screenBrightness = brightness;
                getWindow().setAttributes(layoutParams);
            }
        });
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.life_counter, menu);
		return true;
	}

}
