package dev.screwbox.platformer.scenes;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.environment.ImportRuleset;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.scenes.Scene;
import dev.screwbox.platformer.collectables.Cherries;
import dev.screwbox.platformer.collectables.DeboB;
import dev.screwbox.platformer.collectables.DeboD;
import dev.screwbox.platformer.collectables.DeboE;
import dev.screwbox.platformer.collectables.DeboO;
import dev.screwbox.platformer.components.CurrentLevelComponent;
import dev.screwbox.platformer.effects.Background;
import dev.screwbox.platformer.effects.WaterfallSound;
import dev.screwbox.platformer.enemies.MovingSpikes;
import dev.screwbox.platformer.enemies.slime.Slime;
import dev.screwbox.platformer.enemies.tracer.Tracer;
import dev.screwbox.platformer.map.MapBorderLeft;
import dev.screwbox.platformer.map.MapBorderRight;
import dev.screwbox.platformer.map.MapBorderTop;
import dev.screwbox.platformer.map.MapGravity;
import dev.screwbox.platformer.map.WorldInformation;
import dev.screwbox.platformer.props.Box;
import dev.screwbox.platformer.props.Diggable;
import dev.screwbox.platformer.props.Platfom;
import dev.screwbox.platformer.props.VanishingBlock;
import dev.screwbox.platformer.specials.CatCompanion;
import dev.screwbox.platformer.specials.SmokeEmitter;
import dev.screwbox.platformer.specials.Waypoint;
import dev.screwbox.platformer.specials.player.Player;
import dev.screwbox.platformer.systems.*;
import dev.screwbox.platformer.tiles.NonSolidTile;
import dev.screwbox.platformer.tiles.OneWayGround;
import dev.screwbox.platformer.tiles.SolidGround;
import dev.screwbox.platformer.zones.ChangeMapZone;
import dev.screwbox.platformer.zones.KillZone;
import dev.screwbox.platformer.zones.ReflectionZone;
import dev.screwbox.platformer.zones.ShowLabelZone;
import dev.screwbox.tiled.GameObject;
import dev.screwbox.tiled.Layer;
import dev.screwbox.tiled.Map;
import dev.screwbox.tiled.Tile;

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
        engine.graphics().configuration().setAutoEnableLight(false);
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
                .addSystem(new ToggleLightSystem())
                .addSystem(new KilledFromAboveSystem())
                .addSystem(new ToggleSplitscreenSystem())
                .addSystem(new KillZoneSystem())
                .addSystem(new DebugConfigSystem())
                .addSystem(new PauseSystem())
                .addSystem(new ZoomSystem())
                .addSystem(new OldschoolModeSystem())
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
                .addSystem(new BackgroundSystem())
                .addSystem(new CatMovementSystem());
    }

    void importEntities(final Environment environment) {
        final Map map = Map.fromJson(mapName);

        environment
                .addEntity(new CurrentLevelComponent(mapName))
                .importSource(ImportRuleset.source(map)
                        .make(new MapGravity())
                        .make(new WorldInformation()));

        environment.importSourceDEPRECATED(map)
                .when(propertyIsActive("closed-left")).as(new MapBorderLeft())
                .when(propertyIsActive("closed-right")).as(new MapBorderRight())
                .when(propertyIsActive("closed-top")).as(new MapBorderTop());

        environment.importSourceDEPRECATED(map.layers())
                .when(Layer::isImageLayer).as(new Background());

        environment.importSourceDEPRECATED(map.tiles())
                .usingIndex(this::tileType)
                .when("non-solid").as(new NonSolidTile())
                .when("solid").as(new SolidGround())
                .when("diggable").as(new Diggable())
                .when("one-way").as(new OneWayGround());

        environment.importSourceDEPRECATED(map.objects())
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
