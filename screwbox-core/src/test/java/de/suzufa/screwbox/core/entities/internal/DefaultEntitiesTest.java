package de.suzufa.screwbox.core.entities.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.Entity;

@ExtendWith(MockitoExtension.class)
class DefaultEntitiesTest {

    DefaultEntities entities;

    @Mock
    Engine engine;

    @BeforeEach
    void beforeEach() {
        DefaultEntityManager entityManager = new DefaultEntityManager();
        entities = new DefaultEntities(entityManager, new DefaultSystemManager(engine, entityManager));
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

}
