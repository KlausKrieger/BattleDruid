package de.kriegergilde.battledruid.enums;

import de.kriegergilde.battledruid.A;
import de.kriegergilde.battledruid.R;
import de.kriegergilde.battledruid.R.drawable;
import android.util.Log;
import static de.kriegergilde.battledruid.common.Constants.TAG;

public enum EffectType {
    
    DD("direct_damage"), DOT("damage_over_time"), RES_BUFF("resistance"), MELEE("melee");
    
    private String text;
    
    private EffectType(String text){
        this.text = text;
    }
    
    public String getText(){
    	int id = A.getContext().getResources().getIdentifier(text, "string", A.getContext().getPackageName());
        return A.getContext().getResources().getString(id);
    }
    
    public int getDrawableId(){
    	switch (this) {
    	case DD:
    		return R.drawable.effect_type_dd;
    	case DOT:
    		return R.drawable.effect_type_dot;
    	case RES_BUFF:
    		return R.drawable.effect_type_res_buff;
    	case MELEE:
    		return R.drawable.effect_type_melee;
    	default:
    		Log.e(TAG, "enum not handled in EffectType.getDrawableId()");
    		return -1;
    	}
    }
    
}
