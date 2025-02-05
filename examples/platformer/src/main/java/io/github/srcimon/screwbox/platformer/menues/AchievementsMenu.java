package io.github.srcimon.screwbox.platformer.menues;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.achievements.Achievement;
import io.github.srcimon.screwbox.core.ui.UiMenu;
import io.github.srcimon.screwbox.core.ui.WobblyUiLayouter;

import java.util.List;

public class AchievementsMenu extends UiMenu {

    public AchievementsMenu(List<Achievement> achievements) {

        for (final var achievement : achievements) {
            addItem(engine -> achievement.isCompleted()
                    ? "%s - completed".formatted(achievement.title())
                    : "%s - %s of %s".formatted(achievement.title(), achievement.score(), achievement.goal()));
        }

        addItem("reset achievements").onActivate(engine -> engine.achievements().reset());
        addItem("back").onActivate(this::onExit);
    }

    @Override
    public void onExit(Engine engine) {
        engine.ui()
                .setLayouter(new WobblyUiLayouter())
                .openPreviousMenu();
    }
}
