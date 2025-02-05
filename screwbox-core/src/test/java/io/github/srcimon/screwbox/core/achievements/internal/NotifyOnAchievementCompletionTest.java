package io.github.srcimon.screwbox.core.achievements.internal;

import io.github.srcimon.screwbox.core.achievements.Achievement;
import io.github.srcimon.screwbox.core.audio.SoundBundle;
import io.github.srcimon.screwbox.core.graphics.SpriteBundle;
import io.github.srcimon.screwbox.core.ui.NotificationDetails;
import io.github.srcimon.screwbox.core.ui.Ui;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MockitoSettings
class NotifyOnAchievementCompletionTest {

    @Mock
    Ui ui;

    @Mock
    Achievement achievement;

    @InjectMocks
    NotifyOnAchievementCompletion notifyOnAchievementCompletion;

    @Test
    void accept_noIcon_showsNotificationWithDefaultIcon() {
        when(achievement.title()).thenReturn("clicked 10 times");
        notifyOnAchievementCompletion.accept(achievement);

        verify(ui).showNotification(NotificationDetails
                .text("Achievement completed: clicked 10 times")
                .sound(SoundBundle.ACHIEVEMENT)
                .icon(SpriteBundle.ACHIEVEMENT));
    }

    @Test
    void accept_hasIcon_showsNotificationWithSpecifiedIcon() {
        when(achievement.title()).thenReturn("clicked 10 times");
        when(achievement.icon()).thenReturn(Optional.of(SpriteBundle.EXPLOSION.get()));
        notifyOnAchievementCompletion.accept(achievement);

        verify(ui).showNotification(NotificationDetails
                .text("Achievement completed: clicked 10 times")
                .sound(SoundBundle.ACHIEVEMENT)
                .icon(SpriteBundle.EXPLOSION));
    }
}
