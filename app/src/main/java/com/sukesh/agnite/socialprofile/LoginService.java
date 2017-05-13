package com.sukesh.agnite.socialprofile;

/*
 * Copyright (C) 2016 Surviving with Android (http://www.survivingwithandroid.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.cloudrail.si.interfaces.Profile;
import com.cloudrail.si.services.Facebook;
import com.cloudrail.si.services.Twitter;
//import com.sukesh.agnite.socialprofile.R;

/**
 * Created by francesco on 13/10/16.
 */

public class LoginService extends IntentService {

    public static String PROFILE_INFO = "profile_info";

    public LoginService() {
        super("LoginService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("Prf", "Login service");
        SocialProfile socialProfile = null;

        Profile profile = null;

        if (intent != null)
            socialProfile = (SocialProfile) intent.getSerializableExtra(LoginService.PROFILE_INFO);

        switch(socialProfile) {
            case FACEBOOK:
               profile = getFacebookProfile();
                break;

            case TWITTER:
                profile = getTwitterProfile();
                break;
        }


        String fullName = profile.getFullName();
        String email = profile.getEmail();
        String url = profile.getPictureURL();

        sendBroatcast(fullName, email, url);
    }

    private Profile getFacebookProfile() {
        Log.d("Prf", "Facebook Profile");
        Profile profile = new Facebook(this, getString(R.string.facebook_app_id), getString(R.string.facebook_app_secret));
        return profile;
    }

    private Profile getTwitterProfile() {
        Log.d("Prf", "Twitter Profile");
        Profile profile = new Twitter(this, getString(R.string.twitter_app_id), getString(R.string.twitter_app_secret));
        return profile;
    }



    private void sendBroatcast(String fullName, String email, String url) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(MainActivity.ResponseReceiver.ACTION);
        broadcastIntent.putExtra("fullName", fullName);
        broadcastIntent.putExtra("email", email);
        broadcastIntent.putExtra("url", url);
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.sendBroadcast(broadcastIntent);
    }
}
