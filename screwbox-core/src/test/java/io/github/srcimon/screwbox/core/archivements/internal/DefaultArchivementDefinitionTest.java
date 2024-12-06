package io.github.srcimon.screwbox.core.archivements.internal;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.archivements.ArchivementDefinition;
import io.github.srcimon.screwbox.core.archivements.ArchivementDetails;
import io.github.srcimon.screwbox.core.archivements.Archivement;
import io.github.srcimon.screwbox.core.loop.Loop;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@MockitoSettings
class DefaultArchivementDefinitionTest {

    @InjectMocks
    DefaultArchivements archivements;

    @Mock
    Engine engine;

    @Mock
    Consumer<Archivement> completionReaction;

    public static class MockArchivementDefinition implements ArchivementDefinition {

        @Override
        public ArchivementDetails details() {
            return ArchivementDetails.title("i am a mock");
        }
    }

    public static class MockArchivementDefinitionWithTenSecondAutocompletion implements ArchivementDefinition {

        @Override
        public ArchivementDetails details() {
            return ArchivementDetails
                    .title("i am a mock that will atuo complete at 10 seconds runtime")
                    .useAbsoluteProgression()
                    .goal(10);
        }

        @Override
        public int progress(Engine engine) {
            return (int) engine.loop().runningTime().seconds();
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
        archivements.add(new MockArchivementDefinition());

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
        assertThatNoException().isThrownBy(() -> archivements.progess(MockArchivementDefinition.class, 4));
    }

    @Test
    void completedArchivements_onlyOneActiveArchivement_isEmpty() {
        archivements.add(new MockArchivementDefinition());

        assertThat(archivements.completedArchivements()).isEmpty();
    }

    @Test
    void addAllFromClassPackage_containsArchivementClassDefinition_addsArchivement() {
        archivements.addAllFromClassPackage(DefaultArchivementDefinitionTest.class);

        assertThat(archivements.activeArchivements()).hasSize(2)
                .anyMatch(archivement -> archivement.title().equals("i am a mock"));
    }

    @Test
    void add_archivementAlreadyAdded_noException() {
        archivements.add(new MockArchivementDefinition());

        assertThatNoException().isThrownBy(() -> archivements.add(new MockArchivementDefinition()));
    }

    @Test
    void progress_archivementCompleted_causesReactionAfterUpdate() {
        archivements.add(new MockArchivementDefinition());
        archivements.progess(MockArchivementDefinition.class);

        archivements.setCompletionReaction(completionReaction);

        archivements.update();

        verify(completionReaction).accept(argThat(archivement ->
                archivement.isCompleted() && archivement.title().equals("i am a mock")));
    }

    @Test
    void update_autoCompletedOnSecondUpdate_invokesReactionOnSecondUpdate() {
        Loop loop = Mockito.mock(Loop.class);
        when(engine.loop()).thenReturn(loop);
        when(loop.runningTime()).thenReturn(Duration.ofSeconds(5), Duration.ofSeconds(11));
        archivements.setCompletionReaction(completionReaction);

        archivements.add(new MockArchivementDefinitionWithTenSecondAutocompletion());

        archivements.update();

        verifyNoInteractions(completionReaction);

        archivements.update();

        verify(completionReaction).accept(any());
    }
}
