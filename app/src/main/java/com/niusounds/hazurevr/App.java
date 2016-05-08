package com.niusounds.hazurevr;

import android.animation.ValueAnimator;

import com.eje_c.meganekko.Meganekko;
import com.eje_c.meganekko.MeganekkoApp;
import com.eje_c.meganekko.Scene;
import com.eje_c.meganekko.gearvr.MeganekkoActivity;
import com.niusounds.hazurevr.scene.PreStageScene;
import com.niusounds.hazurevr.scene.StageScene;

public class App extends MeganekkoApp {
    private Scene topScene;
    private PreStageScene preStage;
    private Scene failureScene, allClearScene;
    private StageScene stageScene;
    private int stage;

    protected App(Meganekko meganekko) {
        super(meganekko);
        AudioEngine.init(meganekko.getContext());
        toTopScene();
    }


    @Override
    public void update() {
        AudioEngine.update(this);
        super.update();
    }

    /**
     * トップ画面へ遷移する
     */
    public void toTopScene() {
        showGazeCursor();
        if (topScene == null) {
            setSceneFromXML(R.xml.top);
            topScene = getScene();
        } else {
            topScene.setOpacity(1);
            setScene(topScene);
        }
        recenter();
    }

    /**
     * ステージ前の画面へ遷移する
     */
    private void toPreStage() {
        hideGazeCursor();
        if (preStage == null) {
            setSceneFromXML(R.xml.pre_stage);
            preStage = (PreStageScene) getScene();
        } else {
            setScene(preStage);
        }
        preStage.setStage(stage);
        recenter();
    }

    /**
     * ステージ画面へ遷移する
     */
    private void toStage() {
        showGazeCursor();
        if (stageScene == null) {
            int stageRes = ResourceID.stageXML(stage);
            setSceneFromXML(stageRes);
            stageScene = (StageScene) getScene();
        } else {
            setScene(stageScene);
        }
    }

    /**
     * 全部クリアしたシーンへ遷移する
     */
    private void toAllClearScene() {
        showGazeCursor();
        if (allClearScene == null) {
            setSceneFromXML(R.xml.all_clear);
            allClearScene = getScene();
        } else {
            setScene(allClearScene);
        }
        recenter();
    }

    /**
     * ゲームオーバーシーンへ遷移する
     */
    private void toFailureScene() {
        showGazeCursor();
        if (failureScene == null) {
            setSceneFromXML(R.xml.failure);
            failureScene = getScene();
        } else {
            setScene(failureScene);
        }
        recenter();
    }

    /**
     * トップ画面のスタートボタンが押された時
     */
    public void onStartButtonPressed() {
        AudioEngine.play("count.ogg", 0, 0, 0);
        hideGazeCursor();
        stage = getContext().getResources().getInteger(R.integer.init_stage); // 最初のステージ
        topScene.animate()
                .opacity(0)
                .duration(getContext().getResources().getInteger(R.integer.scene_transition_fade_out_time))
                .onEnd(() -> toPreStage())
                .start(this);

        runOnUiThread(() -> AudioEngine.fadeBgmVolume("hazure.ogg", 1000, 1, 0));
    }

    /**
     * よ～い！はじめ！の後
     */
    public void onStartStage() {
        toStage();
    }

    /**
     * 全部見つけ終わってクリアが表示されたとき。
     */
    public void onClearStage() {
        runOnGlThread(() -> {
            stageScene.animate()
                    .opacity(0)
                    .duration(getContext().getResources().getInteger(R.integer.scene_transition_fade_out_time))
                    .onEnd(() -> {

                        ++stage;

                        // 全ステージクリアしていたら全クリアのシーンへ
                        // そうでなければ次のステージへ
                        if (stage > getContext().getResources().getInteger(R.integer.total_stages)) {
                            toAllClearScene();
                        } else {
                            toPreStage();
                        }

                        deleteStageScene();
                    })
                    .start(this);
        }, 3000);
    }

    /**
     * ステージで残り時間が0になった時
     */
    public void onFailed() {
        toFailureScene();
        deleteStageScene();
    }

    /**
     * 視線カーソルを表示する
     */
    private void showGazeCursor() {
        ((MeganekkoActivity) getContext()).showGazeCursor();
    }

    /**
     * 視線カーソルを非表示にする
     */
    private void hideGazeCursor() {
        ((MeganekkoActivity) getContext()).hideGazeCursor();
    }

    /**
     * ステージシーンのリソースを開放する
     */
    private void deleteStageScene() {
        if (stageScene != null) {
            delete(stageScene);
            stageScene = null;
        }
    }
}
