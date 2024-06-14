package io.github.srcimon.screwbox.core;

import io.github.srcimon.screwbox.core.assets.Assets;
import io.github.srcimon.screwbox.core.scenes.Scenes;

public class EngineSetup implements Customizer<Engine> {

    private final Customizer<Engine> customization;

    EngineSetup(final Customizer<Engine> customization) {
        this.customization = customization;
    }

    public static EngineSetup setupScenes(final Customizer<Scenes> scenesCustomizer) {
        return new EngineSetup(engine -> scenesCustomizer.customize(engine.scenes()));
    }

    public static EngineSetup setupAssets(final Customizer<Assets> assetsCustomizer) {
        return new EngineSetup(engine -> assetsCustomizer.customize(engine.assets()));
    }

    public static EngineSetup combine(EngineSetup... setups) {
        return new EngineSetup(null);//TODO
    }

    @Override
    public void customize(final Engine engine) {
        customization.customize(engine);
    }
}
