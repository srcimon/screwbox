package dev.screwbox.core.environment.internal;

import dev.screwbox.core.environment.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EntityContainerTest {

    EntityContainer entityContainer;

    @BeforeEach
    void setUp() {
        entityContainer = new EntityContainer();
    }

    @Test
    void testEntitiesWillPreserveOrder() {
        Entity first = new Entity(1);
        Entity second = new Entity(2);
        Entity third = new Entity(3);

        entityContainer.add(first);
        entityContainer.add(second);
        entityContainer.add(third);

        assertThat(entityContainer.all()).containsExactly(first, second, third);

        entityContainer.remove(second);
        entityContainer.add(second);

        assertThat(entityContainer.all()).containsExactly(first, third, second);
    }
}
