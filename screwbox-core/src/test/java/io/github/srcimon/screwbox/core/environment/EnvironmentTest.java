package io.github.srcimon.screwbox.core.environment;

import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.light.LightRenderSystem;
import io.github.srcimon.screwbox.core.environment.light.OptimizeLightPerformanceSystem;
import io.github.srcimon.screwbox.core.environment.logic.AreaTriggerSystem;
import io.github.srcimon.screwbox.core.environment.logic.StateSystem;
import io.github.srcimon.screwbox.core.environment.physics.*;
import io.github.srcimon.screwbox.core.environment.rendering.*;
import io.github.srcimon.screwbox.core.environment.tweening.TweenDestroySystem;
import io.github.srcimon.screwbox.core.environment.tweening.TweenOpacitySystem;
import io.github.srcimon.screwbox.core.environment.tweening.TweenSystem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EnvironmentTest {

    @Spy
    Environment environment;

    @Test
    void fetchAllHaving_component_callsActualMethod() {
        environment.fetchAllHaving(RenderComponent.class);

        verify(environment).fetchAll(Archetype.of(RenderComponent.class));
    }

    @Test
    void fetchAllHaving_twoComponents_callsActualMethod() {
        environment.fetchAllHaving(RenderComponent.class, TransformComponent.class);

        verify(environment).fetchAll(Archetype.of(RenderComponent.class, TransformComponent.class));
    }

    @Test
    void fetchHaving_component_callsActualMethod() {
        environment.fetchHaving(RenderComponent.class);

        verify(environment).fetch(Archetype.of(RenderComponent.class));
    }

    @Test
    void fetchHaving_twoComponents_callsActualMethod() {
        environment.fetchHaving(RenderComponent.class, TransformComponent.class);

        verify(environment).fetch(Archetype.of(RenderComponent.class, TransformComponent.class));
    }

    @Test
    void forcedFetchHaving_component_callsActualMethod() {
        environment.forcedFetchHaving(RenderComponent.class);

        verify(environment).forcedFetch(Archetype.of(RenderComponent.class));
    }

    @Test
    void forcedFetchHaving_twoComponents_callsActualMethod() {
        environment.forcedFetchHaving(RenderComponent.class, TransformComponent.class);

        verify(environment).forcedFetch(Archetype.of(RenderComponent.class, TransformComponent.class));
    }

    @Test
    void enableTweening_addsTweeningSystems() {
        environment.enableTweening();

        assertThat(environment.systems()).hasSize(3)
                .anyMatch(system -> system.getClass().equals(TweenSystem.class))
                .anyMatch(system -> system.getClass().equals(TweenDestroySystem.class))
                .anyMatch(system -> system.getClass().equals(TweenOpacitySystem.class));
    }

    @Test
    void enableLogic_addsLogicSystems() {
        environment.enableLogic();

        assertThat(environment.systems()).hasSize(2)
                .anyMatch(system -> system.getClass().equals(AreaTriggerSystem.class))
                .anyMatch(system -> system.getClass().equals(StateSystem.class));
    }

    @Test
    void enableRendering_addsRenderingSystems() {
        environment.enableRendering();

        assertThat(environment.systems()).hasSize(5)
                .anyMatch(system -> system.getClass().equals(ReflectionRenderSystem.class))
                .anyMatch(system -> system.getClass().equals(RotateSpriteSystem.class))
                .anyMatch(system -> system.getClass().equals(FlipSpriteSystem.class))
                .anyMatch(system -> system.getClass().equals(ScreenTransitionSystem.class))
                .anyMatch(system -> system.getClass().equals(RenderSystem.class));
    }

    @Test
    void enablePhysics_addsPhysicsSystems() {
        environment.enablePhysics();

        assertThat(environment.systems()).hasSize(6)
                .anyMatch(system -> system.getClass().equals(AutomovementSystem.class))
                .anyMatch(system -> system.getClass().equals(GravitySystem.class))
                .anyMatch(system -> system.getClass().equals(MagnetSystem.class))
                .anyMatch(system -> system.getClass().equals(OptimizePhysicsPerformanceSystem.class))
                .anyMatch(system -> system.getClass().equals(PhysicsSystem.class))
                .anyMatch(system -> system.getClass().equals(CollisionDetectionSystem.class));
    }

    @Test
    void enableLight_addsLightSystems() {
        environment.enableLight();

        assertThat(environment.systems()).hasSize(2)
                .anyMatch(system -> system.getClass().equals(LightRenderSystem.class))
                .anyMatch(system -> system.getClass().equals(OptimizeLightPerformanceSystem.class));
    }
}
