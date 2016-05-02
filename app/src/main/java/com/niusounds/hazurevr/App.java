package com.niusounds.hazurevr;

import com.eje_c.meganekko.Meganekko;
import com.eje_c.meganekko.MeganekkoApp;
import com.eje_c.meganekko.Scene;
import com.eje_c.meganekko.gearvr.MeganekkoActivity;
import com.niusounds.hazurevr.scene.PreStageScene;

public class App extends MeganekkoApp {
    private Scene topScene;
    private PreStageScene preStage;

    protected App(Meganekko meganekko) {
        super(meganekko);
//        toTopScene();
        toPreStage();
//        toStage();
    }

    /**
     * トップ画面へ遷移する
     */
    private void toTopScene() {
        recenter();
        showGazeCursor();
        if (topScene == null) {
            setSceneFromXML(R.xml.top);
            topScene = getScene();
        } else {
            topScene.setOpacity(1);
            setScene(topScene);
        }
    }

    /**
     * ステージ前の画面へ遷移する
     */
    private void toPreStage() {
        recenter();
        if (preStage == null) {
            setSceneFromXML(R.xml.pre_stage);
            preStage = (PreStageScene) getScene();
        } else {
            setScene(preStage);
        }
    }

    /**
     * ステージ画面へ遷移する
     */
    private void toStage() {
        recenter();
        showGazeCursor();
        setSceneFromXML(R.xml.stage1);
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

    private void showGazeCursor() {
        ((MeganekkoActivity) getContext()).showGazeCursor();
    }

    private void hideGazeCursor() {
        ((MeganekkoActivity) getContext()).hideGazeCursor();
    }
}
