package io.github.srcimon.screwbox.platformer.scenes;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.environment.core.LogFpsSystem;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.scenes.Scene;
import io.github.srcimon.screwbox.platformer.collectables.Cherries;
import io.github.srcimon.screwbox.platformer.collectables.DeboB;
import io.github.srcimon.screwbox.platformer.collectables.DeboD;
import io.github.srcimon.screwbox.platformer.collectables.DeboE;
import io.github.srcimon.screwbox.platformer.collectables.DeboO;
import io.github.srcimon.screwbox.platformer.components.CurrentLevelComponent;
import io.github.srcimon.screwbox.platformer.effects.Background;
import io.github.srcimon.screwbox.platformer.effects.WaterfallSound;
import io.github.srcimon.screwbox.platformer.enemies.MovingSpikes;
import io.github.srcimon.screwbox.platformer.enemies.slime.Slime;
import io.github.srcimon.screwbox.platformer.enemies.tracer.Tracer;
import io.github.srcimon.screwbox.platformer.map.MapBorderLeft;
import io.github.srcimon.screwbox.platformer.map.MapBorderRight;
import io.github.srcimon.screwbox.platformer.map.MapBorderTop;
import io.github.srcimon.screwbox.platformer.map.MapGravity;
import io.github.srcimon.screwbox.platformer.map.WorldInformation;
import io.github.srcimon.screwbox.platformer.props.Box;
import io.github.srcimon.screwbox.platformer.props.Diggable;
import io.github.srcimon.screwbox.platformer.props.Platfom;
import io.github.srcimon.screwbox.platformer.props.VanishingBlock;
import io.github.srcimon.screwbox.platformer.specials.CatCompanion;
import io.github.srcimon.screwbox.platformer.specials.SmokeEmitter;
import io.github.srcimon.screwbox.platformer.specials.Waypoint;
import io.github.srcimon.screwbox.platformer.specials.player.Player;
import io.github.srcimon.screwbox.platformer.systems.*;
import io.github.srcimon.screwbox.platformer.tiles.NonSolidTile;
import io.github.srcimon.screwbox.platformer.tiles.OneWayGround;
import io.github.srcimon.screwbox.platformer.tiles.SolidGround;
import io.github.srcimon.screwbox.platformer.zones.ChangeMapZone;
import io.github.srcimon.screwbox.platformer.zones.KillZone;
import io.github.srcimon.screwbox.platformer.zones.ReflectionZone;
import io.github.srcimon.screwbox.platformer.zones.ShowLabelZone;
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
//        engine.graphics().screen().setRotation(Rotation.degrees(20));
        engine.graphics().screen().setCanvasBounds(new ScreenBounds(400, 100, 700, 300));
    }

    @Override
    public void populate(final Environment environment) {
        if (nonNull(mapName)) {
            importEntities(environment);
        }

        environment
                .enableAllFeatures()
                .addSystem(new LogFpsSystem())
                .addSystem(new MovingPlatformSystem())
                .addSystem(new CollectableSystem())
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
                .addSystem(new ShowLabelSystem())
                .addSystem(new LetsGoSystem())
                .addSystem(new PrintSystem())
                .addSystem(new ChangeMapSystem())
                .addSystem(new ShadowSystem())
                .addSystem(new CameraShiftSystem())
                .addSystem(new DetectLineOfSightToPlayerSystem())
                .addSystem(new PatrollingMovementSystem())
                .addSystem(new BackgroundSystem())
                .addSystem(new CatMovementSystem());
    }

    void importEntities(final Environment environment) {
        final Map map = Map.fromJson(mapName);

        environment
                .addEntity(new CurrentLevelComponent(mapName))
                .importSource(map)
                .as(new MapGravity())
                .as(new WorldInformation())
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
                .when("smoke-emitter").as(new SmokeEmitter())
                .when("waterfall").as(new WaterfallSound())
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
                .when("tracer").as(new Tracer());
    }

    private Predicate<Map> propertyIsActive(final String property) {
        return map -> map.properties().tryGetBoolean(property).orElse(false);
    }

    private String tileType(final Tile tile) {
        final var layerType = tile.layer().properties().tryGetString("type");
        return layerType.orElseGet(() -> tile.properties().tryGetString("type").orElse("none"));
    }

}
