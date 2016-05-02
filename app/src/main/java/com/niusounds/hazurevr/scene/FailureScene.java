package com.niusounds.hazurevr.scene;

import com.eje_c.meganekko.Frame;
import com.eje_c.meganekko.Scene;
import com.niusounds.hazurevr.App;

import ovr.JoyButton;
import ovr.KeyCode;

public class FailureScene extends Scene {

    @Override
    public void update(Frame frame) {

        if (JoyButton.contains(frame.getButtonPressed(), JoyButton.BUTTON_TOUCH_SINGLE)) {
            returnToTopScene();
        }

        super.update(frame);
    }

    private void returnToTopScene() {
        App app = (App) getApp();
        app.runOnGlThread(app::toTopScene);
    }

    @Override
    public boolean onKeyDown(int keyCode, int repeatCount) {

        if (keyCode == KeyCode.OVR_KEY_BACK) {
            return true;
        }

        return super.onKeyDown(keyCode, repeatCount);
    }

    @Override
    public boolean onKeyShortPress(int keyCode, int repeatCount) {

        if (keyCode == KeyCode.OVR_KEY_BACK) {
            returnToTopScene();
            return true;
        }

        return super.onKeyShortPress(keyCode, repeatCount);
    }
}
