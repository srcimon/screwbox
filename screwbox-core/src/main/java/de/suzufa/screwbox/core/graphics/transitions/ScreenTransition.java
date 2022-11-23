package de.suzufa.screwbox.core.graphics.transitions;

import java.io.Serializable;

import de.suzufa.screwbox.core.Percent;
import de.suzufa.screwbox.core.graphics.Screen;

public interface ScreenTransition extends Serializable {

    void draw(Screen screen, Percent progress);
}
