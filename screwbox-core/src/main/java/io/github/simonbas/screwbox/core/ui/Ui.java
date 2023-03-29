package io.github.simonbas.screwbox.core.ui;

import io.github.simonbas.screwbox.core.graphics.Screen;

import java.util.Optional;
import java.util.function.Consumer;

public interface Ui {

    Ui openMenu(UiMenu menu);

    Ui setRenderer(UiRenderer renderer);

    Ui setInteractor(UiInteractor interactor);

    Ui setLayouter(UiLayouter layouter);

    Ui closeMenu();

    Optional<UiMenu> currentMenu();

    /**
     * Starts showing the {@link #loadingAnimation()} until  {@link #hideLoadingAnimation()} is called.
     * The {@link #loadingAnimation()} can be customized via {@link #customizeLoadingAnimation(Consumer)}.
     */
    Ui showLoadingAnimation();

    /**
     * Stops showing the {@link #loadingAnimation()}.
     *
     * @see #showLoadingAnimation()
     */
    Ui hideLoadingAnimation();

    /**
     * Customizes the visual style of the {@link #loadingAnimation()}.
     *
     * @see #showLoadingAnimation()
     * @see #loadingAnimation()
     */
    Ui customizeLoadingAnimation(Consumer<Screen> loadingAnimation);

    /**
     * Returns the current loading animation.
     */
    Consumer<Screen> loadingAnimation();
}
