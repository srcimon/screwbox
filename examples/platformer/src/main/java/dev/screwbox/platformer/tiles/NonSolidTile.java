package dev.screwbox.platformer.tiles;

import dev.screwbox.core.environment.Blueprint;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SourceImport.Converter;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.ShaderBundle;
import dev.screwbox.core.graphics.ShaderSetup;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.tiled.Tile;

import static dev.screwbox.core.graphics.options.SpriteDrawOptions.originalSize;

public class NonSolidTile implements Blueprint<Tile> {

    @Override
    public Entity assembleFrom(Tile tile) {
        boolean isFoliage = tile.properties().tryGetBoolean("foliage").orElse(false);
        ShaderSetup shaderSetup = isFoliage ? ShaderBundle.FOLIAGE.get().randomOffset() : null;
        Sprite sprite = tile.sprite();
        if (shaderSetup != null) {
            sprite.compileShader(shaderSetup.shader());
        }
        return new Entity()
                .add(new RenderComponent(sprite, originalSize().opacity(tile.layer().opacity()).drawOrder(tile.layer().order())),
                        renderComponent -> {
                            renderComponent.options = renderComponent.options.shaderSetup(shaderSetup);
                            renderComponent.parallaxX = tile.layer().parallaxX();
                            renderComponent.parallaxY = tile.layer().parallaxY();
                        })
                .bounds(tile.bounds());
    }

}
