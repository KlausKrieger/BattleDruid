package de.kriegergilde.battledruid;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import de.kriegergilde.battledruid.common.MySQLightOpenHelper;
import de.kriegergilde.battledruid.model.ActiveEffect;
import de.kriegergilde.battledruid.model.Combatant;
import de.kriegergilde.battledruid.model.Spell;
import de.kriegergilde.battledruid.model.SpellSkill;

/*
 * offen allg:
 * logging aus
 * dp statt px
 * about etc
 * assert zu eig. methode?
 * Fehlerhandling wie? entscheiden (Toast, log, exception etc)
 * 
 * async tasks enden nie bzw stapeln ewig (s. debugger)
 * 
 * TODOs ansehen
 * 
 * stäbe müssen irgendwie resistenzen brechen können oder besser werden
 * spätestens wenn die ersten npcs 3 resistenzen haben
 * 
 * testmode beachten!
 * 
 * buildnummer sichtbar zb menu
 * 
 * downloadlink menu
 * 
 * button in popups
 * 
 * popup bestätigung bei duell
 * 
 * effekte entfernen auf gegner bei dessen tod bzw kampfende generell
 * 
 */


public class MainActivity extends ListActivity {

    private final long REG_INTERVAL = 1000 * 10;
    
    private final String CURRENT_ENEMY_ID = "CURRENT_ENEMY_ID";
    
    static final int DIALOG_DRUID_EFFECTS_ID = 1;
    static final int DIALOG_ENEMY_EFFECTS_ID = 2;
    static final int DIALOG_STORY_NEXT = 3;
    static final int DIALOG_VICTORY_ID = 4;
    static final int DIALOG_DEFEAT_ID = 5;
    
    private MySQLightOpenHelper dbh;
    private SQLiteDatabase db;
    
    RegenerationTask regTask;
	
    private Combatant druid;
    private Combatant enemy;
	
	Dialog activeEffectsDialog;
	AlertDialog messageDialog;

	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        dbh = new MySQLightOpenHelper(this);
        db = dbh.getWritableDatabase();
        
        
        
        Spell.loadAll(db);
        druid = Combatant.findById(db, 1L);
        enemy = Combatant.findById(db, getPreferences(MODE_PRIVATE).getLong(CURRENT_ENEMY_ID, 2L));
        
        
        // for testing:
//        druid.setHealthCurrent(99);
//        druid.setEnergyCurrent(99);
        
