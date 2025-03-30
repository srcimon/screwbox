package io.github.srcimon.screwbox.platformer.systems;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.audio.Sound;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.physics.ColliderComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenDestroyComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenShaderComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.options.CameraShakeOptions;
import io.github.srcimon.screwbox.core.graphics.shader.ColorizeShader;
import io.github.srcimon.screwbox.core.physics.Borders;
import io.github.srcimon.screwbox.platformer.components.DiggableComponent;
import io.github.srcimon.screwbox.platformer.components.DiggingComponent;

import static io.github.srcimon.screwbox.core.Duration.ofMillis;
import static io.github.srcimon.screwbox.core.graphics.ShaderSetup.shader;
import static io.github.srcimon.screwbox.core.particles.ParticleOptions.particleSource;

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
