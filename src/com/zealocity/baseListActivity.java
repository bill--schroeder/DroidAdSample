package com.zealocity;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.widget.Toast;

public class baseListActivity extends ListActivity {

    public static final String PREFS_NAME = "basePreferences";

    public void logMessage(String Message){
		// comment this for production
		//showgMessage(Message);
	}
	
	public void showgMessage(String Message){
		Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
	}

	public String getSettings(String key){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String restoredText = settings.getString(key, "");
        
        //logMessage("getSettings: " + key + " - " + restoredText);
        
        return restoredText;
    }
    
	public void saveSettings(String key, String value){
    	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
        
        //logMessage("saveSettings: " + key + " - " + value);
    }
    
}
