package de.kriegergilde.battledruid.common;


public class Helper {
	
	
    public static String convertMillisToFormattedString(long remainingMillis) {
    	
    	if (remainingMillis < 0){
    		return "0s";
    	}
    	
        StringBuffer sb = new StringBuffer(16);
        long days = remainingMillis / (1000L * 60L * 60L * 24L);
        remainingMillis = remainingMillis % (1000L * 60L * 60L * 24L);
        long hours = remainingMillis / (1000L * 60L * 60L);
        remainingMillis = remainingMillis % (1000L * 60L * 60L);
        long minutes = remainingMillis / (1000L * 60L);
        remainingMillis = remainingMillis % (1000L * 60L );
        long seconds = remainingMillis / 1000L;
        if (days > 0){
        	sb.append(days);
        	sb.append("d");
        }
        if (hours > 0){
        	sb.append(hours);
        	sb.append("h");
        }
        if (minutes > 0){
        	sb.append(minutes);
        	sb.append("m");
        }
        if (seconds > 0){
        	sb.append(seconds);
        	sb.append("s");
        }
        
        if (sb.length() == 0){
        	sb.append("0s");
        }
        return sb.toString();
    }
    
    
    public static String convertNumberToRoman(long n){
    	StringBuilder sb = new StringBuilder();
    	if (n > 4000){
    		return Long.toString(n);
    	}
    	long M = n/1000L;
    	for(int i = 0; i<M; i++){
    		sb.append("M");
    	}
    	long C = (n%1000L)/100L;
    	if (C>=5){
    		sb.append("D");
    		C-=5;
    	}
    	for(int i = 0; i<C; i++){
    		sb.append("C");
    	}
    	long X= (n%100L)/10L;
    	if (X>=5){
    		sb.append("L");
    		X-=5;
    	}
    	for(int i = 0; i<X; i++){
    		sb.append("X");
    	}
    	long I = n%10L;
    	if (I>=5){
    		sb.append("V");
    		I-=5;
    	}
    	for(int i = 0; i<I; i++){
    		sb.append("I");
    	}
    	return sb.toString();
    }

}
