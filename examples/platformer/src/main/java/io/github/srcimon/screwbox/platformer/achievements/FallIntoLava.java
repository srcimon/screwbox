package io.github.srcimon.screwbox.platformer.achievements;

import dev.screwbox.core.achievements.AchievementDefinition;
import dev.screwbox.core.achievements.AchievementDetails;

public class FallIntoLava implements AchievementDefinition {

    @Override
    public AchievementDetails details() {
        return AchievementDetails.title("fall into lava");
    }
}
