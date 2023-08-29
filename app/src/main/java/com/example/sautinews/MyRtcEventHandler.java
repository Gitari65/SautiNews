package com.example.sautinews;
import android.content.Context;
import android.widget.Toast;

import io.agora.rtc2.IRtcEngineEventHandler;

public class MyRtcEventHandler extends IRtcEngineEventHandler {
    // Implement the callback methods you need
    Context mContext;
    @Override
    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
        // Handle channel join success event
        showToast("Joined 😊");
    }

    @Override
    public void onUserJoined(int uid, int elapsed) {
        // Handle user joined event
        showToast("new user joined");
    }

    // Implement other callback methods as needed

    private void showToast(String message) {
        // Show a toast message using the provided context
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }
}
