package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.assets.SpritesBundle;
import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.SpriteDrawOptions;

import java.io.Serial;
import java.util.List;

public class ParticleRenderCustomizeComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public List<Sprite> sprites = List.of(SpritesBundle.MOON_SURFACE_16.get(), SpritesBundle.BLOB_ANIMATED_16.get());
    public int order;
    public SpriteDrawOptions options = SpriteDrawOptions.originalSize();

    //TODO constructors
}
