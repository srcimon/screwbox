package io.github.srcimon.screwbox.platformer.menues;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.archivements.Archivement;
import io.github.srcimon.screwbox.core.ui.UiMenu;
import io.github.srcimon.screwbox.core.ui.WobblyUiLayouter;

import java.util.List;

public class ArchivmentsMenu extends UiMenu {

    public ArchivmentsMenu(List<Archivement> archivements) {

        for (final var archivement : archivements) {
            addItem(archivement.isCompleted()
                    ? "%s - completed".formatted(archivement.title())
                    : "%s - %s of %s".formatted(archivement.title(), archivement.score(), archivement.goal()))
                    .activeCondition(engine -> !archivement.isCompleted());
        }
        //TODO add reset
        addItem("back").onActivate(this::onExit);
    }

    @Override
    public void onExit(Engine engine) {
        engine.ui()
                .setLayouter(new WobblyUiLayouter())
                .openPreviousMenu();
    }
}
