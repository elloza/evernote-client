package com.lozasolutions.evernoteclient.injection.module;

import android.content.Context;

import com.evernote.client.android.EvernoteSession;
import com.lozasolutions.evernoteclient.BuildConfig;
import com.lozasolutions.evernoteclient.data.remote.EvernoteAPI;
import com.lozasolutions.evernoteclient.data.remote.EvernoteAPISDK;
import com.lozasolutions.evernoteclient.injection.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by shivam on 29/5/17.
 */
@Module(includes = {NetworkModule.class})
public class ApiModule {

    @Provides
    @Singleton
    EvernoteSession provideEvernoteSession(@ApplicationContext Context context) {

        String CONSUMER_KEY = "Your consumer key";
        String CONSUMER_SECRET = "Your consumer secret";

        EvernoteSession.EvernoteService EVERNOTE_SERVICE = EvernoteSession.EvernoteService.SANDBOX;
        boolean SUPPORT_APP_LINKED_NOTEBOOKS = true;

        String consumerKey;
        if ("Your consumer key".equals(CONSUMER_KEY)) {
            consumerKey = BuildConfig.EVERNOTE_CONSUMER_KEY;
        } else {
            // isn't the default value anymore
            consumerKey = CONSUMER_KEY;
        }

        String consumerSecret;
        if ("Your consumer secret".equals(CONSUMER_SECRET)) {
            consumerSecret = BuildConfig.EVERNOTE_CONSUMER_SECRET;
        } else {
            // isn't the default value anymore
            consumerSecret = CONSUMER_SECRET;
        }

        //Set up the Evernote singleton session, use EvernoteSession.getInstance() later
        return new EvernoteSession.Builder(context)
                .setEvernoteService(EVERNOTE_SERVICE)
                .setSupportAppLinkedNotebooks(SUPPORT_APP_LINKED_NOTEBOOKS)
                .setForceAuthenticationInThirdPartyApp(true)
                .build(consumerKey, consumerSecret)
                .asSingleton();

    }

    @Provides
    @Singleton
    EvernoteAPI provideEvernoteAPI(EvernoteSession evernoteSession) {
        return new EvernoteAPISDK(evernoteSession);
    }

}
