package dev.screwbox.playground.gamescene;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.environment.ai.PathMovementComponent;
import dev.screwbox.core.environment.controls.MovementControlComponent;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.navigation.AvoidanceComponent;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.CameraLeadComponent;
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.scenes.Scene;
import dev.screwbox.core.utils.Scheduler;
import dev.screwbox.playground.GameConfig;
import dev.screwbox.playground.gamescene.flag.FlagPointComponent;
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
    }

    static Scheduler timer = Scheduler.withInterval(Duration.ofMillis(1000));

    @Override
    public void populate(Environment environment) {
        var map = Map.fromJson("map.json");
        environment
            .enableAllFeatures()
            .addSystem(new LogFpsSystem())
            .addSystem(s -> {
                if (timer.isTick()) {
                    for (var P : s.environment().fetchAllHaving(PlayerComponent.class)) {
                        if (!P.hasComponent(MovementControlComponent.class)) {
                            s.async().runExclusive(P, () ->
                                P.get(PathMovementComponent.class).path = s.navigation().findPath(
                                    P.position(),
                                    s.environment().fetchAllHaving(FlagPointComponent.class).getFirst().position()
                                ).orElse(null)
                            );
                        }
                    }
                }
            })
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

        addPlayers(environment);
    }

    private void addPlayers(Environment environment) {
        for (final var team : gameConfig.teams()) {
            for (int i = 0; i < gameConfig.teamSize(); i++) {
                var player = new Entity(environment.allocateId())
                    .add(new PlayerComponent(), config -> config.team = team)
                    .bounds(Bounds.$$(0, 0, 10, 10))
                    .add(new ColliderComponent())
                    .add(new AvoidanceComponent())
                    .add(new PathMovementComponent(70, 2000))
                    .add(new RenderComponent(Sprite.placeholder(team, Size.square(10))))
                    .add(new PhysicsComponent(), config -> config.friction = 10);
                if (i == 0 && gameConfig.humanTeam().equals(team)) {
                    player
                        .add(new MovementControlComponent(), m -> m.maxSpeed = 70)
                        .add(new CameraLeadComponent())
                        .add(new CameraTargetComponent());

                }
                environment.addEntity(player);
            }
        }
    }

}
