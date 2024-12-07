package io.github.srcimon.screwbox.core.archivements.internal;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.archivements.Archivement;
import io.github.srcimon.screwbox.core.archivements.ArchivementDefinition;
import io.github.srcimon.screwbox.core.archivements.ArchivementDetails;
import io.github.srcimon.screwbox.core.mouse.Mouse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.function.Consumer;

import static java.util.function.Predicate.not;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@MockitoSettings
class DefaultArchivementsTest {

    @InjectMocks
    DefaultArchivements archivements;

    @Mock
    Engine engine;

    @Mock
    Consumer<Archivement> onCompletion;

    public static class MockArchivement implements ArchivementDefinition {

        @Override
        public ArchivementDetails details() {
            return ArchivementDetails.title("i am a mock");
        }
    }

    public static class MockArchivementWithAutocompletion implements ArchivementDefinition {

        @Override
        public ArchivementDetails details() {
            return ArchivementDetails
                    .title("click 2 times")
                    .goal(2);
        }

        @Override
        public int progress(Engine engine) {
            return engine.mouse().isPressedLeft() ? 1 : 0;
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
    void progress_archivementUnknown_throwsException() {
        assertThatThrownBy(() -> archivements.progess(MockArchivement.class, 4))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("archivement not present: MockArchivement");
    }

    @Test
    void progress_archivementAlreadyCompleted_noException() {
        archivements.add(new MockArchivement());
        archivements.progess(MockArchivement.class);
        archivements.update();
        assertThat(archivements.completedArchivements()).hasSize(1);

        assertThatNoException().isThrownBy(() -> archivements.progess(MockArchivement.class, 4));
    }


    @Test
    void completedArchivements_onlyOneActiveArchivement_isEmpty() {
        archivements.add(new MockArchivement());

        assertThat(archivements.completedArchivements()).isEmpty();
    }

    @Test
    void addAllFromClassPackage_containsArchivementClassDefinition_addsArchivement() {
        archivements.addAllFromClassPackage(DefaultArchivementsTest.class);

        assertThat(archivements.activeArchivements()).hasSize(2)
                .anyMatch(archivement -> archivement.title().equals("i am a mock"));
    }

    @Test
    void add_archivementAlreadyAdded_addsSecondArchivement() {
        archivements.add(new MockArchivement());
        archivements.add(new MockArchivement());

        assertThat(archivements.allArchivements()).hasSize(2);
    }

    @Test
    void allArchivements_oneCompletedOneUnarchived_containsBoth() {
        archivements.add(new MockArchivement());
        archivements.progess(MockArchivement.class);
        archivements.update();

        archivements.add(new MockArchivement());

        assertThat(archivements.allArchivements()).hasSize(2);
        assertThat(archivements.activeArchivements()).hasSize(1);
        assertThat(archivements.completedArchivements()).hasSize(1);
    }

    @Test
    void progress_archivementCompleted_causesReactionAfterUpdate() {
        archivements.add(new MockArchivement());
        archivements.progess(MockArchivement.class);

        archivements.update();

        verify(onCompletion).accept(argThat(archivement ->
                archivement.isCompleted() && archivement.title().equals("i am a mock")));
    }

    @Test
    void update_autoCompletedOnSecondUpdate_invokesReactionOnSecondUpdate() {
        var mouse = Mockito.mock(Mouse.class);
        when(engine.mouse()).thenReturn(mouse);

        when(mouse.isPressedLeft()).thenReturn(true);

        archivements.add(new MockArchivementWithAutocompletion());

        archivements.update();

        verifyNoInteractions(onCompletion);

        archivements.update();

        verify(onCompletion).accept(any());
    }

    @Test
    void progress_archivementDefinitionHasProgressMethod_throwsException() {
        archivements.add(new MockArchivementWithAutocompletion());

        assertThatThrownBy(() -> archivements.progess(MockArchivementWithAutocompletion.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("archivement MockArchivementWithAutocompletion uses automatic progression and cannot be updated manually");
    }

    @Test
    void progress_archivmentCompleted_setsCompletionTime() {
        archivements.add(new MockArchivement());

        archivements.progess(MockArchivement.class);

        archivements.update();

        assertThat(archivements.completedArchivements())
                .isNotEmpty()
                .allMatch(Archivement::isCompleted)
                .allMatch(archivement -> archivement.progress().isMax())
                .allMatch(archivement -> archivement.completionTime().isSet());
    }

    @Test
    void reset_oneArchivementCompletedOneNot_bothAreResetted() {
        archivements.add(new MockArchivement());
        archivements.progess(MockArchivement.class);
        archivements.add(new MockArchivement());
        archivements.update();

        archivements.reset();

        assertThat(archivements.activeArchivements()).hasSize(2)
                .allMatch(not(Archivement::isCompleted))
                .allMatch(archivement -> archivement.completionTime().isUnset())
                .allMatch(archivement -> archivement.score() == 0);
    }
}
