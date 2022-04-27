package de.suzufa.screwbox.core.graphics.internal;

import de.suzufa.screwbox.core.graphics.Font;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.core.graphics.window.WindowCircle;
import de.suzufa.screwbox.core.graphics.window.WindowLine;
import de.suzufa.screwbox.core.graphics.window.WindowPolygon;
import de.suzufa.screwbox.core.graphics.window.WindowRectangle;
import de.suzufa.screwbox.core.graphics.window.WindowRepeatingSprite;
import de.suzufa.screwbox.core.graphics.window.WindowSprite;
import de.suzufa.screwbox.core.graphics.window.WindowText;

public interface Renderer {

    void updateScreen(boolean antialiased);

    Sprite takeScreenshot();

    int calculateTextWidth(String text, Font font);

    void draw(WindowCircle circle);

    void draw(WindowRectangle rectangle);

    void draw(WindowText text);

    void draw(WindowSprite sprite);

    void draw(WindowRepeatingSprite repeatingSprite);

    void draw(WindowLine line);

	void draw(WindowPolygon polygon);
}
