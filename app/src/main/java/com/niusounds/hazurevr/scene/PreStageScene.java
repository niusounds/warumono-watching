package com.niusounds.hazurevr.scene;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.animation.AnticipateInterpolator;

import com.eje_c.meganekko.Frame;
import com.eje_c.meganekko.MeganekkoApp;
import com.eje_c.meganekko.Scene;
import com.eje_c.meganekko.SceneObject;
import com.eje_c.meganekko.Texture;
import com.niusounds.hazurevr.App;
import com.niusounds.hazurevr.R;

public class PreStageScene extends Scene {
    private SceneObject stageNum, ready, go;
    private int stage;
    private int newStage = 1;

    @Override
    protected void initialize(MeganekkoApp app) {
        super.initialize(app);
        stageNum = findObjectById(R.id.pre_stage_stage_num);
        ready = findObjectById(R.id.pre_stage_ready);
        go = findObjectById(R.id.pre_stage_go);
    }

    @Override
    public void onResume() {
        super.onResume();
        ready.setVisible(false);
        go.setVisible(false);

        App app = (App) getApp();
        app.runOnGlThread(() -> {

            // よ～い！をフェードアウト
            ready.setOpacity(1);
            ready.setVisible(true);
            ready.animate()
                    .opacity(0)
                    .duration(1500)
                    .interpolator(new AnticipateInterpolator(1.0f))
                    .onEnd(() -> {

                        // はじめ！を表示してからステージスタート
                        go.setVisible(true);
                        app.runOnGlThread(app::onStartStage, 1000);
                    })
                    .start(app);
        }, 1000);
    }

    @Override
    public void update(Frame vrFrame) {

        // ステージ番号を設定する
        if (newStage != stage) {
            stage = newStage;
            applyStageNumber();
        }

        super.update(vrFrame);
    }

    /**
     * ステージ番号を設定する。同じ番号を複数回連続で指定しても何も起こらない。
     *
     * @param stage
     */
    public void setStage(int stage) {
        this.newStage = stage;
    }

    /**
     * stageの値によってステージ番号の画像を切替える。
     */
    private void applyStageNumber() {

        Context context = getApp().getContext();
        Texture texture = stageNum.material().texture();

        switch (stage) {
            case 1:
                texture.set(ContextCompat.getDrawable(context, R.drawable.stage1));
                break;
            case 2:
                texture.set(ContextCompat.getDrawable(context, R.drawable.stage2));
                break;
            case 3:
                texture.set(ContextCompat.getDrawable(context, R.drawable.stage3));
                break;
            case 4:
                texture.set(ContextCompat.getDrawable(context, R.drawable.stage4));
                break;
            case 5:
                texture.set(ContextCompat.getDrawable(context, R.drawable.stage5));
                break;
        }
    }
}
