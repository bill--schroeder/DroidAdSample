package com.zealocity;

import com.amazon.device.ads.*;
//import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
//import android.view.View;
import android.widget.FrameLayout;

public class baseActivityAmazon extends baseActivity implements AdListener
{
    private AdLayout adView; // The ad view used to load and display the ad.
    private static final String APP_KEY = "0123456789ABCDEF0123456789ABCDEF"; // Sample Application Key. Replace this variable with your Application Key
    private static final String LOG_TAG = "DroidAdSample"; // Tag used to prefix all log messages
    
    private Handler mHandler = new Handler();
    
    /**
     * When the activity starts, load an ad
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        
        // Setup layout parameters
        FrameLayout.LayoutParams params;
        params = new FrameLayout.LayoutParams(
        		FrameLayout.LayoutParams.FILL_PARENT, 
        		FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM;

        // For debugging purposes enable logging, but disable for production builds
        AdRegistration.enableLogging(false);
        // For debugging purposes flag all ad requests as tests, but set to false for production builds
        AdRegistration.enableTesting(false);
        
        //adView = (AdLayout)findViewById(R.id.ad_view);
        adView = new AdLayout(this);
 
        super.addContentView(adView, params);
        adView.setListener(this);
        
        try {
            AdRegistration.setAppKey(APP_KEY);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Exception thrown: " + e.toString());
            return;
        }
        
        // Calling load ad in the Activity's onCreate method allows a new ad to be loaded 
        // when the application starts and also when the device is rotated.
        LoadAd();
    }

    @Override
    public void onDestroy()
    {
		super.logMessage("onDestroy");
		
		super.onDestroy();
		
		mHandler.removeCallbacks(mRefreshAdTask);

		adView.destroy();
		adView = null;
    }
    
	private Runnable mRefreshAdTask = new Runnable() {
	   public void run() {
		   LoadAd();
	   }
	};	

    /**
     * Load a new ad.
     */
    public void LoadAd() { 
        // Load the ad with the appropriate ad targeting options.
        AdTargetingOptions adOptions = new AdTargetingOptions();
        adView.loadAd(adOptions);
        
        // load a new ad in ... 30,000 = 30 seconds
        mHandler.removeCallbacks(mRefreshAdTask);
        mHandler.postDelayed(mRefreshAdTask, 30000);
    }
    
    /**
     * This event is called after a rich media ads has collapsed from an expanded state.
     */
    @Override
    public void onAdCollapsed(AdLayout view) {
        Log.d(LOG_TAG, "Ad collapsed.");
    }

    /**
     * This event is called if an ad fails to load.
     */
    @Override
    public void onAdFailedToLoad(AdLayout view, AdError error) {
        Log.w(LOG_TAG, "Ad failed to load. Code: " + error.getCode() + ", Message: " + error.getMessage());
    }

    /**
     * This event is called once an ad loads successfully.
     */
    @Override
    public void onAdLoaded(AdLayout view, AdProperties adProperties) {
        Log.d(LOG_TAG, adProperties.getAdType().toString() + " Ad loaded successfully.");
    }

    /**
     * This event is called after a rich media ad expands.
     */
    @Override
    public void onAdExpanded(AdLayout view) {
        Log.d(LOG_TAG, "Ad expanded.");
    }
}