        ((TextView) findViewById(R.id.druidNameView)).setText(druid.getName());
        ((TextView) findViewById(R.id.enemyNameView)).setText(enemy.getName());
        
        
        // show correct button, hide other
        handleCombatModeChange();
        
        
        ((Button)findViewById(R.id.druidButtonActiveEffects)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DIALOG_DRUID_EFFECTS_ID);
			}
		});
        
        ((Button)findViewById(R.id.enemyButtonActiveEffects)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DIALOG_ENEMY_EFFECTS_ID);
			}
		});
        
        ((Button)findViewById(R.id.enemyButtonChallenge)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!druid.isAlive()){
					Toast.makeText(v.getContext(), R.string.unconscious, Toast.LENGTH_SHORT).show();
				}else {
					druid.setDuringCombat(true, db);
					enemy.setDuringCombat(true, db);
					handleCombatModeChange();
				}
			}
		});
        

        
        // Use the SimpleCursorAdapter to show the
        // elements in a ListView
		List<SpellSkill> spellSkills = druid.getSpellSkills();
		Collections.sort(druid.getSpellSkills());
    	final SpellSkillArrayAdapter adapter = new SpellSkillArrayAdapter( this, spellSkills);
        setListAdapter(adapter);
        
        final ListView list = getListView();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //@Override
            public void onItemClick(AdapterView<?> a, View view, int position,long itemID) {
            	
            	if (!view.isEnabled()){
            		return;
            	}
            	
            	SpellSkill spellSkill = (SpellSkill) list.getItemAtPosition(position);
            	
            	if (druid.isAlive()){
            		druid.castSpell(list.getContext(), db, spellSkill, enemy);
            	} 
            	
            	if (!enemy.isAlive()){
            		// TODO enemy ist tot. Toast ausgeben, reagieren (neuer Gegner oder sowas)
            		// kampf läuft nun nicht mehr?!
            		// vorerst: druid lifemax+1, gegner heilen
            		endCombat();
            	} else {
            		druid.applyTicks(list.getContext(), db);
            	}
            	
            	if (!druid.isAlive()){
            		endCombat();
            	}
            	
            	if (druid.isDuringCombat() && enemy.isAlive()){
            		enemy.chooseAndCastSpell(list.getContext(), db, druid);
            	} 
            	
            	if (!druid.isAlive()){
        			endCombat();
        		} else {
        			enemy.applyTicks(list.getContext(), db);
        		}
            	
            	if (!enemy.isAlive()){
            		endCombat();
            	}
            	
				// inc combatTime (1 turn)
				if (druid.isDuringCombat()) {
					druid.setCombatTime(druid.getCombatTime() + 1);
					enemy.setCombatTime(enemy.getCombatTime() + 1);
				}

				// repaint
				dataChanged();
			}

			private void endCombat() {
				
				druid.setDuringCombat(false, db);
				enemy.setDuringCombat(false, db);
				enemy.setHealthCurrent(enemy.getHealthMax());
				enemy.setEnergyCurrent(enemy.getEnergyMax());

				if (druid.isAlive()) { // victory
					showDialog(DIALOG_VICTORY_ID);
					druid.incHealthMax();
					enemy.update(db);
					enemy = chooseNextEnemy();
				} else {
					showDialog(DIALOG_DEFEAT_ID);
				}

				handleCombatModeChange();
			}

		});
        
        
        checkRegeneration();
        dataChanged();
        
        if (enemy.get_id() == 2L){ // TODO spaeter anders
        	showDialog(DIALOG_STORY_NEXT); 
        }
        
        
    }
    


	protected Combatant chooseNextEnemy() {
		Combatant c = Combatant.findById(db, enemy.get_id() + 1);
		if (c == null){
			return enemy; // repeat last enemy forever // TODO aendern?
		} else {
			return c;
		}
	}



	private void handleCombatModeChange() {
		if (druid.isDuringCombat()){
            ((Button)findViewById(R.id.enemyButtonActiveEffects)).setVisibility(View.VISIBLE);
            ((Button)findViewById(R.id.enemyButtonChallenge)).setVisibility(View.GONE);	
        } else {
            ((Button)findViewById(R.id.enemyButtonActiveEffects)).setVisibility(View.GONE);
            ((Button)findViewById(R.id.enemyButtonChallenge)).setVisibility(View.VISIBLE);
        }
	}
    

	@Override
	protected void onRestart() {
		super.onRestart();
		// re-open database (was closed in onClose() )
		db = dbh.getWritableDatabase();
	}
	
	


	@Override
	protected void onStart() {
		super.onStart();
		regTask = new RegenerationTask();
		regTask.execute(REG_INTERVAL);
	}


	@Override
	protected void onStop() {
		super.onStop();

		regTask.cancel(false);

		druid.update(db);
		
		enemy.update(db);
		
		getPreferences(MODE_PRIVATE).edit().putLong(CURRENT_ENEMY_ID, enemy.get_id()).commit();

		db.close();
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
	    switch(id) {
	    case DIALOG_DRUID_EFFECTS_ID:
	    case DIALOG_ENEMY_EFFECTS_ID:
	    	activeEffectsDialog = new Dialog(this);
	    	activeEffectsDialog.setContentView(R.layout.active_effects_dialog);
	    	activeEffectsDialog.setTitle(getResources().getString(R.string.ActiveEffects));
	    	dialog = activeEffectsDialog;
	        break;
	    case DIALOG_STORY_NEXT:
	    case DIALOG_VICTORY_ID:
	    case DIALOG_DEFEAT_ID:
	        messageDialog = new AlertDialog.Builder(this).create();
	        messageDialog.setMessage("_template_");
			messageDialog.setCanceledOnTouchOutside(true);
			dialog = messageDialog;
	    	break;
	    default:
	        dialog = null;
	    }
	    return dialog;
	}
	

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		super.onPrepareDialog(id, dialog);
		ListView listView;
		ActiveEffectArrayAdapter aeAdapter;
    	String message;
		switch (id) {
		case DIALOG_DRUID_EFFECTS_ID:
			aeAdapter = new ActiveEffectArrayAdapter(
					this, druid.getActiveEffects());
			listView = (ListView) dialog
					.findViewById(R.id.active_effects_list);
			listView.setAdapter(aeAdapter);
			break;
		case DIALOG_ENEMY_EFFECTS_ID:
			aeAdapter = new ActiveEffectArrayAdapter(
					this, enemy.getActiveEffects());
			listView = (ListView) dialog
					.findViewById(R.id.active_effects_list);
			listView.setAdapter(aeAdapter);
			break;
	    case DIALOG_STORY_NEXT:
	    	message = getResources().getStringArray(R.array.stories)[0]; // TODO anders
	    	messageDialog.setMessage(message);
	    	break;
	    case DIALOG_VICTORY_ID:
	    	message = getResources().getStringArray(R.array.victories)[(int)enemy.get_id()]; 
	    	messageDialog.setMessage(message);
	    	break;
	    case DIALOG_DEFEAT_ID:
	    	message = getResources().getStringArray(R.array.defeats)[(int)enemy.get_id()]; 
	    	messageDialog.setMessage(message);
	    	break;
		default:
			dialog = null;
		}
	}




	private class RegenerationTask extends AsyncTask<Long, Void, Void> {
    	
    	@Override
        protected Void doInBackground(Long... l) {
    		try {
				Thread.sleep(l[0]);
				// todo evtl schon je zehntel auf isCancelled abfragen?
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
            return null;
        }

    	@Override
        protected void onPostExecute(Void v) {
    		
			checkRegeneration();
            
            // schedule next timeslot for reg. check
            regTask = new RegenerationTask();
            regTask.execute(REG_INTERVAL);
        }

    	
    }
	
	

	private void checkRegeneration() {
		
		if (druid.isDuringCombat()){
			return; // do not regenerate during combat
		}
		
		long currentTimeMillis = System.currentTimeMillis();
		long millisSinceLastReg;
		long millisNeededPerPoint;
		long pointsRegged;
		
		// reg. energy:
		millisSinceLastReg = currentTimeMillis - druid.getEnergyReggedLast();
		millisNeededPerPoint = 1000L * 60L * 60L * 24L / druid.getEnergyMax();
		pointsRegged = millisSinceLastReg / millisNeededPerPoint;
		if (pointsRegged > 0){
			druid.setEnergyReggedLast(druid.getEnergyReggedLast() + pointsRegged * millisNeededPerPoint);
			druid.setEnergyCurrent(Math.min(druid.getEnergyCurrent() + pointsRegged, druid.getEnergyMax()));
		}
		
		// reg. health:
		millisSinceLastReg = currentTimeMillis - druid.getHealthReggedLast();
		millisNeededPerPoint = 1000L * 60L * 60L * 24L * 7L / druid.getHealthMax();
		pointsRegged = millisSinceLastReg / millisNeededPerPoint;
		if (pointsRegged > 0){
			druid.setHealthReggedLast(druid.getHealthReggedLast() + pointsRegged * millisNeededPerPoint);
			druid.setHealthCurrent(Math.min(druid.getHealthCurrent() + pointsRegged, druid.getHealthMax()));
		}
		
		// remove effects, if timed out
		List<ActiveEffect> effectsToRemove = new LinkedList<ActiveEffect>();
		for(ActiveEffect aEffect : druid.getActiveEffects()){
			if (aEffect.getLastsUntil() < currentTimeMillis){
				effectsToRemove.add(aEffect);
			}
		}
		for(ActiveEffect aEffect : effectsToRemove){
			druid.getActiveEffects().remove(aEffect);
			aEffect.delete(db);
		}
		
		dataChanged();
		
	}
	
	private void dataChanged(){
		TextView druidEnergyView = (TextView) findViewById(R.id.druidEnergyView);
        druidEnergyView.setText(druid.getEnergyCurrent() + "/" + druid.getEnergyMax());
    	TextView druidHealthView = (TextView) findViewById(R.id.druidHealthView);
        druidHealthView.setText(druid.getHealthCurrent() + "/" + druid.getHealthMax());
        
		TextView enemyEnergyView = (TextView) findViewById(R.id.enemyEnergyView);
        enemyEnergyView.setText(enemy.getEnergyCurrent() + "/" + enemy.getEnergyMax());
    	TextView enemyHealthView = (TextView) findViewById(R.id.enemyHealthView);
        enemyHealthView.setText(enemy.getHealthCurrent() + "/" + enemy.getHealthMax());
        
        TextView enemyNameView = (TextView) findViewById(R.id.enemyNameView);
        enemyNameView.setText(enemy.getName());
        
        ((SpellSkillArrayAdapter)getListAdapter()).notifyDataSetChanged();
	}
    
    
}


