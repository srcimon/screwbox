package de.suzufa.screwbox.core.entityengine.internal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entityengine.Entity;

@ExtendWith(MockitoExtension.class)
class DefaultEntityEngineTest {

    @InjectMocks
    private DefaultEntityEngine entityEngine;

    @Mock
    private DefaultEntityManager entityManager;

    @Mock
    private DefaultSystemManager systemMananger;

    @Mock
    private Engine engine;

    @Test
    void add_entityNull_throwsException() {
        assertThatThrownBy(() -> entityEngine.add((Entity) null))
                .isInstanceOf(NullPointerException.class);
    }

}
