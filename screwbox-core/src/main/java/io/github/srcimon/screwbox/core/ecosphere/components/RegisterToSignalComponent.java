package io.github.srcimon.screwbox.core.ecosphere.components;

import io.github.srcimon.screwbox.core.ecosphere.Component;

import java.io.Serial;

public final class RegisterToSignalComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final int id;

    public RegisterToSignalComponent(final int id) {
        this.id = id;
    }
}
