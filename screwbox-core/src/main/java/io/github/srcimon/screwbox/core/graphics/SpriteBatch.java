package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.graphics.internal.SpriteBatchEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpriteBatch {

    private final List<SpriteBatchEntry> entries = new ArrayList<>();

    public void add(Sprite sprite, Offset offset, SpriteDrawOptions options, int drawOrder) {
        entries.add(new SpriteBatchEntry(sprite, offset, options, drawOrder));
    }

    public List<SpriteBatchEntry> entriesInOrder() {
        Collections.sort(entries);
        return entries;
    }
}
