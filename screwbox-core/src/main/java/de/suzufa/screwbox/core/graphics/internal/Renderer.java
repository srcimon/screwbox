package de.suzufa.screwbox.core.graphics.internal;

import java.util.concurrent.Future;

import de.suzufa.screwbox.core.Angle;
import de.suzufa.screwbox.core.Percent;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Flip;
import de.suzufa.screwbox.core.graphics.Font;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.core.graphics.WindowBounds;

public interface Renderer {

    void updateScreen(boolean antialiased);

    Sprite takeScreenshot();

    void fillWith(Color color);

    void fillRectangle(WindowBounds bounds, Color color);

    void fillCircle(Offset offset, int diameter, Color color);

    void drawSprite(Sprite sprite, Offset origin, double scale, Percent opacity, Angle rotation,
            Flip flip, WindowBounds clipArea);

    void drawSprite(Future<Sprite> sprite, Offset origin, double scale, Percent opacity, Angle rotation, Flip flip,
            WindowBounds clipArea);

    void drawText(Offset offset, String text, Font font, Color color);

    void drawLine(Offset from, Offset to, Color color);

    void drawTextCentered(Offset position, String text, Font font, Color color);

    void drawFadingCircle(Offset offset, int diameter, Color color);

    void drawCircle(Offset offset, int diameter, Color color);

}
