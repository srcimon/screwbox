package io.github.srcimon.screwbox.core.archivements.internal;

import io.github.srcimon.screwbox.core.archivements.Archivement;
import io.github.srcimon.screwbox.core.audio.SoundBundle;
import io.github.srcimon.screwbox.core.graphics.SpriteBundle;
import io.github.srcimon.screwbox.core.ui.NotificationDetails;
import io.github.srcimon.screwbox.core.ui.Ui;

import java.util.function.Consumer;

public class NotifyOnArchivementCompletion implements Consumer<Archivement> {

    private final Ui ui;

    public NotifyOnArchivementCompletion(final Ui ui) {
        this.ui = ui;
    }

    @Override
    public void accept(final Archivement archivement) {
        ui.showNotification(NotificationDetails
                .text("Archivement completed: " + archivement.title())
                .sound(SoundBundle.ARCHIVEMENT)
                .icon(archivement.icon().orElseGet(SpriteBundle.ARCHIVEMENT)));
    }
}
