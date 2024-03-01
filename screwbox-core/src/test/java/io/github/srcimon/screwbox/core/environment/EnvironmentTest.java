package io.github.srcimon.screwbox.core.environment;

import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

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
    void tryFetchHaving_component_callsActualMethod() {
        environment.tryFetchHaving(RenderComponent.class);

        verify(environment).tryFetch(Archetype.of(RenderComponent.class));
    }

    @Test
    void tryFetchHaving_twoComponents_callsActualMethod() {
        environment.tryFetchHaving(RenderComponent.class, TransformComponent.class);

        verify(environment).tryFetch(Archetype.of(RenderComponent.class, TransformComponent.class));
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
}
