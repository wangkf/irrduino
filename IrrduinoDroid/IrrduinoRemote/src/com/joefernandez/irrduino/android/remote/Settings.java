/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.joefernandez.irrduino.android.remote;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.util.Log;

public class Settings extends PreferenceActivity {

	private static final String TAG = "Settings";
	
	public static final String DEFAULT_CONTROLLER_HOST = "192.168.1.110";
	public static final String DEFAULT_CONTROLLER_PORT = "80";

	public static final String DEFAULT_SERVER_HOST = "192.168.1.111";
	public static final String DEFAULT_SERVER_PORT = "80";
	
	public static final String SETTINGS_CATEGORY_CONTROLLER = "controller_settings_category";
	public static final String CONTROLLER_HOST_NAME = "controller_host_name";
	public static final String CONTROLLER_HOST_PORT = "controller_host_port";

	public static final String SETTINGS_CATEGORY_SERVER = "server_settings_category";
	public static final String SERVER_HOST_NAME = "server_host_name";
	public static final String SERVER_HOST_PORT = "server_host_port";
	
	public static final String SETTINGS_CATEGORY_VIDEOFEED = "videofeed_settings_category";
	public static final String VIDEOFEED_HOST_ADDRESS = "videofeed_host_address";
	public static final String VIDEOFEED_TRIGGER_AUTOFOCUS = "videofeed_trigger_autofocus";
	public static final String VIDEOFEED_AUTOFOCUS_CMD = "/focus";
	
	public static final String SETTINGS_CATEGORY_ABOUT = "about_settings_category";
	public static final String ABOUT_VERSION = "about_version";
	
	protected SharedPreferences prefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings);
        
        // Load and display version settings
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        
        PreferenceCategory controller = (PreferenceCategory) findPreference(SETTINGS_CATEGORY_CONTROLLER);
        if (controller != null){
            Preference controllerHostName = controller.findPreference(CONTROLLER_HOST_NAME);
            if (controllerHostName != null && prefs.contains(CONTROLLER_HOST_NAME)){
            	controllerHostName.setSummary(prefs.getString(
            			CONTROLLER_HOST_NAME, DEFAULT_CONTROLLER_HOST));
            }
            Preference controllerHostPort = controller.findPreference(CONTROLLER_HOST_PORT);
            if (controllerHostPort != null&& prefs.contains(CONTROLLER_HOST_PORT)){
            	controllerHostPort.setSummary(prefs.getString(
            			CONTROLLER_HOST_PORT, DEFAULT_CONTROLLER_PORT));
            }
        }

        PreferenceCategory server = (PreferenceCategory) findPreference(SETTINGS_CATEGORY_SERVER);
        if (server != null){
            Preference serverHostName = server.findPreference(SERVER_HOST_NAME);
            if (serverHostName != null && prefs.contains(SERVER_HOST_NAME)){
            	serverHostName.setSummary(prefs.getString(
            			SERVER_HOST_NAME, DEFAULT_SERVER_HOST));
            }
            Preference serverHostPort = server.findPreference(SERVER_HOST_PORT);
            if (serverHostPort != null&& prefs.contains(SERVER_HOST_PORT)){
            	serverHostPort.setSummary(prefs.getString(
            			SERVER_HOST_PORT, DEFAULT_SERVER_PORT));
            }
        }
        
        PreferenceCategory videofeed = (PreferenceCategory) findPreference(SETTINGS_CATEGORY_VIDEOFEED);
        if (videofeed != null){
            Preference videoFeedAddress = videofeed.findPreference(VIDEOFEED_HOST_ADDRESS);
            if (videoFeedAddress != null && prefs.contains(VIDEOFEED_HOST_ADDRESS)){
            	videoFeedAddress.setSummary(prefs.getString(
            			VIDEOFEED_HOST_ADDRESS, DEFAULT_CONTROLLER_HOST));
            }
            
            Preference videoFeedAutofocus = videofeed.findPreference(VIDEOFEED_TRIGGER_AUTOFOCUS);
            if (videoFeedAutofocus != null){
            	videoFeedAutofocus.setOnPreferenceClickListener(new OnPreferenceClickListener() {
					
					public boolean onPreferenceClick(Preference preference) {
						if (prefs.contains(VIDEOFEED_HOST_ADDRESS)){
							
							new HttpCommandTask().execute(prefs.getString(
									VIDEOFEED_HOST_ADDRESS, DEFAULT_CONTROLLER_HOST)
									+ VIDEOFEED_AUTOFOCUS_CMD);
						}
						return false;
					}
				});
            }
        }
        
        PreferenceCategory about = (PreferenceCategory) findPreference(SETTINGS_CATEGORY_ABOUT);
        if (about != null){
            Preference version = about.findPreference(ABOUT_VERSION);
            if (version != null){
                version.setSummary(getVersionName());
            }
        }
    }
    
    private String getVersionName() {
        String versionName = "";
        PackageInfo packageInfo;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (NameNotFoundException e) {
            Log.d(TAG, "Error retrieving app version info: " + e.getMessage());
            return versionName;
        }
        return versionName;
    }
    
    
}