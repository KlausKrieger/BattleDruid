package de.kriegergilde.battledruid.model;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ListView;
import android.widget.Toast;
import de.kriegergilde.battledruid.A;
import de.kriegergilde.battledruid.R;
import de.kriegergilde.battledruid.common.Constants;
import de.kriegergilde.battledruid.common.Helper;
import de.kriegergilde.battledruid.common.Rules;
import de.kriegergilde.battledruid.enums.EffectType;
import de.kriegergilde.battledruid.enums.Element;

public class Combatant {
	
	private static Random rand = new Random();
	
	private long _id;
	
	private String name;

	private long energyMax;
	private long energyCurrent;
	private long energyReggedLast;
	
	private long healthMax;
	private long healthCurrent;
	private long healthReggedLast;
	
	private double combatTime;
	
	
	
	private List<SpellSkill> spellSkills = new LinkedList<SpellSkill>();
	private List<ActiveEffect> activeEffects = new LinkedList<ActiveEffect>();

	
	public static void onCreate(Context context, SQLiteDatabase db){
		
		// create table
		db.execSQL(
				"create table Combatant (_id integer primary key autoincrement, name text unique, "
				+ "energyMax integer not null, energyCurrent integer not null, energyReggedLast integer default 0, "
				+ "healthMax integer not null, healthCurrent integer not null, healthReggedLast integer default 0, "
				+ "combatTime real default -1);");
		
	}
	public static void onUpgrade(Context context, SQLiteDatabase db, int oldVersion, int newVersion){
		ContentValues values = new ContentValues();
		long newId;
		if (oldVersion < 30){
			db.execSQL(
				"drop table if exists Combatant");
			onCreate(context, db);
		}
		if (oldVersion < 35){
			db.execSQL(
			"alter table Combatant add column storyNumber integer default 0");
		}
		if (oldVersion < 36) {
			db.execSQL("create table CombatantOld (_id integer primary key autoincrement, name text unique, "
					+ "energyMax integer not null, energyCurrent integer not null, energyReggedLast integer default 0, "
					+ "healthMax integer not null, healthCurrent integer not null, healthReggedLast integer default 0, "
					+ "combatTime real default -1);");

			db.execSQL("INSERT INTO CombatantOld SELECT _id, name, energyMax, energyCurrent, energyReggedLast, "
					+ "healthMax, healthCurrent, healthReggedLast, combatTime FROM Combatant;");

			db.execSQL("DROP TABLE Combatant;");

			db.execSQL("create table Combatant (_id integer primary key autoincrement, name text unique, "
					+ "energyMax integer not null, energyCurrent integer not null, energyReggedLast integer default 0, "
					+ "healthMax integer not null, healthCurrent integer not null, healthReggedLast integer default 0, "
					+ "combatTime real default -1);");

			db.execSQL("INSERT INTO Combatant SELECT _id, name, energyMax, energyCurrent, energyReggedLast, "
					+ "healthMax, healthCurrent, healthReggedLast, combatTime FROM CombatantOld;");

			db.execSQL("DROP TABLE CombatantOld;");
		}
		if (oldVersion < 37){
			
			// create second enemy
			values.clear();
			values.put("name", "Ivy");
			values.put("energyMax", 110);
			values.put("energyCurrent", 110);
			values.put("healthMax", 100);
			values.put("healthCurrent", 100);
			newId = db.insert("Combatant", null, values);
			
			// create spell skill entries
			values.clear();
			values.put("spellId", Spell.getSpellByNameTec("spell_icebolt").get_id());
			values.put("combatantId", newId);
			values.put("skill", 5);
			db.insert("SpellSkill", null, values);
			
			values.clear();
			values.put("spellId", Spell.getSpellByNameTec("spell_icestaff").get_id());
			values.put("combatantId", newId);
			values.put("skill", 0);
			db.insert("SpellSkill", null, values);
			
			values.clear();
			values.put("spellId", Spell.getSpellByNameTec("spell_freeze").get_id());
			values.put("combatantId", newId);
			values.put("skill", 3);
			db.insert("SpellSkill", null, values);
			
			values.clear();
			values.put("spellId", Spell.getSpellByNameTec("spell_resist_cold").get_id());
			values.put("combatantId", newId);
			values.put("skill", 2);
			db.insert("SpellSkill", null, values);
			
		}
		
		if (oldVersion < 38){
			
			// create 3. enemy
			values.clear();
			values.put("name", "Mognor");
			values.put("energyMax", 115);
			values.put("energyCurrent", 115);
			values.put("healthMax", 100);
			values.put("healthCurrent", 100);
			newId = db.insert("Combatant", null, values);
			
			// create spell skill entries
			values.clear();
			values.put("spellId", Spell.getSpellByNameTec("spell_falling_rocks").get_id());
			values.put("combatantId", newId);
			values.put("skill", 6);
			db.insert("SpellSkill", null, values);
			
			values.clear();
			values.put("spellId", Spell.getSpellByNameTec("spell_matterstaff").get_id());
			values.put("combatantId", newId);
			values.put("skill", 0);
			db.insert("SpellSkill", null, values);
			
			values.clear();
			values.put("spellId", Spell.getSpellByNameTec("spell_insects").get_id());
			values.put("combatantId", newId);
			values.put("skill", 5);
			db.insert("SpellSkill", null, values);
			
			values.clear();
			values.put("spellId", Spell.getSpellByNameTec("spell_resist_matter").get_id());
			values.put("combatantId", newId);
			values.put("skill", 4);
			db.insert("SpellSkill", null, values);
			
			
			// create 4. enemy
			values.clear();
			values.put("name", "Tonkar");
			values.put("energyMax", 126);
			values.put("energyCurrent", 126);
			values.put("healthMax", 100);
			values.put("healthCurrent", 100);
			newId = db.insert("Combatant", null, values);
			
			// create spell skill entries
			values.clear();
			values.put("spellId", Spell.getSpellByNameTec("spell_falling_rocks").get_id());
			values.put("combatantId", newId);
			values.put("skill", 6);
			db.insert("SpellSkill", null, values);
			
			values.clear();
			values.put("spellId", Spell.getSpellByNameTec("spell_firearrow").get_id());
			values.put("combatantId", newId);
			values.put("skill", 6);
			db.insert("SpellSkill", null, values);
			
			values.clear();
			values.put("spellId", Spell.getSpellByNameTec("spell_matterstaff").get_id());
			values.put("combatantId", newId);
			values.put("skill", 0);
			db.insert("SpellSkill", null, values);
			
			values.clear();
			values.put("spellId", Spell.getSpellByNameTec("spell_firestaff").get_id());
			values.put("combatantId", newId);
			values.put("skill", 0);
			db.insert("SpellSkill", null, values);
			
			values.clear();
			values.put("spellId", Spell.getSpellByNameTec("spell_icestaff").get_id());
			values.put("combatantId", newId);
			values.put("skill", 0);
			db.insert("SpellSkill", null, values);
			
			values.clear();
			values.put("spellId", Spell.getSpellByNameTec("spell_insects").get_id());
			values.put("combatantId", newId);
			values.put("skill", 5);
			db.insert("SpellSkill", null, values);
			
			values.clear();
			values.put("spellId", Spell.getSpellByNameTec("spell_ignite").get_id());
			values.put("combatantId", newId);
			values.put("skill", 5);
			db.insert("SpellSkill", null, values);
			
			values.clear();
			values.put("spellId", Spell.getSpellByNameTec("spell_resist_heat").get_id());
			values.put("combatantId", newId);
			values.put("skill", 4);
			db.insert("SpellSkill", null, values);
			
		}
		
	}


