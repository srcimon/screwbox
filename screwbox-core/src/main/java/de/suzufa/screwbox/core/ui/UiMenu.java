package de.suzufa.screwbox.core.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import de.suzufa.screwbox.core.Engine;

public class UiMenu {

    private final List<UiMenuItem> items = new ArrayList<>();
    private int selectedItemIndex = 0;

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
        return items().get(selectedItemIndex).equals(item);
    }

    public final UiMenuItem selectedItem() {
        return items().get(selectedItemIndex);
    }

    public final void nextItem(Engine engine) {
        if (selectedItemIndex < items().size() - 1) {
            selectedItemIndex++;
        }
        while (isInactive(selectedItemIndex, engine)) {
            nextItem(engine);
        }
        // TODO: endless loop bug
        // TODO: add Test
    }

    public final void previousItem(Engine engine) {
        if (selectedItemIndex > 0) {
            selectedItemIndex--;
        }

        while (isInactive(selectedItemIndex, engine)) {
            previousItem(engine);
        }
        // TODO: endless loop bug
        // TODO: add Test
    }

    public boolean isActive(UiMenuItem item, Engine engine) {
        return !isInactive(itemIndex(item), engine);
    }

    boolean isInactive(int index, Engine engine) {
        List<Function<Engine, Boolean>> activeConditions = items.get(index).activeConditions();
        for (var condition : activeConditions) {
            if (!condition.apply(engine)) {
                return true;
            }
        }
        return false;
    }

    public void onExit(Engine engine) {
        // does nothing
    }

    public final int itemCount() {
        return items.size();
    }

    public final int activeItemIndex() {
        return selectedItemIndex;
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
        selectedItemIndex = itemIndex(menuItem);
    }

}
