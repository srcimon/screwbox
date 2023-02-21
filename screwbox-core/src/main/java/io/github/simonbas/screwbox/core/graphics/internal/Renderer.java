package io.github.simonbas.screwbox.core.graphics.internal;

import io.github.simonbas.screwbox.core.Angle;
import io.github.simonbas.screwbox.core.Percent;
import io.github.simonbas.screwbox.core.assets.Asset;
import io.github.simonbas.screwbox.core.graphics.*;

public interface Renderer {

    void updateScreen(boolean antialiased);

    Sprite takeScreenshot();

    void fillWith(Color color);

    void fillRectangle(WindowBounds bounds, Color color);

    void fillCircle(Offset offset, int diameter, Color color);

    void drawSprite(Sprite sprite, Offset origin, double scale, Percent opacity, Angle rotation,
            Flip flip, WindowBounds clipArea);

    void drawSprite(Asset<Sprite> sprite, Offset origin, double scale, Percent opacity, Angle rotation, Flip flip,
            WindowBounds clipArea);

    void drawText(Offset offset, String text, Font font, Color color);

    void drawLine(Offset from, Offset to, Color color);

    void drawTextCentered(Offset position, String text, Font font, Color color);

    void drawFadingCircle(Offset offset, int diameter, Color color);

    void drawCircle(Offset offset, int diameter, Color color);

}
