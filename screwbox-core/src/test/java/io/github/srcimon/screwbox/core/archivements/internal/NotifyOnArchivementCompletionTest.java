package io.github.srcimon.screwbox.core.archivements.internal;

import io.github.srcimon.screwbox.core.archivements.Archivement;
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
class NotifyOnArchivementCompletionTest {

    @Mock
    Ui ui;

    @Mock
    Archivement archivement;

    @InjectMocks
    NotifyOnArchivementCompletion notifyOnArchivementCompletion;

    @Test
    void accept_noIcon_showsNotificationWithDefaultIcon() {
        when(archivement.title()).thenReturn("clicked 10 times");
        notifyOnArchivementCompletion.accept(archivement);

        verify(ui).showNotification(NotificationDetails
                .text("Archivement completed: clicked 10 times")
                .sound(SoundBundle.ARCHIVEMENT)
                .icon(SpriteBundle.ARCHIVEMENT));
    }

    @Test
    void accept_hasIcon_showsNotificationWithSpecifiedIcon() {
        when(archivement.title()).thenReturn("clicked 10 times");
        when(archivement.icon()).thenReturn(Optional.of(SpriteBundle.EXPLOSION.get()));
        notifyOnArchivementCompletion.accept(archivement);

        verify(ui).showNotification(NotificationDetails
                .text("Archivement completed: clicked 10 times")
                .sound(SoundBundle.ARCHIVEMENT)
                .icon(SpriteBundle.EXPLOSION));
    }
}
