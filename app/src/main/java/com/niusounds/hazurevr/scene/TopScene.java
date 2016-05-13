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
 * 最初の画面
 */
public class TopScene extends Scene {
    private SceneObject startButton; // スタートボタンオブジェクト
    private View startButtonView;    // スタートボタンUI

    @Override
    protected void initialize(MeganekkoApp app) {
        super.initialize(app);
        startButton = findObjectById(R.id.top_start_button);
        startButtonView = startButton.view();
    }

    @Override
    public void onResume() {
        super.onResume();
        AudioEngine.playBgm("hazure.ogg");
    }

    @Override
    public void onPause() {
        super.onPause();
        AudioEngine.stopBgm("hazure.ogg");
    }

    @Override
    public void update(Frame frame) {

        // スタートボタンを見ている時に枠の色を変える
        boolean lookingAt = isLookingAt(startButton);
        startButtonView.setPressed(lookingAt);

        // ジョイパッドのスタートボタンを押すか、スタートボタンを見ている時にシングルタップをしたらゲーム開始
        int buttonPressed = frame.getButtonPressed();
        if (JoyButton.contains(buttonPressed, JoyButton.BUTTON_START)
                || (lookingAt && JoyButton.contains(buttonPressed, JoyButton.BUTTON_TOUCH_SINGLE))) {
            App app = (App) getApp();
            app.runOnGlThread(app::onStartButtonPressed);
        }

        super.update(frame);
    }
}
