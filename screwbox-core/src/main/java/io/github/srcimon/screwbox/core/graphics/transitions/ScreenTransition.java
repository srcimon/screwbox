package io.github.srcimon.screwbox.core.graphics.transitions;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Screen;

import java.io.Serializable;

public interface ScreenTransition extends Serializable {

    void draw(Screen screen, Percent progress);
}
