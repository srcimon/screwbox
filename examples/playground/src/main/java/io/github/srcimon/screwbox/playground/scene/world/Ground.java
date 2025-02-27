package io.github.srcimon.screwbox.playground.scene.world;

import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.physics.ColliderComponent;
import io.github.srcimon.screwbox.core.environment.physics.StaticColliderComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.drawoptions.ShaderOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.graphics.shader.GrayscaleShader;
import io.github.srcimon.screwbox.core.utils.AsciiMap;

public class Ground implements SourceImport.Converter<AsciiMap.Tile> {

    @Override
    public Entity convert(final AsciiMap.Tile tile) {
        return new Entity()
                .name("tile-%s".formatted(tile.value()))
                .add(new ColliderComponent())
                .add(new StaticColliderComponent())
                .add(new RenderComponent(Sprite.placeholder(Color.hex("#005f73"), tile.size()), SpriteDrawOptions.originalSize()
                        .shaderOptions(ShaderOptions.shader(new GrayscaleShader()))))
                .add(new TransformComponent(tile.bounds()));
    }
}
