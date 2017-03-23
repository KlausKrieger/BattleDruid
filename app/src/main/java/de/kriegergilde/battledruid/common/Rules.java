package de.kriegergilde.battledruid.common;

public class Rules {
	
    public static double getMultiplierForSkill(long skillValue){
        return 1 + skillValue * 0.1;
    }

}
