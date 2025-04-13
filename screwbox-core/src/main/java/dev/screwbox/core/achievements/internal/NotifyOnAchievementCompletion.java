package dev.screwbox.core.achievements.internal;

import dev.screwbox.core.achievements.Achievement;
import dev.screwbox.core.audio.SoundBundle;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.ui.NotificationDetails;
import dev.screwbox.core.ui.Ui;

import java.util.function.Consumer;

public class NotifyOnAchievementCompletion implements Consumer<Achievement> {

    private final Ui ui;

    public NotifyOnAchievementCompletion(final Ui ui) {
        this.ui = ui;
    }

    @Override
    public void accept(final Achievement achievement) {
        ui.showNotification(NotificationDetails
                .text("Achievement completed: " + achievement.title())
                .sound(SoundBundle.ACHIEVEMENT)
                .icon(achievement.icon().orElseGet(SpriteBundle.ACHIEVEMENT)));
    }
}
