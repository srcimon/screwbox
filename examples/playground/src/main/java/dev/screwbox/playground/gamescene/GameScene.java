package dev.screwbox.playground.gamescene;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.environment.ai.PathMovementComponent;
import dev.screwbox.core.environment.controls.MovementControlComponent;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.light.OccluderComponent;
import dev.screwbox.core.environment.navigation.AvoidanceComponent;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.CameraLeadComponent;
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.core.scenes.Scene;
import dev.screwbox.playground.GameConfig;
import dev.screwbox.playground.gamescene.blueprints.Decor;
import dev.screwbox.playground.gamescene.blueprints.FlagPoint;
import dev.screwbox.playground.gamescene.blueprints.Floor;
import dev.screwbox.playground.gamescene.blueprints.Light;
import dev.screwbox.playground.gamescene.blueprints.OrhographicWall;
import dev.screwbox.playground.gamescene.blueprints.SpawnArea;
import dev.screwbox.playground.gamescene.blueprints.Wall;
import dev.screwbox.playground.gamescene.map.MapConfig;
import dev.screwbox.playground.gamescene.teams.BotComponent;
import dev.screwbox.playground.gamescene.teams.HeavenComponent;
import dev.screwbox.playground.gamescene.teams.HumanComponent;
import dev.screwbox.playground.gamescene.teams.TeamMemberComponent;
import dev.screwbox.playground.gamescene.teams.ai.AttackComponent;
import dev.screwbox.playground.gamescene.teams.health.HealthComponent;
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
        engine.graphics().light().setAmbientLight(Percent.of(0.1));
        engine.graphics().configuration().setIndirectLightRaySpacing(1).setLightmapBlur(4);
    }

    @Override
    public void populate(Environment environment) {
        var map = Map.fromJson(gameConfig.map());

        environment
            .enableAllFeatures()
            .addSystem(new LogFpsSystem())
            .addSystemsFromPackage("dev.screwbox.playground.gamescene")
            .importSource(source(map)
                .make(new MapConfig()))

            .importSource(indexedSources(map.tiles(), tile -> tile.properties().getString("type"))
                .assign("floor", new Floor())
                .assign("orthographic-wall", new OrhographicWall())
                .assign("wall", new Wall())
                .assign("decor", new Decor())
            )

            .importSource(indexedSources(map.objects(), GameObject::name)
                .assign("respawn-area", new SpawnArea())
                .assign("light", new Light())
                .assignComplex("flag-point", new FlagPoint())
            );

        final int playerDrawOrder = map.layerWithName("objects").orElseThrow().order();
        addPlayers(playerDrawOrder, environment);
    }

    private void addPlayers(int drawOrder, Environment environment) {
        int viewportId = 0;
        HeavenComponent store = new HeavenComponent();
        for (final var team : gameConfig.teams()) {
            for (int i = 0; i < gameConfig.teamSize(); i++) {
                var player = new Entity(environment.allocateId())
                    .add(new TeamMemberComponent(), config -> config.team = team)
                    .bounds(Bounds.$$(0, 0, 10, 10))
                    .add(new ColliderComponent())
                    .add(new AvoidanceComponent())
                    .add(new OccluderComponent(false))
                    .add(new HealthComponent())
                    .add(new PathMovementComponent(70, 2000))
                    .add(new RenderComponent(Sprite.placeholder(team, Size.square(10)), SpriteDrawOptions.originalSize().drawOrder(drawOrder)))
                    .add(new PhysicsComponent(), config -> config.friction = 10)
                    .add(new AttackComponent());
                if (i == 0 && gameConfig.humanTeam().equals(team)) {
                    player
                        .add(new HumanComponent(viewportId))
                        .add(new MovementControlComponent(), m -> m.maxSpeed = 70)
                        .add(new CameraLeadComponent())
                        .add(new CameraTargetComponent(viewportId, 2));
                    viewportId++;
                } else {
                    player.add(new BotComponent());


                }
                store.deadPlayers.add(player);
            }
        }
        environment.addEntity(new Entity().add(store));
    }

}
