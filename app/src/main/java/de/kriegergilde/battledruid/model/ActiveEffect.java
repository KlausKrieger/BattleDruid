package de.kriegergilde.battledruid.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import de.kriegergilde.battledruid.A;
import de.kriegergilde.battledruid.R;
import de.kriegergilde.battledruid.common.Helper;
import de.kriegergilde.battledruid.enums.EffectType;
import de.kriegergilde.battledruid.enums.Element;

public class ActiveEffect {
	
    private long _id;
    private long lastsUntil = 0;
    private long remainingTicks = 0;
    private long nextTickAt = 0;
    private Element element;
    private EffectType effectType;
    private long effect;
    private String spellNameTec;
    
	private transient String spellNameTrans = null;
	
	private Combatant combatant;

    
	public static void onCreate(SQLiteDatabase db){
		// create table
		db.execSQL(
				"create table ActiveEffect (_id integer primary key autoincrement, combatantId integer not null references Combatant, "
				+ "lastsUntil integer not null, remainingTicks integer not null, nextTickAt integer not null, "
				+ "element text not null, effectType text not null, effect integer not null, "
				+ "spellNameTec text not null);");
		db.execSQL("CREATE INDEX fk_idx ON ActiveEffect(combatantId);");
	}
	public static void onUpgrade(Context context, SQLiteDatabase db, int oldVersion, int newVersion){
		ContentValues values = new ContentValues();
		long newId;
		if (oldVersion < 30){
			db.execSQL(
				"drop table if exists ActiveEffect");
			onCreate(db);
		}
	}
	

	public static ActiveEffect cursorToActiveEffect(Combatant combatant, Cursor cursor) {
		ActiveEffect aEffect = new ActiveEffect();
		aEffect.set_id(cursor.getLong(0));
		aEffect.setCombatant(combatant);
		combatant.getActiveEffects().add(aEffect);
		aEffect.setLastsUntil(cursor.getLong(2));
		aEffect.setRemainingTicks(cursor.getLong(3));
		aEffect.setNextTickAt(cursor.getLong(4));
		aEffect.setElement(Enum.valueOf(Element.class, cursor.getString(5)));
		aEffect.setEffectType(Enum.valueOf(EffectType.class, cursor.getString(6)));
		aEffect.setEffect(cursor.getLong(7));
		aEffect.setSpellNameTec(cursor.getString(8));
		return aEffect;
	}
	
	
	public static void update(SQLiteDatabase db, Collection<ActiveEffect> aEffects){
		for(ActiveEffect aEffect : aEffects){
			aEffect.update(db);
		}
	}
	public void update(SQLiteDatabase db){
		ContentValues values = convertEntity2Cv(this);
		String where = "_id=?";
		String[] whereArgs = {""+get_id()};
		db.update("ActiveEffect", values, where, whereArgs);
	}
	public static ActiveEffect create(SQLiteDatabase db,
									Combatant combatant,
									long lastsUntil, long remainingTicks, long nextTickAt, 
									Element element, EffectType effectType, long effect, 
									String spellNameTec){
		
		// create cv and insert
		ContentValues values = new ContentValues();
		values.put("combatantId", combatant.get_id());
		values.put("lastsUntil", lastsUntil);
		values.put("remainingTicks", remainingTicks);
		values.put("nextTickAt", nextTickAt);
		values.put("element", element.toString());
		values.put("effectType", effectType.toString());
		values.put("effect", effect);
		values.put("spellNameTec", spellNameTec);
		long newId = db.insert("ActiveEffect", null, values);
		assert(newId != -1);
		
		// convert to entitiy and return
		values.put("_id", newId);
		return convertCv2Entity(combatant, values);
	}
	public void delete(SQLiteDatabase db){
		// unlink
		combatant.getActiveEffects().remove(this);
		this.setCombatant(null);
		
		// delete on db
		String where = "_id=?";
		String[] whereArgs = {""+get_id()};
		db.delete("ActiveEffect", where, whereArgs);
	}
	private static ActiveEffect convertCv2Entity(Combatant combatant, ContentValues cv){
		ActiveEffect aEffect = new ActiveEffect();
		aEffect.set_id(cv.getAsLong("_id"));
		aEffect.setCombatant(combatant);
		combatant.getActiveEffects().add(aEffect);
		aEffect.setLastsUntil(cv.getAsLong("lastsUntil"));
		aEffect.setRemainingTicks(cv.getAsLong("remainingTicks"));
		aEffect.setNextTickAt(cv.getAsLong("nextTickAt"));
		aEffect.setElement(Enum.valueOf(Element.class, cv.getAsString("element")));
		aEffect.setEffectType(Enum.valueOf(EffectType.class, cv.getAsString("effectType")));
		aEffect.setEffect(cv.getAsLong("effect"));
		aEffect.setSpellNameTec(cv.getAsString("spellNameTec"));
		return aEffect;
	}
	private static ContentValues convertEntity2Cv(ActiveEffect aEffect){
		ContentValues values = new ContentValues();
		values.put("_id", aEffect.get_id());
		values.put("combatantId", aEffect.getCombatant().get_id());
		values.put("lastsUntil", aEffect.getLastsUntil());
		values.put("remainingTicks", aEffect.getRemainingTicks());
		values.put("nextTickAt", aEffect.getNextTickAt());
		values.put("element", aEffect.getElement().toString());
		values.put("effectType", aEffect.getEffectType().toString());
		values.put("effect", aEffect.getEffect());
		values.put("spellNameTec", aEffect.getSpellNameTec());
		return values;
	}
    
	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	public long getLastsUntil() {
		return lastsUntil;
	}

