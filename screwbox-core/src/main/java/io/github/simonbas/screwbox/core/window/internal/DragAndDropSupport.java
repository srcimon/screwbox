package io.github.simonbas.screwbox.core.window.internal;

import io.github.simonbas.screwbox.core.graphics.Offset;
import io.github.simonbas.screwbox.core.window.WindowDropEvent;
import io.github.simonbas.screwbox.core.window.WindowDropListener;

import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TooManyListenersException;

public class DragAndDropSupport extends DropTargetAdapter {

    private final List<WindowDropListener> listeners = new ArrayList<>();

    public DragAndDropSupport(final Frame frame) {
        try {
            DropTarget dropTarget = new DropTarget();
            frame.setDropTarget(dropTarget);
            dropTarget.addDropTargetListener(this);
        } catch (TooManyListenersException e) {
            throw new IllegalStateException("Could not register DropTargetListener", e);
        }
    }

    public void addDropListener(final WindowDropListener listener) {
        listeners.add(listener);
    }

    public void removeDropListener(final WindowDropListener listener) {
        listeners.remove(listener);
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
                    WindowDropEvent dropEvent = new WindowDropEvent(this, position, files);
                    for (final var listener : listeners) {
                        listener.filesDroppedOnWindow(dropEvent);
                    }
                } catch (UnsupportedFlavorException | IOException e) {
                    throw new IllegalStateException("drop failed", e);
                }
            }
        }
    }
}
