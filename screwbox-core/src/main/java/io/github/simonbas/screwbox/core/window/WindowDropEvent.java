package io.github.simonbas.screwbox.core.window;

import io.github.simonbas.screwbox.core.graphics.Offset;

import java.nio.file.Path;
import java.util.EventObject;
import java.util.List;

/**
 * Occurs when one or multiple files are dropped on a {@link Window}.
 */
public class WindowDropEvent extends EventObject {

    private final Offset position;
    private final List<Path> filePaths;

    public WindowDropEvent(final Object source, final Offset position, final List<Path> filePaths) {
        super(source);
        this.position = position;
        this.filePaths = filePaths;
    }

    /**
     * Position of drop on the {@link Window}.
     */
    public Offset position() {
        return position;
    }

    /**
     * List of {@link Path}s of the files dropped on the {@link Window}. Is never empty.
     */
    public List<Path> filePaths() {
        return filePaths;
    }
}
