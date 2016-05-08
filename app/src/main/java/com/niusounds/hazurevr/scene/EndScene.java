package com.niusounds.hazurevr.scene;

import android.view.View;

import com.eje_c.meganekko.Frame;
import com.eje_c.meganekko.MeganekkoApp;
import com.eje_c.meganekko.Scene;
import com.eje_c.meganekko.SceneObject;
import com.niusounds.hazurevr.App;
import com.niusounds.hazurevr.AudioEngine;
import com.niusounds.hazurevr.R;

import ovr.JoyButton;

/**
 * 成功画面・失敗画面共通
 */
public class EndScene extends Scene {

    private SceneObject button;
    private View buttonView;

    @Override
    protected void initialize(MeganekkoApp app) {
        super.initialize(app);
        button = findObjectById(R.id.to_title);
        buttonView = button.view();
    }

    @Override
    public void update(Frame frame) {

        boolean lookingAt = isLookingAt(button);
        buttonView.setPressed(lookingAt);

        if (lookingAt
                && JoyButton.contains(frame.getButtonPressed(), JoyButton.BUTTON_TOUCH_SINGLE)) {
            returnToTitle();
        }

        super.update(frame);
    }

    /**
     * タイトルに戻るボタンを押した時
     */
    private void returnToTitle() {
        AudioEngine.play("count.ogg", 0, 0, 0);
        App app = (App) getApp();
        app.runOnGlThread(app::toTopScene);
    }
}
