package dev.screwbox.playground.game;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.scenes.Scene;
import dev.screwbox.playground.GameConfig;
import dev.screwbox.playground.design.InGameNotificationDesign;
import dev.screwbox.playground.game.blueprints.*;
import dev.screwbox.playground.game.map.MapConfig;
import dev.screwbox.tiled.GameObject;
import dev.screwbox.tiled.Map;

import static dev.screwbox.core.environment.importing.ImportOptions.indexedSources;
import static dev.screwbox.core.environment.importing.ImportOptions.source;

public class GameScene implements Scene {

    private final GameConfig gameConfig;

    public GameScene(GameConfig gameConfig) {
        this.gameConfig = gameConfig;
    }

    @Override
    public void onEnter(Engine engine) {
        engine.graphics().camera().setZoom(4);
        engine.graphics().light().setAmbientLight(Percent.of(0.2));
        engine.audio().configuration().setSoundRange(128);

        engine.ui()
            .setNotificationSound(null)
            .setNotificationTimeout(Duration.ofSeconds(3))
            .setNotificationDesign(new InGameNotificationDesign(gameConfig.teams()));

        engine.graphics().configuration()
            .setIndirectLightIntensity(Percent.quarter())
            .setMaxLightBounces(1)
            .setIndirectLightRaySpacing(4)
            .setLightmapBlur(4)
            .setLightQuality(Percent.of(0.4))
            .setLightBounceLengthLoss(Percent.of(0.8))
            .setIndirectLightDiameter(6);
    }

    @Override
    public void populate(Environment environment) {
        var map = Map.fromJson(gameConfig.map());

//TODO handle flag added for team 3 but not team 3

        environment
            .enableAllFeatures()
            .addSystem(new LogFpsSystem())
            .addSystemsFromPackage("dev.screwbox.playground.game")

            .importSource(source(map)
                .make(new MapConfig())
                .makeComplex(new Teams(gameConfig))
            )

            .importSource(indexedSources(map.tiles(), tile -> tile.properties().getString("type")) //TODO tile.clazz() -> classProperty from layer or tile
                .assign("floor", new Floor())
                .assign("wall-base", new WallBase())
                .assign("wall-mid", new WallMid())
                .assign("wall-top", new WallTop())
                .assign("wall-top-edge", new WallTopEdge())
                .assign("wall-top-edge", new WallTopEdgeBase())
                .assign("decor", new Decor())
            )
            .importSource(indexedSources(map.objects(), GameObject::name)
                .assign("respawn-area", new RespawnArea())
                .assign("light", new Light())
                .assign("flag", new Flag())
                .assign("flag", new CaptureZone())
            );

    }


}
