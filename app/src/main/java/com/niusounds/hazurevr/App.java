package com.niusounds.hazurevr;

import com.eje_c.meganekko.Meganekko;
import com.eje_c.meganekko.MeganekkoApp;
import com.eje_c.meganekko.Scene;
import com.eje_c.meganekko.gearvr.MeganekkoActivity;
import com.niusounds.hazurevr.scene.FailureScene;
import com.niusounds.hazurevr.scene.PreStageScene;
import com.niusounds.hazurevr.scene.StageScene;

public class App extends MeganekkoApp {
    private Scene topScene;
    private PreStageScene preStage;
    private FailureScene failureScene;
    private StageScene stageScene;

    protected App(Meganekko meganekko) {
        super(meganekko);
//        toTopScene();
//        toPreStage();
        toStage();
//        toFailureScene();
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
        if (preStage == null) {
            setSceneFromXML(R.xml.pre_stage);
            preStage = (PreStageScene) getScene();
        } else {
            setScene(preStage);
        }
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
        recenter();
    }

    private void toFailureScene() {
        if (failureScene == null) {
            setSceneFromXML(R.xml.failure);
            failureScene = (FailureScene) getScene();
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
        topScene.animate()
                .opacity(0)
                .duration(1000)
                .onEnd(() -> {
                    toPreStage();
                })
                .start(this);
    }

    /**
     * よ～い！はじめ！の後
     */
    public void onStartStage() {
        toStage();
    }

    /**
     * ステージで残り時間が0になった時
     */
    public void onFailed() {
        toFailureScene();

        // ステージシーンのリソースを開放する
        if (stageScene != null) {
            delete(stageScene);
            stageScene = null;
        }
    }

    private void showGazeCursor() {
        ((MeganekkoActivity) getContext()).showGazeCursor();
    }

    private void hideGazeCursor() {
        ((MeganekkoActivity) getContext()).hideGazeCursor();
    }
}
