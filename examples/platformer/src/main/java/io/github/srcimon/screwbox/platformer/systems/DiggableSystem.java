package io.github.srcimon.screwbox.platformer.systems;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Ease;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.audio.Sound;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.environment.tweening.TweenComponent;
import dev.screwbox.core.environment.tweening.TweenDestroyComponent;
import dev.screwbox.core.environment.tweening.TweenShaderComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.options.CameraShakeOptions;
import dev.screwbox.core.graphics.shader.ColorizeShader;
import dev.screwbox.core.physics.Borders;
import io.github.srcimon.screwbox.platformer.components.DiggableComponent;
import io.github.srcimon.screwbox.platformer.components.DiggingComponent;

import static dev.screwbox.core.Duration.ofMillis;
import static dev.screwbox.core.graphics.ShaderSetup.shader;
import static dev.screwbox.core.particles.ParticleOptions.particleSource;

@Order(Order.SystemOrder.SIMULATION_EARLY)
public class DiggableSystem implements EntitySystem {

    private static final Archetype DIGGINGS = Archetype.of(DiggingComponent.class, PhysicsComponent.class);
    private static final Archetype DIGGABLES = Archetype.ofSpacial(DiggableComponent.class, RenderComponent.class);
    private static final Asset<Sound> DIG_SOUND = Sound.assetFromFile("sounds/dig.wav");

    @Override
    public void update(final Engine engine) {
        for (final var digging : engine.environment().fetchAll(DIGGINGS)) {
            engine.physics().raycastFrom(digging.position())
                    .checkingFor(DIGGABLES)
                    .checkingBorders(Borders.TOP_ONLY)
                    .castingVertical(14)
                    .selectAnyEntity().ifPresent(entity -> {
                        if (!entity.hasComponent(TweenComponent.class)) {
                            engine.graphics().camera().shake(CameraShakeOptions.lastingForDuration(Duration.oneSecond()).strength(8));
                            entity.add(new TweenDestroyComponent());
                            RenderComponent renderComponent = entity.get(RenderComponent.class);
                            engine.particles().spawnMultiple(10, entity.bounds(), particleSource(entity)
                                    .sprite(renderComponent.sprite)
                                    .randomBaseSpeed(30, 80)
                                    .customize("add-gravity", e -> e.get(PhysicsComponent.class).gravityModifier = 0.4)
                                    .randomStartRotation()
                                    .randomRotation(-0.5, 0.5)
                                    .animateScale(0, 0.5));
                            renderComponent.options = renderComponent.options.shaderSetup(shader(new ColorizeShader(Color.WHITE)));
                            entity.add(new TweenShaderComponent(true));
                            entity.add(new TweenComponent(ofMillis(300), Ease.SINE_OUT));
                            entity.remove(ColliderComponent.class);
                            var physicsComponent = digging.get(PhysicsComponent.class);
                            physicsComponent.momentum = Vector.of(physicsComponent.momentum.x(), -150);
                            engine.audio().playSound(DIG_SOUND);
                        }
                    });

        }
    }
}
