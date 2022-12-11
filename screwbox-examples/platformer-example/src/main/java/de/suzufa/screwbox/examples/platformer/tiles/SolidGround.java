package de.suzufa.screwbox.examples.platformer.tiles;

import de.suzufa.screwbox.core.Percent;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.SourceImport.Converter;
import de.suzufa.screwbox.core.entities.components.ColliderComponent;
import de.suzufa.screwbox.core.entities.components.ShadowCasterComponent;
import de.suzufa.screwbox.core.entities.components.RenderComponent;
import de.suzufa.screwbox.core.entities.components.StaticShadowCasterMarkerComponent;
import de.suzufa.screwbox.core.entities.components.StaticMarkerComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.tiled.Tile;

public class SolidGround implements Converter<Tile> {

    @Override
    public Entity convert(Tile tile) {
        return new Entity().add(
                new RenderComponent(tile.sprite(), tile.layer().order()),
                new TransformComponent(tile.renderBounds()),
                new StaticMarkerComponent(),
                new StaticShadowCasterMarkerComponent(),
                new ShadowCasterComponent(),
                new ColliderComponent(500, Percent.min()));
    }

}
