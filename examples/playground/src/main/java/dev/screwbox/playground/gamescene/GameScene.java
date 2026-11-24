package dev.screwbox.playground.gamescene;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.environment.ai.PathMovementComponent;
import dev.screwbox.core.environment.controls.MovementControlComponent;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.light.OccluderComponent;
import dev.screwbox.core.environment.light.PointLightComponent;
import dev.screwbox.core.environment.navigation.AvoidanceComponent;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.CursorAttachmentComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.CameraLeadComponent;
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.core.scenes.Scene;
import dev.screwbox.core.utils.ListUtil;
import dev.screwbox.playground.GameConfig;
import dev.screwbox.playground.gamescene.blueprints.*;
import dev.screwbox.playground.gamescene.map.MapConfig;
import dev.screwbox.playground.gamescene.teams.BotComponent;
import dev.screwbox.playground.gamescene.teams.HeavenComponent;
import dev.screwbox.playground.gamescene.teams.HumanComponent;
import dev.screwbox.playground.gamescene.teams.PlayerComponent;
import dev.screwbox.playground.gamescene.teams.ai.AttackComponent;
import dev.screwbox.playground.gamescene.teams.health.HealthComponent;
import dev.screwbox.tiled.GameObject;
import dev.screwbox.tiled.Map;

import java.util.List;

import static dev.screwbox.core.environment.importing.ImportOptions.indexedSources;
import static dev.screwbox.core.environment.importing.ImportOptions.source;

public class GameScene implements Scene {

    private final GameConfig gameConfig;

    public GameScene(GameConfig gameConfig) {
        this.gameConfig = gameConfig;
    }

    @Override
    public void onEnter(Engine engine) {
        engine.graphics().camera().setZoom(5);
        engine.graphics().light().setAmbientLight(Percent.of(0.1));
        engine.audio().configuration().setSoundRange(128);
        engine.ui().setNotificationSound(null);
        engine.graphics().configuration()
            .setIndirectLightIntensity(Percent.of(0.0))
            .setIndirectLightRaySpacing(2.0)
            .setLightmapBlur(3)
            .setLightQuality(Percent.half())
            .setIndirectLightDiameter(12);
    }

    @Override
    public void populate(Environment environment) {
        var map = Map.fromJson(gameConfig.map());
//environment.addSystem(e -> {
//    e.environment().fetchAllHaving(OccluderComponent.class).forEach(s -> {
//        e.graphics().world().drawRectangle(s.bounds(), RectangleDrawOptions.outline(Color.WHITE).drawOrder(Order.DEBUG_OVERLAY_LATE.drawOrder()+1));
//        e.graphics().world().drawRectangle(s.bounds(), RectangleDrawOptions.filled(Color.RED.opacity(0.5)).drawOrder(Order.DEBUG_OVERLAY_LATE.drawOrder()));
//    });
//});
        environment
            .addEntity(new Entity().add(new TransformComponent()).add(new CursorAttachmentComponent()).add(new PointLightComponent(100, Color.RED)))
            .enableAllFeatures()
            .addSystem(new LogFpsSystem())
            .addSystemsFromPackage("dev.screwbox.playground.gamescene")
            .importSource(source(map)
                .make(new MapConfig()))

            .importSource(indexedSources(map.tiles(), tile -> tile.properties().getString("type"))
                .assign("floor", new Floor())
                .assign("wall-base", new WallBase())
                .assign("wall-mid", new WallMid())
                .assign("wall-top", new WallTop())
                .assign("wall-topspecial", new WallTopSpecial())
                .assign("wall-topspecial", new WallTopSpecial2())
                .assign("decor", new Decor())
            )

            .importSource(indexedSources(map.objects(), GameObject::name)
                .assign("respawn-area", new RespawnArea())
                .assign("light", new Light())
                .assign("flag", new Flag())
                .assign("flag", new CaptureZone())
            );

        final int playerDrawOrder = map.layerWithName("objects").orElseThrow().order();
        addPlayers(playerDrawOrder, environment);
    }

    //TODO find a place for names
    private static final List<String> NAMES = List.of("Russel", "Davi", "Sonora", "Scott", "Gwendel");

    private void addPlayers(int drawOrder, Environment environment) {
        int viewportId = 0;
        HeavenComponent store = new HeavenComponent();
        for (final var team : gameConfig.teams()) {
            for (int i = 0; i < gameConfig.teamSize(); i++) {
                var player = new Entity(environment.allocateId())
                    .add(new PlayerComponent(), config -> {
                        config.team = team;
                        config.name = ListUtil.pickRandom(NAMES);
                    })
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
