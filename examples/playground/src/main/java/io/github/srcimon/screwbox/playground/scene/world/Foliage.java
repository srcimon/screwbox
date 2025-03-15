package io.github.srcimon.screwbox.playground.scene.world;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.ShaderSetup;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.graphics.shader.SizeIncreaseShader;
import io.github.srcimon.screwbox.core.utils.AsciiMap;

public class Foliage implements SourceImport.Converter<AsciiMap.Tile> {
    @Override
    public Entity convert(final AsciiMap.Tile tile) {
        return new Entity().name("foliage")
                .bounds(tile.bounds())
                .add(new RenderComponent(Sprite.placeholder(Color.DARK_GREEN, tile.size()), SpriteDrawOptions.originalSize()
                        .shaderSetup(ShaderSetup.combinedShader(new SizeIncreaseShader(8, 0), new FoliageShader()).duration(Duration.ofSeconds(4)))));
    }
}
