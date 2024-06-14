package io.github.srcimon.screwbox.core;

import io.github.srcimon.screwbox.core.assets.Assets;
import io.github.srcimon.screwbox.core.scenes.Scenes;

import java.util.function.Consumer;

public class EngineBuilder {

    private final Engine engine;

    EngineBuilder(final Engine engine) {
        this.engine = engine;
    }

   public EngineBuilder customizeAssets(Consumer<Assets> assetsCustomizer) {
        assetsCustomizer.accept(engine.assets());//TODO CUSTOMIZER INTERFACE
        return this;
    }

    public EngineBuilder customizeScenes(Consumer<Scenes> scenesConsumer) {
        scenesConsumer.accept(engine.scenes());//TODO CUSTOMIZER INTERFACE
        return this;
    }
}
