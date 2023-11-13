package io.github.srcimon.screwbox.core.window.internal;

import io.github.srcimon.screwbox.core.graphics.Offset;

import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.TooManyListenersException;
import java.util.function.BiConsumer;

public class DragAndDropSupport extends DropTargetAdapter {

    private final BiConsumer<List<File>, Offset> onFilesDroppedOnWindow;

    public DragAndDropSupport(final Frame frame, final BiConsumer<List<File>, Offset> onFilesDroppedOnWindow) {
        try {
            DropTarget dropTarget = new DropTarget();
            frame.setDropTarget(dropTarget);
            dropTarget.addDropTargetListener(this);
        } catch (TooManyListenersException e) {
            throw new IllegalStateException("Could not register DropTargetListener", e);
        }
        this.onFilesDroppedOnWindow = onFilesDroppedOnWindow;
    }

    @Override
    public void drop(final DropTargetDropEvent event) {
        event.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
        Transferable transferable = event.getTransferable();
        for (final var dataFlavor : transferable.getTransferDataFlavors()) {
            if (dataFlavor.isFlavorJavaFileListType()) {
                try {
                    final Offset position = Offset.at(event.getLocation().x, event.getLocation().y);
                    List<File> files = (List<File>) transferable.getTransferData(dataFlavor);
                    onFilesDroppedOnWindow.accept(files, position);
                } catch (UnsupportedFlavorException | IOException e) {
                    throw new IllegalStateException("drop failed", e);
                }
            }
        }
    }
}
