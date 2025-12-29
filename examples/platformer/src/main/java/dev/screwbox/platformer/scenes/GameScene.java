package dev.screwbox.platformer.scenes;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.environment.Environment;
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

import static dev.screwbox.core.environment.ImportCondition.sourceMatches;
import static dev.screwbox.core.environment.ImportRuleset.indexedSources;
import static dev.screwbox.core.environment.ImportRuleset.source;
import static dev.screwbox.core.environment.ImportRuleset.sources;
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

                .importSource(source(map)
                        .make(new MapGravity())
                        .make(new WorldInformation())
                        .assign(sourceMatches(propertyIsActive("closed-left")), new MapBorderLeft())
                        .assign(sourceMatches(propertyIsActive("closed-right")), new MapBorderRight())
                        .assign(sourceMatches(propertyIsActive("closed-top")), new MapBorderTop()))

                .importSource(sources(map.layers())
                        .assign(sourceMatches(Layer::isImageLayer), new Background()))

                .importSource(indexedSources(map.tiles(), this::tileType)
                        .assign("non-solid", new NonSolidTile())
                        .assign("solid", new SolidGround())
                        .assign("diggable", new Diggable())
                        .assign("one-way", new OneWayGround()))

                .importSource(indexedSources(map.objects(), GameObject::name)
                        .assign("reflection-zone", new ReflectionZone())
                        .assign("cat", new CatCompanion())
                        .assign("moving-spikes", new MovingSpikes())
                        .assign("vanishing-block", new VanishingBlock())
                        .assign("slime", new Slime())
                        .assign("platform", new Platfom())
                        .assign("waypoint", new Waypoint())
                        .assign("smoke-emitter", new SmokeEmitter())
                        .assign("waterfall", new WaterfallSound())
                        .assign("player", new Player())
                        .assign("debo-d", new DeboD())
                        .assign("debo-e", new DeboE())
                        .assign("debo-b", new DeboB())
                        .assign("debo-o", new DeboO())
                        .assign("cherries", new Cherries())
                        .assign("killzone", new KillZone())
                        .assign("box", new Box())
                        .assign("change-map-zone", new ChangeMapZone())
                        .assign("show-label-zone", new ShowLabelZone())
                        .assign("tracer", new Tracer()));

    }

    private Predicate<Map> propertyIsActive(final String property) {
        return map -> map.properties().tryGetBoolean(property).orElse(false);
    }

    private String tileType(final Tile tile) {
        final var layerType = tile.layer().properties().tryGetString("type");
        return layerType.orElseGet(() -> tile.properties().tryGetString("type").orElse("none"));
    }
}
