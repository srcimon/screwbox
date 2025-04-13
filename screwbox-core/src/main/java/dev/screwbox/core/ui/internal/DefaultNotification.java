package dev.screwbox.core.ui.internal;

import dev.screwbox.core.Percent;
import dev.screwbox.core.Time;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.ui.Notification;
import dev.screwbox.core.ui.NotificationDetails;

import java.util.Optional;

class DefaultNotification implements Notification {

    private final NotificationDetails details;
    private final Time creationTime;
    private Percent progress;

    DefaultNotification(final NotificationDetails details, final Time creationTime) {
        this.details = details;
        this.creationTime = creationTime;
        this.progress = Percent.zero();
    }

    @Override
    public Time timeCreated() {
        return creationTime;
    }

    @Override
    public String text() {
        return details.text();
    }

    @Override
    public Percent progress() {
        return progress;
    }

    @Override
    public Optional<Sprite> icon() {
        return details.icon();
    }

    void updateProgress(final Percent progress) {
        this.progress = progress;
    }
}
