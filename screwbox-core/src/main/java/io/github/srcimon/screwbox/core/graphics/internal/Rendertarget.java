package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Sizeable;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.SpriteBatch;
import io.github.srcimon.screwbox.core.graphics.drawoptions.CircleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.LineDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.RectangleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteFillOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SystemTextDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.TextDrawOptions;

import java.util.function.Supplier;

//TODO apply offset change here
public class Rendertarget implements Sizeable {//TODO implement renderer?
    //TODO feature = reduce screen size within window
    private final Renderer renderer;
    private ScreenBounds clip = new ScreenBounds(0, 0, 0, 0);

    public Rendertarget(final Renderer renderer) {
        this.renderer = renderer;
    }

    public void updateClip(final ScreenBounds clip) {
        this.clip = clip;
    }

    public void fillWith(final Color color) {
        renderer.fillWith(color, clip);
    }

    public void fillWith(final Sprite sprite, final SpriteFillOptions options) {
        renderer.fillWith(sprite, options.offset(options.offset().add(clip.offset())), clip);
    }

    public void drawText(final Offset offset, final String text, final SystemTextDrawOptions options) {
        renderer.drawText(offset.add(clip.offset()), text, options, clip);
    }

    public void drawRectangle(final Offset offset, final Size size, final RectangleDrawOptions options) {
        renderer.drawRectangle(offset.add(clip.offset()), size, options, clip);
    }

    public void drawLine(final Offset from, final Offset to, final LineDrawOptions options) {
        renderer.drawLine(from.add(clip.offset()), to.add(clip.offset()), options, clip);
    }

    public void drawCircle(final Offset offset, final int radius, final CircleDrawOptions options) {
        renderer.drawCircle(offset.add(clip.offset()), radius, options, clip);
    }

    public void drawSprite(final Supplier<Sprite> sprite, final Offset origin, final SpriteDrawOptions options) {
        renderer.drawSprite(sprite, origin.add(clip.offset()), options, clip);
    }

    public void drawSprite(final Sprite sprite, final Offset origin, final SpriteDrawOptions options) {
        renderer.drawSprite(sprite, origin.add(clip.offset()), options, clip);
    }

    public void drawText(final Offset offset, final String text, final TextDrawOptions options) {
        renderer.drawText(offset.add(clip.offset()), text, options, clip);
    }

    public void drawSpriteBatch(final SpriteBatch spriteBatch) {
        if (clip.offset().equals(Offset.origin())) {
            renderer.drawSpriteBatch(spriteBatch, clip);
        } else {
            //TODO refactor into spritebatch.translate()
            SpriteBatch translatedSpriteBatch = new SpriteBatch();
            for(var entry : spriteBatch.entries()) {
                translatedSpriteBatch.add(entry.sprite(), entry.offset().add(clip.offset()), entry.options(), entry.drawOrder());
            }
            renderer.drawSpriteBatch(spriteBatch, clip);
        }
    }

    @Override
    public Size size() {
        return clip.size();
    }
}
