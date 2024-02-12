package io.github.srcimon.screwbox.examples.platformer.scenes;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.environment.camera.CameraUpdateSystem;
import io.github.srcimon.screwbox.core.environment.debug.LogFpsSystem;
import io.github.srcimon.screwbox.core.scenes.Scene;
import io.github.srcimon.screwbox.examples.platformer.collectables.*;
import io.github.srcimon.screwbox.examples.platformer.components.CurrentLevelComponent;
import io.github.srcimon.screwbox.examples.platformer.components.ScreenshotComponent;
import io.github.srcimon.screwbox.examples.platformer.effects.Background;
import io.github.srcimon.screwbox.examples.platformer.effects.FadeInEffect;
import io.github.srcimon.screwbox.examples.platformer.enemies.MovingSpikes;
import io.github.srcimon.screwbox.examples.platformer.enemies.slime.Slime;
import io.github.srcimon.screwbox.examples.platformer.enemies.tracer.Tracer;
import io.github.srcimon.screwbox.examples.platformer.map.*;
import io.github.srcimon.screwbox.examples.platformer.props.Box;
import io.github.srcimon.screwbox.examples.platformer.props.Diggable;
import io.github.srcimon.screwbox.examples.platformer.props.Platfom;
import io.github.srcimon.screwbox.examples.platformer.props.VanishingBlock;
import io.github.srcimon.screwbox.examples.platformer.specials.Camera;
import io.github.srcimon.screwbox.examples.platformer.specials.CatCompanion;
import io.github.srcimon.screwbox.examples.platformer.specials.Waypoint;
import io.github.srcimon.screwbox.examples.platformer.specials.player.Player;
import io.github.srcimon.screwbox.examples.platformer.systems.*;
import io.github.srcimon.screwbox.examples.platformer.tiles.NonSolidTile;
import io.github.srcimon.screwbox.examples.platformer.tiles.OneWayGround;
import io.github.srcimon.screwbox.examples.platformer.tiles.SolidGround;
import io.github.srcimon.screwbox.examples.platformer.zones.ChangeMapZone;
import io.github.srcimon.screwbox.examples.platformer.zones.KillZone;
import io.github.srcimon.screwbox.examples.platformer.zones.ReflectionZone;
import io.github.srcimon.screwbox.examples.platformer.zones.ShowLabelZone;
import io.github.srcimon.screwbox.tiled.GameObject;
import io.github.srcimon.screwbox.tiled.Layer;
import io.github.srcimon.screwbox.tiled.Map;
import io.github.srcimon.screwbox.tiled.Tile;

import java.util.function.Predicate;

import static java.util.Objects.nonNull;

public class GameScene implements Scene {

    private final String mapName;

    public GameScene() {
        this(null);
    }

    public GameScene(final String mapName) {
        this.mapName = mapName;
    }

    @Override
    public void onEnter(final Engine engine) {
        engine.graphics().light().setAmbientLight(Percent.of(0.06));
        engine.window().setTitle("Platformer");
    }

    @Override
    public void populate(final Environment environment) {
        if (nonNull(mapName)) {
            importEntities(environment);
        }

        environment
                .enablePhysics()
                .enableRendering()
                .enableLight()
                .enableLogic()
                .enableTweening()
                .addSystem(new LogFpsSystem())
                .addSystem(new MovingPlatformSystem())
                .addSystem(new CollectableSystem())
                .addSystem(new CameraUpdateSystem())
                .addSystem(new VanishingOnCollisionSystem())
                .addSystem(new ToggleLightSystemsSystem())
                .addSystem(new KilledFromAboveSystem())
                .addSystem(new GroundDetectorSystem())
                .addSystem(new KillZoneSystem())
                .addSystem(new DebugConfigSystem())
                .addSystem(new PauseSystem())
                .addSystem(new ZoomSystem())
                .addSystem(new MovableSystem())
                .addSystem(new DiggableSystem())
                .addSystem(new FollowPlayerSystem())
                .addSystem(new PlayerControlSystem())
                .addSystem(new SmokePuffSystem())
                .addSystem(new ShowLabelSystem())
                .addSystem(new LetsGoSystem())
                .addSystem(new PrintSystem())
                .addSystem(new ChangeMapSystem())
                .addSystem(new ShadowSystem())
                .addSystem(new CameraShiftSystem())
                .addSystem(new DetectLineOfSightToPlayerSystem())
                .addSystem(new PatrollingMovementSystem())
                .addSystem(new ResetSceneSystem())
                .addSystem(new BackgroundSystem())
                .addSystem(new CatMovementSystem());
    }

    void importEntities(final Environment environment) {
        environment.addEntity(new ScreenshotComponent(), new CurrentLevelComponent(mapName));

        final Map map = Map.fromJson(mapName);

        environment.importSource(map)
                .as(new MapGravity())
                .as(new WorldInformation())
                .when(propertyIsActive("closed-left")).as(new MapBorderLeft())
                .when(propertyIsActive("closed-left")).as(new MapBorderLeft())
                .when(propertyIsActive("closed-right")).as(new MapBorderRight())
                .when(propertyIsActive("closed-top")).as(new MapBorderTop());

        environment.importSource(map.layers())
                .when(Layer::isImageLayer).as(new Background());

        environment.importSource(map.tiles())
                .usingIndex(this::tileType)
                .when("non-solid").as(new NonSolidTile())
                .when("solid").as(new SolidGround())
                .when("one-way").as(new OneWayGround());

        environment.importSource(map.objects())
                .usingIndex(GameObject::name)
                .when("reflection-zone").as(new ReflectionZone())
                .when("cat").as(new CatCompanion())
                .when("moving-spikes").as(new MovingSpikes())
                .when("vanishing-block").as(new VanishingBlock())
                .when("slime").as(new Slime())
                .when("platform").as(new Platfom())
                .when("waypoint").as(new Waypoint())
                .when("camera").as(new Camera())
                .when("player").as(new Player())
                .when("debo-d").as(new DeboD())
                .when("debo-e").as(new DeboE())
                .when("debo-b").as(new DeboB())
                .when("debo-o").as(new DeboO())
                .when("cherries").as(new Cherries())
                .when("killzone").as(new KillZone())
                .when("box").as(new Box())
                .when("diggable").as(new Diggable())
                .when("change-map-zone").as(new ChangeMapZone())
                .when("show-label-zone").as(new ShowLabelZone())
                .when("fade-in").as(new FadeInEffect())
                .when("tracer").as(new Tracer());
    }

    private Predicate<Map> propertyIsActive(final String property) {
        return map -> map.properties().getBoolean(property).orElse(false);
    }

    private String tileType(final Tile tile) {
        final var layerType = tile.layer().properties().get("type");
        return layerType.orElseGet(() -> tile.properties().get("type").orElse("none"));
    }

}
