package dev.screwbox.core.window;

import dev.screwbox.core.graphics.Offset;

import java.io.File;
import java.util.List;

/**
 * List of all {@link File} files dropped on the {@link Window}.
 *
 * @param files  list of {@link File files}
 * @param offset the window offset of the drop
 */
public record FilesDroppedOnWindow(List<File> files, Offset offset) {
}
