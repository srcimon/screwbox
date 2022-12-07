package de.suzufa.screwbox.examples.platformer.scenes;

import static java.util.Objects.nonNull;

import java.util.Optional;
import java.util.function.Predicate;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Percent;
import de.suzufa.screwbox.core.entities.Entities;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.systems.AreaTriggerSystem;
import de.suzufa.screwbox.core.entities.systems.AutoFlipSpriteSystem;
import de.suzufa.screwbox.core.entities.systems.CameraMovementSystem;
import de.suzufa.screwbox.core.entities.systems.CollisionSensorSystem;
import de.suzufa.screwbox.core.entities.systems.CombineStaticCollidersSystem;
import de.suzufa.screwbox.core.entities.systems.CombineStaticShadowCastersSystem;
import de.suzufa.screwbox.core.entities.systems.FadeOutSystem;
import de.suzufa.screwbox.core.entities.systems.GravitySystem;
import de.suzufa.screwbox.core.entities.systems.LogFpsSystem;
import de.suzufa.screwbox.core.entities.systems.PhysicsSystem;
import de.suzufa.screwbox.core.entities.systems.ReflectionRenderSystem;
import de.suzufa.screwbox.core.entities.systems.RenderLightSystem;
import de.suzufa.screwbox.core.entities.systems.ScreenTransitionSystem;
import de.suzufa.screwbox.core.entities.systems.SpriteRenderSystem;
import de.suzufa.screwbox.core.entities.systems.StateSystem;
import de.suzufa.screwbox.core.entities.systems.TimeoutSystem;
import de.suzufa.screwbox.core.scenes.Scene;
import de.suzufa.screwbox.examples.platformer.collectables.Cherries;
import de.suzufa.screwbox.examples.platformer.collectables.DeboB;
import de.suzufa.screwbox.examples.platformer.collectables.DeboD;
import de.suzufa.screwbox.examples.platformer.collectables.DeboE;
import de.suzufa.screwbox.examples.platformer.collectables.DeboO;
import de.suzufa.screwbox.examples.platformer.components.CurrentLevelComponent;
import de.suzufa.screwbox.examples.platformer.components.ScreenshotComponent;
import de.suzufa.screwbox.examples.platformer.effects.Background;
import de.suzufa.screwbox.examples.platformer.effects.FadeInEffect;
import de.suzufa.screwbox.examples.platformer.enemies.MovingSpikes;
import de.suzufa.screwbox.examples.platformer.enemies.slime.Slime;
import de.suzufa.screwbox.examples.platformer.enemies.tracer.Tracer;
import de.suzufa.screwbox.examples.platformer.map.MapBorderLeft;
import de.suzufa.screwbox.examples.platformer.map.MapBorderRight;
import de.suzufa.screwbox.examples.platformer.map.MapBorderTop;
import de.suzufa.screwbox.examples.platformer.map.MapGravity;
import de.suzufa.screwbox.examples.platformer.map.WorldInformation;
import de.suzufa.screwbox.examples.platformer.props.Box;
import de.suzufa.screwbox.examples.platformer.props.Diggable;
import de.suzufa.screwbox.examples.platformer.props.Platfom;
import de.suzufa.screwbox.examples.platformer.props.VanishingBlock;
import de.suzufa.screwbox.examples.platformer.specials.Camera;
import de.suzufa.screwbox.examples.platformer.specials.CatCompanion;
import de.suzufa.screwbox.examples.platformer.specials.Waypoint;
import de.suzufa.screwbox.examples.platformer.specials.player.Player;
import de.suzufa.screwbox.examples.platformer.systems.BackgroundSystem;
import de.suzufa.screwbox.examples.platformer.systems.CameraShiftSystem;
import de.suzufa.screwbox.examples.platformer.systems.CatMovementSystem;
import de.suzufa.screwbox.examples.platformer.systems.ChangeMapSystem;
import de.suzufa.screwbox.examples.platformer.systems.CollectableSystem;
import de.suzufa.screwbox.examples.platformer.systems.DebugConfigSystem;
import de.suzufa.screwbox.examples.platformer.systems.DetectLineOfSightToPlayerSystem;
import de.suzufa.screwbox.examples.platformer.systems.DiggableSystem;
import de.suzufa.screwbox.examples.platformer.systems.FollowPlayerSystem;
import de.suzufa.screwbox.examples.platformer.systems.GroundDetectorSystem;
import de.suzufa.screwbox.examples.platformer.systems.KillZoneSystem;
import de.suzufa.screwbox.examples.platformer.systems.KilledFromAboveSystem;
import de.suzufa.screwbox.examples.platformer.systems.LetsGoSystem;
import de.suzufa.screwbox.examples.platformer.systems.MovableSystem;
import de.suzufa.screwbox.examples.platformer.systems.MovingPlatformSystem;
import de.suzufa.screwbox.examples.platformer.systems.PatrollingMovementSystem;
import de.suzufa.screwbox.examples.platformer.systems.PauseSystem;
import de.suzufa.screwbox.examples.platformer.systems.PlayerControlSystem;
import de.suzufa.screwbox.examples.platformer.systems.PrintSystem;
import de.suzufa.screwbox.examples.platformer.systems.ResetSceneSystem;
import de.suzufa.screwbox.examples.platformer.systems.ShadowSystem;
import de.suzufa.screwbox.examples.platformer.systems.ShowLabelSystem;
import de.suzufa.screwbox.examples.platformer.systems.SmokePuffSystem;
import de.suzufa.screwbox.examples.platformer.systems.ToggleLightSystemsSystem;
import de.suzufa.screwbox.examples.platformer.systems.VanishingOnCollisionSystem;
import de.suzufa.screwbox.examples.platformer.systems.ZoomSystem;
import de.suzufa.screwbox.examples.platformer.tiles.NonSolidTile;
import de.suzufa.screwbox.examples.platformer.tiles.OneWayGround;
import de.suzufa.screwbox.examples.platformer.tiles.SolidGround;
import de.suzufa.screwbox.examples.platformer.zones.ChangeMapZone;
import de.suzufa.screwbox.examples.platformer.zones.KillZone;
import de.suzufa.screwbox.examples.platformer.zones.ReflectionZone;
import de.suzufa.screwbox.examples.platformer.zones.ShowLabelZone;
import de.suzufa.screwbox.tiled.GameObject;
import de.suzufa.screwbox.tiled.Layer;
import de.suzufa.screwbox.tiled.Map;
import de.suzufa.screwbox.tiled.Tile;

