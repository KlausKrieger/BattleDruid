package de.kriegergilde.battledruid.common;

import de.kriegergilde.battledruid.R;
import de.kriegergilde.battledruid.enums.EffectType;
import de.kriegergilde.battledruid.enums.Element;
import de.kriegergilde.battledruid.model.ActiveEffect;
import de.kriegergilde.battledruid.model.Combatant;
import de.kriegergilde.battledruid.model.Spell;
import de.kriegergilde.battledruid.model.SpellSkill;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import static de.kriegergilde.battledruid.common.Constants.TAG;



public class MySQLightOpenHelper extends SQLiteOpenHelper {
	
	Context context;

	public MySQLightOpenHelper(Context context){
		super(context, "druid.db", null, 38);
		this.context = context;
	}
	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.w(TAG, "onCreate entered");
		Spell.onCreate(context, db);
		Combatant.onCreate(context, db);
		ActiveEffect.onCreate(db);
		SpellSkill.onCreate(db);
		
		fillDB(db);
	}
	
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG, "onUpgrade entered");

		Spell.onUpgrade(context, db, oldVersion, newVersion);
		Spell.loadAll(db);
		Combatant.onUpgrade(context, db, oldVersion, newVersion);
		ActiveEffect.onUpgrade(context, db, oldVersion, newVersion);
		SpellSkill.onUpgrade(context, db, oldVersion, newVersion);
		
		
	}
	

	private void fillDB(SQLiteDatabase db) {
		
		ContentValues values;
		
		// create spells
		values = new ContentValues();
		values.put("nameTec", "spell_firestaff");
		values.put("energyCostBase", 0);
		values.put("damageBase", 7);
		values.put("resBase", 0);
		values.put("durationBase", 0);
		values.put("effectType", EffectType.MELEE.toString());
		values.put("element", Element.FIRE.toString());
		db.insert("Spell", null, values);
		
		values = new ContentValues();
		values.put("nameTec", "spell_icestaff");
		values.put("energyCostBase", 0);
		values.put("damageBase", 7);
		values.put("resBase", 0);
		values.put("durationBase", 0);
		values.put("effectType", EffectType.MELEE.toString());
		values.put("element", Element.ICE.toString());
		db.insert("Spell", null, values);
		
		values = new ContentValues();
		values.put("nameTec", "spell_matterstaff");
		values.put("energyCostBase", 0);
		values.put("damageBase", 7);
		values.put("resBase", 0);
		values.put("durationBase", 0);
		values.put("effectType", EffectType.MELEE.toString());
		values.put("element", Element.MATTER.toString());
		db.insert("Spell", null, values);
		
		values = new ContentValues();
		values.put("nameTec", "spell_firearrow");
		values.put("energyCostBase", 10);
		values.put("damageBase", 10);
		values.put("resBase", 0);
		values.put("durationBase", 0);
		values.put("effectType", EffectType.DD.toString());
		values.put("element", Element.FIRE.toString());
		db.insert("Spell", null, values);
		
		values = new ContentValues();
		values.put("nameTec", "spell_icebolt");
		values.put("energyCostBase", 10);
		values.put("damageBase", 10);
		values.put("resBase", 0);
		values.put("durationBase", 0);
		values.put("effectType", EffectType.DD.toString());
		values.put("element", Element.ICE.toString());
		db.insert("Spell", null, values);
		
		values = new ContentValues();
		values.put("nameTec", "spell_falling_rocks");
		values.put("energyCostBase", 10);
		values.put("damageBase", 10);
		values.put("resBase", 0);
		values.put("durationBase", 0);
		values.put("effectType", EffectType.DD.toString());
		values.put("element", Element.MATTER.toString());
		db.insert("Spell", null, values);
		
		values = new ContentValues();
		values.put("nameTec", "spell_ignite");
		values.put("energyCostBase", 20);
		values.put("damageBase", 30);
		values.put("resBase", 0);
		values.put("durationBase", 0);
		values.put("effectType", EffectType.DOT.toString());
		values.put("element", Element.FIRE.toString());
		db.insert("Spell", null, values);
		
		values = new ContentValues();
		values.put("nameTec", "spell_freeze");
		values.put("energyCostBase", 20);
		values.put("damageBase", 30);
		values.put("resBase", 0);
		values.put("durationBase", 0);
		values.put("effectType", EffectType.DOT.toString());
		values.put("element", Element.ICE.toString());
		db.insert("Spell", null, values);
		
		values = new ContentValues();
		values.put("nameTec", "spell_insects");
		values.put("energyCostBase", 20);
		values.put("damageBase", 30);
		values.put("resBase", 0);
		values.put("durationBase", 0);
		values.put("effectType", EffectType.DOT.toString());
		values.put("element", Element.MATTER.toString());
		db.insert("Spell", null, values);
		
		values = new ContentValues();
		values.put("nameTec", "spell_resist_heat");
		values.put("energyCostBase", 30);
		values.put("damageBase", 0);
		values.put("resBase", 5);
		values.put("durationBase", 1000 * 60* 60* 8);
		values.put("effectType", EffectType.RES_BUFF.toString());
		values.put("element", Element.FIRE.toString());
		db.insert("Spell", null, values);
		
		values = new ContentValues();
		values.put("nameTec", "spell_resist_cold");
		values.put("energyCostBase", 30);
		values.put("damageBase", 0);
		values.put("resBase", 5);
		values.put("durationBase", 1000 * 60* 60* 8);
		values.put("effectType", EffectType.RES_BUFF.toString());
		values.put("element", Element.ICE.toString());
		db.insert("Spell", null, values);
		
		values = new ContentValues();
		values.put("nameTec", "spell_resist_matter");
		values.put("energyCostBase", 30);
		values.put("damageBase", 0);
		values.put("resBase", 5);
		values.put("durationBase", 1000 * 60* 60* 8);
		values.put("effectType", EffectType.RES_BUFF.toString());
		values.put("element", Element.MATTER.toString());
		db.insert("Spell", null, values);
		
		
		// load/cache spells for further use
		Spell.loadAll(db);
		
		long newId;
		
		// create protagonist
		values.clear();
		values.put("name", "Jashiro");
		values.put("energyMax", 100);
		values.put("energyCurrent", 100);
		values.put("healthMax", 100);
		values.put("healthCurrent", 100);
		newId = db.insert("Combatant", null, values);
		
		// create spell skill entries of protagonist
		for(Spell spell: Spell.getAllSpells()){
			values.clear();
			values.put("spellId", spell.get_id());
			values.put("combatantId", newId);
			values.put("skill", 0);
			db.insert("SpellSkill", null, values);
		}
		
		// create first enemy
		values.clear();
		values.put("name", "Fred");
		values.put("energyMax", 104);
		values.put("energyCurrent", 104);
		values.put("healthMax", 100);
		values.put("healthCurrent", 100);
		newId = db.insert("Combatant", null, values);
		
		// create spell skill entries
		values.clear();
		values.put("spellId", Spell.getSpellByNameTec("spell_firearrow").get_id());
		values.put("combatantId", newId);
		values.put("skill", 3);
		db.insert("SpellSkill", null, values);
		
		values.clear();
		values.put("spellId", Spell.getSpellByNameTec("spell_firestaff").get_id());
		values.put("combatantId", newId);
		values.put("skill", 0);
		db.insert("SpellSkill", null, values);
		
		values.clear();
		values.put("spellId", Spell.getSpellByNameTec("spell_ignite").get_id());
		values.put("combatantId", newId);
		values.put("skill", 1);
		db.insert("SpellSkill", null, values);
		
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
