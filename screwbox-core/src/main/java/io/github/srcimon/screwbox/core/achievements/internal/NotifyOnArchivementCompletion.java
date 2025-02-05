package io.github.srcimon.screwbox.core.achievements.internal;

import io.github.srcimon.screwbox.core.achievements.Achievement;
import io.github.srcimon.screwbox.core.audio.SoundBundle;
import io.github.srcimon.screwbox.core.graphics.SpriteBundle;
import io.github.srcimon.screwbox.core.ui.NotificationDetails;
import io.github.srcimon.screwbox.core.ui.Ui;

import java.util.function.Consumer;

public class NotifyOnArchivementCompletion implements Consumer<Achievement> {

    private final Ui ui;

    public NotifyOnArchivementCompletion(final Ui ui) {
        this.ui = ui;
    }

    @Override
    public void accept(final Achievement achievement) {
        ui.showNotification(NotificationDetails
                .text("Achievement completed: " + achievement.title())
                .sound(SoundBundle.ARCHIVEMENT)
                .icon(achievement.icon().orElseGet(SpriteBundle.ARCHIVEMENT)));
    }
}
