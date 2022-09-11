package de.suzufa.screwbox.core.entities;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import de.suzufa.screwbox.core.entities.components.SpriteComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;

@ExtendWith(MockitoExtension.class)
class EntityEngineTest {

    @Spy
    EntityEngine entityEngine;

    @Test
    void fetchAllHaving_component_callsActualMethod() {
        entityEngine.fetchAllHaving(SpriteComponent.class);

        verify(entityEngine).fetchAll(Archetype.of(SpriteComponent.class));
    }

    @Test
    void fetchAllHaving_twoComponents_callsActualMethod() {
        entityEngine.fetchAllHaving(SpriteComponent.class, TransformComponent.class);

        verify(entityEngine).fetchAll(Archetype.of(SpriteComponent.class, TransformComponent.class));
    }

    @Test
    void fetchHaving_component_callsActualMethod() {
        entityEngine.fetchHaving(SpriteComponent.class);

        verify(entityEngine).fetch(Archetype.of(SpriteComponent.class));
    }

    @Test
    void fetchHaving_twoComponents_callsActualMethod() {
        entityEngine.fetchHaving(SpriteComponent.class, TransformComponent.class);

        verify(entityEngine).fetch(Archetype.of(SpriteComponent.class, TransformComponent.class));
    }

    @Test
    void forcedFetchHaving_component_callsActualMethod() {
        entityEngine.forcedFetchHaving(SpriteComponent.class);

        verify(entityEngine).forcedFetch(Archetype.of(SpriteComponent.class));
    }

    @Test
    void forcedFetchHaving_twoComponents_callsActualMethod() {
        entityEngine.forcedFetchHaving(SpriteComponent.class, TransformComponent.class);

        verify(entityEngine).forcedFetch(Archetype.of(SpriteComponent.class, TransformComponent.class));
    }
}
