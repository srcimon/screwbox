package dev.screwbox.core.graphics;

import dev.screwbox.core.graphics.effects.PostProcessingEffect;

public interface PostProcessing {

    PostProcessing addEffect(PostProcessingEffect effect);

    PostProcessing clearEffects();
}
