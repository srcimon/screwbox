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

public class StandbyRenderer implements Renderer {

    @Override
    public void updateScreen(final boolean antialiased) {
        // does nothing
    }

    @Override
    public Sprite takeScreenshot() {
        return null;
    }

    @Override
    public int calculateTextWidth(final String text, final Font font) {
        return 0;
    }

    @Override
    public void draw(final WindowRectangle rectangle) {
        // does nothing
    }

    @Override
    public void draw(final WindowText text) {
        // does nothing
    }

    @Override
    public void draw(final WindowSprite sprite) {
        // does nothing
    }

    @Override
    public void draw(final WindowRepeatingSprite repeatingSprite) {
        // does nothing
    }

    @Override
    public void draw(final WindowLine line) {
        // does nothing
    }

    @Override
    public void draw(final WindowCircle circle) {
        // does nothing
    }

	@Override
	public void draw(final WindowPolygon polygon) {
		// does nothing
	}

}
