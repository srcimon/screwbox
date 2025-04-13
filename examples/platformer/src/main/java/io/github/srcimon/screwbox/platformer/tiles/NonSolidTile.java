package io.github.srcimon.screwbox.platformer.tiles;

import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport.Converter;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.ShaderBundle;
import io.github.srcimon.screwbox.core.graphics.ShaderSetup;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import dev.screwbox.tiles.Tile;

import static io.github.srcimon.screwbox.core.graphics.options.SpriteDrawOptions.originalSize;

public class NonSolidTile implements Converter<Tile> {

    @Override
    public Entity convert(Tile tile) {
        boolean isFoliage = tile.properties().tryGetBoolean("foliage").orElse(false);
        ShaderSetup shaderSetup = isFoliage ? ShaderBundle.FOLIAGE.get().randomOffset() : null;
        Sprite sprite = tile.sprite();
        if (shaderSetup != null) {
            sprite.prepareShader(shaderSetup.shader());
        }
        return new Entity()
                .add(
                        new RenderComponent(sprite, tile.layer().order(), originalSize().opacity(tile.layer().opacity())),
                        renderComponent -> {
                            renderComponent.options = renderComponent.options.shaderSetup(shaderSetup);
                            renderComponent.parallaxX = tile.layer().parallaxX();
                            renderComponent.parallaxY = tile.layer().parallaxY();
                        })
                .bounds(tile.renderBounds());
    }

}
