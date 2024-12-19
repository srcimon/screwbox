package io.github.srcimon.screwbox.core.ui;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.utils.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class UiMenu {

    private final List<UiMenuItem> items = new ArrayList<>();
    private int selectedItemIndex = 0;

    public final UiMenuItem addItem(final String label) {
        final UiMenuItem item = new UiMenuItem(label);
        items.add(item);
        return item;
    }

    public final UiMenuItem addItem(final Function<Engine, String> dynamicLabel) {
        final UiMenuItem item = new UiMenuItem(dynamicLabel);
        items.add(item);
        return item;
    }

    public final List<UiMenuItem> items() {
        Validate.notEmpty(items, "no menu item present");
        return new ArrayList<>(items);
    }

    public final boolean isSelectedItem(final UiMenuItem item) {
        return items().get(selectedItemIndex).equals(item);
    }

    public final UiMenuItem selectedItem() {
        return items().get(selectedItemIndex);
    }

    public final void nextItem(final Engine engine) {
        selectedItemIndex = fetchNextSelectableItem(engine);
    }

    public final void previousItem(final Engine engine) {
        selectedItemIndex = fetchPreviousSelectableItem(engine);
    }

    private int fetchNextSelectableItem(final Engine engine) {
        for (int index = selectedItemIndex + 1; index < items().size(); index++) {
            if (isActive(index, engine)) {
                return index;
            }
        }
        return selectedItemIndex;
    }

    public boolean isActive(final UiMenuItem item, final Engine engine) {
        return isActive(itemIndex(item), engine);
    }

    public void onExit(final Engine engine) {
        // does nothing
    }

    public final int itemCount() {
        return items.size();
    }

    public final int activeItemIndex() {
        return selectedItemIndex;
    }

    public final int itemIndex(final UiMenuItem menuItem) {
        int index = 0;
        for (final var item : items) {
            if (item.equals(menuItem)) {
                return index;
            }
            index++;
        }
        throw new IllegalArgumentException("menu doesn't contain specified menu item");
    }

    public final void selectItem(final UiMenuItem menuItem) {
        selectedItemIndex = itemIndex(menuItem);
    }

    private int fetchPreviousSelectableItem(final Engine engine) {
        for (int index = selectedItemIndex - 1; index >= 0; index--) {
            if (isActive(index, engine)) {
                return index;
            }
        }
        return selectedItemIndex;
    }

    private boolean isActive(final int index, final Engine engine) {
        return items.get(index).isActive(engine);
    }

}
