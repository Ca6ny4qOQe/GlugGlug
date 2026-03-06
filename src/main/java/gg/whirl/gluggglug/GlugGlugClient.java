package gg.whirl.gluggglug;

import net.fabricmc.api.ClientModInitializer;

public class GlugGlugClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        GlugGlugConfig.load();
    }
}