class SpellSkillArrayAdapter extends ArrayAdapter<SpellSkill> {
	private final Context context;
	private final List<SpellSkill> values;

	public SpellSkillArrayAdapter(Context context, List<SpellSkill> values) {
		super(context, R.layout.list_item_spell, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater
				.inflate(R.layout.list_item_spell, parent, false);
		SpellSkill spellSkill = values.get(position);
		ImageView imageView = (ImageView) rowView
				.findViewById(R.id.iconSpellEffectType);
		imageView.setImageResource(spellSkill.getSpell().getEffectType().getDrawableId());
		imageView.setColorFilter(spellSkill.getSpell().getElement().getColor());
		TextView textViewLine1 = (TextView) rowView.findViewById(R.id.line1);
		TextView textViewLine2 = (TextView) rowView.findViewById(R.id.line2);
		TextView textViewLine3 = (TextView) rowView.findViewById(R.id.line3);
		textViewLine1.setText(spellSkill.getDescription1());
		textViewLine2.setText(spellSkill.getDescription2());
		textViewLine3.setText(spellSkill.getDescription3());
		if(!spellSkill.getCombatant().isAlive()
			||	spellSkill.getSpell().getEnergyCostEffective(spellSkill.getSkill()) > spellSkill.getCombatant().getEnergyCurrent()
			|| (spellSkill.getCombatant().isDuringCombat() 
						&& spellSkill.getCooldown() > spellSkill.getCombatant().getCombatTime())){
			rowView.setBackgroundColor(Color.DKGRAY);
			rowView.setEnabled(false);
		}
		return rowView;
	}
	
	
}

class ActiveEffectArrayAdapter extends ArrayAdapter<ActiveEffect> {
	private final Context context;
	private final List<ActiveEffect> values;

	public ActiveEffectArrayAdapter(Context context, List<ActiveEffect> values) {
		super(context, R.layout.list_item_active_effect, values);
		this.values = values;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater
				.inflate(R.layout.list_item_active_effect, parent, false);
		ActiveEffect aEffect = values.get(position);
		ImageView imageView = (ImageView) rowView
				.findViewById(R.id.iconSpellEffectType);
		imageView.setImageResource(aEffect.getEffectType().getDrawableId());
		imageView.setColorFilter(aEffect.getElement().getColor());
		TextView textViewLine1 = (TextView) rowView.findViewById(R.id.line1);
		TextView textViewLine2 = (TextView) rowView.findViewById(R.id.line2);
		TextView textViewLine3 = (TextView) rowView.findViewById(R.id.line3);
		textViewLine1.setText(aEffect.getDescription1());
		textViewLine2.setText(aEffect.getDescription2());
		textViewLine3.setText(aEffect.getDescription3());
		return rowView;
	}
}
