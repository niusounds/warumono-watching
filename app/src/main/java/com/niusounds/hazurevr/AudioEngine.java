package com.niusounds.hazurevr;

import android.content.Context;

import com.eje_c.meganekko.MeganekkoApp;
import com.eje_c.meganekko.Scene;
import com.google.vrtoolkit.cardboard.audio.CardboardAudioEngine;

import org.joml.Quaternionf;

public class AudioEngine {
    private static CardboardAudioEngine audioEngine;

    public static void init(Context context) {
        audioEngine = new CardboardAudioEngine(context.getApplicationContext(), CardboardAudioEngine.RenderingMode.BINAURAL_HIGH_QUALITY);
        audioEngine.preloadSoundFile("found_dr1.ogg");
        audioEngine.preloadSoundFile("found_dr2.ogg");
        audioEngine.preloadSoundFile("found_dr3.ogg");
        audioEngine.preloadSoundFile("found_omake1.ogg");
        audioEngine.preloadSoundFile("found_omake2.ogg");
        audioEngine.preloadSoundFile("found_omake3.ogg");
        audioEngine.preloadSoundFile("found_saiaku1.ogg");
        audioEngine.preloadSoundFile("found_saiaku2.ogg");
        audioEngine.preloadSoundFile("found_saiaku3.ogg");
        audioEngine.preloadSoundFile("zannendeshita.ogg");
    }

    public static void update(MeganekkoApp app) {
        Scene scene = app.getScene();
        Quaternionf q = scene.getViewOrientation().rotateY((float) (Math.PI / 2));
        audioEngine.setHeadRotation(q.x, q.y, q.z, q.w);
        audioEngine.update();
    }

    public static int play(String filename, float x, float y, float z) {
        int soundId = audioEngine.createSoundObject(filename);
        if (soundId != CardboardAudioEngine.INVALID_ID) {
            audioEngine.setSoundObjectPosition(soundId, x, y, z);
            audioEngine.playSound(soundId, false);
        }
        return soundId;
    }

    public static void stop(int soundId) {
        if (soundId != CardboardAudioEngine.INVALID_ID && audioEngine.isSoundPlaying(soundId)) {
            audioEngine.stopSound(soundId);
        }
    }

    public static int ambisonic(String filename) {
        int soundId = audioEngine.createSoundfield(filename);
        if (soundId != CardboardAudioEngine.INVALID_ID) {
            audioEngine.playSound(soundId, true);
        }
        return soundId;
    }
}
