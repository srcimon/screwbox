package de.suzufa.screwbox.core.ui;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import de.suzufa.screwbox.core.Engine;

public final class UiMenuItem {

    private final Function<Engine, String> label;
    private Predicate<Engine> activeCondition = engine -> true;
    private Consumer<Engine> onActivate = engine -> {
    };

    UiMenuItem(final String label) {
        this.label = engine -> label;
    }

    UiMenuItem(final Function<Engine, String> dynamicLabel) {
        this.label = dynamicLabel;
    }

    public String label(final Engine engine) {
        return label.apply(engine);
    }

    public UiMenuItem onActivate(final Consumer<Engine> onActivate) {
        this.onActivate = onActivate;
        return this;
    }

    public void trigger(final Engine engine) {
        onActivate.accept(engine);
    }

    public UiMenuItem activeCondition(final Predicate<Engine> condition) {
        activeCondition = condition;
        return this;
    }

    public boolean isActive(final Engine engine) {
        return activeCondition.test(engine);
    }
}
