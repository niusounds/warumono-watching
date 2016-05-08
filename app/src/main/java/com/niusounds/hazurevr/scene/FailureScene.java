package com.niusounds.hazurevr.scene;

import com.niusounds.hazurevr.AudioEngine;

/**
 * 失敗画面
 */
public class FailureScene extends EndScene {

    private int soundId;

    @Override
    public void onResume() {
        super.onResume();
        soundId = AudioEngine.play("zannendeshita.ogg", 0, 0, -3);
    }

    @Override
    public void onPause() {
        super.onPause();
        AudioEngine.stop(soundId);
    }
}
