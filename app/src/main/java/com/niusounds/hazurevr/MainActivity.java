package com.niusounds.hazurevr;

import com.eje_c.meganekko.Meganekko;
import com.eje_c.meganekko.MeganekkoApp;
import com.eje_c.meganekko.gearvr.MeganekkoActivity;

public class MainActivity extends MeganekkoActivity {
    private boolean pausingBgm;

    @Override
    public MeganekkoApp createMeganekkoApp(Meganekko meganekko) {
        return new App(meganekko);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (pausingBgm) {
            AudioEngine.resumeBgm();
            pausingBgm = false;
        }
    }

    @Override
    protected void onPause() {
        if (AudioEngine.isBgmPlaying()) {
            AudioEngine.pauseBgm();
            pausingBgm = true;
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        AudioEngine.release();
        super.onDestroy();
    }
}
