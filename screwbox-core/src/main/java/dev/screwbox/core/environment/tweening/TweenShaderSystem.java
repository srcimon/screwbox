package dev.screwbox.core.environment.tweening;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.rendering.RenderComponent;

import static java.util.Objects.nonNull;

/**
 * Tweens progress of shaders of all {@link Entity entities} having {@link TweenShaderComponent}
 *
 * @since 2.18.0
 */
public class TweenShaderSystem implements EntitySystem {

    private static final Archetype TWEENS = Archetype.of(TweenComponent.class, TweenShaderComponent.class, RenderComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var tweenEntity : engine.environment().fetchAll(TWEENS)) {
            final var renderComponent = tweenEntity.get(RenderComponent.class);
            final var advance = tweenEntity.get(TweenComponent.class).value;
            final var invert = tweenEntity.get(TweenShaderComponent.class).invert;
            final var shaderSetup = renderComponent.options.shaderSetup();
            if (nonNull(shaderSetup)) {
                final var updatedShaderSetup = shaderSetup.progress(invert ? advance.invert() : advance);
                renderComponent.options = renderComponent.options.shaderSetup(updatedShaderSetup);
            }
        }
    }
}
