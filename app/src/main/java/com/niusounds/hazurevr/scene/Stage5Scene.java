package com.niusounds.hazurevr.scene;

import com.niusounds.hazurevr.AudioEngine;

public class Stage5Scene extends StageScene {

    @Override
    public void onResume() {
        super.onResume();
        AudioEngine.playBgm("stage_bgm2.ogg");
    }

    @Override
    public void onPause() {
        super.onPause();
        AudioEngine.stopBgm("stage_bgm2.ogg");
    }
}
