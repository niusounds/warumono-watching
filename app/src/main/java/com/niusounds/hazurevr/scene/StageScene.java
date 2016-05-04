package com.niusounds.hazurevr.scene;

import android.support.annotation.Nullable;
import android.view.animation.AnticipateInterpolator;

import com.eje_c.meganekko.Frame;
import com.eje_c.meganekko.Scene;
import com.eje_c.meganekko.SceneObject;
import com.eje_c.meganekko.utility.Log;
import com.niusounds.hazurevr.App;
import com.niusounds.hazurevr.BuildConfig;
import com.niusounds.hazurevr.R;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import ovr.JoyButton;

public class StageScene extends Scene {
    private static final String TAG = StageScene.class.getName();
    private static final Random RANDOM = new Random();
    private static final int TIME = 30;   // 制限時間（秒）
    private List<SceneObject> characters; // キャラクター
    private SceneObject seeingCharacter;  // 視線を合わせているキャラクター
    private double lookStartTime;         // キャラクターを見つめ始めた時間
    private int restTime = TIME;          // 残り時間
    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor(); // 残り時間を表示するための1秒毎のタスクを管理する
    private Future<?> scheduledFuture;                                                               // 残り時間を表示するタスクをキャンセルするためのFuture
    private SceneObject countContainer;

    @Override
    public void onResume() {
        super.onResume();
        characters = findObjectById(R.id.characters).getChildren();
        countContainer = findObjectById(R.id.count_container);

        // 製品版では3体のみ表示
        if (!BuildConfig.DEBUG) {
            randomChooseCharacter();
        }

        // 全てのキャラクターをカメラの方に向ける
        for (SceneObject character : characters) {
            lookToPlayer(character);
        }

        // 残り時間を計算するバックグラウンド処理を開始する
        scheduledFuture = executorService.scheduleAtFixedRate(() -> {
            restTime--;

            if (restTime <= 0) {
                scheduledFuture.cancel(false);
                onCountZero();
                return;
            }

            if (restTime <= 10) {
                countDown();
            }

        }, 1, 1, TimeUnit.SECONDS);
    }

    /**
     * 残り時間が0になった時
     */
    private void onCountZero() {
        App app = (App) getApp();
        app.runOnGlThread(app::onFailed);
    }

    /**
     * 10～1までのカウントダウンを表示する
     */
    private void countDown() {
        switch (restTime) {
            case 10:
                showAndFadeOutCount(R.id.count_10);
                break;
            case 9:
                showAndFadeOutCount(R.id.count_9);
                break;
            case 8:
                showAndFadeOutCount(R.id.count_8);
                break;
            case 7:
                showAndFadeOutCount(R.id.count_7);
                break;
            case 6:
                showAndFadeOutCount(R.id.count_6);
                break;
            case 5:
                showAndFadeOutCount(R.id.count_5);
                break;
            case 4:
                showAndFadeOutCount(R.id.count_4);
                break;
            case 3:
                showAndFadeOutCount(R.id.count_3);
                break;
            case 2:
                showAndFadeOutCount(R.id.count_2);
                break;
            case 1:
                showAndFadeOutCount(R.id.count_1);
                break;
        }
    }

    /**
     * 数字を表示させてフェードアウトする
     *
     * @param id
     */
    private void showAndFadeOutCount(int id) {
        getApp().runOnGlThread(() -> {
            recenterCount();

            final SceneObject obj = findObjectById(id);
            obj.setVisible(true);
            obj.animate()
                    .interpolator(new AnticipateInterpolator())
                    .duration(500)
                    .opacity(0)
                    .onEnd(() -> obj.setVisible(false))
                    .start(getApp());
        });
    }

    private void recenterCount() {
        Quaternionf viewOrientation = getViewOrientation();
        countContainer.rotation(viewOrientation);
    }

    @Override
    public void delete() {
        cancelRestTimeCount();
        shutdownExecutor();
        super.delete();
    }

    /**
     * 残り時間のカウントダウンを停止する。2回以上呼び出しても何も起こらない。
     */
    private void cancelRestTimeCount() {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
            scheduledFuture = null;
        }
    }

    private void shutdownExecutor() {
        if (executorService != null) {
            executorService.shutdown();
            executorService = null;
        }
    }

    /**
     * 各キャラクターの3箇所の配置のうち、いずれか1個のみ表示する
     */
    private void randomChooseCharacter() {
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
    }

    @Override
    public void update(Frame frame) {

        // 掴んで移動(開発中のみ)
        if (BuildConfig.DEBUG) {
            if (JoyButton.contains(frame.getButtonState(), JoyButton.BUTTON_TOUCH)) {
                SceneObject currentSeeing = getLookingCharacter();

                if (currentSeeing != null) {
                    Vector3f vec = new Vector3f(0, 0, -9.8f);
                    getViewOrientation().transform(vec);
                    currentSeeing.position(vec);
                    lookToPlayer(currentSeeing);
                    Log.d(TAG, "Position: %f %f %f", vec.x, vec.y, vec.z);
                }

                seeingCharacter = currentSeeing;
            }
        } else {

            SceneObject character = getLookingCharacter();

            if (character != null) {
                final double time = frame.getPredictedDisplayTimeInSeconds();

                // 見つめ始めた時間を保持
                if (seeingCharacter != character) {
                    lookStartTime = time;
                }

                // シングルタップするか1秒以上見つめたら見つけたことにする
                if (JoyButton.contains(frame.getButtonPressed(), JoyButton.BUTTON_TOUCH_SINGLE)
                        || time - lookStartTime > 1) {
                    character.setVisible(false);
                    checkClear();
                }
            }

            seeingCharacter = character;
        }

        super.update(frame);
    }

    /**
     * クリア条件を満たしているかどうかを調べる
     */
    private void checkClear() {
        if (allCharactersFound()) {
            // クリア表示
            recenterCount();
            findObjectById(R.id.clear).setVisible(true);

            cancelRestTimeCount();

            // イベント通知
            App app = (App) getApp();
            app.runOnGlThread(app::onClearStage);
        }
    }

    /**
     * 全てのキャラクターを見つけたかどうかを返す。
     *
     * @return 1体でも未発見のキャラクターがいたらfalse。全て見つけていたらtrue。
     */
    private boolean allCharactersFound() {
        for (SceneObject character : characters) {
            if (character.isShown()) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return 視線の先にいるキャラクターオブジェクトを返す。何も見ていない時はnull
     */
    @Nullable
    private SceneObject getLookingCharacter() {
        SceneObject currentSeeing = null;
        for (SceneObject character : characters) {
            if (character.isShown() && isLookingAt(character)) {
                currentSeeing = character;
                break;
            }
        }
        return currentSeeing;
    }

    /**
     * オブジェクトをこちらへ向ける。
     *
     * @param object
     */
    void lookToPlayer(SceneObject object) {
        Vector3f pos = object.position();
        float y = (float) (Math.atan2(pos.x, pos.z) + Math.PI);
        float x = (float) (Math.atan2(pos.y, Math.hypot(pos.x, pos.z)));
        Quaternionf rotation = new Quaternionf().rotationYXZ(y, x, 0);
        object.rotation(rotation);
    }
}
