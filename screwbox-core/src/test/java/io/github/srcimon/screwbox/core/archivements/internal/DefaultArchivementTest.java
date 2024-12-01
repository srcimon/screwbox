package io.github.srcimon.screwbox.core.archivements.internal;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.archivements.Archivement;
import io.github.srcimon.screwbox.core.archivements.ArchivementConfiguration;
import io.github.srcimon.screwbox.core.archivements.ArchivementInfo;
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
class DefaultArchivementTest {

    @InjectMocks
    DefaultArchivements archivements;

    @Mock
    Engine engine;

    @Mock
    Consumer<ArchivementInfo> completionReaction;

    public static class MockArchivement implements Archivement {

        @Override
        public ArchivementConfiguration configuration() {
            return ArchivementConfiguration.title("i am a mock");
        }
    }

    public static class MockArchivementWithTenSecondAutocompletion implements Archivement {

        @Override
        public ArchivementConfiguration configuration() {
            return ArchivementConfiguration
                    .title("i am a mock that will atuo complete at 10 seconds runtime")
                    .useFixedProgressMode()
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

        assertThat(archivements.activeArchivements()).hasSize(2)
                .anyMatch(archivement -> archivement.title().equals("i am a mock"));
    }

    @Test
    void add_archivementAlreadyAdded_throwsException() {
        archivements.add(new MockArchivement());

        var duplicateArchivement = new MockArchivement();
        assertThatThrownBy(() -> archivements.add(duplicateArchivement))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("archivement already present: MockArchivement");
    }

    @Test
    void progress_archivementCompleted_causesReactionAfterUpdate() {
        archivements.add(new MockArchivement());
        archivements.progess(MockArchivement.class);

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

        archivements.add(new MockArchivementWithTenSecondAutocompletion());

        archivements.update();

        verifyNoInteractions(completionReaction);

        archivements.update();

        verify(completionReaction).accept(any());
    }
}
