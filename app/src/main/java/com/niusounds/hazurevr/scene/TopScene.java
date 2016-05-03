package com.niusounds.hazurevr.scene;

import android.view.View;

import com.eje_c.meganekko.Frame;
import com.eje_c.meganekko.MeganekkoApp;
import com.eje_c.meganekko.Scene;
import com.eje_c.meganekko.SceneObject;
import com.eje_c.meganekko.Texture;
import com.niusounds.hazurevr.App;
import com.niusounds.hazurevr.R;

import ovr.JoyButton;

public class TopScene extends Scene {
    private SceneObject startButton;
    private View startButtonView;

    @Override
    protected void initialize(MeganekkoApp app) {
        super.initialize(app);
        startButton = findObjectById(R.id.top_start_button);
        startButtonView = ((Texture.ViewRenderer) startButton.material().texture().getRenderer()).getView();
    }

    @Override
    public void update(Frame frame) {

        // スタートボタンを見ている時に枠の色を変える
        boolean lookingAt = isLookingAt(startButton);
        startButtonView.setPressed(lookingAt);

        // スタートボタンを見ている時にシングルタップをしたら、ゲーム開始
        if (lookingAt
                && JoyButton.contains(frame.getButtonState(), JoyButton.BUTTON_TOUCH_SINGLE)) {
            App app = (App) getApp();
            app.runOnGlThread(app::onStartButtonPressed);
        }
        super.update(frame);
    }
}
