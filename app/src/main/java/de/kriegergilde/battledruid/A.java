package de.kriegergilde.battledruid;

import android.app.Application;
import android.content.Context;

public class A extends Application{

    private static Context context;

    public void onCreate(){
        super.onCreate();
        A.context = getApplicationContext();
    }

    public static Context getContext() {
        return A.context;
    }
}