public class GameScene implements Scene {

    private final String mapName;

    public GameScene() {
        this(null);
    }

    public GameScene(final String mapName) {
        this.mapName = mapName;
    }

    @Override
    public void onEnter(Engine engine) {
        engine.graphics().light().setAmbientLight(Percent.of(0.06));
    }

    @Override
    public void initialize(Entities entities) {
        if (nonNull(mapName)) {
            importEntities(entities);
        }

        entities.add(
                new CombineStaticShadowCastersSystem(),
                new LogFpsSystem(),
                new RenderLightSystem(),
                new ReflectionRenderSystem(),
                new CollisionSensorSystem(),
                new MovingPlatformSystem(),
                new CollectableSystem(),
                new CameraMovementSystem(),
                new StateSystem(),
                new VanishingOnCollisionSystem(),
                new ToggleLightSystemsSystem(),
                new KilledFromAboveSystem(),
                new GroundDetectorSystem(),
                new KillZoneSystem(),
                new DebugConfigSystem(),
                new PauseSystem(),
                new ZoomSystem(),
                new FadeOutSystem(),
                new MovableSystem(),
                new DiggableSystem(),
                new FollowPlayerSystem(),
                new PlayerControlSystem(),
                new SmokePuffSystem(),
                new ShowLabelSystem(),
                new LetsGoSystem(),
                new ScreenTransitionSystem(),
                new PrintSystem(),
                new ChangeMapSystem(),
                new ShadowSystem(),
                new PhysicsSystem(),
                new GravitySystem(),
                new CameraShiftSystem(),
                new CombineStaticCollidersSystem(),
                new DetectLineOfSightToPlayerSystem(),
                new PatrollingMovementSystem(),
                new AreaTriggerSystem(),
                new TimeoutSystem(),
                new ResetSceneSystem(),
                new AutoFlipSpriteSystem(),
                new BackgroundSystem(),
                new CatMovementSystem(),
                new SpriteRenderSystem());
    }

    void importEntities(Entities entities) {
        entities.add(new Entity()
                .add(new ScreenshotComponent())
                .add(new CurrentLevelComponent(mapName)));

        Map map = Map.fromJson(mapName);

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

    private Predicate<Map> propertyIsActive(String property) {
        return map -> map.properties().getBoolean(property).orElse(false);
    }

    private String tileType(Tile tile) {
        final Optional<String> layerType = tile.layer().properties().get("type");
        if (layerType.isPresent()) {
            return layerType.get();
        }
        final Optional<String> tileType = tile.properties().get("type");
        return tileType.isPresent() ? tileType.get() : "none";
    }

}
