package com.tgt.ludo.board;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g3d.ModelInstance;

/**
 * Use this only by the Human player - the other types of playes can just
 * use an Integer since they dont need to see the actual dice mesh (modelInstance) 
 * @author z062260
 *
 */
public class Dice {
   private Map<Integer,ModelInstance> diceInstanceMap = new HashMap<Integer, ModelInstance>();
   int diceValue = 1;
   //is this rolled or waiting to be rolled by the player
   boolean rolled=false;
   boolean shake = false;
  
public ModelInstance getDiceInstance() {
		return diceInstanceMap.get(diceValue);
}



public int getDiceValue() {
	return diceValue;
}

public boolean isRolled() {
	return rolled;
}

public void setDiceValue(int diceValue) {
	this.diceValue = diceValue;
}

public void setRolled(boolean rolled) {
	this.rolled = rolled;
}

public boolean isShake() {
	return shake;
}

public void setShake(boolean shake) {
	this.shake = shake;
}

   public void setDiceInstanceMap(Map<Integer, ModelInstance> diceInstanceMap) {
	this.diceInstanceMap = diceInstanceMap;
}



@Override
	protected void finalize() throws Throwable {
		super.finalize();
		//diceInstance.model.dispose();
	}
   
}
