package io.github.srcimon.screwbox.core.ui.internal;

import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.ui.Notification;
import io.github.srcimon.screwbox.core.ui.NotificationDetails;

class DefaultNotification implements Notification {

    private final NotificationDetails details;
    private final Time creationTime;

    DefaultNotification(final NotificationDetails details, final Time creationTime) {
        this.details = details;
        this.creationTime = creationTime;
    }

    @Override
    public Time timeCreated() {
        return creationTime;
    }

    @Override
    public String text() {
        return details.text();
    }
}
