package de.suzufa.screwbox.examples.pathfinding;

import static de.suzufa.screwbox.core.Bounds.atPosition;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.ScrewBox;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.SourceImport.Converter;
import de.suzufa.screwbox.core.entityengine.components.CameraComponent;
import de.suzufa.screwbox.core.entityengine.components.CameraMovementComponent;
import de.suzufa.screwbox.core.entityengine.components.ColliderComponent;
import de.suzufa.screwbox.core.entityengine.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.entityengine.components.WorldBoundsComponent;
import de.suzufa.screwbox.core.entityengine.systems.CameraMovementSystem;
import de.suzufa.screwbox.core.entityengine.systems.PhysicsSystem;
import de.suzufa.screwbox.core.entityengine.systems.SpriteRenderSystem;
import de.suzufa.screwbox.tiled.GameObject;
import de.suzufa.screwbox.tiled.Map;
import de.suzufa.screwbox.tiled.Tile;
import de.suzufa.screwbox.tiled.TiledSupport;

public class PathfindingExample {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine();

        Map map = TiledSupport.loadMap("maze/map.json");
        var entityEngine = engine.entityEngine();

        entityEngine.importSource(map.allTiles())
                .usingIndex(t -> t.layer().name())
                .when("walls").as(wall())
                .when("floor").as(floor());

        entityEngine.importSource(map)
                .as(worldBounds());

        entityEngine.importSource(map.allObjects())
                .usingIndex(o -> o.name())
                .when("player").as(player())
                .when("camera").as(camera());

        entityEngine
                .add(new SpriteRenderSystem())
                .add(new CameraMovementSystem())
                .add(new PlayerMovementSystem())
                .add(new PhysicsSystem());

        engine.graphics().configuration().setFullscreen(true);
        engine.start();
    }

    // TODO: Physics.createFlowField(area).gridSize(16).towards(target-vector);

    private static Converter<GameObject> camera() {
        return t -> new Entity()
                .add(new TransformComponent(t.bounds()))
                .add(new CameraComponent(2.5))
                .add(new CameraMovementComponent(1, 1));
    }

    private static Converter<GameObject> player() {
        return t -> new Entity(1)
                .add(new PhysicsBodyComponent())
                .add(new TransformComponent(atPosition(t.position(), 8, 8)));
    }

    private static Converter<Map> worldBounds() {
        return t -> new Entity()
                .add(new TransformComponent(t.bounds()))
                .add(new WorldBoundsComponent());
    }

    private static Converter<Tile> floor() {
        return t -> new Entity()
                .add(new SpriteComponent(t.sprite(), t.layer().order()))
                .add(new TransformComponent(t.renderBounds()));
    }

    private static Converter<Tile> wall() {
        return t -> new Entity()
                .add(new SpriteComponent(t.sprite(), t.layer().order()))
                .add(new ColliderComponent())
                .add(new TransformComponent(t.renderBounds()));
    }
}
