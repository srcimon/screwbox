package io.github.simonbas.screwbox.core.window;

import io.github.simonbas.screwbox.core.graphics.Offset;

import java.io.File;
import java.util.EventObject;
import java.util.List;

/**
 * Occurs when one or multiple files are dropped on a {@link Window}.
 */
public class WindowDropEvent extends EventObject {

    private final Offset position;
    private final List<File> files;

    public WindowDropEvent(final Object source, final Offset position, final List<File> files) {
        super(source);
        this.position = position;
        this.files = files;
    }

    /**
     * Position of drop on the {@link Window}.
     */
    public Offset position() {
        return position;
    }

    /**
     * List of {@link File}s dropped on the {@link Window}. Is never empty.
     */
    public List<File> files() {
        return files;
    }
}
