package io.github.srcimon.screwbox.core.ui;

import io.github.srcimon.screwbox.core.Time;

public interface Notification {

    Time creationTime();

    String text();
}