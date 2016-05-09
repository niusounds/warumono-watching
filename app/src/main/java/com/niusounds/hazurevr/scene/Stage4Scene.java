package com.niusounds.hazurevr.scene;

import com.eje_c.meganekko.Frame;
import com.eje_c.meganekko.SceneObject;
import com.niusounds.hazurevr.AudioEngine;
import com.niusounds.hazurevr.R;

import org.joml.Vector3f;

/**
 * 公園ステージの独自処理
 */
public class Stage4Scene extends StageScene {
    private SceneObject omake1, omake2, omake3;
    private float time = 0;
    private Vector3f[] positions = new Vector3f[]{
            new Vector3f(7.098612f, -2.365164f, 6.329130f),
            new Vector3f(9.748602f, -0.929191f, 0.375767f),
            new Vector3f(4.664052f, -1.285114f, -8.522618f),
            new Vector3f(6.721768f, 6.353823f, -3.238341f)
    };

    @Override
    public void onResume() {
        super.onResume();

        omake1 = findObjectById(R.id.character_omake1);
        omake2 = findObjectById(R.id.character_omake2);
        omake3 = findObjectById(R.id.character_omake3);
        AudioEngine.playBgm("stage_bgm2.ogg");
    }

    @Override
    public void onPause() {
        super.onPause();
        AudioEngine.stopBgm("stage_bgm2.ogg");
    }

    @Override
    public void update(Frame frame) {
        time += frame.getDeltaSeconds();

        // 時間経過と共にオマケの透明度が下がる
        float opacity = time / 30.0f;
        omake1.setOpacity(opacity);
        omake2.setOpacity(opacity);
        omake3.setOpacity(opacity);

        // まれにomake2の位置が変わる
        if (omake2.isVisible() && Math.random() < 0.002 && !isLookingAt(omake2)) {
            int idx = (int) (Math.random() * positions.length);
            Vector3f position = positions[idx];
            omake2.position(position);
            lookToPlayer(omake2);
        }

        super.update(frame);
    }
}
