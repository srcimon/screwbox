package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.graphics.drawoptions.CircleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.LineDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.RectangleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteFillOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SystemTextDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.TextDrawOptions;

import java.util.function.Supplier;

public interface Canvas extends Sizeable {

    /**
     * Returns the left upper edge of the {@link Canvas}.
     */
    Offset offset();

    /**
     * Returns the center of the {@link Canvas}.
     */
    Offset center();

    ScreenBounds bounds();

    Canvas fillWith(Color color);

    Canvas fillWith(Sprite sprite, SpriteFillOptions options);

    Canvas drawText(Offset offset, String text, SystemTextDrawOptions options);

    Canvas drawRectangle(Offset offset, Size size, RectangleDrawOptions options);

    Canvas drawLine(Offset from, Offset to, LineDrawOptions options);

    Canvas drawCircle(Offset offset, int radius, CircleDrawOptions options);

    Canvas drawSprite(Supplier<Sprite> sprite, Offset origin, SpriteDrawOptions options);

    Canvas drawSprite(Sprite sprite, Offset origin, SpriteDrawOptions options);

    Canvas drawText(Offset offset, String text, TextDrawOptions options);

    Canvas drawSpriteBatch(SpriteBatch spriteBatch);
}
