package com.zealocity;

import com.amazon.device.ads.*;
import com.mopub.mobileads.*;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.widget.FrameLayout;

public class baseActivityAds extends baseActivity implements AdListener
{
    private static final String LOG_TAG = "DroidAdSample"; // Tag used to prefix all log messages
    private int adRefresh = 60000; // 60,000 = 60 seconds
    private boolean debuggingEnabled = true;
    private boolean primaryAdFailed = false;
    private FrameLayout.LayoutParams params;
    private Handler mHandler = new Handler();

    private AdLayout amazonAdView; // The Amazon ad view used to load and display the ad.
    private static final String APP_KEY = "0123456789ABCDEF0123456789ABCDEF"; // Amazon Application Key. Replace this variable with your Application Key
    
	private MoPubView moPubAdView; // The MoPub ad view used to load and display the ad.
    private static final String MOPUB_KEY = "0123456789ABCDEF0123456789ABCDEF"; // MoPub Application Key. Replace this variable with your Application Key
	
    /**
     * When the activity starts, load an ad
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        
        // Setup layout parameters
        params = new FrameLayout.LayoutParams(
        		FrameLayout.LayoutParams.FILL_PARENT, 
        		FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM;
        
        // For debugging purposes enable logging, but disable for production builds
        AdRegistration.enableLogging(debuggingEnabled);
        // For debugging purposes flag all ad requests as tests, but set to false for production builds
        AdRegistration.enableTesting(debuggingEnabled);

        // Setup Amazon Ads
        amazonAdView = new AdLayout(this);
        super.addContentView(amazonAdView, params);
        amazonAdView.setListener(this);
        
        try {
            AdRegistration.setAppKey(APP_KEY);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Exception thrown: " + e.toString());
            return;
        }
        
        // setup MoPub Ads
        moPubAdView = new MoPubView(this);
        moPubAdView.setAdUnitId(MOPUB_KEY); // Enter your Ad Unit ID from www.mopub.com
        moPubAdView.setAutorefreshEnabled(false);
        super.addContentView(moPubAdView, params);
        
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

		amazonAdView.destroy();
		amazonAdView = null;
		
		moPubAdView.destroy();
		moPubAdView = null;
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
    	if(primaryAdFailed) {
    		primaryAdFailed = false;
    		LoadMoPubAd();
    	}
    	else {
        	LoadAmazonAd();
    	}
        
        // refresh and load a new ad
        mHandler.removeCallbacks(mRefreshAdTask);
        mHandler.postDelayed(mRefreshAdTask, adRefresh);
    }
    
    public void LoadAmazonAd() {
    	super.logMessage(LOG_TAG, "LoadAmazonAd");

        // Load the ad with the appropriate ad targeting options.
        AdTargetingOptions adOptions = new AdTargetingOptions();
        amazonAdView.loadAd(adOptions);
    }
    
    public void LoadMoPubAd() { 
    	super.logMessage(LOG_TAG, "LoadMoPubAd");
        
    	moPubAdView.loadAd();
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
        primaryAdFailed = true;
        
        LoadAd();
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
