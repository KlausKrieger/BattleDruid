package de.kriegergilde.battledruid.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import de.kriegergilde.battledruid.A;
import de.kriegergilde.battledruid.R;
import de.kriegergilde.battledruid.common.Helper;
import de.kriegergilde.battledruid.common.Rules;
import de.kriegergilde.battledruid.enums.EffectType;
import de.kriegergilde.battledruid.enums.Element;

public class Spell {
	
	private static Map<Long, Spell> spellIdMap = null;
	private static Map<String, Spell> spellNameTecMap = null;
	
	private long _id;
	private String nameTec;
	private long energyCostBase;
	private long damageBase;
	private long resBase;
	private long durationBase;
	private EffectType effectType;
	private Element element;
	
	private transient String nameTrans = null;

	
	public static void onCreate(Context context, SQLiteDatabase db){
		
		// create table
		db.execSQL(
				"create table Spell (_id integer primary key autoincrement, "
				+ "nameTec text not null, energyCostBase integer not null, damageBase integer not null, "
				+ "resBase integer not null, durationBase integer not null, effectType text not null, "
				+ "element text not null);");

	}
	public static void onUpgrade(Context context, SQLiteDatabase db, int oldVersion, int newVersion){
		ContentValues values = new ContentValues();
		long newId;
		if (oldVersion < 30){
			db.execSQL(
				"drop table if exists Spell");
			onCreate(context, db);
		}
		if (oldVersion < 31){
			values.clear();
			values.put("nameTec", "spell_ignite");
			values.put("energyCostBase", 20);
			values.put("damageBase", 30);
			values.put("resBase", 0);
			values.put("durationBase", 0);
			values.put("effectType", EffectType.DOT.toString());
			values.put("element", Element.FIRE.toString());
			newId = db.insert("Spell", null, values);
			
			values.clear();
			values.put("spellId", newId);
			values.put("combatantId", 1L);
			values.put("skill", 0);
			db.insert("SpellSkill", null, values);
			
			values.clear();
			values.put("nameTec", "spell_freeze");
			values.put("energyCostBase", 20);
			values.put("damageBase", 30);
			values.put("resBase", 0);
			values.put("durationBase", 0);
			values.put("effectType", EffectType.DOT.toString());
			values.put("element", Element.ICE.toString());
			newId = db.insert("Spell", null, values);
			
			values.clear();
			values.put("spellId", newId);
			values.put("combatantId", 1L);
			values.put("skill", 0);
			db.insert("SpellSkill", null, values);
			
			values.clear();
			values.put("nameTec", "spell_insects");
			values.put("energyCostBase", 20);
			values.put("damageBase", 30);
			values.put("resBase", 0);
			values.put("durationBase", 0);
			values.put("effectType", EffectType.DOT.toString());
			values.put("element", Element.MATTER.toString());
			newId = db.insert("Spell", null, values);
			
			values.clear();
			values.put("spellId", newId);
			values.put("combatantId", 1L);
			values.put("skill", 0);
			db.insert("SpellSkill", null, values);
		}
		
		if (oldVersion < 32){
			db.execSQL(
			"update Spell set effectType='MELEE' where energyCostBase=0");
		}

	}

	public static void loadAll(SQLiteDatabase db) {
		spellIdMap = new HashMap<Long, Spell>();
		spellNameTecMap = new HashMap<String, Spell>();
		Cursor cursor = db.query("Spell", null, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Spell spell = cursorToSpell(cursor);
			spellIdMap.put(spell.get_id(), spell);
			spellNameTecMap.put(spell.getNameTec(), spell);
			cursor.moveToNext();
		}
		cursor.close();
	}
	
	public static Collection<Spell> getAllSpells(){
		return spellIdMap.values();
	}

	public static Spell getSpellBy_id(long _id){
		return spellIdMap.get(_id);
	}
	public static Spell getSpellByNameTec(String nameTec){
		Spell spell = spellNameTecMap.get(nameTec);
		if (spell == null){
			throw new RuntimeException("spell-name not found: " + nameTec);
		} else {
			return spell;
		}
	}

	private static Spell cursorToSpell(Cursor cursor) {
		Spell spell = new Spell();
		spell.set_id(cursor.getLong(0));
		spell.setNameTec(cursor.getString(1));
		spell.setEnergyCostBase(cursor.getLong(2));
		spell.setDamageBase(cursor.getLong(3));
		spell.setResBase(cursor.getLong(4));
		spell.setDurationBase(cursor.getLong(5));
		spell.setEffectType(Enum.valueOf(EffectType.class, cursor.getString(6)));
		spell.setElement(Enum.valueOf(Element.class, cursor.getString(7)));
		return spell;
	}
	
