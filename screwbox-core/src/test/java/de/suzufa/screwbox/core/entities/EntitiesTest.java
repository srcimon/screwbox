package de.suzufa.screwbox.core.entities;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import de.suzufa.screwbox.core.entities.components.RenderComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;

@ExtendWith(MockitoExtension.class)
class EntitiesTest {

    @Spy
    Entities entities;

    @Test
    void fetchAllHaving_component_callsActualMethod() {
        entities.fetchAllHaving(RenderComponent.class);

        verify(entities).fetchAll(Archetype.of(RenderComponent.class));
    }

    @Test
    void fetchAllHaving_twoComponents_callsActualMethod() {
        entities.fetchAllHaving(RenderComponent.class, TransformComponent.class);

        verify(entities).fetchAll(Archetype.of(RenderComponent.class, TransformComponent.class));
    }

    @Test
    void fetchHaving_component_callsActualMethod() {
        entities.fetchHaving(RenderComponent.class);

        verify(entities).fetch(Archetype.of(RenderComponent.class));
    }

    @Test
    void fetchHaving_twoComponents_callsActualMethod() {
        entities.fetchHaving(RenderComponent.class, TransformComponent.class);

        verify(entities).fetch(Archetype.of(RenderComponent.class, TransformComponent.class));
    }

    @Test
    void forcedFetchHaving_component_callsActualMethod() {
        entities.forcedFetchHaving(RenderComponent.class);

        verify(entities).forcedFetch(Archetype.of(RenderComponent.class));
    }

    @Test
    void forcedFetchHaving_twoComponents_callsActualMethod() {
        entities.forcedFetchHaving(RenderComponent.class, TransformComponent.class);

        verify(entities).forcedFetch(Archetype.of(RenderComponent.class, TransformComponent.class));
    }
}
