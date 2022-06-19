package de.suzufa.screwbox.examples.pathfinding;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.ScrewBox;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.SourceImport.Converter;
import de.suzufa.screwbox.core.entityengine.components.ColliderComponent;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.entityengine.systems.SpriteRenderSystem;
import de.suzufa.screwbox.tiled.Tile;
import de.suzufa.screwbox.tiled.TiledSupport;

public class PathfindingExample {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine();

        engine.entityEngine().importSource(TiledSupport.loadMap("maze/map.json").allTiles())
                .usingIndex(t -> t.layer().name())
                .when("walls").as(wall())
                .when("floor").as(floor());

        engine.entityEngine().add(new SpriteRenderSystem());

        engine.start();
    }

    // TODO: Physics.createFlowField(area).gridSize(16).towards(target-vector);

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
