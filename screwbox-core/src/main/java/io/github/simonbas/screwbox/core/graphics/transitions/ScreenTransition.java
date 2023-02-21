package io.github.simonbas.screwbox.core.graphics.transitions;

import io.github.simonbas.screwbox.core.Percent;
import io.github.simonbas.screwbox.core.graphics.Screen;

import java.io.Serializable;

public interface ScreenTransition extends Serializable {

    void draw(Screen screen, Percent progress);
}
