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
import de.kriegergilde.battledruid.common.Rules;
import de.kriegergilde.battledruid.enums.EffectType;
import de.kriegergilde.battledruid.enums.Element;

public class SpellSkill {
	
    private long _id;
    
	private Spell spell;
	private Combatant combatant;
    
	private long skill;
	private long xp;
	
	/**
	 * nr of combat turn until this spell (skill) may not be recast.
	 * if combatTurn >= cooldown, the spell(skill) may be cast again.
	 */
	private long cooldown;
	

    
	public static void onCreate(SQLiteDatabase db){
		// create table
		db.execSQL(
				"create table SpellSkill (_id integer primary key autoincrement"
				+ ", spellId integer not null references Spell, combatantId integer not null references Combatant"
				+ ", skill integer not null, xp integer default 0, cooldown integer default 0);");
		db.execSQL("CREATE INDEX fk2_idx ON SpellSkill(combatantId);");
	}
	public static void onUpgrade(Context context, SQLiteDatabase db, int oldVersion, int newVersion){
		ContentValues values = new ContentValues();
		long newId;
		if (oldVersion < 30){
			db.execSQL(
				"drop table if exists SpellSkill");
			onCreate(db);
		}
		if (oldVersion < 33){
			values.clear();
			values.put("spellId", Spell.getSpellByNameTec("spell_ignite").get_id());
			values.put("combatantId", 2);
			values.put("skill", 1);
			db.insert("SpellSkill", null, values);
			
			db.execSQL(
			"update Combatant set energyMax=104, energyCurrent=104 where name='Fred'");
		}
		if (oldVersion < 34){
			db.execSQL(
			"alter table SpellSkill add column cooldown integer default 0");
		}
	}
	

	public static SpellSkill cursorToSpellSkill(Combatant combatant, Cursor cursor) {
		SpellSkill spellSkill = new SpellSkill();
		spellSkill.set_id(cursor.getLong(0));
		spellSkill.setSpell(Spell.getSpellBy_id(cursor.getLong(1)));
		spellSkill.setCombatant(combatant);
		combatant.getSpellSkills().add(spellSkill);
		spellSkill.setSkill(cursor.getLong(3));
		spellSkill.setXp(cursor.getLong(4));
		spellSkill.setCooldown(cursor.getLong(5));
		return spellSkill;
	}
	
	
	public static void update(SQLiteDatabase db, Collection<SpellSkill> aEffects){
		for(SpellSkill aEffect : aEffects){
			aEffect.update(db);
		}
	}
	public void update(SQLiteDatabase db){
		ContentValues values = convertEntity2Cv(this);
		String where = "_id=?";
		String[] whereArgs = {""+get_id()};
		db.update("SpellSkill", values, where, whereArgs);
	}
	public static SpellSkill create(SQLiteDatabase db,
									Spell spell, Combatant combatant,
									long skill, long xp, long cooldown){
		
		// create cv and insert
		ContentValues values = new ContentValues();
		values.put("spellId", spell.get_id());
		values.put("combatantId", combatant.get_id());
		values.put("skill", skill);
		values.put("xp", xp);
		values.put("cooldown", cooldown);
		long newId = db.insert("SpellSkill", null, values);
		assert(newId != -1);
		
		// convert to entitiy and return
		values.put("_id", newId);
		return convertCv2Entity(spell, combatant, values);
	}
	public void delete(SQLiteDatabase db){
		// unlink
		combatant.getSpellSkills().remove(this);
		this.setCombatant(null);
		
		// delete on db
		String where = "_id=?";
		String[] whereArgs = {""+get_id()};
		db.delete("SpellSkill", where, whereArgs);
	}
	private static SpellSkill convertCv2Entity(Spell spell, Combatant combatant, ContentValues cv){
		SpellSkill spellSkill = new SpellSkill();
		spellSkill.set_id(cv.getAsLong("_id"));
		spellSkill.setSpell(spell);
		spellSkill.setCombatant(combatant);
		combatant.getSpellSkills().add(spellSkill);
		spellSkill.setSkill(cv.getAsLong("skill"));
		spellSkill.setXp(cv.getAsLong("xp"));
		spellSkill.setCooldown(cv.getAsLong("cooldown"));
		return spellSkill;
	}
	private static ContentValues convertEntity2Cv(SpellSkill spellSkill){
		ContentValues values = new ContentValues();
		values.put("_id", spellSkill.get_id());
		values.put("spellId", spellSkill.getSpell().get_id());
		values.put("combatantId", spellSkill.getCombatant().get_id());
		values.put("skill", spellSkill.getSkill());
		values.put("xp", spellSkill.getXp());
		values.put("cooldown", spellSkill.getCooldown());
		return values;
	}
    
	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	
	public Spell getSpell() {
		return spell;
	}
	public void setSpell(Spell spell) {
		this.spell = spell;
	}
	public Combatant getCombatant() {
		return combatant;
	}
	public void setCombatant(Combatant combatant) {
		this.combatant = combatant;
	}
	
	
	public long getSkill() {
		return skill;
	}
	public void setSkill(long skill) {
		this.skill = skill;
	}
	public long incSkill(){
		return ++skill;
	}
	public long getXp() {
		return xp;
	}
	public void setXp(long xp) {
		this.xp = xp;
	}
	public long incXp(){
		return ++xp;
	}
	public void resetXp(){
		xp = 0;
	}
	
	public long getCooldown() {
		return cooldown;
	}
	public void setCooldown(long cooldown) {
		this.cooldown = cooldown;
	}
	
	
	public EffectType getEffectType() {
		return spell.getEffectType();
	}
	public Element getElement() {
		return spell.getElement();
	}
	public long getDurationEffective(){
		return spell.getDurationEffective(skill);
	}
	public long getResEffective(){
		return spell.getResEffective(skill);
	}
	public long getDamageEffective(){
		return spell.getDamageEffective(skill);
	}
	public long getEnergyCostEffective(){
		return spell.getEnergyCostEffective(skill);
	}
	
	
	public String getDescription1(){
		if (skill == 0){
			return spell.getNameTrans();
		} else {
			return spell.getNameTrans() + " (" + Helper.convertNumberToRoman(skill) + ")";
		}
		
	}
	public String getDescription2(){
		switch (spell.getEffectType()){
		case DD:
			return spell.getEffectType().getText() + ": " + spell.getDamageEffective(skill) + " (" + spell.getElement().getText() + ")";
		case MELEE:
			return spell.getEffectType().getText() + ": " + spell.getDamageEffective(skill) + " (" + spell.getElement().getText() + ")";
		case RES_BUFF:
			return spell.getEffectType().getText() + " (" + Helper.convertMillisToFormattedString(spell.getDurationEffective(skill)) + "): " + spell.getResEffective(skill) + " (" + spell.getElement().getText() + ")";
		case DOT:
			return spell.getEffectType().getText() + ": " + spell.getDamageEffective(skill) + " (" + spell.getElement().getText() + ")";
		default:
			return "ERROR: EffectType " + spell.getEffectType() + "not handled in SpellSkill.getDescription2()"; // TODO
		}
		
	}
	public String getDescription3(){
		return A.getContext().getResources().getString(R.string.mana_cost) + " " + spell.getEnergyCostEffective(skill);
	}
	

	
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SpellSkill other = (SpellSkill) obj;
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
        return "SpellSkill[ _id=" + _id + " ]";
    }
	
	
}
