package com.niusounds.hazurevr;

import com.eje_c.meganekko.Meganekko;
import com.eje_c.meganekko.MeganekkoApp;
import com.eje_c.meganekko.Scene;
import com.eje_c.meganekko.gearvr.MeganekkoActivity;
import com.niusounds.hazurevr.scene.PreStageScene;
import com.niusounds.hazurevr.scene.StageScene;

public class App extends MeganekkoApp {
    private static final int TOTAL_STAGES = 1;
    private Scene topScene;
    private PreStageScene preStage;
    private Scene failureScene, allClearScene;
    private StageScene stageScene;
    private int stage = 1;

    protected App(Meganekko meganekko) {
        super(meganekko);
        toTopScene();
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
            // TODO レベルごとに異なるシーンを読み込む
            setSceneFromXML(R.xml.stage1);
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
        hideGazeCursor();
        stage = 1; // 最初のステージ
        topScene.animate()
                .opacity(0)
                .duration(getContext().getResources().getInteger(R.integer.scene_transition_fade_out_time))
                .onEnd(() -> toPreStage())
                .start(this);
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

                        // 全ステージクリアしていたら全クリアのシーンへ
                        // そうでなければ次のステージへ
                        if (++stage >= TOTAL_STAGES) {
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
