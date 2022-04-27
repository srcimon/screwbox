package de.suzufa.screwbox.core.graphics.transitions;

import java.io.Serializable;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.graphics.window.Window;

public interface ScreenTransition extends Serializable {

    void draw(Window screen, Percentage progress);
}
