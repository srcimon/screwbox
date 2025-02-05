package io.github.srcimon.screwbox.core.achievements.internal;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.achievements.Achievement;
import io.github.srcimon.screwbox.core.achievements.AchievementDefinition;
import io.github.srcimon.screwbox.core.achievements.AchievementDetails;
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
class DefaultAchievementsTest {

    @InjectMocks
    DefaultAchievements archivements;

    @Mock
    Engine engine;

    @Mock
    Consumer<Achievement> onCompletion;

    public static class MockAchievement implements AchievementDefinition {

        @Override
        public AchievementDetails details() {
            return AchievementDetails.title("i am a mock");
        }
    }

    public static class MockAchievementWithAutocompletion implements AchievementDefinition {

        @Override
        public AchievementDetails details() {
            return AchievementDetails
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
                .hasMessage("achievement must not be null");
    }

    @Test
    void add_archivementValid_addsIncompleteArchivement() {
        archivements.add(new MockAchievement());

        assertThat(archivements.activeArchivements()).hasSize(1)
                .allMatch(archivement -> archivement.title().equals("i am a mock"));
    }

    @Test
    void progess_archivementTypeNull_throwsException() {
        assertThatThrownBy(() -> archivements.progess(null, 1))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("achievementType must not be null");
    }

    @Test
    void progress_archivementUnknown_throwsException() {
        assertThatThrownBy(() -> archivements.progess(MockAchievement.class, 4))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("achievement not present: MockAchievement");
    }

    @Test
    void progress_archivementAlreadyCompleted_noException() {
        archivements.add(new MockAchievement());
        archivements.progess(MockAchievement.class);
        archivements.update();
        assertThat(archivements.completedArchivements()).hasSize(1);

        assertThatNoException().isThrownBy(() -> archivements.progess(MockAchievement.class, 4));
    }


    @Test
    void completedArchivements_onlyOneActiveArchivement_isEmpty() {
        archivements.add(new MockAchievement());

        assertThat(archivements.completedArchivements()).isEmpty();
    }

    @Test
    void addAllFromClassPackage_containsArchivementClassDefinition_addsArchivement() {
        archivements.addAllFromClassPackage(DefaultAchievementsTest.class);

        assertThat(archivements.activeArchivements()).hasSize(2)
                .anyMatch(archivement -> archivement.title().equals("i am a mock"));
    }

    @Test
    void add_archivementAlreadyAdded_addsSecondArchivement() {
        archivements.add(new MockAchievement());
        archivements.add(new MockAchievement());

        assertThat(archivements.allAchivements()).hasSize(2);
    }

    @Test
    void allAchivements_oneCompletedOneUnarchived_containsBoth() {
        archivements.add(new MockAchievement());
        archivements.progess(MockAchievement.class);
        archivements.update();

        archivements.add(new MockAchievement());

        assertThat(archivements.allAchivements()).hasSize(2);
        assertThat(archivements.activeArchivements()).hasSize(1);
        assertThat(archivements.completedArchivements()).hasSize(1);
    }

    @Test
    void progress_archivementCompleted_causesReactionAfterUpdate() {
        archivements.add(new MockAchievement());
        archivements.progess(MockAchievement.class);

        archivements.update();

        verify(onCompletion).accept(argThat(archivement ->
                archivement.isCompleted() && archivement.title().equals("i am a mock")));
    }

    @Test
    void update_autoCompletedOnSecondUpdate_invokesReactionOnSecondUpdate() {
        var mouse = Mockito.mock(Mouse.class);
        when(engine.mouse()).thenReturn(mouse);

        when(mouse.isPressedLeft()).thenReturn(true);

        archivements.add(new MockAchievementWithAutocompletion());

        archivements.update();

        verifyNoInteractions(onCompletion);

        archivements.update();

        verify(onCompletion).accept(any());
    }

    @Test
    void progress_zeroProgress_doesntUpdateArchivementStatus() {
        archivements.add(new MockAchievement());

        archivements.progess(MockAchievement.class, 0);

        assertThat(archivements.activeArchivements()).allMatch(archivement -> archivement.score() == 0);
    }

    @Test
    void progress_negativeProgress_throwsException() {
        archivements.add(new MockAchievement());

        assertThatThrownBy(() -> archivements.progess(MockAchievement.class, -2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("progress must be positive");
    }
    @Test
    void progress_archivementDefinitionHasProgressMethod_throwsException() {
        archivements.add(new MockAchievementWithAutocompletion());

        assertThatThrownBy(() -> archivements.progess(MockAchievementWithAutocompletion.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("achievement MockAchievementWithAutocompletion uses automatic progression and cannot be updated manually");
    }

    @Test
    void progress_archivmentCompleted_setsCompletionTime() {
        archivements.add(new MockAchievement());

        archivements.progess(MockAchievement.class);

        archivements.update();

        assertThat(archivements.completedArchivements())
                .isNotEmpty()
                .allMatch(Achievement::isCompleted)
                .allMatch(archivement -> archivement.progress().isMax())
                .allMatch(archivement -> archivement.completionTime().isSet());
    }

    @Test
    void reset_oneArchivementCompletedOneNot_bothAreResetted() {
        archivements.add(new MockAchievement());
        archivements.progess(MockAchievement.class);
        archivements.add(new MockAchievement());
        archivements.update();

        archivements.reset();

        assertThat(archivements.activeArchivements()).hasSize(2)
                .allMatch(not(Achievement::isCompleted))
                .allMatch(archivement -> archivement.completionTime().isUnset())
                .allMatch(archivement -> archivement.score() == 0);
    }
}