	public static void update(SQLiteDatabase db, List<Spell> spells){
		for(Spell spell : spells){
			spell.update(db);
		}
	}
	public void update(SQLiteDatabase db){
		ContentValues values = convertEntity2Cv(this);
		String where = "_id=?";
		String[] whereArgs = {""+get_id()};
		db.update("Spell", values, where, whereArgs);
	}

	public static Spell create(SQLiteDatabase db, String nameTec,
									long energyCostBase, long damageBase, long resBase, 
									long durationBase, EffectType effectType, Element element) {

		// create cv and insert
		ContentValues values = new ContentValues();
		values.put("nameTec", nameTec);
		values.put("energyCostBase", energyCostBase);
		values.put("damageBase", damageBase);
		values.put("resBase", resBase);
		values.put("durationBase", durationBase);
		values.put("effectType", effectType.toString());
		values.put("element", element.toString());
		long newId = db.insert("Spell", null, values);
		assert (newId != -1);

		// convert to entitiy and return
		values.put("_id", newId);
		return convertCv2Entity(values);
	}

	public void delete(SQLiteDatabase db) {
		// TODO Abhängige auch löschen
		// Methode derzeit noch nicht verwendet?
		
		String where = "_id=?";
		String[] whereArgs = { "" + get_id() };
		db.delete("Spell", where, whereArgs);
	}
	
	
	private static Spell convertCv2Entity(ContentValues cv){
		Spell spell = new Spell();
		spell.set_id(cv.getAsLong("_id"));
		spell.setNameTec(cv.getAsString("nameTec"));
		spell.setEnergyCostBase(cv.getAsLong("energyCostBase"));
		spell.setDamageBase(cv.getAsLong("damageBase"));
		spell.setResBase(cv.getAsLong("resBase"));
		spell.setDurationBase(cv.getAsLong("durationBase"));
		spell.setEffectType(Enum.valueOf(EffectType.class, cv.getAsString("effectType")));
		spell.setElement(Enum.valueOf(Element.class, cv.getAsString("element")));
		return spell;
	}
	private static ContentValues convertEntity2Cv(Spell spell){
		ContentValues values = new ContentValues();
		values.put("_id", spell.get_id());
		values.put("nameTec", spell.getNameTec());
		values.put("energyCostBase", spell.getEnergyCostBase());
		values.put("damageBase", spell.getDamageBase());
		values.put("resBase", spell.getResBase());
		values.put("durationBase", spell.getDurationBase());
		values.put("effectType", spell.getEffectType().toString());
		values.put("element", spell.getElement().toString());
		return values;
	}
	

	public long get_id() {
		return _id;
	}
	public void set_id(long _id) {
		this._id = _id;
	}
	public String getNameTec() {
		return nameTec;
	}
	public void setNameTec(String nameTec) {
		this.nameTec = nameTec;
	}
	public long getEnergyCostBase() {
		return energyCostBase;
	}
	public long getEnergyCostEffective(long skill){
		return Math.round(energyCostBase * Rules.getMultiplierForSkill(skill));
	}
	public void setEnergyCostBase(long energyCostBase) {
		this.energyCostBase = energyCostBase;
	}
	public long getDamageBase() {
		return damageBase;
	}
	public long getDamageEffective(long skill){
		return Math.round(damageBase * Rules.getMultiplierForSkill(skill));
	}
	public void setDamageBase(long damageBase) {
		this.damageBase = damageBase;
	}
	public long getResBase() {
		return resBase;
	}
	public long getResEffective(long skill){
		return Math.round(resBase * Rules.getMultiplierForSkill(skill));
	}
	public void setResBase(long resBase) {
		this.resBase = resBase;
	}
	public long getDurationBase() {
		return durationBase;
	}
	public long getDurationEffective(long skill){
		return Math.round(durationBase * Rules.getMultiplierForSkill(skill));
	}
	public void setDurationBase(long durationBase) {
		this.durationBase = durationBase;
	}
	public EffectType getEffectType() {
		return effectType;
	}
	public void setEffectType(EffectType effectType) {
		this.effectType = effectType;
	}
	public Element getElement() {
		return element;
	}
	public void setElement(Element element) {
		this.element = element;
	}


	
	public String getNameTrans() {
		if (nameTrans == null) {
			int id = A.getContext().getResources().getIdentifier(nameTec, "string", A.getContext().getPackageName());
			nameTrans = A.getContext().getResources().getString(id);
		}
		return nameTrans;
	}

	
	

	
	
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Spell other = (Spell) obj;
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
        return "Spell[ _id=" + _id + " ]";
    }
	
	
	
	

}
