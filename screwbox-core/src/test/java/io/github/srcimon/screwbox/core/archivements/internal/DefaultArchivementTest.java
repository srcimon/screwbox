package io.github.srcimon.screwbox.core.archivements.internal;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.archivements.Archivement;
import io.github.srcimon.screwbox.core.archivements.ArchivementConfiguration;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@MockitoSettings
class DefaultArchivementTest {

    @InjectMocks
    DefaultArchivements archivements;

    @Mock
    Engine engine;

    public static class MockArchivement implements Archivement {

        @Override
        public ArchivementConfiguration configuration() {
            return ArchivementConfiguration.title("i am a mock");
        }
    }

    @Test
    void add_archivementIsNull_throwsException() {
        assertThatThrownBy(() -> archivements.add(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("archivement must not be null");
    }

    @Test
    void add_archivementValid_addsIncompleteArchivement() {
        archivements.add(new MockArchivement());

        assertThat(archivements.activeArchivements()).hasSize(1)
                .allMatch(archivement -> archivement.title().equals("i am a mock"));
    }

    @Test
    void progess_archivementFamilyNull_throwsException() {
        assertThatThrownBy(() -> archivements.progess(null, 1))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("archivement family must not be null");
    }

    @Test
    void progress_noArchivementForUpdateFound_noException() {
        assertThatNoException().isThrownBy(() -> archivements.progess(MockArchivement.class, 4));
    }

    @Test
    void completedArchivements_onlyOneActiveArchivement_isEmpty() {
        archivements.add(new MockArchivement());

        assertThat(archivements.completedArchivements()).isEmpty();
    }

    @Test
    void addAllFromClassPackage_containsArchivementClassDefinition_addsArchivement() {
        archivements.addAllFromClassPackage(DefaultArchivementTest.class);

        assertThat(archivements.activeArchivements()).hasSize(1)
                .allMatch(archivement -> archivement.title().equals("i am a mock"));
    }
}
