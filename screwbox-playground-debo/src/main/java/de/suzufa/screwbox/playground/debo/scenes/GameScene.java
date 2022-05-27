package de.suzufa.screwbox.playground.debo.scenes;

import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntityEngine;
import de.suzufa.screwbox.core.entityengine.systems.AreaTriggerSystem;
import de.suzufa.screwbox.core.entityengine.systems.CameraMovementSystem;
import de.suzufa.screwbox.core.entityengine.systems.CollisionSensorSystem;
import de.suzufa.screwbox.core.entityengine.systems.CombineStaticCollidersSystem;
import de.suzufa.screwbox.core.entityengine.systems.FadeOutSystem;
import de.suzufa.screwbox.core.entityengine.systems.GravitySystem;
import de.suzufa.screwbox.core.entityengine.systems.LogFpsSystem;
import de.suzufa.screwbox.core.entityengine.systems.PhysicsSystem;
import de.suzufa.screwbox.core.entityengine.systems.ScreenTransitionSystem;
import de.suzufa.screwbox.core.entityengine.systems.SpriteRenderSystem;
import de.suzufa.screwbox.core.entityengine.systems.StateSystem;
import de.suzufa.screwbox.core.entityengine.systems.TimeoutSystem;
import de.suzufa.screwbox.core.scenes.Scene;
import de.suzufa.screwbox.playground.debo.DeboMapConverter;
import de.suzufa.screwbox.playground.debo.collectables.CherriesConverter;
import de.suzufa.screwbox.playground.debo.collectables.DeboBConverter;
import de.suzufa.screwbox.playground.debo.collectables.DeboDConverter;
import de.suzufa.screwbox.playground.debo.collectables.DeboEConverter;
import de.suzufa.screwbox.playground.debo.collectables.DeboOConverter;
import de.suzufa.screwbox.playground.debo.components.CurrentLevelComponent;
import de.suzufa.screwbox.playground.debo.components.ScreenshotComponent;
import de.suzufa.screwbox.playground.debo.effects.BackgroundConverter;
import de.suzufa.screwbox.playground.debo.effects.FadeInConverter;
import de.suzufa.screwbox.playground.debo.enemies.MovingSpikesConverter;
import de.suzufa.screwbox.playground.debo.enemies.slime.SlimeConverter;
import de.suzufa.screwbox.playground.debo.enemies.tracer.TracerConverter;
import de.suzufa.screwbox.playground.debo.map.CloseMapBottomConverter;
import de.suzufa.screwbox.playground.debo.map.CloseMapLeftConverter;
import de.suzufa.screwbox.playground.debo.map.CloseMapRightConverter;
import de.suzufa.screwbox.playground.debo.map.CloseMapTopConverter;
import de.suzufa.screwbox.playground.debo.map.MapGravityConverter;
import de.suzufa.screwbox.playground.debo.map.WorldBoundsConverter;
import de.suzufa.screwbox.playground.debo.props.BoxConverter;
import de.suzufa.screwbox.playground.debo.props.DiggableConverter;
import de.suzufa.screwbox.playground.debo.props.PlatfomConverter;
import de.suzufa.screwbox.playground.debo.props.VanishingBlockConverter;
import de.suzufa.screwbox.playground.debo.specials.CameraConverter;
import de.suzufa.screwbox.playground.debo.specials.CatConverter;
import de.suzufa.screwbox.playground.debo.specials.WaypointConverter;
import de.suzufa.screwbox.playground.debo.specials.player.PlayerConverter;
import de.suzufa.screwbox.playground.debo.systems.AutoflipByMovementSystem;
import de.suzufa.screwbox.playground.debo.systems.BackgroundSystem;
import de.suzufa.screwbox.playground.debo.systems.CameraShiftSystem;
import de.suzufa.screwbox.playground.debo.systems.CatMovementSystem;
import de.suzufa.screwbox.playground.debo.systems.ChangeMapSystem;
import de.suzufa.screwbox.playground.debo.systems.CollectableSystem;
import de.suzufa.screwbox.playground.debo.systems.DebugConfigSystem;
import de.suzufa.screwbox.playground.debo.systems.DetectLineOfSightToPlayerSystem;
import de.suzufa.screwbox.playground.debo.systems.DiggableSystem;
import de.suzufa.screwbox.playground.debo.systems.FollowPlayerSystem;
import de.suzufa.screwbox.playground.debo.systems.GroundDetectorSystem;
import de.suzufa.screwbox.playground.debo.systems.KillZoneSystem;
import de.suzufa.screwbox.playground.debo.systems.KilledFromAboveSystem;
import de.suzufa.screwbox.playground.debo.systems.LetsGoSystem;
import de.suzufa.screwbox.playground.debo.systems.MovableSystem;
import de.suzufa.screwbox.playground.debo.systems.MovingPlattformSystem;
import de.suzufa.screwbox.playground.debo.systems.PatrollingMovementSystem;
import de.suzufa.screwbox.playground.debo.systems.PauseSystem;
import de.suzufa.screwbox.playground.debo.systems.PlayerControlSystem;
import de.suzufa.screwbox.playground.debo.systems.PrintSystem;
import de.suzufa.screwbox.playground.debo.systems.ResetSceneSystem;
import de.suzufa.screwbox.playground.debo.systems.ShadowSystem;
import de.suzufa.screwbox.playground.debo.systems.ShowLabelSystem;
import de.suzufa.screwbox.playground.debo.systems.SmokePuffSystem;
import de.suzufa.screwbox.playground.debo.systems.VanishingOnCollisionSystem;
import de.suzufa.screwbox.playground.debo.systems.ZoomSystem;
import de.suzufa.screwbox.playground.debo.tiles.NonSolidConverter;
import de.suzufa.screwbox.playground.debo.tiles.OneWayConverter;
import de.suzufa.screwbox.playground.debo.tiles.SolidConverter;
import de.suzufa.screwbox.playground.debo.zones.ChangeMapZoneConverter;
import de.suzufa.screwbox.playground.debo.zones.KillZoneConverter;
import de.suzufa.screwbox.playground.debo.zones.ShowLabelZoneConverter;
import de.suzufa.screwbox.tiled.GameConverter;
import de.suzufa.screwbox.tiled.GameObject;
import de.suzufa.screwbox.tiled.Layer;
import de.suzufa.screwbox.tiled.Map;
import de.suzufa.screwbox.tiled.Tile;
import de.suzufa.screwbox.tiled.TiledSupport;

