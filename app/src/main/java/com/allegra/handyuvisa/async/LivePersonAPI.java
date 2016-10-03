package com.allegra.handyuvisa.async;

import android.content.Context;

import com.liveperson.mobile.android.service.LPMobileDelegateAPI;
import com.liveperson.mobile.android.ui.LPMobileDelegateAPIImp;

import java.util.Map;

/**
 * Created by sergiofarfan on 3/10/16.
 */

public class LivePersonAPI implements LPMobileDelegateAPI {

    public Context context;

    public LivePersonAPI(Context context){
        this.context = context;
    }

    public LivePersonAPI() {
        super();
    }

    @Override
    public void onAgentAvailabilityChanged(boolean b) {

    }

    @Override
    public void onEnabledChanged(boolean b) {

    }

    @Override
    public void onEnabledChanged(boolean b, String s, String s1) {

    }

    @Override
    public void onHideChatButton() {

    }

    @Override
    public void onShowChatButton() {

    }

    @Override
    public void onEndChat(boolean b) {

    }

    @Override
    public boolean shouldUseCustomActionForChatNotAnswered() {
        return false;
    }

    @Override
    public void customActionForChatNotAnswered() {

    }

    @Override
    public void onEvent(String s, Map<String, Object> map) {

    }

    @Override
    public void onNewUnreadAgentMessage(int i) {

    }

    @Override
    public String getSingleSignOnKeyGenURL() {
        return null;
    }

    @Override
    public boolean shouldUseCustomActionForURLClicked() {
        return false;
    }

    @Override
    public void customActionForURLClicked(String s) {

    }

    @Override
    public boolean shouldEnablePhotoSharing() {
        return false;
    }

    @Override
    public boolean canDrawTabOverlay() {
        return false;
    }

    @Override
    public boolean shouldHideLPWindowOnBackgroundTapping() {
        return false;
    }

    @Override
    public boolean shouldUseCustomNotification() {
        return false;
    }

    @Override
    public void showCustomNotification(String s, String s1) {

    }
}
