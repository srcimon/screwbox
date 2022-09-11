package de.suzufa.screwbox.core.entities.internal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import de.suzufa.screwbox.core.entities.Entity;

@ExtendWith(MockitoExtension.class)
class DefaultEntitiesTest {

    @InjectMocks
    private DefaultEntities entities;

    @Test
    void add_entityNull_throwsException() {
        assertThatThrownBy(() -> entities.add((Entity) null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void importSource_sourceNull_throwsException() {
        assertThatThrownBy(() -> entities.importSource((String) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Source must not be null");
    }

    @Test
    void importSource_sourceLiszNull_throwsException() {
        assertThatThrownBy(() -> entities.importSource((List<String>) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Source must not be null");
    }

}
