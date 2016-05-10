package com.niusounds.hazurevr.scene;

import com.niusounds.hazurevr.AudioEngine;

/**
 * 失敗画面
 */
public class AllClearScene extends EndScene {

    private int soundId;

    @Override
    public void onResume() {
        super.onResume();
        soundId = AudioEngine.play("chikushou.ogg", 0, 0, -3);
    }

    @Override
    public void onPause() {
        super.onPause();
        AudioEngine.stop(soundId);
    }
}
