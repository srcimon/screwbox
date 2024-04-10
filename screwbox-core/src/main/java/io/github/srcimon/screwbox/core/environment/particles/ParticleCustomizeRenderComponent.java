package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.SpriteDrawOptions;

import java.io.Serial;
import java.util.List;
import java.util.function.Supplier;

public class ParticleCustomizeRenderComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final List<Sprite> sprites;
    public final int order;
    public final SpriteDrawOptions options;

    public ParticleCustomizeRenderComponent(final Supplier<Sprite> sprite, final SpriteDrawOptions options) {
        this(sprite.get(), options);
    }

    public ParticleCustomizeRenderComponent(final Sprite sprite, final SpriteDrawOptions options) {
        this(List.of(sprite), 0, options);
    }

    public ParticleCustomizeRenderComponent(final List<Sprite> sprites, final int order, final SpriteDrawOptions options) {
        this.sprites = sprites;
        this.order = order;
        this.options = options;
    }
}