	private static Combatant cursorToCombatant(Cursor cursor) {
		Combatant combatant = new Combatant();
		combatant.set_id(cursor.getLong(0));
		combatant.setName(cursor.getString(1));
		combatant.setEnergyMax(cursor.getLong(2));
		combatant.setEnergyCurrent(cursor.getLong(3));
		combatant.setEnergyReggedLast(cursor.getLong(4));
		combatant.setHealthMax(cursor.getLong(5));
		combatant.setHealthCurrent(cursor.getLong(6));
		combatant.setHealthReggedLast(cursor.getLong(7));
		combatant.setCombatTime(cursor.getDouble(8));
		return combatant;
	}
	
	public static void update(SQLiteDatabase db, List<Combatant> combatants){
		for(Combatant combatant : combatants){
			combatant.update(db);
		}
	}
	public void update(SQLiteDatabase db){
		// update self
		ContentValues values = convertEntity2Cv(this);
		String where = "_id=?";
		String[] whereArgs = {""+get_id()};
		db.update("Combatant", values, where, whereArgs);
		
		// update 'children'
		SpellSkill.update(db, getSpellSkills());
		ActiveEffect.update(db, getActiveEffects());
	}
	
	/**
	 * 
	 * @param db
	 * @param _id
	 * @return null if not found
	 */
	public static Combatant findById(SQLiteDatabase db, long _id) {

		Combatant combatant;
		Cursor cursor = db.query("Combatant", null, "_id=?",
				new String[] { String.valueOf(_id) }, null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			combatant = Combatant.cursorToCombatant(cursor);
			cursor.close();
		} else {
			return null; // not found
		}

		ActiveEffect activeEffect;
		cursor = db.query("ActiveEffect", null, "combatantId=?",
				new String[] { String.valueOf(_id) }, null, null, null, null);
		cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	activeEffect = ActiveEffect.cursorToActiveEffect(combatant, cursor);
	      cursor.moveToNext();
	    }
	    cursor.close();
	    
