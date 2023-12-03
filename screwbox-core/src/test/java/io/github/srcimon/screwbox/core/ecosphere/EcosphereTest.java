package io.github.srcimon.screwbox.core.ecosphere;

import io.github.srcimon.screwbox.core.ecosphere.components.RenderComponent;
import io.github.srcimon.screwbox.core.ecosphere.components.TransformComponent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EcosphereTest {

    @Spy
    Ecosphere ecosphere;

    @Test
    void fetchAllHaving_component_callsActualMethod() {
        ecosphere.fetchAllHaving(RenderComponent.class);

        verify(ecosphere).fetchAll(Archetype.of(RenderComponent.class));
    }

    @Test
    void fetchAllHaving_twoComponents_callsActualMethod() {
        ecosphere.fetchAllHaving(RenderComponent.class, TransformComponent.class);

        verify(ecosphere).fetchAll(Archetype.of(RenderComponent.class, TransformComponent.class));
    }

    @Test
    void fetchHaving_component_callsActualMethod() {
        ecosphere.fetchHaving(RenderComponent.class);

        verify(ecosphere).fetch(Archetype.of(RenderComponent.class));
    }

    @Test
    void fetchHaving_twoComponents_callsActualMethod() {
        ecosphere.fetchHaving(RenderComponent.class, TransformComponent.class);

        verify(ecosphere).fetch(Archetype.of(RenderComponent.class, TransformComponent.class));
    }

    @Test
    void forcedFetchHaving_component_callsActualMethod() {
        ecosphere.forcedFetchHaving(RenderComponent.class);

        verify(ecosphere).forcedFetch(Archetype.of(RenderComponent.class));
    }

    @Test
    void forcedFetchHaving_twoComponents_callsActualMethod() {
        ecosphere.forcedFetchHaving(RenderComponent.class, TransformComponent.class);

        verify(ecosphere).forcedFetch(Archetype.of(RenderComponent.class, TransformComponent.class));
    }
}
