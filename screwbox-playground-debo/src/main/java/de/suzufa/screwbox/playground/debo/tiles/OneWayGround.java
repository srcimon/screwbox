package de.suzufa.screwbox.playground.debo.tiles;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.SourceImport.Converter;
import de.suzufa.screwbox.core.entityengine.components.ColliderComponent;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.core.entityengine.components.StaticMarkerComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.entityengine.components.WaterReflectionComponent;
import de.suzufa.screwbox.tiled.Tile;

public class OneWayGround implements Converter<Tile> {

    @Override
    public Entity convert(Tile tile) {
        return new Entity().add(
                new WaterReflectionComponent(),
                new SpriteComponent(tile.sprite(), tile.layer().order()),
                new StaticMarkerComponent(),
                new TransformComponent(tile.renderBounds()),
                new ColliderComponent(500, Percentage.min(), true));
    }

}
