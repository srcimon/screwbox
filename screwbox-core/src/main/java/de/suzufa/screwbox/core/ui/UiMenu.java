package de.suzufa.screwbox.core.ui;

import java.util.ArrayList;
import java.util.List;

import de.suzufa.screwbox.core.Engine;

public class UiMenu {

    private final List<UiMenuItem> items = new ArrayList<>();
    private int activeItemIndex = 0;

    public final void add(UiMenuItem item) {
        items.add(item);
    }

    public final List<UiMenuItem> items() {
        if (itemCount() == 0) {
            throw new IllegalStateException("no menu item present");
        }
        return items;
    }

    public final boolean isSelectedItem(UiMenuItem item) {
        return items().get(activeItemIndex).equals(item);
    }

    public final UiMenuItem selectedItem() {
        return items().get(activeItemIndex);
    }

    public final void nextItem() {
        if (activeItemIndex < items().size() - 1) {
            activeItemIndex++;
        }

    }

    public final void previousItem() {
        if (activeItemIndex > 0) {
            activeItemIndex--;
        }
    }

    public void onExit(Engine engine) {
        // does nothing
    }

    public final int itemCount() {
        return items.size();
    }

    public final int activeItemIndex() {
        return activeItemIndex;
    }

    public final int itemIndex(UiMenuItem menuItem) {
        int index = 0;
        for (var item : items) {
            if (item.equals(menuItem)) {
                return index;
            }
            index++;
        }
        throw new IllegalArgumentException("Menu doesn't contain specified MenuItem.");
    }

    public final void selectItem(UiMenuItem menuItem) {
        activeItemIndex = itemIndex(menuItem);
    }

}
