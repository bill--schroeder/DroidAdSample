package com.zealocity;

import com.mopub.mobileads.*;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.FrameLayout;

public class baseActivityFree extends baseActivity {

	private MoPubView mAdView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	super.logMessage("baseActivityFree:onCreate");
        
        // Setup layout parameters
        FrameLayout.LayoutParams params;
        params = new FrameLayout.LayoutParams(
        		FrameLayout.LayoutParams.FILL_PARENT, 
        		FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM;
        
        
        // 	*******	this is for MoPub
        //mAdView = (MoPubView) findViewById(R.id.adview);
        mAdView = new MoPubView(this);
        mAdView.setAdUnitId("0123456789ABCDEF0123456789ABCDEF"); // Enter your Ad Unit ID from www.mopub.com
        
        super.addContentView(mAdView, params);
        
        mAdView.loadAd();
                
    }

}
