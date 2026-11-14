package dev.screwbox.playground.gamescene;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.environment.controls.MovementControlComponent;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.scenes.Scene;
import dev.screwbox.playground.GameConfig;
import dev.screwbox.playground.gamescene.mapimport.FlagPoint;
import dev.screwbox.playground.gamescene.mapimport.Floor;
import dev.screwbox.playground.gamescene.mapimport.SpawnArea;
import dev.screwbox.playground.gamescene.mapimport.Wall;
import dev.screwbox.playground.gamescene.player.PlayerComponent;
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
        engine.graphics().camera().setZoom(3);
        for(final var player : engine.environment().fetchAllHaving(PlayerComponent.class)) {
        }
    }

    @Override
    public void populate(Environment environment) {
        var map = Map.fromJson("map.json");
        environment
            .enableAllFeatures()
            .addSystem(new LogFpsSystem())
            .addSystemsFromPackage("dev.screwbox.playground.gamescene")
            .importSource(source(map)
                .make(new MapConfig()))

            .importSource(indexedSources(map.tiles(), tile -> tile.properties().getString("type"))
                .assign("floor", new Floor())
                .assign("wall", new Wall())
            )

            .importSource(indexedSources(map.objects(), GameObject::name)
                .assign("respawn-area", new SpawnArea())
                .assign("flag-point", new FlagPoint())
            );

        for (final var team : gameConfig.teams()) {
            for (int i = 0; i < gameConfig.teamSize(); i++) {
                var player = new Entity(environment.allocateId())
                    .add(new PlayerComponent(), config -> config.team = team)
                    .bounds(Bounds.$$(0, 0, 16, 16))
                    .add(new RenderComponent(Sprite.placeholder(team, Size.square(16))))
                    .add(new PhysicsComponent(), config -> config.friction = 10);
                if (i == 0 && gameConfig.humanTeam().equals(team)) {
                    player
                        .add(new MovementControlComponent())
                        .add(new CameraTargetComponent());
                }
                environment.addEntity(player);
            }
        }
    }

}
