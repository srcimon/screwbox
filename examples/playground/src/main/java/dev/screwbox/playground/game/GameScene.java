package dev.screwbox.playground.game;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.environment.ai.PathMovementComponent;
import dev.screwbox.core.environment.controls.MovementControlComponent;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.light.OccluderComponent;
import dev.screwbox.core.environment.navigation.AvoidanceComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.CameraLeadComponent;
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.core.scenes.Scene;
import dev.screwbox.core.utils.ListUtil;
import dev.screwbox.playground.GameConfig;
import dev.screwbox.playground.design.InGameNotificationDesign;
import dev.screwbox.playground.game.blueprints.*;
import dev.screwbox.playground.game.map.MapConfig;
import dev.screwbox.playground.game.teams.BotComponent;
import dev.screwbox.playground.game.teams.HeavenComponent;
import dev.screwbox.playground.game.teams.HumanComponent;
import dev.screwbox.playground.game.teams.PlayerComponent;
import dev.screwbox.playground.game.teams.TeamInfoComponent;
import dev.screwbox.playground.game.teams.ai.AttackComponent;
import dev.screwbox.playground.game.teams.health.HealthComponent;
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
        populateTeamsAndTranslateTeamValues(map, environment);

        environment
            .enableAllFeatures()
            .addSystem(new LogFpsSystem())
            .addSystemsFromPackage("dev.screwbox.playground.game")

            .importSource(source(map)
                .make(new MapConfig()))

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

    //TODO move into gameconfig
    private static final List<String> NAMES = List.of("Russel", "Davi", "Sonora", "Scott", "Gwendel");

    //TODO split into create and populate
    private void populateTeamsAndTranslateTeamValues(Map map, Environment environment) {
        final int playerDrawOrder = map.layerWithName("objects").orElseThrow().order();
        int viewportId = 0;
        HeavenComponent heaven = new HeavenComponent();
        int teamIndex = 0;
        for (final var team : gameConfig.teams()) {
            final var teamInfo = new TeamInfoComponent();
            teamInfo.color = team.color();
            teamInfo.name = team.name();
            teamInfo.index = teamIndex;

            int entityId = environment.allocateId();
            final Entity teamInfoEntity = new Entity(entityId).add(teamInfo);
            environment.addEntity(teamInfoEntity);

            map.translateGameObjectProperty("team", "team-id", teamIndex, entityId);
            map.translateGameObjectProperty("team", "team-color", teamIndex, team.color().hex());
            for (int i = 0; i < team.teamSize(); i++) {
                var player = new Entity(environment.allocateId())
                    .add(new PlayerComponent(), config -> {
                        config.teamId = entityId;
                        config.name = ListUtil.pickRandom(NAMES);
                    })
                    .bounds(Bounds.$$(0, 0, 10, 10))
                    .add(new AvoidanceComponent())
                    .add(new OccluderComponent(false))
                    .add(new HealthComponent())
                    .add(new PathMovementComponent(70, 2000))
                    .add(new RenderComponent(Sprite.placeholder(team.color(), Size.square(10)), SpriteDrawOptions.originalSize().drawOrder(playerDrawOrder)))
                    .add(new PhysicsComponent(), config -> config.friction = 10)
                    .add(new AttackComponent());
                if (i == 0 && gameConfig.humanTeamName().equals(team.name())) {
                    player
                        .add(new HumanComponent(viewportId))
                        .add(new MovementControlComponent(), m -> m.maxSpeed = 70)
                        .add(new CameraLeadComponent())
                        .add(new CameraTargetComponent(viewportId, 2));
                    viewportId++;
                } else {
                    player.add(new BotComponent());
                }
                heaven.deadPlayers.add(player);
            }
            teamIndex++;
        }
        environment.addEntity(new Entity().add(heaven));
    }

}
