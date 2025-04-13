package dev.screwbox.core.achievements.internal;

import dev.screwbox.core.Engine;
import dev.screwbox.core.achievements.Achievement;
import dev.screwbox.core.achievements.AchievementDefinition;
import dev.screwbox.core.achievements.AchievementDetails;
import dev.screwbox.core.mouse.Mouse;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@MockitoSettings
class DefaultAchievementsTest {

    @InjectMocks
    DefaultAchievements achievements;

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
    void add_achievementIsNull_throwsException() {
        assertThatThrownBy(() -> achievements.add(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("achievement must not be null");
    }

    @Test
    void add_achievementValid_addsIncompleteAchievement() {
        achievements.add(new MockAchievement());

        assertThat(achievements.activeAchievements()).hasSize(1)
                .allMatch(achievement -> achievement.title().equals("i am a mock"));
    }

    @Test
    void progress_achievementTypeNull_throwsException() {
        assertThatThrownBy(() -> achievements.progress(null, 1))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("achievementType must not be null");
    }

    @Test
    void progress_achievementUnknown_throwsException() {
        assertThatThrownBy(() -> achievements.progress(MockAchievement.class, 4))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("achievement not present: MockAchievement");
    }

    @Test
    void progress_achievementAlreadyCompleted_noException() {
        achievements.add(new MockAchievement());
        achievements.progress(MockAchievement.class);
        achievements.update();
        assertThat(achievements.completedAchievements()).hasSize(1);

        assertThatNoException().isThrownBy(() -> achievements.progress(MockAchievement.class, 4));
    }


    @Test
    void completedAchievements_onlyOneActiveAchievement_isEmpty() {
        achievements.add(new MockAchievement());

        assertThat(achievements.completedAchievements()).isEmpty();
    }

    @Test
    void addAllFromClassPackage_containsAchievementClassDefinition_addsAchievement() {
        achievements.addAllFromClassPackage(DefaultAchievementsTest.class);

        assertThat(achievements.activeAchievements()).hasSize(2)
                .anyMatch(achievement -> achievement.title().equals("i am a mock"));
    }

    @Test
    void add_achievementAlreadyAdded_addsSecondAchievement() {
        achievements.add(new MockAchievement());
        achievements.add(new MockAchievement());

        assertThat(achievements.allAchievements()).hasSize(2);
    }

    @Test
    void allAchievements_oneCompletedOneUnarchived_containsBoth() {
        achievements.add(new MockAchievement());
        achievements.progress(MockAchievement.class);
        achievements.update();

        achievements.add(new MockAchievement());

        assertThat(achievements.allAchievements()).hasSize(2);
        assertThat(achievements.activeAchievements()).hasSize(1);
        assertThat(achievements.completedAchievements()).hasSize(1);
    }

    @Test
    void progress_achievementCompleted_causesReactionAfterUpdate() {
        achievements.add(new MockAchievement());
        achievements.progress(MockAchievement.class);

        achievements.update();

        verify(onCompletion).accept(argThat(achievement ->
                achievement.isCompleted() && achievement.title().equals("i am a mock")));
    }

    @Test
    void update_autoCompletedOnSecondUpdate_invokesReactionOnSecondUpdate() {
        var mouse = Mockito.mock(Mouse.class);
        when(engine.mouse()).thenReturn(mouse);

        when(mouse.isPressedLeft()).thenReturn(true);

        achievements.add(new MockAchievementWithAutocompletion());

        achievements.update();

        verifyNoInteractions(onCompletion);

        achievements.update();

        verify(onCompletion).accept(any());
    }

    @Test
    void progress_zeroProgress_doesntUpdateAchievementStatus() {
        achievements.add(new MockAchievement());

        achievements.progress(MockAchievement.class, 0);

        assertThat(achievements.activeAchievements()).allMatch(achievement -> achievement.score() == 0);
    }

    @Test
    void progress_negativeProgress_throwsException() {
        achievements.add(new MockAchievement());

        assertThatThrownBy(() -> achievements.progress(MockAchievement.class, -2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("progress must be positive");
    }

    @Test
    void progress_achievementDefinitionHasProgressMethod_throwsException() {
        achievements.add(new MockAchievementWithAutocompletion());

        assertThatThrownBy(() -> achievements.progress(MockAchievementWithAutocompletion.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("achievement MockAchievementWithAutocompletion uses automatic progression and cannot be updated manually");
    }

    @Test
    void progress_achievementCompleted_setsCompletionTime() {
        achievements.add(new MockAchievement());

        achievements.progress(MockAchievement.class);

        achievements.update();

        assertThat(achievements.completedAchievements())
                .isNotEmpty()
                .allMatch(Achievement::isCompleted)
                .allMatch(achievement -> achievement.progress().isMax())
                .allMatch(achievement -> achievement.completionTime().isSet());
    }

    @Test
    void reset_oneAchievementCompletedOneNot_bothAreResetted() {
        achievements.add(new MockAchievement());
        achievements.progress(MockAchievement.class);
        achievements.add(new MockAchievement());
        achievements.update();

        achievements.reset();

        assertThat(achievements.activeAchievements()).hasSize(2)
                .allMatch(not(Achievement::isCompleted))
                .allMatch(achievement -> achievement.completionTime().isUnset())
                .allMatch(achievement -> achievement.score() == 0);
    }

    @Test
    void setCompletionReaction_reactionNotNull_customizesReaction() {
        Consumer<Achievement> customOnCompletion = Mockito.mock(Consumer.class);
        achievements.setCompletionReaction(customOnCompletion);

        achievements.add(new MockAchievement());
        achievements.progress(MockAchievement.class);

        achievements.update();

        verify(onCompletion, never()).accept(any());
        verify(customOnCompletion).accept(any());
    }

    @Test
    void setCompletionReaction_reactionNull_throwsException() {
        assertThatThrownBy(() -> achievements.setCompletionReaction(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("reaction must not be null");
    }
}
