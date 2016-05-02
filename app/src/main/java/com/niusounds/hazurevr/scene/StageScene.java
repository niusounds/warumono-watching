package com.niusounds.hazurevr.scene;

import com.eje_c.meganekko.Frame;
import com.eje_c.meganekko.Scene;
import com.eje_c.meganekko.SceneObject;
import com.eje_c.meganekko.utility.Log;
import com.niusounds.hazurevr.R;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;
import java.util.Random;

import ovr.JoyButton;

public class StageScene extends Scene {
    private static final String TAG = StageScene.class.getName();
    private static final Random RANDOM = new Random();
    private List<SceneObject> characters;
    private SceneObject seeingCharacter;

    @Override
    public void onResume() {
        super.onResume();
        characters = findObjectById(R.id.characters).getChildren();

        // 3箇所のうちいずれかのみ表示する
        int saiakuIndex = RANDOM.nextInt(3);
        int omakeIndex = RANDOM.nextInt(3);
        int drIndex = RANDOM.nextInt(3);

        findObjectById(R.id.character_saiaku1).setVisible(saiakuIndex == 0);
        findObjectById(R.id.character_saiaku2).setVisible(saiakuIndex == 1);
        findObjectById(R.id.character_saiaku3).setVisible(saiakuIndex == 2);

        findObjectById(R.id.character_omake1).setVisible(omakeIndex == 0);
        findObjectById(R.id.character_omake2).setVisible(omakeIndex == 1);
        findObjectById(R.id.character_omake3).setVisible(omakeIndex == 2);

        findObjectById(R.id.character_dr1).setVisible(drIndex == 0);
        findObjectById(R.id.character_dr2).setVisible(drIndex == 1);
        findObjectById(R.id.character_dr3).setVisible(drIndex == 2);

        // 全てのキャラクターをカメラの方に向ける
        for (SceneObject character : characters) {
            lookToPlayer(character);
        }
    }

    @Override
    public void update(Frame frame) {

        if (JoyButton.contains(frame.getButtonState(), JoyButton.BUTTON_TOUCH)) {
            SceneObject currentSeeing = null;
            for (SceneObject character : characters) {
                if (isLookingAt(character)) {

                    // DEBUG
                    if (JoyButton.contains(frame.getButtonState(), JoyButton.BUTTON_TOUCH)) {
                        currentSeeing = character;
                        break;
                    }
                }
            }

            if (currentSeeing != null) {
                Vector3f vec = new Vector3f(0, 0, -9.8f);
                getViewOrientation().transform(vec);
                currentSeeing.position(vec);
                lookToPlayer(currentSeeing);
                Log.d(TAG, "Position: %f %f %f", vec.x, vec.y, vec.z);
            }

            seeingCharacter = currentSeeing;
        }

        super.update(frame);
    }

    private void lookToPlayer(SceneObject character) {
        Vector3f pos = character.position();
        float y = (float) (Math.atan2(pos.x, pos.z) + Math.PI);
        float x = (float) (Math.atan2(pos.y, Math.hypot(pos.x, pos.z)));
        Quaternionf rotation = new Quaternionf().rotationYXZ(y, x, 0);
        character.rotation(rotation);
    }
}
