package io.github.srcimon.screwbox.core.ecosphere.internal;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ecosphere.Entity;
import io.github.srcimon.screwbox.core.ecosphere.systems.RenderLightSystem;
import io.github.srcimon.screwbox.core.ecosphere.systems.RenderSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class DefaultEcosphereTest {

    DefaultEcosphere entities;

    @Mock
    Engine engine;

    @BeforeEach
    void beforeEach() {
        entities = new DefaultEcosphere(engine);
    }

    @Test
    void addEntity_entityNull_exception() {
        assertThatThrownBy(() -> entities.addEntity(null))
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
    void addEntity_freshEntity_addsEntity() {
        Entity freshEntity = new Entity();

        entities.addEntity(freshEntity);

        assertThat(entities.allEntities()).contains(freshEntity);
        assertThat(entities.entityCount()).isEqualTo(1);
    }

    @Test
    void toggleSystem_systemPresent_removesSystem() {
        entities.addSystem(new RenderLightSystem());

        entities.toggleSystem(new RenderLightSystem());

        assertThat(entities.isSystemPresent(RenderLightSystem.class)).isFalse();
    }

    @Test
    void toggleSystem_systemNotPresent_addsSystem() {
        entities.toggleSystem(new RenderLightSystem());

        assertThat(entities.isSystemPresent(RenderLightSystem.class)).isTrue();
    }

    @Test
    void systems_returnsAllSystemsInOrder() {
        RenderLightSystem renderLightSystem = new RenderLightSystem();
        RenderSystem renderSystem = new RenderSystem();

        entities.addSystem(renderLightSystem).addSystem(renderSystem);

        assertThat(entities.systems()).containsExactly(renderSystem, renderLightSystem);
    }

    @Test
    void fetchById_idNotPresent_isEmpty() {
        Optional<Entity> entity = entities.fetchById(149);

        assertThat(entity).isEmpty();
    }

    @Test
    void fetchById_idPresent_returnsEntityWithMatchingId() {
        Entity entityWithMatchingId = new Entity(149);
        entities.addEntity(new Entity(20)).addEntity(entityWithMatchingId);

        Optional<Entity> entity = entities.fetchById(149);

        assertThat(entity).isEqualTo(Optional.of(entityWithMatchingId));
    }

}
