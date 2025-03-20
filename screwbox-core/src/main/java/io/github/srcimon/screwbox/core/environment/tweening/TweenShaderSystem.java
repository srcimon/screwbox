package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;

//TODO document
public class TweenShaderSystem implements EntitySystem {

    private static final Archetype TWEENS = Archetype.of(TweenComponent.class, TweenShaderComponent.class, RenderComponent.class);

    //TODO error when not having any shader
    @Override
    public void update(Engine engine) {
        for (final var tweenEntity : engine.environment().fetchAll(TWEENS)) {
            final var renderComponent = tweenEntity.get(RenderComponent.class);
            final var advance = tweenEntity.get(TweenComponent.class).value;
            final var invert = tweenEntity.get(TweenShaderComponent.class).invert;
            final var updatedShaderSetup = renderComponent.options.shaderSetup().progress(invert ? advance.invert() : advance);
            renderComponent.options = renderComponent.options.shaderSetup(updatedShaderSetup);
        }
    }
}
