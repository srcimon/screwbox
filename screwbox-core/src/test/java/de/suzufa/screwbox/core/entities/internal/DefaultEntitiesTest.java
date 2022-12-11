package de.suzufa.screwbox.core.entities.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.systems.RenderLightSystem;
import de.suzufa.screwbox.core.entities.systems.SpriteRenderSystem;

@ExtendWith(MockitoExtension.class)
class DefaultEntitiesTest {

    DefaultEntities entities;

    @Mock
    Engine engine;

    @BeforeEach
    void beforeEach() {
        entities = new DefaultEntities(engine);
    }

    @Test
    void add_entityNull_exception() {
        assertThatThrownBy(() -> entities.add((Entity) null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void importSource_sourceNull_exception() {
        assertThatThrownBy(() -> entities.importSource((String) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Source must not be null");
    }

    @Test
    void importSource_sourceLiszNull_exception() {
        assertThatThrownBy(() -> entities.importSource((List<String>) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Source must not be null");
    }

    @Test
    void add_freshEntity_addsEntity() {
        Entity freshEntity = new Entity();

        entities.add(freshEntity);

        assertThat(entities.allEntities()).contains(freshEntity);
        assertThat(entities.entityCount()).isEqualTo(1);
    }

    @Test
    void toggleSystem_systemPresent_removesSystem() {
        entities.add(new RenderLightSystem());

        entities.toggleSystem(new RenderLightSystem());

        assertThat(entities.isSystemPresent(RenderLightSystem.class)).isFalse();
    }

    @Test
    void toggleSystem_systemNotPresent_addsSystem() {
        entities.toggleSystem(new RenderLightSystem());

        assertThat(entities.isSystemPresent(RenderLightSystem.class)).isTrue();
    }

    @Test
    void allSystems_returnsAllSystemsInOrder() {
        RenderLightSystem renderLightSystem = new RenderLightSystem();
        SpriteRenderSystem spriteRenderSystem = new SpriteRenderSystem();

        entities.add(renderLightSystem).add(spriteRenderSystem);

        assertThat(entities.allSystems()).containsExactly(spriteRenderSystem, renderLightSystem);
    }

    @Test
    void fetchById_idNotPresent_isEmpty() {
        Optional<Entity> entity = entities.fetchById(149);

        assertThat(entity).isEmpty();
    }

    @Test
    void fetchById_idPresent_returnsEntityWithMatchingId() {
        Entity entityWithMatchingId = new Entity(149);
        entities.add(new Entity(20)).add(entityWithMatchingId);

        Optional<Entity> entity = entities.fetchById(149);

        assertThat(entity).isEqualTo(Optional.of(entityWithMatchingId));
    }

}
