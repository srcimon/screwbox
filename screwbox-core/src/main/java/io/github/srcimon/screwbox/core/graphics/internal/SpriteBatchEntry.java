package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.SpriteDrawOptions;

public record SpriteBatchEntry(Sprite sprite, Offset offset, SpriteDrawOptions options, int drawOrder)
        implements Comparable<SpriteBatchEntry> {

    @Override
    public int compareTo(final SpriteBatchEntry other) {
        return Integer.compare(drawOrder, other.drawOrder);
    }
}