package io.github.simonbas.screwbox.core.ui;

import io.github.simonbas.screwbox.core.Engine;

public abstract class UiSubMenu extends UiMenu {

    private final UiMenu caller;

    protected UiSubMenu(final UiMenu caller) {
        this.caller = caller;
    }

    @Override
    public void onExit(final Engine engine) {
        engine.ui().openMenu(caller);
    }

}
