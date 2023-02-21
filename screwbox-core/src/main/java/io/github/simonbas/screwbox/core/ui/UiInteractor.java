package io.github.simonbas.screwbox.core.ui;

import io.github.simonbas.screwbox.core.Engine;

public interface UiInteractor {

    void interactWith(UiMenu menu, UiLayouter layouter, Engine engine);

}
