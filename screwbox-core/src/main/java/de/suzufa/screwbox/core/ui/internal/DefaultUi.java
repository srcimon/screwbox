package de.suzufa.screwbox.core.ui.internal;

import java.util.Optional;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.ui.KeyboardInteractor;
import de.suzufa.screwbox.core.ui.SimpleUiLayouter;
import de.suzufa.screwbox.core.ui.SimpleUiRenderer;
import de.suzufa.screwbox.core.ui.Ui;
import de.suzufa.screwbox.core.ui.UiInteractor;
import de.suzufa.screwbox.core.ui.UiLayouter;
import de.suzufa.screwbox.core.ui.UiMenu;
import de.suzufa.screwbox.core.ui.UiRenderer;

public class DefaultUi implements Ui {

    private final Engine engine;

    private UiRenderer renderer = new SimpleUiRenderer();
    private UiInteractor interactor = new KeyboardInteractor();
    private UiLayouter layouter = new SimpleUiLayouter();

    private Optional<UiMenu> currentMenu = Optional.empty();

    public DefaultUi(final Engine engine) {
        this.engine = engine;
    }

    @Override
    public Ui openMenu(final UiMenu menu) {
        currentMenu = Optional.of(menu);
        return this;
    }

    public void update() {
        if (currentMenu.isPresent()) {
            final var window = engine.graphics().window();
            final var menu = currentMenu.get();
            interactor.interactWith(menu, layouter, engine);
            renderer.render(menu, layouter, window);
        }
    }

    @Override
    public Ui setRenderer(final UiRenderer renderer) {
        this.renderer = renderer;
        return this;
    }

    @Override
    public Ui setInteractor(final UiInteractor interactor) {
        this.interactor = interactor;
        return this;
    }

    @Override
    public Ui closeMenu() {
        currentMenu = Optional.empty();
        return this;
    }

    @Override
    public Ui setLayouter(final UiLayouter layouter) {
        this.layouter = layouter;
        return this;
    }

}
