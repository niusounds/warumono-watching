package com.niusounds.hazurevr;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import com.eje_c.meganekko.MeganekkoApp;
import com.eje_c.meganekko.Scene;
import com.google.vrtoolkit.cardboard.audio.CardboardAudioEngine;

import org.joml.Quaternionf;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AudioEngine {
    private static CardboardAudioEngine audioEngine;
    private static Map<String, MediaPlayer> mediaPlayerMap = new HashMap<>();
    private static String pausingBgm;

    public static void init(Context context) {
        audioEngine = new CardboardAudioEngine(context.getApplicationContext(), CardboardAudioEngine.RenderingMode.BINAURAL_HIGH_QUALITY);
        audioEngine.preloadSoundFile("count.ogg");
        audioEngine.preloadSoundFile("found_dr1.ogg");
        audioEngine.preloadSoundFile("found_dr2.ogg");
        audioEngine.preloadSoundFile("found_dr3.ogg");
        audioEngine.preloadSoundFile("found_omake1.ogg");
        audioEngine.preloadSoundFile("found_omake2.ogg");
        audioEngine.preloadSoundFile("found_omake3.ogg");
        audioEngine.preloadSoundFile("found_saiaku1.ogg");
        audioEngine.preloadSoundFile("found_saiaku2.ogg");
        audioEngine.preloadSoundFile("found_saiaku3.ogg");
        audioEngine.preloadSoundFile("pre_stage.ogg");
        audioEngine.preloadSoundFile("zannendeshita.ogg");
        try {
            preloadMediaPlayer(context, "hazure.ogg", true);
            preloadMediaPlayer(context, "stage_bgm.ogg", true);
            preloadMediaPlayer(context, "pre_stage.ogg", false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void release() {
        for (MediaPlayer mp : mediaPlayerMap.values()) {
            mp.release();
        }
        mediaPlayerMap.clear();
    }

    private static void preloadMediaPlayer(Context context, String filename, boolean looping) throws IOException {
        AssetFileDescriptor fd = context.getAssets().openFd(filename);
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
        mediaPlayer.prepare();
        mediaPlayer.setLooping(looping);
        mediaPlayerMap.put(filename, mediaPlayer);
    }

    public static void update(MeganekkoApp app) {
        Scene scene = app.getScene();
        Quaternionf q = scene.getViewOrientation();
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

    public static void bgm(String filename) {
        MediaPlayer mediaPlayer = mediaPlayerMap.get(filename);
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    public static void stopBgm(String filename) {
        MediaPlayer mediaPlayer = mediaPlayerMap.get(filename);
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
        }
    }

    public static boolean isBgmPlaying() {
        for (MediaPlayer mp : mediaPlayerMap.values()) {
            if (mp.isPlaying()) return true;
        }
        return false;
    }

    public static void pauseBgm() {
        for (Map.Entry<String, MediaPlayer> entry : mediaPlayerMap.entrySet()) {
            MediaPlayer mp = entry.getValue();
            if (mp.isPlaying()) {
                mp.pause();
                pausingBgm = entry.getKey();
            }
        }
    }

    public static void resumeBgm() {
        if (pausingBgm != null) {
            MediaPlayer mp = mediaPlayerMap.get(pausingBgm);
            if (mp != null) {
                mp.start();
                pausingBgm = null;
            }
        }
    }
}
