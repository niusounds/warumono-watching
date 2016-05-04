package com.niusounds.hazurevr;

import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.XmlRes;

public class ResourceID {
    @XmlRes
    public static int stageXML(int stage) {
        switch (stage) {
            case 1:
                return R.xml.stage1;
            case 2:
                return R.xml.stage2;
            case 3:
                return R.xml.stage3;
            case 4:
                return R.xml.stage4;
            case 5:
                return R.xml.stage5;
            default:
                throw new IllegalArgumentException("Invalid stage " + stage);
        }
    }

    @DrawableRes
    public static int stageNumDrawable(int stage) {
        switch (stage) {
            case 1:
                return R.drawable.stage1;
            case 2:
                return R.drawable.stage2;
            case 3:
                return R.drawable.stage3;
            case 4:
                return R.drawable.stage4;
            case 5:
                return R.drawable.stage5;
            default:
                throw new IllegalArgumentException("Invalid stage " + stage);
        }
    }

    @IdRes
    public static int countId(int restTime) {
        switch (restTime) {
            case 10:
                return R.id.count_10;
            case 9:
                return R.id.count_9;
            case 8:
                return R.id.count_8;
            case 7:
                return R.id.count_7;
            case 6:
                return R.id.count_6;
            case 5:
                return R.id.count_5;
            case 4:
                return R.id.count_4;
            case 3:
                return R.id.count_3;
            case 2:
                return R.id.count_2;
            case 1:
                return R.id.count_1;
            default:
                throw new IllegalArgumentException("Invalid restTime " + restTime);
        }
    }

}
