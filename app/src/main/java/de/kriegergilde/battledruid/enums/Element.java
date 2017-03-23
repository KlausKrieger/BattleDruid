package de.kriegergilde.battledruid.enums;

import de.kriegergilde.battledruid.A;
import android.graphics.Color;

public enum Element {
    
    FIRE("fire", Color.RED), ICE("ice", Color.BLUE), MATTER("matter", Color.GREEN);
    
    private String text;
    private int color;
    
    private Element(String text, int color){
        this.text = text;
        this.color = color;
    }
    
    public String getText(){
    	int id = A.getContext().getResources().getIdentifier(text, "string", A.getContext().getPackageName());
        return A.getContext().getResources().getString(id);
    }
    
    public int getColor(){
    	return color;
    }
    
}