		SpellSkill spellSkill;
		cursor = db.query("SpellSkill", null, "combatantId=?",
				new String[] { String.valueOf(_id) }, null, null, null, null);
		cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	spellSkill = SpellSkill.cursorToSpellSkill(combatant, cursor);
	      cursor.moveToNext();
	    }
	    cursor.close();

		return combatant;

	}

	public static Combatant create(SQLiteDatabase db, String name,
									long energyMax, long energyCurrent, long energyReggedLast, 
									long healthMax, long healthCurrent, long healthReggedLast,
									double combatTime) {

		// create cv and insert
		ContentValues values = new ContentValues();
		values.put("name", name);
		values.put("energyMax", energyMax);
		values.put("energyCurrent", energyCurrent);
		values.put("energyReggedLast", energyReggedLast);
		values.put("healthMax", healthMax);
		values.put("healthCurrent", healthCurrent);
		values.put("healthReggedLast", healthReggedLast);
		values.put("combatTime", combatTime);
		long newId = db.insert("Combatant", null, values);
		assert (newId != -1);

		// convert to entitiy and return
		values.put("_id", newId);
		return convertCv2Entity(values);
	}

	public void delete(SQLiteDatabase db) {
		// TODO auch Abhängige löschen.
		// Methode derzeit noch nicht verwendet?
		
		String where = "_id=?";
		String[] whereArgs = { "" + get_id() };
		db.delete("Combatant", where, whereArgs);
	}
	
	
	private static Combatant convertCv2Entity(ContentValues cv){
		Combatant combatant = new Combatant();
		combatant.set_id(cv.getAsLong("_id"));
		combatant.setName(cv.getAsString("name"));
		combatant.setEnergyMax(cv.getAsLong("energyMax"));
		combatant.setEnergyCurrent(cv.getAsLong("energyCurrent"));
		combatant.setEnergyReggedLast(cv.getAsLong("energyReggedLast"));
		combatant.setHealthMax(cv.getAsLong("healthMax"));
		combatant.setHealthCurrent(cv.getAsLong("healthCurrent"));
		combatant.setHealthReggedLast(cv.getAsLong("healthReggedLast"));
		combatant.setCombatTime(cv.getAsDouble("combatTime"));
		return combatant;
	}
	private static ContentValues convertEntity2Cv(Combatant combatant){
		ContentValues values = new ContentValues();
		values.put("_id", combatant.get_id());
		values.put("name", combatant.getName());
		values.put("energyMax", combatant.getEnergyMax());
		values.put("energyCurrent", combatant.getEnergyCurrent());
		values.put("energyReggedLast", combatant.getEnergyReggedLast());
		values.put("healthMax", combatant.getHealthMax());
		values.put("healthCurrent", combatant.getHealthCurrent());
		values.put("healthReggedLast", combatant.getHealthReggedLast());
		values.put("combatTime", combatant.getCombatTime());
		return values;
	}
	

	public long get_id() {
		return _id;
	}
	public void set_id(long _id) {
		this._id = _id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getEnergyMax() {
		return energyMax;
	}
	public void setEnergyMax(long energyMax) {
		this.energyMax = energyMax;
	}
	public void incEnergyMax(){
		this.energyMax++;
	}
	public long getEnergyCurrent() {
		return energyCurrent;
	}
	public void setEnergyCurrent(long energyCurrent) {
		this.energyCurrent = energyCurrent;
	}
	public long getEnergyReggedLast() {
		return energyReggedLast;
	}
	public void setEnergyReggedLast(long energyReggedLast) {
		this.energyReggedLast = energyReggedLast;
	}
	public long getHealthMax() {
		return healthMax;
	}
	public void setHealthMax(long healthMax) {
		this.healthMax = healthMax;
	}
	public void incHealthMax(){
		this.healthMax++;
	}
	public long getHealthCurrent() {
		return healthCurrent;
	}
	public void setHealthCurrent(long healthCurrent) {
		this.healthCurrent = healthCurrent;
	}
	public long getHealthReggedLast() {
		return healthReggedLast;
	}
	public void setHealthReggedLast(long healthReggedLast) {
		this.healthReggedLast = healthReggedLast;
	}
	public double getCombatTime() {
		return combatTime;
	}
	public void setCombatTime(double combatTime) {
		this.combatTime = combatTime;
	}
	public List<SpellSkill> getSpellSkills() {
		return spellSkills;
	}
	public void setSpellSkills(List<SpellSkill> spellSkills) {
		this.spellSkills = spellSkills;
	}
	public List<ActiveEffect> getActiveEffects() {
		return activeEffects;
	}
	public void setActiveEffects(List<ActiveEffect> activeEffects) {
		this.activeEffects = activeEffects;
	}
	
	
	public long getResistanceAgainst(Element element){
		long sum = 0;
		for(ActiveEffect aEffect : activeEffects){
			if (EffectType.RES_BUFF == aEffect.getEffectType()
				&& element == aEffect.getElement()){
				sum += aEffect.getEffect();
			}
		}
		return sum;
	}
	
	
	public boolean isProtagonist(){
		return _id == 1;
	}
	
	
	public boolean isAlive(){
		return healthCurrent > 0;
	}
	
	public void chooseAndCastSpell(Context context, SQLiteDatabase db, Combatant enemy) {
		
		// if has res buff spell that isnt active and mana is sufficient: cast it
		for(SpellSkill spellSkill : spellSkills){
			if (spellSkill.getEffectType() == EffectType.RES_BUFF
			  && !hasActiveEffect(EffectType.RES_BUFF, spellSkill.getElement())
			  && energyCurrent >= spellSkill.getEnergyCostEffective()){
				castSpell(context, db, spellSkill, enemy);
				return;
			}
		}
		
		// if has DOT spell without cooldown and enough mana: cast it
		// TODO: dont cast if opponent has high resistance 
		for(SpellSkill spellSkill : spellSkills){
			if(spellSkill.getEffectType() == EffectType.DOT
			  && spellSkill.getCooldown() <= this.combatTime
			  && spellSkill.getEnergyCostEffective() <= this.getEnergyCurrent()){
				castSpell(context, db, spellSkill, enemy);
				return;
			}
		}
		
		// if has DD spell and mana  is sufficient: cast it
		List<SpellSkill> dds = new LinkedList<SpellSkill>();
		for(SpellSkill spellSkill : spellSkills){
			if (spellSkill.getEffectType() == EffectType.DD
			  && spellSkill.getEnergyCostEffective() <= this.getEnergyCurrent()){
				dds.add(spellSkill);
			}
		}
		if (!dds.isEmpty()){
			// randomly choose one:
			SpellSkill spellSkill = dds.get(rand.nextInt(dds.size()));
			castSpell(context, db, spellSkill, enemy);
			return;
		}

		
		// else use a melee attack 
		List<SpellSkill> melees = new LinkedList<SpellSkill>();
		for(SpellSkill spellSkill : spellSkills){
			if (spellSkill.getEffectType() == EffectType.MELEE){
				melees.add(spellSkill);
			}
		}
		if (!melees.isEmpty()){
			// randomly choose one:
			SpellSkill spellSkill = melees.get(rand.nextInt(melees.size()));
			castSpell(context, db, spellSkill, enemy);
			return;
		}
		
	}
	
	
	public boolean hasActiveEffect(EffectType effectType, Element element){
		for(ActiveEffect aEffect : activeEffects){
			if (aEffect.getEffectType() == effectType && aEffect.getElement() == element){
				return true; // found
			}
		}
		return false; // nor found
	}
	
	
	public void castSpell(Context context, SQLiteDatabase db, SpellSkill spellSkill, Combatant enemy) {
		Combatant caster = spellSkill.getCombatant();
		Spell spell = spellSkill.getSpell();
    	long skill = spellSkill.getSkill();
    	Resources res = context.getResources();
    	
    	// reduce energy
    	if (spell.getEnergyCostEffective(skill) > caster.getEnergyCurrent()){
    		Toast.makeText(context, R.string.out_of_energy, Toast.LENGTH_SHORT).show();
    		return;
    	} else {
    		caster.setEnergyCurrent(caster.getEnergyCurrent() - spell.getEnergyCostEffective(skill));
        	
    		Toast.makeText(context, 
        			String.format(res.getString(R.string.cast_message), 
        					new Object[]{caster.getName(), 
        								spell.getNameTrans()}) 
        					, Toast.LENGTH_SHORT).show();
    	}
    	
        
        // apply effect:
        switch (spell.getEffectType()){
        case DD:
        	if (caster.isDuringCombat()){
            	long damageInflicted = spellSkill.getSpell().getDamageEffective(skill) - enemy.getResistanceAgainst(spell.getElement());
            	damageInflicted = Math.max(0, damageInflicted);
            		enemy.setHealthCurrent(enemy.getHealthCurrent() - damageInflicted);
            	Toast.makeText(context, 
            			String.format(res.getString(R.string.dd_damage), 
            					new Object[]{enemy.getName(), 
            								damageInflicted, 
            								spell.getElement().getText()}) 
            					, Toast.LENGTH_SHORT).show();
        	}
        	break;
        case MELEE:
        	if (caster.isDuringCombat()){
            	long damageInflicted = spellSkill.getSpell().getDamageEffective(skill) - enemy.getResistanceAgainst(spell.getElement());
            	damageInflicted = Math.max(0, damageInflicted);
            		enemy.setHealthCurrent(enemy.getHealthCurrent() - damageInflicted);
            	Toast.makeText(context, 
            			String.format(res.getString(R.string.melee_damage), 
            					new Object[]{enemy.getName(), 
            								damageInflicted, 
            								spell.getElement().getText()}) 
            					, Toast.LENGTH_SHORT).show();
        	}
        	break;
        case DOT:
        	if (caster.isDuringCombat()){
        		ActiveEffect relevantEffect = ActiveEffect.create(db, // DB
        				enemy,
            			0L, // lastsUntil
            			Constants.DOT_TICKS,  // remainingTicks
            			0L, // nextTickAt // TODO umbauen wenn kampf nicht mehr abwechselnd ist
            			spell.getElement(), 
            			spell.getEffectType(), 
            			Math.round(spell.getDamageEffective(skill) / Constants.DOT_TICKS), // effect (per tick)
            			spell.getNameTec());
        		spellSkill.setCooldown((long)caster.getCombatTime() + Constants.DOT_TICKS);
        	}
        	break;
        case RES_BUFF:
        	
        	ActiveEffect relevantEffect = null;
        	
        	// effect with type and element is already active?
        	for(ActiveEffect aEffect : caster.getActiveEffects()){
        		if (aEffect.getElement() == spell.getElement()
        				&& aEffect.getEffectType() == spell.getEffectType()){
        			relevantEffect = aEffect;
        		}
        	}
        	
        	if (relevantEffect != null){
        		// old effect found. -> prolong effect:
        		relevantEffect.setEffect(spell.getResEffective(skill));
        		relevantEffect.setLastsUntil(relevantEffect.getLastsUntil() + spell.getDurationEffective(skill));
        	} else { // no old effect found. create new one:
        		relevantEffect = ActiveEffect.create(db, // DB
        				caster,
            			System.currentTimeMillis() + spell.getDurationEffective(skill), // lastsUntil
            			0L,  // remainingTicks
            			0L, // nextTickAt
            			spell.getElement(), 
            			spell.getEffectType(), 
            			spell.getResEffective(skill), 
            			spell.getNameTec());
        	}
        	// TODO: evtl repaint der resis auslösen?
        	break;
        default:
        	Toast.makeText(context, 
        				"enum not handled: " + spell.getEffectType() + " while applying effect", 
        				Toast.LENGTH_SHORT)
        		.show();
        }
        
        // inc xp and check skill up
        if (isProtagonist() && spell.getEnergyCostBase() != 0){
            if (spellSkill.getXp() < skill){
            	spellSkill.incXp();
            } else {
            	spellSkill.incSkill();
            	caster.incEnergyMax();
            	spellSkill.resetXp();
            	Toast.makeText(context, R.string.skill_gained, Toast.LENGTH_SHORT).show();
            }
        }
        

	}

	
	public boolean isDuringCombat(){
		return combatTime >= 0;
	}
	public void setDuringCombat(boolean duringCombat, SQLiteDatabase db){
		if(duringCombat){
			combatTime = 0;
			
			// reset cooldowns of spells // TODO hier entfernen, unten im else reicht.
			for(SpellSkill spellSkill : spellSkills){
				spellSkill.setCooldown(0);
			}
		} else {
			combatTime = -1;
			
			// clear DOTs
			List<ActiveEffect> effectsToRemove = new LinkedList<ActiveEffect>();
			for(ActiveEffect aEffect : activeEffects){
				if (aEffect.getEffectType() == EffectType.DOT){
					effectsToRemove.add(aEffect);
				}
			}
			for(ActiveEffect aEffect : effectsToRemove){
				aEffect.delete(db);
			}
			
			// reset cooldowns of spells
			for(SpellSkill spellSkill : spellSkills){
				spellSkill.setCooldown(0);
			}
		}
	}
	
	
	public void applyTicks(Context context, SQLiteDatabase db) {
		List<ActiveEffect> effectsToRemove = new LinkedList<ActiveEffect>();
		for(ActiveEffect aEffect : activeEffects){
			switch (aEffect.getEffectType()) {
			case DOT:
				long damageInflicted = aEffect.getEffect() - getResistanceAgainst(aEffect.getElement());
            	damageInflicted = Math.max(0, damageInflicted);
            	setHealthCurrent(getHealthCurrent() - damageInflicted);
            	Toast.makeText(context, 
            			String.format(context.getResources().getString(R.string.dot_damage), 
            					new Object[]{aEffect.getSpellNameTrans(),
            								damageInflicted,
            								aEffect.getElement().getText(),
            								getName()}) 
            					, Toast.LENGTH_SHORT).show();
            	aEffect.setRemainingTicks(aEffect.getRemainingTicks() - 1);
            	if (aEffect.getRemainingTicks() == 0){
            		effectsToRemove.add(aEffect);
            	}
				break;
			case DD:
			case MELEE:
			case RES_BUFF:
			default:
				; // nothing to do
			}
		}
		
		// remove outdated effects
		for(ActiveEffect aEffect : effectsToRemove){
			aEffect.delete(db);
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
        final Combatant other = (Combatant) obj;
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
        return "Combatant[ _id=" + _id + " ]";
    }
    
	

}
