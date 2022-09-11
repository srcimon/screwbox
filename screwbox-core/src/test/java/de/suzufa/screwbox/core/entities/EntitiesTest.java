package de.suzufa.screwbox.core.entities;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import de.suzufa.screwbox.core.entities.components.SpriteComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;

@ExtendWith(MockitoExtension.class)
class EntitiesTest {

    @Spy
    Entities entities;

    @Test
    void fetchAllHaving_component_callsActualMethod() {
        entities.fetchAllHaving(SpriteComponent.class);

        verify(entities).fetchAll(Archetype.of(SpriteComponent.class));
    }

    @Test
    void fetchAllHaving_twoComponents_callsActualMethod() {
        entities.fetchAllHaving(SpriteComponent.class, TransformComponent.class);

        verify(entities).fetchAll(Archetype.of(SpriteComponent.class, TransformComponent.class));
    }

    @Test
    void fetchHaving_component_callsActualMethod() {
        entities.fetchHaving(SpriteComponent.class);

        verify(entities).fetch(Archetype.of(SpriteComponent.class));
    }

    @Test
    void fetchHaving_twoComponents_callsActualMethod() {
        entities.fetchHaving(SpriteComponent.class, TransformComponent.class);

        verify(entities).fetch(Archetype.of(SpriteComponent.class, TransformComponent.class));
    }

    @Test
    void forcedFetchHaving_component_callsActualMethod() {
        entities.forcedFetchHaving(SpriteComponent.class);

        verify(entities).forcedFetch(Archetype.of(SpriteComponent.class));
    }

    @Test
    void forcedFetchHaving_twoComponents_callsActualMethod() {
        entities.forcedFetchHaving(SpriteComponent.class, TransformComponent.class);

        verify(entities).forcedFetch(Archetype.of(SpriteComponent.class, TransformComponent.class));
    }
}
