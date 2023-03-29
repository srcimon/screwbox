package io.github.simonbas.screwbox.examples.platformer.scenes;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.Percent;
import io.github.simonbas.screwbox.core.entities.Entities;
import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.entities.systems.*;
import io.github.simonbas.screwbox.core.scenes.Scene;
import io.github.simonbas.screwbox.examples.platformer.collectables.*;
import io.github.simonbas.screwbox.examples.platformer.components.CurrentLevelComponent;
import io.github.simonbas.screwbox.examples.platformer.components.ScreenshotComponent;
import io.github.simonbas.screwbox.examples.platformer.effects.Background;
import io.github.simonbas.screwbox.examples.platformer.effects.FadeInEffect;
import io.github.simonbas.screwbox.examples.platformer.enemies.MovingSpikes;
import io.github.simonbas.screwbox.examples.platformer.enemies.slime.Slime;
import io.github.simonbas.screwbox.examples.platformer.enemies.tracer.Tracer;
import io.github.simonbas.screwbox.examples.platformer.map.*;
import io.github.simonbas.screwbox.examples.platformer.props.Box;
import io.github.simonbas.screwbox.examples.platformer.props.Diggable;
import io.github.simonbas.screwbox.examples.platformer.props.Platfom;
import io.github.simonbas.screwbox.examples.platformer.props.VanishingBlock;
import io.github.simonbas.screwbox.examples.platformer.specials.Camera;
import io.github.simonbas.screwbox.examples.platformer.specials.CatCompanion;
import io.github.simonbas.screwbox.examples.platformer.specials.Waypoint;
import io.github.simonbas.screwbox.examples.platformer.specials.player.Player;
import io.github.simonbas.screwbox.examples.platformer.systems.*;
import io.github.simonbas.screwbox.examples.platformer.tiles.NonSolidTile;
import io.github.simonbas.screwbox.examples.platformer.tiles.OneWayGround;
import io.github.simonbas.screwbox.examples.platformer.tiles.SolidGround;
import io.github.simonbas.screwbox.examples.platformer.zones.ChangeMapZone;
import io.github.simonbas.screwbox.examples.platformer.zones.KillZone;
import io.github.simonbas.screwbox.examples.platformer.zones.ReflectionZone;
import io.github.simonbas.screwbox.examples.platformer.zones.ShowLabelZone;
import io.github.simonbas.screwbox.tiled.GameObject;
import io.github.simonbas.screwbox.tiled.Layer;
import io.github.simonbas.screwbox.tiled.Map;
import io.github.simonbas.screwbox.tiled.Tile;

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
    }

    @Override
    public void initialize(final Entities entities) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (nonNull(mapName)) {
            importEntities(entities);
        }

        entities.add(new CombineStaticShadowCastersSystem())
                .add(new LogFpsSystem())
                .add(new RenderLightSystem())
                .add(new ReflectionRenderSystem())
                .add(new CollisionSensorSystem())
                .add(new MovingPlatformSystem())
                .add(new CollectableSystem())
                .add(new CameraMovementSystem())
                .add(new StateSystem())
                .add(new VanishingOnCollisionSystem())
                .add(new ToggleLightSystemsSystem())
                .add(new KilledFromAboveSystem())
                .add(new GroundDetectorSystem())
                .add(new KillZoneSystem())
                .add(new DebugConfigSystem())
                .add(new PauseSystem())
                .add(new ZoomSystem())
                .add(new FadeOutSystem())
                .add(new MovableSystem())
                .add(new DiggableSystem())
                .add(new FollowPlayerSystem())
                .add(new PlayerControlSystem())
                .add(new SmokePuffSystem())
                .add(new ShowLabelSystem())
                .add(new LetsGoSystem())
                .add(new ScreenTransitionSystem())
                .add(new PrintSystem())
                .add(new ChangeMapSystem())
                .add(new ShadowSystem())
                .add(new PhysicsSystem())
                .add(new GravitySystem())
                .add(new CameraShiftSystem())
                .add(new CombineStaticCollidersSystem())
                .add(new DetectLineOfSightToPlayerSystem())
                .add(new PatrollingMovementSystem())
                .add(new AreaTriggerSystem())
                .add(new TimeoutSystem())
                .add(new ResetSceneSystem())
                .add(new AutoFlipSpriteSystem())
                .add(new BackgroundSystem())
                .add(new CatMovementSystem())
                .add(new RenderSystem());
    }

    void importEntities(final Entities entities) {
        entities.add(new Entity()
                .add(new ScreenshotComponent())
                .add(new CurrentLevelComponent(mapName)));

        final Map map = Map.fromJson(mapName);

        entities.importSource(map)
                .as(new MapGravity())
                .as(new WorldInformation())
                .when(propertyIsActive("closed-left")).as(new MapBorderLeft())
                .when(propertyIsActive("closed-left")).as(new MapBorderLeft())
                .when(propertyIsActive("closed-right")).as(new MapBorderRight())
                .when(propertyIsActive("closed-top")).as(new MapBorderTop());

        entities.importSource(map.layers())
                .when(Layer::isImageLayer).as(new Background());

        entities.importSource(map.tiles())
                .usingIndex(this::tileType)
                .when("non-solid").as(new NonSolidTile())
                .when("solid").as(new SolidGround())
                .when("one-way").as(new OneWayGround());

        entities.importSource(map.objects())
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
        if (layerType.isPresent()) {
            return layerType.get();
        }
        return tile.properties().get("type").orElse("none");
    }

}
