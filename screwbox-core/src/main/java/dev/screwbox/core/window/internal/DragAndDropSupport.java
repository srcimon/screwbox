package dev.screwbox.core.window.internal;

import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.window.DroppedFiles;

import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.TooManyListenersException;
import java.util.function.Consumer;

public class DragAndDropSupport extends DropTargetAdapter {

    private final Consumer<DroppedFiles> onFilesDroppedOnWindow;

    public DragAndDropSupport(final Frame frame, final Consumer<DroppedFiles> onFilesDroppedOnWindow) {
        try {
            final var dropTarget = new DropTarget();
            frame.setDropTarget(dropTarget);
            dropTarget.addDropTargetListener(this);
        } catch (final TooManyListenersException e) {
            throw new IllegalStateException("could not register DropTargetListener", e);
        }
        this.onFilesDroppedOnWindow = onFilesDroppedOnWindow;
    }

    @Override
    public void drop(final DropTargetDropEvent event) {
        event.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
        final var transferable = event.getTransferable();
        for (final var dataFlavor : transferable.getTransferDataFlavors()) {
            if (dataFlavor.isFlavorJavaFileListType()) {
                try {
                    final Offset position = Offset.at(event.getLocation().x, event.getLocation().y);
                    final List<File> files = (List<File>) transferable.getTransferData(dataFlavor);
                    onFilesDroppedOnWindow.accept(new DroppedFiles(files, position));
                } catch (final UnsupportedFlavorException | IOException e) {
                    throw new IllegalStateException("drop failed", e);
                }
            }
        }
    }
}
