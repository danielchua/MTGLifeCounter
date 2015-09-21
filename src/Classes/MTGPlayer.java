package Classes;

import android.content.Context;
import at.daniel.mtglifecounter.R;

/* 
 * TODO: 
 * - implement poison count
 * - implement 2v2 two-headed giant mode
 */

public class MTGPlayer {
	
	/* CONSTANTS */
	
	/* VARIABLES */
	private Context context; // context to access Activity resources
	private int life;
	private int startingLife; 
	
	/* PUBLIC METHODS */
	
	public MTGPlayer(Context context){
		this.context = context;
		this.startingLife = context.getResources().getInteger(R.integer.startingLife1v1);
		this.life = startingLife;
	}
	
	public int getLife(){ /* returns player's life amount */
		return this.life;
	}
		
	public void gainLife(int amount){ /* player gains life */
		this.setLife(this.getLife()+amount);
	}
	
	public void loseLife(int amount){ /* player loses life */
		this.setLife(this.getLife()-amount);
	}
	
	public void resetLife(){ /* reset player's life count */
		this.setLife(startingLife);
	}
	
	/* PRIVATE METHODS */
	
	private void setLife(int newLife){ /* sets player's life count */
		this.life = newLife;
	}
	
	private boolean hasLost(){ /* checks if the player has lost */
		if(this.getLife() <= 0){
			return true;
		}else{
			return false;
		}
	}
	
	/* TEST/DEBUG METHODS */
	
	public String toString(){
		return new String("Life: "+Integer.valueOf(this.life).toString());
	}
	
}
