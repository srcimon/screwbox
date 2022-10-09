package de.suzufa.screwbox.core.ui;

import de.suzufa.screwbox.core.Engine;

public abstract class UiMenuItem {

    private final String label;

    protected UiMenuItem(final String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }

    public abstract void onActivate(Engine engine);

    public boolean isActive(Engine engine) {
        return true;
    }
}
