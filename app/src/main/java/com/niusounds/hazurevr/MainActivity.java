package com.niusounds.hazurevr;

import com.eje_c.meganekko.Meganekko;
import com.eje_c.meganekko.MeganekkoApp;
import com.eje_c.meganekko.gearvr.MeganekkoActivity;

public class MainActivity extends MeganekkoActivity {
    @Override
    public MeganekkoApp createMeganekkoApp(Meganekko meganekko) {
        return new App(meganekko);
    }

    @Override
    protected void onDestroy() {
        AudioEngine.release();
        super.onDestroy();
    }
}