public class GameScene implements Scene {

    private final String mapName;

    public GameScene(final String mapName) {
        this.mapName = mapName;
    }

    @Override
    public void initialize(EntityEngine entityEngine) {
        Map map = TiledSupport.loadMap(mapName);
        DeboMapConverter mapConverter = new DeboMapConverter();
        entityEngine
                .add(gameConverter(map).createEnttiesFrom(map))// TODO: REDUNDANT MAP PARAMETER
                .add(new Entity().add(new ScreenshotComponent(), new CurrentLevelComponent(mapName)));

        entityEngine.add(
                new LogFpsSystem(),
                new CollisionSensorSystem(),
                new MovingPlattformSystem(),
                new CollectableSystem(),
                new CameraMovementSystem(),
                new StateSystem(),
                new VanishingOnCollisionSystem(),
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
                new AutoflipByMovementSystem(),
                new BackgroundSystem(),
                new CatMovementSystem(),
                new SpriteRenderSystem());
    }

    private GameConverter<Map> gameConverter(Map map) {
        return new GameConverter<Map>()
                .add(map.layerExtractor())
                .add(map.mapExtractor())
                .add(map.objectExtractor())
                .add(map.tileExtractor())
                .add(new CloseMapLeftConverter(), Map.class)
                .add(new CloseMapRightConverter(), Map.class)
                .add(new CloseMapBottomConverter(), Map.class)
                .add(new CloseMapTopConverter(), Map.class)
                .add(new MapGravityConverter(), Map.class)
                .add(new WorldBoundsConverter(), Map.class)
                .add(new BackgroundConverter(), Layer.class)
                .add(new NonSolidConverter(), Tile.class)
                .add(new SolidConverter(), Tile.class)
                .add(new OneWayConverter(), Tile.class)
                .add(new CatConverter(), GameObject.class)
                .add(new MovingSpikesConverter(), GameObject.class)
                .add(new VanishingBlockConverter(), GameObject.class)
                .add(new SlimeConverter(), GameObject.class)
                .add(new PlatfomConverter(), GameObject.class)
                .add(new WaypointConverter(), GameObject.class)
                .add(new CameraConverter(), GameObject.class)
                .add(new PlayerConverter(), GameObject.class)
                .add(new DeboDConverter(), GameObject.class)
                .add(new DeboEConverter(), GameObject.class)
                .add(new DeboBConverter(), GameObject.class)
                .add(new DeboOConverter(), GameObject.class)
                .add(new CherriesConverter(), GameObject.class)
                .add(new KillZoneConverter(), GameObject.class)
                .add(new BoxConverter(), GameObject.class)
                .add(new DiggableConverter(), GameObject.class)
                .add(new ChangeMapZoneConverter(), GameObject.class)
                .add(new ShowLabelZoneConverter(), GameObject.class)
                .add(new FadeInConverter(), GameObject.class)
                .add(new TracerConverter(), GameObject.class);
    }

}
