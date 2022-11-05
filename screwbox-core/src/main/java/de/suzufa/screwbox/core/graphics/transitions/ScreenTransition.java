package de.suzufa.screwbox.core.graphics.transitions;

import java.io.Serializable;

import de.suzufa.screwbox.core.Percent;
import de.suzufa.screwbox.core.graphics.Window;

public interface ScreenTransition extends Serializable {

    void draw(Window window, Percent progress);
}