	public void setLastsUntil(long lastsUntil) {
		this.lastsUntil = lastsUntil;
	}

	public long getRemainingTicks() {
		return remainingTicks;
	}

	public void setRemainingTicks(long remainingTicks) {
		this.remainingTicks = remainingTicks;
	}

	public long getNextTickAt() {
		return nextTickAt;
	}

	public void setNextTickAt(long nextTickAt) {
		this.nextTickAt = nextTickAt;
	}

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}

	public EffectType getEffectType() {
		return effectType;
	}

	public void setEffectType(EffectType effectType) {
		this.effectType = effectType;
	}

	public long getEffect() {
		return effect;
	}

	public void setEffect(long effect) {
		this.effect = effect;
	}

	public String getSpellNameTec() {
		return spellNameTec;
	}

	public void setSpellNameTec(String spellNameTec) {
		this.spellNameTec = spellNameTec;
	}
	
	
	public String getSpellNameTrans() {
		if (spellNameTrans == null) {
			int id = A.getContext().getResources().getIdentifier(spellNameTec, "string", A.getContext().getPackageName());
			spellNameTrans = A.getContext().getResources().getString(id);
		}
		return spellNameTrans;
	}

	public Combatant getCombatant() {
		return combatant;
	}
	public void setCombatant(Combatant combatant) {
		this.combatant = combatant;
	}
	
	
	public String getDescription1(){
		return getSpellNameTrans();
	}
	public String getDescription2(){
		switch (effectType) {
		case RES_BUFF:
			return A.getContext().getString(R.string.active_effect_res_buff_line2 , 
					new Object[]{element.getText(), effect});
		case DOT:
			return A.getContext().getString(R.string.active_effect_dot_line2 , 
					new Object[]{element.getText(), effect});
		case DD:
			default:
				return null;
		}
		
		
	}
	public String getDescription3(){
		switch (effectType) {
		case RES_BUFF:
			return A.getContext().getString(R.string.duration)
			+ Helper.convertMillisToFormattedString(lastsUntil - System.currentTimeMillis());
		case DOT:
			return A.getContext().getString(R.string.ticks)
			+ remainingTicks;
		case DD:
			default:
				return null;
		}
	}
	

	
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ActiveEffect other = (ActiveEffect) obj;
        if (this._id != other._id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + (int) (this._id ^ (this._id >>> 32));
        return hash;
    }

    @Override
    public String toString() {
        return "ActiveEffect[ _id=" + _id + " ]";
    }
	
	
}
